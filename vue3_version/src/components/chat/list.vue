<!--
 * @Author: zyy 1741540959@qq.com
 * @Date: 2023-02-21 16:01:32
 * @LastEditors: zyy
 * @LastEditTime: 2023-02-23 22:29:42
 * @Description: file content
-->
<template>
  <div id="list">
    <ul style="padding-left: 0;">
      <!-- 如果currentSession不存在直接给false 存在就判断一下用户名是否相同 -->
      <li v-for="item in admins" :class="{ active: currentSession ? item.username === currentSession.username : false }"
        v-on:click="pinia.changeCurrentSession(item)">
        <!--         未读消息提示 小红点  <el-badge is-dot> </el-badge>-->
        <el-badge :is-dot="idDot[currentAdmin.username + '#' + item.username]"><img class="avatar" :src="item.userFace"
            :alt="item.name"></el-badge>
        <!--        <img class="avatar" :src="item.userFace" :alt="item.name">-->
        <p class="name">{{ item.name }}</p>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { store } from '@/stores';
import { storeToRefs } from 'pinia';
const pinia = store();

let {currentAdmin,currentSession,idDot,admins} = storeToRefs(pinia);
</script>

<style lang="scss" scoped>
#list {
  li {
    padding: 8px 15px;
    border-bottom: 1px solid #292C33;
    cursor: pointer;

    &:hover {
      background-color: rgba(255, 255, 255, 0.03);
    }
  }

  li.active {
    /*注意这个是.不是冒号:*/
    background-color: rgba(255, 255, 255, 0.1);
  }

  .avatar {
    border-radius: 2px;
    width: 30px;
    height: 30px;
    vertical-align: middle;
  }

  .name {
    display: inline-block;
    margin-left: 15px;
  }
}
</style>
