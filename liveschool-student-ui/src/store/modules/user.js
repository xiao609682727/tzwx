import {setToken, removeToken} from '@/util/auth'
import {setStore, getStore,removeStore} from '@/util/store'
import {login, getUserInfo,  logout, refeshToken,checkToken} from '@/api/user'
import {msgreceiveList} from "@/api";
import { validatenull } from "@/util/validate";
import { calcDate } from "@/util/date.js";

const user = {
  state: {
    isLogin:getStore({name: 'isLogin'}) || false,
    letterCount:getStore({name: 'letterCount'}) || 0,
    studentInfo: [],
    userInfo: getStore({name: 'userInfo'}) || [],
    permission: getStore({name: 'permission'}) || {},
    roles: [],
    token: getStore({name: 'token'}) || '',
    refeshToken: getStore({name: 'refeshToken'}) || '',
    timeOut:null
  },
  actions: {
    setTime({dispatch,state, commit}){
      //token 监测更新
      //清理定时
      if ( state.timeOut ) {
        clearInterval(state.timeOut);
      }
      //创建定时
      state.timeOut = setInterval(() => {

        const token = getStore({
          name: "token",
          debug: true
        });
        if(token){
          const date = calcDate(token.datetime, new Date().getTime());
          if (validatenull(date)) return;
          if (date.seconds >= 1200) {
            dispatch("RefeshToken")
              .then((res) => {
                console.log(res)
              })
              .catch((error) => {
                console.log(error)
              });
          }
        }


      }, 600000);
    },
    //根据用户名登录
    LoginByUsername({dispatch,state, commit}, userInfo) {
      commit('SET_TOKEN', userInfo.accessToken);
      commit('SET_USERIFNO', userInfo);
      //是否保持登录
      if(userInfo.saveLogin){
        commit('SET_REFESHTOKEN', userInfo.refreshToken);
      }
    },
    //根据手机号登录
    LoginByPhone({dispatch,state,commit}, userInfo) {
      commit('SET_TOKEN', userInfo.accessToken);
      commit('SET_USERIFNO', userInfo);
      //是否保持登录
      if(userInfo.saveLogin){
        commit('SET_REFESHTOKEN', userInfo.refreshToken);
      }
    },
    updateLetterCount({commit},userInfo){
      //查询站内信
      let params = {
        "current":1,
        "size":20,
        "status":0,
        "receivingCusid":userInfo.id
      }
      msgreceiveList(params).then(res =>{
        if(res){
          if(res.data.code == 200){
            commit('SET_LETTER', res.data.data.total);
          }
        }

      })
    },
    GetUserInfo({commit}) {
      return new Promise((resolve, reject) => {
        getUserInfo().then((res) => {
          const data = res.data.data;
          commit('SET_STUDENT', data);
          resolve(data);
        }).catch(err => {
          reject(err);
        })
      })
    },
    checkToken({commit},token) {
      return new Promise((resolve, reject) => {
        checkToken(token).then((res) => {
          const data = res.data.data;
          if(data){
            commit('SET_ISLOGIN',data);
          }else{
            commit('CLEAR');
          }
          resolve(data);
        }).catch(err => {
          reject(err);
        })
      })
    },
    //刷新token
    RefeshToken({dispatch,state, commit}) {
      return new Promise((resolve, reject) => {
        refeshToken(state.refeshToken).then(res => {
          const data = res.data.data;
          commit('SET_TOKEN', data.accessToken);
          commit('SET_USERIFNO', data);
          getUserInfo().then((res) => {
            const data = res.data.data;
            commit('SET_STUDENT', data);
            resolve(data);
          }).catch(err => {
            reject(err);
          })
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 登出
    LogOut({commit,state}) {
      return new Promise((resolve, reject) => {
        logout().then(() => {

          commit('SET_TOKEN', '')
          // commit('SET_MENU', [])
          commit('SET_STUDENT', {});
          commit('SET_USERIFNO', {});
          commit('CLEAR');
          removeToken()
          clearInterval(state.timeOut);
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    //注销session
    FedLogOut({commit}) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        commit('SET_STUDENT', {});
        commit('SET_USERIFNO', {});
        commit('CLEAR');
        removeToken()
        resolve()
      })
    },
  },
  mutations: {
    SET_REFESHTOKEN: (state, refeshToken) => {
      state.refeshToken = refeshToken;
      setStore({name: 'refeshToken', content: state.refeshToken})
    },
    SET_TOKEN: (state, token) => {
      setToken(token)
      state.token = token;
      setStore({name: 'token', content: state.token})
    },
    SET_USERIFNO: (state, userInfo) => {
      state.userInfo = userInfo;
      setStore({name: 'userInfo', content: state.userInfo})
    },
    SET_STUDENT: (state, studentInfo) => {
      state.isLogin = true;
      setStore({name: 'isLogin', content: state.isLogin, type: 'session'})
      state.studentInfo = studentInfo;
      // setStore({name: 'studentInfo', content: state.studentInfo, type: 'session'})

    },
    SET_ISLOGIN: (state,isLogin) => {
      state.isLogin = isLogin;
      setStore({name: 'isLogin', content: isLogin, type: 'session'})
    },
    SET_LETTER: (state, count) =>{
      state.letterCount = count
      setStore({name: 'letterCount', content: state.letterCount})
    },
    CLEAR:(state) => {
      state.isLogin = false;
      state.refeshToken = "";
      removeStore({name: 'isLogin', type: 'session'})
      // removeStore({name: 'studentInfo', type: 'session'})
      removeStore({name: 'userInfo'})
      removeStore({name: 'token'})
      removeStore({name: 'refeshToken'})
    },
  },
  getters: {
    userInfo: state => {
      return state.userInfo
    },
    studentInfo: state => {
      return state.studentInfo
    },
    isLogin: state => {
      return state.isLogin
    },
    token: state => {
      return state.token
    },
    refeshToken: state => {
      return state.refeshToken
    },
    letterCount: state => {
      return state.letterCount
    },
    timeOut: state =>{
      return state.timeOut
    }
  }

}
export default user
