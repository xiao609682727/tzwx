<template>
  <div>
    <div class="bgfff">
    <div>
      <div class="container-types container-rank clearfix" >
        <h3 class="types-title clearfix">
          <span>热门资讯</span>
          <router-link target="_blank" class="more"  :to="{path:'/article/list'}">查看更多 <i class="imv2-right2"></i></router-link>
        </h3>
        <div class="clearfix js-rank-content">
          <section class="in-new-box clerfix">
            <div class="clearfix d2-in-news-warp mt30">
              <article class="d2-i-ns-left">
                <div class="d2-i-n-l-box of">
                  <ul class="clearfix">
                    <li v-for="article in articleListleft" :key="article.id">
                      <div class="inwarp">
                        <router-link class="pic-a-n" :to="{path:'/article/info/'+article.id}" target="_blank" :title="article.title" >
                          <img v-lazy="article.imageUrl" :alt="article.title">
                        </router-link>
                        <router-link class="name-a-n" :to="{path:'/article/info/'+article.id}" :title="article.title" >
                          <span class="txtOf">{{article.title}}</span>
                        </router-link>
                      </div>
                    </li>
                  </ul>
                </div>
              </article>
              <article class="d2-i-ns-right">
                <div class="d2-i-n-r-box">
                  <section class="d2-i-nr-body">
                    <ul class="d2-i-nr-b-list">
                      <li class="li-1" v-for="article in articleListright" :key="article.id">
                        <div class="li-1-in">
                          <em class="d2-b-l-ico">&nbsp;</em>
                          <router-link :to="{path:'/article/info/'+article.id}" :title="article.title" class="name f-fM c-666" onfocus="this.blur()">{{article.title}}</router-link>
                          <span class="time f-fM c-666">{{article.createTime}}</span>
                        </div>
                      </li>
                    </ul>
                  </section>
                </div>
              </article>
            </div>
          </section>
        </div>
        <div class="clear"></div>
      </div>
    </div>
  </div>
  </div>
</template>

<script>
  import {articleList} from "@/api";
  import { setStore, getStore } from '@/util/store'

  export default {
    name: 'hotArticle',
    data() {
      return {
        articleList:getStore({ name: 'articleList' }) || [],
        articleListleft:[],
        articleListright:[]
      }
    },
    components: {},
    methods: {
      init(){
        if(this.articleList.length == 0){
          articleList(1,12,"").then(res =>{
            this.articleList = res.data.data.records
            for (let i = 0; i < this.articleList.length; i++) {
              if(i<4){
                this.articleListleft.push(this.articleList[i])
              }else{
                this.articleListright.push(this.articleList[i])
              }
            }
            setStore({ name: 'articleList', content: this.articleList,type: 'session' })
          })
        }else{
          for (let i = 0; i < this.articleList.length; i++) {
            if(i<4){
              this.articleListleft.push(this.articleList[i])
            }else{
              this.articleListright.push(this.articleList[i])
            }
          }
        }

      }
    },
    mounted:function() {
      this.init();
    }
  }
</script>

<style scoped>

</style>
