<!--
 * @Author: zyy 1741540959@qq.com
 * @Date: 2023-01-11 16:21:56
 * @LastEditors: zyy
 * @LastEditTime: 2023-02-23 22:16:18
 * @Description: file content
-->
<!--
 * @Author: zyy 1741540959@qq.com
 * @Date: 2023-01-11 16:21:56
 * @LastEditors: zyy
 * @LastEditTime: 2023-02-07 16:04:07
 * @Description: file content
-->
<template>
  <div class="common-layout">
    <el-container>
      <el-header class="homeHeader">
        <div class="title">简人事</div>
        <!-- 1-1 添加在线聊天入口 -->
        <div>
          <el-button type="text" :icon="Bell" size="default" style="margin-right: 8px;color: black;"
            @click="goChat"></el-button>
          <el-dropdown class="userInfo" @command="commandHandler">
            <span class="el-dropdown-link" style="">
              {{ currentAdmin.name }}
              <i><img style="margin: auto" :src="currentAdmin.userFace"></i>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="userinfo">个人中心</el-dropdown-item>
                <el-dropdown-item command="logout">注销登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-container>
        <el-aside width="200px">
          <!-- 1、添加 router  -->
          <el-menu router unique-opened>
            <!-- 2、循环整个路由组件，不展示 hidden: true 的路由组件 -->
            <template v-for="(item, index) in routes" >
              <el-sub-menu :index="index + ''" v-if="!item.hidden|| true" :key="index">
                <template #title>
                  <i :class="item.iconCls" style="color: black;margin-right: 5px"></i>
                  <span>{{ item.name }}</span>
                </template>
                <!-- 3、循环遍历子路由 -->
                <el-menu-item :index="children.path" v-for="(children, index) in item.children" :key="index">
                  {{children.name}}
                </el-menu-item>
              </el-sub-menu>
            </template>
          </el-menu>
        </el-aside>
        <el-container>
          <el-main>
            <!-- 面包屑导航区域 -->
            <el-breadcrumb separator-class="el-icon-arrow-right" v-if="router.currentRoute.value.path !== '/home'">
              <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
              <el-breadcrumb-item>{{ router.currentRoute.value.name }}</el-breadcrumb-item>
            </el-breadcrumb>
            <!-- vue3获取当前路由写法 currentRoute是Ref型数据 -->
            <div class="homeWelcome" v-if="router.currentRoute.value.path === '/home'">
              欢迎来到简人事后台管理系统！
            </div>
            <!-- 路由点位符 -->
            <router-view class="homeRouterView" />
          </el-main>
        </el-container>
      </el-container>
    </el-container>
  </div>
</template>


<script lang="ts">
import { defineComponent, isProxy, isReactive, isRef, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { storeToRefs } from 'pinia';
import { store } from '@/stores/index'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Bell } from '@element-plus/icons-vue'
import api from '@/api/request'
export default defineComponent({
  setup() {
    const router = useRouter()
    const route = useRoute()
    const pinia = store();

/*     console.log(isProxy(pinia));//true
    console.log(isReactive(pinia));//true
    console.log(isProxy(pinia.token));//false
    console.log(isReactive(pinia.token));//false
    console.log(Object.prototype.toString.call(pinia.token));//[object String]
    console.log(isReactive(pinia.empSelect));//true */
    
    let { currentAdmin, routes, token } = storeToRefs(pinia);//响应式解构取值
    // 进入在线聊天页面
    const goChat = () => {
      router.push('/home/chat')
    }
    const commandHandler = (command: string) => {
      if (command === 'logout') {
        ElMessageBox.confirm(
          '此操作将注销登录, 是否继续?', '提示',
          {
            confirmButtonText: 'OK',
            cancelButtonText: 'Cancel',
            type: 'warning',
          }
        )
          .then(() => {
            // 注销登录
            api.post('/logout')
            // 清空用户信息和菜单信息 $patch一次性修改多个状态
            pinia.$patch({
              token: '',
              currentAdmin: {},
              routes: []
            })
            window.sessionStorage.removeItem('token')
            window.sessionStorage.removeItem('currentAdmin')
            // 跳转回登录页
            router.replace('/')
          })
          .catch(() => {
            ElMessage({
              type: 'info',
              message: '已取消注销登录',
            })
          })
      } else if (command === 'userinfo') {
        router.push('/home/userinfo')
      }
    }
    return {
      currentAdmin,
      routes,
      token,
      router,
      goChat,
      commandHandler,
      Bell
    }
  }
})
</script>

<style scoped>
.common-layout{
  width: 100vw;
  height: 100vh;  
}
.homeHeader {
  background: #3e9ef5;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 15px;
  box-sizing: border-box;
  max-height: 10vh;
  line-height: 10vh;
}

.el-menu{
  height: 90vh;
}

.homeHeader .title {
  font-size: 30px;
  /*font-family: 微软雅黑;*/
  font-family: 华文楷体;
  color: white;
}

.homeHeader .userInfo {
  cursor: pointer;
}

.el-dropdown-link img {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  margin-left: 8px;
}

.homeWelcome {
  text-align: center;
  font-size: 30px;
  font-family: 华文楷体;
  color: #409ef4;
  padding-top: 50px;
}

.homeRouterView {
  margin-top: 10px;
}
.example-showcase .el-dropdown-link {
  cursor: pointer;
  color: var(--el-color-primary);
  display: flex;
  align-items: center;
}
</style>