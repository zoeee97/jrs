package com.shixun.server.service;

import com.shixun.server.pojo.Department;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shixun.server.utils.RespBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyy
 * @since 2022-07-06
 */
public interface DepartmentService extends IService<Department> {

    /**
     * 获取所有部门
     * @return
     */
    List<Department> getAllDepartments();

    /**
     * 添加部门
     * @param dep
     * @return
     */
    RespBean addDep(Department dep);

    /**
     * 删除部门
     * @param id
     * @return
     */
    RespBean deleteDep(Integer id);
}
