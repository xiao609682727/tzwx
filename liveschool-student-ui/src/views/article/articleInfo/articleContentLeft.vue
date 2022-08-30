<template>
  <div class="left_essay" style="margin-right:20px" v-wechat-title="articleInfoTitle">
    <div class="part_essay js-part_essay ">
      <!--<div class="originalImg"></div>-->
      <!-- 面包屑 -->
      <div class="detail-path">
        <router-link :to="{path:'/article/list'}" target="_blank">资讯</router-link>
        /
        <router-link :to="{path:'/article/list?subjectId='+subject.id}" target="_blank" class="js-column">{{subject.name}}</router-link>
      </div>
      <!-- 面包屑end -->
      <div class="detail-title-wrap">
        <h1 class="detail-title">
          <span class="d-t js-title">{{article.title}}</span>
        </h1>
        <div class="dc-profile clearfix">
          <div class="l">
            <span class="spacer">{{article.createTime}}</span>
            <span class="spacer spacer-2">{{article.clickNum}}浏览</span>
          </div>
        </div>
      </div>
      <!-- 手记详情 -->
      <div class="detail-content-wrap">
        <div class="detail-content js-lookimg long-content">
          <div v-html="article.content">
          </div>
        </div>
        <!-- 标签 -->
        <div class="cat-box">
          <div class="original-warp">
            <p class="original">转载请注明出处，谢谢合作</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {articleInfo} from "@/api";
  import Store from "../../../store";

  export default {
    name: 'articleRight',
    data() {
      return {
        articleInfoTitle: "",
        article: {},
        subject: {}
      }
    }, components: {

    },
    methods: {
      init(){
        let id = this.$route.params.id;
        articleInfo(id).then(res =>{
          this.article = res.data.data
          //把资讯名字动态赋值给title
          let title = "";
          if (Store.getters.website.title) {
            title = res.data.data.title+"-"+Store.getters.website.title;
          }
          this.articleInfoTitle = title;
          this.subject.id = this.article.subjectId;
          this.subject.name = this.article.subjectName;
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
