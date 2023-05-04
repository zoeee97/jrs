<!--
 * @Author: zyy 1741540959@qq.com
 * @Date: 2023-02-21 16:01:32
 * @LastEditors: zyy
 * @LastEditTime: 2023-02-24 17:22:15
 * @Description: file content
-->
<template>
  <div id="uesrtext">
    <textarea placeholder="按Enter 发送" v-model="content" @keyup.enter="addMessage"></textarea>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import { store } from '@/stores';
import { storeToRefs } from 'pinia';
import type { MsgObj } from '@/types/emp/chat';
const pinia = store();

let {currentSession} = storeToRefs(pinia);
let content = ref('')
let msg:MsgObj = reactive({
  to:'',
  content:'',
  notSelf:false
})
// @click 等事件 参数的 TS 类型的定义方法 Event
function addMessage(e:Event) {
  if (content.value!='') {
    msg.to = currentSession.value!.username;
    msg.content = content.value
    pinia.stomp!.publish({
      destination:'/ws/chat', //发送给服务端的消息
      body:JSON.stringify(msg)//body只能是字符串
    })
    pinia.addMessage(msg);
    content.value = '';
  }
}
</script>

<style lang="scss" scoped>
#uesrtext {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 100%;
  height: 30%;
  border-top: solid 1px #DDD;

  > textarea {
    padding: 10px;
    width: 100%;
    height: 100%;
    border: none;
    outline: none;
  }
}
</style>