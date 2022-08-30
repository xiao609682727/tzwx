import Vue from 'vue'
import VueRouter from 'vue-router'
const index = ()=>import("@/views/index/index")
const courseList = ()=>import("@/views/course/courseList/courseList")
const courseInfo = ()=>import("@/views/course/courseInfo/courseInfo.vue")
const teacherList = ()=>import("@/views/teacher/teacherList/teacherList.vue")
const teacherInfo = ()=>import("@/views/teacher/teacherInfo/teacherInfo.vue")
const lineDownCourseList = ()=>import("@/views/course/lineDownCourse/lineDownCourseList")
const lineDownCourseInfo = ()=>import("@/views/course/lineDownCourseInfo/lineDownCourseInfo")

const loginIndex = ()=>import("@/views/user/loginIndex.vue")
const myCourse = ()=>import("@/views/ucenter/common/ucenter.vue")
const article = ()=>import("@/views/article/article.vue")
const player = ()=>import("@/views/ucenter/player/player.vue")
const live = ()=>import("@/views/ucenter/live/player.vue")
const privacy = ()=>import("@/views/privacy.vue")
const examList = ()=>import("@/views/exam/examList/examList.vue")
const examAnswers = ()=>import("@/views/exam/examAnswers/examAnswers.vue")
const examResult = ()=>import("@/views/exam/examResult/examResult.vue")
const examAnalysis = ()=>import("@/views/exam/examAnalysis/examAnalysis.vue")
const examIndex = ()=>import("@/views/exam/examIndex/examIndex.vue")
const examInfo = ()=>import("@/views/exam/examInfo/examInfo.vue")
const examErrorQuestionInfo = ()=>import("@/views/exam/examErrorQuestion/examErrorQuestionInfo.vue")
const examFavQuestionInfo = ()=>import("@/views/exam/examFavQuestion/examFavQuestionInfo.vue")


