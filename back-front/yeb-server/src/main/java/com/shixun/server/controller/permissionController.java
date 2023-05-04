package com.shixun.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shixun.server.pojo.Menu;
import com.shixun.server.pojo.MenuRole;
import com.shixun.server.utils.RespBean;
import com.shixun.server.pojo.Role;
import com.shixun.server.service.MenuRoleService;
import com.shixun.server.service.MenuService;
import com.shixun.server.service.RoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限组
 *
 * @author zhanglishen
 */

@RestController
@RequestMapping("/system/basic/permission")
public class permissionController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuRoleService menuRoleService;

    @ApiOperation(value = "查询所有角色")
    @GetMapping("/")
    public List<Role> getAllRoles(){
        return roleService.list();
    }

    @ApiOperation(value = "添加角色")
    @PostMapping("/")
    public RespBean addRole(@RequestBody Role role){
        /*SpringSecurity权限命名ROLE_*/
        if (!role.getName().startsWith("ROLE_")){
            role.setName("ROLE_"+role.getName());
        }
        if (roleService.save(role)){
            return RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }

    @ApiOperation(value = "删除角色")
    @DeleteMapping("/role/{rid}")
    public RespBean deleteRole(@PathVariable Integer rid){
        if (roleService.removeById(rid)){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @ApiOperation(value = "获取所有菜单")
    @GetMapping("/menus")
    private List<Menu> getAllMenus(){
        return menuService.getAllMenus();
    }

    @ApiOperation(value = "根据角色ID查询菜单ID")
    @GetMapping("/mid/{rid}")
    private List<Integer> getMidByRid(@PathVariable Integer rid){
        return menuRoleService.list(new QueryWrapper<MenuRole>().eq("rid",rid)).stream()
                .map(MenuRole::getMid).collect(Collectors.toList());
    }

    @ApiOperation(value = "更新角色菜单")
    @PutMapping("/")
    /*因为进行的操作可能同时存在添加喝删除，因此此处的处理是：
        当传进角色id时，先清空它所有的菜单权限，再将传进来的菜单id和这个角色id相关联*/
    public RespBean updateMenuRole(Integer rid,Integer[] mids){
        return menuRoleService.updateMenuRole(rid,mids);
    }

}
