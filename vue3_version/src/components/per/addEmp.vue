<!--
 * @Author: zyy 1741540959@qq.com
 * @Date: 2023-02-07 16:31:14
 * @LastEditors: zyy
 * @LastEditTime: 2023-02-20 17:17:42
 * @Description: file content
-->
<template>
  <div>
    <!-- 开始- 添加员工弹框 -->
    <!-- 编辑员工 将添加员工弹框标题改为变量 根据条件显示是添加还是编辑 :title="title"  -->
    <el-dialog :title="title" v-model="dialogVisible" width="80%">
      <div>
        <!-- <el-row  <el-form -->
        <!-- 数据校验对象 :rules="empRules" ，每项属性对应 prop="posId" -->
        <el-form ref="empRef" :model="emp" :rules="empRules">
          <el-row>
            <el-col :span="6">
              <el-form-item label="姓名：" prop="name">
                <el-input v-model="emp.name" :prefix-icon="Edit" placeholder="请输入员工姓名" size="small"
                  style="width: 150px;"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="性别：" prop="gender">
                <el-radio-group v-model="emp.gender" style="margin-top: 8px;">
                  <el-radio label="男">男</el-radio>
                  <el-radio label="女">女</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="出生日期：" prop="birthday">
                <el-date-picker v-model="emp.birthday" type="date" value-format="YYYY-MM-DD" format="YYYY-MM-DD" size="small" 
                  style="width: 150px;" placeholder="出生日期" >
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <!-- 添加员工 给每项赋值 -->
              <el-form-item label="政治面貌：" prop="politicId">
                <el-select v-model="emp.politicId" placeholder="请选择政治面貌" size="small" style="width: 200px;">
                  <el-option v-for="item in politicsstatus" :key="item.id" :label="item.name" :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="民族：" prop="nationId">
                <el-select v-model="emp.nationId" placeholder="民族" size="small" style="width: 150px;">
                  <el-option v-for="item in nations" :key="item.id" :label="item.name" :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="籍贯：" prop="nativePlace">
                <el-input v-model="emp.nativePlace" placeholder="请输入籍贯" :prefix-icon="Edit" size="small"
                  style="width: 120px;"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="电子邮箱：" prop="email">
                <el-input v-model="emp.email" placeholder="请输入电子邮箱" :prefix-icon="Message" size="small"
                  style="width: 150px;"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="联系地址：" prop="address">
                <el-input v-model="emp.address" placeholder="请输入联系地址" :prefix-icon="Edit" size="small"
                  style="width: 200px;"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="职位：" prop="posId">
                <el-select v-model="emp.posId" placeholder="职位" size="small" style="width: 150px;">
                  <el-option v-for="item in pinia.positions" :key="item.id" :label="item.name" :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="职称：" prop="jobLevelId">
                <el-select v-model="emp.jobLevelId" placeholder="职称" size="small" style="width: 150px;">
                  <el-option v-for="item in joblevels" :key="item.id" :label="item.name" :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="所属部门：" prop="departmentId">
                <!-- manual 手动弹出框 -->
                <el-popover placement="bottom" title="请选择部门" width="200" trigger="manual" :visible="visible">
                  <!-- 添加树形控件 default-expand-all	是否默认展开所有节点 ，节点点击事件 @node-click="handleNodeClick" -->
                  <el-tree :data="allDeps" :props="defaultProps" default-expand-all
                    @node-click="handleNodeClick"></el-tree>
                  <!-- node-click	节点被点击时的回调 共三个参数，依次为：传递给 data 属性的数组中该节点所对应的对象、节点对应的 Node、节点组件本身。 -->
                  <!-- 自定义点击事件 -->
                  <template #reference>
                    <div style="width:150px;display: inline-flex;
                       border-radius: 5px;border: 1px solid #dedede;height: 28px;cursor: pointer;align-items: center;
                       font-size: 12px;padding-left: 8px;box-sizing: border-box;" @click="showDepView">
                      {{inputDepName}}
                    </div>
                  </template><!--  回显数据 {{inputDepName}} -->
                </el-popover>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="电话号码：" prop="phone">
                <el-input v-model="emp.phone" placeholder="请输入电话号码" size="small" style="width: 200px;"
                  :prefix-icon="Phone"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="工号：" prop="workID">
                <el-input v-model="emp.workID" placeholder="请输入工号" size="small" style="width: 150px;"
                  :prefix-icon="Edit" disabled></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="学历：" prop="tiptopDegree">
                <el-select v-model="emp.tiptopDegree" placeholder="学历" size="small" style="width: 150px;">
                  <el-option v-for="item in tiptopDegrees" :key="item" :label="item" :value="item">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="毕业院校：" prop="school">
                <el-input v-model="emp.school" placeholder="请输入学校" size="small" style="width: 150px;"
                  :prefix-icon="Edit"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="专业名称：" prop="specialty">
                <el-input v-model="emp.specialty" placeholder="请输入专业名称" size="small" style="width: 200px;"
                  :prefix-icon="Edit"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="入职日期：" prop="beginDate">
                <el-date-picker v-model="emp.beginDate" type="date"  value-format="YYYY-MM-DD" format="YYYY-MM-DD" size="small"
                  style="width: 120px;" placeholder="入职日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="转正日期：" prop="conversionTime">
                <el-date-picker v-model="emp.conversionTime" type="date"  value-format="YYYY-MM-DD" format="YYYY-MM-DD" size="small"
                  style="width: 120px;" placeholder="转正日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="合同起始日期：" prop="beginContract">
                <el-date-picker v-model="emp.beginContract" type="date"  value-format="YYYY-MM-DD" format="YYYY-MM-DD" size="small"
                  style="width: 135px;" placeholder="合同起始日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="合同截止日期：" prop="endContract">
                <el-date-picker v-model="emp.endContract" type="date"  value-format="YYYY-MM-DD" format="YYYY-MM-DD" size="small"
                  style="width: 170px;" placeholder="合同截止日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="身份证号码：" prop="idCard">
                <el-input v-model="emp.idCard" placeholder="请输入身份证号码" size="small" :prefix-icon="Edit"
                  style="width: 180px;"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="聘用形式：" prop="engageForm">
                <el-radio-group v-model="emp.engageForm" style="margin-top: 8px;">
                  <el-radio label="劳动合同">劳动合同</el-radio>
                  <el-radio label="劳务合同">劳务合同</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="婚姻状况：" prop="wedlock">
                <el-radio-group v-model="emp.wedlock" style="margin-top: 8px;">
                  <el-radio label="未婚">未婚</el-radio>
                  <el-radio label="已婚">已婚</el-radio>
                  <el-radio label="离异">离异</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <!-- 23-26 @click="doAddEmp"-->
        <el-button type="primary" @click="doAddEmp(empRef)">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import type { EmpState,Nations,JobLevel,PoliticsStatus,Deps } from '@/types/emp/emp'
