<template>
  <div class="login-wrap">
    <div class="rl-modal">
      <div class="js-forgotpwd-form-wrap" v-if="step == 1">
        <div class="forgot-title">
          <h2>通过手机/邮箱找回密码</h2>
          <p></p>
        </div>
        <div class="rl-modal-body">
          <form id="js-forgot-form">
            <div class="rlf-group">
              <input v-model="form.mobile" type="text" name="email" data-validate="email" class="ipt ipt-email" placeholder="请输入登录手机号/邮箱">
              <p class="rlf-tip-wrap errorHint tips">{{formMsg.mobileMsg}}</p>
            </div>
            <div class="rlf-group clearfix form-control">
              <input v-model="form.code" id="verify-ipt" type="text" name="verify" maxlength="5" autocomplete="off" class="ipt" placeholder="请输入验证码">
              <img class="verify-img-wrap js-change-verify-code" :src="captchaImg" alt="验证码" title="验证码">
              <a @click="clickCaptcha" href="javascript:void(0)" class="icon-refresh js-change-verify-code"></a>
              <p class="rlf-tip-wrap errorHint tips">{{formMsg.codeMsg}}</p>
            </div>
            <p class="color-red" id="js-g-forgot-error">{{formMsg.error}}</p>
            <div>
              <a @click="next()" href="javascript:void(0);" id="js-forgot-submit" class="moco-btn moco-btn-red moco-btn-lg btn-full">下一步</a>
            </div>
          </form>
        </div>
      </div >

      <div class="js-forgotpwd-form-wrap" v-if="step == 3" >
        <div class="forgot-title">
          <h2>找回密码</h2>
          <p>短信验证码已发送至<span class="js-phoneNumber">{{form.mobile}}</span></p>
        </div>
        <div class="rl-modal-body phoneVerityBox">
          <form id="signup-form">
            <div class="rlf-group pr">
              <input v-model="code" type="text" data-validate="require-string" data-minlength="4" class="js-phoneVerity ipt ipt-pwd" placeholder="请输入短信验证码" maxlength="4" autocomplete="off">
              <p @click="reSendSMS()" class="reSend pa js-phonecode-box active">{{reSendMsg}}</p>
              <p class="rlf-tip-wrap errorHint color-red" id="error-phoneVerity" data-error-hint="请输入正确验证码">{{formMsg.smsMsg}}</p>
            </div>
            <div class="rlf-group proclaim-loc js-pwdWrap pr">
              <a @click="switchPassword()" href="javascript:void(0)" hidefocus="true" v-bind:class="{ 'imv2-visibility_off': showPassword == false,'imv2-visibility' :showPassword }" class="proclaim-btn js-proclaim is-pwd"></a>
              <input v-model="form.newPwd" :type="[showPassword?'text':'password']" id="js-password" name="password" data-validate="require-newpassword" class="ipt ipt-pwd js-pass-pwd" placeholder="8-20位,字母/数字/字符至少包含两种" maxlength="20" tabindex="2">
              <p class="rlf-tip-wrap errorHint color-red" id="error-password">{{formMsg.newPwdMsg}}</p>
            </div>
            <p id="js-g-forgot-error" class="color-red">{{formMsg.error1}}</p>
            <div class="rlf-group clearfix">
              <input @click="updatePwd()" type="button" id="submit-btn" value="提交" hidefocus="true" class="moco-btn moco-btn-red moco-btn-lg btn-full">
            </div>
            <p @click="goStep1()" class="backNotify js-back" data-fromto="signin:signup">返回修改手机号 </p>
          </form>
        </div>
      </div >

      <div class="js-forgot-result forgot-send-result forgot-modal"  v-if="step == 2">
        <span class="icon-send-success">
          <i class="imv2-check"></i>
        </span>
        <p>密码重设邮件发送成功</p>
        <div class="get-info">已发送至
          <span class="xa-email">{{form.mobile}}</span>
        </div>
        <router-link :to="{path:'/user/login'}" class="moco-btn moco-btn-red moco-btn-lg js-email-add">去登录</router-link>
        <div class="get-info">收不到邮件？查看
