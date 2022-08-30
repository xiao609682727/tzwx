// 配置编译环境和线上环境之间的切换

// let baseUrl = 'https://wx.tongzhoushidai.com/api';
let baseUrl = 'http://localhost:1888/api';
//通过变量动态获取baseurl
const API_PATH = process.env.VUE_APP_API_PATH
if(""!=API_PATH){
  baseUrl = API_PATH;
}
export {
  baseUrl, // 客户端id
}

