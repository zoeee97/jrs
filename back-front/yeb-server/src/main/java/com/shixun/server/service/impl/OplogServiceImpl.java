package com.shixun.server.service.impl;

import com.shixun.server.pojo.Oplog;
import com.shixun.server.mapper.OplogMapper;
import com.shixun.server.service.OplogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zyy
 * @since 2022-07-06
 */
@Service
public class OplogServiceImpl extends ServiceImpl<OplogMapper, Oplog> implements OplogService {

}
