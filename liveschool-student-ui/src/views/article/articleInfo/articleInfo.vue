<template>
    <div class="main_con clearfix">
      <div class="articlecontainer">
        <article-content-left></article-content-left>
        <article-content-right></article-content-right>
      </div>
    </div>
</template>

<script>
import articleContentRight from '@/views/article/articleList/articleContentRight'
  import articleContentLeft from '@/views/article/articleInfo/articleContentLeft'
  export default {
    name: 'articleInfo',
    data () {
      return {
        subjectIdLevel1:"",
        subjectIdLevel2:"",
        type:""
      }
    },components:{
      articleContentLeft,articleContentRight
    },
    methods: {
    }
  }
</script>

<style scoped>


</style>
