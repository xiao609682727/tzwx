<template>
  <div class="login-wrap">
    <div id="signin" class="rl-modal">
      <div class="rl-modal-header">
        <h1>
          <router-link :to="{name:'login'}">
            <span class="active-title">登录</span>
          </router-link>
          <router-link :to="{name:'register'}">
            <span data-fromto="signin:signup"
                  class="xa-showSignup">注册
            </span>
          </router-link>
        </h1>
      </div>
      <div class="rl-modal-body js-loginWrap" v-if="loginType == 1">
        <div class="clearfix">
          <form autocomplete="off">
            <div class="rlf-group pr">
              <input type="text" value="" maxlength="37" name="email" v-model="form.mobile"
                     data-validate="require-mobile-phone" autocomplete="off"
                     class="xa-emailOrPhone ipt ipt-email js-own-name"
                     placeholder="请输入登录手机号/邮箱/账号">
              <p class="rlf-tip-wrap errorHint color-red" data-error-hint="请输入正确的邮箱或手机号">{{errMobileMsg}}</p>
            </div>
            <div class="rlf-group  pr">
              <a href="javascript:void(0)" hidefocus="true" @click="switchPassword()" v-bind:class="{ 'imv2-visibility_off': showPassword == false,'imv2-visibility' :showPassword }"
                 class="proclaim-btn js-proclaim is-pwd"
                 tabindex="-1"></a>
              <input :type="[showPassword?'text':'password']" name="password"
                     data-validate="require-password"
                     class="ipt ipt-pwd js-loginPassword js-pass-pwd"
                     placeholder="请输入密码" maxlength="20" v-model="form.password"
                     autocomplete="off">
              <p class="rlf-tip-wrap errorHint color-red ">{{errPasswordMsg}}</p>
            </div>
            <div class="rlf-group clearfix form-control js-verify-row" v-show="captchaFlag">
              <input type="text"
                     name="verify"
                     class="ipt ipt-verify l"
                     data-validate="require-string"
                     data-callback="checkverity"
                     maxlength="5"
                     data-minlength="5"
                     v-model="form.captchaCode"
                     placeholder="请输入验证码">
              <a href="javascript:void(0)" hidefocus="true" class="verify-img-wrap js-verify-refresh">
                <img class="verify-img" :src="captchaImg">
              </a>
              <a
                href="javascript:void(0)" hidefocus="true" class="icon-refresh js-verify-refresh"></a>
              <p class="rlf-tip-wrap errorHint color-red" data-error-hint="请输入正确验证码">{{errCaptchaCodeMsg}}</p>
            </div>
            <div class="rlf-group rlf-appendix form-control  clearfix">
              <label for="auto-signin" class="rlf-autoin l"
                     hidefocus="true">
                <input v-model="saveLoginPWD"
                  type="checkbox"  class="auto-cbx" id="auto-signin">
                7天内自动登录
              </label>

              <router-link :to="{path:'/user/forget'}" class="rlf-forget r" hidefocus="true">找回密码</router-link>
            </div>
            <p class="rlf-tip-globle color-red" id="signin-globle-error">{{errLoginMsg}}</p>
            <div class="rlf-group clearfix">
              <input type="button" value="登录" hidefocus="true" @click="login()"
                     class="moco-btn moco-btn-red moco-btn-lg btn-full xa-login">
            </div>
          </form>
        </div>
      </div>
      <div class="rl-modal-body js-loginWrap" v-if="loginType == 2">
        <div class="clearfix">
          <form autocomplete="off">
            <div class="rlf-group pr">
              <div class="rlf-areacode">+86</div>
              <input v-model="form.mobile" type="text" value="" maxlength="37" name="phone" data-validate="require-mobile-phone" autocomplete="off" class="ipt ipt-phone js-phone-name" placeholder="短信登录仅限中国大陆用户">
              <p class="rlf-tip-wrap errorHint color-red" data-error-hint="请输入正确的手机号">{{errMobileMsg}}</p>
            </div>
            <div class="rlf-group pr phoneVerityBox">
              <input v-model="form.smsCode" type="text" id="js-phoneVerity" data-validate="require-string" data-minlength="4" class="ipt ipt-pwd" placeholder="请输入短信验证码" maxlength="4" autocomplete="off">
              <p class="reSend pa active js-phonecode-box">
                <span class="js-signin-send" @click="reSendSMS" >{{reSendMsg}}</span>
              </p>
              <p class="rlf-tip-wrap errorHint color-red" data-error-hint="请输入正确验证码">{{errCaptchaCodeMsg}}</p>
            </div>
            <div class="rlf-group rlf-appendix form-control  clearfix">
              <label for="auto-signin" class="rlf-autoin l" hidefocus="true">
                <input v-model="saveLoginSMS" type="checkbox"  class="auto-cbx" >
                7天内自动登录
              </label>
            </div>
            <p class="rlf-tip-globle color-red" >{{errLoginMsg}}</p>
            <div class="rlf-group clearfix">
              <input @click="loginByMobile()" type="button" value="登录" hidefocus="true" class="moco-btn moco-btn-red moco-btn-lg btn-full xa-phone-login">
            </div>
          </form>
        </div>
      </div>
      <div class="rl-model-footer">
        <div class="pop-login-sns clearfix" :style="website.smsloginFlag == '1'? '':'margin: 0 28% 10px '">
          <span v-if="website.smsloginFlag == '1'" @click="changeLoginType()" class="l rlf-other xa-showPhoneSignin">{{loginTypeName}}</span>

          <a v-if="website.weiboOpenFlag == '1'"
            href="javascript:void(0)" @click="oauthLogin('weibo')" hidefocus="true" data-login-sns="/passport/user/tplogin?tp=weibo"
            class="pop-sns-weibo l">
            <img  src="/static/img/login-xb.png" height="26px" />
          </a>
          <a v-if="website.wechatOpenFlag == '1'" href="javascript:void(0)" @click="oauthLogin('wechat_open')" hidefocus="true"
             data-login-sns="/passport/user/tplogin?tp=weixin"
             class="pop-sns-weixin l">
            <img  src="/static/img/login-wx.png" height="26px" />

          </a>
          <a v-if="website.qqOpenFlag == '1'" href="javascript:void(0)" @click="oauthLogin('qq')" hidefocus="true"
             data-login-sns="/passport/user/tplogin?tp=qq" class="pop-sns-qq l">
            <img  src="/static/img/login-qq.png" height="26px" />
          </a>
        </div>
        <div class="privacy_tip">登录即同意
          <a href="javascript:void(0)" @click="openAgreement()" >《用户使用协议》和《隐私政策》</a>
        </div>
        <!-- 扫描登录 -->
        <!--<div class="erweima xa-showQrcode"></div>-->
      </div>
    </div>
  </div>
