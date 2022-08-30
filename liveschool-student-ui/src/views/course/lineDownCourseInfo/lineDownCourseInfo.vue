<template>
    <div v-wechat-title="courseInfoTitle">
        <common-header :headNav="headNav"></common-header>
        <div class="main">
            <div class="CoursePage_Banner">
                <div class="CoursePage_Banner_Cont BannerInner">
                    <div class="CoursePage_Banner_Cont_Way">
                        <a href="/lineDownCourse/list">面授课</a> <span>&gt;</span>
                        <a href="javascript:void(0)" v-on:click="clicksubject(courseInfo.sellType,courseInfo.subject.id,0)" > {{subject.subjectName}}</a> <span>&gt;</span>
                        <a href="javascript:void(0)" v-on:click="clicksubject(courseInfo.sellType,courseInfo.subject.id,courseInfo.subSubject.id)">{{subSubject.subjectName}}</a>
                    </div>
                    <div style="overflow: hidden;width: 100%">
                        <div class="CoursePage_Banner_img">
                            <img v-lazy="courseInfo.logo" onerror="this.src='/static/img/course-default.png'" class="CoursePage_Banner_ContImg"></div>
                        <div class="CoursePage_Banner_Cont_WayTotalLeft">
                            <div><span class="CoursePage_Banner_Cont_Title">{{courseInfo.courseName}}</span></div>
                            <div class="CoursePage_Banner_Cont_WayLeft"><img src="https://web.eduhiker.com/_nuxt/img/address.bd20730.png" style="width: 16px;height: 16px"> <span style="font-size: 14px;line-height: 32px;color:#efa85c ">{{courseInfo.faceTeachingSubjectAddress}}</span></div>
                            <div style="display: flex;margin: 15px 0 5px">
                                <span style="font-size: 14px;color: #333333;line-height: 14px">已报人数：</span>
                                <span style="font-size:14px;color: #9698a2;line-height: 14px">
                                    {{courseInfo.pageBuycount + courseInfo.bogusBuycount}}人</span>
                            </div>
                            <div style="display: flex;margin: 15px 0 5px">

                                <span style="font-size: 14px;color: #333333;line-height: 14px">上课时间：</span>
                                <span style="font-size:14px;color: #9698a2;line-height: 14px">
                                    {{courseInfo.faceTeachingTime|dataFormat('yyyy年MM月dd日 hh:mm')}} 至 {{courseInfo.endTime|dataFormat('yyyy年MM月dd日 hh:mm')}}</span>
                            </div>
                            <div style="display: flex; margin: 15px 0px 5px;">
                                <span style="font-size: 14px;color: #333333;line-height: 14px">上课地点：</span>
                                <span style="font-size:14px;color: #9698a2;line-height: 14px">{{courseInfo.faceTeachingAddress}}</span>
                            </div>
                          <div style="margin: 15px auto 28px">
                            <span style="font-size: 14px;color: #333333;line-height: 14px">联系电话：</span>
                            <span style="font-size:14px;color: red;line-height: 14px;font-weight: 600;">{{courseInfo.faceTeachingMobile}}</span>
                          </div>
                            <div style="background-color: #f7f7f7;padding-bottom: 12px">
                                <div class="CoursePage_Banner_Cont_Intro" style="padding-left: 12px">
                                    <div class="CoursePage_Banner_Cont_Price">
                                        <div v-if="courseInfo.currentPrice != '0.00'">
                                            <span style="color:#333333;font-size:14px;font-weight:400;">价格：</span>
                                            <span style="font-size: 18px;color:#ff4242;font-weight: 700 ">￥</span><span style="color: #ff4242;font-size: 23px;font-weight: 600;">{{courseInfo.currentPrice}}</span>

                                            <span style="color: #9698a2;font-size: 14px;margin-left: 9px">
                                                <span style="color:#333333;font-size:14px;font-weight:400;">原价：</span>
                                                <del>￥{{courseInfo.sourcePrice}}</del>
                                            </span>
                                        </div>
                                      <div v-if="courseInfo.currentPrice == '0.00'">
                                        <span style="color:#333333;font-size:14px;font-weight:400;">价格：</span>
                                        <span style="color: #ff4242;font-size: 23px;font-weight: 600;">免费</span>

                                      </div>
                                    </div>
                                </div>
                            </div>
                            <div class="CoursePage_Banner_Cont_button " style="display: flex;justify-content: space-between;align-items: flex-end"><!---->
                                <button v-if="courseInfo.endTime >= dateFormat('YYYY-mm-dd HH:MM:SS',new Date()) && statie==0" class="go-buyCourse" @click="showBaoMing()">立即报名</button>
                                <button v-if="courseInfo.endTime >= dateFormat('YYYY-mm-dd HH:MM:SS',new Date()) && statie==1" class="go-buyCourse" @click="toPay(courseInfo)">立即支付</button>
                                <button v-if="courseInfo.endTime >= dateFormat('YYYY-mm-dd HH:MM:SS',new Date()) && statie==2" class="go-buyCourse" style="background-color:#E8E6E7" disabled >已报名</button>
                                <button v-if="courseInfo.endTime >= dateFormat('YYYY-mm-dd HH:MM:SS',new Date()) && statie==3" class="go-buyCourse" @click="showBaoMing()">立即报名</button>
                                <button v-if="courseInfo.endTime <= dateFormat('YYYY-mm-dd HH:MM:SS',new Date()) " class="go-buyCourse" style="background-color:#E8E6E7" disabled >已结束</button>
                              <div class="extra">
                                    <div data-bd-bind="1589099537986" class="share-action r bdsharebuttonbox bdshare-button-style0-16">
                                        <div class="share js-follow-action" data-cid="9" data-cmd="follow" title="收藏" @click="favorites()">
                                        <i class="follow-action icon-star_outline" :class="favoritesFlag==true? '':'icon-star2'"></i>
                                        </div>
                                        <a @click="wechatShare()" href="javascript:;" data-cmd="weixin" title="分享到微信" class="share wx js-share icon-share-weichat"></a>
                                      <a @click="qqzoneShare()" href="javascript:;" data-cmd="qzone" title="分享到QQ空间" class="share qq js-share icon-share-qq"></a>
                                      <a @click="sinaShare()" href="javascript:;" data-cmd="tsina" title="分享到新浪微博" class="share sina js-share icon-share-weibo"></a></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="CoursePage_Cont" style="margin: 16px auto;">
                <div class="CoursePage_Cont-left">
                    <div class="CoursePage_subNav" style="position: static; top: 0px; width: 100%; border-bottom: 1px solid rgba(232, 235, 237, 0.39); box-shadow: none;">
                        <div class="CoursePage_subNav_inner" style="width: 100%;">
                            <ol class="js-tap fl">
                                <li class="contentModule "  v-bind:class="{'current':showFlag == 0}"><a @click="showFlag = 0" class="moco-change-big-btn"  href="javascript:void(0)">课程介绍</a></li>
                                <li class="sectionModule"  v-bind:class="{'current':showFlag == 1}"><a @click="showFlag = 1" class="moco-change-big-btn"  id="learnOn" href="javascript:void(0)">课时安排</a></li>
                            </ol>
                        </div>
                    </div>
                    <div style="padding-bottom: 40px;margin-bottom:50px;background-color: #ffffff ">
                        <ul class="CoursePage_Cont_TabCont-left">
                            <li v-show="showFlag == 0" class="courseInfoTab" >
                                <div   class="CoursePage_Intro_First " v-html="courseInfo.context" style="min-height: 560px"></div>
                            </li>
                            <li v-show="showFlag == 1"  class="courseInfoTab" >
                                <div class="CoursePage_Intro_First " v-html="courseInfo.faceTeachingContext" style="min-height: 560px"></div>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="CoursePage_Cont_sidebar-right">
                    <section class="right-info bg-wh  ite">
                        <div class="i-box curriculum-lecturer">
                            <header class="comm-title"><h2 class="fl tac tit-header"><span class="c-333" style="font-weight: 600;font-size: 16px;color: rgba(34,34,34,1);">课程讲师</span></h2></header>
                            <div class="cou-teacher-box">
                                <div class="c-t-wrap" style="padding-top: 12px">
                                    <aside class="c-t-pic">
                                      <router-link target="_blank" :to="{path:'/teacher/teacherInfo/'+teacherInfo.id}" :title="teacherInfo.name" class="course-card">
                                        <img v-lazy="teacherInfo.headImg" onerror="this.src='/static/img/user-avatar.png'">
                                      </router-link>
                                    </aside>
                                    <h3 class="hLh30" style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
                                      <router-link target="_blank" :to="{path:'/teacher/teacherInfo/'+teacherInfo.id}" :title="teacherInfo.name" class="fsize18 c-333 c-t-name teacherHover">
                                        {{teacherInfo.name}}
                                      </router-link>
                                    </h3>
                                    <p class="c-666 mt5"><span class="c-master title rank">{{courseInfo.teacherSubjectName}}</span></p>
                                </div>
                            </div>
                        </div>
                    </section>
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
            <div class="clear"></div>
        </div>
        <common-footer></common-footer>
    </div>
