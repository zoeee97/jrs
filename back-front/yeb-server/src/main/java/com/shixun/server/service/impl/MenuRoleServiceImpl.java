package com.shixun.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shixun.server.pojo.MenuRole;
import com.shixun.server.mapper.MenuRoleMapper;
import com.shixun.server.service.MenuRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shixun.server.utils.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zyy
 * @since 2022-07-06
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements MenuRoleService {

    @Autowired
    private MenuRoleMapper menuRoleMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 更新角色菜单
     * @param rid
     * @param mids
     * @return
     */
    @Override
    public RespBean updateMenuRole(Integer rid, Integer[] mids) {
        menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq("rid",rid));
        //清除所有的菜单缓存
        Set menus = redisTemplate.keys("menus");
        redisTemplate.delete(menus);
        if (mids == null || mids.length==0){
            return RespBean.success("更新成功");
        }
        Integer result = menuRoleMapper.insertRecord(rid, mids);
        if (mids.length == result){
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }
}
