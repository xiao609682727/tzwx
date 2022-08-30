<template>
  <div>
    <common-header :headNav="headNav"></common-header>
    <div id="head" class="sig-head">
      <a href="/" target="_self" class="">
        <img :src="website.loginLogo">
      </a>
    </div>
    <div id="main " style="margin-bottom: 50px;min-height: 450px;">
      <router-view/>
      <div class="clear"></div>
    </div>
    <common-footer></common-footer>
  </div>
</template>

<script>
import commonHeader from '@/views/common/header'
import commonFooter from '@/views/common/footer'
  import { mapGetters } from "vuex";
  import {websiteProfile} from "@/api";
import selectCourseSubject from "@/views/course/courseList/selectCourseSubject";
import courseListContent from "@/views/course/courseList/courseListContent";


  export default {
    name: 'Login',
    data() {
      return {

      }
    },components:{
      commonHeader,commonFooter
    },
    methods: {

    },
    computed: {
      ...mapGetters(["website"]),
    },
    mounted:function() {
    }
  }
</script>

<style scoped>

</style>
