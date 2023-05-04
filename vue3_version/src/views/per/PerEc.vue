<!--
 * @Author: zyy 1741540959@qq.com
 * @Date: 2023-01-11 16:25:43
 * @LastEditors: zyy
 * @LastEditTime: 2023-02-12 13:39:10
 * @Description: file content
-->
<template>
  <div>
    <div>
      <el-input size="small" v-model="data.jl.eid" placeholder="奖罚员工编号..." :prefix-icon="Plus"
        style="width: 150px"></el-input>
      <el-input size="small" v-model="data.jl.ecReason" placeholder="奖罚原因..." :prefix-icon="Plus"
        style="width: 150px;margin-left: 6px"></el-input>
      <el-input size="small" v-model="data.jl.ecPoint" placeholder="添加奖罚分数..." :prefix-icon="Plus"
        style="width: 150px;margin-left: 6px"></el-input>
      <el-select size="small" v-model="data.jl.ecType" placeholder="奖罚类型" style="margin-left: 6px;margin-right: 6px">
        <el-option v-for="item in data.titleLevels" :key="item" :label="item" :value="item">
        </el-option>
      </el-select>
      <el-button type="primary" icon="Plus" size="small" @click="addJobLevel">添加</el-button>
    </div>
    <div style="margin-top: 10px;">
      <el-table :data="data.jls" stripe border size="small" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55">
        </el-table-column>
        <el-table-column prop="id" label="编号" width="55">
        </el-table-column>
        <el-table-column prop="eid" label="员工编号" align="center" width="100">
        </el-table-column>
        <el-table-column prop="ecReason" label="奖罚原因" align="center" width="150">
        </el-table-column>
        <el-table-column prop="ecDate" label="创建日期" align="center" width="150">
        </el-table-column>
        <el-table-column prop="ecPoint" label="奖罚分数" align="center" width="100">
        </el-table-column>
        <el-table-column prop="ecType" label="奖罚类型" align="center" width="100">
          <template #default="scope">
            <el-tag type="success" v-if="scope.row.ecType == 0">奖</el-tag>
            <el-tag type="danger" v-else>罚</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template #default="scope">
            <el-button size="small" @click="showEditView(scope.row)">编辑
            </el-button>
            <el-button size="small" type="danger" @click="deleteHandle(scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-button size="small" style="margin-top: 8px" type="danger" :disabled="data.multipleSelection.length === 0"
        @click="deleteMany">批量删除
      </el-button>
    </div>
    <!-- 编辑弹框 -->
    <el-dialog title="编辑奖罚信息" v-model="data.dialogVisible" width="30%">
      <table>
        <tr>
          <td>
            <el-tag>奖罚原因</el-tag>
          </td>
          <td>
            <el-input v-model="data.updateJl.ecReason" size="small" style="margin-left: 6px"></el-input>
          </td>
        </tr>
        <tr>
          <td>
            <el-tag>奖罚分数</el-tag>
          </td>
          <td>
            <el-input v-model="data.updateJl.ecPoint" size="small" style="margin-left: 6px"></el-input>
          </td>
        </tr>
      </table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="data.dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="doUpdate">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ElMessage, ElMessageBox } from 'element-plus';
import api from '@/api/request'
import { Plus } from '@element-plus/icons-vue'
import { reactive } from 'vue';
import type { Jl, MultipleSelection,UpdateJl } from '@/types/per/perec';

interface DataState {
  jl: Jl,
  // 更新 数据对象
  updateJl: UpdateJl,
  titleLevels: [
    '奖','罚'
  ],
  jls: Array<Jl>, // 删除单条
  dialogVisible: boolean,
  multipleSelection: Array<MultipleSelection>
}

let data = reactive<DataState>({
  // 查询 添加 数据对象
  jl: {
    eid: '',
    ecReason: '',
    ecType: '',
    ecPoint: ''
  },
  // 更新 数据对象
  updateJl: {
    ecReason: '',
    ecPoint: '',
    createDate:'',
    ecDate: '',
    ecType: '',
    eid: '',
    id: '',
    remark:''
  },
  titleLevels: [
    // 0,
    // 1
    '奖',
    '罚'
  ],
  jls: [], // 删除单条
  dialogVisible: false,
  multipleSelection: []  // 批量删除勾选中的值
})
// 执行批量删除
function deleteMany() {
  ElMessageBox.confirm('此操作将永久删除[' + data.multipleSelection.length + ']条奖罚信息, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    let ids = '?'
    data.multipleSelection.forEach(item => {
      ids += 'ids=' + item.id + '&'
    })
    api.del('/personnel/ec/' + ids).then(resp => {
      if (resp) {
        initJls()
      }
    })
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '已取消删除'
    });
  });
}
// 批量删除勾选中的值
function handleSelectionChange(val:MultipleSelection[]) {
  data.multipleSelection = val
}
// 更新奖罚信息
function doUpdate() {
  api.put('/personnel/ec/', data.updateJl).then(resp => {
    if (resp) {
      initJls()
      data.dialogVisible = false
    }
  })
}
// 显示编辑弹框
function showEditView(row:any) {
  Object.assign(data.updateJl, row) // 复制数据,注意这里是 , 号隔开
  data.updateJl.createDate = '' // 更新日期由后端处理，这里不用传
  data.dialogVisible = true // 显示编辑弹框
}
// 删除奖罚信息
function deleteHandle(row:any) {
  ElMessageBox.confirm('此操作将永久删除员工编号为[' + row.eid + ']的奖惩信息, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    api.del('/personnel/ec/' + row.id).then(resp => {
      if (resp) {
        initJls()
      }
    })
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '已取消删除'
    });
  });
}
// 添加奖罚信息
function addJobLevel() {
  if (data.jl.ecReason && data.jl.ecPoint) {
    if (data.jl.ecType === '奖') {
      data.jl.ecType = '0'
    } else {
      data.jl.ecType = '1'
    }
    api.post('/personnel/ec/', data.jl).then(resp => {
      if (resp) {
        initJls()
      }
    })
  } else {
    ElMessage.error('字段不能为空！')
  }

}
// 获取奖罚信息列表数据
function initJls() {
  api.get('/personnel/ec/').then((resp:any) => {
    if (resp) {
      data.jls = resp;
      data.jl.ecReason = ''
      data.jl.ecType = ''
      data.jl.ecPoint = ''
      data.jl.eid = ''
    }
  })
}
// 初始化
initJls();
</script>

<style scoped></style>