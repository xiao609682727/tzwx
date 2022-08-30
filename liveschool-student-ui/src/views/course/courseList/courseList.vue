<template>
  <div>
    <common-header :headNav="headNav"></common-header>

    <div class="main">
      <select-course-subject></select-course-subject>
      <course-list-content  ></course-list-content>
      <div class="clear"></div>
    </div>

    <common-footer></common-footer>
  </div>
</template>

<script>
import commonHeader from '@/views/common/header'
import commonFooter from '@/views/common/footer'
import selectCourseSubject from '@/views/course/courseList/selectCourseSubject'
import courseListContent from '@/views/course/courseList/courseListContent'
export default {
  name: 'courseList',
  data () {
    return {
      headNav:"/course/courseList"
    }
  },components:{
    commonHeader,commonFooter,selectCourseSubject,courseListContent
  },
  methods: {

  },
  mounted:function() {
    let courseType = this.$route.query.courseType;
    if(courseType == 0){
      this.headNav = "/course/courseList?courseType=1";
    }
    if(courseType == 1){
      this.headNav = "/course/courseList?courseType=1";
    }
    if(courseType==2){
      this.headNav = "/course/courseList?courseType=2";
    }
    if(courseType==3){
      this.headNav = "/course/courseList?courseType=3";
    }
  },
}
</script>

<style scoped>

</style>
