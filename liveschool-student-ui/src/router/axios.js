/**
 * 全站http配置
 *
 * axios参数说明
 * isSerialize是否开启form表单提交
 * isToken是否需要token
 */
import axios from 'axios'
import router from '@/router/'
import {getToken} from '@/util/auth'
import website from '@/config/website';
import {Base64} from 'js-base64';
import Store from '../store/';
import Vue from 'vue'

axios.defaults.timeout = 30000;
//返回其他状态吗
axios.defaults.validateStatus = function (status) {
  return status >= 200 && status <= 500; // 默认的
};
//跨域请求，允许保存cookie
axios.defaults.withCredentials = true;
//HTTPrequest拦截
axios.interceptors.request.use(config => {
  const meta = (config.meta || {});
  const isToken = meta.isToken === false;
  config.headers['Authorization'] = `Basic ${Base64.encode(`${website.clientId}:${website.clientSecret}`)}`;
  if (getToken() && !isToken) {
    config.headers['Crazy-Auth'] = 'bearer ' + getToken() // 让每个请求携带token--['Authorization']为自定义key 请根据实际情况自行修改
  }

  //headers中配置serialize为true开启序列化
/*  if (config.method === 'post' && meta.isSerialize === true) {
    config.data = serialize(config.data);
  }*/
  return config
}, error => {
  return Promise.reject(error)
});
let flag = true;
//HTTPresponse拦截
axios.interceptors.response.use(res => {
  const status = res.data.code || 200
  const message = res.data.msg || '未知错误';
  //如果是600 则是异地登录弹出提示，跳入主页
  if (status === 600) {
    //清理本地session
    if(flag){
      flag = false;
      Store.dispatch('FedLogOut').then(() => {
        Vue.prototype.$layer.alert(
          "您的账号被别人挤下！",
          {
            shade: true,
            icon: 0,
            title: "提示"
          },
          laeryid => {
            flag = true;
            router.push({path: '/'})
            Vue.prototype.$layer.close(laeryid);
          }
        );
      });
    }
    return res;
  }
  //如果是401则跳转到登录页面
  if (status === 401) {
    //清理本地session
    Store.dispatch('FedLogOut').then(() => router.push({path: '/user/login'}));
    // Store.dispatch('FedLogOut')
    return;
  }
  // 如果请求为非200否者默认统一处理
  if (status !== 200) {
    return Promise.reject(new Error(message))
  }
  return res;
}, error => {
  return Promise.reject(new Error(error));
})

export default axios;
