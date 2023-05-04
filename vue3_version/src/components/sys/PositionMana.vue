<template>
  <div>
    <div class="positionManaTool">
      <el-input v-model="data.role.name" placeholder="请输入角色英文名" size="small">
        <template slot="prepend">ROLE_</template>
      </el-input>
      <el-input v-model="data.role.nameZh" placeholder="请输入角色中文名" size="small"
        @keydown.enter.native="doAddRole"></el-input>
      <el-button type="primary" :icon="Plus" size="small" @click="doAddRole">添加角色</el-button>
    </div>
    <!-- 手风琴 -->
    <div class="positionManaMain">
      <el-collapse v-model="data.activeName" accordion @change="change">
        <el-collapse-item :title="r.nameZh" :name="r.id" v-for="(r, index) in data.roles" :key="index">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>可访问资源</span>
              <el-button type="text" :icon="Delete" style="float: right;padding: 3px 0;color: #f41f0a"
                @click="doDeleteRole(r)">
              </el-button>
            </div>
            <div>
              <el-tree ref="treeRef" show-checkbox :data="data.allMenus" :props="data.defaultProps"
                :default-checked-keys="data.selectedMenus" node-key="id" :key="index"></el-tree>
              <div style="display: flex;justify-content: flex-end">
                <el-button size="small" @click="cancelUpdate">取消修改</el-button>
                <el-button size="small" type="primary" @click="doUpdate(r.id,index)">确认修改</el-button>
              </div>
            </div>
          </el-card>
        </el-collapse-item>
      </el-collapse>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ElMessage, ElMessageBox, ElTree } from 'element-plus';
import api from '@/api/request'
import { Plus, Delete } from '@element-plus/icons-vue'
import { reactive, ref } from 'vue';
import type { Roles } from '@/types/sys/sys';
import type { routesState } from '@/stores/index'

const treeRef = ref<InstanceType<typeof ElTree>>()

interface DataState {
  role: Roles,
  roles: Roles[],
  allMenus: routesState[],
  defaultProps: { // 树形控件
    children: 'children',
    label: 'name' // 绑定数据 :name="r.id"
  },
  selectedMenus: Array<number>,
  activeName: -1 // 折叠面板 默认关闭
}
let data = reactive<DataState>({
  role: {
    name: '',
    nameZh: ''
  },
  roles: [],
  allMenus: [],
  defaultProps: { // 树形控件
    children: 'children',
    label: 'name' // 绑定数据 :name="r.id"
  },
  selectedMenus: [],
  activeName: -1 // 折叠面板 默认关闭
})
// 删除角色
function doDeleteRole(role: Roles) {
  ElMessageBox.confirm('此操作将永久删除[' + role.nameZh + ']角色, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    api.del('/system/basic/permission/role/' + role.id).then((resp: any) => {
      if (resp) {
        initRoles()
      }
    })
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '已取消删除'
    });
  });

}
// 添加角色
function doAddRole() {
  if (data.role.name && data.role.nameZh) {
    api.post('/system/basic/permission/', data.role).then((resp: any) => {
      if (resp) {
        initRoles()
        data.role.name = ''
        data.role.nameZh = ''
      }
    })
  } else {
    ElMessage.error('所有字段不能为空！')
  }
}
// 取消修改
function cancelUpdate() {
  data.activeName = -1 // 关闭折叠面板
}
// 确认修改
function doUpdate(rid: any,index:any) {
  // 注意对ref数组的某个元素的处理 这里因为存在很多个树形控件 所以需要通过key值确定选中的tree
  // console.log(treeRef.value[index]);
  let selectedKeys = treeRef.value[index].getCheckedKeys(true) // 获取选中的节点数组,true 仅返回被选中的叶子节点的 keys,如三级分类
  console.log(selectedKeys)
  let url = '/system/basic/permission/?rid=' + rid
  selectedKeys.forEach(key => {
    // 循环遍历出数组 id ，拼接在一起
    url += '&mids=' + key;
  })
  api.put(url).then(resp => {
    if (resp) {
      initRoles();
      data.activeName = -1 // 关闭折叠面板
    }
  })
}
// 手风琴点击事件
function change(rid: any) {
  if (rid) {
    initAllMenus() // 调用获取所有菜单
    initSelectedMenus(rid) // 调用获取所有选中的菜单
  }
}
// 获取所有选中的菜单
function initSelectedMenus(rid: any) { // :name="r.id"  change(rid)
  api.get('/system/basic/permission/mid/' + rid).then((resp: any) => {
    if (resp) {
      data.selectedMenus = resp;
    }
  })
}
// 获取所有菜单
function initAllMenus() {
  api.get('/system/basic/permission/menus').then((resp: any) => {
    if (resp) {
      data.allMenus = resp;
    }
  })
}
// 获取所有角色
function initRoles() {
  api.get('/system/basic/permission/').then((resp: any) => {
    if (resp) {
      data.roles = resp;
    }
  })
}
initRoles()
initAllMenus()
</script>

<style scoped>
.positionManaTool {
  display: flex;
  justify-content: flex-start;
}

.positionManaTool .el-input {
  width: 300px;
  margin-right: 6px;
}

.positionManaMain {
  margin-top: 10px;
  width: 700px;
}
</style>