import {getToken} from '@/util/auth'
import Store from '../store/';

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'index',
    component: index,
    meta: {
      title: '首页'
    }
  },{
    path: '/course/courseList',
    name: 'courseList',
    component: courseList,
    meta: {
      title: '课程列表'
    }
  },{
    path: '/teacher/teacherList',
    name: 'teacherList',
    component: teacherList,
    meta: {
      title: '讲师列表'
    }
  },{
    path: '/teacher/teacherInfo/:id',
    name: 'teacherInfo',
    component: teacherInfo,
    meta: {
      title: '讲师详情'
    }
  },{
    path: '/course/courseInfo/:id',
    name: 'courseInfo',
    component: courseInfo,
    meta: {
      title: '课程详情',
    }
  },{
    path: '/course/confirmOrder/:id',
    name: 'confirmOrder',
    component: () => import( '../views/ucenter/myOrder/confirmOrder.vue'),
    meta: {
      title: '订单确认'
    }
  },{
    path: '/course/payCenter/:id',
    name: 'confirmOrder',
    component: () => import( '../views/ucenter/myOrder/payCenter.vue'),
    meta: {
      title: '支付中心'
    }
  },{
    path: '/lineDownCourse/list',
    name: 'lineDownCourseList',
    component: lineDownCourseList,
    meta: {
      title: '面授课程列表'
    }
  },{
    path: '/lineDownCourse/lineInfo/:id',
    name: 'lineDownCourseInfo',
    component: lineDownCourseInfo,
    meta: {
      title: '面授课程详情'
    }
  },{
    path: '/uc',
    name: 'uc',
    component: myCourse,
    meta: {
      title: '我的课程',
    },
    children: [
      {
        path: 'myCourse',
        name: 'myCourse',
        meta: {
          title: '我的点播',
          login: true,
        },
        component: () => import('../views/ucenter/myCourse/myCourse.vue')
      },{
        path: 'myLive',
        name: 'myLive',
        meta: {
          title: '我的直播',
          login: true,
        },
        component: () => import('../views/ucenter/myCourse/myCourse.vue')
      },{
        path: 'myPackage',
        name: 'myPackage',
        meta: {
          title: '我的班级',
          login: true,
        },
        component: () => import('../views/ucenter/myCourse/myCourse.vue')
      },{
        path: 'myLineDownCourse',
        name: 'myLineDownCourse',
        meta: {
          title: '我的活动',
          login: true,
        },
        component: () => import('../views/ucenter/lineDownCourse/myLineDownCourse')
      },{
        path: 'myLineEndCourse',
        name: 'myLineEndCourse',
        meta: {
          title: '过期课程',
          login: true,
        },
        component: () => import('../views/ucenter/lineDownCourse/myLineEndCourse')
      },{
        path: 'courseCollection',
        name: 'courseCollection',
        meta: {
          title: '我的收藏',
          login: true,
        },
        component: () => import('../views/ucenter/courseFavorites/courseFavorites.vue')
      },{
        path: 'selfInfo',
        name: 'selfInfo',
        meta: {
          title: '个人信息',
          login: true,
        },
        component: () => import( '../views/ucenter/selfInfo/selfInfo.vue')
      },{
        path: 'accountBinding',
        name: 'accountBinding',
        meta: {
          title: '账号绑定',
          login: true,
        },
        component: () => import( '../views/ucenter/accountBinding/accountBinding.vue')
      },{
        path: 'myOrder',
        name: 'myOrder',
        meta: {
          title: '我的订单',
          login: true,
        },
        component: () => import( '../views/ucenter/myOrder/myOrder.vue')
      },{
        path: 'myAccount',
        name: 'myAccount',
        meta: {
          title: '我的学币',
          login: true,
        },
        component: () => import( '../views/ucenter/myAccount/myAccount.vue')
      },{
        path: 'letter',
        name: 'letter',
        meta: {
          title: '消息中心',
          login: true,
        },
        component: () => import( '../views/ucenter/letter/letter.vue')
      },{
        path: 'examRecord',
        name: 'examRecord',
        meta: {
          title: '考试记录',
          login: true,
        },
        component: () => import( '../views/ucenter/exam/examRecordAll.vue')
      },{
        path: 'examErrorQuestions',
        name: 'examErrorQuestions',
        meta: {
          title: '考试错题本',
          login: true,
        },
        component: () => import( '../views/ucenter/exam/examErrorQuestions.vue')
      }
    ]
  },{
    path: '/uc/player/:id',
    name: 'player',
    component: player,
    meta: {
      title: '播放大厅',
      login:true
    }
  },{
    path: '/uc/live/:id',
    name: 'live',
    component: live,
    meta: {
      title: '直播大厅',
      login:true
    }
  },{
    path: '/user',
    name: 'loginIndex',
    component: loginIndex,
    meta: {
      title: '登录'
    },
    children: [
      {
        path: 'login',
        name: 'login',
        meta: {
          title: '登录'
        },
        component: () => import(/* webpackChunkName: "list" */ '../views/user/login.vue')
      }, {
        path: 'register',
        name: 'register',
        meta: {
          title: '注册'
        },
        component: () => import(/* webpackChunkName: "list" */ '../views/user/register.vue')
      }, {
        path: 'forget',
        name: 'forget',
        meta: {
          title: '忘记密码'
        },
        component: () => import(/* webpackChunkName: "list" */ '../views/user/forget.vue')
      },{
        path: '/paySuccess',
        name: 'paySuccess',
        component: () => import( '../views/ucenter/myOrder/paySuccess.vue'),
        meta: {
          title: '支付成功'
        }
      }
    ]
  },{
    path: '/article',
    name: 'article',
    component: article,
    meta: {
      title: '资讯'
    },
    children: [
      {
        path: 'list',
        name: 'list',
        meta: {
          title: '资讯列表'
        },
        component: () => import('../views/article/articleList/articleList.vue')
      },{
        path: 'info/:id',
        name: 'info',
        meta: {
          title: '资讯详情'
        },
        component: () => import('../views/article/articleInfo/articleInfo.vue'),
      }
    ]
  },
  {
    path: '/privacy',
    name: 'privacy',
    component: privacy
  },{
    path: '/help',
    name: 'help',
    meta: {
      title: '帮助中心'
    },
    component: () => import(/* webpackChunkName: "about" */ '../views/help/help.vue')
  },{
    path: '/oauth/back',
    name: 'back',
    meta: {
      title: '授权回调'
    },
    component: () => import(/* webpackChunkName: "about" */ '../views/oauthBack.vue')
  },{
    path: '/oauth/',
    name: 'bindUser',
    meta: {
      title: '绑定用户'
    },
    component: () => import(/* webpackChunkName: "about" */ '../views/binduser/loginIndex.vue'),
    children: [
      {
        path: 'bindUser',
        name: 'bindUser',
        meta: {
          title: '绑定用户'
        },
        component: () => import(/* webpackChunkName: "list" */ '../views/binduser/login.vue')
      },
      {
        path: 'registerUser',
        name: 'registerUser',
        meta: {
          title: '注册用户'
        },
        component: () => import(/* webpackChunkName: "list" */ '../views/binduser/register.vue')
      }
    ]
  },{
    path: '/help/:id',
    name: 'help',
    meta: {
      title: '帮助中心'
    },
    component: () => import(/* webpackChunkName: "about" */ '../views/help/help.vue')
  },{
    path: '/exam/examList',
    name: 'exam',
    component: examList,
    meta: {
      title: '试卷列表'
    }
  },{
    path: '/exam/examIndex',
    name: 'examIndex',
    component: examIndex,
    meta: {
      title: '考试首页'
    }
  },{
    path: '/exam/examAnswer',
    name: 'examAnswer',
    component: examAnswers,
    meta: {
      title: '考试页面'
    }
  },{
    path: '/exam/examResult',
    name: 'examResult',
    component: examResult,
    meta: {
      title: '考试结果'
    }
  },{
    path: '/exam/examInfo',
    name: 'examInfo',
    component: examInfo,
    meta: {
      title: '试卷详情'
    }
  },{
    path: '/exam/examAnalysis',
    name: 'examAnalysis',
    component: examAnalysis,
    meta: {
      title: '考试解析'
    }
  },{
    path: '/exam/examErrorQuestionInfo',
    name: 'examErrorQuestionInfo',
    component: examErrorQuestionInfo,
    meta: {
      title: '考试错题详情'
    }
  },{
    path: '/exam/examFavQuestion',
    name: 'examFavQuestionInfo',
    component: examFavQuestionInfo,
    meta: {
      title: '考试收藏详情'
    }
  },

]




