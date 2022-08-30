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


      <div class="cart-body">
        <div class="cart-body-title">
          <div class="clearfix">
            <div class="left trade-title">
              <span class="order">订单：</span><span class="num">{{order.orderNo}}</span>
            </div>
          </div>
          <ul class="js-itemUl item-ul" style="height: 150px;">
            <li class="item clearfix" data-cid="$info.type_id" v-for="detail in order.detailVOList" :key="detail.id">
              <!-- type为类型 1实战购买 2实战续费 4就业班购买 5就业班续费 30为付费手记 8慕课小课-->
              <div class="item-left left clearfix">
                <router-link v-if="detail.trxorderType=='LINECLASS'" target="_blank" :to="{path:'/lineDownCourse/lineInfo/'+detail.courseId}"><img v-lazy="detail.logo" onerror="this.src='/static/img/course-default.png'" width="160" height="90" class="left"></router-link>
                <router-link v-if="detail.trxorderType!='LINECLASS'" target="_blank" :to="{path:'/course/courseInfo/'+detail.courseId}"><img v-lazy="detail.logo" onerror="this.src='/static/img/course-default.png'" width="160" height="90" class="left"></router-link>
                <dl class="left">
                  <!-- type为类型 1实战购买 2实战续费 4就业班购买 5就业班续费-->
                  <router-link v-if="detail.trxorderType=='LINECLASS'" :to="{path:'/lineDownCourse/lineInfo/'+detail.courseId}" target="_blank">
                    <dt>{{detail.courseName}}</dt>
                  </router-link>
                  <router-link v-if="detail.trxorderType!='LINECLASS'" :to="{path:'/course/courseInfo/'+detail.courseId}" target="_blank">
                    <dt>{{detail.courseName}}</dt>
                  </router-link>
                  <dd v-html="detail.description">
                  </dd>
                </dl>
              </div>

              <div class="item-right right">
                <div class="price clearfix">
                  <span class="price-text">实付金额：</span>
                  <em>￥</em>
                  <span>{{detail.currentPrice}}</span>
                </div>
              </div>
            </li>
          </ul>
        </div>
        <div class="pay-method">
          <h2 class="pay-method-title">支付方式</h2>
          <ul class="clearfix js-pay-method">
            <li v-if="website.aliFlag == '1'" @click="clickPay(1)" class="pay-way alipay " :class="{'active':payType == 1}" data-type="1" data-account="2">
              <div class="bottomright">
                <div class="triangle"></div>
                <i class="imv2-check"></i>
              </div>
            </li>
            <li v-if="website.wechatFlag == '1'" @click="clickPay(2)" class="pay-way wxpay" :class="{'active':payType == 2}" data-type="2">
              <div class="bottomright">
                <div class="triangle"></div>
                <i class="imv2-check"></i>
              </div>
            </li>
            <li @click="clickPay(4)" class="pay-way yue" :class="{'active':payType == 4}" data-type="4">
              <div class="leftImg"></div>
              <div class="rightInfo">
                <h2>我的学币</h2>
                <p>￥<span>{{userAccount.balance}}</span></p>
              </div>
              <div class="bottomright">
                <div class="triangle"></div>
                <i class="imv2-check"></i>
              </div>
            </li>
          </ul>
        </div>
        <div class="pay-summary clearfix huabei-active">
          <div class="warning">
            <i class="imv2-error_c"></i>
            <span class="js-countdown-time"><!--已为你锁定课程席位，请在 <b>13:58</b> 内完成支付即可   -->请完成支付</span>
          </div>
          <div class="summary">
            <div class="clearfix total">
              <span class="label">应付金额：</span>
              <span class="price">
					<em>￥</em>
					<span>{{order.orderAmount}}</span>
				</span>
            </div>
            <div class="submit-warp clearfix">
              <input type="hidden" class="zg_hascoupon" value="0">
              <span @click="toPay()" data-ordernum="2006071610321171" class="pay-summary-submit js-pay-submit">立即支付</span>
            </div>

            <div class="clearfix feedback-href">
            </div>

          </div>
        </div>
      </div>

    </div>
    <!-- 主干结束 -->

    <div class="moco-modal-overlay" style="z-index: 99999; opacity: 1;" v-if="payTC">
      <div class="moco-modal-layer" style="z-index: 901; visibility: visible; left: 512px; top: 260px;">
        <div class="moco-modal-title">
          <div>
            <span>余额支付</span>
            <a href="javascript:void(0)" class="moco-icon-close moco-modal-close js-modal-close"></a>
          </div>
        </div>
        <div class="moco-modal-inner">
          <div class="moco-modal-dialog" style="display: block;">
            <div class="pay-balance-modal">
              <div class="modal">
                <p>剩余需支付金额</p>
                <ul class="rechargeMoney js-recharge-money">
                  <li class="active" data-money="3998.00">
                    <h2>￥3998.00</h2>
                    <div class="bottomright">
                      <div class="triangle"></div>
                      <i class="imv2-check"></i>
                    </div>
                  </li>
                </ul>
                <p>支付方式</p>
                <ul class="rechargeType js-recharge-type">
                  <li class="active alipay" data-type="1">
                    <div class="bottomright">
                      <div class="triangle"></div>
                      <i class="imv2-check"></i>
                    </div>
                  </li>
                  <li class="wxpay" data-type="2">
                    <div class="bottomright">
                      <div class="triangle"></div>
                      <i class="imv2-check"></i>
                    </div>
                  </li>
                </ul>
                <button class="js-rechare-balance" data-type="1">立即支付</button>
                <p class="tip">点击立即支付则视为您已同意
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="moco-modal-blackout js-moco-modal-cancel" style="z-index: 900; opacity: 0.7; width: 100%; height: 100%;" v-if="payTC"></div>

    <div class="moco-modal-overlay" style="z-index: 99999; opacity: 1;background: rgba(7,17,27,0.6);" v-if="waitpay">
      <div class="moco-modal-layer" style="z-index: 904; visibility: visible; left: 344px; top: 209.5px;">
        <div class="moco-modal-inner">
          <div class="moco-modal-dialog" style="display: block;margin: 0px;padding: 0px;">
            <div class="pay-tip-modal">
              <div class="modal">
                <div class="wait-img">
                  <span class="imv2-load_o"></span>
                </div>
                <h3>等待您完成支付...</h3>
                <p class="js-error " :class="{'error':orderCss}">{{orderMsg}}</p>
                <div class="clearfix btnwarp">
                  <div class="success-btn js-success-btn" data-ispay="1" @click="checko()">支付完成</div>
                  <div class="restart-btn js-restart-btn" @click="waitpay = false">
                    <span class="imv2-refresh"></span>
                    重新选择支付方式
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>


    <div class="moco-modal-overlay" style="z-index: 99999; opacity: 1;background: rgba(7,17,27,0.6);" v-if="wechatPay">
      <div class="moco-modal-layer" style="z-index: 901; visibility: visible; left: 416px; top: 293px; width: 320px; height: 400px;">
        <div class="moco-modal-inner">
          <div class="" style="display: block;">
            <div class="wechat-modal js-wechat-modal">
              <div class="modal">
                <h1 class="price">微信支付￥<span>{{order.orderAmount}}</span></h1>
                <div class="qrcode js-qrcode-img">
                  <qrcode :value="wechatPayUrl" :options="{ width: 180 }"></qrcode>
                </div>
                <p>使用微信扫描二维码进行支付</p>
                <div class="restart-btn js-restart-btn" @click="closeWechat()">
                  <span class="imv2-refresh"></span>
                  重新选择支付方式</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>


    <div class="moco-modal-overlay" style="z-index: 99999; opacity: 1;background: rgba(7,17,27,0.6);" v-if="payOk" >
      <div class="moco-modal-layer" style="z-index: 904; visibility: visible; left: 344px; top: 209.5px;">
        <div class="moco-modal-inner">
          <div class="moco-modal-dialog" style="display: block;margin: 0px;padding: 0px;">
            <div class="pay-tip-modal">
              <div class="modal">
                <span class="icon-send-success">
                  <i class="imv2-check"></i>
                </span>
                <h3>恭喜您支付成功</h3>
                <p class="js-error " :class="{'error':orderCss}">点击下方按钮跳转到课程列表页面</p>
                <div class="clearfix btnwarp">
                  <div class="success-btn js-success-btn" data-ispay="1" @click="goCourseList()">支付完成</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>


  </div>
