/*
 * @Author: zyy 1741540959@qq.com
 * @Date: 2023-01-11 16:00:34
 * @LastEditors: zyy
 * @LastEditTime: 2023-02-23 20:16:04
 * @Description: file content
 */
import { createApp } from 'vue'
import App from './App.vue'
// 导入构造函数
import { createPinia } from 'pinia'
import piniaPluginPersist from 'pinia-plugin-persist'
import router from './router/index'

import api from '@/api/request'

import './assets/main.css'

/* 不能写在这里！！
   不能先引入 */
// 导入我们自定义的store
// import { store } from '@/stores/index'
// import { initMenu } from '@/utils/menu'

// 创建Vue应用实例app
const app = createApp(App)
const pinia = createPinia();
// 装载pinia的持久化插件
pinia.use(piniaPluginPersist);
// 应用以插件形式挂载Pinia实例
app.use(router).use(pinia).mount('#app')

// 导入我们自定义的store
import { store } from '@/stores/index'
import { initMenu } from '@/utils/menu'

// 配置全局守卫
router.beforeEach((to, from, next) => {
  // 用户登录成功时，把 token 存入 sessionStorage，如果携带 token，初始化菜单，放行
  if (window.sessionStorage.getItem('token') && window.sessionStorage.getItem('token')!='null') {
    const userStore = store()
    initMenu(router)
    
    if (!window.sessionStorage.getItem('currentAdmin')) {
      // 判断用户信息是否存在
      return api.get('/admin/info').then(resp => {
        console.log('aa');
        if (resp) {
          // 存入用户信息，转字符串，存入 sessionStorage
          console.log('userinforesp:' + JSON.stringify(resp));
          window.sessionStorage.setItem('currentAdmin',JSON.stringify(resp))
          next({ ...to, replace: true })
          /* 
正以为如此很多人在使用动态添加路由addRoutes()会遇到下面的情况：
在addRoutes()之后第一次访问被添加的路由会白屏，这是因为刚刚addRoutes()就立刻访问被添加的路由，然而此时addRoutes()没有执行结束，因而找不到刚刚被添加的路由导致白屏。因此需要从新访问一次路由才行。

该如何解决这个问题 ?
此时就要使用next({ …to, replace: true })来确保addRoutes()时动态添加的路由已经被完全加载上去。

next({ …to, replace: true })中的replace: true只是一个设置信息，告诉VUE本次操作后，不能通过浏览器后退按钮，返回前一个路由。

因此next({ …to, replace: true })可以写成next({ …to })，不过你应该不希望用户在addRoutes()还没有完成的时候，可以点击浏览器回退按钮搞事情吧。

只有next()才是放行，其他都是中断当前导航，执行新的导航
*/
        }
      }).catch(e => {
        console.log('!' + e);
      })
    }
    // console.log('userstore:'+JSON.stringify(userStore));
    // 有个地方很奇怪 我好像是路径跳转完store里才有routes的数据 现在不知道怎么搞 就先这样吧
    next();
  } else {
    // 去Login页 放行
    if (to.path === '/') next();
    // else next('/redirect=?'+to.path)
    else next({path:'/'})
  }

})