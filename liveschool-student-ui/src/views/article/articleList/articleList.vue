<template>
    <div class="article container clearfix">
      <article-banner></article-banner>
      <div class="articlecontainer">
        <article-subject-left @subjectData="clickSubjectId"></article-subject-left>
        <article-content-right ></article-content-right>
      </div>
    </div>
</template>

<script>
  import articleSubjectLeft from '@/views/article/articleList/articleContentLeft'
  import articleContentRight from '@/views/article/articleList/articleContentRight'
  import articleBanner from '@/views/article/articleList/articleBanner.vue'
  export default {
    name: 'articleindex',
    data () {
      return {
        subjectId:"0"
      }
    },components:{
      articleSubjectLeft,articleContentRight,articleBanner
    },
    methods: {
      clickSubjectId(subjectId){
        this.subjectId = subjectId+""
      }
    },

  }
</script>

<style scoped>

body{
  background-color: #f9f9f9;
}
</style>
