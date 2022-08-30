<template>
  <div>
  <div class="course-infos">
    <div class="w pr">
      <div class="path">
        <a href="javascript:void(0)" @click="clicksubject(courseInfo.sellType,0,0)" v-if="courseInfo.sellType == '1'">课程</a>
        <a href="javascript:void(0)" @click="clicksubject(courseInfo.sellType,0,0)" v-if="courseInfo.sellType == '2'">直播</a>
        <a href="javascript:void(0)" @click="clicksubject(courseInfo.sellType,0,0)" v-if="courseInfo.sellType == '3'">班级</a>
        <i class="path-split" v-if="Object.keys(courseInfo.subject).length!=0" >\</i><a href="javascript:void(0)" v-on:click="clicksubject(courseInfo.sellType,courseInfo.subject.id,0)">{{courseInfo.subject.subjectName}}</a>
        <i class="path-split" v-if="Object.keys(courseInfo.subSubject).length!=0" >\</i><a href="javascript:void(0)" v-on:click="clicksubject(courseInfo.sellType,courseInfo.subject.id,courseInfo.subSubject.id)" >{{courseInfo.subSubject.subjectName}}</a>
        <i class="path-split">\</i><a href="javascript:void(0)"><span>{{courseInfo.courseName}}</span></a>
      </div>
      <div class="hd clearfix">
        <h2 class="l">{{courseInfo.courseName}}</h2>
      </div>

      <div class="statics clearfix">
        <div class="teacher-info l" @click="teacher(courseInfo.teacher.id)">
          <a href="javascript:void(0)" >
            <img data-userid="6357397" class="js-usercard-dialog" :src="courseInfo.teacher.headImg" width="80" height="80">
          </a>
          <span class="tit">
                <a href="javascript:void(0)" >{{courseInfo.teacher.name}}</a>
            </span>
          <span class="job txtOf" :title="courseInfo.teacher.education" style="width: 100px;">{{courseInfo.teacher.education}}</span>
        </div>

        <div class="static-item l">
          <span class="meta">浏览人数</span>
          <span class="meta-value"> {{courseInfo.pageViewcount+courseInfo.bogusViewcount}}</span>
        </div>
        <div class="static-item l">
          <span class="meta">学习人数</span>
          <span class="meta-value"> {{courseInfo.pageBuycount + courseInfo.bogusBuycount}}</span>
        </div>
        <div class="static-item static-item2 l">
          <span class="meta">有效期</span>
          <span class="meta-value" v-if="courseInfo.losetype == '0'">
          到{{courseInfo.endTime}}止
          </span>
          <span class="meta-value" v-if="courseInfo.losetype == '1'">
          从购买之日起{{courseInfo.loseTime}}天
          </span>
        </div>


      </div>
      <div class="extra">

        <!-- credit -->

        <!-- share -->
        <div class="share-action r bdsharebuttonbox bdshare-button-style0-16" data-bd-bind="1589099537986">
          <!--收藏-->
          <div class="share js-follow-action" data-cid="9" data-cmd="follow" title="收藏" @click="favorites()">
            <i class="follow-action icon-star_outline" :class="favoritesFlag==true? '':'icon-star2'"></i>
            <span></span></div>
          <a @click="wechatShare()" href="javascript:;" class="share wx js-share icon-share-weichat" data-cmd="weixin" title="分享到微信"></a>
          <a @click="qqzoneShare()" href="javascript:;" class="share qq js-share icon-share-qq" data-cmd="qzone" title="分享到QQ空间"></a>
          <a @click="sinaShare()" href="javascript:;" class="share sina js-share icon-share-weibo" data-cmd="tsina" title="分享到新浪微博"></a>
        </div>
      </div>
    </div>
    <div class="info-bg" id="js-info-bg">
      <div class="cover-img-wrap">
<!--        <img data-src="//img.mukewang.com/55af49ad000116a506000338.jpg" alt="" style="display: none" id="js-cover-img">-->
      </div>
      <div class="cover-mask"></div>
      <div width="1899" height="200px" class="cover-canvas" id="js-cover-canvas" v-bind:style="{backgroundImage:'url(' + courseHeaderImgList[0].imageUrl + ')'}" ></div>
    </div>
  </div>

    <div style="position: fixed;width: 100%;height: 100%;z-index: 100;top:0;left: 0;" v-if="wechatShareFlag">
      <div class="bd_weixin_popup" >
        <div class="bd_weixin_popup_head"><span>分享到微信朋友圈</span><a @click="closeWechatShare" href="#" onclick="return false;" class="bd_weixin_popup_close">×</a></div>
        <div id="bdshare_weixin_qrcode_dialog_qr" class="bd_weixin_popup_main" style="text-align: center">
          <qrcode :value="shareUrl" :options="{ width: 220 }"></qrcode>
        </div>
        <div class="bd_weixin_popup_foot">打开微信，点击底部的“发现”，<br>使用“扫一扫”即可将网页分享至朋友圈。</div>
      </div>
    </div>
  </div>