import { store } from '@/stores';
import { reactive,ref, type Ref } from 'vue';
import type { FormInstance, FormRules } from 'element-plus'
import api from '@/api/request'
import { storeToRefs } from 'pinia';
import { Edit,Phone,Message } from '@element-plus/icons-vue'

defineProps({
    nations:{ type:Array<Nations> },
    joblevels:{ type:Array<JobLevel>},
    politicsstatus:{ type:Array<PoliticsStatus>},
    allDeps:{ type:Array<Deps>},
  })

const pinia = store()
let { emp,title,dialogVisible,inputDepName } = storeToRefs(pinia)
let visible:Ref<boolean> = ref(false)
// vue3+element-plus用ref获取元素节点的方法
const empRef = ref<FormInstance>()
//表单数据校验
const empRules = reactive<FormRules>({
  name: [{ required: true, message: '请输入员工名', trigger: 'blur' }],
  gender: [{ required: true, message: '请输入员工性别', trigger: 'blur' }],
  birthday: [{ required: true, message: '请输入出生日期', trigger: 'blur' }],
  idCard: [{ required: true, message: '请输入身份证号码', trigger: 'blur' },
  {
    pattern: /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$)/,
    message: '身份证号码不正确', trigger: 'blur'
  }],
  wedlock: [{ required: true, message: '请输入婚姻状况', trigger: 'blur' }],
  nationId: [{ required: true, message: '请输入民族', trigger: 'blur' }],
  nativePlace: [{ required: true, message: '请输入籍贯', trigger: 'blur' }],
  politicId: [{ required: true, message: '请输入政治面貌', trigger: 'blur' }],
  email: [{ required: true, message: '请输入邮箱地址', trigger: 'blur' },
  { type: 'email', message: '邮箱地址格式不正确', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入电话号码', trigger: 'blur' },
  {
    pattern: /^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/,
    message: '请输入合法手机号码', trigger: 'blur'
  }],

  address: [{ required: true, message: '请输入地址', trigger: 'blur' }],
  departmentId: [{ required: true, message: '请输入部门名称', trigger: 'blur' }],
  jobLevelId: [{ required: true, message: '请输入职称', trigger: 'blur' }],
  posId: [{ required: true, message: '请输入职位', trigger: 'blur' }],
  engageForm: [{ required: true, message: '请输入聘用形式', trigger: 'blur' }],
  tiptopDegree: [{ required: true, message: '请输入学历', trigger: 'blur' }],
  specialty: [{ required: true, message: '请输入专业', trigger: 'blur' }],
  school: [{ required: true, message: '请输入毕业院校', trigger: 'blur' }],
  beginDate: [{ required: true, message: '请输入入职日期', trigger: 'blur' }],
  workState: [{ required: true, message: '请输入工作状态', trigger: 'blur' }],
  workID: [{ required: true, message: '请输入工号', trigger: 'blur' }],
  contractTerm: [{ required: true, message: '请输入合同期限', trigger: 'blur' }],
  conversionTime: [{ required: true, message: '请输入转正日期', trigger: 'blur' }],
  notworkDate: [{ required: true, message: '请输入离职日期', trigger: 'blur' }],
  beginContract: [{ required: true, message: '请输入合同起始日期', trigger: 'blur' }],
  endContract: [{ required: true, message: '请输入合同结束日期', trigger: 'blur' }],
  workAge: [{ required: true, message: '请输入工龄', trigger: 'blur' }]
})
const tiptopDegrees = ['博士', '硕士', '本科', '大专', '高中', '初中', '小学', '其它']
const defaultProps = {
  children: 'children',
  label: 'name'
}

