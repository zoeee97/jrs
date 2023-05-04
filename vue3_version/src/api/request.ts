/*
 * @Author: zyy 1741540959@qq.com
 * @Date: 2023-01-06 17:07:31
 * @LastEditors: zyy
 * @LastEditTime: 2023-02-20 19:07:26
 * @Description: file content
 */
import axios, { type AxiosRequestHeaders } from 'axios'
import { ElMessage } from 'element-plus'

// 创建实例
const request = axios.create({
  baseURL: "/api",//设置基础路径，所有请求路径开头会自带/api，配合配置代理时匹配所有请求地址里包含/api的路径，将此路径进行跨域代理操作
  // 这么写在ts里没用 可能也是要自己定义接口
  // headers: {
  //   Authorization : sessionStorage.getItem('token')
  // }
})

interface paramsObj {
  [propName: string]: any
}

// 配置请求拦截器
request.interceptors.request.use(config => {
  // 如果存在 token，请求携带这个 token( 登录的时候 把 token 存入了 sessionStorage ）
  if (window.sessionStorage.getItem("token")&&window.sessionStorage.getItem("token")!=undefined) {
  //   // token 的key : Authorization ; value: token
    (config.headers as AxiosRequestHeaders)['Authorization'] = window.sessionStorage.getItem('token');
  }
  return config;
}, error => {
  console.log(error);
})

// 配置响应拦截器
request.interceptors.response.use(
  // 成功的回调
  (res) => {
    return res.data;
  },
  // 失败的回调
  (error) => {
    /***** 接收到异常响应的处理开始 *****/
    if (error && error.response) {
      // 1.公共错误处理
      // 2.根据响应码具体处理
      switch (error.response.status) {
        case 400:
          error.message = '错误请求'
          break;
        case 401:
          error.message = '未授权，请重新登录'
          break;
        case 403:
          error.message = '拒绝访问'
          break;
        case 404:
          error.message = '请求错误,未找到该资源'
          window.location.href = "/NotFound"
          break;
        case 405:
          error.message = '请求方法未允许'
          break;
        case 408:
          error.message = '请求超时'
          break;
        case 500:
          error.message = '服务器端出错'
          break;
        case 501:
          error.message = '网络未实现'
          break;
        case 502:
          error.message = '网络错误'
          break;
        case 503:
          error.message = '服务不可用'
          break;
        case 504:
          error.message = '网络超时'
          break;
        case 505:
          error.message = 'http版本不支持该请求'
          break;
        default:
          error.message = `连接错误${error.response.status}`
      }
    } else {
      // 超时处理
      if (JSON.stringify(error).includes('timeout')) {
        console.log('服务器响应超时，请刷新当前页')
      }
      error.message = '连接服务器失败'
    }

    ElMessage.error(error.message)
    /***** 处理结束 *****/
    //如果不需要错误处理，以上的处理过程都可省略

    return Promise.reject(error);
  }
)

function formatUrl(url:string,query:paramsObj):string{
  let queryArr = [];
  for(let key in query){
    // encodeURIComponent函数可把字符串作为 URI 组件进行编码。
    queryArr.push(`${key}=${encodeURIComponent(query[key])}`)
  }
  /* join() 方法将一个数组（或一个类数组对象）的所有元素连接成一个字符串并返回这个字符串，
  用逗号或指定的分隔符字符串分隔。如果数组只有一个元素，那么将返回该元素而不使用分隔符。 */
  return url+'/?'+queryArr.join('&');
}


// 封装请求
const get = (url: string = ``, query?:paramsObj,params?: paramsObj) => {
  // 注意这么封装是携带params参数 query参数是直接后缀在url的
  /* 因此这里我写接收多一个参数，请求时若存在query参数则直接封装成一个对象，然后把参数传递进来，
      然后写一个工具类函数去将这个参数整理到url后缀中 */
  if(query){
    url = formatUrl(url,query)
  }
  console.log('url:'+url);
  return request.get(url, params)
}
const post = (url: string = ``, params?: paramsObj) => {
  console.log('params：'+JSON.stringify(params));
  return request.post(url, params)
}
const del = (url: string = ``, params?: paramsObj) => {
  return request.delete(url, { params })
}
const put = (url: string = ``, params?: paramsObj) => {
  return request.put(url, params)
}

export default {
  get, post, del, put
}