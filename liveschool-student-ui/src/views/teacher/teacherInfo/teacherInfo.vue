<template>
  <div v-wechat-title="teacherInfoTitle">
    <common-header :headNav="headNav"></common-header>
    <div class="main teacherCont ">
      <div class="banner">
        <div class="teacher-index">
          <div class="inner">
            <div class="fl tea-left">
              <div class="pic">
                <img :src="teacher.headImg" alt="" class="bg2">
              </div>
              <div class="intro">
                <div class="clearfix teach-name">
                  <div class="name fl">{{teacher.name}}</div>
                </div>
                <div class="profession">
                  {{teacher.education}}
                </div>
                <div class="tea-right" >
                  <span >共</span>
                  <span class="num" >{{teacherCourseListSize}}</span>
                  <span >课程</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="inner">
        <div class="teacher-intro">
          <div class="jsjs" >
            <div class="fl">
              <h3 class="title">讲师介绍</h3>
            </div>
          </div>
          <div class="jsjs-content" v-html="teacher.profile">
          </div>
        </div>
      </div>
      <div class="inner">
        <div class="teacher-intro">
          <div class="jsjs" >
            <div class="fl">
              <h3 class="title">讲师课程</h3>
            </div>
          </div>
          <div class="jskc-content" >
            <div class="course-list">
              <div class="moco-course-list">
                <div class="clearfix">
                  <div class="course-card-container course-card-container-live" v-for="(course,index) in teacherCourseList" :key="index">
                    <!--点播-->
                    <div v-if="course.sellType == '1'">
                      <div v-if="course.sellType == '1'" class="course-stat hot">
                        点播
                      </div>
                      <router-link target="_blank" :to="{path:'/course/courseInfo/'+course.id}" :title="course.courseName" class="course-card">
                        <div class="course-card-top">
                          <img class="course-banner lazy"
                               style="display: inline;" v-lazy="course.logo" onerror="this.src='/static/img/course-default.png'">
                          <!--<div class="course-label">
                            <label>Java</label>
                          </div>-->
                        </div>
                        <div class="course-card-content">
                          <h3 class="course-card-name">{{course.courseName}}</h3>
                          <div class="clearfix course-card-bottom">
                            <div class="course-card-price">
                              <div class="price-box">
                                <span class="price red fs18" v-if="course.currentPrice == '0.00'" >免费</span>
                                <span class="price red fs18" v-if="course.currentPrice != '0.00'" >￥{{course.currentPrice}}</span>
                                <span class="cost-price" v-if="course.sourcePrice != '0.00'" >￥{{course.sourcePrice}}</span>
                              </div>
                            </div>
                          </div>
                          <div class="clearfix course-card-bottom">
                            <div class="course-card-price">
                              <div class="price-box">
                                <span v-if="course.bogusBuycount == 0" class="fl signUpNum">{{course.pageViewcount}}人浏览</span>
                                <span v-if="course.bogusBuycount != 0" class="fl signUpNum">{{course.bogusViewcount + course.pageViewcount}}人浏览</span>
                                <span v-if="course.bogusBuycount == 0" class="r signUpNum">{{course.pageBuycount}}人学习</span>
                                <span v-if="course.bogusBuycount != 0" class="r signUpNum">{{course.bogusBuycount + course.pageBuycount}}人学习</span>
                              </div>
                            </div>
                          </div>
                        </div>
                      </router-link>
                    </div>
                    <!--直播-->
                    <div v-if="course.sellType == '2'">
                      <div v-if="course.sellType == '2'" class="course-stat new">
                        直播
                      </div>
                      <router-link target="_blank" :to="{path:'/course/courseInfo/'+course.id}" :title="course.courseName" class="course-card">
                        <div class="course-card-top">
                          <img class="course-banner lazy"
                               style="display: inline;" v-lazy="course.logo" onerror="this.src='/static/img/course-default.png'">
                          <!--<div class="course-label">
                            <label>Java</label>
                          </div>-->
                        </div>
                        <div class="course-card-content">
                          <h3 class="course-card-name">{{course.courseName}}</h3>
                          <div class="clearfix course-card-bottom">
                            <div class="course-card-info" v-if="JSON.stringify(course.nextLiveCourseKpoint)!=='{}'" >
                              <span>开播时间：{{dateFormat('mm月dd日 HH:MM',course.nextLiveCourseKpoint.liveBeginTime)}}</span>
                              <span v-if="dateCompareToDate(course.nextLiveCourseKpoint.liveBeginTime) < new Date() && dateCompareToDate(course.nextLiveCourseKpoint.liveEndTime) > new Date()" class="r js-hover-evaluation" style="color: #0091ff;margin-right: 0px;">
                                <span class="rank-icon"><i></i><i></i><i></i></span>
                                直播中
                              </span>
                            </div>
                            <div class="course-card-info" v-if="JSON.stringify(course.nextLiveCourseKpoint)==='{}'">
                              <span>直播已结束</span>
                            </div>
                            <div class="course-card-price">
                              <div class="price-box">
                                <span class="price red fs18" v-if="course.currentPrice == '0.00'" >免费</span>
                                <span class="price red fs18" v-if="course.currentPrice != '0.00'" >￥{{course.currentPrice}}</span>
                                <span class="cost-price" v-if="course.sourcePrice != '0.00'" >￥{{course.sourcePrice}}</span>

                                <!--<div class="collect clearfix js-course-collect"  title="收藏"><i
                                  class="imv2-star"></i><span>收藏</span></div>-->
                              </div>
                            </div>
                          </div>
                        </div>
                      </router-link>
                    </div>
                    <!--班级-->
                    <div v-if="course.sellType == '3'">
                      <div v-if="course.sellType == '3'" class="course-stat upgrade">
                        班级
                      </div>
                      <router-link target="_blank" :to="{path:'/course/courseInfo/'+course.id}" :title="course.courseName" class="course-card">
                        <div class="course-card-top">
                          <img class="course-banner lazy"
                               style="display: inline;" v-lazy="course.logo" onerror="this.src='/static/img/course-default.png'">
                          <!--<div class="course-label">
                            <label>Java</label>
                          </div>-->
                        </div>
                        <div class="course-card-content">
                          <h3 class="course-card-name">{{course.courseName}}</h3>
                          <div class="clearfix course-card-bottom">
                            <div class="course-card-price">
                              <div class="price-box">
                                <span class="price red fs18" v-if="course.currentPrice == '0.00'" >免费</span>
                                <span class="price red fs18" v-if="course.currentPrice != '0.00'" >￥{{course.currentPrice}}</span>
                                <span class="cost-price" v-if="course.sourcePrice != '0.00'" >￥{{course.sourcePrice}}</span>
                              </div>
                            </div>
                          </div>
                          <div class="clearfix course-card-bottom">
                            <div class="course-card-price">
                              <div class="price-box">
                                <span v-if="course.bogusBuycount == 0" class="fl signUpNum">{{course.pageViewcount}}人浏览</span>
                                <span v-if="course.bogusBuycount != 0" class="fl signUpNum">{{course.bogusViewcount + course.pageViewcount}}人浏览</span>
                                <span v-if="course.bogusBuycount == 0" class="r signUpNum">{{course.pageBuycount}}人学习</span>
                                <span v-if="course.bogusBuycount != 0" class="r signUpNum">{{course.bogusBuycount + course.pageBuycount}}人学习</span>
                              </div>
                            </div>
                          </div>
                        </div>
                      </router-link>
                    </div>
                  </div>

                  <div class="clear"></div>
                </div>
              </div>
              <page-component :page="page" @goto="goto" v-show="teacherCourseList.length > 0" ></page-component>
              <div class="nodata" v-if="teacherCourseList.length == 0">
                <p><i class="imv2-error_c"></i></p>
                <p>暂无数据</p>
              </div>
            </div>
            <div class="clear"></div>
          </div>
        </div>
      </div>
      <div>

      </div>
    </div>

    <common-footer></common-footer>
  </div>
