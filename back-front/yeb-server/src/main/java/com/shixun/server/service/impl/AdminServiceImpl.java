package com.shixun.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shixun.server.config.security.JwtTokenUtil;
import com.shixun.server.mapper.AdminRoleMapper;
import com.shixun.server.mapper.RoleMapper;
import com.shixun.server.pojo.Admin;
import com.shixun.server.mapper.AdminMapper;
import com.shixun.server.pojo.AdminRole;
import com.shixun.server.pojo.Role;
import com.shixun.server.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shixun.server.utils.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zyy
 * @since 2022-07-06
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    private UserDetailsService userDetailsService; // 权限框架的

    @Autowired
    private PasswordEncoder passwordEncoder; // 安全框架-密码加密解密

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.tokenHead}")
    private String tokenHead; // token 头部信息

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminRoleMapper adminRoleMapper;
    /**
     * 登录之后返回 token
     */
    @Override
    public RespBean login(String username, String password, HttpServletRequest request) {
        // 登录
        UserDetails userDetails = userDetailsService.loadUserByUsername(username); // 调用权限框架方法获取用户名
        // 参数：第一个前端传过来的明文密码，第二个从 userDetails 中获取的
        if (null == userDetails || !passwordEncoder.matches(password, userDetails.getPassword())) {
            return RespBean.error("用户名或密码不正确");
        }
        if (!userDetails.isEnabled()) {
            return RespBean.error("账号被禁用，请联系管理员！");
        }

        // 更新 security 登录用户对象，设置到全局
        /**/
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails
                , null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 登录成功，生成 token
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("tokenHead",tokenHead);
        tokenMap.put("token",token);
        return RespBean.success("登录成功", tokenMap);
    }

    /**
     * 根据用户名获取用户
     *
     * @param username
     * @return
     */
    @Override
    public Admin getAdminByUserName(String username) {
        // 要作空判断，这里为了简单直接返回
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", username));
    }

    /**
     * 根据用户ID查询角色
     * @param adminId
     * @return
     */
    @Override
    public List<Role> getRolesByAdminId(Integer adminId) {
        return roleMapper.getRolesByAdminId(adminId);
    }

    /**
     * 获取所有操作员
     * @param keywords
     * @return
     */
    @Override
    public List<Admin> getAllAdmins(String keywords) {
        Integer id = ((Admin)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return adminMapper.getAllAdmins(id,keywords);
    }

    /**
     * 更新操作员角色
     * @param adminId
     * @param rids
     * @return
     */
    @Override
    @Transactional
    public RespBean updateAdminRole(Integer adminId, Integer[] rids) {
        adminRoleMapper.delete(new QueryWrapper<AdminRole>().eq("adminId",adminId));
        Integer result = adminRoleMapper.addRole(adminId, rids);
        if (rids.length == result){
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }

    /**
     * 更新用户密码
     * @param oldPass
     * @param pass
     * @param adminId
     * @return
     */
    @Override
    public RespBean updatePassword(String oldPass, String pass, Integer adminId) {
        Admin admin = adminMapper.selectById(adminId);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(oldPass,admin.getPassword())){
            admin.setPassword(encoder.encode(pass));
            int i = adminMapper.updateById(admin);
            if (i == 1){
                return RespBean.success("更新成功");
            }
        }
        return RespBean.error("更新失败");
    }
}
