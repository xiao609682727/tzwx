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
                 <li class="sec-li" :class="child.id == courseKpoint.id?'active':''" data-id="2147" v-for="child in kpoint.children" :key="child.id" @click="toPlay(child)">
                   <a href="javascript:void(0)"><em class="imv2-play_circle type "></em>{{child.name}}
                     <span v-if="child.videoType=='aliyunlive'||child.videoType=='baijiayunlive'||child.videoType=='polyvlive'||child.videoType=='otherlive'" >
                        <span v-if="child.liveEndTime <= dateFormat('YYYY-mm-dd HH:MM:SS',new Date())">({{child.liveBeginTime|dataFormat('MM月dd日 hh:mm')}}-{{child.liveEndTime|dataFormat('hh:mm')}}  已结束)</span>
                        <span v-if="child.liveEndTime > dateFormat('YYYY-mm-dd HH:MM:SS',new Date())">({{child.liveBeginTime|dataFormat('MM月dd日 hh:mm')}}-{{child.liveEndTime|dataFormat('hh:mm')}})</span>
                      </span>
                     <i class="imv2-empty unpass" v-if="child.id != courseKpoint.id && child.complete==0">
                          未学完
                     </i>
                     <i class="imv2-empty" v-if="child.id != courseKpoint.id && child.complete==1">
                          学习中
                     </i>
                     <i class="imv2-finish pass" v-if="child.id != courseKpoint.id && child.complete==2">
                          已学完
                     </i>
                     {{child.complete}}
                     <i class="half" v-if="child.id == courseKpoint.id">
                       <span class="rank-icon">
                         <i ></i>
                         <i ></i>
                         <i ></i>
                       </span>正在学</span>
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

               </div>
               <div id="video-box-mocoplayer" class="mocoplayer">
                 <div class="bjc-player-wrapper">
                   <video class="video video-js vjs-default-skin">
                   </video>
                 </div>
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
<!--                         <img src="//img.mukewang.com/5ea6507109b99bd803480172.jpg">-->
                       </div><div></div></div>
                   </div>
                 </div>
               </div>
               <div class="next-box J_next-box hide" style="display: none;">
                 <div class="next-box-inner">

                   <div class="course-tip-layer J-next-course" data-next-src="/video/2149">
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
<!--                         <img src="//img.mukewang.com/5ea65155099c258403480172.jpg">-->
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
  import {courseInfo , materialInfo , checkHaveCourse} from "@/api";
  import {addCoursestudyhistory,updateCoursestudyhistory} from "@/api/user";
  import request from '@/router/axios';
  import Cookies from 'js-cookie';

  export default {
    name: 'playerContent',
    data() {
      return {
        sectionListFlag:false,
        materialListFlag:false,
        videoTime:0,
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
          let taocanId = this.$route.query.taocanId;
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
            let userId=that.$parent.studentInfo.id;
            console.log("用户id"+userId);
            if(this.courseKpoint.videoType == "aliyunvod"||this.courseKpoint.videoType == "aliyunReplay"){
              if(this.courseKpoint.playProgress=='1'&& this.courseKpoint.rateComponent=='1'){
                // eslint-disable-next-line no-undef
                player = new Aliplayer({
                  id: 'video-box',
                  width: '100%',
                  autoplay: true,
                  height: '100%',
                  vid : this.courseKpoint.videoUrl,
                  // cover: 'http://liveroom-img.oss-cn-qingdao.aliyuncs.com/logo.png',
                  // cover: this.courseInfo.logo,
                  playauth : this.courseKpoint.params.playAuth,
                  encryptType: 1,
                  format: "m3u8",
                  components: [{
                    name: 'RateComponent',
                    // eslint-disable-next-line no-undef
                    type: AliPlayerComponent.RateComponent
                  }],
                  "skinLayout": [
                    {
                      "name": "bigPlayButton",
                      "align": "blabs",
                      "x": 30,
                      "y": 80
                    },
                    {
                      "name": "H5Loading",
                      "align": "cc"
                    },
                    {
                      "name": "errorDisplay",
                      "align": "tlabs",
                      "x": 0,
                      "y": 0
                    },
                    {
                      "name": "infoDisplay"
                    },
                    {
                      "name": "tooltip",
                      "align": "blabs",
                      "x": 0,
                      "y": 56
                    },
                    {
                      "name": "thumbnail"
                    },
                    {
                      "name": "controlBar",
                      "align": "blabs",
                      "x": 0,
                      "y": 0,
                      "children": [
                        {
                          "name": "playButton",
                          "align": "tl",
                          "x": 15,
                          "y": 12
                        },
                        {
                          "name": "timeDisplay",
                          "align": "tl",
                          "x": 10,
                          "y": 7
                        },
                        {
                          "name": "fullScreenButton",
                          "align": "tr",
                          "x": 10,
                          "y": 12
                        },
                        {
                          "name": "volume",
                          "align": "tr",
                          "x": 5,
                          "y": 10
                        }
                      ]
                    }
                  ]
                },function(player){
                  console.log("没有锁住进度条")
                  let taocanId = that.$route.query.taocanId;
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
                  this.videoTime = Cookies.get("video_play"+userId+""+that.courseKpoint.courseId+""+that.courseKpoint.id);
                  if(this.videoTime!=undefined&&this.videoTime!=null&&this.videoTime!=0){
                    player.seek(parseInt(this.videoTime));
                  }
                  setTimeout(function () {
                    that.$parent.cookieTime =setInterval(function () {
                      this.videoTime = parseInt(player.getCurrentTime());
                      Cookies.set("video_play"+userId+""+that.courseKpoint.courseId+""+that.courseKpoint.id,this.videoTime,7*24*3600)
                    },1000);
                  },3000);
                });
              }
              else if(this.courseKpoint.playProgress=='1'&& this.courseKpoint.rateComponent=='2'){
                // eslint-disable-next-line no-undef
                player = new Aliplayer({
                  id: 'video-box',
                  width: '100%',
                  autoplay: true,
                  height: '100%',
                  vid : this.courseKpoint.videoUrl,
                  // cover: 'http://liveroom-img.oss-cn-qingdao.aliyuncs.com/logo.png',
                  // cover: this.courseInfo.logo,
                  playauth : this.courseKpoint.params.playAuth,
                  encryptType: 1,
                  format: "m3u8",
                  "skinLayout": [
                    {
                      "name": "bigPlayButton",
                      "align": "blabs",
                      "x": 30,
                      "y": 80
                    },
                    {
                      "name": "H5Loading",
                      "align": "cc"
                    },
                    {
                      "name": "errorDisplay",
                      "align": "tlabs",
                      "x": 0,
                      "y": 0
                    },
                    {
                      "name": "infoDisplay"
                    },
                    {
                      "name": "tooltip",
                      "align": "blabs",
                      "x": 0,
                      "y": 56
                    },
                    {
                      "name": "thumbnail"
                    },
                    {
                      "name": "controlBar",
                      "align": "blabs",
                      "x": 0,
                      "y": 0,
                      "children": [
                        {
                          "name": "playButton",
                          "align": "tl",
                          "x": 15,
                          "y": 12
                        },
                        {
                          "name": "timeDisplay",
                          "align": "tl",
                          "x": 10,
                          "y": 7
                        },
                        {
                          "name": "fullScreenButton",
                          "align": "tr",
                          "x": 10,
                          "y": 12
                        },
                        {
                          "name": "volume",
                          "align": "tr",
                          "x": 5,
                          "y": 10
                        }
                      ]
                    }
                  ]
                },function(player){
                  console.log("没有锁住进度条")
                  let taocanId = that.$route.query.taocanId;
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
                  this.videoTime = Cookies.get("video_play"+userId+""+that.courseKpoint.courseId+""+that.courseKpoint.id);
                  if(this.videoTime!=undefined&&this.videoTime!=null&&this.videoTime!=0){
                    player.seek(parseInt(this.videoTime));
                  }
                  setTimeout(function () {
                    that.$parent.cookieTime =setInterval(function () {
                      this.videoTime = parseInt(player.getCurrentTime());
                      Cookies.set("video_play"+userId+""+that.courseKpoint.courseId+""+that.courseKpoint.id,this.videoTime,7*24*3600)
                    },1000);
                  },3000);
                });
              }
              else if(this.courseKpoint.playProgress=='2'&& this.courseKpoint.rateComponent=='1'){
                // eslint-disable-next-line no-undef
                player = new Aliplayer({
                  id: 'video-box',
                  width: '100%',
                  autoplay: true,
                  height: '100%',
                  vid : this.courseKpoint.videoUrl,
                  // cover: 'http://liveroom-img.oss-cn-qingdao.aliyuncs.com/logo.png',
                  // cover: this.courseInfo.logo,
                  playauth : this.courseKpoint.params.playAuth,
                  encryptType: 1,
                  format: "m3u8",
                  components: [{
                    name: 'RateComponent',
                    // eslint-disable-next-line no-undef
                    type: AliPlayerComponent.RateComponent
                  }],
                  "skinLayout": [
                    {
                      "name": "bigPlayButton",
                      "align": "blabs",
                      "x": 30,
                      "y": 80
                    },
                    {
                      "name": "H5Loading",
                      "align": "cc"
                    },
                    {
                      "name": "errorDisplay",
                      "align": "tlabs",
                      "x": 0,
                      "y": 0
                    },
                    {
                      "name": "infoDisplay"
                    },
                    {
                      "name": "tooltip",
                      "align": "blabs",
                      "x": 0,
                      "y": 56
                    },
                    {
                      "name": "thumbnail"
                    },
                    {
                      "name": "controlBar",
                      "align": "blabs",
                      "x": 0,
                      "y": 0,
                      "children": [
                        {
                          "name": "progress",
                          "align": "blabs",
                          "x": 0,
                          "y": 44
                        },
                        {
                          "name": "playButton",
                          "align": "tl",
                          "x": 15,
                          "y": 12
                        },
                        {
                          "name": "timeDisplay",
                          "align": "tl",
                          "x": 10,
                          "y": 7
                        },
                        {
                          "name": "fullScreenButton",
                          "align": "tr",
                          "x": 10,
                          "y": 12
                        },
                        {
                          "name": "volume",
                          "align": "tr",
                          "x": 5,
                          "y": 10
                        }
                      ]
                    }
                  ]
                },function(player){
                  console.log("没有锁住进度条")
                  let taocanId = that.$route.query.taocanId;
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

                  this.videoTime = Cookies.get("video_play"+userId+""+that.courseKpoint.courseId+""+that.courseKpoint.id);
                  if(this.videoTime!=undefined&&this.videoTime!=null&&this.videoTime!=0){
                    player.seek(parseInt(this.videoTime));
                  }
                  setTimeout(function () {
                    that.$parent.cookieTime =setInterval(function () {
                      this.videoTime = parseInt(player.getCurrentTime());
                      Cookies.set("video_play"+userId+""+that.courseKpoint.courseId+""+that.courseKpoint.id,this.videoTime,7*24*3600)
                    },1000);
                  },3000);
                });
              }
              else {
                // eslint-disable-next-line no-undef
                player = new Aliplayer({
                  id: 'video-box',
                  width: '100%',
                  autoplay: true,
                  height: '100%',
                  vid : this.courseKpoint.videoUrl,
                  // cover: 'http://liveroom-img.oss-cn-qingdao.aliyuncs.com/logo.png',
                  // cover: this.courseInfo.logo,
                  playauth : this.courseKpoint.params.playAuth,
                  encryptType: 1,
                  format: "m3u8",

                  "skinLayout": [
                    {
                      "name": "bigPlayButton",
                      "align": "blabs",
                      "x": 30,
                      "y": 80
                    },
                    {
                      "name": "H5Loading",
                      "align": "cc"
                    },
                    {
                      "name": "errorDisplay",
                      "align": "tlabs",
                      "x": 0,
                      "y": 0
                    },
                    {
                      "name": "infoDisplay"
                    },
                    {
                      "name": "tooltip",
                      "align": "blabs",
                      "x": 0,
                      "y": 56
                    },
                    {
                      "name": "thumbnail"
                    },
                    {
                      "name": "controlBar",
                      "align": "blabs",
                      "x": 0,
                      "y": 0,
                      "children": [
                        {
                          "name": "progress",
                          "align": "blabs",
                          "x": 0,
                          "y": 44
                        },
                        {
                          "name": "playButton",
                          "align": "tl",
                          "x": 15,
                          "y": 12
                        },
                        {
                          "name": "timeDisplay",
                          "align": "tl",
                          "x": 10,
                          "y": 7
                        },
                        {
                          "name": "fullScreenButton",
                          "align": "tr",
                          "x": 10,
                          "y": 12
                        },
                        {
                          "name": "volume",
                          "align": "tr",
                          "x": 5,
                          "y": 10
                        }
                      ]
                    }
                  ]
                },function(player){
                  console.log("没有锁住进度条")
                  let taocanId = that.$route.query.taocanId;
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
                  this.videoTime = Cookies.get("video_play"+userId+""+that.courseKpoint.courseId+""+that.courseKpoint.id);
                  if(this.videoTime!=undefined&&this.videoTime!=null&&this.videoTime!=0){
                    player.seek(parseInt(this.videoTime));
                  }
                  setTimeout(function () {
                    that.$parent.cookieTime =setInterval(function () {
                      this.videoTime = parseInt(player.getCurrentTime());
                      Cookies.set("video_play"+userId+""+that.courseKpoint.courseId+""+that.courseKpoint.id,this.videoTime,7*24*3600)
                    },1000);
                  },3000);
                });
              }

            }
          if(this.courseKpoint.videoType == "aliyunlive"){
            // eslint-disable-next-line no-undef
            player = new Aliplayer({
              id: 'video-box',
              width: '100%',
              autoplay: true,
              height: '100%',
              isLive: true,
              source : this.courseKpoint.videoUrl,
              skinLayout: [
                {
                  name: "bigPlayButton",
                  align: "blabs",
                  x: 30,
                  y: 80
                },
                {
                  name: "errorDisplay",
                  align: "tlabs",
                  x: 0,
                  y: 0
                },
                {
                  name: "infoDisplay"
                },
                {
                  name: "controlBar",
                  align: "blabs",
                  x: 0,
                  y: 0,
                  children: [
                    {
                      name: "liveDisplay",
                      align: "tlabs",
                      x: 30,
                      y: 6
                    },
                    {
                      name: "fullScreenButton",
                      align: "tr",
                      x: 15,
                      y: 17
                    },
                    {
                      name: "volume",
                      align: "tr",
                      x: 15,
                      y: 15
                    }
                  ]
                }
              ]
            },function(player){
              let taocanId = that.$route.query.taocanId;
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
            });

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
      // 通过$once来监听定时器，在beforeDestroy钩子可以被清除。
      this.$once('hook:beforeDestroy', () => {
        clearInterval(that.$parent.cookieTime)
      });
      window.addEventListener('beforeunload',this.beforeUnloadHandler,true);

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
  .section-list .sec-li.active a {
    color: #0091ff;
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
  .section-list .sec-li.active i {
    color: #0091ff;
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
  .rank-icon {
    display: inline-block;
    position: relative;
    vertical-align: -6px;
    margin-right: 5px;
    width: 22px;
    height: 22px;
    margin-right: 0px !important;
  }
  .rank-icon i:nth-child(1) {
    animation: lines 2s linear .2s infinite;
    left: 6px;
  }
  .rank-icon i:nth-child(2) {
    animation: lines 2s linear .4s infinite;
    left: 10px;
  }
  .rank-icon i:nth-child(3) {
    animation: lines 2s linear .6s infinite;
    left: 14px;
  }
  .rank-icon i {
    width: 2px;
    height: 2px;
    background: #0091ff;
    position: absolute;
    bottom: 7px;

  }
  @keyframes lines{
    0%{height:2px}
    25%{height:8px}
    50%{height:2px}
    75%{height:8px}
    100%{height:2px}
  }
  .pass{
    color: #00b783 !important;
  }
  .unpass{
    color: #ff0000 !important;
  }
</style>
