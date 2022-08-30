<template>
  <div>
    <common-header :headNav="headNav"></common-header>

    <div class="main">
      <select-course-subject></select-course-subject>
      <line-down-course-list-content></line-down-course-list-content>
      <div class="clear"></div>
    </div>

    <common-footer></common-footer>
  </div>
</template>

<script>
import commonHeader from '@/views/common/header'
import commonFooter from '@/views/common/footer'
import selectCourseSubject from '@/views/course/lineDownCourse/selectCourseSubject'
import lineDownCourseListContent from '@/views/course/lineDownCourse/lineDownCourseListContent'
export default {
  name: 'courseList',
  data () {
    return {
      headNav:"/lineDownCourse/list"
    }
  },components:{
    commonHeader,commonFooter,selectCourseSubject,lineDownCourseListContent
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