</template>

<script>
  import commonHeader from '@/views/common/header'
  import { mapGetters } from "vuex";
  import {courseOrderDetail,getUserAccount,updateOrder,accountPay,checkOrder,getwechatPay} from "@/api/user";


  export default {
    name: 'confirmOrder',
    data() {
      return {
        payOk:false,
        order:{},
        payTC:false,
        waitpay:false,
        wechatPay:false,
        payType:1,
        wechatPayUrl:"",
        userAccount:{},
        orderMsg:"请在支付页面继续完成支付",
        orderCss:false,
        timeInterval:{}
      }
    },
    methods: {
      clickPay(type){
        this.payType = type
      },
      init(){
        let id = this.$route.params.id;
        courseOrderDetail({id:id}).then(res =>{
          this.order = res.data.data
        })
        let userId = this.studentInfo.id;
        if (typeof(userId) != "undefined"){
          getUserAccount().then(res =>{
            let accountList = res.data.data.records
            if(accountList.length >0){
              this.userAccount = accountList[0];
            }
          })
        }
        //获取微信支付二维码信息
        getwechatPay({orderId:id}).then(res=>{
          if(res.data.code == 200){
            this.wechatPayUrl = res.data.data
          }
        })
      },
      goCourseList(){
        this.$router.push({ path: '/uc/myCourse',query:{courseType:''}})
      },
      closeWechat(){
        this.wechatPay = false
        clearInterval(this.timeInterval)
      },
      toPay(){
        //支付宝支付
        if(this.payType == 1){
          let params = {
            id:this.order.id,
            payType:1
          }
          let that = this
          window.open(this.baseUrl + "/front/aliPay/pcPay?orderId="+that.order.id);
          /*updateOrder(params).then(res =>{
            this.waitpay = true
            window.open(this.baseUrl + "/front/aliPay/pcPay?orderId="+that.order.id);
          })*/
        }
        //微信支付
        if(this.payType == 2){
          let params = {
            id:this.order.id,
            payType:2
          }
          let that = this
          // updateOrder(params).then(res =>{
            this.wechatPay = true
            that.timeInterval = setInterval(function () {
              let params = {
                orderId:that.order.id
              }
              checkOrder(params).then(res=>{
                that.wechatPay = false;
                that.payOk = true
                clearInterval(that.timeInterval)
                // alert("支付成功")
                // this.$router.push({ path: '/uc/myCourse'})
              },error =>{
                console.log("未支付成功，请在支付页面继续完成支付！")
              })
            },1000)

          // })
        }
        //储值支付
        if(this.payType == 4){
          let params = {
            orderId:this.order.id,
          }
          let that = this
          accountPay(params).then(res =>{
            that.payOk = true
            // this.$router.push({ path: '/uc/myCourse'})
          },error =>{
            this.$layer.alert(
              error.message,
              {
                shade: true,
                title: "提示"
              },
              laeryid => {
                this.$layer.close(laeryid);
              }
            );
          })
        }
      },
      checko(){
        let params = {
          orderId:this.order.id
        }
        let that = this
        checkOrder(params).then(res=>{
          that.waitpay = false;
          that.payOk = true
          // alert("支付成功")
          // this.$router.push({ path: '/uc/myCourse'})
        },error =>{
          this.orderCss = true
          this.orderMsg = "未支付成功，请在支付页面继续完成支付！"
        })
      },
    },
    mounted:function() {
      this.init()
    },
    watch: {
      studentInfo: function (val, oldVal) {
        this.init()
      }
    },
    computed: {
      ...mapGetters(["studentInfo","website"]),
    },components:{
      commonHeader
    }
  }
</script>

<style scoped>
  .wechat-modal {
    width: 320px;
    background-color: #fff;
    text-align: center;
    padding: 36px 0 32px;
    border-radius: 12px;
  }
  .wechat-modal .modal {
    position: relative;
  }
  .wechat-modal .price {
    font-size: 16px;
    color: #545c63;
    line-height: 24px;
    font-weight: 700;
  }
  .wechat-modal .qrcode {
    width: 180px;
    height: 180px;
    margin: 12px auto;
  }
  .wechat-modal p {
    font-size: 12px;
    color: #545c63;
    line-height: 18px;
  }
  .wechat-modal .restart-btn {
    font-size: 14px;
    color: #9199a1;
    line-height: 24px;
    margin-top: 24px;
    cursor: pointer;
  }
</style>