</template>

<script>
import commonHeader from '@/views/common/header'
import commonFooter from '@/views/common/footer'
import {getTeacherInfo} from "@/api/teacher";
import {course} from "@/api";
import pageComponent from '@/views/common/pageComponent'
import Store from "@/store";
export default {
  name: 'teacherInfo',
  data () {
    return {
      teacherInfoTitle:"",
      headNav:"/teacher/teacherList",
      teacher:{},
      teacherId:0,
      teacherCourseList:{},
      teacherCourseListSize:0,
      page:{},
      current:1,
      size:8,
    }
  },components:{
    commonHeader,commonFooter,pageComponent
  },
  methods: {
    init(){
      let id = this.$route.params.id;
      this.teacherId=id;
      getTeacherInfo(id).then(res=>{
        this.teacher=res.data.data;
        //把教师名字动态赋值给title
        let title = "";
        if (this.$store.getters.website.title) {
          title = this.teacher.name+"介绍-"+this.$store.getters.website.title;
        }
        this.teacherInfoTitle = title;
      })
      this.getCourseList();
    },
    getCourseList(){
      let that = this
      course(this.current,this.size,'',0,0,'',this.teacherId,'default').then(res =>{
        that.teacherCourseList = res.data.data.records;
        that.page = res.data.data;
        that.teacherCourseListSize = res.data.data.total;
      })
    },goto(i){
      this.current = i
      this.getCourseList();
    }
  },
  mounted:function() {
    this.init();
  },
}
</script>

