<!--
 * @Author: zyy 1741540959@qq.com
 * @Date: 2023-01-11 16:25:29
 * @LastEditors: zyy
 * @LastEditTime: 2023-03-04 17:12:16
 * @Description: file content
-->
<template>
  <div>
    <Suspense>
      <!-- 放子组件 -->
      <template #default>
        <empTable :nations="nations" :joblevels="joblevels" :politicsstatus="politicsstatus" :allDeps="allDeps"></empTable>
      </template>
      <!-- 放子组件加载之前出现的内容 -->
      <template #fallback>
        <h3>稍等，加载中</h3>
      </template>
    </Suspense>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive,defineAsyncComponent } from 'vue'
import api from '@/api/request'
import type { Nations,JobLevel,PoliticsStatus,Deps } from '@/types/emp/emp'
// 异步组件的引入方式
const empTable = defineAsyncComponent(()=>import('../../components/emp/empTable.vue'))

export default defineComponent({
  components:{empTable},
  setup () {
    // 需要一个空数组来接受来自api的数据 手动定义
    let nations = reactive<Array<Nations>>([])
    let joblevels = reactive<Array<JobLevel>>([])
    let politicsstatus = reactive<Array<PoliticsStatus>>([])
    let allDeps = reactive<Array<Deps>>([])
    // 不怎么变动的数据直接缓存 减少请求频率
    function initData() {
      // 获取民族数据:先从 sessionStorage 里取，取不到再调用接口获取数据
      if (!window.sessionStorage.getItem("nations")) {
        api.get('/employee/basic/nation').then((resp:any) => {
          if(resp){
            nations = resp
            // 存到 sessionStorage 里,把对象转字符串
            window.sessionStorage.setItem('nations', JSON.stringify(resp))
          }
        })
      } else {
        // 从 sessionStorage 获取，字符串转对象
        nations = JSON.parse(window.sessionStorage.getItem('nations') as string)
      }
      // 获取职称
      if (!window.sessionStorage.getItem('joblevels')) {
        api.get('/employee/basic/joblevel').then((resp:any) => {
          if (resp) {
            joblevels = resp
            window.sessionStorage.setItem('joblevels', JSON.stringify(resp))
          }
        })
      } else {
        // 从 sessionStorage 获取，字符串转对象
        joblevels = JSON.parse(window.sessionStorage.getItem('joblevels') as string)
      }
      // 获取政治面貌
      if (!window.sessionStorage.getItem('politicsstatus')) {
        api.get('/employee/basic/politicsStatus').then((resp:any) => {
          if (resp) {
            politicsstatus = resp
            window.sessionStorage.setItem('politicsstatus', JSON.stringify(resp))
          }
        })
      } else {
        // 从 sessionStorage 获取，字符串转对象
        politicsstatus = JSON.parse(window.sessionStorage.getItem('politicsstatus') as string)
      }
      // 23-22 树形控件 绑定 所属部门 数据对象
      if (!window.sessionStorage.getItem('allDeps')) {
        api.get('/employee/basic/deps').then((resp:any) => {
          if (resp) {
            allDeps = resp
            window.sessionStorage.setItem('allDeps', JSON.stringify(resp))
          }
        })
      } else {
        allDeps = JSON.parse(window.sessionStorage.getItem('allDeps') as string)
      }
    }
    initData()
    return {
      nations,
      joblevels,
      politicsstatus,
      allDeps
    }
  }
})
</script>

<style scoped>

</style>