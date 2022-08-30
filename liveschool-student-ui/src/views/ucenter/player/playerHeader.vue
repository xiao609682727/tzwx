<template>
  <div id="header" class="course-detail-header">
    <div class="cd-inner clearfix">
      <ul class="l">
        <li class="nv nv-goback"><router-link :to="{path:'/uc/myCourse?courseType='}" class="revert l"><i class="imv2-arrow3_l"></i></router-link></li>
        <li class="nv nv-menu">
          <a href="javascript:;" class="chaptername J-chaptername" data-id="2876" style="cursor:text;">
            <em>{{courseKpoint.name}}</em>
          </a>
        </li>
        <li class="nv nv-follow-a">
          <div class="follow-action js-follow-action clearfix" data-prompt="1" data-cid="166" data-cmd="follow" title="收藏" @click="favorites()">
            <i class="icon-star_outline" :class="favoritesFlag==true? '':'icon-star2'"></i>
          </div>
        </li>
<!--        <li class="nv nv-share bdsharebuttonbox bdshare-button-style0-16" data-bd-bind="1590584111610">
          &lt;!&ndash; note:bdshare cannt user inner icon tag&ndash;&gt;
          <a class="bds_weixin" data-cmd="weixin" title="分享到微信"></a>
          <a class="bds_qzone" data-cmd="qzone" href="#" title="分享到QQ空间"></a>
          <a class="bds_tsina" data-cmd="tsina" title="分享到新浪微博"></a>
          <div style="display:none" id="coursePic"><img src="//img.mukewang.com/576b7afb00019e4906000338-590-330.jpg"></div>
        </li>-->
      </ul>
      <div id="login-area">
        <ul class="clearfix logined">
  <!--        <li class="shop-cart" id="shop-cart">
            <a href="//order.imooc.com/pay/cart" class="shop-cart-icon" target="_blank">
              <span class="icon-shopping-cart js-endcart"></span>
              <span>购物车</span>
              <span class="shopping_icon js-cart-num" data-ordernum="0" data-cartnum="0" style="display: none">0</span>
            </a>
            <div class="my-cart" id="js-my-cart"></div>
          </li>-->

          <li class="remind_warp">
            <i class="msg_remind" style="display: inline;" v-show="letterCount>0"></i>
            <router-link :to="{path:'/uc/letter'}" target="_self">
              <i class="icon-notifi"></i>
              <!-- <span class="msg_icon" style="display: none;"></span> -->
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
                    {{studentInfo.showName}}
                  </router-link>
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
             <!-- <div class="card-sets clearfix">
                <a href="javascript:void(0)" @click="logOut" class="l">安全退出</a>
              </div>-->
            </div>
            </div>
          </li>

        </ul>
      </div>

    </div>
  </div>
</template>

<script>
  import { mapGetters } from "vuex";
  import { validatenull } from '@/util/validate';
  import {coursefavorites,checkFavorites,removeCoursefavorites} from "@/api/user";
  import {courseInfo} from "@/api";

  export default {
    name: 'playerHeader',
    data() {
      return {
        favoritestext:"",
        favoritesFlag:false,
        favoritesId:0,
      }
    },
    components: {

    },
    methods: {
      init(){
        //判断如果website 为空则更新数据
        if (validatenull(this.website)){
          this.$store.dispatch("getWebsiteConfig")
        }
        //判断如果website 为空则更新数据
        if (validatenull(this.websiteNavigate)){
          this.$store.dispatch("getWebsiteNavigate")
        }
        let param = {
          "courseId":this.courseInfo.id
        }
        checkFavorites(param).then(res=>{
          if(res.data.data.length>0){
            this.favoritesId = res.data.data[0].id
            this.favoritestext = "已收藏"
            this.favoritesFlag = false
          }else{
            this.favoritestext = "收藏"
            this.favoritesFlag = true
          }
        })
      },
      logOut(){
        this.$store.dispatch("LogOut")
        this.$router.push({ name: 'index' }).catch(data => {  });
      },
      favorites(){
        //收藏前先判断是否登录如果没有登录前往登录
        if(!this.isLogin){
          this.$router.push({ path: '/user/login' })
        }
        //判断是否收藏  如果收藏过则可以取消收藏
        if(this.favoritesFlag){
          let param = {
            "courseName":this.courseInfo.name,
            "courseId":this.courseInfo.id,
          }
          coursefavorites(param).then(res=>{
            this.favoritestext = "已收藏"
            this.$layer.msg("收藏成功", {
              shade: true,
              shadeClose: true,
            });
            this.favoritestext = "已收藏";
            this.favoritesId = res.data.data.id;
            this.favoritesFlag = false;

          })
        }else{//取消收藏
          removeCoursefavorites(this.favoritesId).then(res =>{
            this.$layer.msg("取消收藏", {
              shade: true,
              shadeClose: true,
            });
            this.favoritestext = "收藏";
            this.favoritesFlag = true;
          })
        }
      },
    },
    props:{
      courseKpoint: Object,
      courseInfo: Object
    },
    watch: {
      courseKpoint: {
        deep: true,
        handler(newVal){
          this.init();
        }
      }
    },
    mounted:function() {

    },
    computed: {
      ...mapGetters(["website","navigation","userInfo","studentInfo","isLogin","letterCount"]),
    }
  }
