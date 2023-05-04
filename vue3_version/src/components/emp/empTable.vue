<!--
 * @Author: zyy 1741540959@qq.com
 * @Date: 2023-02-07 16:32:33
 * @LastEditors: zyy
 * @LastEditTime: 2023-02-15 18:45:47
 * @Description: file content
-->
<template>
  <div>
    <div>
      <div style="display: flex;justify-content: space-between;">
        <div style="margin-top: 10px;">
          <el-input style="width: 300px;margin-right: 10px;"
                    :prefix-icon="Search"
                    v-model="empSelect.name"
                    placeholder="请输入员工名进行搜索..."
                    @keyup.enter="initEmps"
                    clearable
                    @clear="initEmps"
                    :disabled="showAdvanceSearchVisible"
          ></el-input>
          <el-button type="primary" :icon="Search" @click="initEmps"
                     :disabled="showAdvanceSearchVisible">搜索
          </el-button>
          <el-button type="primary" @click="showAdvanceSearchVisible = !showAdvanceSearchVisible">
            <component :is="showAdvanceSearchVisible? ArrowUp : ArrowDown"
               aria-hidden="true"></component>高级搜索
          </el-button>
        </div>
        <div>

        </div>
      </div>
      <!-- 高级搜索条件框 -->
      <transition name="slide-fade">
        <div v-show="showAdvanceSearchVisible"
             style="border: 1px solid #379ff5;border-radius: 5px;box-sizing: border-box;padding: 5px;margin: 10px 0;">
          <el-row>
            <el-col :span="5">
              政治面貌：
              <el-select v-model="empSelect.politicId" placeholder="请选择政治面貌" size="small" style="width: 130px;">
                <el-option
                    v-for="item in politicsstatus"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              民族：
              <el-select v-model="empSelect.nationId" placeholder="民族" size="small" style="width: 130px;">
                <el-option
                    v-for="item in nations"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              职位：
              <el-select v-model="empSelect.posId" placeholder="职位" size="small" style="width: 130px;">
                <el-option
                    v-for="item in positions"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              职称：
              <el-select v-model="empSelect.jobLevelId" placeholder="职称" size="small" style="width: 130px;">
                <el-option
                    v-for="item in joblevels"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
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
              <el-popover
                  placement="bottom"
                  title="请选择部门"
                  width="220"
                  trigger="contextmenu"
                  :visible="visible2">
                <!--添加树形控件 default-expand-all	是否默认展开所有节点 ，节点点击事件 @node-click="handleNodeClick" -->
                <el-tree :data="allDeps"
                         :props="defaultProps"
                         default-expand-all
                         @node-click="searchHandleNodeClick"></el-tree>
                <!-- node-click	节点被点击时的回调 共三个参数，依次为：传递给 data 属性的数组中该节点所对应的对象、节点对应的 Node、节点组件本身。 -->
                <!-- 自定义点击事件 -->
                <template #reference>
                  <div style="width:130px;display: inline-flex;
                    border-radius: 5px;border: 1px solid #dedede;height: 25px;cursor: pointer;align-items: center;
                    font-size: 12px;padding-left: 8px;box-sizing: border-box;"
                     @click="showDepView2">{{ inputDepName }}</div>
                </template><!-- 23-25 回显数据 {{inputDepName}} -->
              </el-popover>
            </el-col>
            <el-col :span="5" :offset="4">
              <el-button size="small" @click="showAdvanceSearchVisible = !showAdvanceSearchVisible">取消</el-button>
              <el-button type="primary" :icon="Search" size="small" @click="initEmps()">搜索</el-button>
            </el-col>
          </el-row>
        </div>
      </transition>
    </div>
    <!-- 表格展示 -->
    <el-table
      :data="data" 
      v-loading="loading" 
      element-loading-text="拼命加载中" 
      element-loading-spinner="el-icon-loading"
      element-loading-background="rgba(0, 0, 0, 0.8)"
      style="width: 100%" stripe border
    >
      <el-table-column
        v-for="col in columns"
        :prop="col.prop"
        :align="col.align"
        :label="col.label"
        :width="col.width"
        :fixed="col.fixed"
      >
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <div style="display: flex;justify-content: flex-end;margin-top: 10px;">
        <!-- @current-change="currentChange" 当前页
             @size-change="sizeChange" 每页显示多少条 -->
        <el-pagination
            :hide-on-single-page="true"
            prev-text="上一页"
            next-text="下一页"
            @current-change="currentChange"
            @size-change="sizeChange"
            :page-sizes="[10,20,30,50,100]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total" background>
        </el-pagination>
      </div>
  </div>
</template>

