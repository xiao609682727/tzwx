import Vue from 'vue';
import axios from './router/axios';
import VueAxios from 'vue-axios';
import App from './App';
import router from './router/router';
import './permission'; // 权限
import './error'; // 日志
import store from './store';
import * as urls from '@/config/env';
import Element from 'element-ui';
import Avue from '@smallwei/avue';
import '@smallwei/avue/lib/index.css';

// elementui 全局变量 esc 不关闭弹窗
Element.Dialog.props.closeOnPressEscape.default = false
Element.Dialog.props.closeOnClickModal.default = false;

import i18n from './lang' // Internationalization
import './styles/common.scss';

import basicContainer from './components/basic-container/main'

//引用富文本
import AvueUeditor from 'avue-plugin-ueditor'
import VueQrcode from '@chenfengyuan/vue-qrcode';//引入二维码生成


Vue.use(AvueUeditor);
Vue.use(router)
Vue.use(VueAxios, axios)
Vue.use(Element, {
    i18n: (key, value) => i18n.t(key, value)
})

Vue.use(Avue, {
    i18n: (key, value) => i18n.t(key, value)
})
//注册全局容器
Vue.component('basicContainer', basicContainer)
//二维码
Vue.component(VueQrcode.name, VueQrcode);
// 加载相关url地址
Object.keys(urls).forEach(key => {
    Vue.prototype[key] = urls[key];
})


Vue.config.productionTip = false;

new Vue({
    router,
    store,
    i18n,
    render: h => h(App)
}).$mount('#app')
