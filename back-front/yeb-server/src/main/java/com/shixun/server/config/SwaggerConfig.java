package com.shixun.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger2 配置类
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createAPI() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //指定哪个包下面生成接口文档
                .apis(RequestHandlerSelectors.basePackage("com.shixun.server.controller"))
                .paths(PathSelectors.any())
                .build()
                .securityContexts(securityContexts())
                .securitySchemes(securitySchemes()); // 配置请求头信息
    }

    /**
     * 文档基本信息 api info
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .version("1.0")
                .title("简人事接口文档")
                .description("简人事接口文档")
                .contact(new Contact("zyy", "localhost:8081/doc.html", "1741540959@qq.com"))
                .build();

    }

    // 1. 解决访问接口登录问题
    private List<ApiKey> securitySchemes() {
        // 设置请求头信息
        List<ApiKey> result = new ArrayList<>();
        // 参数：api key 名字 { 准备的 key 名字，value 请求头 }
        result.add(new ApiKey("Authorization", "Authorization", "header"));
        return result;
    }

    // 2. 解决访问接口登录问题
    private List<SecurityContext> securityContexts() {
        // 设置需要登录认证的路径
        List<SecurityContext> result = new ArrayList<>();
        result.add(getContextByPath("/hello/.*"));
        return result;
    }

    // 3. 解决访问接口登录问题
    private SecurityContext getContextByPath(String pathRegex) {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
//                .forPaths(PathSelectors.regex("/hello/.*"))
//                .forPaths(PathSelectors.regex("/register/.*"))
                .forPaths(PathSelectors.regex(pathRegex))
                .build();
    }

    // 4. 设置默认授权 - 解决访问接口登录问题
    private List<SecurityReference> defaultAuth() {
        List<SecurityReference> result = new ArrayList<>();
        /*授权范围*/
        AuthorizationScope authorizationScope = new AuthorizationScope("global","accessEverything");
        AuthorizationScope[] scopes = new AuthorizationScope[1];
        scopes[0] = authorizationScope;
        result.add(new SecurityReference("Authorization", scopes));
        return result;
    }

}