<style scoped>
.teacherCont .banner{
  background-image: url(/static/img/teacher/teacher-bg.png);
  width: 100%;
  box-shadow: 0 4px 8px 0 rgba(28, 31, 33, 0.1);
}
.teacherCont .banner .teacher-index{
  width: 100%;
}
.teacherCont .banner .teacher-index .inner{
  margin-left: auto;
  margin-right: auto;
  width: 1200px;
  padding: 20px;
  overflow: hidden;
}
.teacherCont .banner .teacher-index .tea-left .pic{
  float: left;
  width: 136px;
  height: 136px;
  border-radius: 50%;
  overflow: hidden;
  position: relative;
}
.teacherCont .banner .teacher-index .tea-left .pic img.bg2{
  width: 90%;
  height: 90%;
  border-radius: 50%;
  position: absolute;
  top: 5%;
  left: 5%;
}
.teacherCont .banner .teacher-index .tea-left .intro .name{
  font-size: 2rem;
  color: #FFFFFF;
  margin-top: 10px;
  float: left;
  font-weight: 600;
}
.teacherCont .banner .teacher-index .tea-left .intro .profession{
  font-size: 1.2rem;
  color: #FFFFFF;
  width: 950px;
  margin-top: 12px;
  display: -webkit-box;
  word-break: break-all;
  text-overflow: ellipsis;
  overflow: hidden;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
  height: 1.6em;
  line-height: 1.6;
  position: relative;
}
.tea-right{
  margin-right: 20px;
  margin-top: 15px;
}
.tea-right span.num, .tea-right span {
  font-size: 1.2rem;
}
.tea-right span {
  color: #FFFFFF;
}
.teacherCont .banner .teacher-index .tea-left .intro {
  float: left;
  margin-left: 20px;
}
.teacherCont .teacher-intro{
  background-color: #fff;
  border-radius: 6px;
}
.teacherCont .teacher-intro .jsjs{
  height: 17px;
  line-height: 17px;
  margin-bottom: 22px;
  padding-top: 20px;
}
.teacherCont .teacher-intro .jsjs-content{
  padding-left: 20px;
  font-size: 1.2rem;
  padding-bottom: 20px;
}
.teacherCont .teacher-intro .jskc-content{
  font-size: 1.2rem;
  padding-bottom: 20px;
}
.teacherCont .teacher-intro h3.title {
  border-left: 4px solid #1890ff;
  line-height: 1;
  margin: 0 20px;
  padding-left: 10px;
  font-size: 1.6rem;
  font-weight: 500;
}
.inner {
  width: 1200px;
  padding: 20px 0;
  margin-left: auto;
  margin-right: auto;
}
.course-card-container {
  box-shadow: 0 6px 10px 0 rgba(95, 101, 105, 0.15);
}
</style>
