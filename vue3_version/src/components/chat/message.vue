<!--
 * @Author: zyy 1741540959@qq.com
 * @Date: 2023-02-21 16:01:32
 * @LastEditors: zyy
 * @LastEditTime: 2023-03-04 19:54:29
 * @Description: file content
-->
<template>
  <div id="message" class="wrapper" ref="wrapper">
    <ul class="content">
      <div v-if="currentSession">
        <li v-for="entry in sessions[currentAdmin.username + '#' + currentSession.username]">
          <p class="time">
            <span>{{ filterTime(entry.date) }}</span>
          </p>
          <div class="main" :class="{ self: entry.self }">
            <img class="avatar" :src="entry.self ? currentAdmin.userFace : currentSession.userFace" alt="">
            <p class="text">{{ entry.content }}</p>
          </div>
        </li>
      </div>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { store } from '@/stores';
import { storeToRefs } from 'pinia';
import { getCurrentInstance, nextTick, onMounted, onUpdated } from 'vue';
import BScroll from '@better-scroll/core';
import MouseWheel from '@better-scroll/mouse-wheel'
BScroll.use(MouseWheel)
const pinia = store();

let { currentAdmin, currentSession, sessions } = storeToRefs(pinia);
let scroll: any = null;
let instance:any =null;
/* 
vue3取消了Vue2的过滤器
filters: {
    time(date) {
      if (date) {
        date = new Date(date);
      }
      return `${date.getHours()}:${date.getMinutes()}`;
    }
  }, */
function filterTime(date: any) {
  if (date) {
    date = new Date(date);
  }
  return `${date.getHours()}:${date.getMinutes()}`;
}

onMounted(() => {
  // 获取当前实例
  instance = getCurrentInstance();
  let wrapper = instance?.refs.wrapper
  scroll = new BScroll(wrapper as any, {
    probeType: 2,//屏幕滑动过程中实时派发scroll事件
    scrollY: true,
    mouseWheel: {
      speed: 20,
      invert: false,
      easeTime: 300
    }
  })
  // 监听事件scroll
  scroll.on("scroll", (position: any) => {
    console.log(position);
  })
  nextTick(() => {
    console.log("aaaa");
    scroll.scrollTo(0, scroll.maxScrollY)
  })
})

onUpdated(() => {
    scroll.refresh()
    scroll.scrollTo(0, scroll.maxScrollY,300)
})



</script>

<style lang="scss" scoped>
#message {
  padding: 15px;
  // max-height: 68%;
  height: 70%;
  overflow-y: hidden;
  // overflow: hidden;

  ul {
    list-style-type: none;
    padding-left: 0;

    li {
      margin-bottom: 15px;
    }
  }

  .time {
    text-align: center;
    margin: 7px 0;

    >span {
      display: inline-block;
      padding: 0 18px;
      font-size: 12px;
      color: #FFF;
      background-color: #dcdcdc;
      border-radius: 2px;
    }
  }

  .main {
    .avatar {
      float: left;
      margin: 0 10px 0 0;
      border-radius: 3px;
      width: 30px;
      height: 30px;

    }

    .text {
      display: inline-block;
      padding: 0 10px;
      max-width: 80%;
      background-color: #fafafa;
      border-radius: 4px;
      line-height: 30px;
    }
  }

  .self {
    text-align: right;

    .avatar {
      float: right;
      margin: 0 0 0 10px;
      border-radius: 3px;
      width: 30px;
      height: 30px;
    }

    .text {
      display: inline-block;
      padding: 0 10px;
      max-width: 80%;
      background-color: #b2e281;
      border-radius: 4px;
      line-height: 30px;
    }
  }
}
</style>