</template>

<script>
  import { setStore, getStore } from '@/util/store'
  import {getBannerList} from "@/api";
  import { mapGetters } from "vuex";
  import {coursefavorites,checkFavorites,removeCoursefavorites} from "@/api/user";

export default {
  name: 'courseInfoheader',
  data () {
    return {
      favoritestext:"",
      courseHeaderImgList:getStore({ name: 'courseHeaderImgList' }) ||[],
      favoritesFlag:false,
      wechatShareFlag:false,
      shareUrl:window.location.protocol+"//"+window.location.host+"/schoolapp/#/pages/course/detail?courseId="+this.courseInfo.id,
      favoritesId:0
    }
  },
  methods: {
    clicksubject(type,subLevel1Active,subLevel2Active){
      this.$router.push({ path: "/course/courseList",query:{courseType:type,subLevel1:subLevel1Active,subLevel2:subLevel2Active} });
    },
    teacher(id){
      this.$router.push({ path: "/teacher/teacherInfo/"+id});
    },
    favorites(){
      let that = this
      //收藏前先判断是否登录如果没有登录前往登录
      if(!this.isLogin){
        this.$router.push({ path: '/user/login' })
        return
      }
      //判断是否收藏  如果收藏过则可以取消收藏
      if(this.favoritesFlag){
        let param = {
          "courseName":this.courseInfo.name,
          "courseId":this.courseInfo.id,
        }

        coursefavorites(param).then(res=>{
          this.$layer.msg("收藏成功", {
            shade: true,
            shadeClose: true,
          });
          this.favoritestext = "已收藏";
          that.favoritesFlag = false
          this.favoritesId = res.data.data.id
        })
      }else{
        removeCoursefavorites(this.favoritesId).then(res =>{
          this.$layer.msg("取消收藏", {
            shade: true,
            shadeClose: true,
          });
          this.favoritestext = "收藏";
          that.favoritesFlag = true
        })
      }
    },
    init(){
      if(this.isLogin){
        let param = {
          "courseId":this.courseInfo.id
        }
        let that = this
        checkFavorites(param).then(res=>{
          if(res.data.data.length>0){
            this.favoritesId = res.data.data[0].id
            that.favoritestext = "已收藏"
            that.favoritesFlag = false
          }else{
            that.favoritestext = "收藏"
            that.favoritesFlag = true
          }
        })
      }
      //查询广告位
      if(this.courseHeaderImgList.length == 0){
        getBannerList("3").then(res => {
          this.courseHeaderImgList = res.data.data;
          setStore({ name: 'courseHeaderImgList', content: res.data.data,type: 'session' })
        });
      }
    },
    sinaShare(){
      var title = this.courseInfo.courseName;
      var url = window.document.location.href;
      var picurl = this.courseInfo.logo
      var sharesinastring = 'http://v.t.sina.com.cn/share/share.php?title=' + title + '&url=' + url + '&content=utf-8&sourceUrl=' + url + '&pic=' + picurl;
      window.open(sharesinastring);
    },
    qqzoneShare(){
      var title = this.courseInfo.courseName;
      var url = window.document.location.href;
      var picurl = this.courseInfo.logo
      var shareqqzonestring = 'http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?url='+url+'&title='+title+'&desc=&summary=&site={{100*100}}&pics='+picurl;
      window.open(shareqqzonestring);
    },
    wechatShare(){
      this.wechatShareFlag = true
    },
    closeWechatShare(){
      this.wechatShareFlag = false
    }
  },
  props:{
    courseInfo: Object,
  },
  watch: {
    courseInfo: {
      deep: true,
      handler(newVal){
        this.init()

      }
    }
  },
  computed: {
    ...mapGetters(["website","navigation","userInfo","studentInfo","isLogin"]),
  },
  mounted:function() {
    this.init()
  },
}
</script>

<style scoped>
.course-infos .statics .static-item:after {
  margin-left: 16px;
  content: "·";
}
.course-infos .statics .static-item2:after {
  content: "";
}
</style>
