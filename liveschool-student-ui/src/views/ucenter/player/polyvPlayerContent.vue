<template>
  <div>

    <!-- 阿里云播放器引入 -->


    <div id="studyMain">
      <div id="bgarea" class="video-con">
        <div id="courseLayout" class="course-layout">
          <div id="js-ques-box"></div>    <!--此处结构为练习题，视频，编程公用的侧栏-->
          <div class="section-list " v-bind:class="{'active':sectionListFlag == true }">
            <div class="nano has-scrollbar">
              <div class="nano-content chapter-list" tabindex="0" style="right: -17px;">
                <ul v-for="kpoint in courseInfo.list" :key="kpoint.id">
                  <li class="sec-title">
                    <span>{{kpoint.name}}</span>
                  </li>
                  <li class="sec-li" data-id="2147" v-for="child in kpoint.children" :key="child.id" @click="toPlay(child)">
                    <a href="javascript:void(0)"><em class="imv2-video_circle"></em>
                      {{child.name}}
                      <span v-if="child.videoType=='aliyunlive'||child.videoType=='baijiayunlive'||child.videoType=='polyvlive'||child.videoType=='otherlive'" >
                        <span v-if="child.liveEndTime <= dateFormat('YYYY-mm-dd HH:MM:SS',new Date())">({{child.liveBeginTime|dataFormat('MM月dd日 hh:mm')}}-{{child.liveEndTime|dataFormat('hh:mm')}}  已结束)</span>
                        <span v-if="child.liveEndTime > dateFormat('YYYY-mm-dd HH:MM:SS',new Date())">({{child.liveBeginTime|dataFormat('MM月dd日 hh:mm')}}-{{child.liveEndTime|dataFormat('hh:mm')}})</span>
                      </span>

                      <i class="imv2-empty" v-if="child.id != courseKpoint.id"></i><i class="half" v-if="child.id == courseKpoint.id">正在学<span class="imv2-history"></span></i></a>
                  </li>
                </ul>
              </div>
              <div class="nano-pane"><div class="nano-slider" style="height: 560px; transform: translate(0px, 0px);"></div></div></div>
          </div>
          <div class="course-sidebar-layout" id="courseSidebar">
            <dl>
              <dd class="openchapter" @click="showKpoint">
                <i class="imv2-nav_chapter"></i>
                <span>章节</span>
              </dd>
              <!--<dd class="openpanel" data-panel="qa">
                <i class="imv2-yuanwen"></i>
                <span>问答</span>
              </dd>
              <dd class="opentick">
                <i class="imv2-tick"></i>
                <span>课签</span>
              </dd>
              <dd class="openpanel" data-panel="note">
                <i class="imv2-homework"></i>
                <span>笔记</span>
              </dd>
              <dd class="opencomment">
                <i class="imv2-chat_bubble"></i>
                <span>评论</span>
              </dd>-->
              <dd class="vhidden">
                <span>占位</span>
              </dd>
              <dd class="vhidden">
                <span>占位</span>
              </dd>
              <dd class="vhidden">
                <span>占位</span>
              </dd>
              <dd class="vhidden">
                <span>占位</span>
              </dd>
              <dd class="vhidden">
                <span>占位</span>
              </dd>
              <dd class="vhidden">
                <span>占位</span>
              </dd>
            </dl>
          </div>

          <div class="course-center-layout" id="courseCenter" data-minw="500">

            <div class="js-box-wrap course-video-wrap" style="width: 100%; height: 796px;">
              <div id="J_Box" class="course-video-box">
                <span class="moco-tick hide"></span>
                <div id="video-box" class="video-wrap" style="width:100%;height:100%">

                </div>
                <div id="video-box-mocoplayer" class="mocoplayer">
                </div>
                <div class="pause-box J_pause-box hide">
                  <div class="pause-box-inner">
                    <div class="pause-box-wrap">
                      <div id="courseVideoPause">
                        <div class="close-pause-box js-close-pause"><i class="icon-close2"></i></div>
                        <div class="show-select">
                          <input type="checkbox" name="stopPauseAd">
                          <span>今日不再显示</span>
                        </div>
                        <div>
