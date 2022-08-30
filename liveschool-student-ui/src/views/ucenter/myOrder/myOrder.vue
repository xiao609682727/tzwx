<template>
  <div class="u-container">
    <div class="right-container l">
      <div class="right-title">
        <h2>我的订单</h2>
        <ul>
          <li  v-bind:class="{'action' :undefined == states}">
            <router-link :to="{path:'/uc/myOrder'}">全部
            </router-link>
          </li>
          <li v-bind:class="{'action' :'1' == states}">
            <router-link :to="{path:'/uc/myOrder',query:{states:1}}">未支付</router-link>
          </li>
          <li v-bind:class="{'action' :'2' == states}">
            <router-link :to="{path:'/uc/myOrder',query:{states:2}}">已完成</router-link>
          </li>
          <li v-bind:class="{'action' :'3' == states}">
            <router-link :to="{path:'/uc/myOrder',query:{states:3}}">已失效</router-link>
          </li>
        </ul>

       <!-- <span>
          <a href="/myorder?type=del">
            <i class="imv2-delete"></i>
            订单回收站
          </a>
        </span>-->
        <!--<div class="mygroup">
          <a href="javascript:void(0);" class="js-show-group">
            <i class="imv2-assignment_ind"></i>
            我的拼团
          </a>
          <div id="show-group">
            <div class="show-group-wrap">
              <p>使用手机扫码查看拼团订单，或使用
                <br>
                慕课网APP-我的拼团中查看。
              </p>
              <div class="show-group">
                <div class="loading"></div>
              </div>
            </div>
          </div>
        </div>-->
      </div>
      <div class="myOrder">


        <ul class="myOrder-list"  v-if="orderList.length > 0">
          <li data-flag="2005202106522488" v-for="order in orderList" :key="order.id">
            <p class="myOrder-number">
              <i class="imv2-receipt"></i>
              订单编号：{{order.orderNo}}
              <span class="date">{{order.createTime}}</span>
              <i class="imv2-delete js-order-del" title="删除订单"></i>
<!--              <a href="javascript:void(0)" target="_blank" class="myfeedback r">我要反馈</a>-->
            </p>

            <div class="myOrder-course clearfix" v-for="detail in order.detailVOList" :key="detail.id">
              <dl class="course-del l">
                <dd class="clearfix">
                  <router-link v-if="detail.trxorderType=='LINECLASS'" target="_blank" :to="{path:'/lineDownCourse/lineInfo/'+detail.courseId}" class="l">
                    <img class="l" v-lazy="detail.logo" width="160" height="90">
                  </router-link>
                  <router-link v-if="detail.trxorderType!='LINECLASS'" target="_blank" :to="{path:'/course/courseInfo/'+detail.courseId}" class="l">
                    <img class="l" v-lazy="detail.logo" width="160" height="90">
                  </router-link>
                  <div class="del-box l">
                    <!-- type为类型 1实战购买 2实战续费 4就业班购买 5就业班续费 -->
                    <router-link v-if="detail.trxorderType=='LINECLASS'" :to="{path:'/lineDownCourse/lineInfo/'+detail.courseId}">
                      <p class="course-name">{{detail.courseName}}</p>
                    </router-link>
                    <router-link v-if="detail.trxorderType!='LINECLASS'" :to="{path:'/course/courseInfo/'+detail.courseId}">
                      <p class="course-name">{{detail.courseName}}</p>
                    </router-link>

                    <p class="price-btn-box clearfix">
                      <!-- 如果有优惠券 -->
                      <!--<span class="l truepay-text">实付</span>-->
                      <span class="l course-little-price">￥{{detail.currentPrice}}</span>

                    </p>
                  </div>
                  <div class="fenxiao_refund">
                  </div>
                </dd>
              </dl>

              <!-- 使用优惠券 -->
              <div class="course-money l pt15">
                <!--<div class="type-box clearfix mb10">
                  <p class="type-text l">原价</p>
                  <p class="type-price l line-though">
                    <span class="RMB">¥</span>
                    69.00
                  </p>
                </div>
                <div class="type-box clearfix mb10">
                  <p class="type-text l">折扣</p>
                  <p class="type-price l">
                    -
                    <span class="RMB">¥</span>
                    60.20
                  </p>
                </div>-->
                <div class="total-box clearfix">
<!--                  <p class="type-text l">实付</p>-->
                  <p class="type-price l">
                    <span class="RMB">¥</span>
                    {{detail.currentPrice}}
                  </p>
                </div>
              </div>
              <div class="course-action l">
                <div  v-if="detail.authStatus == '1'">
                  <a @click="toPay(detail.trxorderId)" class="pay-now" href="javascript:void(0)">立即支付</a>
                  <a @click="cancelOrder(detail)" class="order-cancel" href="javascript:void(0);">取消订单</a>
                </div>
                <p class="order-close" v-if="detail.authStatus == '2'">
                  支付成功
                </p>
                <p class="order-close" v-if="detail.authStatus == '3'">
                  退款
                </p>
                <p class="order-close" v-if="detail.authStatus == '4'">
                  关闭
                </p>
              </div>
            </div>
          </li>
        </ul  >
        <!-- 是否有分页 -->
        <page-component :page="page" @goto="goto" v-show="orderList.length > 0" ></page-component>
        <!-- 是否有分页 end -->
        <div class="nodata" v-if="orderList.length == 0">
          <p><i class="imv2-error_c"></i></p>
          <p>你还没有课程订单</p>
        </div>
      </div>

    </div>
    <div class="clear"></div>
  </div>
</template>

<script>
  import { mapGetters } from "vuex";
  import {getOrderList,updateOrder} from "@/api/user";
  import pageComponent from '@/views/common/pageComponent'

  export default {
    name: 'myOrder',
    data() {
      return {
        orderList:[],
        states:this.$route.query.states,
        current:1,
        size:10,
        page:{},
      }
    },
    methods: {
      goto(i){
        this.current = i
        this.init()
      },
      init(){
        if (typeof(this.studentInfo.id) == "undefined"){
          return;
        }
        let param = {};
        if(undefined != this.states){
          param.states = this.states
        }
        param.orderType = "COURSE,LIVE,PACKAGE,LINECLASS"
        param.current = this.current
        param.size = this.size
        getOrderList(param).then(res =>{
          this.page = res.data.data
          this.orderList = res.data.data.records
        })
      },
      cancelOrder(detail){
        this.$layer.confirm(
          "确定要取消订单吗？",
          {
            title: "取消订单",
            btn: ["确定", "取消"]
          },
          layerid => {
            // this.$layer.msg("执行了删除");
            let params = {
              id:detail.trxorderId,
              states:3
            }
            let that = this
            updateOrder(params).then(res =>{
              that.init()
            })
            this.$layer.close(layerid);
          },
          layerid => {
            this.$layer.close(layerid);
          }
        )

      },
      toPay(orderId){
        this.$router.push({ path: '/course/payCenter/'+orderId })
      }
    },
    mounted:function() {
      this.init()
    },
    computed: {
      ...mapGetters(["studentInfo"]),
    },
    watch: {
      studentInfo: function (val, oldVal) {
        this.init()
      }
    },
    components: {
      pageComponent
    },
  }
</script>

<style scoped>

</style>
