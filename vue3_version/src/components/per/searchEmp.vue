<template>
  <div>
    <div>
      <div style="display: flex;justify-content: space-between;">
        <!-- 搜索 v-model="empName" <el-input @keydown.enter.native="initEmps" 回车键调用初始化会员方法
             @click="initEmps">搜索</el-button>
             清空 clearable @clear="initEmps" -->
        <!--   :disabled="data.showAdvanceSearchVisible" -->
        <div style="margin-top: 10px;">
          <el-input style="width: 300px;margin-right: 10px;" :prefix-icon="Search" v-model="empSelect.name"
            placeholder="请输入员工名进行搜索..." @keyup.enter="initEmps" clearable @clear="initEmps"
            :disabled="data.showAdvanceSearchVisible"></el-input>
          <el-button type="primary" :icon="Search" @click="initEmps" :disabled="data.showAdvanceSearchVisible">搜索
          </el-button>
          <!-- @click="data.showAdvanceSearchVisible = !data.showAdvanceSearchVisible" -->
          <!-- 判断图标样式 :class="data.showAdvanceSearchVisible?'fa fa-angle-double-up':'fa fa-angle-double-down'"-->
          <el-button type="primary" @click="data.showAdvanceSearchVisible = !data.showAdvanceSearchVisible">
            <component :is="data.showAdvanceSearchVisible ? ArrowUp : ArrowDown" aria-hidden="true"></component>高级搜索
          </el-button>
        </div>
        <div>
          <!-- 导出数据 @click="exportData" -->
          <el-button type="success" @click="exportData"><i class="el-icon-download" aria-hidden="true"></i>&nbsp; 导出数据
          </el-button>
          <!-- @click="showAddEmpView" -->
          <el-button type="primary" icon="el-icon-plus" @click="pinia.showAddEmpView()">添加员工</el-button>
        </div>
      </div>
      <!-- 高级搜索条件框 -->
      <!-- 高级搜索条件框 v-show="data.showAdvanceSearchVisible" -->
      <!-- 添加展开动画效果 <transition name="fade"> 包含整个搜索条件框 </transition> -->
      <!-- 绑定搜索条件数据 v-model="searchValue.xxxxx" -->
      <transition name="slide-fade">
        <div v-show="data.showAdvanceSearchVisible"
          style="border: 1px solid #379ff5;border-radius: 5px;box-sizing: border-box;padding: 5px;margin: 10px 0;">
          <el-row>
            <el-col :span="5">
              政治面貌：
              <el-select v-model="empSelect.politicId" placeholder="请选择政治面貌" size="small" style="width: 130px;">
                <el-option v-for="item in politicsstatus" :key="item.id" :label="item.name" :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              民族：
              <el-select v-model="empSelect.nationId" placeholder="民族" size="small" style="width: 130px;">
                <el-option v-for="item in nations" :key="item.id" :label="item.name" :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              职位：
              <el-select v-model="empSelect.posId" placeholder="职位" size="small" style="width: 130px;">
                <el-option v-for="item in pinia.positions" :key="item.id" :label="item.name" :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              职称：
              <el-select v-model="empSelect.jobLevelId" placeholder="职称" size="small" style="width: 130px;">
                <el-option v-for="item in joblevels" :key="item.id" :label="item.name" :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              聘用形式：
              <el-radio-group v-model="empSelect.engageForm">
                <el-radio label="劳动合同">劳动合同</el-radio>
                <el-radio label="劳务合同">劳务合同</el-radio>
              </el-radio-group>
            </el-col>
          </el-row>
          <el-row style="margin-top: 10px;">
            <el-col :span="5">
              所属部门：
              <el-popover placement="bottom" title="请选择部门" width="220" trigger="contextmenu" :visible="data.visible2">
                <el-tree :data="allDeps" :props="defaultProps" default-expand-all
                  @node-click="searchHandleNodeClick"></el-tree>
                <!-- node-click	节点被点击时的回调 共三个参数，依次为：传递给 data 属性的数组中该节点所对应的对象、节点对应的 Node、节点组件本身。 -->
                <!-- 自定义点击事件 -->
                <template #reference>
                  <div style="width:130px;display: inline-flex;
                    border-radius: 5px;border: 1px solid #dedede;height: 25px;cursor: pointer;align-items: center;
                    font-size: 12px;padding-left: 8px;box-sizing: border-box;" @click="showDepView2">{{ data.inputDepName }}
                  </div>
                </template><!-- 回显数据 {{inputDepName}} -->
              </el-popover>
            </el-col>
            <el-col :span="5" :offset="4">
              <el-button size="small" @click="data.showAdvanceSearchVisible = !data.showAdvanceSearchVisible">取消</el-button>
              <el-button type="primary" :icon="Search" size="small" @click="initEmps()">搜索</el-button>
            </el-col>
          </el-row>
        </div>
      </transition>
    </div>
  </div>
</template>

<script setup lang="ts">
import { store } from '@/stores';
import type { Nations,JobLevel,PoliticsStatus,Deps, Position } from '@/types/emp/emp'
import api from '@/api/request'
import { Search,ArrowUp,ArrowDown } from '@element-plus/icons-vue'
import {downloadRequest} from "@/utils/download";
import { storeToRefs } from 'pinia';
import { reactive } from 'vue';

  defineProps({
    nations:{ type:Array<Nations> },
    joblevels:{ type:Array<JobLevel>},
    politicsstatus:{ type:Array<PoliticsStatus>},
    allDeps:{ type:Array<Deps>},
  })

  const pinia = store();

  const defaultProps = {
    children:'children',
    label:'name'
  }

  let { empSelect } = storeToRefs(pinia)
  let data = reactive({
    showAdvanceSearchVisible:false,
    visible2:false,
    inputDepName:''
  })

  pinia.initPositions()
  //下载请求
  function exportData() {
    downloadRequest('/employee/basic/export')
  }
  // 高级搜索 部门点击事件
  function searchHandleNodeClick(p:any) {
    data.inputDepName = p.name
    empSelect.value.departmentId = p.id
    // js中操作ref数据要用.value
    data.visible2 = !(data.visible2) // 弹框
  }
  function initEmps(){
    pinia.initEmps()
  }
    //高级搜索 部门弹框
  function showDepView2() {
    data.visible2 = !(data.visible2)
  }
</script>

<style scoped>

</style>