<template>
  <div>
    <div class="i-slide banner-slide">
      <section>
        <div class="swiper-container">
          <div class="swiper-wrapper">
            <div class="swiper-slide" v-for="banner in bannerList" :key="banner.id">
              <a v-if="banner.linkAddress!= ''" :href="banner.linkAddress" target="_blank">
              <img class="imgload" :src="banner.imageUrl" alt="banner" :title="banner.title">
              </a>
              <a v-if="banner.linkAddress== ''" href="javascript:void(0);" >
                <img class="imgload" :src="banner.imageUrl" alt="banner" :title="banner.title">
              </a>
            </div>
          </div>
        </div>
        <!-- 如果需要分页器 -->
        <div class="swiper-pagination "></div>
        <!-- 如果需要导航按钮 -->
        <div class="swiper-button-prev"></div>
        <div class="swiper-button-next"></div>
      </section>
    </div>
  </div>
</template>

<script>
  // import $ from 'jquery'
  import { setStore, getStore } from '@/util/store'
  import Swiper from 'swiper';
  import 'swiper/css/swiper.css';
  import {getBannerList} from "@/api";

  export default {
    name: 'banner',
    data() {
      return {
        bannerList:getStore({ name: 'bannerList' }) || [],
        isReg: false,
        name: '',
        password: '',
        repeat: ''
      }
    },mounted:function() {
      this.SwiperFun();
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
        new Swiper ('.swiper-container', {
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
          observer: true,
          observeParents: true,

          // 如果需要前进后退按钮
          navigation: {
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev',
          },
          // 如果需要滚动条
          scrollbar: {
            el: '.swiper-scrollbar',
          },
        });
      }
    }
  }
</script>

<style scoped lang="scss">
@import "../../../public/static/css/swiper/swiper.scss";


</style>