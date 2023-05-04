package com.shixun.server.utils;

import com.shixun.server.pojo.Admin;
import org.springframework.security.core.context.SecurityContextHolder;

/** 操作员工具类 - 获取当前登录用户
 */
public class AdminUtils {

    public static Admin getCurrentAdmin(){
        return (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
