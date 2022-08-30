<template>
  <div class="u-container">
    <div class="c-tab clearfix">
      <div class="tool-left l">
        <router-link :to="{path:'/uc/myCourse',query:{courseType:''}}" active-class="on" class="sort-item " v-bind:class="{'active' :'' == courseType}" >全部</router-link>
        <router-link :to="{path:'/uc/myCourse',query:{courseType:'COURSE'}}" active-class="on" class="sort-item " v-bind:class="{'active' :'COURSE' == courseType}" >点播</router-link>
        <router-link :to="{path:'/uc/myLive',query:{courseType:'LIVE'}}"  class="sort-item " v-bind:class="{'active' :'LIVE' == courseType}" >直播</router-link>
        <router-link :to="{path:'/uc/myPackage',query:{courseType:'PACKAGE'}}" class="sort-item  " v-bind:class="{'active' :'PACKAGE' == courseType}" >班级</router-link>
        <router-link :to="{path:'/uc/myLineDownCourse',query:{courseType:'LINECLASS'}}" class="sort-item  " v-bind:class="{'active' :'LINECLASS' == courseType}" >面授</router-link>
        <router-link :to="{path:'/uc/myLineEndCourse',query:{courseType:'LINECLASSEND'}}" class="sort-item  " v-bind:class="{'active' :'LINECLASSEND' == courseType}" >过期课程</router-link>
      </div>
    </div>

    <div class="all-course-main" v-if="detailList.length >0">
      <div class="allcourse-content js-course-list ">
        <div class="courseitem tl-item"  v-for="detail in detailList" :key="detail.id">
          <div class="img-box">
            <router-link :to="{path:'/lineDownCourse/lineInfo/'+detail.courseId}" target="_blank">
              <img width="200" height="116" alt="vue-cli全集" v-lazy="detail.logo">
            </router-link>
          </div>
          <div class="info-box course-list">
            <div class="title">
              <span >面授</span>
              <router-link :to="{path:'/lineDownCourse/lineInfo/'+detail.courseId}"  class="hd" target="_blank">{{detail.courseName}}</router-link>
            </div>
            <div class="catog-points" v-if="detail.lineTeacherIs==1">
              <span class="i-left span-common ml10" >报名姓名：{{detail.lineName}}</span>
              <span class="i-left span-common ml10" >报名手机：{{detail.lineMobile}}</span>
              <a @click="lineTeacherPlay(detail.courseId)" href="javascrpt:void(0)" class="btn-red continute-btn">查看安排</a>
            </div>
            <div class="catog-points" v-if="detail.lineTeacherIs==1">
              <span class="i-left span-common ml10">报名时间：{{detail.lineTime}}</span>
           </div>
            <div class="catog-points" >
              <span class="i-left span-common ml10" >上课时间：{{detail.lineBagenTime|dataFormat('yyyy年MM月dd日 hh:mm')}} 至 {{detail.lineEndTime|dataFormat('yyyy年MM月dd日 hh:mm')}}</span>
            </div>
            <div class="catog-points" v-if="detail.lineTeacherIs==2">
              <span class="i-left span-common ml10 color-red" >暂未报名，请尽快报名。   </span>
              <a @click="showBaoMing(detail.courseId)" href="javascrpt:void(0)" class="btn-red continute-btn">立即报名</a>
            </div>
          </div>
        </div>
      </div>

    </div>
    <!-- 分页 -->
    <page-component :page="page" @goto="goto" v-show="detailList.length >0"></page-component>
    <div class="nodata" v-if="detailList.length == 0">
      <p><i class="imv2-error_c"></i></p>
      <p>你还没有面授课程。</p>
    </div>
    <div class="bMask" v-show="courseCardShowFlag"></div>

  </div>
</template>

