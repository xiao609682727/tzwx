<template>
  <div v-wechat-title="title">
    <common-header :headNav="headNav"></common-header>
    <div class="main">
      <course-infoheader v-if="headFlag" :courseInfo="courseInfo"></course-infoheader>
      <course-info-content :courseInfo="courseInfo" v-if="courseInfo.sellType != 3"></course-info-content>
      <course-info-taocan-content :courseInfo="courseInfo" v-if="courseInfo.sellType == 3"></course-info-taocan-content>
      <div class="clear"></div>
    </div>
    <common-footer></common-footer>
  </div>
</template>

<script>

import {courseInfo} from "@/api";
import commonHeader from '@/views/common/header'
import commonFooter from '@/views/common/footer'
import courseInfoheader from '@/views/course/courseInfo/courseInfoheader'
import courseInfoContent from '@/views/course/courseInfo/courseInfoContent'
import courseInfoTaocanContent from '@/views/course/courseInfo/courseInfoTaocanContent'
import Store from "../../../store";
export default {
  name: 'courseList',
  data () {
    return {
      title:"",
      courseInfo:{},
      headNav:"",
      headFlag:false
    }
  },components:{
    commonHeader,commonFooter,courseInfoheader,courseInfoContent,courseInfoTaocanContent
  },
  methods: {
    init(){
      let that = this
      let id = this.$route.params.id;
      //查询课程信息
      courseInfo(id).then(res =>{
        that.courseInfo = res.data.data;
        //把课程名字动态赋值给title
        let title = "";
        if (Store.getters.website.title) {
          title = res.data.data.courseName+"-"+Store.getters.website.title;
        }
        this.title = title;
        that.headFlag = true
        if(that.courseInfo.sellType == "1"){
          that.headNav = "/course/courseList?courseType=1"
        }
        if(that.courseInfo.sellType == "2"){
          that.headNav = "/course/courseList?courseType=2"
        }
        if(that.courseInfo.sellType == "3"){
          that.headNav = "/course/courseList?courseType=3"
        }
      })
    }
  },
  mounted:function() {
    this.init()
  },
}
</script>

<style scoped>

</style>
