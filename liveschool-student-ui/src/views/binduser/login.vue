<template>
  <div class="login-wrap">
    <div id="signin" class="rl-modal">
      <div class="rl-modal-header">
        <h1>
          <router-link :to="{name:'bindUser',query:{uuid:this.$route.query.uuid,name:this.$route.query.name,oauthType:this.$route.query.oauthType,unionId:this.$route.query.unionId}}">
            <span class="active-title">绑定</span>
          </router-link>
          <router-link :to="{name:'registerUser',query:{uuid:this.$route.query.uuid,name:this.$route.query.name,oauthType:this.$route.query.oauthType,unionId:this.$route.query.unionId}}" ><span >注册</span></router-link>
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
            <div class="rlf-group rlf-appendix form-control  clearfix">
              <label class="rlf-autoin l" hidefocus="true">
                <p class="rlf-tip-globle color-red" id="signin-globle-error">{{errLoginMsg}}</p>
              </label>
<!--              <router-link :to="{path:'/user/forget'}" class="rlf-forget r" hidefocus="true">找回密码</router-link>-->
            </div>
            <div class="rlf-group clearfix">
              <input type="button" value="绑定" hidefocus="true" @click="login()"
                     class="moco-btn moco-btn-red moco-btn-lg btn-full xa-login">
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {captcha,sendSMS,sendSMSByMobile} from "@/api";
  import {login,loginBySMS,getAuthInfo,updateUser,updateUserProfile} from "@/api/user";
  import { validatenull,isMobile,isPassword} from '@/util/validate';
  import privacy from '@/views/privacy'


  export default {
    name: 'Login',
    data() {
      return {
        uuid:"1",
        name:"2",
        unionId:"1",
        oauthType:"3",
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
    beforeDestroy(){
      window.removeEventListener("message", this.msg, false);
    },
    methods: {
      msg(e){
        //更新用户信息
        let arr = e.data.split(",");
        getAuthInfo({id:arr[2]}).then(res=>{
          let obj = res.data.data
          this.$store.dispatch("LoginByUsername", obj)
          this.$store.dispatch("GetUserInfo").then(res =>{
            this.$router.push({ path: "/uc/myCourse",query:{courseType:''} });
          })
        })
      },
      oauthLogin(oauthType){

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
        login(this.form.mobile,this.form.password,grantType,this.captchaKey,this.form.captchaCode).then(res=>{
          if(res.data.code == 200){
            let obj = res.data.data;
            obj.saveLogin = that.saveLoginSMS;
            this.$store.dispatch("LoginByUsername", obj)
            this.$store.dispatch("GetUserInfo").then(res =>{
              let uuid = this.$route.query.uuid;
              let unionId = this.$route.query.unionId;
              let name = this.$route.query.name;
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
              updateUserProfile(param).then(res=>{
                //更新用户信息
                this.$store.dispatch("GetUserInfo")
              })
              this.$router.push({ path: "/uc/myCourse",query:{courseType:''} });
            })
          }else{
            //登录失败则 loginNum+1
            this.errLoginMsg = res.data.msg;
          }

        }, error => {
          this.errLoginMsg = error.message;
        })
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
        if (key == 13) {
          if(_self.loginType == 1){
            _self.login();
          }
          if(_self.loginType == 2){
            _self.loginByMobile();
          }
        }
      }
    }
  }
</script>

<style scoped>

</style>
