<template>
  <div>
    <!-- 头部开始 -->
    <common-header></common-header>
    <!-- 头部结束 -->
    <!-- 主干开始 -->
    <div class="body-main">
      <div class="cart-header">
        <div class="cart-header-warp clearfix">
          <div class="cart-title left clearfix">
            <h1 class="left">订单确认</h1>
          </div>
          <!-- 购物车广告位 -->
          <div id="orderCartBanner"></div>
          <div class="right">
          </div>
        </div>
      </div>
      <div class="cart-body" id="cartBody">
        <div class="title-box clearfix">
          <p class="l goods-info-title">商品信息</p>
        </div>
        <div class="detail-box">
          <ul>

            <li class="clearfix js-item-cart js-each-85" data-type="4" data-typeid="85" data-goodsid="1996">
              <!-- 判断类型 -->
              <!-- type为类型 1实战购买 2实战续费 4就业班购买 5就业班续费-->
              <a href="javascript:void(0)" target="_blank"><img :src="courseInfo.logo" alt="" class="l"></a>
              <div class="text-info-box l">
                <!-- type为类型 1实战购买 2实战续费 4就业班购买 5就业班续费-->
                <!-- 如果是就业班组合 -->
                <p class="package-title"></p>
                <router-link target="_blank" :to="{path:'/course/courseInfo/'+courseInfo.id}" ><p class="package-info-title">{{courseInfo.courseName}}</p></router-link>
                <!-- end -->
                <!-- type为类型 1实战购买 2实战续费 4就业班购买 5就业班续费-->
                <!-- 如果是组合 -->
                <p class="info-del" v-html="courseInfo.title"></p>
              </div>
              <div class="info-price l">
                <em>￥</em><span class="price">{{courseInfo.currentPrice}}</span>
              </div>
            </li>
          </ul>
        </div>
        <div id="Anchor"></div>
        <div class="pay-box clearfix js-total-body-bot fixed">
          <div class="row-bottom">
            <div class="row">
              <div class="goods-total-price-box clearfix">
                <p class="r rw price-num">
                  <em>￥</em><span id="js-actual-price">{{courseInfo.currentPrice}}</span>
                </p>
                <p class="r price-text"><span class="js-total-hide">共<span>1</span>件商品，</span>商品总金额：</p>
              </div>
            </div>





            <!-- 优惠券/优惠码抵扣 -->
            <div class="coupons-discount-box clearfix" style="display:none;">
              <p class="r rw price-num">-<em>￥</em><span id="js-coupon-lose-money">0.00</span></p>
              <p class="r price-text">优惠券/优惠码抵扣：</p>
            </div>
            <!-- 优惠券抵扣end -->




            <!-- 组合套餐 or 学生优惠 -->
            <div class="package-discount-box clearfix">
              <p class="r rw price-num">-<em>￥</em><span id="js-discount-lose-money">1389.00</span></p>
              <p class="r price-text">优惠折扣：</p>
            </div>
            <!-- 组合套餐 or 学生优惠end -->

            <div class="pay-price-box clearfix">
              <p class="r rw price">
                <em>￥</em><span id="js-pay-price">{{courseInfo.currentPrice}}</span>
              </p>
              <p class="r price-text">应付：</p>
            </div>
            <a @click="createOrder()" href="javascript:void(0);" class="r moco-btn moco-btn-red submit-btn js-pay-submit" data-typecate="normal">提交订单</a>
          </div>
        </div>
      </div>
    </div>
    <!-- 主干结束 -->
  </div>
</template>

<script>
  import commonHeader from '@/views/common/header'
  import {courseInfo} from "@/api";
  import {createCourseOrder} from "@/api/user";
  import { mapGetters } from "vuex";

  export default {
    name: 'confirmOrder',
    data() {
      return {
        courseInfo:{}
      }
    },
    methods: {
      init(){
        let id = this.$route.params.id;
        courseInfo(id).then(res =>{
          this.courseInfo = res.data.data
        })
      },
      createOrder(){
        let courseId = this.courseInfo.id;
        let studentId = this.studentInfo.id
        let param = {
          courseId: courseId,
          payType: "",
          reqChannel:"web"
        }
        createCourseOrder(param).then(res =>{
          this.order = res.data.data
          this.$router.push({ path: '/course/payCenter/'+this.order.id })
        })
      }
    },
    mounted:function() {
      this.init()
    },
    computed: {
      ...mapGetters(["studentInfo"]),
    },components:{
      commonHeader
    }
  }
</script>

<style scoped>
  .detail-box ul li .text-info-box{
    height:auto;
  }
</style>
