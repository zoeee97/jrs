package com.shixun.server.service;

import com.shixun.server.pojo.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyy
 * @since 2022-07-06
 */
public interface MenuService extends IService<Menu> {

    //通过用户ID查询菜单列表
    List<Menu> getMenusByAdminId();

    //根据角色查询菜单
    List<Menu> getMenusWithRole();

    //获取所有菜单
    List<Menu> getAllMenus();
}
