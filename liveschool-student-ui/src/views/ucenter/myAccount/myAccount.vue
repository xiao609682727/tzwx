<template>
  <div class="u-container">
    <div class="right-container l">

      <div class="right-title">
        <h2>我的学币</h2>
      </div>
      <div class="balance-container">
        <div class="title-box">
          <div>
            <p class="title">当前学币</p>
            <button @click="chongzhi()" class="recharge-btn js-recharge-btn">立即充值</button>
          </div>
          <div>
            <p class="charge" style="left:0px">{{userAccount.balance}}</p>
            <p class="charge-tip">充值学币不可提现，仅限用于本平台课程消费，如有问题，请联系客服  {{website.phone}}</p>
          </div>
        </div>
        <div class="noData" v-if="userAccountHistory.length == 0">
          <span class="imv2-error_c"></span>
          <p>目前暂无任何收支记录</p>
        </div>
        <div class="list-box" v-if="userAccountHistory.length > 0">
          <table>
            <thead>
            <tr>
              <td width="154px">时间</td>
              <td width="94px">金额</td>
              <td width="74px">操作</td>
              <td width="94px">结余</td>
              <td>备注</td>
            </tr>
            </thead>
            <tbody>
            <tr v-for="history in userAccountHistory" :key="history.id">
              <td>{{history.createTime}}</td>
              <td>￥{{history.trxAmount}}</td>
              <td v-if="history.actHistoryType == '1'">转入</td>
              <td v-if="history.actHistoryType == '2'">消费</td>
              <td>￥{{history.balance}}</td>
              <td>{{history.description}}</td>
            </tr>
            </tbody>
          </table>
        </div>
        <page-component :page="page" @goto="goto" v-show="userAccountHistory.length > 0"></page-component>
      </div>
    </div>

    <div class="moco-modal-overlay" style="z-index: 901; opacity: 1;background: rgba(7,17,27,0.6);" v-if="payFlag">
      <div class="moco-modal-layer" style="z-index: 901; visibility: visible; left: 339px; top: 227.5px;">
        <div class="moco-modal-title">
          <div>
            <span>余额充值</span>
            <a @click="payFlag = false" href="javascript:void(0)" class="moco-icon-close moco-modal-close js-modal-close"></a>
          </div>
        </div>
        <div class="moco-modal-inner">
          <div class="moco-modal-dialog" style="display: block;">
            <div class="pay-balance-modal">
              <div class="modal">
                <p>充值金额
                  <!--<span>不可用于ios客户端</span>-->
                </p>
                <ul class="rechargeMoney js-recharge-money">
                  <li @click="clickMoney(1)" data-money="1" :class="{'active' :moneyType == 1}" >
                    <h2>￥300</h2>
                    <div class="bottomright">
                      <div class="triangle"></div>
                      <i class="imv2-check"></i>
                    </div>
                  </li>
                  <li data-money="2" @click="clickMoney(2)" :class="{'active' :moneyType == 2}">
                    <h2>￥500</h2>
                    <div class="bottomright">
                      <div class="triangle"></div>
                      <i class="imv2-check"></i>
                    </div>
                  </li>
                  <li data-money="3" @click="clickMoney(3)" :class="{'active' :moneyType == 3}">
                    <h2>￥1000</h2>
                    <div class="bottomright">
                      <div class="triangle"></div>
                      <i class="imv2-check"></i>
                    </div>
                  </li>
                  <br>
                  <li data-money="4" class="js-custom-recharge custom-recharge" :class="{'active' :moneyType == 4}">
                    <span class="money-icon">￥</span>
                    <input @click="clickMoney(4)"  @keydown="clickMoney(4)" v-model="money" type="text" maxlength="5" placeholder="其他金额，请输入1-50000的整数" class="js-custom-input" autocomplete="off" >
                    <div class="bottomright">
                      <div class="triangle"></div>
                      <i class="imv2-check"></i>
                    </div>
                  </li>
                </ul>
                <p>支付方式</p>
                <ul class="rechargeType js-recharge-type">
                  <li v-if="website.aliFlag == '1'" @click="clickPayType(1)" class="alipay" :class="{'active' :payType == 1}" data-type="1">
                    <div class="bottomright">
                      <div class="triangle"></div>
                      <i class="imv2-check"></i>
                    </div>
                  </li>
                  <li v-if="website.wechatFlag == '1'" @click="clickPayType(2)" class="wxpay" :class="{'active' :payType == 2}">
                    <div class="bottomright">
                      <div class="triangle"></div>
                      <i class="imv2-check"></i>
                    </div>
                  </li>
                </ul>
                <button @click="topay()" class="js-rechare-balance">立即充值</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

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


  </div>
