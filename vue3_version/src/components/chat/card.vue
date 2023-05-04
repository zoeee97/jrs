<!--
 * @Author: zyy 1741540959@qq.com
 * @Date: 2023-02-21 16:01:32
 * @LastEditors: zyy
 * @LastEditTime: 2023-02-23 16:08:11
 * @Description: file content
-->
<template>
  <div id="card">
  	<header>
			<!-- 绑定当前用户的头像和名字 -->
  		<img class="avatar" v-bind:src="currentAdmin.userFace" v-bind:alt="currentAdmin.name">
  		<p class="name">{{currentAdmin.name}}</p>
  	</header>
  	<footer>
  		<input class="search" type="text" v-model="pinia.filterKey" placeholder="search user...">
  	</footer>
  </div>
</template>

<script setup lang="ts">
import { store } from '@/stores';
import { storeToRefs } from 'pinia';
const pinia = store();

let {currentAdmin} = storeToRefs(pinia);
</script>

<style lang="scss" scoped>
#card {
	padding: 12px;
  .avatar{
  	width: 40px;
  	height: 40px;
  	vertical-align: middle;/*这个是图片和文字居中对齐*/
  }
  .name {
  	display: inline-block;
  	padding: 10px;
  	margin-bottom: 15px;
  	font-size: 16px;
  }
  .search {
  	background: #26292E;
  	height: 30px;
  	line-height: 30px;
  	padding: 0 10px;
  	border: 1px solid #3a3a3a;
  	border-radius: 4px;
  	outline: none;/*鼠标点击后不会出现蓝色边框*/
    color: #FFF;
  }
}
</style>
