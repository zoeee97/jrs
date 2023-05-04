<template>
  <div>
    <div style=" display: flex;justify-content: center;margin-top: 10px;">
      <!-- v-model="keywords" \ @click="doSearch">搜索 -->
      <el-input v-model="data.keywords" placeholder="通过用户名搜索用户..." :prefix-icon="Search"
        style="width: 400px;margin-right: 10px;"></el-input>
      <el-button type="primary" :icon="Search" @click="doSearch">搜索</el-button>
    </div>
    <div class="admin-container">
      <el-card class="admin-card" v-for="(admin, index) in data.admins" :key="index">
        <div slot="header" class="clearfix">
          <span>{{ admin.name }}</span>
          <el-button style="float: right; padding: 3px 0;color:red;" type="text" :icon="Delete"
            @click="deleteAdmin(admin)"></el-button>
        </div>
        <div>
          <div class="img-container">
            <img :src="admin.userFace" :alt="admin.name" :title="admin.name" class="userFace-img">
          </div>
        </div>
        <div class="userinfo-container">
          <div>用户名：{{ admin.name }}</div>
          <div>手机号码：{{ admin.phone }}</div>
          <div>电话号码：{{ admin.telephone }}</div>
          <div>地址：{{ admin.address }}</div>
          <div>用户状态：
            <!-- 更新操作员 @change="enabledChange(admin)" -->
            <el-switch v-model="admin.enabled" active-color="#13ce66" inactive-color="#ff4949"
              @change="enabledChange(admin)" active-text="启用" inactive-text="禁用">
            </el-switch>
          </div>
          <div>
            用户角色：
            <el-tag style="margin-right: 4px;" type="success" v-for="(role, index) in admin.roles" :key="index">
              {{ role.nameZh }}
            </el-tag>
            <!-- 更新操作员角色 弹出框、选择器、 -->
            <!-- @show="showPop(admin)" -->
            <!-- @hide="hidePop(admin)" hide 隐藏时触发-->
            <el-popover placement="right" title="角色列表" width="200" @show="showPop(admin)" @hide="hidePop(admin)"
              trigger="click">
              <!-- 更新操作员角色 下拉框 -->
              <!-- v-model="selectedRoles" 存的是1个角色id，multiple 多选，显示已有角色 -->
              <el-select v-model="data.selectedRoles" multiple placeholder="请选择">
                <el-option v-for="(r, index) in data.allRoles" :key="index" :label="r.nameZh" :value="r.id">
                </el-option>
              </el-select>
              <!-- 3个点按钮 ... -->
              <template #reference>
                <el-button slot="reference" type="text" :icon="More"></el-button>
              </template>
            </el-popover>
          </div>
          <div>备注：{{ admin.remark }}</div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ElMessage, ElMessageBox } from 'element-plus';
import api from '@/api/request'
import { More, Search, Delete } from '@element-plus/icons-vue'
import { reactive } from 'vue';
import type { AdminRole,Roles } from '@/types/sys/sys';

interface DataState {
  admins: Array<AdminRole>,
  keywords: string,
  allRoles: Array<Roles>, 
  selectedRoles: Array<number>
}

let data = reactive<DataState>({
  admins: [],
  keywords: '', // 搜索关键字
  allRoles: [], // 更新操作员角色
  selectedRoles: []
})
// 更新操作员角色
function hidePop(admin:AdminRole) {
  let roles:Array<Roles> = [];
  Object.assign(roles, admin.roles);// 拷贝对象
  let flag = false;
  // 如果选中的角色 id 的长度和原来的不一样
  if (roles.length != data.selectedRoles.length) { // 用户对应角色id
    flag = true;
  } else {
    // 角色 id 长度和原来的一样，但可能角色不一样
    // 先循环 admin.roles
    for (let i = 0; i < roles.length; i++) {
      let role = roles[i] // 用户对应的角色对象
      for (let j = 0; j < data.selectedRoles.length; j++) {
        let sr = data.selectedRoles[j]  // 拿到用户对应的角色对象的id
        if (role.id == sr) { // 角色一样
          roles.splice(i, 1);// 删除
          i--;
          break;
        }
      }
    }
    if (roles.length != 0) {
      flag = true
    }
  }
  if (flag) {
    // 拼接 url(参数为 adminId、角色 rids )
    let url = '/system/admin/role?adminId=' + admin.id;
    data.selectedRoles.forEach(sr => {
      url += '&rids=' + sr
    });
    api.put(url).then(resp => {
      if (resp) {
        initAdmins()
      }
    });
  }
}
// 下拉框获取所有用户角色
function showPop(admin:AdminRole) {
  initAllRoles();
  let roles:Roles[] = admin.roles;// 拿到整个数组
  data.selectedRoles = [];
  roles.forEach(r => {
    data.selectedRoles.push(r.id) // r.id 相同的角色放进数组
  });
}
// 获取所有操作员
function initAllRoles() {
  api.get('/system/admin/roles').then((resp:any) => {
    if (resp) {
      data.allRoles = resp;
    }
  })
}
// 更新操作员
function enabledChange(admin:AdminRole) {
  api.put('/system/admin/', admin).then(resp => {
    if (resp) {
      initAdmins();
    }
  })
}
// 删除操作员
function deleteAdmin(admin:AdminRole) {
  ElMessageBox.confirm('此操作将永久删除[' + admin.name + ']操作员, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    api.del('/system/admin/' + admin.id).then(resp => {
      if (resp) {
        initAdmins()
      }
    })
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '已取消删除'
    });
  });
}
// 搜索
function doSearch() {
  initAdmins()
}
// 获取操作员
function initAdmins() {
  api.get('/system/admin/?keywords=' + data.keywords).then((resp:any) => {
    if (resp) {
      data.admins = resp;
    }
  })
}

initAdmins()
</script>

<style scoped>
.admin-container {
  margin-top: 10px;
  display: flex;
  justify-content: space-around;
  /* 在弹性盒对象的 <div> 元素中的各项周围留有空白：*/
  flex-wrap: wrap;
  /* 自动换行 */
}

.admin-card {
  width: 350px;
  margin-bottom: 20px;
}

.userFace-img {
  width: 72px;
  height: 72px;
  border-radius: 72px;
}

/* 头像居中 */
.img-container {
  width: 100%;
  display: flex;
  justify-content: center;
}

.userinfo-container {
  font-size: 12px;
  color: #3e9ef5;
}
</style>