</template>

<script>
  import { mapGetters } from "vuex";
  import {getUserAccount,getUserAccountHistory,createOrder,checkOrder,getwechatPay} from "@/api/user";
  import pageComponent from '@/views/common/pageComponent'

  export default {
    name: 'myAccount',
    data() {
      return {
        userAccount:{},
        userAccountHistory:[],
        moneyType:1,
        payType:0,
        money:"",
        payFlag:false,
        wechatPay:false,
        waitpay:false,
        order:{},
        orderCss:false,
        orderMsg:"请在支付页面继续完成支付",
        current:1,
        size:10,
        page:{},
        timeInterval:{},
        wechatPayUrl:"",

      }
    },
    methods: {
      goto(i){
        this.current = i
        let param= {
          "current":this.current,
          "size":this.size,
        }
        getUserAccountHistory(param).then(res =>{
          this.page = res.data.data
          this.userAccountHistory = res.data.data.records
        })
      },
      chongzhi(){
        this.money = ""
        this.moneyType = 1

        //设置支付默认
        if(this.website.aliFlag == '1') {
          this.payType = 1
        }else{
          if(this.website.wechatFlag == '1'){
            this.payType = 2
          }
        }
        this.payFlag = true;

      },
      closeWechat(){
        this.wechatPay = false
        clearInterval(this.timeInterval)
      },
      topay(){
        let money = 0
        switch (this.moneyType) {
          case 1:
            money = 300;
            break
          case 2:
            money = 500;
            break
          case 3:
            money = 1000;
            break
          case 4:
            money = this.money;
            break
        }
        let that = this
        //支付宝支付
        if(this.payType == 1){
          let param = {
            orderType:"ACCOUNT",
            money:money,
            payType: this.payType,
            reqChannel:"web"
          }
          createOrder(param).then(res =>{
            this.order = res.data.data
            this.waitpay = true
            window.open(this.baseUrl + "/front/aliPay/pcPay?orderId="+res.data.data.id);
          })
        }
        //微信支付
        if(this.payType == 2){
          let param = {
            orderType:"ACCOUNT",
            money:money,
            payType: this.payType,
            reqChannel:"web"
          }
          createOrder(param).then(res =>{
            this.order = res.data.data
            //获取微信支付二维码信息
            getwechatPay({orderId:this.order.id}).then(res=>{
              if(res.data.code == 200){
                this.wechatPayUrl = res.data.data
                this.wechatPay = true
              }
            })
            that.timeInterval = setInterval(function () {
              let params = {
                orderId:that.order.id
              }
              checkOrder(params).then(res=>{
                that.wechatPay = false;
                that.payOk = true
                that.payFlag = false
                that.waitpay = false
                that.init()
                clearInterval(that.timeInterval)
                // alert("支付成功")
                // this.$router.push({ path: '/uc/myCourse'})
              },error =>{
                console.log("未支付成功，请在支付页面继续完成支付！")
              })
            },1000)
          })
        }
      },
      checko(){
        let params = {
          orderId:this.order.id
        }
        checkOrder(params).then(res=>{
          this.payFlag = false
          this.waitpay = false
          this.init()
        },error =>{
          this.orderCss = true
          this.orderMsg = "未支付成功，请在支付页面继续完成支付！"
        })
      },
      clickMoney(moneyType){
        this.moneyType = moneyType
      },
      clickPayType(payType){
        this.payType = payType;
      },
      init(){
        if (typeof(this.studentInfo.id) == "undefined"){
          return;
        }
        getUserAccount().then(res =>{
          // this.userAccount = res.data.data;
          let accountList = res.data.data.records
          if(accountList.length >0){
            this.userAccount = accountList[0];
          }
        })
        let param= {
          "current":this.current,
          "size":this.size,
        }
        getUserAccountHistory(param).then(res =>{
          this.page = res.data.data
          this.userAccountHistory = res.data.data.records
        })
      }
    },
    mounted:function() {
      this.init();
    },
    computed: {
      ...mapGetters(["studentInfo","website"]),
    },
    components: {
      pageComponent
    },
    watch: {
      studentInfo: function (val, oldVal) {
        this.init();
      }
    },
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
