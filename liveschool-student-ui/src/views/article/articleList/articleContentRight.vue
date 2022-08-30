<template>
  <div class="articleright bg-fff">
    <div class="articleright_title">
      热门资讯
    </div>
    <div >
      <div class="articleright_right_tablist" v-for="(article,i) in articleList" :key="article.id">

        <div class="articleright_right_tab_Id">
          <span class="iconfont articleright_right_tab_Idiconfont" style="">{{i+1}}</span>
        </div>
        <div class="articleright_right_tab_content">
          <router-link :to="{path:'/article/info/'+article.id}" target="_blank" class="title">
            <div class="articleright_right_tab_content_title">{{article.title}}</div>
          </router-link>
            <div class="articleright_right_tab_content_describe">
              <span>{{article.clickNum}}浏览</span>
              <span>{{article.createTime}}</span>
            </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {articleHotList} from "@/api";
 /* import pageComponent from '@/views/common/pageComponent'*/

  export default {
    name: 'articleContentRight',
    data() {
      return {
        articleList:[],
        page:{}
      }
    },
    components: {
      /*pageComponent*/
    },
    methods: {
      getArticleList(current,size){
        articleHotList(current,size,this.subjectId).then(res =>{
          this.page = res.data.data
          this.articleList = res.data.data.records
        })
      },
    },
    mounted:function() {
      this.getArticleList()
    },
  }
</script>

<style scoped>
.articleright{
  width: 265px;
  padding: 20px;
  height: 100%;
  border-radius: 12px;
}
.articleright_title{
  font-size: 20px;
  line-height: 20px;
  font-weight: 600;
  color: #2a2a2a;
  height: 47px;
  margin-bottom: 40px;
  border-bottom: 1px solid rgba(218,222,224,.5)
}
.articleright_right_tablist:hover .articleright_right_tab_content .articleright_right_tab_content_title{
  color: #0091ff;
}
.articleright_right_tablist{
  display: -webkit-flex;
  display: flex;
  -webkit-justify-content: flex-start;
  justify-content: flex-start;
  margin-bottom: 30px;
  cursor: pointer;
}
.articleright_right_tab_content{width: 240px;}
.articleright_right_tab_content_title{
  font-size: 16px;
  font-weight: 400;
  color: #2a2a2a;
  line-height: 24px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
.articleright_right_tab_content_describe{
  margin-top: 10px;
  height: 18px;
  font-size: 12px;
  font-weight: 400;
  color: #93999f;
  line-height: 18px;
}
.articleright_right_tab_content_describe>span:first-child{
  display: inline-block;
  width: 80px;
  margin-right: 16px;
}

.articleright_right_tab_Id{
  width:45px;
  text-align: center;
  margin-right: 10px;
}


</style>
