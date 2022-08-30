<template>
  <div>
    <div v-for="(recommend,index) in recommendList" :key="recommend.id">
    <div  :class="index % 2===1 ? 'bg000' : 'bgfff'" >
      <div class="container-types container-rank clearfix" >
        <h3 class="types-title clearfix">
          <span>{{recommend.name}}</span>
          <router-link class="more"  :to="{path:recommend.link}">查看更多 <i class="imv2-right2"></i></router-link>
        </h3>

        <div class="clearfix types-content js-rank-content">
          <div class="index-card-container course-card-container container" v-for="detail in recommend.detailVOList" :key="detail.id">
            <div v-if="detail.sellType == '1'" class="course-stat hot">
              点播
            </div>
            <div v-if="detail.sellType == '2'" class="course-stat new">
              直播
            </div>
            <div v-if="detail.sellType == '3'" class="course-stat upgrade">
              班级
            </div>
            <router-link target="_blank" class="course-card" :to="{path:'/course/courseInfo/'+detail.busId}" data-track="xshk-1-1">
              <div class="course-card-top hashadow">
                <img class="course-banner" v-lazy="detail.logo" onerror="this.src='/static/img/course-default.png'">
                <div class="course-label">
                  <!--<label v-if="detail.sellType == '1'">点播</label>
                  <label v-if="detail.sellType == '2'">直播</label>
                  <label v-if="detail.sellType == '3'">班级</label>-->
                </div>
              </div>
              <div class="course-card-content">
                <h3 class="course-card-name">{{detail.courseName}}</h3>
                <div class="clearfix course-card-bottom">
                  <div class="course-card-info">
                    <span v-if="detail.bogusBuycount == 0">{{detail.pageBuycount}}人学习</span>
                    <span v-if="detail.bogusBuycount != 0">{{detail.bogusBuycount+detail.pageBuycount}}人学习</span>
                    <span v-if="detail.bogusViewcount == 0" class="r js-hover-evaluation">{{detail.pageViewcount}}人浏览</span>
                    <span v-if="detail.bogusViewcount != 0" class="r js-hover-evaluation">{{detail.bogusViewcount+detail.pageViewcount}}人浏览</span>
                  </div>
                  <div class="course-card-price">
                    <div class="price-box">
                      <span class="price red fs18" v-if="detail.currentPrice == '0.00'" >免费</span>
                      <span class="price red fs18" v-if="detail.currentPrice != '0.00'" >￥{{detail.currentPrice}}</span>
                      <span class="cost-price" v-if="detail.sourcePrice != '0.00'" >￥{{detail.sourcePrice}}</span>

                      <!--<div class="collect clearfix js-course-collect"  title="收藏"><i
                        class="imv2-star"></i><span>收藏</span></div>-->
                    </div>
                  </div>
                </div>
              </div>
            </router-link>
          </div>
        </div>
        <div class="clear"></div>
      </div>
    </div>
  </div>
  </div>
</template>

<script>
  import {recommendCategory} from "@/api";
  import { setStore, getStore } from '@/util/store'

  export default {
    name: 'hotCourse',
    data() {
      return {
        recommendList:getStore({ name: 'recommendList' }) || [],
      }
    },
    components: {},
    methods: {
      init(){
        if(this.recommendList.length == 0){
          recommendCategory("1").then(res => {
            this.recommendList = res.data.data;
            setStore({ name: 'recommendList', content: res.data.data,type: 'session' })
          })
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
