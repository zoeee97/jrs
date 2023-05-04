<!--
 * @Author: zyy 1741540959@qq.com
 * @Date: 2023-01-11 16:25:13
 * @LastEditors: zyy
 * @LastEditTime: 2023-02-23 22:26:10
 * @Description: file content
-->
<template>
  <div id="app">
    <div class="sidebar">
      <card></card>
      <list></list>
    </div>
    <div class="main">
      <message></message>
      <userText></userText>
    </div>
  </div>
</template>

<script setup lang="ts">
import card from '@/components/chat/card.vue'
import list from '@/components/chat/list.vue'
import message from '@/components/chat/message.vue'
import userText from '@/components/chat/usertext.vue'
import { store } from '@/stores/index'

const pinia = store()
pinia.init_data()
</script>

<style lang="scss" scoped>
#app {
  margin: 20px auto;
  width: 600px;
  height: 500px;
  overflow: hidden;
  border-radius: 10px;
  border: 1px solid #c8c9c9;
  display: flex;
  .sidebar, .main {
    height: 100%;
  }
  .sidebar {
    // float: left;
    flex: 1;
    color: #f4f4f4;
    background-color: #2e3238;
    width: 200px;
  }
  .main {
    // position: relative;
    flex: 2;
    overflow: hidden;
    background-color: #eee;
  }
}
</style>