// 添加和编辑这里就请求方式不一样 putRequest postRequest ，其它的都一样
async function doAddEmp(formEL:FormInstance | undefined) {
  if(!formEL) return;
  if (emp.value.id!='') {
    // 有 id 编辑员工
    await formEL.validate((valid,fields) => {
      if (valid) {
        // 如果直接用storeToRefs出来的数据作为参数去发请求会出现大问题！！
        api.put('/employee/basic/', pinia.emp).then(resp => {
          if (resp) {
            dialogVisible.value = false
            pinia.initEmps()
          }
        })
      }else {
        console.log('error submit!',fields);
      }
    })
  } else {
    // 没有id 添加员工
    // empRef 表单中定义的引用对象 ref="empRef"
    await formEL.validate((valid,fields) => {
      if (valid) {
        api.post('/employee/basic/', pinia.emp).then(resp => {
          if (resp) {
            dialogVisible.value = false
            pinia.initEmps()
          }
        })
      }else{
        console.log('error submit!',fields);
      }
    })
  }
}
// 树控件节点点击事件
function handleNodeClick(data:Deps) {
  inputDepName.value = data.name
  emp.value.departmentId = data.id.toString()
  showDepView() // 弹框
}

function showDepView(){
  visible.value = !visible.value
}
</script>

<style scoped>

</style>