<template>
  <div>
    <div class="i-slide banner-slide" >
      <section>
        <div class="swiper-container swiper-container-article">
          <div class="swiper-wrapper">
            <div class="swiper-slide" v-for="article in articleList" :key="article.id">
              <div class="swiperNews_slide">
                <div class="swiperNews_left">
                  <img :src="article.imageUrl">
                </div>
                <div class="swiperNews_right">
                  <div class="swiperNews_clas">
                    <strong>#行业动态</strong>
                    <span>#行业动态</span>
                  </div>
                  <div class="swiperNews_title">{{article.title}}</div>
                  <div class="swiperNews_desc">{{article.summary}}
                    <span>
                      <router-link :to="{path:'/article/info/'+article.id}" target="_blank" class="title">
                        【查看全部】
                      </router-link>
                    </span>
                  </div>
                  <div class="swiperNews_time">{{article.createTime}}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- 如果需要分页器 -->
        <div class="swiper-pagination "></div>
        <!-- 如果需要导航按钮 -->
      </section>
    </div>
  </div>
</template>

<script>
  // import $ from 'jquery'
  import { setStore, getStore } from '@/util/store'

  import Swiper from 'swiper';
  import 'swiper/css/swiper.css';
  import {articleList, getBannerList} from "@/api";

  export default {
    name: 'banner',
    data() {
      return {
        subjectId:'',
        bannerList:getStore({ name: 'bannerList' }) || [],
        isReg: false,
        articleList:[],
        name: '',
        password: '',
        repeat: ''
      }
    },mounted:function() {
      this.SwiperFun();
      this.getArticleList(1,3);
    }, components: {

    },
    methods: {
      SwiperFun () {
        if(this.bannerList.length == 0){
          getBannerList("1").then(res => {
            this.bannerList = res.data.data;
            setStore({ name: 'bannerList', content: res.data.data,type: 'session' })
          });
        }
        setTimeout(()=>{
          new Swiper ('.swiper-container-article', {
            // 如果需要分页器
            pagination: {
              el: '.swiper-pagination',
              clickable:true,
            },
            autoplay: {
              delay: 5000,
              disableOnInteraction: false, // 取消鼠标操作后的轮播暂停【无操作轮播继续】【参考链接1】
              stopOnLastSlide: false, // 在最后一页停止

            },
            loop: true,
            loopAdditionalSlides : 3,
            observer: true,
            observeParents: true,
            // 如果需要滚动条
            scrollbar: {
              el: '.swiper-scrollbar',
            },
          });
        },500)
      },
      getArticleList(current,size){
        articleList(current,size,this.subjectId).then(res =>{
          this.articleList = res.data.data.records
        })
      }
    }
  }
</script>

<style scoped lang="scss">
@import "../../../../public/static/css/swiper/swiper.scss";
.swiperNews_slide{
  display: -webkit-flex;
  display: flex;
}
.swiperNews_slide .swiperNews_left{
  width: 582px;
  height: 366px;
}
.swiperNews_slide .swiperNews_left img{
  width: 582px;
  height: 366px;
  border-top-left-radius: 20px;
  border-bottom-left-radius: 20px;
}
.swiperNews_slide .swiperNews_right{
  width: 618px;
  padding: 21px 34px 21px 50px;
  background-color: #fff;
}
.swiperNews_slide .swiperNews_right .swiperNews_clas{
  height: 28px;
  position: relative;
  overflow: hidden;
}
.swiperNews_slide .swiperNews_right .swiperNews_clas strong{
  position: absolute;
  top: 0;
  left: -2px;
  z-index: 2;
  height: 28px;
  padding: 0 18px;
  border-radius: 8px 0 8px 0;
  -webkit-transform: skewX(-20deg);
  transform: skewX(-20deg);
  line-height: 28px;
  text-align: center;
  background: #008aff;
  color: #008aff;
}
.swiperNews_slide .swiperNews_right .swiperNews_clas span{
  padding-left: 18px;
  position: absolute;
  top: 0;
  left: 0;
  z-index: 3;
  font-size: 12px;
  float: left;
  height: 28px;
  line-height: 28px;
  color: #fff;
  font-weight: 500;
  -webkit-transform: skewX(0deg);
  transform: skewX(0deg);
}
.swiperNews_slide .swiperNews_right .swiperNews_title {
  margin-top: 18px;
  font-size: 36px;
  line-height: 50px;
  height: 100px;
  font-weight: 500;
  color: #2a2a2a;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
.swiperNews_slide .swiperNews_right .swiperNews_desc {
  height: 60px;
  line-height: 30px;
  font-size: 16px;
  font-weight: 400;
  color: #707478;
  margin-top: 18px;
  width: 100%;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}
.swiperNews_slide .swiperNews_right .swiperNews_desc span{
   color: #008aff;
}
.swiperNews_slide .swiperNews_right .swiperNews_time{
  margin-top: 70px;
  font-size: 16px;
  line-height: 30px;
  font-weight: 400;
  color: #666;
}
.banner-slide{
  box-shadow: 0 2px 20px 0 rgba(4,9,76,.05);
  border-radius: 20px;
}

</style>