<script>
  import { mapGetters } from "vuex";
  import {getTrxorderdetail} from "@/api/user";
  import pageComponent from '@/views/common/pageComponent'
  import {courseInfo} from "@/api";
  import courseCardLayer from '@/views/ucenter/common/courseCardLayer'
  import baoMingUcLayer from '@/views/ucenter/common/baoMingUcLayer'
  export default {
    name: 'myCourse',
    data() {
      return {
        detailList:[],
        page:{},
        courseCardShowFlag:false,
        current:1,
        size:20,
        courseType:"",
        lineFaceCourseId:0,
      }
    },
    methods: {
      clickTab(i){
        this.courseType = i
        this.getList();
      },
      showBaoMing(id){
        this.lineFaceCourseId=id;
        this.$layer.iframe({
          content: {
            content: baoMingUcLayer,
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
      lineTeacherPlay(courseId) {//去往面授详情。
        this.$router.push({ path: '/lineDownCourse/lineInfo/'+courseId+'?showFlag=1' })
        return
      },
      gotoPlay(courseId,trxorderType){//课程详情去播放
        if(!(trxorderType == 'COURSE')){
          this.$router.push({ path: '/course/courseInfo/'+courseId })
          return
        }
        //为了防止浏览器进行弹出拦截先打开页面
        var tempwindow=window.open(); // 先打开页面
          courseInfo(courseId).then(res =>{
              let coursedata = res.data.data;
              if(coursedata.sellType == "3"){
                this.$router.push({ path: '/course/courseInfo/'+coursedata.id })
              }else if(coursedata.sellType == "2"){
                this.$router.push({ path: '/course/courseInfo/'+coursedata.id })
              }else{
                let klist = coursedata.list
                let kpoint =0;
                try{
                  if(klist.length >0){
                    let child = klist[0].children
                    if(child.length > 0){
                      kpoint = child[0]
                    }
                  }
                }catch (e) {
                  let url = this.$router.resolve({ path: '/course/courseInfo/'+coursedata.id })
                  tempwindow.location=url.href;
                  return
                }

                if (kpoint != 0){
                  var newUrl = this.$router.resolve({ path: '/uc/player/'+kpoint.id })
                  // window.open(newUrl.href, "_blank");
                  tempwindow.location=newUrl.href;
                }else {
                  tempwindow.close()
                  this.$layer.alert(
                    "课程暂无章节，请学习其他课程",
                    {
                      shade: true,
                      title: "提示"
                    })
                }

              }

          })
      },
      goto(i){
        this.current = i
        this.getList();
      },
      showCourseCardJihuo(){
        // this.$layer.confirm('确定要<br/>删除吗？', layerid => {
        //   this.$layer.msg('执行取消');
        //   this.$layer.close(layerid);
        // });
        this.$layer.iframe({
          content: {
            content: courseCardLayer,
            parent: this,
            // data: { iframeData: this.iframeData }
          },
          area: ['380px', '240px'],
          title: '学习卡激活',
          maxmin: false,
          shade: true,
          shadeClose: true,
          cancel: () => { //关闭弹窗事件
            // alert('关闭iframe');
          }
        });
        // this.courseCardShowFlag = true;
      },
      getList(){
        if (typeof(this.studentInfo.id) == "undefined"){
          return;
        }
        let params = {
          authStatus:2,
          current:this.current,
          size:this.size,
        }
        if(this.courseType != ''){
          params.trxorderType = this.courseType
        }
        getTrxorderdetail(params).then(res =>{
          this.detailList = res.data.data.records
          this.page = res.data.data
        })
      }
    },
    mounted:function() {
      this.courseType = this.$route.query.courseType;
      this.getList();
    },
    computed: {
      ...mapGetters(["studentInfo"]),
    },
    components: {
      pageComponent
    },
    watch: {
      studentInfo: function (val, oldVal) {
        this.getList();
      }
    },
  }
</script>

<style scoped>
  .course_card_btn {
    display: inline-block;
    width: 100px;
    height: 26px;
    font-size: 14px;
    background: #0091ff;
    border: 1px solid #0091ff;
    border-radius: 12px;
    margin: 15px auto 0;
    text-align: center;
    color: #ffffff;
    line-height: 26px;
    cursor: pointer;
  }
  .bMask {
    background: #000;
    opacity: .3;
    filter: alpha(opacity=30);
    position: fixed;
    height: 100%;
    width: 100%;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    z-index: 999999;
  }

 /* display: inline-block;
  position: absolute;
  right: 0;
  font-size: 14px;
  border: none;
  color: #fff;
  width: 104px;
  height: 36px;
  line-height: 36px;
  text-align: center;
  background: rgba(0, 145, 255, 0.8);
  border-radius: 18px;*/
</style>
