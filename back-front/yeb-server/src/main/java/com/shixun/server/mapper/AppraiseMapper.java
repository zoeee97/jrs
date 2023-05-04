package com.shixun.server.mapper;

import com.shixun.server.pojo.Appraise;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface AppraiseMapper extends BaseMapper<Appraise> {

}
