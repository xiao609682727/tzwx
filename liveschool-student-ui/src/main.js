import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import './error'; // 日志
import VueQrcode from '@chenfengyuan/vue-qrcode';//引入二维码生成
import layer from 'vue-layer'
import * as urls from '@/config/env';
import VueLazyLoad from 'vue-lazyload' // 图片懒加载
//vue动态title
import VueWechatTitle from 'vue-wechat-title'
// import echarts from 'echarts'


// 图片懒加载
Vue.use(VueLazyLoad, {
  preLoad: 1,
  error: '/static/img/coures.gif',
  loading: '/static/img/coures.gif',
  attempt: 5,
  lazyComponent: true
})
//vue动态title
Vue.use(VueWechatTitle)
Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
//引入二维码生成
Vue.component(VueQrcode.name, VueQrcode);

// 在env 中配置全局ip加载相关url地址
Object.keys(urls).forEach(key => {
  Vue.prototype[key] = urls[key];
})
//弹层引用
Vue.prototype.$layer = layer(Vue);
// Vue.prototype.$echarts = echarts

Vue.filter('dataFormat', function (value, fmt) {
  let getDate = new Date(value);
  let o = {
    'M+': getDate.getMonth() + 1,
    'd+': getDate.getDate(),
    'h+': getDate.getHours(),
    'm+': getDate.getMinutes(),
    's+': getDate.getSeconds(),
    'q+': Math.floor((getDate.getMonth() + 3) / 3),
    'S': getDate.getMilliseconds()
  };
  if (/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (getDate.getFullYear() + '').substr(4 - RegExp.$1.length))
  }
  for (let k in o) {
    if (new RegExp('(' + k + ')').test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)))
    }
  }
  return fmt;
});

