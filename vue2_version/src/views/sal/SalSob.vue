<template>
  <div>
    <!-- 绘制表格 -->
    <div style="display: flex;justify-content: space-between;">
      <!-- @click="showAddSalaryView" 点击打开 添加工资账套对话框 -->
      <el-button type="primary" icon="el-icon-plus" size="mini" @click="showAddSalaryView">添加工资账套</el-button>
      <!-- 刷新功能 直接绑定点击事件 调用获取所有数据方法 -->
      <el-button type="success" icon="el-icon-refresh" size="mini" @click="initSalaries"></el-button>
    </div>
    <div style="margin-top: 10px; ">
      <el-table
          :data="salaries"
          width="80%"
          stripe
          border>
        <!-- 多选框 type="selection" -->
        <el-table-column
            type="selection"
            width="40">
        </el-table-column>
        <el-table-column
            prop="name"
            label="账套名称"
            width="120">
        </el-table-column>
        <el-table-column
            prop="basicSalary"
            label="基本工资"
            width="70">
        </el-table-column>
        <el-table-column
            prop="trafficSalary"
            label="交通补助"
            width="70">
        </el-table-column>
        <el-table-column
            prop="lunchSalary"
            label="午餐补助"
            width="70">
        </el-table-column>
        <el-table-column
            prop="bonus"
            label="奖金"
            width="70">
        </el-table-column>
        <el-table-column
            prop="createDate"
            label="启用时间"
            width="100">
        </el-table-column>
        <!-- 多级表头：el-table-column 里面嵌套 el-table-column，就可以实现多级表头  -->
        <el-table-column
            label="养老金"
            align="center">
          <el-table-column
              prop="pensionPer"
              label="比率"
              width="70">
          </el-table-column>
          <el-table-column
              prop="pensionBase"
              label="基数"
              width="70">
          </el-table-column>
        </el-table-column>
        <!-- 多级表头 -->
        <el-table-column
            label="医疗保险"
            align="center">
          <el-table-column
              prop="medicalPer"
              label="比率"
              width="70">
          </el-table-column>
          <el-table-column
              prop="medicalBase"
              label="基数"
              width="70">
          </el-table-column>
        </el-table-column>
        <!-- 多级表头 -->
        <el-table-column
            label="公积金"
            align="center">
          <el-table-column
              prop="accumulationFundPer"
              label="比率"
              width="70">
          </el-table-column>
          <el-table-column
              prop="accumulationFundBase"
              label="基数"
              width="70">
          </el-table-column>
        </el-table-column>
        <el-table-column
            label="操作"
            >
          <!-- 删除工资账套 拿到当前行数据 绑定点击事件 传行数据-->
          <template slot-scope="scope">
            <!-- @click="showEditSalaryView(scope.row)">编辑  -->
            <el-button type="primary" size="mini" @click="showEditSalaryView(scope.row)">编辑</el-button>
            <el-button type="danger" size="mini" @click="deleteSalary(scope.row)">删除</el-button>
          </template>
        </el-table-column>

      </el-table>
    </div>
    <!-- 添加工资账套对话框 -->
    <!-- 把标题变成属性 -->
    <el-dialog
        :title="dialogTitle"
        :visible.sync="dialogVisible"
        width="50%">
      <!-- 调整样式 -->
      <div style="display: flex;justify-content: space-around;align-items: center;">
        <!--添加步骤条 -->
        <!-- :active="activeItemIndex" -->
        <el-steps direction="vertical" :active="activeItemIndex">
          <!-- 循环遍历数据 -->
          <el-step :title="itemName" v-for="(itemName,index) in salaryItemName" :key="index"></el-step>
        </el-steps>
        <!-- 循环遍历数据 -->
        <!-- v-show="activeItemIndex = index" 与下标相等才展示 -->
        <!-- 修改各项的值 绑定和遍历-->
        <el-input v-model="salary[title]" :placeholder="'请输入'+salaryItemName[index]+'...'"
                  v-for="(value,title,index) in salary"
                  :key="index" v-show="activeItemIndex === index" style="width: 200px;"></el-input>
      </div>
      <span slot="footer" class="dialog-footer">
        <!-- 按钮判断根据索引显示 文字提示 -->
        <el-button @click="preStep">{{ activeItemIndex === 10 ? '取消' : '上一步' }}</el-button>
        <el-button type="primary" @click="nextStep">{{ activeItemIndex === 10 ? '完成' : '下一步' }}</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
