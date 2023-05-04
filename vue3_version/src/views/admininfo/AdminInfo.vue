<!--
 * @Author: zyy 1741540959@qq.com
 * @Date: 2023-01-11 16:22:02
 * @LastEditors: zyy
 * @LastEditTime: 2023-02-23 22:05:23
 * @Description: file content
-->
<template>
  <div>
    <el-card class="box-card" style="width: 400px;" v-if="data.admin">
      <div slot="header" class="clearfix">
        <span>{{ data.admin!.name }}</span>
      </div>
      <div>
        <div>
          <div style="display: flex;justify-content: center;">
            <img title="点击修改用户头像" :src="data.admin!.userFace" style="height: 100px;width: 100px;border-radius: 50px;" alt="">
          </div>
          <div>电话号码：
            <el-tag>{{ data.admin.telephone }}</el-tag>
          </div>
          <div>手机号码：
            <el-tag>{{ data.admin.phone }}</el-tag>
          </div>
          <div>居住地址：
            <el-tag>{{ data.admin.address }}</el-tag>
          </div>
          <div>用户标签：
            <el-tag type="success" v-for="(r, index) in data.admin.roles" :key="index">{{ r.nameZh }}</el-tag>
          </div>
        </div>
        <div style="display: flex;justify-content: space-around;margin-top: 10px;">
          <!-- @click="showUpdateAdminInfoView" -->
          <el-button type="primary" size="small" @click="showUpdateAdminInfoView">修改信息</el-button>
          <!-- 用户修改密码 @click="showUpdatePasswordView"  -->
          <el-button type="danger" size="small" @click="showUpdatePasswordView">修改密码</el-button>
        </div>
      </div>
    </el-card>
    <!--编辑用户信息 -->
    <el-dialog title="编辑用户信息" v-model="data.dialogVisible" width="30%">
      <div>
        <table>
          <tr>
            <td>用户昵称：</td>
            <td>
              <!-- 重新给每项赋值 admin2 -->
              <el-input v-model="data.admin2.name"></el-input>
            </td>
          </tr>
          <tr>
            <td>电话号码：</td>
            <td>
              <el-input v-model="data.admin2.telephone"></el-input>
            </td>
          </tr>
          <tr>
            <td>手机号码：</td>
            <td>
              <el-input v-model="data.admin2.phone"></el-input>
            </td>
          </tr>
          <tr>
            <td>用户地址：</td>
            <td>
              <el-input v-model="data.admin2.address"></el-input>
            </td>
          </tr>
        </table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="data.dialogVisible = false">取 消</el-button>
        <!-- @click="updateAdminInfo" -->
        <el-button type="primary" @click="updateAdminInfo">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 修改密码 -->
    <el-dialog title="更新密码" v-model="data.passwordDialogVisible" width="30%">
      <div>
        <!-- 调整修改密码表单 -->
        <el-form :model="ruleForm" status-icon :rules="rules" ref="RefForm" label-width="100px" class="demo-ruleForm">
          <el-form-item label="请输入旧密码" prop="oldPass">
            <el-input type="password" v-model="ruleForm.oldPass" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="请输入新密码" prop="pass">
            <el-input type="password" v-model="ruleForm.pass" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="确认新密码" prop="checkPass">
            <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm(RefForm)">提交</el-button>
            <el-button @click="resetForm(RefForm)">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import type { FormInstance, FormRules } from 'element-plus';
import api from '@/api/request'
import { reactive, ref } from 'vue';
import { store } from '@/stores/index'
import { useRouter } from 'vue-router'
import type { AdminRole } from '@/types/sys/sys'

const userStore = store()
const router = useRouter()
const RefForm = ref<FormInstance>()
//表单数据校验
const ruleForm = reactive({
  pass: '',
  checkPass: '',
  oldPass: '',
  adminId:''
})
const validatePass = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请输入密码'))
  } else {
    if (ruleForm.checkPass !== '') {
      if (!RefForm.value) return
      RefForm.value.validateField('checkPass', () => null)
    }
    callback()
  }
}
const validatePass2 = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== ruleForm.pass) {
    callback(new Error("两次输入密码不一致!"))
  } else {
    callback()
  }
}
const rules = reactive<FormRules>({
  pass: [{ validator: validatePass, trigger: 'blur' }],
  checkPass: [{ validator: validatePass2, trigger: 'blur' }],
  oldPass: [{ validator: validatePass, trigger: 'blur' }],
})

interface DataState {
  admin: AdminRole | null,
  admin2: AdminRole | null, // 编辑的对象
  dialogVisible: boolean, 
  passwordDialogVisible: boolean, // 修改密码
}
let data = reactive<DataState>({
  admin: null,
  admin2: null, // 编辑的对象
  dialogVisible: false, // 编辑用户信息
  passwordDialogVisible: false, // 修改密码
})

function initAdmin() {
  api.get('/admin/info').then((resp: any) => {
    if (resp) {
      data.admin = resp
      console.log(data.admin);
      data.admin2 = Object.assign({}, data.admin) // 对象拷贝给 admin2
      window.sessionStorage.setItem('currentAdmin', JSON.stringify(resp))
      // data.$store.commit('INIT_ADMIN', resp)
    }
  })
}
initAdmin()
// 预校验 提交表单
function submitForm(formName: FormInstance | undefined) {
  if (!formName) return;
  formName.validate((valid) => {
    if (valid) {
      ruleForm.adminId = (data.admin!.id).toString()
      api.put('/admin/pass', ruleForm).then(resp => {
        if (resp) {
          // 更新密码成功后 退出登录
          api.post('/logout') // 退出登录
          window.sessionStorage.removeItem('currentAdmin')
          window.sessionStorage.removeItem('token')
          userStore.routes = [] // 初始化路由 菜单 置空
          router.replace('/') // 跳到登录页面

        }
      })
    } else {
      console.log('error submit!!');
      return false;
    }
  });
}
// 重置表单
function resetForm(formName: FormInstance | undefined) {
  formName!.resetFields();
}
// 修改密码
function showUpdatePasswordView() {
  data.passwordDialogVisible = true
}
// 更新用户
function updateAdminInfo() {
  if(!data.admin2) return
  api.put('/admin/info', data.admin2).then((resp: any) => {
    if (resp) {
      data.dialogVisible = false
      initAdmin()
    }
  })
}
// 编辑用户信息弹框
function showUpdateAdminInfoView() {
  data.dialogVisible = true
}

</script>

<style scoped></style>