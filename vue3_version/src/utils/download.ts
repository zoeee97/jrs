/*
 * @Author: zyy 1741540959@qq.com
 * @Date: 2023-02-20 13:22:21
 * @LastEditors: zyy
 * @LastEditTime: 2023-02-24 18:45:42
 * @Description: file content
 */
// 封装导出数据工具类

/* axios本身并不支持下载的功能，因此我们要利用js-file-download插件 

    Content-Type: application/octet-stream
Content-Disposition: attachment;filename=2018-03-03_15-47-45--2019-03-03_15-48-13--73c75b84-ba2a-470f-a713-07216fcd214b.xlsx

Server 端实现文件下载

    打开文件，将文件内容写入到 response 中
    设置 Response header（content-disposition，content-type）

一般有两种方式：

    inline：直接在页面显示
    attchment：以附件形式下载

Content-disposition是MIME协议的扩展，MIME协议指示MIME用户代理如何显示附加的文件。当Internet Explorer接收到头时，他会激活文件下载对话框，它的文件名框自动填充headers指定的文件名。
服务器向浏览器发送文件时，如果是浏览器支持的文件类型，一般会默认使用浏览器打开，比如txt、jpg等。如果需要提示用户保存，就要利用Content-Disposition进行处理，（敲黑板，划重点）关键在于一定要加上attachment 。
*/

// 本身也是通过 axios 调用后端接口 创建另一个axios实例对象 但依然要检测token 所以配置请求拦截器
import axios, { type AxiosRequestHeaders } from "axios";

// 让 axios 默认带一个响应的类型 arraybuffer 二进制数组 流  就是二进制
const service = axios.create({
    responseType: 'arraybuffer',
})

// 请求拦截器
service.interceptors.request.use(config => {
    if (window.sessionStorage.getItem("token")&&window.sessionStorage.getItem("token")!=undefined) {
      (config.headers as AxiosRequestHeaders)['Authorization'] = window.sessionStorage.getItem('token');
    }
    return config;
  }, (error) => {
    console.log(error);
  })

// 响应拦截器
service.interceptors.response.use((resp) => {
    const headers = resp.headers // 拿到请求头
    /* 请求成功时，后端返回文件流，正常导出文件;
请求失败时，后端返回json对象，如：{"Status":"false","StatusCode":"500","Result":"操作失败"}，也被转成了arraybuffer
    此时请求成功和失败返回的http状态码都是200
    所以才要判断是流还是字符串 */
    let reg = RegExp(/application\/json/) // 判断返回的是 流 还是 Json 字符串
    // 如果请求头 匹配正则 说明是一个普通请求
    // 因为大部分后端返回的结果都是JSON字符串，只有一些特殊的接口是以流的形式返回
    // 使用声明的可选属性时，报“未定义”  使用可选链操作符 ?. 的方式，已经确定当前值有可能为空的情况下使用
    if (headers['content-type']?.match(reg)) {//content-type指示响应内容的格式
        resp.data = unitToString(resp.data) // 拿到 json 字符串对象
    } else {
        let fileDownload = require('js-file-download') // 插件
        let fileName = headers['content-disposition']?.split(';')[1].split('filename=')[1]//文件名
        let contentType = headers['content-type'] // 响应类型
        fileName = decodeURIComponent(fileName as string) // 格式转换 防止乱码
        fileDownload(resp.data, fileName, contentType) // 通过插件下载文件
    }
    return resp
}, (error) => {
    console.log(error)
})

// 将已转为arraybuffer类型的数据转回Json对象
// 处理 json 格式的转换( 编码 解码 ）
function unitToString(unitArray:any) {
    // Uint8Array 数组类型表示一个8位无符号整型数组，创建时内容被初始化为0
    // Javascript 的 Uint8Array 支持字节数据，对于操作二进制数据非常有用
    // String.fromCharCode函数需要一个数字数组(number[])作为参数，因此需要在传递Uint8Array之前将它转换为一个数字数组
    let encodedString = String.fromCharCode.apply(null, [...new Uint8Array(unitArray)])
    let decodedString = decodeURIComponent(escape(encodedString))
    return JSON.parse(decodedString) // 转对象
}

// 下载文件的请求
export const downloadRequest = (url:string) => {
    return service.get(url)
}

// 整体暴露出去，供外界调用
export default service;
