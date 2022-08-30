<template>
  <div>
    <common-header ></common-header>
    <div class="main">
      <banner></banner>
      <hot-course></hot-course>
      <teacher-list></teacher-list>
      <hot-article></hot-article>
<!--      <update-course></update-course>-->
      <div class="clear"></div>
    </div>
    <common-footer></common-footer>
  </div>
</template>

<script>
import commonHeader from '@/views/common/header'
import commonFooter from '@/views/common/footer'
import hotCourse from './hotCourse.vue'
import hotArticle from './hotArticle.vue'
import teacherList from './teacherList.vue'
import banner from './banner.vue'
export default {
  name: 'Login',
  data () {
    return {
      isReg: false,
      name: '',
      password: '',
      repeat: ''
    }
  },components:{
    commonHeader,commonFooter,hotCourse,banner,hotArticle,teacherList
  },
  methods: {

  }
}
</script>

<style scoped>


</style>
