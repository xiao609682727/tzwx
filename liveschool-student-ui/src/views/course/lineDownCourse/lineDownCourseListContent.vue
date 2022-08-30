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
        <div class="clearfix"  v-for="(course,index) in courseList" :key="index">
          <div class="series_item">
            <img class="series_img" v-lazy="course.logo" onerror="this.src='/static/img/course-default.png'">
            <div class="series_info">
              <div class="series_title">
              <router-link target="_blank" :to="{path:'/lineDownCourse/lineInfo/'+course.id}" :title="course.courseName" class="course-card">{{course.courseName}}</router-link>
                <div class="series_place">{{course.faceTeachingSubjectAddress}}</div></div>
              <div class="series_subtitle">{{course.faceTeachingTime|dataFormat('yyyy年MM月dd日 hh:mm')}} 至 {{course.endTime|dataFormat('yyyy年MM月dd日 hh:mm')}}</div>
              <div class="series_subtitle">上课地点：{{course.faceTeachingAddress}}</div>
              <div class="clearfix">
                <div class="float_left">
                  <div  v-if="course.bogusBuycount == 0" class="series_txt">学习人数  {{course.pageBuycount}}</div>
                  <div  v-if="course.bogusBuycount != 0" class="series_txt">学习人数  {{course.bogusBuycount + course.pageBuycount}}</div>
                  <div class="series_dian"></div>
                  <div v-if="course.bogusBuycount == 0" class="series_txt">浏览  {{course.pageViewcount}}</div>
                  <div v-if="course.bogusBuycount != 0" class="series_txt">浏览  {{course.bogusViewcount + course.pageViewcount}}</div>
                </div>

              </div>
            </div>
            <div class="fl" style="height: 130px; border-left: 1px dashed rgb(220, 220, 220); padding-left: 20px; margin-top: 3%;">
              <div class="price">
                <span  v-if="course.currentPrice == '0.00'">免费</span>
                <span v-if="course.currentPrice != '0.00'"><span style="font-size: 17px;">￥</span>{{course.currentPrice}}</span>
               <!--<span v-if="course.currentPrice != '0.00'"><span style="font-size: 17px;">￥</span>{{course.sourcePrice}}</span>-->
              </div>
              <div class="view-details">
                <router-link target="_blank" :to="{path:'/lineDownCourse/lineInfo/'+course.id}" :title="course.courseName" class="course-card">
                  <span style="font-size: 16px; font-weight: 500; color: rgb(255, 255, 255); line-height: 22px;">查看详情</span>
                </router-link>
              </div>
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
  import {lineCourse} from "@/api";
  import pageComponent from '@/views/common/pageComponent'

  export default {
    name: 'courseListContent',
    data() {
      return {
        courseList:[],
        current:1,
        size:5,
        sellType:4,
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
        let sellType = 4;
        let courseName = this.$route.query.searchCourseName;
        let typeActive = this.$route.query.courseType;
        let subLevel1Active = this.$route.query.subLevel1;
        let subLevel2Active = this.$route.query.subLevel2;
        let addressActive = this.$route.query.addressActive;
        if (typeof(subLevel1Active) == "undefined"){
          subLevel1Active = 0
        }
        if (typeof(subLevel2Active) == "undefined"){
          subLevel2Active = 0
        }
        if (typeof(addressActive) == "undefined"){
          addressActive = 0
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
        lineCourse(this.current,this.size,sellType,level,subjectId,courseName,'',this.orderBy,addressActive).then(res =>{
          this.courseList = res.data.data.records;
          this.page = res.data.data;
          console.log(this.page);
          this.pages = res.data.data.pages;
          console.log(this.pages);
          this.computePage(res.data.data.pages)
        })
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
  .series_item{
    background: #fff;
    height: 223px;
    -webkit-box-shadow: 0 1px 15px 0 rgba(66,65,76,.06);
    box-shadow: 0 1px 15px 0 rgba(66,65,76,.06);
    border-radius: 5px;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    padding: 30px;
    margin-bottom: 20px;
  }
  .series_img{
    width: 300px;
    border-radius: 8px;
    float: left;
  }
  .series_info{
    float: left;
    margin-left: 30px;
    width: 57%;
  }
  .series_title{
    color: #05111a;
    font-size: 18px;
    font-weight: 700;
    margin-bottom: 18px;
    display: flex;
  }
  .series_subtitle{
    color: #666;
    font-size: 13px;
    margin-bottom: 12px;
    line-height: 22px;
    overflow: hidden;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 4;
    display: -webkit-box;
  }
  .float_left {
    float: left;
  }
  .float_right {
    float: right;
  }
  .series_txt{
    float: left;
    color: #666;
    font-size: 14px;
    line-height: 18px;
  }
  .series_dian{
    float: left;
    width: 3px;
    height: 3px;
    background: #bbb;
    border-radius: 50%;
    margin: 7px 10px 0;
  }

  .series_place{
    height: 22px;
    background: rgb(239, 168, 92);
    border-radius: 2px;
    margin-left: 10px;
    font-size: 14px;
    color: rgb(255, 255, 255);
    display: flex;
    justify-content: center;
    align-items: center;
    padding-left: 10px;
    padding-right: 10px;
  }
  .price{
    font-size: 25px;
    font-weight: 600;
    color: #ff1d00;
    line-height: 25px;
    margin-bottom: 40px;
    text-align: center;
  }
  .view-details{
    width: 120px;
    height: 36px;
    background: #1890ff;
    border-radius: 19px;
    display: flex;
    justify-content: center;
    align-items: center;
  }
</style>
