<template>
  <div>
    <div>
      <div style="display: flex;justify-content: space-between;">
        <div style="margin-top: 10px;">
          <el-input style="width: 300px;margin-right: 10px;"
                    prefix-icon="el-icon-search"
                    v-model="empName"
                    placeholder="请输入员工名进行搜索..."
                    @keydown.enter.native="initEmps"
                    clearable
                    @clear="initEmps"
                    :disabled="showAdvanceSearchVisible"
          ></el-input>
          <el-button type="primary" icon="el-icon-search" @click="initEmps"
                     :disabled="showAdvanceSearchVisible">搜索
          </el-button>
          <el-button type="primary" @click="showAdvanceSearchVisible = !showAdvanceSearchVisible">
            <i :class="showAdvanceSearchVisible?'fa fa-angle-double-up':'fa fa-angle-double-down'"
               aria-hidden="true"></i>高级搜索
          </el-button>
        </div>
        <div>
          <el-button type="success" @click="exportData"><i class="el-icon-download" aria-hidden="true"></i>&nbsp; 导出数据
          </el-button>
          <el-button type="primary" icon="el-icon-plus" @click="showAddEmpView">添加员工</el-button>
        </div>
      </div>
      <!-- 高级搜索条件框 -->
      <!-- 高级搜索条件框 v-show="showAdvanceSearchVisible" -->
      <!-- 添加展开动画效果 <transition name="fade"> 包含整个搜索条件框 </transition> -->
      <!-- 绑定搜索条件数据 v-model="searchValue.xxxxx" -->
      <transition name="slide-fade">
        <div v-show="showAdvanceSearchVisible"
             style="border: 1px solid #379ff5;border-radius: 5px;box-sizing: border-box;padding: 5px;margin: 10px 0;">
          <el-row>
            <el-col :span="5">
              政治面貌：
              <el-select v-model="searchValue.politicId" placeholder="请选择政治面貌" size="mini" style="width: 130px;">
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
              <el-select v-model="searchValue.nationId" placeholder="民族" size="mini" style="width: 130px;">
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
              <el-select v-model="searchValue.posId" placeholder="职位" size="mini" style="width: 130px;">
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
              <el-select v-model="searchValue.jobLevelId" placeholder="职称" size="mini" style="width: 130px;">
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
              <el-radio-group v-model="searchValue.engageForm">
                <el-radio label="劳动合同">劳动合同</el-radio>
                <el-radio label="劳务合同">劳务合同</el-radio>
              </el-radio-group>
            </el-col>
          </el-row>
          <el-row style="margin-top: 10px;">
            <!-- 处理部门 v-model="visible2" -->
            <el-col :span="5">
              所属部门：
              <el-popover
                  placement="bottom"
                  title="请选择部门"
                  width="220"
                  trigger="manual"
                  v-model="visible2">
                <el-tree :data="allDeps"
                         :props="defaultProps"
                         default-expand-all
                         @node-click="searchHandleNodeClick"></el-tree>
                <div slot="reference"
                     style="width:130px;display: inline-flex;
                 border-radius: 5px;border: 1px solid #dedede;height: 28px;cursor: pointer;align-items: center;
                 font-size: 12px;padding-left: 8px;box-sizing: border-box;"
                     @click="showDepView2">{{ inputDepName }}
                </div><!-- 回显数据 {{inputDepName}} -->
              </el-popover>
            </el-col>
            <el-col :span="5" :offset="4">
              <el-button size="mini" @click="showAdvanceSearchVisible = !showAdvanceSearchVisible">取消</el-button>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="initEmps('advanced')">搜索</el-button>
            </el-col>
          </el-row>
        </div>
      </transition>
    </div>
    <div style="margin-top: 10px;">
      <!-- 表格 添加 loading -->
      <el-table
          :data="emps"
          v-loading="loading"
          element-loading-text="拼命加载中"
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.8)"
          style="width: 100%" stripe border>
        <el-table-column
            type="selection"
            width="55">
        </el-table-column>
        <el-table-column
            prop="name"
            label="姓名"
            align="left"
            fixed
            width="90">
        </el-table-column>
        <el-table-column
            prop="gender"
            label="性别"
            align="left"
            width="45">
        </el-table-column>
        <el-table-column
            prop="workID"
            label="工号"
            align="left"
            width="85">
        </el-table-column>
        <el-table-column
            prop="birthday"
            label="出生日期"
            align="left"
            width="95">
        </el-table-column>
        <el-table-column
            prop="idCard"
            label="身份证号"
            width="150">
        </el-table-column>
        <el-table-column
            prop="wedlock"
            label="婚姻状态"
            align="center"
            width="70">
        </el-table-column>
        <el-table-column
            prop="nation.name"
            label="民族"
            align="left"
            width="50">
        </el-table-column>
        <el-table-column
            prop="nativePlace"
            label="籍贯"
            align="center"
            width="80">
        </el-table-column>
        <el-table-column
            prop="politicsStatus.name"
            label="政治面貌"
            width="100">
        </el-table-column>
        <el-table-column
            prop="email"
            label="电子邮件"
            align="left"
            width="160">
        </el-table-column>
        <el-table-column
            prop="phone"
            label="电话号码"
            align="left"
            width="100">
        </el-table-column>
        <el-table-column
            prop="address"
            label="联系地址"
            align="center"
            width="270">
        </el-table-column>
        <el-table-column
            prop="department.name"
            label="所属部门"
            align="left"
            width="100">
        </el-table-column>
        <el-table-column
            prop="position.name"
            label="职位"
            width="100">
        </el-table-column>
        <el-table-column
            prop="joblevel.name"
            label="职称"
            width="100">
        </el-table-column>
        <el-table-column
            prop="engageForm"
            label="聘用形式"
            align="left"
            width="100">
        </el-table-column>
        <el-table-column
            prop="tiptopDegree"
            label="最高学历"
            align="center"
            width="80">
        </el-table-column>
        <el-table-column
            prop="school"
            label="毕业学校"
            align="left"
            width="150">
        </el-table-column>
        <el-table-column
            prop="specialty"
            label="所属专业"
            align="left"
            width="150">
        </el-table-column>
        <el-table-column
            prop="workState"
            label="在职状态"
            align="center"
            width="70">
        </el-table-column>
        <el-table-column
            prop="beginDate"
            label="入职日期"
            align="left"
            width="95">
        </el-table-column>
        <el-table-column
            prop="conversionTime"
            label="转正日期"
            align="left"
            width="95">
        </el-table-column>
        <el-table-column
            prop="beginContract"
            label="合同起始日期"
            align="left"
            width="95">
        </el-table-column>
        <el-table-column
            prop="endContract"
            label="合同截止日期"
            align="left"
            width="95">
        </el-table-column>
        <el-table-column
            label="合同期限"
            align="left"
            width="100">
          <template slot-scope="scope">
            <el-tag>{{ scope.row.contractTerm }}</el-tag>
            年
          </template>
        </el-table-column>
        <el-table-column
            label="操作"
            fixed="right"
            align="center"
            width="150">
          <template slot-scope="scope">
            <!--  给编辑按钮绑定点击事件 @click="showEmpView(scope.row)" -->
            <el-button style="padding: 3px;" size="mini" @click="showEmpView(scope.row)">编辑</el-button>
            <el-button style="padding: 3px;" size="mini" type="danger" @click="deleteEmp(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <div style="display: flex;justify-content: flex-end;margin-top: 10px;">
        <el-pagination
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
    <!-- 开始- 添加员工弹框 -->
    <!-- 编辑员工 将添加员工弹框标题改为变量 根据条件显示是添加还是编辑 :title="title"  -->
    <el-dialog
        :title="title"
        :visible.sync="dialogVisible"
        width="80%">
      <div>
        <!-- 数据校验对象 :rules="empRules" ，每项属性对应 prop="posId" -->
        <el-form ref="empRef" :model="emp" :rules="empRules">
          <el-row>
            <el-col :span="6">
              <el-form-item label="姓名：" prop="name">
                <el-input v-model="emp.name" prefix-icon="el-icon-edit" placeholder="请输入员工姓名" size="mini"
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
                <el-date-picker
                    v-model="emp.birthday"
                    type="date"
                    value-format="yyyy-MM-dd"
                    size="mini"
                    style="width: 150px;"
                    placeholder="出生日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <!-- 添加员工 给每项赋值 -->
              <el-form-item label="政治面貌：" prop="politicId">
                <el-select v-model="emp.politicId" placeholder="请选择政治面貌" size="mini" style="width: 200px;">
                  <el-option
                      v-for="item in politicsstatus"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="民族：" prop="nationId">
                <el-select v-model="emp.nationId" placeholder="民族" size="mini" style="width: 150px;">
                  <el-option
                      v-for="item in nations"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="籍贯：" prop="nativePlace">
                <el-input v-model="emp.nativePlace" placeholder="请输入籍贯" prefix-icon="el-icon-edit" size="small"
                          style="width: 120px;"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="电子邮箱：" prop="email">
                <el-input v-model="emp.email" placeholder="请输入电子邮箱" prefix-icon="el-icon-message" size="mini"
                          style="width: 150px;"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="联系地址：" prop="address">
                <el-input v-model="emp.address" placeholder="请输入联系地址" prefix-icon="el-icon-edit" size="mini"
                          style="width: 200px;"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="职位：" prop="posId">
                <el-select v-model="emp.posId" placeholder="职位" size="mini" style="width: 150px;">
                  <el-option
                      v-for="item in positions"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="职称：" prop="jobLevelId">
                <el-select v-model="emp.jobLevelId" placeholder="职称" size="mini" style="width: 150px;">
                  <el-option
                      v-for="item in joblevels"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="所属部门：" prop="departmentId">
                <!-- manual 手动弹出框 -->
                <el-popover
                    placement="bottom"
                    title="请选择部门"
                    width="200"
                    trigger="manual"
                    v-model="visible">
                  <!--添加树形控件 default-expand-all	是否默认展开所有节点 ，节点点击事件 @node-click="handleNodeClick" -->
                  <el-tree :data="allDeps"
                           :props="defaultProps"
                           default-expand-all
                           @node-click="handleNodeClick"></el-tree>
                  <!-- node-click	节点被点击时的回调 共三个参数，依次为：传递给 data 属性的数组中该节点所对应的对象、节点对应的 Node、节点组件本身。 -->
                  <!-- 自定义点击事件 -->
                  <div slot="reference"
                       style="width:150px;display: inline-flex;
                       border-radius: 5px;border: 1px solid #dedede;height: 28px;cursor: pointer;align-items: center;
                       font-size: 12px;padding-left: 8px;box-sizing: border-box;"
                       @click="showDepView">{{ inputDepName }}
                  </div>
                </el-popover>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="电话号码：" prop="phone">
                <el-input v-model="emp.phone" placeholder="请输入电话号码" size="mini" style="width: 200px;"
                          prefix-icon="el-icon-phone"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="工号：" prop="workID">
                <el-input v-model="emp.workID" placeholder="请输入工号" size="mini" style="width: 150px;"
                          prefix-icon="el-icon-edit" disabled></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <!-- 数据在 data 中写死的 -->
              <el-form-item label="学历：" prop="tiptopDegree">
                <el-select v-model="emp.tiptopDegree" placeholder="学历" size="mini" style="width: 150px;">
                  <el-option
                      v-for="item in tiptopDegrees"
                      :key="item"
                      :label="item"
                      :value="item">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="毕业院校：" prop="school">
                <el-input v-model="emp.school" placeholder="请输入学校" size="mini" style="width: 150px;"
                          prefix-icon="el-icon-edit"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="专业名称：" prop="specialty">
                <el-input v-model="emp.specialty" placeholder="请输入专业名称" size="mini" style="width: 200px;"
                          prefix-icon="el-icon-edit"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="入职日期：" prop="beginDate">
                <el-date-picker
                    v-model="emp.beginDate"
                    type="date"
                    value-format="yyyy-MM-dd"
                    size="mini"
                    style="width: 120px;"
                    placeholder="入职日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="转正日期：" prop="conversionTime">
                <el-date-picker
                    v-model="emp.conversionTime"
                    type="date"
                    value-format="yyyy-MM-dd"
                    size="mini"
                    style="width: 120px;"
                    placeholder="转正日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="合同起始日期：" prop="beginContract">
                <el-date-picker
                    v-model="emp.beginContract"
                    type="date"
                    value-format="yyyy-MM-dd"
                    size="mini"
                    style="width: 135px;"
                    placeholder="合同起始日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="合同截止日期：" prop="endContract">
                <el-date-picker
                    v-model="emp.endContract"
                    type="date"
                    value-format="yyyy-MM-dd"
                    size="mini"
                    style="width: 170px;"
                    placeholder="合同截止日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="身份证号码：" prop="idCard">
                <el-input v-model="emp.idCard" placeholder="请输入身份证号码"
                          size="mini" prefix-icon="el-icon-edit" style="width: 180px;"></el-input>
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
        <el-button type="primary" @click="doAddEmp">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "PerEmp",
  data() {
    return {
      searchValue: { // 高级搜索 条件对象
        politicId: null, // 政治面貌
        nationId: null, // 民族
        posId: null, // 职位
        jobLevelId: null, // 职称
        engageForm: '', // 聘用形式
        departmentId: null, // 部门 id
      },
      showAdvanceSearchVisible: false, // 高级搜索框 动态效果
      headers: { // 定义请求头
        Authorization: window.sessionStorage.getItem('tokenStr')
      },
      title: '', // 添加编辑员工弹框动态标题
      emps: [], // 获取所有员工（分页）
      loading: false, // 添加 loading
      total: 0, // 分页总条数
      currentPage: 1, // 默认显示第1页(currentPage 后端字段）
      size: 10, // 默认每页显示 10 条
      empName: '', // 搜索
      dialogVisible: false, // 添加员工弹框
      nations: [],   // 添加员工 民族
      joblevels: [], // 职称
      politicsstatus: [], // 政治面貌
      positions: [],  // 职位
      department: [], // 部门
      // 学历
      tiptopDegrees: ['博士', '硕士', '本科', '大专', '高中', '初中', '小学', '其它'],
      // 添加员工
      emp: {
        id: null,
        name: '',
        gender: '',
        birthday: '',
        idCard: '',
        wedlock: '',
        nationId: null,
        nativePlace: '',
        politicId: null,
        email: '',
        phone: '',
        address: '',
        departmentId: null,
        jobLevelId: null,
        posId: null,
        engageForm: '',
        tiptopDegree: '',
        specialty: '',
        school: '',
        beginDate: '',
        workState: '在职',
        workID: null,
        contractTerm: null,
        conversionTime: '',
        notworkDate: null,
        beginContract: '',
        endContract: '',
        workAge: null,
        salaryId: null
      },
      visible: false, // 弹出框
      visible2: false, // 高级搜索 部门
      // 树形控件
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      allDeps: [], // 树形控件 绑定 所属部门 数据对象
      inputDepName: '',// 回显部门数据
      // 表单数据校验

      empRules: {
        name: [{required: true, message: '请输入员工名', trigger: 'blur'}],
        gender: [{required: true, message: '请输入员工性别', trigger: 'blur'}],
        birthday: [{required: true, message: '请输入出生日期', trigger: 'blur'}],
        idCard: [{required: true, message: '请输入身份证号码', trigger: 'blur'},
          {
            pattern: /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$)/,
            message: '身份证号码不正确', trigger: 'blur'
          }],
        wedlock: [{required: true, message: '请输入婚姻状况', trigger: 'blur'}],
        nationId: [{required: true, message: '请输入民族', trigger: 'blur'}],
        nativePlace: [{required: true, message: '请输入籍贯', trigger: 'blur'}],
        politicId: [{required: true, message: '请输入政治面貌', trigger: 'blur'}],
        email: [{required: true, message: '请输入邮箱地址', trigger: 'blur'},
          {type: 'email', message: '邮箱地址格式不正确', trigger: 'blur'}],
        phone: [{required: true, message: '请输入电话号码', trigger: 'blur'},
          {
            pattern: /^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/,
            message: '请输入合法手机号码', trigger: 'blur'
          }],

        address: [{required: true, message: '请输入地址', trigger: 'blur'}],
        departmentId: [{required: true, message: '请输入部门名称', trigger: 'blur'}],
        jobLevelId: [{required: true, message: '请输入职称', trigger: 'blur'}],
        posId: [{required: true, message: '请输入职位', trigger: 'blur'}],
        engageForm: [{required: true, message: '请输入聘用形式', trigger: 'blur'}],
        tiptopDegree: [{required: true, message: '请输入学历', trigger: 'blur'}],
        specialty: [{required: true, message: '请输入专业', trigger: 'blur'}],
        school: [{required: true, message: '请输入毕业院校', trigger: 'blur'}],
        beginDate: [{required: true, message: '请输入入职日期', trigger: 'blur'}],
        workState: [{required: true, message: '请输入工作状态', trigger: 'blur'}],
        workID: [{required: true, message: '请输入工号', trigger: 'blur'}],
        contractTerm: [{required: true, message: '请输入合同期限', trigger: 'blur'}],
        conversionTime: [{required: true, message: '请输入转正日期', trigger: 'blur'}],
        notworkDate: [{required: true, message: '请输入离职日期', trigger: 'blur'}],
        beginContract: [{required: true, message: '请输入合同起始日期', trigger: 'blur'}],
        endContract: [{required: true, message: '请输入合同结束日期', trigger: 'blur'}],
        workAge: [{required: true, message: '请输入工龄', trigger: 'blur'}]

      }

    }
  },
  mounted() {
    this.initEmps() //获取所有员工（分页）
    this.initData() // 添加员工
    this.initPositions() //获取职位
  },
  methods: {
    //下载请求
    exportData() {
      this.downloadRequest('/employee/basic/export')
    },
    //编辑员工按钮 点击事件
    showEmpView(data) {
      this.title = '编辑员工信息'
      this.emp = data // 回显数据
      this.inputDepName = data.department.name //回显部门信息
      this.initPositions() // 初始化职位信息
      this.dialogVisible = true
    },
    //删除员工
    deleteEmp(data) {
      this.$confirm('此操作将永久删除该员工' + data.name + ', 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequest('/employee/basic/' + data.id).then(resp => {
          if (resp) {
            this.initEmps()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 确定添加员工
    //添加或编辑员工 有id编辑员工 没有id添加员工
    doAddEmp() {
      if (this.emp.id) {
        // 有 id 编辑员工
        this.$refs['empRef'].validate(valid => {
          if (valid) {
            this.putRequest('/employee/basic/', this.emp).then(resp => {
              if (resp) {
                this.dialogVisible = false
                this.initEmps()
              }
            })
          }
        })
      } else {
        // 没有id 添加员工
        // empRef 表单中定义的引用对象 ref="empRef"
        this.$refs['empRef'].validate(valid => {
          if (valid) {
            this.postRequest('/employee/basic/', this.emp).then(resp => {
              if (resp) {
                this.dialogVisible = false
                this.initEmps()
              }
            })
          }
        })
      }
    },
    // 高级搜索 部门点击事件
    searchHandleNodeClick(data) {
      this.inputDepName = data.name
      this.searchValue.departmentId = data.id
      this.visible2 = !this.visible2 // 弹框
    },
    // 树控件节点点击事件
    handleNodeClick(data) {
      this.inputDepName = data.name
      this.emp.departmentId = data.id
      this.visible = !this.visible // 弹框
    },
    // 高级搜索 部门弹框
    showDepView2() {
      this.visible2 = !this.visible2
    },
    // 添加员工 所属部门
    showDepView() {
      this.visible = !this.visible // 23-19 弹出框
    },
    // 添加员工 获取最大号
    getMaxWorkID() {
      this.getRequest('/employee/basic/maxWorkID').then(resp => {
        if (resp) {
          this.emp.workID = resp.obj
        }
      })
    },
    //添加员工
    initPositions() {
      this.getRequest('/employee/basic/position').then(resp => {
        if (resp) {
          this.positions = resp
        }
      })
    },
    // 添加员工
    initData() {
      // 获取民族数据:先从 sessionStorage 里取，取不到再调用接口获取数据
      if (!window.sessionStorage.getItem("nations")) {
        this.getRequest('/employee/basic/nation').then(resp => {
          if(resp){
            this.nations = resp
            // 存到 sessionStorage 里,把对象转字符串
            window.sessionStorage.setItem('nations', JSON.stringify(resp))
          }
        })
      } else {
        // 从 sessionStorage 获取，字符串转对象
        this.nations = JSON.parse(window.sessionStorage.getItem('nations'))
        // this.nations = JSON.parse(window.sessionStorage.getItem('nations')) && window.sessionStorage.getItem('nations');
      }
      // 获取职称
      if (!window.sessionStorage.getItem('joblevels')) {
        this.getRequest('/employee/basic/joblevel').then(resp => {
          if (resp) {
            this.joblevels = resp
            window.sessionStorage.setItem('joblevels', JSON.stringify(resp))
          }
        })
      } else {
        // 从 sessionStorage 获取，字符串转对象
        this.joblevels = JSON.parse(window.sessionStorage.getItem('joblevels'))
      }
      // 获取政治面貌
      if (!window.sessionStorage.getItem('politicsstatus')) {
        this.getRequest('/employee/basic/politicsStatus').then(resp => {
          if (resp) {
            this.politicsstatus = resp
            window.sessionStorage.setItem('politicsstatus', JSON.stringify(resp))
          }
        })
      } else {
        // 从 sessionStorage 获取，字符串转对象
        this.politicsstatus = JSON.parse(window.sessionStorage.getItem('politicsstatus'))
      }
      // 树形控件 绑定 所属部门 数据对象
      if (!window.sessionStorage.getItem('allDeps')) {
        this.getRequest('/employee/basic/deps').then(resp => {
          if (resp) {
            this.allDeps = resp
            window.sessionStorage.setItem('allDeps', JSON.parse(resp))
          }
        })
      } else {
        this.allDeps = window.sessionStorage.getItem('allDeps')
      }
    },
    // 添加员点击事件
    showAddEmpView() {
      this.emp = {
        id: null,
        name: '',
        gender: '',
        birthday: '',
        idCard: '',
        wedlock: '',
        nationId: null,
        nativePlace: '',
        politicId: null,
        email: '',
        phone: '',
        address: '',
        departmentId: null,
        jobLevelId: null,
        posId: null,
        engageForm: '',
        tiptopDegree: '',
        specialty: '',
        school: '',
        beginDate: '',
        workState: '在职',
        workID: '',
        contractTerm: null,
        conversionTime: '',
        notworkDate: null,
        beginContract: '',
        endContract: '',
        workAge: null,
        salaryId: null
      }
      this.inputDepName = '' // 清空部门信息
      this.title = '添加员工' // 点击添加员工按钮时，弹出框标题为 添加员工
      this.getMaxWorkID() // 获取最大工号
      this.initPositions() // 获取职位
      this.dialogVisible = true
    },
    // 分页
    sizeChange(size) {
      this.size = size;
      this.initEmps();
    },
    currentChange(currentPage) {
      this.currentPage = currentPage; 
      this.initEmps(); // 调用方法

    },
    initEmps(type) {
      this.loading = true // 添加 loading
      //定义高级搜索 url
      let url = '/employee/basic/?currentPage=' + this.currentPage + '&size=' + this.size
      if (type && type === 'advanced') { // 说明是高级搜索
        if (this.searchValue.politicId) {
          url += '&politicId=' + this.searchValue.politicId
        }
        if (this.searchValue.nationId) {
          url += '&nationId=' + this.searchValue.nationId
        }
        if (this.searchValue.posId) {
          url += '&posId=' + this.searchValue.posId
        }
        if (this.searchValue.jobLevelId) {
          url += '&jobLevelId=' + this.searchValue.jobLevelId
        }
        if (this.searchValue.engageForm) {
          url += '&engageForm=' + this.searchValue.engageForm
        }
        if (this.searchValue.departmentId) {
          url += '&departmentId=' + this.searchValue.departmentId
        }
        if (this.searchValue.beginDateScope) {
          url += '&beginDateScope=' + this.searchValue.beginDateScope
        }
      } else {
        url += '&name=' + this.empName
      }
      // 添加分页参数 ?currentPage='+this.currentPage+'&size='+this.size
      // 添加用户名搜索参数 +'&name='+this.empName,传参 根据条件搜索，不传参查询所有
      this.getRequest(url).then(resp => {
        this.loading = false 
        if (resp) {
          this.emps = resp.data
          this.total = resp.total 
        }
      });
    }
  }
}
</script>

<style>
/* 展开收起条件搜索框动画样式 */
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
