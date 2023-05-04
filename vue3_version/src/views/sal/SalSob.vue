<template>
  <div>
    <!--绘制表格 -->
    <div style="display: flex;justify-content: space-between;">
      <!--@click="showAddSalaryView" 点击打开 添加工资账套对话框 -->
      <el-button type="primary" :icon="Plus" size="small" @click="showAddSalaryView">添加工资账套</el-button>
      <!--刷新功能 直接绑定点击事件 调用获取所有数据方法 -->
      <el-button type="success" :icon="Refresh" size="small" @click="initSalaries"></el-button>
    </div>
    <div style="margin-top: 10px; ">
      <el-table :data="data.salaries" width="80%" stripe border>
        <!-- 多选框 type="selection" -->
        <el-table-column type="selection" width="40">
        </el-table-column>
        <el-table-column prop="name" label="账套名称" width="120">
        </el-table-column>
        <el-table-column prop="basicSalary" label="基本工资" width="70">
        </el-table-column>
        <el-table-column prop="trafficSalary" label="交通补助" width="70">
        </el-table-column>
        <el-table-column prop="lunchSalary" label="午餐补助" width="70">
        </el-table-column>
        <el-table-column prop="bonus" label="奖金" width="70">
        </el-table-column>
        <el-table-column prop="createDate" label="启用时间" width="100">
        </el-table-column>
        <!-- 多级表头：el-table-column 里面嵌套 el-table-column，就可以实现多级表头  -->
        <el-table-column label="养老金" align="center">
          <el-table-column prop="pensionPer" label="比率" width="70">
          </el-table-column>
          <el-table-column prop="pensionBase" label="基数" width="70">
          </el-table-column>
        </el-table-column>
        <!-- 多级表头 -->
        <el-table-column label="医疗保险" align="center">
          <el-table-column prop="medicalPer" label="比率" width="70">
          </el-table-column>
          <el-table-column prop="medicalBase" label="基数" width="70">
          </el-table-column>
        </el-table-column>
        <!-- 多级表头 -->
        <el-table-column label="公积金" align="center">
          <el-table-column prop="accumulationFundPer" label="比率" width="70">
          </el-table-column>
          <el-table-column prop="accumulationFundBase" label="基数" width="70">
          </el-table-column>
        </el-table-column>
        <el-table-column label="操作">
          <!-- 删除工资账套 拿到当前行数据 绑定点击事件 传行数据-->
          <template #default="scope">
            <!-- @click="showEditSalaryView(scope.row)">编辑  -->
            <el-button type="primary" size="small" @click="showEditSalaryView(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="deleteSalary(scope.row)">删除</el-button>
          </template>
        </el-table-column>

      </el-table>
    </div>
    <!-- 添加工资账套对话框 -->
    <!-- 把标题变成属性 -->
    <el-dialog :title="data.dialogTitle" v-model="data.dialogVisible" width="50%">
      <!-- 调整样式 -->
      <div style="display: flex;justify-content: space-around;align-items: center;">
        <!-- 添加步骤条 -->
        <!-- :active="activeItemIndex" -->
        <el-steps direction="vertical" :active="data.activeItemIndex">
          <!-- 循环遍历数据 -->
          <el-step :title="itemName" v-for="(itemName, index) in data.salaryItemName" :key="index"></el-step>
        </el-steps>
        <!-- 循环遍历数据 -->
        <!-- v-show="activeItemIndex = index" 与下标相等才展示 -->
        <!-- 修改各项的值 绑定和遍历-->
        <el-input v-model="data.salary[title]" :placeholder="'请输入' + data.salaryItemName[index] + '...'"
          v-for="(value, title, index) in data.salary" :key="index" v-show="data.activeItemIndex === index"
          style="width: 200px;"></el-input>
      </div>
      <span slot="footer" class="dialog-footer">
        <!-- 按钮判断根据索引显示 文字提示 -->
        <el-button @click="preStep">{{ data.activeItemIndex === 10 ? '取消' : '上一步' }}</el-button>
        <el-button type="primary" @click="nextStep">{{ data.activeItemIndex === 10 ? '完成' : '下一步' }}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ElMessage, ElMessageBox } from 'element-plus';
import api from '@/api/request'
import { Plus, Refresh } from '@element-plus/icons-vue'
import { reactive } from 'vue';
import type { Salary } from '@/types/sal/sal';

interface DataState {
  dialogTitle: string,
  dialogVisible: boolean,
  salaries: Array<Salary>,
  activeItemIndex: number,
  salaryItemName: Array<string>,
  salary: Salary
}

let data = reactive<DataState>({
  dialogTitle: '添加工资账套', //标题
  dialogVisible: false, // 添加工资账套对话框
  salaries: [], // 定义数组
  activeItemIndex: 0, // 步骤条激活索引
  salaryItemName: [ // 步骤条数据对象
    '账套名称',
    '基本工资',
    '交通补助',
    '午餐补助',
    '奖金',
    '养老金比率',
    '养老金基数',
    '医疗保险比率',
    '医疗保险基数',
    '公积金比率',
    '公积金基数'
  ],
  // 定义工资账套数据
  salary: {
    id:'',
    name: '',
    basicSalary: '',
    trafficSalary: '',
    lunchSalary: '',
    bonus: '',
    pensionPer: '',
    pensionBase: '',
    medicalPer: '',
    medicalBase: '',
    accumulationFundPer: '',
    accumulationFundBase: '',
    createDate:''
  }
})

// 点击编辑显示对话框
function showEditSalaryView(resp:Salary) {
  data.dialogTitle = '编辑工资账套' // 设置标题
  data.activeItemIndex = 0 // 默认激活的索引
  data.salary = resp
  data.dialogVisible = true // 打开对话框
}
// 删除工资账套
function deleteSalary(data:Salary) {
  ElMessageBox.confirm('此操作将永久删除该[' + data.name + ']工资账套, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    api.del('/salary/sob/' + data.id).then(resp => {
      if (resp) {
        initSalaries()
      }
    })
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '已取消删除'
    });
  });
}
function preStep() { // 上一步 取消
  if (data.activeItemIndex === 0) {
    return
  } else if (data.activeItemIndex === 10) {
    data.dialogVisible = false;
    return;
  }
  data.activeItemIndex--;
}
function nextStep() { //下一步 完成
  if (data.activeItemIndex === 10) {
    // 添加工资账套
    if (data.salary.id) { // 有 id 调用编辑接口，没有 id 执行添加
      api.put('/salary/sob/', data.salary).then(resp => {
        if (resp) {
          initSalaries()
          data.dialogVisible = false // 关闭弹框
        }
      })

    } else {
      api.post('/salary/sob/', data.salary).then(resp => {
        if (resp) {
          initSalaries()
          data.dialogVisible = false
        }
      })
    }
    return
  }
  data.activeItemIndex++
}
// 点击打开添加工资账套对话框
function showAddSalaryView() {
  data.dialogTitle = '添加工资账套' //添加的时候显示此标题
  data.salary = { // 清空表单
    name: '',
    basicSalary: '',
    trafficSalary: '',
    lunchSalary: '',
    bonus: '',
    pensionPer: '',
    pensionBase: '',
    medicalPer: '',
    medicalBase: '',
    accumulationFundPer: '',
    accumulationFundBase: '',
    id:'',
    createDate:''
  }
  data.activeItemIndex = 0 // 步骤条索引从0开始
  data.dialogVisible = true;
}
// 初始化数据
function initSalaries() {
  api.get('/salary/sob/').then((resp:any) => {
    if (resp) {
      data.salaries = resp
    }
  })
}
initSalaries()
</script>

<style scoped></style>