const router = new VueRouter({
  // base: "/school/",
  mode: 'history',
  linkActiveClass: 'active',
  routes
})


//路由拦截
router.beforeEach((to, from, next) => {



  //判断登录状态 1 如果有refeshToken 则定是登录状态通过refeshtoken换取登录 2 如果token存在则认为是登录状态
  //1 token 判断如果已经登录 则可进行下一步
  if(getToken()){
    //判断如果用户信息为空则获取
    if(typeof(Store.getters.studentInfo) == "undefined"||Store.getters.studentInfo.length == 0){
      Store.dispatch('GetUserInfo').then(res=>{
        Store.dispatch("updateLetterCount", res)
      })
    }else{
      Store.dispatch("updateLetterCount", Store.getters.studentInfo)
    }
    if(!Store.getters.timeOut){
      Store.dispatch('setTime')
    }
    next();
    return;
  }
  //2 token 通过refeshToken进行登录
  if(Store.getters.refeshToken) {
    //没有登录尝试RefeshToken 登录
    Store.dispatch('RefeshToken').then(res=>{
      //判断是否登录成功 成功则进行下一步
      if(Store.getters.isLogin){
        next();
      }else{
        if (to.meta.login) { // 判断该路由是否需要登录权限
          next({
            path: '/user/login',
            query: { redirect: to.fullPath }
          })
        } else {
          next();
        }
      }
    },error =>{
      Store.dispatch('FedLogOut')
      if (to.meta.login) { // 判断该路由是否需要登录权限
        next({
          path: '/user/login',
          query: { redirect: to.fullPath }
        })
      } else {
        next();
      }
    });
    return;
  }

  //获取用户信息失败，则清除信息
  Store.dispatch('FedLogOut')
  if (to.meta.login) { // 判断该路由是否需要登录权限
    next({
      path: '/user/login',
      query: { redirect: to.fullPath }
    })
  } else {
    next();
  }
  return;

});

export default router
