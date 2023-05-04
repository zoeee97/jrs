package com.shixun.server.service;

import com.shixun.server.pojo.MenuRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shixun.server.utils.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyy
 * @since 2022-07-06
 */
public interface MenuRoleService extends IService<MenuRole> {

    /**
     * 更新角色菜单
     * @param rid
     * @param mids
     * @return
     */
    RespBean updateMenuRole(Integer rid, Integer[] mids);
}
