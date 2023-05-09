# 项目介绍    
本项目是基于springboot和vue全家桶实现的中小型企业在线办公系统，涵盖了人事管理、部门管理、职位管理、权限管理以及在线聊天室等模块。目前前端部分已完成vue2和vue3两个版本。    

## 技术栈    
服务端：Spring Boot、Spring Security、Mybatis-plus、Swagger2、Redis      
前端vue2版本：vue2、vuex、element-ui、webpack      
前端vue3版本：vue3、pinia、element-plus、vite      

# 功能介绍    
1.用户登录    
用户登陆是在线办公系统的入口，用户登陆界面为初始化界面，对操作员登录权限进行核实确认，依据操作员的相应角色，赋予其不同等级的权限。    
2.用户管理    
操作员可对自身资料信息和登录密码进行修改。    
3.员工管理    
操作员可对员工资料进行增删改查，查询功能可分为精准查询与模糊查询。操作员可用excel文件形式导出并保存员工资料表。    
操作员可对员工奖惩信息进行增删改。    
4.薪资管理    
操作员可对部门工资账套进行增删改，对企业员工工资账套进行修改。    
5.部门管理    
操作员可对各部门进行增删查。    
6.职位职称管理    
操作员可对企业现有职位职称进行增删改。    
7.权限组    
管理员可对操作员角色相应权限进行修改，获取不同的菜单权限。    
8.操作员管理    
系统管理员可对全体操作员进行增删改查，并可更改操作员登录权限。    
9.聊天平台    
操作员之间可进行即时的文字发送，有未读信息提示功能。    

# 使用说明    
vue2:    
``` sh
npm run serve
```    
vue3:    
``` sh
npm run dev
```    

# 目录结构说明    
jrs    
├─ back-front                 //后端代码    
│  ├─ pom.xml    
│  ├─ yeb-generator           //代码生成器的配置    
│  │  ├─ pom.xml    
│  │  └─ src    
│  │     └─ main    
│  │        ├─ java    
│  │        │  └─ com    
│  │        │     └─ shixun    
│  │        │        └─ generator    
│  │        └─ resources    
│  └─ yeb-server              //服务端    
│     ├─ pom.xml    
│     └─ src    
│        └─ main    
│           ├─ java    
│           │  └─ com    
│           │     └─ shixun    
│           │        └─ server    
│           │           ├─ config      //配置文件    
│           │           │  ├─ filter   //权限控制    
│           │           │  ├─ redis    
│           │           │  ├─ security  // SpringSecurity配置    
│           │           │  └─ webSocket    
│           │           ├─ controller   //控制层    
│           │           ├─ converter    // 转换工具类    
│           │           ├─ exception    //全局异常    
│           │           ├─ mapper    
│           │           ├─ pojo         //实体类    
│           │           ├─ service      //服务类    
│           │           │  └─ impl      //服务实现类    
│           │           └─ utils      //通用工具类    
│           │           └─ YebApplication.java    
│           └─ resources    
├─ vue2_version             //前端vue2版本代码    
│  ├─ babel.config.js    
│  ├─ jsconfig.json    
│  ├─ package-lock.json    
│  ├─ package.json    
│  ├─ public                //静态资源    
│  ├─ src    
│  │  ├─ assets             //字体 主题等静态资源    
│  │  ├─ components         //公共级组件    
│  │  ├─ main.js    
│  │  ├─ router             //路由配置    
│  │  ├─ store              //vuex仓库    
│  │  ├─ utils              //工具类    
│  │  └─ views              //页面级组件    
   ├─ App.vue    
│  └─ vue.config.js    
└─ vue3_version             //前端vue3版本代码    
   ├─ auto-imports.d.ts    
   ├─ components.d.ts    
   ├─ env.d.ts    
   ├─ index.html    
   ├─ package-lock.json    
   ├─ package.json    
   ├─ public    
   ├─ src    
   │  ├─ api                //请求的配置    
   │  ├─ assets    
   │  ├─ components    
   │  ├─ main.ts    
   │  ├─ router    
   │  ├─ stores             //pinia仓库    
   │  ├─ types              //约束数据类型    
   │  ├─ utils    
   │  └─ views    
   ├─ App.vue    
   ├─ tsconfig.config.json    
   ├─ tsconfig.json    
   └─ vite.config.ts    
    