<!--          <a href="javascript:void(0)" target="_blank">常见问题</a>-->
        </div>
      </div>

      <div class="js-forgot-result forgot-send-result forgot-modal"  v-if="step == 4">
        <span class="icon-send-success">
          <i class="imv2-check"></i>
        </span>
        <p>密码重设成功</p>
        <div class="get-info">
          <span class="xa-email"></span>
        </div>
        <router-link :to="{path:'/user/login'}" class="moco-btn moco-btn-red moco-btn-lg js-email-add">去登录</router-link>
        <div class="get-info">
<!--          <a href="javascript:void(0)" target="_blank">常见问题</a>-->
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {captcha,sendEmailResetPwd,sendSMSByMobile,checkCaptcha,updatePwdByMobile} from "@/api";
  import {register,checkMobile} from "@/api/user";
  import { mapGetters } from "vuex";
  import { isEmail,isPassword,validatenull,isMobile} from '@/util/validate';

  export default {
    name: 'Login',
    data() {
      return {
        step:1,
        reSendTime:0,
        showPassword:false,
        reSendMsg:"重新发送",
        captchaImg:"",
        captchaKey:"",
        code:"",
        form:{
          mobile:"",
          code:"",
          key:"",
          newPwd:""
        },
        formMsg:{
          mobileMsg:"",
          codeMsg:"",
          error:"",
          smsMsg:"",
          newPwdMsg:"",
          error1:""
        }
      }
    },
    methods: {
      updatePwd(){
        //判断验证码是否为空
        if(validatenull(this.code)) {
          this.formMsg.smsMsg = "请输入短信验证码";
          return;
        }
        //判断密码是否符合规则
        if(isPassword(this.form.newPwd)) {
          this.formMsg.newPwdMsg = "请输入8-20位字母、数字或字符，至少包含两种！";
          return;
        }
        this.form.code = this.code
        updatePwdByMobile(this.form).then(res=>{
          this.step = 4
        },error=>{
          this.formMsg.error1 = error.message
        })
      },
      reSendSMS(){
        if(this.reSendTime == 0){
          //发送手机验证码
          sendSMSByMobile(this.form.mobile,"common_code").then(res =>{
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
      init(){
        this.formMsg.mobileMsg = ""
        this.formMsg.codeMsg = ""
        this.formMsg.error = ""
        var _self = this;
        this.clickCaptcha()
        //防止浏览器报错验证码
        setTimeout(function(){
          _self.formMsg.mobileMsg="";
          _self.formMsg.codeMsg="";
        },3000);
      },
      clickCaptcha(){
        captcha().then(res =>{
          this.captchaImg = res.data.data.image
          this.captchaKey = res.data.data.key
        })
      },
      goStep1(){
        this.formMsg.mobileMsg = ""
        this.formMsg.codeMsg = ""
        this.formMsg.error = ""
        this.step = 1;
      },
      next(){

        this.formMsg.mobileMsg = ""
        this.formMsg.codeMsg = ""
        if(validatenull(this.form.mobile)) {
          this.formMsg.mobileMsg = "手机号/邮箱不能为空！";
          return;
        }
        if(validatenull(this.form.code)) {
          this.formMsg.codeMsg = "请输入正确的验证码！";
          return;
        }
        //是手机
        if(isMobile(this.form.mobile)){
          checkMobile(this.form.mobile).then(res =>{
            this.formMsg.mobileMsg = "该手机号没有注册！";
          }, error => {
            //验证验证码
            let param = {
              "key":this.captchaKey,
              "code":this.form.code
            }
            checkCaptcha(param).then(res =>{
              //发送手机验证码
              sendSMSByMobile(this.form.mobile,"common_code").then(res=>{
                this.step = 3;
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
              })
            }, error => {
              this.formMsg.codeMsg = error.message;
            });
          });
          return;
        }
        //是邮箱
        if(isEmail(this.form.mobile)){
          checkMobile(this.form.mobile,"3").then(res =>{
            this.formMsg.mobileMsg = "该邮箱没有注册！";
          }, error => {
            this.form.email = this.form.mobile
            this.form.key = this.captchaKey
            sendEmailResetPwd(this.form).then(res =>{
              this.step = 2
              this.$store.dispatch("LogOut")
            },error =>{
              this.formMsg.error = error.message
            })
          })

          return;
        }
        this.formMsg.mobileMsg = "请输入正确的手机号/邮箱！";
        return;
      }
    },
    mounted:function() {
      this.init()
    },

  }
</script>

<style scoped>

</style>
