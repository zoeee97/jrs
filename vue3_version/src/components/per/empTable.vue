<!--
 * @Author: zyy 1741540959@qq.com
 * @Date: 2023-02-14 23:00:52
 * @LastEditors: zyy
 * @LastEditTime: 2023-02-21 16:10:31
 * @Description: file content
-->
<template>
  <div>
    <!-- 表格展示 -->
    <el-table :data="perData" v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading"
      element-loading-background="rgba(0, 0, 0, 0.8)" style="width: 100%" stripe border>
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column v-for="col in columns" :prop="col.prop" :align="col.align" :label="col.label" :width="col.width">
      </el-table-column>
      <el-table-column label="操作" fixed="right" align="center" width="150">
        <template #default="scope">
          <!-- 给编辑按钮绑定点击事件 @click="showEmpView(scope.row)" -->
          <el-button style="padding: 3px;" size="small" @click="pinia.showEmpView(scope.row)">编辑</el-button>
          <!--删除员工 @click="deleteEmp(scope.row)" -->
          <el-button style="padding: 3px;" size="small" type="danger" @click="pinia.deleteEmp(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <div style="display: flex;justify-content: flex-end;margin-top: 10px;">
      <!-- @current-change="currentChange" 当前页
          @size-change="sizeChange" 每页显示多少条 -->
      <el-pagination :hide-on-single-page="true" prev-text="上一页" next-text="下一页" @current-change="currentChange"
        @size-change="sizeChange" :page-sizes="[10, 20, 30, 50, 100]" layout="total, sizes, prev, pager, next, jumper"
        :total="total" background>
      </el-pagination>
    </div>
  </div>
</template>

<script setup lang="ts">
import { store } from '@/stores';
import { storeToRefs } from 'pinia';

const pinia = store();
const defaultProps = {
  children: 'children',
  label: 'name'
}
const columns = [
  { prop: "name", label: "姓名", align: "left", width: "90" },
  { prop: "gender", label: "性别", align: "left", width: "45" },
  { prop: "workID", label: "工号", align: "left", width: "85" },
  { prop: "birthday", label: "出生日期", align: "left", width: "95" },
  { prop: "idCard", label: "身份证号", align: "left", width: "150" },
  { prop: "wedlock", label: "婚姻状态", align: "center", width: "70" },
  { prop: "nation.name", label: "民族", align: "left", width: "50" },
  { prop: "nativePlace", label: "籍贯", align: "center", width: "80" },
  { prop: "politicsStatus.name", label: "政治面貌", align: "left", width: "100" },
  { prop: "email", label: "电子邮件", align: "left", width: "160" },
  { prop: "phone", label: "电话号码", align: "left", width: "100" },
  { prop: "address", label: "联系地址", align: "center", width: "270" },
  { prop: "department.name", label: "所属部门", align: "left", width: "100" },
  { prop: "position.name", label: "职位", align: "left", width: "100" },
  { prop: "joblevel.name", label: "职称", align: "left", width: "100" },
  { prop: "engageForm", label: "聘用形式", align: "left", width: "100" },
  { prop: "tiptopDegree", label: "最高学历", align: "center", width: "80" },
  { prop: "school", label: "毕业学校", align: "left", width: "150" },
  { prop: "specialty", label: "所属专业", align: "left", width: "150" },
  { prop: "workState", label: "在职状态", align: "center", width: "70" },
  { prop: "beginDate", label: "入职日期", align: "left", width: "95" },
  { prop: "conversionTime", label: "转正日期", align: "left", width: "95" },
  { prop: "beginContract", label: "合同起始日期", align: "left", width: "95" },
  { prop: "endContract", label: "合同截止日期", align: "left", width: "100" },
  { prop: "contractTerm", label: "合同期限/年", align: "left", width: "100" },
]

// 解构赋值会失去响应性 所以要用storeToRefs 但是会把对象里面的所有值都转化成ref对象 下面在js操作时就要.value
let { empSelect, perData, loading, total } = storeToRefs(pinia)

pinia.initEmps()

// 分页-当前页 参数会在鼠标点击时自动携带
function currentChange(currentChange: any) {
  // console.log('ww'+Object.prototype.toString.call(currentChange));
  empSelect.value.currentPage = currentChange
  pinia.initEmps()
}
// 分页-每页显示多少条
function sizeChange(size: any) {
  // console.log(Object.prototype.toString.call(size)); 确实是Number类型
  empSelect.value.size = size
  pinia.initEmps()
}
</script>

<style scoped>

</style>