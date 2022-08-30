import Vue from 'vue'
import Vuex from 'vuex'
//导入站点配置相关
import website from './modules/website'
import user from './modules/user'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    website,
    user
  }
})
