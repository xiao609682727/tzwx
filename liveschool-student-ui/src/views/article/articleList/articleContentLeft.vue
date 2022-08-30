<template>
  <div class="articleleft bg-fff">
      <div class="articleleft_tab">
        <div class="articleleft_title">
          最新资讯
        </div>
        <div class="articleleft_tablist">
          <div class="" v-bind:class="{'active' :0 == activeIndex}"><a href="javascript:void(0)" @click="clickSubject('')">全部</a></div>
          <div class="" v-bind:class="{'active' :subject.id == activeIndex}" v-for="(subject) in subjectList" :key="subject.id">
            <a href="javascript:void(0)" @click="clickSubject(subject.id)">{{subject.subjectName}}</a>
          </div>
        </div>
      </div>
      <div class="tab-content">
        <div class=article_card v-for="(article) in articleList" :key="article.id">

          <div class="article_card_left">
            <router-link :to="{path:'/article/info/'+article.id}" target="_blank" class="title"><img :src="article.imageUrl"></router-link>
          </div>
          <router-link :to="{path:'/article/info/'+article.id}" target="_blank" class="title">
          <div class="article_card_right">
            <div class="article_card_right_title">
              {{article.title}}
            </div>
            <div class="article_card_right_describe">
              {{article.summary}}
            </div>
            <div class="article_card_right_bottom">
              <span class="article_card_right_type"><i class="imv2-visibility"></i>{{article.clickNum}}</span>
              <span class="article_card_right_date fr">{{article.createTime}}</span>
            </div>

          </div>
          </router-link>
        </div>
        <page-component :page="page" @goto="goto" v-show="articleList.length != 0"></page-component>
        <div class="nodata" v-if="articleList.length == 0">
          <p><i class="imv2-error_c"></i></p>
          <p>暂无数据</p>
        </div>
      </div>
  </div>
</template>
<script>
import {articleList, cmsSubject} from "@/api";
  import { setStore, getStore } from '@/util/store'
 import pageComponent from '@/views/common/pageComponent'
  export default {
    name: 'articleSubjectLeft',
    data() {
      return {
        articleList:[],
        subjectId:0,
        subjectList:getStore({ name: 'subjectList' }) || [],
        activeIndex:'',
        page:{},
      }
    },
    components: {
      pageComponent
    },
    mounted:function() {
      this.init();
      let subjectId = this.$route.query.subjectId;

      if(typeof(subjectId) == "undefined"){
        subjectId = ""
      }
      this.clickSubject(subjectId)
    },
    methods: {
      init(){
        let subjectId = "";
        if(this.subjectList.length == 0){
          cmsSubject().then(res => {
            this.subjectList = res.data.data;
            setStore({ name: 'subjectList', content: res.data.data,type: 'session' })
          });
        }
        if(this.subjectList.length >0){
          subjectId = this.subjectList[0].id
          this.$emit('subjectData',"")
        }
      },
      clickSubject(subjectId){
        this.activeIndex = subjectId
        this.subjectId = subjectId;
        this.getArticleList(1,12);
      },
      getArticleList(current,size){
        articleList(current,size,this.subjectId).then(res =>{
          this.page = res.data.data
          this.articleList = res.data.data.records
        })
      },
      goto(i){
        this.getArticleList(i,12);
      },
    },
  }
</script>

<style scoped>
body, html {
  background-color: #fff;
}

</style>
