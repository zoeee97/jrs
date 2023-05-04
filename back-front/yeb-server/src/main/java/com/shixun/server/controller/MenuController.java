package com.shixun.server.controller;


import com.shixun.server.pojo.Menu;
import com.shixun.server.service.MenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zyy
 * @since 2022-07-06
 */
@RestController
@RequestMapping("/system/cfg")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /*没有传用户id进来，是因为到这里用户已经登陆了，用户信息一般是直接从后端取，而非让前端传进来，因为有篡改的可能
    * 因此可以利用spring security的全局对象取到用户信息*/
    @ApiOperation(value = "通过用户ID查询菜单列表")
    @GetMapping("/menu")
    // 只要登录，用户信息存在 security 全局对象中，从全局对象中获取用户id
    public List<Menu> getMenusByAdminId(){
        System.out.println("aaa" + menuService.getMenusByAdminId());
        return menuService.getMenusByAdminId();
    }

}