<template>
  <div class="login-wrap">

    <div id="signup" class="rl-modal  rl-model-signup">
      <div class="rl-modal-header">
        <h1>
          <router-link :to="{name:'bindUser',query:{uuid:this.$route.query.uuid,name:this.$route.query.name,oauthType:this.$route.query.oauthType,unionId:this.$route.query.unionId}}" ><span data-fromto="signup:signin" class="xa-showSignin">绑定</span></router-link>
          <router-link :to="{name:'registerUser',query:{uuid:this.$route.query.uuid,name:this.$route.query.name,oauthType:this.$route.query.oauthType,unionId:this.$route.query.unionId}}" ><span class="active-title">注册</span></router-link>
        </h1>
      </div>
      <div v-if="step == 1" class="rl-modal-body js-modal-body js-registerWrap">
        <form id="signup-form pr">
          <!--<div class="rlf-group  pr">-->
            <!--<input type="text" v-model="form.username" maxlength="37" value="" name="username" data-callback="checkusername" data-validate="require-mobile-phone" autocomplete="off" class="ipt " placeholder="请输入账号">-->
            <!--<p class="rlf-tip-wrap errorHint color-red" >{{errUsernameMsg}}</p>-->
          <!--</div>-->
          <div class="rlf-group  pr">
            <input type="text" v-model="form.mobile" maxlength="37" value="" name="email" data-callback="checkusername" data-validate="require-mobile-phone" autocomplete="off" class="ipt " placeholder="请输入注册手机号">
            <p class="rlf-tip-wrap errorHint color-red" >{{errMobileMsg}}</p>
          </div>
          <div class="rlf-group proclaim-loc  pr">
            <input :type="[showPassword?'text':'password']" id="js-password" name="password" v-model="form.password" data-validate="require-newpassword" class="ipt ipt-pwd js-pass-pwd" placeholder="8-20位,字母/数字/字符至少包含两种" maxlength="20" autocomplete="off">
            <p class="rlf-tip-wrap errorHint color-red ">{{passwordMsg}}</p>
          </div>

          <div class="rlf-group clearfix form-control " v-if="website.smsOpenFlag == 1">
            <input type="text" v-model="code" name="verify" class="ipt ipt-verify js-emailverify l" data-validate="require-string" data-callback="checkverity" autocomplete="off" maxlength="5" data-minlength="5" placeholder="请输入验证码">
            <a href="javascript:void(0)" @click="clickCaptcha" hidefocus="true" class="verify-img-wrap js-verify-refresh">
              <img class="verify-img" :src="captchaImg">
            </a>
            <a href="javascript:void(0)" @click="clickCaptcha" hidefocus="true" class="icon-refresh js-verify-refresh"></a>
            <p class="rlf-tip-wrap errorHint color-red" >{{errCodeMsg}}</p>
          </div>
          <div class="rlf-group pr" v-if="website.smsOpenFlag == 1">
            <input type="text" id="js-phoneVerity" v-model="form.smsCode" data-validate="require-string" data-minlength="4" class="ipt ipt-pwd" placeholder="请输入短信验证码" maxlength="4" autocomplete="off">
            <p class="reSend pa js-phonecode-box active">
              <span class="js-reSend" @click="reSendSMS">{{reSendMsg}}</span>
            </p>
            <p class="rlf-tip-wrap errorHint color-red" >{{smsCodeMsg}}</p>
          </div>
          <div class="rlf-group rlf-appendix form-control  clearfix" style="margin-bottom:0">
            <label for="signup-protocol" class="rlf-autoin l" hidefocus="true">
              <input v-model="agreement" type="checkbox" class="auto-cbx" id="signup-protocol">
              同意
              <a href="javascript:void(0)" @click="openAgreement()" class="ipt-agreement" >《用户使用协议》和《隐私政策》</a>
            </label>
            <p class="rlf-tip-wrap errorHint color-red rlf-tip-globle" id="signup-globle-error" >{{agreementMsg}}</p>
          </div>
          <div class="rlf-group clearfix">
            <a href="javascript:void(0)" @click="registerUser(2)" id="signup-btn" hidefocus="true" class="moco-btn moco-btn-red moco-btn-lg btn-full btn r"> 注册</a>
          </div>
        </form>
      </div>
      <div class="rl-modal-body reg-success" v-if="step == 3">
        <span class="icon-send-success">
          <i class="imv2-check"></i>
        </span>
        <p>恭喜您注册成功</p>
        <div class="rlf-group clearfix finished_wrap">
          <input @click="toMyCourse" type="button" value="开始学习" hidefocus="true" class="moco-btn moco-btn-red moco-btn-lg js-gotoLearn">
          <div>
