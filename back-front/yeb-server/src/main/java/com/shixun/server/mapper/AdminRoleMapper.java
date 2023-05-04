package com.shixun.server.mapper;

import com.shixun.server.pojo.AdminRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zyy
 * @since 2022-07-06
 */
@Repository
@Mapper
public interface AdminRoleMapper extends BaseMapper<AdminRole> {

    /**
     * 添加操作员角色
     * @param adminId
     * @param rids
     * @return
     */
    Integer addRole(@Param("adminId") Integer adminId, @Param("rids") Integer[] rids);
}
