<template>
  <div>
    <div class="header">
      <div id="header" class="" style="background-color: #fff !important;">
        <div class="page-container1 clearfix addZ-index uc-wrap" id="nav" >
          <div id="logo" class="logo"><a href="/" target="_self" title="首页"><img title="" :src="website.logo"></a></div>

        <button type="button" class="navbar-toggle visible-xs-block js-show-menu">
          <i class="icon-menu"></i>
        </button>
        <ul class="nav-item l">
          <li  v-for="(nav) in navigation" :key="nav.id" >
            <a :href="nav.url" v-if="nav.url == headNav" class="current"  :title="nav.name"  :key="nav.id">{{nav.name}}</a>
            <a :href="nav.url" v-if="nav.url != headNav"   :title="nav.name"  :key="nav.id">{{nav.name}}</a>
          </li>
        </ul>
        <div id="login-area">
          <ul class="clearfix " v-bind:class="{ 'header-unlogin': isLogin == false,'logined' :isLogin }">

              <li class="header-signin" v-if="isLogin == false" >

                <router-link :to="{path:'/user/login'}" ><a href="javascript:void(0)" id="js-signin-btn">登录</a></router-link>
                /
                <router-link :to="{path:'/user/register'}" ><a href="javascript:void(0)" id="js-signup-btn">注册</a></router-link>
              </li>
              <li class="remind_warp"  v-if="isLogin">
                <i class="msg_remind" style="display: inline;" v-show="letterCount>0"></i>
                <router-link :to="{path:'/uc/letter'}">
                  <i class="icon-notifi">
                  </i>
                </router-link>
              </li>
              <li class="user-mycourse-box" v-if="isLogin" >
                <router-link class="user-card-item js-header-courseurl" :to="{path:'/uc/myCourse',query:{courseType:''}}" target="_self">
                  <span>我的课程</span>
                </router-link>
              </li>

              <li class="set_btn user-card-box" id="header-user-card" v-if="isLogin">
                <router-link id="header-avator" class="user-card-item js-header-avator" action-type="my_menu" :to="{path:'/uc/myCourse'}" target="_self">
                  <img width="40" height="40" :src="studentInfo.headImg" onerror="this.src='/static/img/user-avatar.png'">
                  <i class="myspace_remind" style="display: none;"></i>
                  <span style="display: none;">动态提醒</span>
                </router-link>
                <div class="g-user-card"><div class="card-inner">
                  <div class="card-top clearfix">
                    <router-link class="l" :to="{path:'/uc/myCourse',query:{courseType:''}}">
                      <img :src="studentInfo.headImg" onerror="this.src='/static/img/user-avatar.png'" alt="studentInfo.name">
                    </router-link>
                    <div class="card-top-right-box l">
                      <router-link :to="{path:'/uc/myCourse',query:{courseType:''}}">
                        <span class="name text-ellipsis">{{studentInfo.showName}}</span>
                      </router-link>
                      <!--<div class="meta">
                        <a href="/u/index/experience">经验<b id="js-user-mp">1,870</b></a>
                        <a href="/u/index/credit">积分<b id="js-user-credit">2</b></a>
                      </div>-->
                    </div>
                  </div>
                  <div class="user-center-box">
                    <ul class="clearfix">
                      <li class="l">
                        <router-link :to="{path:'/uc/myCourse',query:{courseType:''}}" target="_self"><span class="user-center-icon icon-tick"></span>我的课程</router-link>
                      </li>
                      <li class="l">
                        <router-link :to="{path:'/uc/myOrder'}" target="_self">
                          <span class="user-center-icon icon-receipt"></span>订单中心
                        </router-link>
                      </li>
                      <li class="l">
                        <router-link :to="{path:'/uc/myAccount'}" target="_self">
                          <span class="user-center-icon icon-score_shop"></span>我的学币
                        </router-link>
                      </li>
                      <li class="l">
                        <router-link :to="{path:'/uc/selfInfo'}" target="_self">
                          <span class="user-center-icon icon-set"></span>个人设置
                        </router-link>
                      </li>
                    </ul>
                  </div>
                  <div class="card-sets clearfix">
                    <a href="javascript:void(0)" @click="logOut" class="l">安全退出</a>
                  </div>
                </div>
                </div>
              </li>
            </ul>
          </div>
          <div class="search-warp clearfix" >

            <div class="search-area" data-search="top-banner">
              <input v-model="searchCourseName" @keyup.enter="search()" ref="searchCourseName" class="search-input" data-suggest-trigger="suggest-trigger" placeholder="请输入关键字..." type="text" autocomplete="off">
              <input type="hidden" class="btn_search" data-search-btn="search-btn">
            </div>
            <div class="showhide-search" data-show="no" @click="search()">
              <i class="">
                <svg t="1588663140180" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1290" width="100%" height="100%"><path d="M675.84 198.0416a348.16 348.16 0 1 0 0 491.52 348.16 348.16 0 0 0 0-491.52z m-28.8768 463.2576a307.2 307.2 0 1 1 0-434.3808 307.2 307.2 0 0 1 0 434.3808z" p-id="1291"></path><path d="M675.84 661.2992a20.48 20.48 0 0 1 28.8768 0l231.8336 231.8336A20.48 20.48 0 1 1 907.4688 921.6L675.84 690.3808a20.48 20.48 0 0 1 0-29.0816zM574.464 299.4176zM574.464 589.0048v-0.2048 0.2048zM266.24 435.6096h-40.96a206.6432 206.6432 0 0 0 5.9392 59.392l39.5264-10.6496a165.2736 165.2736 0 0 1-4.5056-48.7424zM284.8768 299.4176a202.3424 202.3424 0 0 0-53.6576 95.4368h42.5984a162.2016 162.2016 0 0 1 193.3312-109.568l10.6496-39.5264a204.8 204.8 0 0 0-192.9216 53.6576z" p-id="1292"></path></svg>
              </i>
            </div>
          </div>
          <div class="clear"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import { mapGetters } from "vuex";
  import { validatenull } from '@/util/validate';
  import {getToken} from '@/util/auth'

  export default {
    name: 'pageHeader',
    data () {
      return {
        searchCourseName:"",
      }
    },
    props:{
      headNav: {
        type: String,
        default: "/"
      },

    },
    methods: {
      checkUrl(url){
        let s = url
        if(url.indexOf("?") >0){
          s = url.substr(0,url.indexOf("?"));
        }
        return s;
      },
      search(){
        this.$router.push({ path: '/course/courseList',query: {courseType:"0",searchCourseName:this.searchCourseName} })
      },
      init(){
        let courseName = this.$route.query.searchCourseName;
        this.searchCourseName = courseName
        //判断如果website 为空则更新数据
        if (validatenull(this.website)){
          this.$store.dispatch("getWebsiteConfig")
        }
        //判断如果navigation 为空则更新数据
        if (validatenull(this.websiteNavigate)){
          this.$store.dispatch("getWebsiteNavigate")
        }
      },
      logOut(){
        this.$store.dispatch("LogOut").then(res=>{
          this.$router.push({ name: 'index' }).catch(data => {  });
        })
      }
    },
    mounted:function() {
      this.init();
    },
    computed: {
      ...mapGetters(["website","navigation","userInfo","studentInfo","isLogin","letterCount"]),
    }
  }
</script>

<style scoped>
  .header{
    width:100%;
    height:72px;
    border-bottom: 1px solid #ebeced;
    box-shadow: 0 1px 8px 0 hsla(0,0%,71.8%,.5);
    position: relative;
    z-index: 9;
    height: 70px;
  }

</style>
