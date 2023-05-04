package com.shixun.server.service.impl;

import com.shixun.server.pojo.Admin;
import com.shixun.server.pojo.Menu;
import com.shixun.server.mapper.MenuMapper;
import com.shixun.server.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shixun.server.utils.AdminUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zyy
 * @since 2022-07-06
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 通过用户ID查询菜单列表
     *
     * @return
     */
    @Override
    public List<Menu> getMenusByAdminId() {
        // Integer adminId = ((Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        // 上面写法使用方法抽取工具类
        Integer adminId = AdminUtils.getCurrentAdmin().getId();
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        //从 redis 获取菜单数据 查看缓存中有没有数据
        List<Menu> menus = (List<Menu>) valueOperations.get("menus" + adminId);
        if (CollectionUtils.isEmpty(menus)) {
            //如果没有数据,数据库查询,并设置到缓存
            menus = menuMapper.getMenusByAdminId(adminId);
            valueOperations.set("menu_" + adminId, menus);
        }
        return menus;
    }

    /**
     * 根据角色查询菜单列表
     * @return
     */
    @Override
    public List<Menu> getMenusWithRole() {
        return menuMapper.getMenusWithRole();
    }

    @Override
    public List<Menu> getAllMenus() {
        return menuMapper.getAllMenus();
    }
}
