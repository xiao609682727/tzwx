<template>
  <div class="user-head-info" v-bind:style="{backgroundImage:'url(' + userHeaderImgList[0].imageUrl + ')',backgroundRepeat:'no-repeat', backgroundPosition:'center', backgroundSize:'cover'}">
    <div class="user-info clearfix">
      <div class="user-pic" data-is-fans="0" data-is-follows="">
        <div class="user-pic-bg">
          <img class="img" :src="studentInfo.headImg" onerror="this.src='/static/img/user-avatar.png'" alt="">
        </div><!--user-pic-big end-->
      </div>
      <div class="user-info-right">
        <h3 class="user-name clearfix">
          <span>{{studentInfo.name}}</span>
        </h3>
        <!--25-->
        <!--<p class="about-info">
          <span>保密</span>
          <span>

          </span>
          <a class="more-user-info">
            <i class="imv2-arrow2_d"></i>
            更多信息
          </a>
        </p>-->
      </div>
      <div class="user-sign hide">
        <p class="user-desc">这位同学很懒，木有签名的说～</p>
      </div>
      <div class="study-info clearfix">
        <!--<div class="item follows">
          <div class="u-info-learn" title="学习时长100小时4分" style="cursor:pointer;">
            <em>100h</em>
            <span>学习时长</span>
          </div>
        </div>
        <div class="item follows">
          <a href="/u/index/experience"><em>1870</em></a>
          <span>经验</span>
        </div>
        <div class="item follows">
          <a href="/u/index/credit"><em>2</em></a>
          <span>积分</span>
        </div>
        <div class="item follows">
          <a href="/u/index/follows"><em>1</em></a>
          <span>关注</span>
        </div>
        <div class="item follows">
          <a href="/u/index/fans"><em>0</em></a>
          <span>粉丝</span>
        </div>-->
        <div class="item follows">
          <router-link :to="{path:'/uc/selfInfo'}" class="set-btn">
            <i class="icon-set"></i>
            个人设置
          </router-link>
        </div>

      </div><!--.study-info end-->
    </div><!-- .user-info end -->
  </div>
</template>

<script>
  import { mapGetters } from "vuex";
  import { setStore, getStore } from '@/util/store'
  import {getBannerList} from "@/api";
  export default {
    name: 'ucenterHeader',
    data() {
      return {
        isReg: false,
        name: '',
        password: '',
        userHeaderImgList:getStore({ name: 'userHeaderImgList' }) ||[],
        repeat: ''
      }
    },
    methods: {
      init(){
        //查询广告位
        if(this.userHeaderImgList.length == 0){
          getBannerList("4").then(res => {
            this.userHeaderImgList = res.data.data;
            setStore({ name: 'userHeaderImgList', content: res.data.data,type: 'session' })
          });
        }
      }
    },
    watch: {
      studentInfo: {
        deep: true,
        handler(newVal){
        }
      }
    },
    mounted:function() {
      this.init()
    },
    computed: {
      ...mapGetters(["website","navigation","userInfo","studentInfo","isLogin"]),
    }
  }
</script>

<style scoped>

</style>
