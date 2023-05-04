package com.shixun.server.config.security;

import com.shixun.server.config.filter.CustomFilter;
import com.shixun.server.config.filter.CustomUrlDecisionManager;
import com.shixun.server.pojo.Admin;
import com.shixun.server.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security 配置类
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RestAuthorizationEntryPoint restAuthorizationEntryPoint; // 未登录 token 失效时自定义处理结果

    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler; // 无权访问时自定义处理结果

    @Autowired
    private CustomUrlDecisionManager customUrlDecisionManager;

    @Autowired
    private CustomFilter customFilter;

    // 1、重写 UserDetailsService，用我们自己写的业务逻辑
    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Admin admin = adminService.getAdminByUserName(username);
            if (null != admin) {
                admin.setRoles(adminService.getRolesByAdminId(admin.getId()));
                return admin;
            }
            /*springSecurity提供的异常*/
            throw new UsernameNotFoundException("用户名或密码不正确");
        };
    }

    // 2、让 Security 走我们重写的 UserDetailsService ，通过 getAdminByUserName 获取用户名
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    // 3、密码加解密对象
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 6、放行路径（不走拦截链）
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
//                "/websocket/**",
//                "/**.html",
//                "/login/**",
//                "//hello/**",
//                "/register/**",
//                "/logout/**",
//                "/css/**",
//                "/js/**",
//                "/img/**",
//                "/fonts/**",
//                "favicon.ico",
//                "/doc.html",                    // 放行 swagger 资源
//                "/webjars/**",                  // 放行 swagger 资源
//                "/swagger-resources/**",        // 放行 swagger 资源
//                "/v2/api-docs/**",              // 放行 swagger 资源
//                "/ws/**"
                "/login",
                "/logout",
                "/ws/**",
                "/css/**",
                "/js/**",
                "/index.html",
                "favicon.ico",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources/**",
                "/v2/api-docs/**",
                "/register/**",
                "/img/**",
                "/fonts/**",
                "favicon.ico",
                "/system/cfg/menu"
        );
    }

    /**
     * 4、SpringSecurity 配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 使用 JWT , 不需要 csrf
        http.csrf()
                .disable()
                // 基于 token,不需要 session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 所有请求都要求认证
                .anyRequest()
                .authenticated()
                //动态权限配置
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setAccessDecisionManager(customUrlDecisionManager);
                        o.setSecurityMetadataSource(customFilter);
                        return o;
                    }
                })
                .and()
                // 禁用缓存
                .headers()
                .cacheControl();

        // 添加 jwt 登录授权过滤器
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // 添加自定义未授权和未登录结果返回
        http.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthorizationEntryPoint);

    }

    // 5、JWT 登录授权过滤器
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

}
