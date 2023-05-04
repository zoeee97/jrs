<!--
 * @Author: zyy 1741540959@qq.com
 * @Date: 2023-01-11 16:21:48
 * @LastEditors: zyy
 * @LastEditTime: 2023-02-14 09:19:30
 * @Description: file content
-->
<template>
  <div class="wrap">
    <el-card class="box-card">
      <div class="card-header">
        <span>登录</span>
      </div>
      <div class="form">
        <el-form :model="form" label-width="80px">
          <el-form-item label="用户名">
            <el-input v-model="form.username"></el-input>
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="form.password"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="login">登录</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
// 导入我们自定义的store
import { store } from '@/stores/index'
import api from '@/api/request'
interface Form {
  username: string,
  password: string
}

export default defineComponent({
  setup() {
    // 调用函数创建store
    const userStore = store()
    
    const router = useRouter()
    const route = useRoute()
    // 将store中的状态解构出来
    // const { token } = storeToRefs(userStore)
    let form = reactive<Form>({
      username: '',
      password: ''
    })
    function login() {
      api.post('/login', form).then((resp: any) => {
        if (resp.code == 200) {
          const tokenStr = resp.obj.tokenHead + resp.obj.token;
          userStore.token = tokenStr
          // 路由跳转至首页
          router.replace({path:'/home'})//replace是路径替换但无法回退到上一页
        }
      }).catch(e => {
        console.log(e);
      })
    }
    return {
      form,
      login
    }
  }
})
</script>

<style scoped lang="scss">
.wrap {
  width: 100vw;
  height: 100vh;
  background-color: rgb(148, 211, 203);
  position: relative;
}

.box-card {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
}

.card-header {
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.box-card {
  width: 350px;
  height: 200px;
}

.el-button {
  margin-left: 20%;
}
</style>