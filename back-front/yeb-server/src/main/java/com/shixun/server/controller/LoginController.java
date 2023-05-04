package com.shixun.server.controller;

import com.shixun.server.pojo.Admin;
import com.shixun.server.pojo.AdminLoginParam;
import com.shixun.server.service.AdminService;
import com.shixun.server.utils.RespBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * 登录
 */
@Api(tags = "LoginController")
@RestController
public class LoginController {

    @Autowired
    private AdminService adminService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @ApiOperation(value = "登录之后返回token")
    @PostMapping("/login")
    public RespBean login(@RequestBody AdminLoginParam adminLoginParam, HttpServletRequest request) {
        return adminService.login(adminLoginParam.getUsername(), adminLoginParam.getPassword(), request);
    }


    @ApiOperation(value = "获取当前登录用户的信息")
    @GetMapping("/admin/info")
    public Admin getAdminInfo(Principal principal) { // 通过 Principal 对象，获取当前登录用户对象
        if (null == principal) {
            return null;
        }
        String username = principal.getName();
        Admin admin = adminService.getAdminByUserName(username); // 新建方法 getAdminByUserName 根据用户名获取用户
        admin.setPassword(null); // 安全起见，不给前端返回用户密码
        admin.setRoles(adminService.getRolesByAdminId(admin.getId())); // 获取登录用户对应的角色列表
        return admin;
    }


    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public RespBean logout() {
        return RespBean.success("退出成功！");
    }


    // 注册
    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    public RespBean registerUser(@RequestBody String username, String password, String phone) {

        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPhone(phone);
        admin.setPassword(passwordEncoder.encode(password));
        adminService.save(admin);
        return RespBean.success("注册成功", admin);
    }


}
