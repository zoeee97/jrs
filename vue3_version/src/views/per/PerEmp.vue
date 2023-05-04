<template>
  <div>
    <!-- <KeepAlive> -->
    <searchEmp :nations="data.nations" :joblevels="data.joblevels" :politicsstatus="data.politicsstatus"
      :allDeps="data.allDeps"></searchEmp>
    <!-- </KeepAlive> -->
    <Suspense>
      <template #default>
        <empTable></empTable>
      </template>
      <template #fallback>
        <h3>表格加载中</h3>
      </template>
    </Suspense>
    <Suspense v-if="pinia.dialogVisible">
      <template #default>
        <!-- 需要注意的是 异步注册的组件 无法正确获取ref对象 -->
        <addEmp :nations="data.nations" :joblevels="data.joblevels" :politicsstatus="data.politicsstatus"
          :allDeps="data.allDeps"></addEmp>
      </template>
      <template #fallback>
        <h3>表格加载中</h3>
      </template>
    </Suspense>
  </div>
</template>

<script setup lang="ts">
import { defineAsyncComponent, reactive } from 'vue';
import type { Nations, JobLevel, PoliticsStatus, Deps } from '@/types/emp/emp'
import api from '@/api/request'
import searchEmp from '@/components/per/searchEmp.vue';
import { store } from '@/stores';
// setup语法糖直接引入组件就能用，不用通过components注册了
const empTable = defineAsyncComponent(() => import('@/components/per/empTable.vue'))
const addEmp = defineAsyncComponent(() => import('@/components/per/addEmp.vue'))

const pinia = store()

interface DataState {
  nations: Array<Nations>,
  joblevels: Array<JobLevel>,
  politicsstatus: Array<PoliticsStatus>,
  allDeps: Array<Deps>,
}

let data = reactive<DataState>({
  nations: [],
  joblevels: [],
  politicsstatus: [],
  allDeps: []
})
function initData() {
  // 获取民族数据:先从 sessionStorage 里取，取不到再调用接口获取数据
  if (!window.sessionStorage.getItem("nations")) {
    api.get('/employee/basic/nation').then((resp: any) => {
      if (resp) {
        // 作为reactive对象的属性去修改就不会失去响应性 直接赋值就会失去
        data.nations = resp
        // 存到 sessionStorage 里,把对象转字符串
        window.sessionStorage.setItem('nations', JSON.stringify(resp))
      }
    })
  } else {
    // 从 sessionStorage 获取，字符串转对象
    data.nations = JSON.parse(window.sessionStorage.getItem('nations') as string)
  }
  // 获取职称
  if (!window.sessionStorage.getItem('joblevels')) {
    api.get('/employee/basic/joblevel').then((resp: any) => {
      if (resp) {
        data.joblevels = resp
        window.sessionStorage.setItem('joblevels', JSON.stringify(resp))
      }
    })
  } else {
    // 从 sessionStorage 获取，字符串转对象
    data.joblevels = JSON.parse(window.sessionStorage.getItem('joblevels') as string)
  }
  // 获取政治面貌
  if (!window.sessionStorage.getItem('politicsstatus')) {
    api.get('/employee/basic/politicsStatus').then((resp: any) => {
      if (resp) {
        data.politicsstatus = resp
        window.sessionStorage.setItem('politicsstatus', JSON.stringify(resp))
      }
    })
  } else {
    // 从 sessionStorage 获取，字符串转对象
    data.politicsstatus = JSON.parse(window.sessionStorage.getItem('politicsstatus') as string)
  }
  // 树形控件 绑定 所属部门 数据对象
  if (!window.sessionStorage.getItem('allDeps')) {
    api.get('/employee/basic/deps').then((resp: any) => {
      if (resp) {
        data.allDeps = resp
        window.sessionStorage.setItem('allDeps', JSON.stringify(resp))
      }
    })
  } else {
    data.allDeps = JSON.parse(window.sessionStorage.getItem('allDeps') as string)
  }
}
initData()
</script>

<style>
.slide-fade-enter-active {
  transition: all .8s ease;
}

.slide-fade-leave-active {
  transition: all .8s cubic-bezier(1.0, 0.5, 0.8, 1.0);
}

.slide-fade-enter,
.slide-fade-leave-to

/* .slide-fade-leave-active for below version 2.1.8 */
  {
  transform: translateX(10px);
  opacity: 0;
}
</style>