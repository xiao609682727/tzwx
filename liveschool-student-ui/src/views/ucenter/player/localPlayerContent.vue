<template>
  <div>
    <!-- 阿里云播放器引入 -->
    <remote-css href="https://g.alicdn.com/de/prismplayer/2.8.8/skins/default/aliplayer-min.css"></remote-css>
    <remote-js src="/static/js/aliyunPlayer/2.8.2/aliplayer-min.js"></remote-js>
    <remote-js src="/static/js/aliyunPlayer/aliplayer-components/aliplayercomponents-1.0.5.min.js"></remote-js>

    <div id="studyMain">
      <div id="bgarea" class="video-con">
        <div id="courseLayout" class="course-layout">
          <div id="js-ques-box"></div>    <!--此处结构为练习题，视频，编程公用的侧栏-->
          <div class="section-list " v-bind:class="{'active':sectionListFlag == true }">
            <div class="nano has-scrollbar">
              <div class="nano-content chapter-list" tabindex="0" style="right: 2px;">
                <ul v-for="kpoint in courseInfo.list" :key="kpoint.id">
                  <li class="sec-title">
                    <span>{{kpoint.name}}</span>
                  </li>
                  <li class="sec-li" data-id="2147" v-for="child in kpoint.children" :key="child.id" @click="toPlay(child)">
                    <a href="javascript:void(0)"><em class="imv2-play_circle type "></em>{{child.name}}
                      <span v-if="child.videoType=='aliyunlive'||child.videoType=='baijiayunlive'||child.videoType=='polyvlive'||child.videoType=='otherlive'" >
                        <span v-if="child.liveEndTime <= dateFormat('YYYY-mm-dd HH:MM:SS',new Date())">({{child.liveBeginTime|dataFormat('MM月dd日 hh:mm')}}-{{child.liveEndTime|dataFormat('hh:mm')}}  已结束)</span>
                        <span v-if="child.liveEndTime > dateFormat('YYYY-mm-dd HH:MM:SS',new Date())">({{child.liveBeginTime|dataFormat('MM月dd日 hh:mm')}}-{{child.liveEndTime|dataFormat('hh:mm')}})</span>
                      </span>
                      <i class="imv2-empty" v-if="child.id != courseKpoint.id && child.complete==0">
                        未学完
                      </i>
                      <i class="imv2-empty" v-if="child.id != courseKpoint.id && child.complete==1">
                        学习中
                      </i>
                      <i class="imv2-empty" v-if="child.id != courseKpoint.id && child.complete==2">
                        已学完
                      </i>
                      <i class="half" v-if="child.id == courseKpoint.id">正在学<span class="imv2-history"></span>
                      </i>
                    </a>

                  </li>
                </ul>
              </div>
            </div>
          </div>
          <!--资料列表-->
          <div class="section-list " v-bind:class="{'active':materialListFlag == true }">
            <div class="nano has-scrollbar">
              <div class="nano-content chapter-list" tabindex="0" style="right: 2px;" v-if="materialInfo != ''">
                <ul>
                  <li class="sec-title">资料下载</li>
                </ul>
                <ul v-for="material in materialInfo" :key="material.id">
                  <li class="sec-li" data-id="2148">
                    <a :href="material.fileUrl" :download="material.fileName" target="_blank" >
                      <em class="imv2-folder type "></em>{{material.name}}
                      <i class="imv2-arrow3_d" style="color: rgba(255,255,255,.6);">立即下载</i>
                    </a>

                  </li>
                </ul>
              </div>
              <div class="nano-content chapter-list" tabindex="0" style="right: 2px;" v-if="materialInfo == ''">
                <ul>
                  <li class="sec-title">资料下载</li>
                </ul>
                <div class="nodata">
                  <p><i class="imv2-error_c"></i></p>
                  <p>暂无资料</p>
                </div>
              </div>
            </div>
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
              <dd class="opencomment" @click="showMaterial">
                <i class="imv2-folder type"></i>
                <span>资料</span>
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
            <div class="js-box-wrap course-video-wrap" ref="homePage" style="width: 100%; height: 796px;">
              <div id="J_Box" class="course-video-box">
                <span class="moco-tick hide"></span>
                <div id="video-box" class="video-wrap" style="width:100%;height:100%">
                  <video  ref='video' controls="controls" style="width:100%;height:100%"></video>
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
  import {courseInfo , materialInfo , checkHaveCourse} from "@/api";
  import {addCoursestudyhistory,updateCoursestudyhistory} from "@/api/user";
  import request from '@/router/axios';

  export default {
    name: 'playerContent',
    data() {
      return {
        sectionListFlag:false,
        materialListFlag:false,
        clientHeight:'',
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
      materialInfo: Array,
      times:Number
    },
    watch: {
      courseKpoint: {
        deep: true,
        handler(newVal){
          this.init();
        }
      },// 如果 `clientHeight` 发生改变，这个函数就会运行
      clientHeight: function () {
        this.changeFixed(this.clientHeight)
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
      }, changeFixed(clientHeight){                        //动态修改样式
        this.$refs.homePage.style.height = (clientHeight-60)+'px';

      },
      initHeight(){  //获取高度
        //获取浏览器可视区域高度
        this.clientHeight = document.body.clientHeight;
        // console.log($(document).height());//浏览器可视区域对象宽度
        window.onresize = () => {  //当窗口或框架发生改变时触发
          //console.log("onresize进来了");
          this.clientHeight = document.body.clientHeight;
        };
      }
      ,
      showKpoint(){
        this.materialListFlag = false ;
        this.sectionListFlag = !this.sectionListFlag
      },
      showMaterial(){
        this.sectionListFlag = false ;
        this.materialListFlag = !this.materialListFlag
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

          let that = this
          this.$refs.video.src = this.courseKpoint.videoUrl
          this.$refs.video.play()

          let courseType = 1
          let taocanId = that.$route.query.taocanId;
          if(taocanId != undefined && taocanId != '' && taocanId != 0){
            courseType = 2
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
        if(obj.videoType == 'polyvlive'||obj.videoType == 'aliyunlive'||obj.videoType == 'baijiayunlive'||obj.videoType == 'otherlive'){
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
        // location.reload()
      },

    },
    mounted:function() {
      let that = this
      setTimeout(function () {
        that.init();
      },500)
      this.initHeight();
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
