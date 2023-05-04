/*
 * @Author: zyy 1741540959@qq.com
 * @Date: 2023-01-11 16:00:34
 * @LastEditors: zyy
 * @LastEditTime: 2023-02-24 19:07:12
 * @Description: file content
 */
import { ref, reactive } from 'vue'
import { defineStore } from 'pinia'
import api from '@/api/request'
import type { EmpState,Position } from '@/types/emp/emp'
import { ElMessage,ElMessageBox, ElNotification } from 'element-plus'
// 在线聊天-导入下载的在线聊天依赖
import {Client} from '@stomp/stompjs'
import SockJS from 'sockjs-client/dist/sockjs.min.js'
import type { Admin, MsgObj } from '@/types/emp/chat'

interface userInfo {
  name: string,
  phone: string,
  telephone: string,
  address: string,
  enabled: boolean,
  username: string,
  userFace: string,
  [propsName: string]: any
}

export interface routesState {
  path:string
  name:string,
  iconCls:string,
  children:routesState[],
  hidden?:boolean,
  component:()=>void
}

// 员工资料搜索参数
interface EmpSelect {
  currentPage: number,
  size: number,
  politicId?: string,
  nationId?: string,
  posId?: string,
  jobLevelId?: string,
  departmentId?: string,
  beginDateScope?: string,
  engageForm?: string,
  name?: string
}

interface doukeyi {
  [propName:string]:any
}

interface MessageState{
  content:string,
  date:string
}

interface SessionsState{
  id:number,
  user:{
    name:string,
    img:string
  },
  messages:MessageState[]
}

// 方便重置状态里的单一变量，因为pinia只能重置整个state状态（$reset
const defaultData = {
  emp: {
    id: '',
    name: '',
    gender: '',
    birthday: '',
    idCard: '',
    wedlock: '',
    nationId: '',
    nativePlace: '',
    politicId: '',
    email: '',
    phone: '',
    address: '',
    departmentId: '',
    jobLevelId: '',
    posId: '',
    engageForm: '',
    tiptopDegree: '',
    specialty: '',
    school: '',
    beginDate: '',
    workState: '在职',
    workID: '',
    contractTerm: '',
    conversionTime: '',
    notworkDate: '',
    beginContract: '',
    endContract: '',
    workAge: '',
    salaryId: ''
  }
}

// 自定义存储方式
const tokenSession:Storage={
  setItem(key,state:any){
    // 传进来的state是string 因此要先转换成对象
    let data = JSON.parse(state)
    // 如果直接写成data.key 或者直接写data[kay] 都是等价于直接取data对象的key字段属性 而不是取key变量所对应的值的字段属性
    return sessionStorage.setItem(key,data[`${key}`])
  },
  getItem(key){
    return JSON.stringify({
      token:sessionStorage.getItem(key)
    })
  }
}