<!--                          <img src="//img.mukewang.com/5ea6507109b99bd803480172.jpg">-->
                        </div><div></div></div>
                    </div>
                  </div>
                </div>
                <div class="next-box J_next-box hide" style="display: none;">
                  <div class="next-box-inner">

                    <div class="course-tip-layer J-next-course" data-next-src="/video/2149">
                      <!--<div class="wechat-box js-wechat-box">
                          <a href="javascript:void(0)" class="moco-icon-close moco-modal-close wechat-close js-wechat-close"></a>
                          检测到您还没有关注慕课网服务号，无法接收课程更新通知。请扫描二维码即可绑定<div style="text-align:center">
                          <img width="100%" src=""></div>
                      </div>-->

                      <h2>
                        下一节课程： PS之视图菜单
                        <span class="course-duration"> (08:59)</span>
                        <span class="J-next-btn hide next-auto moco-btn moco-btn-green">下一节</span>
                        <a href="/video/2147/0" class="review-course">重新观看</a>
                      </h2>
                      <div class="J-next-auto hide next-auto"><em>3</em> 秒后播放下一节</div>

                      <div class="video_over_ad J-next-ad">
                        <div class="line"></div>
                        <h4>为你推荐</h4>
                        <div id="courseVideoNext"><div>
<!--                          <img src="//img.mukewang.com/5ea65155099c258403480172.jpg">-->
                        </div><div></div></div>
                      </div>
                    </div>
                    <div class="qqQunAdd-box js-qqQunAdd">
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  import {courseInfo,checkHaveCourse} from "@/api";
  import {addCoursestudyhistory,updateCoursestudyhistory} from "@/api/user";

  export default {
    name: 'playerContent',
    data() {
      return {
        sectionListFlag:false,
        childertiems:0,
      }
    },
    components: {
      // eslint-disable-next-line vue/no-unused-components
      'remote-css': {
        render(createElement) {
          return createElement('link', { attrs: { rel: 'stylesheet', href: this.href }});
        },
        props: {
          href: { type: String, required: true },
        },
      },
      // eslint-disable-next-line vue/no-unused-components
      'remote-js': {
        render(createElement) {
          return createElement('script', { attrs: { type: 'text/javascript', src: this.src }});
        },
        props: {
          src: { type: String, required: true },
        },
      },
    },
    props:{
      courseKpoint: Object,
      courseInfo: Object,
      times:Number
    },
    watch: {
      courseKpoint: {
        deep: true,
        handler(newVal){
          this.init();
        }
      },
      times: {
        deep: true,
        handler(newVal){
          this.childertiems=newVal;
        }
      },
    },
    methods: {
      dateFormat(fmt, date){
        let ret;
        const opt = {
          "Y+": date.getFullYear().toString(),        // 年
          "m+": (date.getMonth() + 1).toString(),     // 月
          "d+": date.getDate().toString(),            // 日
          "H+": date.getHours().toString(),           // 时
          "M+": date.getMinutes().toString(),         // 分
          "S+": date.getSeconds().toString()          // 秒
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
      showKpoint(){
        this.sectionListFlag = !this.sectionListFlag
      },
      init(){
        checkHaveCourse(this.courseKpoint.courseId).then(res =>{
          let checkCourseFlag = res.data.data;
          if(checkCourseFlag == false){
            this.$layer.alert(
              "您还未拥有此课程",
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
          let player
          let that = this
          if(this.courseKpoint.videoType == "polyvvod"||this.courseKpoint.videoType == "polyvReplay"){
            // eslint-disable-next-line no-undef
            player = polyvPlayer({
              wrap: '#video-box-mocoplayer',
              width: "100%",
              height: "100%",
              autoplay:true,
              vid: this.courseKpoint.videoUrl,
            });

            window.s2j_onPlayOver = function(){

            }

            window.s2j_onPlayStart = function(){
              let taocanId = this.$route.query.taocanId;
              let courseType=1;
              if(taocanId!=undefined&&taocanId!=''&&taocanId!=0){
                courseType=2;
              }else {
                taocanId=0;
              }
              let param = {
                courseId:that.courseKpoint.courseId,
                kpointId:that.courseKpoint.id,
                courseName:that.courseInfo.courseName,
                kpointName:that.courseKpoint.name,
                kpointCourseId:taocanId,
                courseType:courseType,
                complete:1
              }
              addCoursestudyhistory(param).then(res =>{
                //console.log("创建播放记录完成")
              })
            }
          }
        })
      },
      toPlay(obj){
        let taocanIdc = this.$route.query.taocanId;
        let courseType=1;
        let courseId=this.courseKpoint.id;
        if(taocanIdc!=undefined&&taocanIdc!=''&&taocanIdc!=0){
          courseType=2;
        }else {
          taocanIdc=0;
        }
        let param = {
          courseId:obj.courseId,
          kpointId:this.courseKpoint.id,
          kpointCourseId:taocanIdc,
          courseType:courseType,
          watchStingTime:this.childertiems
        }
        updateCoursestudyhistory(param).then(res =>{
          this.childertiems=0;
        })
        //判断日期 如果是直播 则进行时间判断 如果是回放则不进行时间判断
        if(obj.videoType == 'polyvlive'||obj.videoType == 'aliyunlive'||obj.videoType == 'baijiayunlive'||obj.videoType=='otherlive'){
          //修改时间提前10分钟进场
          let d = new Date();
          let addDate = d.setMinutes(d.getMinutes()+10);
          new Date(addDate)
          let ti = this.dateFormat("YYYY-mm-dd HH:MM:SS",new Date(addDate) );
          if(obj.liveBeginTime > ti){
            this.$layer.alert(
              "课程还未开始",
              {
                shade: true,
                title: "提示"
              },
              laeryid => {
                this.$layer.close(laeryid);
              }
            );
            return
          }
          ti = this.dateFormat("YYYY-mm-dd HH:MM:SS",new Date());
          if(obj.liveEndTime < ti){
            this.$layer.alert(
              "课程已结束",
              {
                shade: true,
                title: "提示"
              },
              laeryid => {
                this.$layer.close(laeryid);
              }
            );
            return
          }
        }
        let taocanId = this.$route.query.taocanId;
        this.$router.push({ path: '/uc/player/'+obj.id,query:{"taocanId":taocanId} })
        location.reload()
      },

    },
    mounted:function() {
      this.init();
    }
  }
</script>

<style scoped>
  .bjc-player-wrapper {
    width: 100%;
    height: 100%;
  }
  .bjc-player-wrapper .video-js {
    width: 100%;
    height: 100%;
  }
  #studyMain {
    position: relative;
    z-index: 1;
  }
  .video-con {
    position: relative;
    background-color: #000;
    overflow: hidden;
    width:100%;
  }
  .course-layout {
    display: flex;
    display: -webkit-flex;
    position: relative;
    zindex: 1;
  }
  #js-ques-box {
    position: absolute;
    bottom: 14px;
    z-index: 100;
  }
  .section-list {
    position: absolute;
    left: -400px;
    top: 0;
    bottom: 0;
    width: 400px;
    background: #26292c;
    box-shadow: 0 8px 16px 0 #1c1f21;
    z-index: 9999;
    transition: .3s all linear;
    opacity: 0;
    padding: 8px 0;
    box-sizing: border-box;
  }
  .nano {
    position: relative;
    width: 100%;
    height: 100%;
    overflow: hidden;
  }
  .nano>.nano-content {
    position: absolute;
    overflow: scroll;
    overflow-x: hidden;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
  }
  .section-list .nano-content {
    padding-bottom: 20px;
  }
  ol, ul {
    list-style: none;
  }

  .section-list ul {
    padding: 0 24px;
  }
  .section-list .sec-title {
    padding-top: 20px;
    font-size: 14px;
    color: #fff;
    line-height: 22px;
    margin-bottom: 8px;
  }
  .section-list .sec-title {
    padding-top: 20px;
    font-size: 14px;
    color: #fff;
    line-height: 22px;
    margin-bottom: 8px;
  }
  .section-list .sec-li {
    height: 30px;
  }
  .section-list .sec-li a {
    padding: 6px 0 6px 28px;
    width: 100%;
    height: 30px;
    box-sizing: border-box;
    overflow: hidden;
    display: block;
    position: relative;
    font-size: 12px;
    color: rgba(255,255,255,.6);
    line-height: 20px;
  }
  .clearfix:after {
    content: '\0020';
    display: block;
    height: 0;
    clear: both;
    visibility: hidden;
  }
  .section-list .sec-li em {
    position: absolute;
    left: 8px;
    top: 6px;
    font-size: 16px;
    line-height: 18px;
  }
  .imv2-video_circle:before {
    content: "\e923";
  }
  .section-list .sec-li i {
    float: right;
    font-size: 12px;
    color: rgba(255,255,255,.2);
    line-height: 20px;
  }
  .section-list.active {
    left: 60px;
    opacity: 1;
  }
  .nano>.nano-pane {
    position: absolute;
    width: 10px;
    right: 10px;
    top: 0;
    bottom: 0;
    visibility: hidden\9;
    -webkit-transition: .2s;
    -moz-transition: .2s;
    -o-transition: .2s;
    transition: .2s;
    -moz-border-radius: 5px;
    -webkit-border-radius: 5px;
    border-radius: 5px;
  }
  .nano>.nano-pane>.nano-slider {
    background: #4e5a5e;
    position: relative;
    margin: 0 1px;
    -moz-border-radius: 3px;
    -webkit-border-radius: 3px;
    border-radius: 3px;
  }
  .course-sidebar-layout {
    background: #1c1f21;
    width: 60px;
    display: flex;
    display: -webkit-flex;
    align-items: center;
    z-index: 10000;
  }
  .course-sidebar-layout dl {
    flex: 1;
    width: 100%;
  }
  .course-sidebar-layout dd {
    display: flex;
    display: -webkit-flex;
    height: 72px;
    flex-direction: column;
    text-align: center;
    justify-content: center;
    cursor: pointer;
  }
  .course-sidebar-layout i {
    font-size: 24px;
    color: rgba(255,255,255,.6);
    line-height: 24px;
  }
  .course-sidebar-layout span {
    font-size: 12px;
    color: rgba(255,255,255,.4);
    line-height: 18px;
  }
  .course-sidebar-layout dd.vhidden {
    display: none;
  }
  .course-center-layout {
    position: relative;
    flex: 1;
    background: #1c1f21;
  }
  .course-video-wrap {
    padding-top: 16px;
    padding-bottom: 16px;
    box-sizing: border-box;
  }
  .js-box-wrap {
    width: 100%;
    height: 300px;
  }
  .course-video-box {
    position: relative;
    margin: 0 auto;
    height: 100%;
    background: #0e1011;
    box-shadow: 0 8px 16px 0 rgba(14,16,17,.8);
    overflow: hidden;
  }
  .course-video-box .moco-tick.hide {
    display: none;
  }
  .course-video-box .moco-tick {
    top: 0;
    right: 40px;
    z-index: 1;
  }
  .hide {
    display: none;
  }
  .video-wrap {
    width: 100%;
    height: 100%;
  }
  .mocoplayer {
    width: 100%;
    height: 100%;
    background: #000;
  }
  .pause-box {
    position: absolute;
    left: 0;
    top: 0;
    right: 0;
    bottom: 50px;
  }
  .course-right-layout {
    position: relative;
    background: #1c1f21;
    width: 384px;
    padding: 0;
    right: 0;
    z-index: 1;
  }
  .course-right-layout .course-right-nano, .course-right-scroll .course-right-nano {
    position: relative;
    width: 100%;
    height: 100%;
    overflow: hidden;
    z-index: 2;
  }
  .course-right-layout .nano-right-content, .course-right-scroll .nano-right-content {
    position: absolute;
    overflow: scroll;
    overflow-x: hidden;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
  }
  .c-panel.current {
    display: block;
    z-index: 1;
  }
  .c-panel.current {
    display: block;
    z-index: 1;
  }
  .video-panel {
    padding: 32px;
    background: #1c1f21;
  }
  .video-panel .video-panel-close, .video-panel .video-panel-show {
    position: absolute;
    right: 24px;
    top: 16px;
    font-size: 24px;
    color: #93999f;
    line-height: 24px;
    cursor: pointer;
    z-index: 1;
  }
  .video-panel .video-panel-close {
    display: inline;
  }
  .imv2-close:before {
    content: "\e5cd";
  }
  .video-panel .v-teachers {
    margin-bottom: 16px;
  }
  .video-panel .v-teachers-info {
    font-size: 12px;
    color: rgba(255,255,255,.6);
    line-height: 22px;
  }

  #courseVideoRight {
    max-height: 160px;
    min-height: 60px;
    width: 320px;
    overflow: hidden;
    margin-top: 24px;
    cursor: pointer;
    position: relative;
    z-index: 899;
    display: none;
    line-height: 0;
    border-radius: 12px;
  }
  #courseVideoRight img {
    width: 100%;
  }
  .video-panel .v-course-wrap {
    padding-top: 24px;
  }
  .video-panel .v-course-wrap h3 {
    font-size: 16px;
    color: #fff;
    line-height: 24px;
  }
  .video-panel .v-course-wrap .v-course-list {
    padding-top: 16px;
  }
  .video-panel .v-course-wrap .v-course:last-child {
    margin-bottom: 0;
  }
  .video-panel .v-course-wrap .v-course {
    float: left;
    width: 272px;
    position: relative;
    margin-bottom: 20px;
    padding-left: 112px;
    box-sizing: border-box;
    margin-right: 15px;
    height: 64px;
    min-height: 64px;
  }
  .video-panel .v-course-wrap img {
    position: absolute;
    left: 0;
    top: 0;
    width: 96px;
    height: 64px;
    border-radius: 8px;
  }
  .video-panel .v-course-wrap .v-course-name {
    font-size: 14px;
    color: rgba(255,255,255,.6);
    line-height: 20px;
    transition: .2s all linear;
    max-height: 40px;
    overflow: hidden;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
  }
  .video-panel .v-course-wrap .v-course-info span {
    float: left;
    position: relative;
    font-size: 12px;
    color: rgba(255,255,255,.4);
    line-height: 18px;
    padding-right: 14px;
    transition: .2s all linear;
  }
  .video-panel .v-course-wrap .v-course-info span::before {
    content: "·";
    position: absolute;
    right: 4px;
    top: 0;
    width: 6px;
    text-align: center;
    line-height: 18px;
  }

  #app{
    height: 100%;
  }
</style>
