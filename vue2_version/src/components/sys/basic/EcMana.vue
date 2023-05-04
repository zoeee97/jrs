<template>
  <div>
    <div>
      <el-input size="small" v-model="jl.ecReason" placeholder="添加奖罚原因..."
                prefix-icon="el-icon-plus" style="width: 150px"></el-input>
      <el-input size="small" v-model="jl.ecPoint" placeholder="添加奖罚分数..."
                prefix-icon="el-icon-plus" style="width: 150px;margin-left: 6px"></el-input>
      <el-select size="small" v-model="jl.ecType" placeholder="奖罚类型" style="margin-left: 6px;margin-right: 6px">
        <el-option
            v-for="item in titleLevels"
            :key="item"
            :label="item"
            :value="item">
        </el-option>
      </el-select>
      <el-button type="primary" icon="el-icon-plus" size="small" @click="addJobLevel">添加</el-button>
    </div>
    <div style="margin-top: 10px;">
      <el-table
          :data="jls"
          stripe
          border
          size="small"
          style="width: 90%;"
          @selection-change="handleSelectionChange">
        <el-table-column
            type="selection"
            width="55">
        </el-table-column>
        <el-table-column
            prop="id"
            label="编号"
            width="55">
        </el-table-column>
<!--        <el-table-column-->
<!--            prop="eid"-->
<!--            label="员工编号"-->
<!--            width="100">-->
<!--        </el-table-column>-->
        <el-table-column
            prop="ecReason"
            label="奖罚原因"
            width="300">
        </el-table-column>
        <el-table-column
            prop="ecDate"
            label="创建日期"
            width="150">
        </el-table-column>
        <el-table-column
            prop="ecPoint"
            label="奖罚分数"
            width="150">
        </el-table-column>
        <el-table-column
            prop="ecType"
            label="奖罚类型"
            width="100">
          <template slot-scope="scope">
            <el-tag type="success" v-if="scope.row.ecType==0">奖</el-tag>
            <el-tag type="danger" v-else>罚</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
                size="small"
                @click="showEditView(scope.row)">编辑
            </el-button>
            <el-button
                size="small"
                type="danger"
                @click="deleteHandle(scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-button size="small" style="margin-top: 8px" type="danger"
                 :disabled="this.multipleSelection.length===0" @click="deleteMany">批量删除
      </el-button>
    </div>
    <!-- 编辑弹框 -->
    <el-dialog
        title="编辑奖罚信息"
        :visible.sync="dialogVisible"
        width="30%">
      <table>
        <tr>
          <td>
            <el-tag>奖罚原因</el-tag>
          </td>
          <td>
            <el-input v-model="updateJl.ecReason" size="small" style="margin-left: 6px"></el-input>
          </td>
        </tr>
        <tr>
          <td>
            <el-tag>奖罚分数</el-tag>
          </td>
          <td>
            <el-input v-model="updateJl.ecPoint" size="small" style="margin-left: 6px"></el-input>
          </td>
        </tr>
        <tr>
          <td>
            <el-tag>奖罚类型</el-tag>
          </td>
          <td>
            <el-select size="small" v-model="updateJl.ecType" placeholder="奖罚类型"
                       style="margin-left: 6px;margin-right: 6px">
              <el-option
                  v-for="item in titleLevels"
                  :key="item"
                  :label="item"
                  :value="item">
              </el-option>
            </el-select>
          </td>
        </tr>
      </table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="doUpdate">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
export default {
  name: "EcMana",
  data() {
    return {
      // 查询 添加 数据对象
      jl: {
        ecReason:'',
        ecType: '',
        ecPoint:'',
      },
      // 更新 数据对象
      updateJl: {
        ecReason: '',
        ecPoint: '',
        ecType: 0
      },
      titleLevels: [
          // 0,
          // 1
          '奖',
          '罚'
      ],
      jls: [], // 删除单条
      dialogVisible: false,
      multipleSelection: [] // 批量删除勾选中的值
    }
  },
  mounted() {
    this.initJls()
  },
  methods: {
    // 执行批量删除
    deleteMany(){
      this.$confirm('此操作将永久删除[' + this.multipleSelection.length + ']条奖罚信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let ids = '?'
        this.multipleSelection.forEach(item => {
          ids += 'ids=' + item.id + '&'
        })
        this.deleteRequest('/personnel/ec/' + ids).then(resp => {
          if (resp) {
            this.initJls()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 批量删除勾选中的值
    handleSelectionChange(val){
      this.multipleSelection = val
    },
    // 更新职称信息
    doUpdate() {
      this.putRequest('/personnel/ec/', this.updateJl).then(resp => {
        if (resp) {
          this.initJls()
          this.dialogVisible = false
        }
      })
    },
    // 显示编辑弹框
    showEditView(data) {
      Object.assign(this.updateJl, data) // 复制数据,注意这里是 , 号隔开
      this.updateJl.ecDate = '' // 更新日期由后端处理，这里不用传
      this.dialogVisible = true // 显示编辑弹框
    },
    // 删除职称
    deleteHandle(data) {
      this.$confirm('此操作将永久删除[' + data.ecReason + '], 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequest(' /personnel/ec/' + data.id).then(resp => {
          if (resp) {
            this.initJls()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 添加职称
    addJobLevel() {
      // if (this.jl.ecReason) {
      if (this.jl.ecReason  && this.jl.ecPoint) {
        if(this.jl.ecType==='奖'){
          this.jl.ecType===0
        }else {
          this.jl.ecType===1
        }
        this.postRequest('/personnel/ec/', this.jl).then(resp => {
          if (resp) {
            this.initJls()
          }
        })
      } else {
        this.$message.error('字段不能为空！')
      }

    },
    // 获取职称列表数据
    initJls() {
      this.getRequest('/personnel/ec/').then(resp => {
        if (resp) {
          this.jls = resp;
          this.jl.ecReason = ''
          this.jl.ecType = ''
          this.jl.ecPoint = ''
        }
      })
    }
  }
}
</script>

<style scoped>

</style>