export default {
  name: "SalSob",
  data() {
    return {
      dialogTitle: '添加工资账套', // 标题
      dialogVisible: false, // 添加工资账套对话框
      salaries: [], // 定义数组
      activeItemIndex: 0, // 步骤条激活索引
      salaryItemName: [ // 步骤条数据对象
        '账套名称',
        '基本工资',
        '交通补助',
        '午餐补助',
        '奖金',
        '养老金比率',
        '养老金基数',
        '医疗保险比率',
        '医疗保险基数',
        '公积金比率',
        '公积金基数'
      ],
      // 定义工资账套数据
      salary: {
        name: '',
        basicSalary: null,
        trafficSalary: null,
        lunchSalary: null,
        bonus: null,
        pensionPer: null,
        pensionBase: null,
        medicalPer: null,
        medicalBase: null,
        accumulationFundPer: null,
        accumulationFundBase: null
      }
    }
  },
  mounted() {
    this.initSalaries()
  },
  methods: {
    // 点击编辑显示对话框
    showEditSalaryView(data) {
      this.dialogTitle = '编辑工资账套' // 设置标题
      this.activeItemIndex = 0 // 默认激活的索引
      this.salary.id = data.id
      this.salary.name = data.name
      this.salary.basicSalary = data.basicSalary
      this.salary.trafficSalary = data.trafficSalary
      this.salary.lunchSalary = data.lunchSalary
      this.salary.bonus = data.bonus
      this.salary.pensionPer = data.pensionPer
      this.salary.pensionBase = data.pensionBase
      this.salary.medicalPer = data.medicalPer
      this.salary.medicalBase = data.medicalBase
      this.salary.accumulationFundPer = data.accumulationFundPer
      this.salary.accumulationFundBase = data.accumulationFundBase

      this.dialogVisible = true // 打开对话框
    },
    // 删除工资账套
    deleteSalary(data) {
      this.$confirm('此操作将永久删除该[' + data.name + ']工资账套, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequest('/salary/sob/' + data.id).then(resp => {
          if (resp) {
            this.initSalaries()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    preStep() { // 上一步 取消
      if (this.activeItemIndex === 0) {
        return
      } else if (this.activeItemIndex === 10) {
        this.dialogVisible = false;
        return;
      }
      this.activeItemIndex--
    },
    nextStep() { // 下一步 完成
      if (this.activeItemIndex === 10) {
        // alert("ok")
        // console.log(this.salary)
        // 添加工资账套
        if (this.salary.id) { // 有 id 调用编辑接口，没有 id 执行添加
          this.putRequest('/salary/sob/', this.salary).then(resp => {
            if (resp) {
              this.initSalaries()
              this.dialogVisible = false // 关闭弹框
            }
          })

        } else {
          this.postRequest('/salary/sob/', this.salary).then(resp => {
            if (resp) {
              this.initSalaries()
              this.dialogVisible = false
            }
          })
        }
        return
      }
      this.activeItemIndex++
    },
    //点击打开添加工资账套对话框
    showAddSalaryView() {
      this.dialogTitle = '添加工资账套' // 添加的时候显示此标题
      this.salary = { // 清空表单
        name: '',
        basicSalary: null,
        trafficSalary: null,
        lunchSalary: null,
        bonus: null,
        pensionPer: null,
        pensionBase: null,
        medicalPer: null,
        medicalBase: null,
        accumulationFundPer: null,
        accumulationFundBase: null
      }
      this.activeItemIndex = 0 // 步骤条索引从0开始
      this.dialogVisible = true;
    },
    // 初始化数据
    initSalaries() {
      this.getRequest('/salary/sob/').then(resp => {
        if (resp) {
          this.salaries = resp
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