</template>

<script>

    import {courseInfo} from "@/api";
    import commonHeader from '@/views/common/header'
    import commonFooter from '@/views/common/footer'
    import Store from "../../../store";
    import baoMingLayer from '@/views/ucenter/common/baoMingLayer'
    import {coursefavorites,checkFavorites,removeCoursefavorites,getCourseFaceTeacherStatis,createCourseOrder } from "@/api/user";
    import { mapGetters } from "vuex";

    export default {
        name: 'courseList',
        data() {
            return {
                courseInfoTitle: "",
                courseInfo: {},
                teacherInfo: {},
                headNav: "",
                headFlag: false,
                wechatShareFlag:false,
                favoritesFlag:false,
                favoritestext:"",
                shareUrl:"",
                favoritesId:0,
                subject:{},
                subSubject:{},
                showFlag:0,
                statie:0,
            }
        },
        components: {
            commonHeader, commonFooter
        },
        methods: {
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
                "type":5,
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
          wechatShare(){
            this.wechatShareFlag = true
          },
          closeWechatShare(){
            this.wechatShareFlag = false
          },
          qqzoneShare(){
            var title = this.courseInfo.courseName;
            var url = window.document.location.href;
            var picurl = this.courseInfo.logo
            var shareqqzonestring = 'http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?url='+url+'&title='+title+'&desc=&summary=&site={{100*100}}&pics='+picurl;
            window.open(shareqqzonestring);
          },
          sinaShare(){
            var title = this.courseInfo.courseName;
            var url = window.document.location.href;
            var picurl = this.courseInfo.logo
            var sharesinastring = 'http://v.t.sina.com.cn/share/share.php?title=' + title + '&url=' + url + '&content=utf-8&sourceUrl=' + url + '&pic=' + picurl;
            window.open(sharesinastring);
          },
          clicksubject(type,subLevel1Active,subLevel2Active){
            this.$router.push({ path: "/lineDownCourse/list",query:{courseType:type,subLevel1:subLevel1Active,subLevel2:subLevel2Active} });
          },
          dateFormat(fmt, date){
            let ret;
            let getDate = new Date(date);
            const opt = {
              "Y+": getDate.getFullYear().toString(),        // 年
              "m+": (getDate.getMonth() + 1).toString(),     // 月
              "d+": getDate.getDate().toString(),            // 日
              "H+": getDate.getHours().toString(),           // 时
              "M+": getDate.getMinutes().toString(),         // 分
              "S+": getDate.getSeconds().toString()          // 秒
              // 有其他格式化字符需求可以继续添加，必须转化成字符串
            };
            for (let k in opt) {
              ret = new RegExp("(" + k + ")").exec(fmt);
              if (ret) {
                fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
              }
            }
            return fmt;
          },
          init() {
            debugger
              let id = this.$route.params.id;
              let showFlag = this.$route.query.showFlag;
            if(showFlag!=undefined && showFlag!='' &&showFlag!=0){
              this.showFlag=showFlag;
            }
              courseInfo(id).then(res => {
                this.courseInfo = res.data.data;
                this.subject=res.data.data.subject;
                this.subSubject=res.data.data.subSubject;
                this.teacherInfo=res.data.data.teacher;
                //把课程名字动态赋值给title
                let title = "";
                if (Store.getters.website.title) {
                  title = res.data.data.courseName + "-" + Store.getters.website.title;
                }
                this.courseInfoTitle = title;
                this.headFlag = true
                this.headNav = "/lineDownCourse/list"
                if(this.isLogin){
                  let param = {
                    "courseId":id
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
                }
                this.shareUrl=window.location.protocol+"//"+window.location.host+"/schoolapp/#/pages/course/detail?courseId="+id;
                if(this.isLogin){
                  let parama = {
                    courseId: id,
                  }
                  getCourseFaceTeacherStatis(parama).then(res => {
                    this.statie=res.data.data;
                  })
                }else {
                  this.statie=0
                }
              })
          },
          showBaoMing(){
            //未登录则跳转到登录页面
            if(!this.isLogin){
              this.$router.push({ path: '/user/login' })
              return;
            }
              this.$layer.iframe({
                  content: {
                      content: baoMingLayer,
                      parent: this,
                  },
                  area: ['380px', '240px'],
                  title: '报名',
                  maxmin: false,
                  shade: true,
                  shadeClose: true,
                  cancel: () => { //关闭弹窗事件
                      // alert('关闭iframe');
                  }
              });
          },
          maoming(){

          },
          toPay(courseInfo){//立即购买
            //未登录则跳转到登录页面
            if(!this.isLogin){
              this.$router.push({ path: '/user/login' })
              return;
            }
            //判断课程是否过期
            if(this.courseInfo.losetype == '0'){
              if(Date.parse(this.courseInfo.endTime) < new Date()){
                this.$layer.alert(
                  "课程已过期，请学习其他课程",
                  {
                    shade: true,
                    title: "提示"
                  },
                  laeryid => {
                    this.$layer.close(laeryid);
                  }
                );
                return;
              }
            }
            // this.$router.push({ path: '/course/confirmOrder/'+courseInfo.id })
            let courseId = this.courseInfo.id;
            let param = {
              courseId: courseId,
              payType: "",
              reqChannel:"web"
            }
            createCourseOrder(param).then(res =>{
              let order = res.data.data
              this.$router.push({ path: '/course/payCenter/'+order.id })
            })

          },
        },
        computed: {
          ...mapGetters(["website","navigation","userInfo","studentInfo","isLogin"]),
        },
        mounted: function () {
          this.init()
        },
    }
</script>

<style scoped>
    .CoursePage_Banner {
        background-color: #ffffff;
        padding-bottom: 45px;
    }
    .CoursePage_Banner .BannerInner {
        height: 100%;
        margin: 0 auto;
        width: 1150px;
    }
    .CoursePage_Banner_Cont_Way {
        color: #9698a2;
        font-size: 14px;
        line-height: 14px;
        padding: 20px 0;
        width: 100%;
    }
    .CoursePage_Banner_img {
        width: 480px;
        position: relative;
    }
    .CoursePage_Banner_ContImg {
        width: 480px;
        height: 274px;
        float: left;
        border-radius: 12px;
    }
    .CoursePage_Banner_Cont_WayTotalLeft {
        float: right;
        width: calc(100% - 520px);
        position: relative;
    }
    .CoursePage_Banner_Cont_Title {
        width: 605px;
        height: 22px;
        font-size: 24px;
        font-weight: 500;
        color: #262c3a;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        line-height: 25px;
        margin: 7px 0 10px;
    }
    .CoursePage_Banner_Cont_WayLeft {
        position: absolute;
        right: 24px;
        top: 0;
        display: flex;
        justify-content: center;
        align-items: center;
    }
    .info-title {
        margin: 20px 0 10px;
        width: 96%;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        color: #0091ff;
    }
    .info-title-span {
        font-size: 14px;
        font-weight: 400;
        color: #0091ff;
        line-height: 14px;
    }
    .CoursePage_Banner_Cont_Intro {
        position: relative;
        width: 100%;
        padding-top: 10px;
        overflow: hidden;
        display: flex;
        align-items: center;
    }
    .CoursePage_Banner_Cont_button {
        margin-bottom: 18px;
    }
    .go-buyCourse-top, .go-buyCourse {
        text-align: center;
        background-color: #0091ff;
        color: #ffffff;
        font-size: 16px;
        font-weight: 500;
    }
    .go-buyCourse {
        height: 44px;
        line-height: 44px;
        width: 116px;
        border-radius: 4px;
        margin-top: 20px;
    }
    .CoursePage_Cont {
        width: 1150px;
        overflow: hidden;
        margin: 16px auto;
    }
    .CoursePage_Cont-left {
        float: left;
        width: 73%;
    }
    .CoursePage_Cont_sidebar-right {
        float: right;
        width: 25%;
        padding-left: 16px;
    }
    .CoursePage_subNav {
        z-index: 9;
        background-color: #ffffff;
        border-bottom: 1px solid rgba(232, 235, 237, .39);
    }
    .CoursePage_subNav_inner {
        width: 100%;
        overflow: hidden;
    }
    .CoursePage_subNav ol {
        float: left;
        width: 100%;
    }
    .CoursePage_subNav li {
        float: left;
        width: 33%;
        line-height: 54px;
        text-align: center;
        font-size: 16px;
        color: #9698a2;
        font-weight: 400;
        cursor: pointer;
    }
    .CoursePage_subNav li.current {
        font-weight: 600;
        position: relative;
        color: #454651;
    }
    .courseInfoTab {
        background-color: #ffffff;
        min-height: 410px;
    }
    .CoursePage_Intro, .CoursePage_Intro_First {
        background: #ffffff;
        line-height: 28px;
        font-size: 14px;
    }
    .CoursePage_Intro_First {
        padding: 24px 32px 32px;
    }
    .CoursePage_Cont_sidebar-right {
        float: right;
        width: 25%;
        padding-left: 16px;
    }
    .CoursePage_Cont_sidebar-right .right-info {
        background: #ffffff;
    }
    .CoursePage_Cont_sidebar-right .right-info .i-box {
        background: transparent;
    }
    .curriculum-lecturer {
        padding-top: 20px;
    }
    .comm-title {
        clear: both;
        height: 44px;
    }
    .tit-header {
        border-left: 4px solid #0091ff;
        height: 18px;
        line-height: 13px;
        margin-top: 10px;
        padding-left: 10px;
    }
    .tac {
        text-align: center;
    }
    .c-t-wrap {
        position: relative;
        padding: 5px 0 18px 80px;
    }
    .c-t-wrap .c-t-pic {
        position: absolute;
        left: 0;
        top: 10px;
        width: 60px;
        height: 60px;
    }
    .c-t-wrap .c-t-pic img {
        width: 100%;
        height: 100%;
        border-radius: 50%;
    }
    .c-master {
        color: #0091ff;
        display: inline-block;
    }
    .i-box {
        overflow: hidden;
        padding: 8px 20px;
        background: #ffffff;
    }
    .CoursePage_subNav li.current:after {
        display: block;
        content: "";
        width: 32px;
        height: 5px;
        border-radius: 3px;
        background: linear-gradient(90deg, #35c2ff, #0091ff);
        position: absolute;
        bottom: 0;
        left: calc(50% - 16px);
    }
    .extra .share {

        display: inline-block;
        float: none;
        cursor: pointer;
        padding: 0;
        line-height: normal;
        margin: 0 12px;
        width: auto;
        height: auto;
        vertical-align: middle;
        background-image: none;
        font-size: 24px;
        background-repeat: no-repeat;
    }
</style>
