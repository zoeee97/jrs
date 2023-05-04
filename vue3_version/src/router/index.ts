/*
 * @Author: zyy 1741540959@qq.com
 * @Date: 2023-01-11 16:00:34
 * @LastEditors: zyy
 * @LastEditTime: 2023-02-23 22:15:10
 * @Description: file content
 */
import { createRouter, createWebHistory, type RouteRecordRaw} from 'vue-router'

const routes: Array<RouteRecordRaw> = [
  {
    path:'/',
    name:'Login',
    component:()=>import('@/views/login/Login.vue'),
    meta:{
      hidden:true,
    }
  },
  {
    path:'/home',
    name:'Home',
    component:()=>import('@/views/home/Home.vue'),
    children:[
      {
        path:'chat',
        name:'在线聊天',
        component:()=>import('@/views/chat/FriendChat.vue'),
      },
      {
        path:'userinfo',
        name:'个人中心',
        component:()=>import('@/views/admininfo/AdminInfo.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