</template>

<script>
  import { mapGetters } from "vuex";
  import {captcha,sendSMS,sendSMSByMobile} from "@/api";
  import {login,loginBySMS,getAuthInfo} from "@/api/user";
  import { validatenull,isMobile,isPassword} from '@/util/validate';
  import privacy from '@/views/privacy'


  export default {
    name: 'Login',
    data() {
      return {
        oauthType:"",
        loginType:1,
        loginTypeName:"手机短信登录",
        reSendMsg:"获取验证码",
        reSendTime:0,
        errMobileMsg:"",
        errPasswordMsg:"",
        errCaptchaCodeMsg:"",
        errLoginMsg:"",
        showPassword:false,
        captchaFlag:false,
        captchaImg:"",
        captchaKey:"",
        saveLoginSMS:true,
        saveLoginPWD:true,
        loginNum:0,//判断如果登录错误超过3次则需要验证码
        form:{
          mobile:"",
          password:"",
          smsCode:"",
          captchaCode:""
        }
      }
    },
    methods: {
      msg(e){
        //更新用户信息
        let arr = e.data.split(",");
        if("2"==arr[1]){
          getAuthInfo({id:arr[2]}).then(res=>{
            let obj = res.data.data
            this.$router.push({ path: "/oauth/bindUser",query:{uuid:obj.uuid,name:obj.nickname,oauthType:this.oauthType,unionId:arr[2]} });
          })

        }
        if("1"==arr[1]){
          getAuthInfo({id:arr[2]}).then(res=>{
            let obj = res.data.data
            this.$store.dispatch("LoginByUsername", obj)
            this.$store.dispatch("GetUserInfo").then(res =>{
              this.$router.push({ path: "/uc/myCourse",query:{courseType:''} });
            })
          })
        }
      },
      oauthLogin(oauthType){
        this.oauthType = oauthType
        var iWidth = 800;
        var iHeight = 600;
        var iTop = (window.screen.availHeight-30-iHeight)/2;       //获得窗口的垂直位置;
        var iLeft = (window.screen.availWidth-10-iWidth)/2;           //获得窗口的水平位置;
        let url = "/api/front/oauth/login/"+oauthType+"?userId=&type=login";
        window.open( url, "授权", 'height=' + iHeight + ',innerHeight=' + iHeight + ',width=' + iWidth + ',innerWidth=' + iWidth + ',top=' + iTop + ',left=' + iLeft + ', toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no, titlebar=yes, alwaysRaised=yes');
      },
      reSendSMS(){
        if(this.reSendTime == 0){
          //手机号规则验证
          if(validatenull(this.form.mobile)) {
            this.errMobileMsg = "请输入手机号";
            return;
          }
          if(!isMobile(this.form.mobile)) {
            this.errMobileMsg = "请输入正确的手机号";
            return;
          }
          sendSMSByMobile(this.form.mobile).then(res =>{
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
      changeLoginType(){
        this.form.mobile = "";
        this.form.smsCode = "";
        this.form.captchaCode = "";
        this.form.password = "";
        if(this.loginType == 1){
          this.loginType = 2
          this.loginTypeName="账号密码登录"
        }else if(this.loginType == 2){
          this.loginType = 1
          this.loginTypeName="手机短信登录"
        }
      },
      switchPassword(){
        this.showPassword = !this.showPassword
      },
      clickCaptcha(){
        captcha().then(res =>{
          this.captchaImg = res.data.data.image
          this.captchaKey = res.data.data.key
        })
      },
      loginByMobile(){
        this.errMobileMsg = "";
        this.errPasswordMsg = "";
        this.errCaptchaCodeMsg = "";
        this.errLoginMsg = "";
        //手机号规则验证
        if(validatenull(this.form.mobile)) {
          this.errMobileMsg = "请输入手机号";
          return;
        }
        if(!isMobile(this.form.mobile)) {
          this.errMobileMsg = "请输入正确的手机号";
          return;
        }
        if(validatenull(this.form.smsCode)) {
          this.errCaptchaCodeMsg = "请输入验证码";
          return;
        }
        let that = this
        loginBySMS(this.form.mobile,this.form.smsCode).then(res =>{
          if(res.data.code == 200){
            let obj = res.data.data;
            obj.saveLogin = that.saveLoginSMS;
            this.$store.dispatch("LoginByUsername", obj)
            this.$store.dispatch("GetUserInfo").then(res =>{
              let redirect = this.$route.query.redirect;
              if(redirect){
                this.$router.push({
                  path: redirect
                });
              }else{
                this.$router.push({ path: "/uc/myCourse",query:{courseType:''} });
              }
            })
          }else{
            this.errLoginMsg = res.data.msg;
          }
        },error =>{
          this.errLoginMsg = error.message;
        })
      },
      //登录方法
      login(){
        this.errMobileMsg = "";
        this.errPasswordMsg = "";
        this.errCaptchaCodeMsg = "";
        this.errLoginMsg = "";
        //用户名密码登录
        //手机号规则验证
        if(validatenull(this.form.mobile)) {
          this.errMobileMsg = "请输入手机号";
          return;
        }
        //密码为空验证
        if(validatenull(this.form.password)) {
          this.errPasswordMsg = "请输入密码";
          return;
        }
        let grantType = "password";
        if(this.captchaFlag){
          grantType = "captcha";
          if(validatenull(this.form.captchaCode)) {
            this.errCaptchaCodeMsg = "请输入验证码";
            return;
          }
        }
        let that = this
        //登录方法请求
        login(this.form.mobile,this.form.password,grantType,this.captchaKey,this.form.captchaCode).then(res=>{
          if(res.data.code == 200){
            let obj = res.data.data;
            obj.saveLogin = that.saveLoginPWD;
            this.$store.dispatch("LoginByUsername", obj)
            this.$store.dispatch("GetUserInfo").then(res =>{
              let redirect = this.$route.query.redirect;
              if(redirect){
                this.$router.push({
                  path: redirect
                });
              }else{
                this.$router.push({ path: "/uc/myCourse",query:{courseType:''} });
              }
            })

          }else{
            //登录失败则 loginNum+1
            this.loginNum = this.loginNum+1;
            if(this.loginNum >= 4){
              this.captchaFlag = true;
              //创建验证码
              this.clickCaptcha()
            }
            this.errLoginMsg = res.data.msg;
          }

        }, error => {
          this.loginNum = this.loginNum+1;
          if(this.loginNum >= 4){
            this.captchaFlag = true;
            //创建验证码
            this.clickCaptcha()
          }
          this.errLoginMsg = error.message;
        })
      },
      log(){
        console.log("111111111111")
      },
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
      }
    },
    mounted:function() {
      window.addEventListener("message", this.msg, false);
      var _self = this;
      document.onkeydown = function (e) {
        var key = window.event.keyCode;
        if (key == 13 || key == 100) {
          if(_self.loginType == 1){
            _self.login();
          }
          if(_self.loginType == 2){
            _self.loginByMobile();
          }
        }
      }
    },
    computed: {
      ...mapGetters(["website"]),
    },
    beforeDestroy(){
      window.removeEventListener("message", this.msg, false);
    }
  }
</script>

<style scoped>

</style>
