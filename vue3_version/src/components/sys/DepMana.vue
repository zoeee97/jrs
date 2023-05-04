<template>
  <div style="width: 500px">
    <el-input placeholder="请输入部门名称进行搜索..." :prefix-icon="Search" v-model="filterText">
    </el-input>
    <!-- :expand-on-click-node="false" 点击小三角箭头才会展开
            :default-expand-all="false"	设置默认不展开所有节点 -->
    <el-tree :data="data.deps" :props="defaultProps" :filter-node-method="filterNode" :expand-on-click-node="false"
      ref="treeRef">
      <!-- label: 'name' -->
      <!-- style="display: flex;justify-content: space-between;width: 100% 父容器宽度" 让添加和删除按键居右 -->
      <template #default="{ node, data }">
        <span class="custom-tree-node" slot-scope="{ node, data }"
          style="display: flex;justify-content: space-between;width: 100%">
          <span>{{ data.name }}</span>
          <span>
            <el-button plain type="primary" size="small" class="depBtn" @click="() => showAddDep(data)">
              添加部门
            </el-button>
            <!-- showAddDep(data)  deleteDep(data)  data 后端传过来的完整的 json 对象 -->
            <el-button plain type="danger" size="small" class="depBtn" @click="() => deleteDep(data)">
              删除部门
            </el-button>
          </span>
        </span>
      </template>
    </el-tree>
    <!-- 对话弹框 -->
    <el-dialog title="添加部门" v-model="data.dialogVisible" width="30%">
      <div>
        <table>
          <tr>
            <td>
              <el-tag>上级部门</el-tag>
            </td>
            <td>{{ data.pname }}</td>
          </tr>
          <tr>
            <td>
              <el-tag>部门名称</el-tag>
            </td>
            <td>
              <el-input v-model="data.dep.name" placeholder="请输入部门名称..." size="small"></el-input>
            </td>
          </tr>
        </table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="data.dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="doAddDep">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ElMessage, ElMessageBox, ElTree } from 'element-plus';
import api from '@/api/request'
import { Search } from '@element-plus/icons-vue'
import { reactive, ref, watch } from 'vue';
import type { Deps } from '@/types/emp/emp';

interface Tree {
  id: number
  label: string
  children?: Tree[]
}
interface DataState {
  deps: Deps[],
  dialogVisible: boolean,
  dep: Deps,
  pname: string
}

const treeRef = ref<InstanceType<typeof ElTree>>()
let filterText = ref('')
const defaultProps = { //关联子部门
  children: 'children',
  label: 'name'
}

let data = reactive<DataState>({
  deps: [], // 所有部门整个数组
  dialogVisible: false,
  dep: { // 添加部门数据
    name: '',
    parentId: -1,
    isParent: true,
  },
  pname: '' // 上级部门名称
})
watch(filterText, (val) => {
  // 观察者事件，监控输入框的值(框架方法）
  treeRef.value!.filter(val)
})
// 删除部门调用的方法
function removeDepFromDeps(p:any, deps:any, id:any) {
  for (let i = 0; i < deps.length; i++) {
    let d = deps[i]
    if (d.id === id) {
      deps.splice(i, 1)
      if (deps.length === 0) {
        p.isParent = false;
      }
      return;
    } else {
      removeDepFromDeps(d, d.children, id)
    }
  }
}
// 删除部门
function deleteDep(res:Deps) {
  if (res.isParent) {
    ElMessage.error('父部门删除失败！')
  } else {
    ElMessageBox.confirm('此操作将永久删除该[' + res.name + ']部门, 是否继续?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      api.del('/system/basic/department/' + res.id).then(resp => {
        if (resp) {
          removeDepFromDeps(null, data.deps, res.id)
        }
      })
    }).catch(() => {
      ElMessage({
        type: 'info',
        message: '已取消删除'
      });
    });
  }
}
// 添加完部门 初始化 清空数据
function initDep() {
  data.dep = {
    name: '',
    parentId: -1
  }
  data.pname = ''
}
// 递归查询所有部门信息，deps 查询到的整个数组，dep 添加的部门
function addDep2Deps(deps:Deps[], dep:Deps) {
  for (let i = 0; i < deps.length; i++) {
    let d = deps[i] // 父部门
    if (d.id === dep.parentId) {
      d.children = d.children!.concat(dep) // 把 dep 加为 d 的子部门
      if (d.children.length > 0) {
        d.isParent = true;
      }
      return;
    } else {
      addDep2Deps(d.children!, dep) // 递归调用此方法 以查询结果为条件 继续查询子部门
    }
  }
}
// 确认添加部门
function doAddDep() {
  api.post('/system/basic/department/', data.dep).then(resp => {
    if (resp) {
      data.dialogVisible = false // 关闭对话框
      addDep2Deps(data.deps, resp.obj)
      initDep() // 调用初始化方法 清空数据
    }
  })
}
// 添加部门弹框
function showAddDep(res:any) {
  data.dep.parentId = res.id
  data.pname = res.name
  data.dialogVisible = true
}
// 获取所有部门
function initDeps() {
  api.get('/system/basic/department/').then((resp: any) => {
    if (resp) {
      data.deps = resp;
    }
  })
}
// 事件(框架方法）
function filterNode(value: string, res: Tree) { // data 整行数据
  if (!value) return true; // true 节点可以展示，false 节点隐藏
  console.log('label'+res.name);
  return res.name.includes(value) // label: 'name'
}
initDeps()
</script>

<style scoped>
.depBtn {
  padding: 2px;
}
</style>