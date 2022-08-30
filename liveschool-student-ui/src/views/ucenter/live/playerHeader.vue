<template>
  <div id="header" class="course-detail-header">
    <div class="cd-inner clearfix">
      <ul class="l">
        <li class="nv nv-goback"><router-link :to="{path:'/course/courseInfo/'+courseKpoint.courseId}" class="revert l"><i class="imv2-arrow3_l"></i></router-link></li>
        <li class="nv nv-menu">
          <a href="javascript:;" class="chaptername J-chaptername" data-id="2876" style="cursor:text;">
            <em>{{courseKpoint.name}}</em>
          </a>
        </li>
        <li class="nv nv-follow-a">
          <div class="follow-action js-follow-action clearfix" data-prompt="1" data-cid="166" data-cmd="follow" title="收藏">
            <i class="icon-star_outline"></i>
            <span>收藏</span>
          </div>
        </li>
        <li class="nv nv-share bdsharebuttonbox bdshare-button-style0-16" data-bd-bind="1590584111610">
          <!-- note:bdshare cannt user inner icon tag-->
          <a class="bds_weixin" data-cmd="weixin" title="分享到微信"></a>
          <a class="bds_qzone" data-cmd="qzone" href="#" title="分享到QQ空间"></a>
          <a class="bds_tsina" data-cmd="tsina" title="分享到新浪微博"></a>
<!--          <div style="display:none" id="coursePic"><img src="//img.mukewang.com/576b7afb00019e4906000338-590-330.jpg"></div>-->
        </li>
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
            <i class="msg_remind" style="display: block;"></i>
            <a target="_blank" href="/u/8891510/notices">
              <i class="icon-notifi"></i>
              <!-- <span class="msg_icon" style="display: none;"></span> -->
            </a>
          </li>
          <li class="set_btn user-card-box "  id="header-user-card">
            <router-link id="header-avator" class="user-card-item" action-type="my_menu" :to="{path:'/uc/myAccount'}" target="_self">
              <img :src="studentInfo.headImg" width="40" height="40">
              <i class="myspace_remind" style="display: none;"></i>
              <span style="display: none;">动态提醒</span>
            </router-link>
            <div class="g-user-card">
              <div class="card-inner">
                <div class="card-top clearfix">
                  <a class="l" href="javascript:void(0)">
                    <img :src="studentInfo.headImg" :alt="studentInfo.name"></a>
                  <div class="card-top-right-box l">
                    <a href="javascript:void(0)">
                      <span class="name text-ellipsis">{{studentInfo.name}}</span>
                    </a>
                    <div class="meta">
                      <a href="/u/index/experience">经验<b id="js-user-mp">0</b></a>
                      <a href="/u/index/credit">积分<b id="js-user-credit">1</b></a>
                    </div>
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
                <div class="card-sets clearfix"><a href="javascript:void(0)" @click="logOut" class="l">安全退出</a></div>
              </div>
            </div>
          </li>
        </ul>
      </div>

      <div class="search-warp clearfix">
        <div class="search-area" data-search="top-banner">
          <input class="search-input" data-suggest-trigger="suggest-trigger" placeholder="搜索感兴趣的内容" type="text" autocomplete="off">
          <input type="hidden" class="btn_search" data-search-btn="search-btn">
          <ul class="search-area-result" data-suggest-result="suggest-result">
          </ul>
        </div>
        <div class="showhide-search" data-show="no"><i class="icon-search"></i></div>
      </div>
    </div>
  </div>
</template>

<script>
  import { mapGetters } from "vuex";
  import { validatenull } from '@/util/validate';

  export default {
    name: 'playerHeader',
    data() {
      return {
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
      },
      logOut(){
        this.$store.dispatch("LogOut")
        this.$router.push({ name: 'index' }).catch(data => {  });
      }
    },
    props:{
      courseKpoint: Object,
    },
    mounted:function() {
      this.init();
    },
    computed: {
      ...mapGetters(["website","navigation","userInfo","studentInfo","isLogin"]),
    }
  }
</script>

<style scoped>
  ol, ul {
    list-style: none;
  }
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
  #header #login-area .logined>li>a {
    height: 60px;
    line-height: 60px;
  }
  #header #login-area .logined>li>a {
    display: block;
    width: 60px;
    height: 72px;
    line-height: 72px;
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
  #header #login-area .logined>li>a {
    height: 60px;
    line-height: 60px;
  }
  #header #login-area .logined>li>a {
    display: block;
    width: 60px;
    height: 72px;
    line-height: 72px;
    color: #787d82;
    text-align: center;
    -webkit-transition: background-color .2s;
    -moz-transition: background-color .2s;
    transition: background-color .2s;
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
  #header #login-area .logined>li>a {
    height: 60px;
    line-height: 60px;
  }
  #header #login-area .logined>li>a {
    display: block;
    width: 60px;
    height: 72px;
    line-height: 72px;
    color: #787d82;
    text-align: center;
    -webkit-transition: background-color .2s;
    -moz-transition: background-color .2s;
    transition: background-color .2s;
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
    margin: 12px 0;
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