<!--            <a href="javascript:void(0)" class="js-gotoSetting" style="padding:12px 35px;">完善资料</a>-->
          </div>
        </div>
      </div>
    <!--  <div v-if="step == 1" class="rl-model-footer">
        <div class="pop-login-sns clearfix">
          <span class="l ">其他方式登录：</span>
          <a href="javascript:void(0)" hidefocus="true" data-login-sns="/passport/user/tplogin?tp=weibo" class="pop-sns-weibo r">
            <i class="imv2-weibo1"></i>
          </a>
          <a href="javascript:void(0)" hidefocus="true" data-login-sns="/passport/user/tplogin?tp=weixin" class="pop-sns-weixin r">
            <i class="imv2-weixin"></i>
          </a>
          <a href="javascript:void(0)" hidefocus="true" data-login-sns="/passport/user/tplogin?tp=qq" class="pop-sns-qq r">
            <i class="imv2-qq"></i>
          </a>
        </div>
      </div>-->
    </div>
  </div>
</template>

<script>
  import { mapGetters } from "vuex";
  import {captcha,sendSMS} from "@/api";
  import {register,checkMobile,updateUser,updateUserProfile} from "@/api/user";
  import { validatenull,isMobile,isPassword} from '@/util/validate';
  import privacy from '@/views/privacy'

  export default {
    name: 'register',
    data() {
      return {
        uuid:"1",
        name:"2",
        unionId:"1",
        oauthType:"3",
        step:1,
        captchaImg:"",
        captchaKey:"",
        code:"",
        agreement:1,
        errMobileMsg:"",
        errCodeMsg:"",
        agreementMsg:"",
        errUsernameMsg:"",
        showPassword:false,
        reSendMsg:"发送验证码",
        reSendTime:0,
        passwordMsg:"",
        smsCodeMsg:"",
        form:{
          mobile:"",
          password:"",
          smsCode:"",
          username:""
        }
      }
    },
    methods: {
      openAgreement(){
        this.$layer.iframe({
          content: {
            content: privacy, //传递的组件对象
            parent: this,//当前的vue对象
            data:{
              info:{a:1}
            }//props
          },
          area:['800px','600px'],
          title:"用户使用协议"
        });
      },
      init(){
        this.clickCaptcha()
      },
      clickCaptcha(){
        captcha().then(res =>{
          this.captchaImg = res.data.data.image
          this.captchaKey = res.data.data.key
        })
      },
      toStep(step){
        this.errMobileMsg = "";
        this.errCodeMsg = "";
        this.agreementMsg = "";

        if(step == 2){

          //手机号规则验证
          if(validatenull(this.form.mobile)) {
            this.errMobileMsg = "请输入手机号";
            return;
          }
          if(!isMobile(this.form.mobile)) {
            this.errMobileMsg = "请输入正确的手机号";
            return;
          }
          if(validatenull(this.code)) {
            this.errCodeMsg = "请输入验证码";
            return;
          }
          //验证是否勾选协议
          if(this.agreement == 0){
            this.agreementMsg = "请同意注册协议";
            return;
          }
          //检查手机号是否已注册
          checkMobile(this.form.mobile).then(res =>{
            //手机号检查成功发送验证码
            sendSMS(this.captchaKey,this.form.mobile,this.code).then(res =>{
              this.form.password = ""
              this.form.smsCode = ""
              this.step = step
            }, error => {
              this.errCodeMsg = error.message;
            });
          }, error => {
            this.errMobileMsg = error.message;
          });
        }else{
          this.form.mobile = "";
          this.code = "";
          //刷新验证码
          this.clickCaptcha()
          this.step = step
        }
      },
      reSendSMS(){
        this.errCodeMsg = ""
        if(this.reSendTime == 0){
          sendSMS(this.captchaKey,this.form.mobile,this.code).then(res =>{
            this.reSendTime = 60
            let that = this
            let timer = setInterval(function () {
              that.reSendTime = that.reSendTime-1
              that.reSendMsg = "重新发送  "+ that.reSendTime;
              if(that.reSendTime == 0){
                clearInterval(timer);
                that.reSendMsg = "重新发送";
              }
            },1000)
          }, error => {
            this.errCodeMsg = error.message;
          });
        }
      },
      switchPassword(){
        this.showPassword = !this.showPassword
      },
      registerUser(){
        this.errUsernameMsg = "";
        this.errMobileMsg = "";
        this.errCodeMsg = "";
        this.agreementMsg = "";
        this.passwordMsg = "";
        this.smsCodeMsg = "";
        //账号判断
        /*if(validatenull(this.form.username)) {
          this.errUsernameMsg = "请输入账号";
          return;
        }*/
        //检查账号是否已注册
        // let a = checkMobile(this.form.username,"1").then(res =>{
          //手机号规则验证
          if(validatenull(this.form.mobile)) {
            this.errMobileMsg = "请输入手机号";
            return;
          }
          if(!isMobile(this.form.mobile)) {
            this.errMobileMsg = "请输入正确的手机号";
            return;
          }
          //检查手机号是否已注册
          checkMobile(this.form.mobile).then(res =>{
            if(isPassword(this.form.password)){
              this.passwordMsg = "请输入8-20位密码,字母/数字/字符至少包含两种";
              return;
            }
            //后台短信验证码注册判断
            if(this.website.smsOpenFlag == "1"){
              if(validatenull(this.code)) {
                this.errCodeMsg = "请输入验证码";
                return;
              }
              if(validatenull(this.form.smsCode)){
                this.smsCodeMsg = "请输入验证码";
                return;
              }
            }

            //验证是否勾选协议
            if(this.agreement == 0){
              this.agreementMsg = "请同意注册协议";
              return;
            }

            register(this.form.password,this.form.mobile,this.form.smsCode,this.form.username).then(res =>{
              //注册成功跳到登录
              this.$store.dispatch("LoginByUsername", res.data.data,false)
              this.$store.dispatch("GetUserInfo").then(res =>{
                let uuid = this.$route.query.uuid;
                let name = this.$route.query.name;
                let unionId = this.$route.query.unionId;
                let oauthType = this.$route.query.oauthType;
                let param;
                if(oauthType == "weibo"){
                  param = {
                    "openid":uuid,
                    "unionid":unionId,
                    "profiletype":"weibo",
                    "name":name,
                    "userid":res.id
                  }
                }
                if(oauthType == "wechat_open"){
                  param = {
                    "openid":uuid,
                    "unionid":unionId,
                    "profiletype":"wxPC",
                    "name":name,
                    "userid":res.id
                  }
                }
                if(oauthType == "qq"){
                  param = {
                    "openid":uuid,
                    "unionid":unionId,
                    "profiletype":"qqPC",
                    "name":name,
                    "userid":res.id
                  }
                }
                updateUser(param).then(res=>{
                  //更新用户信息
                  this.$store.dispatch("GetUserInfo")
                })
                updateUserProfile(param).then(res=>{
                  //新增或修改原有绑定第三信息
                  this.$store.dispatch("GetUserInfo")
                })
                this.step = 3;
                // this.$router.push({ path: "/uc/myCourse",query:{courseType:''} });
              })

            }, error => {
              this.smsCodeMsg = error.message;
            })
          }, error => {
            this.errMobileMsg = error.message;
          });

        //}, error => {
        //   this.errUsernameMsg = error.message;
        //   return;
        // });

      },
      toMyCourse(){
        this.$router.push({ path: "/uc/myCourse",query:{courseType:''} });
      }
    },
    mounted:function() {
      this.init()
    },
    computed: {
      ...mapGetters(["website"]),
    },
  }
</script>

<style scoped>

</style>
