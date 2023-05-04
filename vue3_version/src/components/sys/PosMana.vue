<template>
  <div>
    <div>
      <el-input size="small" class="addPosInput" placeholder="添加职位..." suffix-icon="el-icon-plus"
        @keydown.enter.native="addPosition" v-model="data.pos.name">
      </el-input>
      <el-button size="small" icon="el-icon-plus" type="primary" @click="addPosition">添加</el-button>
    </div>
    <div class="posManaMain">
      <el-table border stripe size="small" :data="data.positions" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55">
        </el-table-column>
        <el-table-column prop="id" label="编号" width="55">
        </el-table-column>
        <el-table-column prop="name" label="职位" width="120">
        </el-table-column>
        <el-table-column prop="createDate" label="创建时间" width="150">
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button size="small" @click="showEditView(scope.row)">编辑
            </el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- :disabled 不禁用条件为勾选中，没勾选中为禁用。 -->
    <el-button size="small" style="margin-top: 8px" type="danger" :disabled="data.multipleSelection.length === 0"
      @click="deleteMany">批量删除
    </el-button>
    <el-dialog title="编辑职位" v-model="data.dialogVisible" width="30%">
      <div>
        <el-tag>职位名称</el-tag>
        <el-input v-model="data.updatePos.name" size="small" class="updatePosInput"></el-input>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="data.dialogVisible = false">取 消</el-button>
        <el-button size="small" type="primary" @click="doUpdate">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import api from '@/api/request'
import { ElMessage, ElMessageBox } from 'element-plus';
import { reactive } from 'vue';
import type { Position } from '@/types/emp/emp';

interface DataState {
  pos: Position,
  positions: Position[],
  dialogVisible: boolean,
  updatePos: Position,
  multipleSelection: Position[] // 批量删除勾选的对象
}

let data = reactive<DataState>({
  pos: { // 查询添加职位数据
    id: 0,
    name: '',
    createDate: '',
    enabled: false
  },
  positions: [],
  dialogVisible: false,
  updatePos: { // 更新职位数据
    id: 0,
    name: '',
    createDate: '',
    enabled: false
  },
  multipleSelection: [] // 批量删除勾选的对象
})
// 批量删除请求
function deleteMany() {
  ElMessageBox.confirm('此操作将永久删除[' + data.multipleSelection.length + ']条职位, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    let ids = '?'
    data.multipleSelection.forEach(item => {
      ids += 'ids=' + item.id + '&'
    })
    api.del('/system/basic/pos/' + ids).then(resp => {
      if (resp) {
        initPositions()
      }
    })
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '已取消删除'
    });
  });
}
// 批量删除(取值）
function handleSelectionChange(val) {
  data.multipleSelection = val
}
// 编辑职位
function doUpdate() {
  api.put('/system/basic/pos/', data.updatePos).then(resp => {
    if (resp) {
      initPositions() // 刷新数据列表
      data.dialogVisible = false // 关闭对话框
    }
  })
}
// 编辑职位对话框
function showEditView(res:any) {
  Object.assign(data.updatePos,res) // 回显数据,数据拷贝
  data.updatePos.createDate = ''
  data.dialogVisible = true // 显示编辑框
}
// 添加职位
function addPosition() {
  if (data.pos.name) {
    api.post('/system/basic/pos/', data.pos).then(resp => {
      if (resp) {
        initPositions()
        data.pos.name = ''
      }
    })

  } else {
    ElMessage.error('职位名称不能为空！')
  }
}
// 删除职位
function handleDelete(res:any) {
  ElMessageBox.confirm('此操作将永久删除[' + res.name + ']职位, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    api.del('/system/basic/pos/' + res.id).then(resp => {
      if (resp) {
        initPositions()
      }
    })
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '已取消删除'
    });
  });
}
// 获取后端接口所有职位数据
function initPositions() {
  api.get('/system/basic/pos/').then((resp: any) => {
    if (resp) {
      data.positions = resp
    }
  })
}
initPositions()
</script>

<style scoped>
/*添加职位输入框*/
.addPosInput {
  width: 300px;
  margin-right: 8px;
}

/*所有数据表格*/
.posManaMain {
  margin-top: 10px;
}

/*编号职位输入框*/
.updatePosInput {
  width: 200px;
  margin-left: 8px;
}
</style>