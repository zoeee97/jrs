package com.shixun.server.controller;

import com.shixun.server.pojo.EmployeeEc;
import com.shixun.server.service.EmployeeEcService;
import com.shixun.server.utils.RespBean;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zyy
 * @since 2022-07-06
 */
@RestController
@RequestMapping("/personnel/ec")
public class EmployeeEcController {

    @Autowired
    private EmployeeEcService employeeEcService;

    @ApiOperation(value = "获取奖罚信息")
    @GetMapping("/")
    private List<EmployeeEc> getAllPosition(){
        return employeeEcService.list();
    }

    @ApiOperation(value = "添加奖罚信息")
    @PostMapping("/")
    private RespBean addPosition(@RequestBody EmployeeEc employeeEc){
        employeeEc.setEcDate(LocalDate.now());
        if (employeeEcService.save(employeeEc)){
            return RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }

    @ApiOperation(value = "修改奖罚信息")
    @PutMapping("/")
    private RespBean updatePosition(@RequestBody EmployeeEc employeeEc){
        if (employeeEcService.updateById(employeeEc)){
            return RespBean.success("修改成功");
        }
        return RespBean.error("修改失败");
    }

    @ApiOperation(value = "删除奖罚信息")
    @DeleteMapping("/{id}")
    private RespBean deletePosition(@PathVariable Integer id){
        if (employeeEcService.removeById(id)){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @ApiOperation(value = "批量删除奖罚信息")
    @DeleteMapping("/")
    public RespBean deletePositionByIds(Integer[] ids){
        if (employeeEcService.removeByIds(Arrays.asList(ids))){
            return RespBean.success("批量删除成功");
        }
        return RespBean.error("批量删除失败");
    }

}