</script>

<style scoped>
  .user-card-box:hover .g-user-card {
    visibility: initial;
  }
  .user-card-box.hover .g-user-card {
    visibility: visible;
  }
  #header {
    background: #fff;
    z-index: 2;
    box-shadow: 0 4px 8px 0 rgba(28,31,33,.1);
  }
  .course-detail-header {
    position: relative;
    padding-right: 24px;
    height: 60px;
  }
  .l {
    float: left;
  }
  .nv {
    float: left;
  }
  .nv-goback a {
    width: 70px;
    text-align: center;
  }
  .nv>a {
    display: block;
    height: 60px;
    line-height: 60px;
  }
  a:link, a:visited {
    color: #1c1f21;
  }
  .nv-goback a i {
    font-size: 24px;
    color: #9199a1;
    line-height: 60px;
    transition: .3s all linear;
  }
  .nv-menu>a {
    padding-right: 24px;
    font-size: 14px;
    color: #b5b9bc;
    position: relative;
  }
  .nv>a {
    display: block;
    height: 60px;
    line-height: 60px;
  }
  .chaptername em {
    font-size: 16px;
    color: #1c1f21;
    font-weight: 700;
  }
  .nv-follow-a {
    margin-right: 24px;
  }
  .follow-action {
    cursor: pointer;
  }
  .follow-action i {
    font-size: 20px;
    color: #b2b8bd;
    margin-right: 4px;
  }
  .follow-action i, .follow-action span {
    float: left;
    line-height: 60px;
    transition: .3s all linear;
  }
  star_outline:before {
    content: "\e901";
  }
  .follow-action span {
    font-size: 12px;
    color: #9199a1;
  }
  .follow-action i, .follow-action span {
    float: left;
    line-height: 60px;
    transition: .3s all linear;
  }
  .bdshare-button-style0-16 {
    zoom: 1;
  }
  .nv-share {
    float: left;
    position: relative;
    height: 20px;
    top: 20px;
  }
  .bdshare-button-style0-16 a, .bdshare-button-style0-16 .bds_more {
    float: left;
    font-size: 12px;
    padding-left: 17px;
    line-height: 16px;
    height: 16px;
    /*background-image: url(../img/share/icons_0_16.png?v=ba7acbd3.png);*/
    background-repeat: no-repeat;
    cursor: pointer;
    margin: 6px 6px 6px 0;
  }
  .nv-share a {
    float: left;
    background: 0 0!important;
    font-size: 20px!important;
    padding-left: 0!important;
    color: #b2b8bd!important;
    font-family: icomoon;
    speak: none;
    font-style: normal;
    font-weight: 400;
    font-variant: normal;
    text-transform: none;
    line-height: 20px!important;
    margin-left: 0!important;
    margin-right: 24px!important;
    margin-top: 0!important;
    margin-bottom: 0!important;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    -webkit-text-stroke-width: .2px;
  }
  .bdshare-button-style0-16 .bds_qzone {
    background-position: 0 -52px;
  }
  .bdshare-button-style0-16 .bds_tsina {
    background-position: 0 -104px;
  }
  #header #login-area {
    float: right;
    position: relative;
  }
  #header #login-area .logined>li {
    float: left;
    position: relative;
  }
  #header #login-area .shop-cart {
    margin: 12px 0;
  }
  #header #login-area .shop-cart {
    height: 36px;
    line-height: 36px;
    margin: 18px 0;
  }
  #header #login-area .shop-cart .shop-cart-icon {
    display: inline-block;
    padding: 0 18px;
    width: auto;
    box-sizing: border-box;
    border: 1px solid rgba(255,255,255,.2);
    border-radius: 18px;
    height: 36px;
    line-height: 34px;
    color: #787d82;
    text-align: center;
  }
  #header #login-area .logined>li>a {
    height: 60px;
    line-height: 60px;
  }
  #header #login-area .logined>li>a {
    display: block;
    width: 60px;
    height: 60px;
    line-height: 60px;
    color: #787d82;
    text-align: center;
    -webkit-transition: background-color .2s;
    -moz-transition: background-color .2s;
    transition: background-color .2s;
  }
  .shop-cart .shop-cart-icon .imv2-cart, .shop-cart .shop-cart-icon .icon-shopping-cart {
    position: relative;
    top: 2px;
    font-size: 16px;
    margin-right: 8px;
  }
  .shop-cart .shop-cart-icon span {
    color: #b2b8bd;
  }
  .shop-cart .shop-cart-icon .shopping_icon {
    padding: 0 5px;
    height: 16px;
    line-height: 16px;
    background: #f01414;
    color: #fff;
    margin-left: 8px;
    display: inline-flex;
    border-radius: 8px;
  }
  #header #login-area .shop-cart .my-cart {
    top: 54px;
  }
  #header #login-area .logined>li {
    float: left;
    position: relative;
  }
  #header #login-area .remind_warp {
    width: 60px;
    height: 48px;
    margin: 6px 0;
  }
  #header #login-area .remind_warp .msg_remind {
    display: none;
    position: absolute;
    width: 8px;
    height: 8px;
    background-color: #f01414;
    right: 14px;
    top: 10px;
    border-radius: 50%;
  }
  .remind_warp .msg_remind {
    width: 4px;
    height: 4px;
    background: #f01414;
    border-radius: 3px;
    right: 20px;
    top: 10px;
  }
  #header #login-area .remind_warp a {
    height: 48px;
    line-height: 48px;
  }
  #header #login-area .remind_warp .icon-notifi {
    display: block;
    line-height: 48px;
    font-size: 22px;
    text-align: center;
    cursor: pointer;
    -webkit-transition: color .2s;
    -moz-transition: color .2s;
    transition: color .2s;
  }
  #header #login-area .logined>li {
    float: left;
    position: relative;
  }
  .set_btn {
    right: 0;
  }
  .logined>li #header-avator {
    height: 60px;
    line-height: 60px;
    width: 44px;
  }
  address, caption, cite, code, dfn, em, strong, th, var, optgroup {
    font-style: normal;
    font-weight: 400;
  }
  .user-card-box .g-user-card {
    visibility: hidden;
    position: absolute;
    right: 0;
    top: 100%;
    z-index: 1000;
    width: 306px;
    padding: 24px;
    background-color: #fff;
    box-shadow: 0 8px 16px 0 rgba(7,17,27,0.2);
    border-bottom-right-radius: 8px;
    border-bottom-left-radius: 8px;
    box-sizing: border-box;
  }
  .search-warp {
    float: right;
    margin: 12px 36px 12px 0;
    position: relative;
  }

  .search-warp .search-area {
    height: 36px;
    width: 360px!important;
    background: #f3f5f6;
    border-radius: 8px;
    box-sizing: border-box;
    padding-right: 56px;
    border: 1px solid #f3f5f6;
    margin: 0;
    transition: .3s all linear;
  }
  .search-area {
    float: right;
    position: relative;
    height: 40px;
    padding-right: 40px;
    border-bottom: 1px solid rgba(255,255,255,.4);
    zoom: 1;
    background: #e9e9e9;
    border-radius: 8px;
    margin: 16px 0;
    width: 324px;
    box-sizing: border-box;
    font-size: 0;
    -webkit-transition: width .3s;
    -moz-transition: width .3s;
    transition: width .3s;
  }
  .remind_warp, .search-area {
    margin: 0px;
  }
  .search-warp .search-input {
    height: 34px;
    color: #1c1f21;
    padding-left: 16px;
    border-top-left-radius: 8px;
    border-bottom-left-radius: 8px;
  }
  .search-area .search-input {
    padding: 8px 12px;
    font-size: 14px;
    color: #a6a6a6;
    line-height: 24px;
    height: 40px;
    width: 100%;
    float: left;
    border: 0;
    -webkit-transition: background-color .3s;
    -moz-transition: background-color .3s;
    transition: background-color .3s;
    background-color: transparent;
    -moz-box-sizing: border-box;
    -webkit-box-sizing: border-box;
    -ms-box-sizing: border-box;
    box-sizing: border-box;
  }
  .search-area .btn_search {
    float: left;
    cursor: pointer;
    width: 30px;
    height: 38px;
    text-align: center;
    -webkit-transition: background-color .3s;
    -moz-transition: background-color .3s;
    transition: background-color .3s;
  }
  .search-warp .search-area-result {
    width: 358px;
    top: 40px;
  }
  .search-area .search-area-result {
    position: absolute;
    left: 0;
    top: 57px;
    width: 300px;
    margin-bottom: 20px;
    border-top: none;
    background-color: #fff;
    box-shadow: 0 8px 16px 0 rgba(7,17,27,.2);
    font-size: 12px;
    overflow: hidden;
    display: none;
    z-index: 800;
    border-bottom-right-radius: 8px;
    border-bottom-left-radius: 8px;
  }
  .search-warp .showhide-search {
    width: 56px;
    height: 36px;
    text-align: center;
    top: 0;
    right: 0;
    padding: 0;
    border-radius: inherit;
    border-top-right-radius: 8px;
    border-bottom-right-radius: 8px;
    transition: .3s all linear;
  }
</style>