// store本身就是一个reactive对象
export const store = defineStore('index', {
  state: () => {
    // pinia不需要在这里写 这是vuex的写法,vuex最后return所有变量即可
    // pinia如果想用ts约束类型，就自己定义接口然后直接在return那块初始化并用断言来约束
    /* 
    let token:string = ''
    let routes:routesState[] = []
    let currentAdmin = JSON.parse(window.sessionStorage.getItem('currentAdmin') || '{}')
    let empSelect:EmpSelect = reactive<EmpSelect>({
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
    */
  //  pinia拥有自动类型推断
    return {
      // 登录信息和路由信息
      token:'',
      routes:[] as routesState[],
      currentAdmin:JSON.parse(window.sessionStorage.getItem('currentAdmin') || '{}'),
      // 员工资料
      perData:[],
      total:0,
      loading:false,
      empSelect:{
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
      } as EmpSelect,
      // addEmp组件
      dialogVisible:false,
      title:'',
      emp: {
        id: '',
        name: '',
        gender: '',
        birthday: '',
        idCard: '',
        wedlock: '',
        nationId: '',
        nativePlace: '',
        politicId: '',
        email: '',
        phone: '',
        address: '',
        departmentId: '',
        jobLevelId: '',
        posId: '',
        engageForm: '',
        tiptopDegree: '',
        specialty: '',
        school: '',
        beginDate: '',
        workState: '在职',
        workID: '',
        contractTerm: '',
        conversionTime: '',
        notworkDate: '',
        beginContract: '',
        endContract: '',
        workAge: '',
        salaryId: ''
      } as EmpState,
      positions:[] as Position[],
      // 用于回显部门数据
      inputDepName:'',
      /* chat模块 */
      filterKey: '',
      stomp: null as null|Client, //在线聊天-定义对象
      idDot: {} as doukeyi, // 未读消息 对象
      // 当前聊天窗口对象
      currentSession: null as null|Admin,
      admins: [] as Admin[],//获取接口返回的其他用户的数据
      sessions: {} as doukeyi,//保存聊天记录
    }
  },
  getters: {

  },
  actions: {
      // 分页获取所有员工
    async initEmps() {
      this.loading = true
      await api.get('/employee/basic', this.empSelect)
        .then(
          (res: any) => {
            // 这么写是因为直接给reactive对象赋值会失去响应性
            // Object.assign(data,res.data)
            this.perData = res.data
            this.total = res.total
            this.loading = false;
          }
        )
    },
    // 添加员工按钮触发弹窗的点击事件
    showAddEmpView(){
      //只重置特定状态
      Object.assign(this,defaultData)
      // 设置addEmp组件可见性
      this.dialogVisible = true
      // 修改addEmp组件标题
      this.title = '添加员工'
      // 获取最大工号 自动填充
      api.get('/employee/basic/maxWorkID').then((resp:any)=>{
        if(resp){
          this.emp.workID = resp.obj
        }
      })
      this.inputDepName = ''//清空部门
      this.initPositions()
    },
        // 获取职位 有可能变动 打开对话框的时候调用此方法
    initPositions() {
      api.get('/employee/basic/position').then((resp:any) => {
        if (resp) {
          this.positions = resp
        }
      })
    },
    // 编辑员工按钮触发弹窗的点击事件
    showEmpView(data:any){
      this.title = '编辑员工信息'
      console.log('this.emp:'+JSON.stringify(this.emp));
      this.emp = data
      console.log('this.emp:'+JSON.stringify(this.emp));
      this.inputDepName = data.department.name // 回显部门信息
      this.initPositions() // 初始化职位信息
      this.dialogVisible = true
    },
    // 删除员工
    deleteEmp(data:any) {
      ElMessageBox.confirm('此操作将永久删除该员工' + data.name + ', 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        api.del('/employee/basic/' + data.id).then(resp => {
          if (resp) {
            this.initEmps()
          }
        })
      }).catch(() => {
        ElMessage({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    /* ws模块 */
    // 点击事件-改变currentsession
    changeCurrentSession(data:Admin) {
      this.currentSession = data;
      this.idDot[this.currentAdmin.username+'#'+this.currentSession.username] = false
      // 添加响应式属性
      // Vue.set(state.idDot, state.currentAdmin.username + '#' + state.currentSession.username, false)
    },
    // 通过接口获取其他所有操作员
    init_data(){
      api.get('/chat/').then((resp:any)=>{
        if(resp){
          this.admins = resp
        }
      },e=>console.log(e))
    },
    // ws连接
    connect() {
      let a = this.token
      this.stomp = new Client({
        // 连接头信息，放认证登录信息
        connectHeaders:{
          'Auth-Token': a
        },
        // brokerURL:'ws:'+location.host+'/ws/ep',//连接url
        debug:function(str){
          console.log('STOMP:'+str);
        },
        // 失败重连时间
        reconnectDelay:5000,
        webSocketFactory:()=>{ return new SockJS('/ws/ep')},
        // 发生错误的监听
        onStompError:(frame)=>{
          console.log('Broker reported error:' + frame.headers['message']);
          console.log('Additional details:' + frame.body);
        },
        // 连接成功的监听，订阅在其内部完成
        onConnect:()=>{
          // 订阅/user/queue/chat消息频道 返回的对象有一个unsubscribe的方法
          this.stomp!.subscribe('/user/queue/chat', msg=>{
            console.log(`Received:${msg.body}`);
            let receiveMsg = JSON.parse(msg.body)
            // 有消息发来，右下角 弹框提示
            if (!this.currentSession || receiveMsg.from !== this.currentSession.username) {
              ElNotification({
                  title: '【' + receiveMsg.fromNickName + '】发来一条消息',
                  message: receiveMsg.content.length > 10 ? receiveMsg.content.substr(0, 10) : receiveMsg.content,
                  position: 'bottom-right'
              })
              // 有未读消息 展示小红点
              this.idDot[this.currentAdmin.username + '#' + receiveMsg.from] = true
            }
            receiveMsg.notSelf = true;//接收来的消息肯定不是自己发的
            // 改变一下指向，避免出现我#我的属性
            receiveMsg.to = receiveMsg.from;
            this.addMessage(receiveMsg)
          })
        }
      })
      // 开启连接
      this.stomp.activate()
    },
    // 保存聊天记录
    addMessage(msg:MsgObj){   
      let me = this.currentAdmin.username;
      let he = msg.to;
      // 如果不存在使用者和对方的聊天记录，那么就新增加一条属性，然后在对象数组中push
      // 记录我和他人的聊天记录用的是 session[我的名字#他人名字]:[{content,date,self},...]
      if(!this.sessions[me+'#'+he]){
        this.sessions[me+'#'+he] = []
      }
      this.sessions[me+'#'+he].push({
        content:msg.content,
        date:new Date(),
        self:!msg.notSelf//不是自己
        /* 如果是自己发送的消息 就会出现我#我的情况 
           之前在usertext组件中默认传过来的是false，取反后为true，说明是自己发送的消息，
           这样方便进一步做处理
        */
      })
    }
  },
  persist: {
    enabled: true,//只写这个配置项而不写其他的话，则代表整个仓库都存储持久化
    strategies: [
      { key:'token',storage: tokenSession, paths: ['token'] },//指定密钥名称、字段及存储方式
    ]
  }
})
