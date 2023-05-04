<template>
  <div>
    <div>
      <el-input size="small" v-model="data.jl.name" placeholder="添加职称名称..." :prefix-icon="Plus"
        style="width: 300px"></el-input>
      <el-select size="small" v-model="data.jl.titleLevel" placeholder="职称等级" style="margin-left: 6px;margin-right: 6px">
        <el-option v-for="item in data.titleLevels" :key="item" :label="item" :value="item">
        </el-option>
      </el-select>
      <el-button type="primary" :icon="Plus" size="small" @click="addJobLevel">添加</el-button>
    </div>
    <div style="margin-top: 10px;">
      <el-table :data="data.jls" stripe border size="small"  @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55">
        </el-table-column>
        <el-table-column prop="id" label="编号" width="55">
        </el-table-column>
        <el-table-column prop="name" label="职称名称" width="150">
        </el-table-column>
        <el-table-column prop="titleLevel" label="职称等级" width="150">
        </el-table-column>
        <el-table-column prop="createDate" label="创建日期" width="150">
        </el-table-column>
        <el-table-column label="操作">
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
    <el-dialog title="编辑职称" v-model="data.dialogVisible" width="30%">
      <table>
        <tr>
          <td>
            <el-tag>职称名称</el-tag>
          </td>
          <td>
            <el-input v-model="data.updateJl.name" size="small" style="margin-left: 6px"></el-input>
          </td>
        </tr>
        <tr>
          <td>
            <el-tag>职称等级</el-tag>
          </td>
          <td>
            <el-select size="small" v-model="data.updateJl.titleLevel" placeholder="职称等级"
              style="margin-left: 6px;margin-right: 6px">
              <el-option v-for="item in data.titleLevels" :key="item" :label="item" :value="item">
              </el-option>
            </el-select>
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
import type { JobLevel } from '@/types/emp/emp';

interface DataState {
  // 查询 添加 数据对象
  jl: JobLevel,
  // 更新 数据对象
  updateJl: JobLevel,
  titleLevels: [
    '正高级',
    '副高级',
    '中级',
    '初级',
    '员级'
  ],
  jls: JobLevel[], // 删除单条
  dialogVisible: boolean,
  multipleSelection: JobLevel[] // 批量删除勾选中的值
}
let data = reactive<DataState>({
  jl: {
    name: '',
    titleLevel: ''
  },
  // 更新 数据对象
  updateJl: {
    name: '',
    titleLevel: '',
  },
  titleLevels: [
    '正高级',
    '副高级',
    '中级',
    '初级',
    '员级'
  ],
  jls: [], // 删除单条
  dialogVisible: false,
  multipleSelection: [] // 批量删除勾选中的值
})
// 执行批量删除
function deleteMany() {
  ElMessageBox.confirm('此操作将永久删除[' + data.multipleSelection.length + ']条职称, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    let ids = '?'
    data.multipleSelection.forEach(item => {
      ids += 'ids=' + item.id + '&'
    })
    api.del('/system/basic/joblevel/' + ids).then(resp => {
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
function handleSelectionChange(val:JobLevel[]) {
  data.multipleSelection = val
}
// 更新职称信息
function doUpdate() {
  api.put('/system/basic/joblevel/', data.updateJl).then(resp => {
    if (resp) {
      initJls()
      data.dialogVisible = false
    }
  })
}
// 显示编辑弹框
function showEditView(res:any) {
  Object.assign(data.updateJl, res) // 复制数据,注意这里是 , 号隔开
  data.updateJl.createDate = '' // 更新日期由后端处理，这里不用传
  data.dialogVisible = true // 显示编辑弹框
}
// 删除职称
function deleteHandle(res:any) {
  ElMessageBox.confirm('此操作将永久删除[' + res.name + ']职称, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    api.del('/system/basic/joblevel/' + res.id).then(resp => {
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
// 添加职称
function addJobLevel() {
  if (data.jl.name && data.jl.titleLevel) {
    api.post('/system/basic/joblevel/', data.jl).then(resp => {
      if (resp) {
        initJls()
      }
    })
  } else {
    ElMessage.error('字段不能为空！')
  }

}
// 获取职称列表数据
function initJls() {
  api.get('/system/basic/joblevel/').then((resp:any) => {
    if (resp) {
      data.jls = resp;
      data.jl.name = ''
      data.jl.titleLevel = ''
    }
  })
}
initJls()
</script>

<style scoped></style>