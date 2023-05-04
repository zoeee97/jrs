/*
 * @Author: zyy 1741540959@qq.com
 * @Date: 2023-01-16 16:02:31
 * @LastEditors: zyy
 * @LastEditTime: 2023-02-14 11:13:03
 * @Description: file content
 */
import api from '@/api/request'
import type { Router } from 'vue-router'

// 导入我们自定义的store
import { store } from '@/stores/index'

// 菜单请求工具类
// 根据菜单权限动态生成前端路由

/* 
但是这里也需要注意几点：

1：addRouter方法添加的router就像是Vuex里的缓存一样，并不是添加过了就会一直都在，只要页面刷新了， 添加的router就没了，需要重新添加，这个很好理解，因为静态路由也是这样，每次刷新了页面，静态路由也需要重新构造。

所以，我们一般会在路由钩子里动态添加路由，保证刷新之后的路由会重新添加。

具体做法当然是在router.beforeEach中，执行router.addRouer()。

不过一般需要做个判断，不是每次访问新的路由都需要重新添加路由，否则会重复添加路由，也会出问题。

2：addRouter方法执行之后，router.options.routes是找不到你动态添加的路由的，如果需要更新，必须手动将动态添加的路由push进去。此时，router.options.routes才可以用来构建菜单导航。
*/
export const initMenu = (router:Router) => {
  const userStore = store()
  // 如果store里有数据，则直接返回
  if (userStore.routes.length > 0) return;
  // 如果没有则发请求
  else api.get('/system/cfg/menu').then(data => {
    // console.log('menudata:'+JSON.stringify(data));
    if (data) {
      // 格式化路由
      let routes:any = formatRoutes(data)
      // 将数据存到store
      userStore.routes = routes
      // 动态添加到router
      routes.forEach((element:any) => {
        router.addRoute(element)
      });
      // router.addRoutes vue2才有
      // router.addRoute(routes)
      
      // 连接 WebSocket
      userStore.connect()
    }
  }).catch(e=>{
    console.log('wuwu'+e);
  })
}

// 格式化路由
export const formatRoutes = (routes:any) => {
  let fmtRoutes:any[] = []
  const modules = import.meta.glob
  routes.forEach((element:any) => {
    // es6解构赋值
    let { path, component, name, iconCls, children } = element
    // 如果有children且类型为数组
    if (children && children instanceof Array)
      children = formatRoutes(children)
    // 单独对某个路由格式化
    let fmtRouter = {
      path,
      name,
      iconCls,
      children,
      component: () => {
        // 判断组件以什么开头，然后去对应目录寻找
        if (component.startsWith('Home')) {
         return import(`@/views/home/${component}.vue`)
        } else if (component.startsWith('Emp')) {
         return import(`@/views/emp/${component}.vue`)
        } else if (component.startsWith('Per')) {
          return import(`@/views/per/${component}.vue`)
        } else if (component.startsWith('Sal')) {
          return import(`@/views/sal/${component}.vue`)
        } else if (component.startsWith('Sys')) {
          return import(`@/views/sys/${component}.vue`)
        }
      }

    }
    fmtRoutes.push(fmtRouter)
  });
  return fmtRoutes
}