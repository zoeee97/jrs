<template>
  <div>
    <el-table size="small" :data="data.emps" stripe border>
      <el-table-column align="left" type="selection" width="55">
      </el-table-column>
      <el-table-column prop="name" label="姓名" align="left" fixed width="80">
      </el-table-column>
      <el-table-column prop="workID" label="工号" align="left" width="80">
      </el-table-column>
      <el-table-column prop="email" label="邮箱地址" align="left" width="150">
      </el-table-column>
      <el-table-column prop="phone" label="电话号码" align="left" width="150">
      </el-table-column>
      <el-table-column prop="department.name" label="所属部门" align="left" width="80">
      </el-table-column>
      <el-table-column label="工资账套" align="center">
        <template #default="scope">
          <el-tooltip placement="right" v-if="scope.row.salary">
            <template #content>
              <table>
                <tr>
                  <td>基本工资</td>
                  <td>{{ scope.row.salary.basicSalary }}</td>
                </tr>
                <tr>
                  <td>交通补助</td>
                  <td>{{ scope.row.salary.trafficSalary }}</td>
                </tr>
                <tr>
                  <td>午餐补助</td>
                  <td>{{ scope.row.salary.lunchSalary }}</td>
                </tr>
                <tr>
                  <td>奖金</td>
                  <td>{{ scope.row.salary.bonus }}</td>
                </tr>
                <tr>
                  <td>养老金比率</td>
                  <td>{{ scope.row.salary.pensionPer }}</td>
                </tr>
                <tr>
                  <td>养老金基数</td>
                  <td>{{ scope.row.salary.pensionBase }}</td>
                </tr>
                <tr>
                  <td>医疗保险比率</td>
                  <td>{{ scope.row.salary.medicalPer }}</td>
                </tr>
                <tr>
                  <td>医疗保险基数</td>
                  <td>{{ scope.row.salary.medicalBase }}</td>
                </tr>
                <tr>
                  <td>公积金比率</td>
                  <td>{{ scope.row.salary.accumulationFundPer }}</td>
                </tr>
                <tr>
                  <td>公积金基数</td>
                  <td>{{ scope.row.salary.accumulationFundBase }}</td>
                </tr>
              </table>
            </template>
            <el-tag>{{ scope.row.salary.name }}</el-tag>
          </el-tooltip>
          <el-tag v-else>暂未设置</el-tag>
        </template>
      </el-table-column>
      <!-- 编辑工资账套 -->
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <!-- 当前员工的工资账套 @show="showPop(scope.row.salary)" show	显示时触发 -->
          <!-- @hide="hidePop(scope.row)" hide	隐藏时触发 -->
          <el-popover size="small" @show="showPop(scope.row.salary)" @hide="hidePop(scope.row)" placement="left"
            title="编辑工资账套" width="200" trigger="click">
            <div>
              <el-select v-model="data.currentSalary" placeholder="请选择">
                <el-option size="small" v-for="item in data.salaries" :key="item.id" :label="item.name" :value="item.id">
                </el-option>
              </el-select>
            </div>
            <template #reference>
              <el-button type="danger">修改工资账套</el-button>
            </template>
          </el-popover>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <div style="display: flex;justify-content: flex-end;margin-top: 5px;">
      <el-pagination @size-change="sizeChange" @current-change="currentChange"
        layout="total, sizes, prev, pager, next, jumper" :total="data.total" background>
      </el-pagination>
    </div>
  </div>
</template>

<script setup lang="ts">
import api from '@/api/request'
import { reactive } from 'vue';
import type { Salary,Emp } from '@/types/sal/sal';

interface DataState {
  emps:Array<Emp>,
  salaries:Array<Salary>,
  currentPage:number,
  size:number,
  total:number,
  currentSalary:string|null
}

let data = reactive<DataState>({
  emps: [],
  salaries: [], // 工资账套数组
  currentPage: 1, //当前页
  size: 10, // 每页显示条数
  total: 0, // 分页
  currentSalary: null // 当前员工工资账套
})
function hidePop(res:any) { // 隐藏时触发
  // 当前员工工资账套存在 并且不等于当前的 才更新
  if (data.currentSalary && data.currentSalary !== res.salary.id) {
    api.put('/salary/sobcfg/?eid=' + res.id + '&sid=' + data.currentSalary).then(resp => {
      if (resp) {
        initEmps()
      }
    });
  }
}
// 员工工资账套
function showPop(res:Salary) { // 显示时触发
  if (res) {
    data.currentSalary = res.id;
  } else {
    data.currentSalary = null
  }
}
// 获取所有工资账套
function initSalaries() {
  api.get('/salary/sobcfg/salaries').then((resp:any) => {
    if (resp) {
      data.salaries = resp
    }
  })
}
// 分页-当前页
function currentChange(page:number) {
  data.currentPage = page
  initEmps()
}
// 分页-每页显示数量
function sizeChange(size:number) {
  data.size = size
  initEmps()
}
// 获取所有数据
function initEmps() {
  api.get('/salary/sobcfg/?currentPage=' + data.currentPage + '&size=' + data.size).then((resp:any) => {
    if (resp) {
      data.emps = resp.data
      data.total = resp.total
    }
  })
}
initEmps()
initSalaries() // 初始化 获取所有工资账套
</script>

<style scoped></style>