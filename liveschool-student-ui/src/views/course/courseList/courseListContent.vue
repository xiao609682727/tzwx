<template>
  <div class="container">
    <div class="course-tool-bar clearfix">
      <div class="tool-left l">
        <a href="javascript:void(0)" @click="toOrderBy('default')" :class="orderBy=='default' ? 'active' : ''" class="moco-change-smalle-btn">默认</a>
        <a href="javascript:void(0)" @click="toOrderBy('update')" :class="orderBy=='update' ? 'active' : ''" class="moco-change-smalle-btn">最新</a>
        <a href="javascript:void(0)" @click="toOrderBy('free')" :class="orderBy=='free' ? 'active' : ''" class="moco-change-smalle-btn">免费</a>
      </div>
    </div>
    <div class="course-list mt10">
      <div class="moco-course-list">
        <div class="clearfix">
          <div class="course-card-container course-card-container-live" v-for="(course,index) in courseList" :key="index">
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
      <page-component :page="page" @goto="goto" v-show="courseList.length > 0" ></page-component>
      <div class="nodata" v-if="courseList.length == 0">
        <p><i class="imv2-error_c"></i></p>
        <p>暂无数据</p>
      </div>
    </div>

  </div>
</template>

<script>
  import {course} from "@/api";
  import pageComponent from '@/views/common/pageComponent'

  export default {
    name: 'courseListContent',
    data() {
      return {
        courseList:[],
        current:1,
        size:8,
        sellType:"",
        level:"",
        subjectId:"",
        subject1Id:"",
        pageList:[],
        pages:0,
        page:{},
        levelFlag:false,
        orderBy:"default",
      }
    },
    components: {
      pageComponent
    },
    methods: {
      handler (component) {
        console.log('this component is showing')
      },
      goto(i){
        this.current = i
        this.selectCouseList();
      },
      toOrderBy(orderBy1){//排序
          this.orderBy = orderBy1;
          this.selectCouseList();
      },
      selectCouseList(){
        let subjectId = "";
        let level = "";
        let sellType = "";
        let courseName = this.$route.query.searchCourseName;
        let typeActive = this.$route.query.courseType;
        let subLevel1Active = this.$route.query.subLevel1;
        let subLevel2Active = this.$route.query.subLevel2;
        if (typeof(subLevel1Active) == "undefined"){
          subLevel1Active = 0
        }
        if (typeof(subLevel2Active) == "undefined"){
          subLevel2Active = 0
        }

        if(typeActive != 0){
          sellType = typeActive
        }
        if(subLevel2Active == 0&&subLevel1Active == 0){
          level = "";
          subjectId = "";
        }else if(subLevel2Active == 0&&subLevel1Active != 0){
          level = "1";
          subjectId = subLevel1Active;
        }else{
          level = "2";
          subjectId = subLevel2Active;
        }
        course(this.current,this.size,sellType,level,subjectId,courseName,'',this.orderBy).then(res =>{
          this.courseList = res.data.data.records;
          this.page = res.data.data
          console.log(this.page);
          this.pages = res.data.data.pages
          console.log(this.pages);
          this.computePage(res.data.data.pages)
        })
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
      dateCompareToDate(date){
        let getDate = new Date(date);
        return getDate;
      },
      computePage(pages){
        this.pageList = []
        if(pages<=7){
          for (let i = 1; i <= pages; i++) {
            this.pageList.push(i)
          }
        }else{
          if(this.current <= 4){
            this.pageList = [1,2,3,4,5,6,7];
          }else if(this.current+3<=pages) {
            let n = this.current
            this.pageList = [n-3,n-2,n-1,n,n+1,n+2,n+3];
          }else{
            this.pageList = [pages-6,pages-5,pages-4,pages-3,pages-2,pages-1,pages];
          }
        }
      }
    },
    mounted:function() {
      this.selectCouseList();
    },
  }
</script>

<style scoped>
  .rank-icon {
    display: inline-block;
    position: relative;
    vertical-align: -6px;
    margin-right: 5px;
    width: 22px;
    height: 22px;
    background: #FFFFFF;
    border-radius: 50%;
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
  .signUpNum{
    font-size: 10px;
    color: #5e5e5e;
    line-height: 28px;
  }
</style>