<script lang="ts">
import type { EmpSelect, Position } from '@/types/emp/emp'
import { defineComponent, reactive, ref, toRefs, type Ref ,type Reactive} from 'vue'
import api from '@/api/request'
import { Search,ArrowUp,ArrowDown } from '@element-plus/icons-vue'

export default defineComponent({
  // 非setup语法糖接收Props的写法，要用对象或字符串数组先声明，setup函数第一个参数为组件的Props
  props:['nations','joblevels','politicsstatus','allDeps'],
  components:{Search,ArrowUp,ArrowDown},
  async setup(props) {
    const defaultProps = {
      children:'children',
      label:'name'
    }
    // 直接解构会失去响应式
    const {nations,joblevels,politicsstatus,allDeps} = toRefs(props)
    let positions = reactive<Array<Position>>([])

    let loading = ref(false)

    let total:Ref<number> = ref(0)
    
    let showAdvanceSearchVisible:Ref<boolean> = ref(false)
    let visible2:Ref<boolean> = ref(false)

    let data:Reactive<Array<Object>> = reactive([])
    let inputDepName:Ref<string> = ref('')
      // 发现这种query参数好像最后都变成字符串了、、所以好像也就不用怎么规范类型
    let empSelect = reactive<EmpSelect>({
      currentPage:1,
      size:10,
      politicId:'',
      nationId:'',
      posId:'',
      jobLevelId:'',
      departmentId:'',
      beginDateScope:'',
      engageForm:'',
      name:''
    })
    const columns = [
      { prop: "name", label: "姓名", align: "left", fixed: true, width: "90" },
      { prop: "gender", label: "性别", align: "left", width: "45" },
      { prop: "workID", label: "工号", align: "left", width: "85" },
      { prop: "idCard", label: "身份证号", align: "left", width: "150" },
      { prop: "nation.name", label: "民族", align: "left", width: "50" },
      { prop: "politicsStatus.name", label: "政治面貌", align: "left", width: "100" },
      { prop: "phone", label: "电话号码", align: "left", width: "100" },
      { prop: "address", label: "联系地址", align: "center", width: "270" },
      { prop: "department.name", label: "所属部门", align: "left", width: "100" },
      { prop: "position.name", label: "职位", align: "left", width: "100" },
      { prop: "joblevel.name", label: "职称", align: "left", width: "100" },
      { prop: "engageForm", label: "聘用形式", align: "left", width: "100" },
    ]
    // 分页获取所有员工
    async function initEmps() {
      loading.value = true
      await api.get('/employee/basic',empSelect)
        .then(
          (res:any)=>{
            data.splice(0, data.length)
            data.push(...res.data)
            total.value = res.total
            loading.value = false;
          }
        )
    }
    // 获取职位 有可能变动 打开对话框的时候调用此方法
    async function initPositions() {
      api.get('/employee/basic/position').then((resp:any) => {
        if (resp) {
          positions = resp
        }
      })
    }
    // 调用函数初始化员工资料
    await initEmps()
    await initPositions()
    // 分页-当前页 参数会在鼠标点击时自动携带
    function currentChange(currentChange:any){
      // console.log('ww'+Object.prototype.toString.call(currentChange));
      empSelect.currentPage = currentChange
      initEmps()
    }
    // 分页-每页显示多少条
    function sizeChange(size:any){
      // console.log(Object.prototype.toString.call(size)); 确实是Number类型
      empSelect.size = size
      initEmps()
    }
        // 高级搜索 部门点击事件
    function searchHandleNodeClick(data:any) {
      inputDepName.value = data.name
      empSelect.departmentId = data.id
      // js中操作ref数据要用.value
      visible2.value = !(visible2.value) // 弹框
    }
    // 高级搜索 部门弹框
    function showDepView2() {
      visible2.value = !(visible2.value)
    }
    return {
      data,
      defaultProps,
      visible2,
      columns,
      total,
      empSelect,
      showAdvanceSearchVisible,
      nations,joblevels,politicsstatus,allDeps,positions,
      inputDepName,
      loading,
      Search,ArrowUp,ArrowDown,
      currentChange,
      sizeChange,
      initEmps,
      initPositions,
      searchHandleNodeClick,
      showDepView2
    }
  }
})
</script>

<style scoped>
/* 可以设置不同的进入和离开动画 */
/* 设置持续时间和动画函数 */
.slide-fade-enter-active {
  transition: all .8s ease;
}

.slide-fade-leave-active {
  transition: all .8s cubic-bezier(1.0, 0.5, 0.8, 1.0);
}

.slide-fade-enter, .slide-fade-leave-to
  /* .slide-fade-leave-active for below version 2.1.8 */
{
  transform: translateX(10px);
  opacity: 0;
}
</style>