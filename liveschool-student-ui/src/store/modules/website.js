/**
 * 站点缓存
 */
import {websiteProfile} from "@/api";
import { setStore, getStore } from '@/util/store'
import { validatenull } from '@/util/validate';
import {websiteNavigate} from "@/api";

const website = {
  state: {
    websiteConfig: getStore({ name: 'websiteConfig' }) || {},//站点设置
    websiteNavigate: getStore({ name: 'websiteNavigate' }) || [],//站点导航
    bottomLink: getStore({ name: 'bottomLink' }) || [],//底部标签
    friendLink: getStore({ name: 'friendLink' }) || [],//友情链接
  },
  mutations:{
    SET_WESITECONFIG: (state,websiteConfig) =>{
        state.websiteConfig = websiteConfig
    },
    SET_WEBSITENAVIGATE: (state,websiteNavigate) =>{
      state.websiteNavigate = websiteNavigate
    },
    SET_BOTTOMLINK: (state,bottomLink) =>{
      state.bottomLink = bottomLink
    },
    SET_FRIENDLINK: (state,friendLink) =>{
      state.friendLink = friendLink
    },
  },
  actions: {
    getWebsiteConfig({state,commit}){
      return new Promise((resolve, reject) => {
        //判断如果站点配置有值则不更新数据
        if(validatenull(state.websiteConfig)){
          websiteProfile("web,qqlogin,weibologin,wechatlogin,wechatpay,alipay,webregister,networklogin").then(res =>{
            commit('SET_WESITECONFIG',res.data.data);
            //将数据存入本地浏览器seession
            setStore({ name: 'websiteConfig', content: res.data.data ,type: 'session'})
            resolve(res.data.data);
          })
        }else{
          resolve(state.websiteConfig);
        }
      })

    },
    getWebsiteNavigate({state,commit}){
      //判断如果站点配置有值则不更新数据
      if(validatenull(state.websiteNavigate)){
        websiteNavigate("1").then(res =>{
          commit('SET_WEBSITENAVIGATE',res.data.data);
          //将数据存入本地浏览器seession
          setStore({ name: 'websiteNavigate', content: res.data.data,type: 'session' })
        })
      }
    },
    getBottomLink({state,commit}){
      //判断如果站点配置有值则不更新数据
      if(validatenull(state.bottomLink)){
        websiteNavigate("4").then(res =>{
          commit('SET_BOTTOMLINK',res.data.data);
          //将数据存入本地浏览器seession
          setStore({ name: 'bottomLink', content: res.data.data,type: 'session' })
        })
      }
    },
    getFriendLink({state,commit}){
      //判断如果站点配置有值则不更新数据
      if(validatenull(state.bottomLink)){
        websiteNavigate("3").then(res =>{
          commit('SET_FRIENDLINK',res.data.data);
          //将数据存入本地浏览器seession
          setStore({ name: 'friendLink', content: res.data.data,type: 'session' })
        })
      }
    }
  },
  getters: {
    website: state => {
      return state.websiteConfig
    },
    navigation: state => {
      return state.websiteNavigate
    },
    bottomLink: state => {
      return state.bottomLink
    },
    friendLink: state => {
      return state.friendLink
    }
  }

}
export default website
