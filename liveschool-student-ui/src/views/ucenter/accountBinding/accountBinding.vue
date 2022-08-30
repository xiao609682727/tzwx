<template>
  <div class="u-container">
  <div class="setting-right-wrap wrap-boxes settings">
    <div class="formBox">
      <div class="page-settings">
        <!--<div class="oplog-tip">
          上次登录时间：2020-05-24 09:07:11&nbsp;&nbsp;&nbsp;地点：
          <a href="/user/oplog">查看最近操作记录</a>
          <span class="tip-close pull-right js-tip-close">×</span>
        </div>-->
        <div class="common-title">
          账号绑定
          <!--        <span class="title-tips">完成<b class="color-red">2/4</b></span>-->
          <a href="/help/10" class="pull-right" target="_blank">
            <i class="icon-ques-revert"></i>
            常见问题
          </a>
        </div>
        <div class="line"></div>
        <div class="setting">
          <div class="contentBox">
            <div class="bingd">
              <div class="itemBox">
                <div class="left">
                  <i class="icon-set_email"></i>
                </div>
                <div class="center">
                  <p>
                    <span class="font1">邮箱</span>
                    <span class="font3"  v-if="studentInfo.email != ''">
                    {{studentInfo.email}}
                      <span style="color: #f01414;">已绑定</span>

                  </span>
                    <span class="font4" v-if="studentInfo.email == ''">未绑定</span>
                  </p>
                  <p class="font2">可用邮箱加密码登录，可用邮箱找回密码</p>
                </div>
                <div class="right">
                  <a v-if="studentInfo.email != ''" @click="showBindEmail()" href="javascript:void(0);" class="binding js-bindemail moco-btn moco-btn-normal">更改</a>
                  <a v-if="studentInfo.email == ''" @click="showBindEmail()" href="javascript:void(0);" class="binding js-bindemail moco-btn moco-btn-normal">立即绑定</a>
                </div>
              </div>
              <div class="itemBox">
                <div class="left">
                  <i class="icon-set_phone"></i>
                </div>
                <div class="center">
                  <p>
                    <span class="font1">手机</span>
                    <span class="font4" id="jsPhone">{{usermobile}}</span>
                  </p>
                  <p class="font2">可用手机号加密码登录，可通过手机号找回密码
                  </p>
                </div>
                <div class="right">
                  <a @click="showChangeMobile" href="javascript:void(0);" class="change js-operate-phone moco-btn moco-btn-normal" changetype="phone">操作</a>
                  <input type="hidden" id="account" value="131******29">
                </div>
              </div>

              <div class="itemBox">
                <div class="left">
                  <i class="icon-set_key"></i>
                </div>
                <div class="center">
                  <p>
                    <span class="font1">密码</span>
                    已设置
                  </p>
                  <p class="font2">用于保护账号信息和登录安全</p>
                </div>
                <div class="right">
                  <a href="javascript:void(0);" class="moco-btn moco-btn-normal js-changePWD" @click="showPassword()">修改</a>
                </div>
              </div>

              <div class="itemBox bb0 h380">
                <div class="left">
                  <i class="icon-feedback"></i>
                </div>
                <div class="center">
                  <p class="font1">社交账号</p>
                  <p class="font2">绑定第三方账号，可以直接登录，还可以将内容同步到以下平台，与更多好友分享</p>
                  <div class="accountBox">

                    <div  class="inner-i-box" v-if="studentInfo.wechat == ''&&website.wechatOpenFlag == '1'">
                      <i class="icon-weixin"></i>
                      <p class="mr87 bind-name">微信</p>
                      <p class="mr87 red">未绑定</p>
                      <a href="javascript:void(0)" @click="bind('wechat_open')" aria-role="button" hidefocus="true" class="moco-btn-normal rlf-btn-green btn js-bind mr87">添加绑定</a>
                    </div>
                    <div class="inner-i-box" v-if="studentInfo.wechat != ''&&website.wechatOpenFlag == '1'">
                      <i class="icon-weixin color_blue"></i>
                      <p class="mr87 bind-name">微信</p>
                      <p class="mr87 bind-status">已绑定 {{studentInfo.wechatname}}</p>
                      <a href="javascript:void(0)" @click="unbind('wechat')" aria-role="button" hidefocus="true" class="moco-btn-normal rlf-btn-green btn js-bind mr87">解除绑定</a>
                    </div>
                    <div class="inner-i-box" v-if="studentInfo.weibo == ''&&website.weiboOpenFlag == '1'">
                      <i class="icon-weibo"></i>
                      <p class="mr87 bind-name">微博</p>
                      <p class="mr87 red">未绑定</p>
                      <a href="javascript:void(0)" @click="bind('weibo')" aria-role="button" hidefocus="true" class="moco-btn-normal rlf-btn-green btn js-bind mr87">添加绑定</a>
                    </div>
                    <div class="inner-i-box" v-if="studentInfo.weibo != ''&&website.weiboOpenFlag == '1'">
                      <i class="icon-weibo color_blue"></i>
                      <p class="mr87 bind-name">微博</p>
                      <p class="mr87 bind-status">已绑定 {{studentInfo.weiboname}}</p>
                      <a href="javascript:void(0)" @click="unbind('weibo')" aria-role="button" hidefocus="true" class="moco-btn-normal rlf-btn-green btn js-bind mr87">解除绑定</a>
                    </div>
                    <div class="inner-i-box" v-if="studentInfo.qq == ''&&website.qqOpenFlag == '1'">
                      <i class="icon-qq"></i>
                      <p class="mr87 bind-name">QQ</p>
                      <p class="mr87 red">未绑定</p>
                      <a href="javascript:void(0)" @click="bind('qq')" aria-role="button" hidefocus="true" class="moco-btn-normal rlf-btn-green btn js-bind mr87">添加绑定</a>
                    </div>
                    <div class="inner-i-box" v-if="studentInfo.qq != ''&&website.qqOpenFlag == '1'">
                      <i class="icon-qq color_blue"></i>
                      <p class="mr87 bind-name">QQ</p>
                      <p class="mr87 bind-status">已绑定 {{studentInfo.qqname}}</p>
                      <a href="javascript:void(0)" @click="unbind('qq')" aria-role="button" hidefocus="true" class="moco-btn-normal rlf-btn-green btn js-bind mr87">解除绑定</a>
                    </div>
                  </div>
                </div>
                <div class="right"></div>
                <div class="cb"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>


    <!-- 邮箱绑定弹层 -->
    <div class="moco-modal-overlay" style="opacity: 1;" v-if="bindEmailFlag">
      <div class="moco-modal-layer" style="z-index: 904; visibility: visible; left: 716px; top: 336px; width: 488px;">
        <div class="moco-modal-title">
          <div>
            <span>绑定邮箱</span>
            <a @click="showBindEmail()" href="javascript:void(0)" class="moco-icon-close moco-modal-close js-modal-close"></a>
          </div>
        </div>
        <div class="moco-modal-inner clearfix">
          <div class="moco-modal-dialog clearfix" style="display: block;">
            <div class="tpl-changeemail" style="width: 430px;">
              <div class="moco-form-group">
                <label for="inputEmail3" class="moco-control-label">邮箱地址：</label>
                <div class="moco-control-input has-error" style="position: relative;">
                  <input type="email" v-model="bindEmail" placeholder="请输入您的邮箱" class="moco-form-control js-email" data-validate="require-email" id="">
                  <p data-v-b149b182="" class="reSend pa active js-phonecode-box" @click="onSendEmail()"><span data-v-b149b182="" class="js-signin-send">{{sendEmailBtn}}</span></p>
                  <div class="moco-control-tip errorHint color_red">{{bindEmailMsg}}</div>
                </div>
              </div>
              <div class="moco-form-group">
                <label for="inputEmail3" class="moco-control-label">激活码：</label>
                <div class="moco-control-input has-error">
                  <input v-model="activeCode" type="email" placeholder="请输入您的激活码" class="moco-form-control js-email" data-validate="require-email" id="">
                  <div class="moco-control-tip errorHint color_red">{{activeCodeMsg}}</div>
                </div>
              </div>
              <div class="moco-form-group">
                <label for="inputEmail3" class="moco-control-label"></label>
                <div class="moco-control-input">
                  <a @click="clickBindEmail()" href="javascript:void(0)" class="moco-btn moco-btn-blue js-submit">确定</a>
                  <a @click="showBindEmail()" href="javascript:void(0)" class="moco-btn moco-btn-normal js-modal-close">取消</a>
                  <p class="js-gerror g_error"></p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 修改密码弹层 -->
    <div class="moco-modal-overlay" style="z-index: 99999; opacity: 1;" v-if="changePasswordFlag">
      <div class="moco-modal-layer" style="z-index: 901; visibility: visible; left: 716px; top: 336px; width: 488px; ">
        <div class="moco-modal-title"><div>
          <span>修改密码</span>
          <a @click="showPassword()" href="javascript:void(0)" class="moco-icon-close moco-modal-close js-modal-close"></a>
        </div>
        </div>
        <div class="moco-modal-inner clearfix">
          <div class="moco-modal-dialog clearfix" style="display: block;">
            <div class="dialogBox tpl-changePwd" style="width: 430px;">
              <div class="moco-form-group">
                <label for="inputEmail3" class="moco-control-label">输入原密码：</label>
                <div class="moco-control-input">
                  <input v-model="uploadPasswordForm.oldPassword" type="password" placeholder="输入原密码" class="moco-form-control js-prePwd" data-validate="require-password" id="">
                  <div class="moco-control-tip errorHint color_red">{{uploadPasswordMsg.oldPasswordMsg}}</div>
                </div>
              </div>
              <div class="moco-form-group">
                <label for="inputEmail3" class="moco-control-label">输入新密码：</label>
                <div class="moco-control-input">
                  <input v-model="uploadPasswordForm.newPassword" type="password" placeholder="输入新密码" class="moco-form-control js-pwd" data-validate="require-newpassword" id="">
                  <div class="moco-control-tip errorHint color_red">{{uploadPasswordMsg.newPasswordMsg}}</div>
                </div>
              </div>
              <div class="moco-form-group">
                <label for="inputEmail3" class="moco-control-label">确认新密码：</label>
                <div class="moco-control-input">
                  <input v-model="uploadPasswordForm.confirmPassword" type="password" placeholder="确认新密码" class="moco-form-control js-surPwd" data-validate="require-newpassword" id="">
                  <div class="moco-control-tip errorHint color_red">{{uploadPasswordMsg.confirmPasswordMsg}}</div>
                </div>
              </div>
              <div class="moco-form-group">
                <label for="inputEmail3" class="moco-control-label"></label>
                <div class="moco-control-input">
                  <a @click="clickUpdatePassword" href="javascript:void(0);" class="moco-btn moco-btn-blue js-submit">确定</a>
                  <a @click="showPassword()" href="javascript:void(0);" class="moco-btn moco-btn-normal js-modal-close">取消</a>
                  <p class="js-gerror g_error"></p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>


    <!-- 绑定手机号 -->
    <div class="moco-modal-overlay" style="z-index: 99999; opacity: 1;" v-if="changeMobile">
      <div class="moco-modal-layer" style="z-index: 904; visibility: visible; left: 716px; top: 336px;">
        <div class="moco-modal-title">
          <div>
            <span>验证当前绑定手机号</span>
            <a @click="showChangeMobile" href="javascript:void(0)" class="moco-icon-close moco-modal-close js-modal-close"></a>
          </div>
        </div>
        <div class="moco-modal-inner clearfix">
          <div class="moco-modal-dialog clearfix" style="display: block;">
            <div class="yanzheng-phone">
              <div class="keybox">解绑后，您将无法再使用该手机号对此账号进行登陆及找回密码等操作</div>
              <div class="dialogBox yanZhengBox" style="width: 430px;">
                <div class="moco-form-group">
                  <label for="inputEmail3"  class="moco-control-label">当前绑定手机号：</label>
                  <div class="moco-control-input" style="height: 50px;width: 55%;line-height: 40px;"><span class="yanzheng-phone-text">{{usermobile}}</span></div>
                </div>
                <div class="moco-form-group">
                  <label for="inputEmail3"  class="moco-control-label">请输入当前绑定手机号：</label>
                  <div class="moco-control-input" style="width:55%;position: relative;">
                    <input v-model="form.mobile" data-callback="checkPhone" placeholder="请输入手机号" class="moco-form-control js-phoneNumber inpt-error" id="">
                    <p data-v-b149b182="" class="reSend pa active js-phonecode-box" @click="reSendSMS()"><span data-v-b149b182="" class="js-signin-send">{{reSendMsg}}</span></p>
                    <div class="moco-control-tip errorHint color_red">{{formMsg.mobileMsg}}</div>
                  </div>
                </div>
                <div class="moco-form-group">
                  <label for="inputEmail3" class="moco-control-label">验证码：</label>
                  <div class="moco-control-input" style="width:55%;position: relative;">
                    <input v-model="form.code" data-callback="checkPhone" placeholder="请输入验证码" class="moco-form-control js-phoneNumber inpt-error" id="">
                    <div class="moco-control-tip errorHint color_red">{{formMsg.codeMsg}}</div>
                  </div>
                </div>
                <div class="moco-form-group" style="text-align: center;">
                  <label for="inputEmail3" class="moco-control-label btn-box"></label>
                  <div class="moco-control-input">
                    <a @click="clickUpdateMobile()" href="javascript:void(0);" class="moco-btn moco-btn-blue js-phone-submit">确定</a>
                    <a @click="showChangeMobile" href="javascript:void(0);" class="moco-btn moco-btn-normal js-modal-close">取消</a>
                    <p class="js-gerror tl g_error js-error"></p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-if="bindEmailFlag||changePasswordFlag||changeMobile" class="moco-modal-blackout js-moco-modal-cancel" style="z-index: 900; opacity: 0.7; width: 100%; height: 100%; display: block;"></div>

    <!-- 密码修改成功通知-->
    <div class="moco-modal-overlay" style="z-index: 99999; opacity: 1;" v-if="passwordtip">
      <div class="moco-modal-layer" style="z-index: 904; visibility: visible;left: 716px; top: 336px;">
        <div class="moco-modal-inner"><div class="moco-modal-wrap" style="display: block;">
          <p class="moco-modal-content js-modal-content">密码修改成功，请重新登录。</p>
          <div class="moco-modal-btns">
            <a @click="goLogin()" class="moco-btn moco-btn-blue js-modal-submit" onclick="return false" href="javascript:void(0)">
              <span>确定</span>
            </a>
          </div>
        </div>
        </div>
      </div>
    </div>

    <!-- 手机修改成功通知-->
    <div class="moco-modal-overlay" style="z-index: 99999; opacity: 1;" v-if="changeMobiletip">
      <div class="moco-modal-layer" style="z-index: 904; visibility: visible; left: 716px; top: 336px;">
        <div class="moco-modal-inner"><div class="moco-modal-wrap" style="display: block;">
          <p class="moco-modal-content js-modal-content">手机号修改成功。</p>
          <div class="moco-modal-btns">
            <a @click="changeMobiletip = false" class="moco-btn moco-btn-dark js-modal-submit" onclick="return false" href="javascript:void(0)">
              <span>确定</span>
            </a>
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
  import {sendEmail,bindEmail,updatePassword,updateMobile,updateUser,deleteUserProfile} from "@/api/user";
  import {sendSMSByMobile} from "@/api";
  import { isEmail,isPassword,validatenull,isMobile} from '@/util/validate';

  export default {
    name: 'myCourse',
    data() {
      return {
        activeCode: "",
        bindEmail:"",
        bindEmailFlag:false,
        bindEmailMsg:"",
        activeCodeMsg:"",
        sendEmailBtn:"获取激活码",
        reSendTime:0,
        changePasswordFlag:false,
        uploadPasswordForm:{
          oldPassword:"",
          newPassword:"",
          confirmPassword:""
        },
        uploadPasswordMsg:{
          oldPasswordMsg:"",
          newPasswordMsg:"",
          confirmPasswordMsg:"",
        },
        passwordtip:false,
        changeMobile:false,
        changeMobiletip:false,
        reSendMsg:"获取验证码",
        form:{
          mobile:"",
          code:"",
        },
        formMsg:{
          mobileMsg:"",
          codeMsg:"",
        },
        usermobile:""
      }
    },
    beforeDestroy(){
      window.removeEventListener("message", this.msg, false);
    },
    methods: {
      msg(e){
        //更新用户信息
        this.$store.dispatch("GetUserInfo")
      },
      bind(oauthType){

        var iWidth = 800;
        var iHeight = 600;
        var iTop = (window.screen.availHeight-30-iHeight)/2;       //获得窗口的垂直位置;
        var iLeft = (window.screen.availWidth-10-iWidth)/2;           //获得窗口的水平位置;
        let url = "/api/front/oauth/login/"+oauthType+"?userId="+this.studentInfo.id+"&type=bind";
        window.open( url, "_blank", 'height=' + iHeight + ',innerHeight=' + iHeight + ',width=' + iWidth + ',innerWidth=' + iWidth + ',top=' + iTop + ',left=' + iLeft + ', toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no, titlebar=yes, alwaysRaised=yes');
      },
      unbind(oauthType){
        let param;
        if(oauthType == "weibo"){
          param = {
            "userid":this.studentInfo.id,
            "profiletype":"weibo"
          }
        }
        if(oauthType == "wechat"){
          param = {
            "userid":this.studentInfo.id,
            "profiletype":"wxPC"
          }
        }
        if(oauthType == "qq"){
          param = {
            "userid":this.studentInfo.id,
            "profiletype":"qqPC"
          }
        }
        deleteUserProfile(param).then(res=>{
          //更新用户信息
          this.$store.dispatch("GetUserInfo")
        })
      },
      clickUpdateMobile(){
        this.formMsg.mobileMsg = ""
        this.formMsg.codeMsg = ""
        if(this.form.mobile == this.studentInfo.mobile) {
          this.formMsg.mobileMsg = "不可使用相同的手机号";
          return;
        }
        if(validatenull(this.form.code)) {
          this.formMsg.codeMsg = "请输入验证码";
          return;
        }
        let that = this
        updateMobile(this.form).then(res =>{

          //更新本地缓存
          this.$store.dispatch("GetUserInfo").then(res =>{
            that.changeMobile = false
            that.changeMobiletip = true
            let mobile = that.studentInfo.mobile;
            that.usermobile = mobile.substr(0,3)+"******"+mobile.substr(9,2)
          })
        },error =>{
          this.formMsg.codeMsg = error.message;
        })
      },
      reSendSMS(){
        this.formMsg.mobileMsg = ""
        this.formMsg.codeMsg = ""
        if(this.reSendTime == 0){
          //手机号规则验证
          if(validatenull(this.form.mobile)) {
            this.formMsg.mobileMsg = "请输入手机号";
            return;
          }
          if(!isMobile(this.form.mobile)) {
            this.formMsg.mobileMsg = "请输入正确的手机号";
            return;
          }
          if(this.form.mobile == this.studentInfo.mobile) {
            this.formMsg.mobileMsg = "不可使用相同的手机号";
            return;
          }
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
      showChangeMobile(){
        this.form.mobile = "";
        this.form.code = "";
        this.changeMobile = !this.changeMobile

      },
      showPassword(){
        this.uploadPasswordForm.oldPassword = ""
        this.uploadPasswordForm.newPassword = ''
        this.uploadPasswordForm.confirmPassword = ''
        this.changePasswordFlag = !this.changePasswordFlag

      },
      showBindEmail(){
        this.bindEmailMsg="";
        this.activeCodeMsg = "";
        this.bindEmailFlag = !this.bindEmailFlag
      },
      onSendEmail(){
        if(this.bindEmail == ""){
          this.bindEmailMsg = "请输入邮箱";
          return;
        }
        if(!isEmail(this.bindEmail)){
          this.bindEmailMsg = "请输入正确的邮箱";
          return;
        }
        this.reSendTime = 60
        let param = {
          "email":this.bindEmail,
        }
        sendEmail(param).then(res=>{
          this.bindEmailMsg="";
          this.activeCodeMsg = "";
          let that = this
          let timer = setInterval(function () {
            that.reSendTime = that.reSendTime-1
            that.sendEmailBtn = "重新发送  "+ that.reSendTime;
            if(that.reSendTime == 0){
              clearInterval(timer);
              that.sendEmailBtn = "重新发送";
            }
          },1000)
        },error =>{
          this.bindEmailMsg = error.message;
        })
      },
      clickBindEmail(){
        if(this.activeCode == ""){
          this.activeCodeMsg = "请输入激活码";
          return;
        }
        let param = {
          "email":this.bindEmail,
          "activeCode": this.activeCode
        }
        bindEmail(param).then(res=>{
            //更新本地缓存
            this.$store.dispatch("GetUserInfo").then(res =>{
              this.showBindEmail()
            })
            this.activeCodeMsg = res.data.msg;
        },error =>{
          this.activeCodeMsg = error.message;
        })
      },
      clickUpdatePassword(){
        this.uploadPasswordForm.oldPasswordMsg = ""
        this.uploadPasswordForm.newPasswordMsg = ''
        this.uploadPasswordForm.confirmPasswordMsg = ''
        if(this.uploadPasswordForm.oldPassword == ''){
          this.uploadPasswordMsg.oldPasswordMsg = "请输入旧密码";
          return;
        }
        if(this.uploadPasswordForm.newPassword == ''){
          this.uploadPasswordMsg.newPasswordMsg = "请输入新密码";
          return;
        }
        if(isPassword(this.uploadPasswordForm.newPassword)){
          this.uploadPasswordMsg.newPasswordMsg = "请输入8-20位字母、数字或字符，至少包含两种！";
          return;
        }
        if(this.uploadPasswordForm.confirmPassword == ''){
          this.uploadPasswordMsg.confirmPasswordMsg = "请输入确认新密码";
          return;
        }
        if(this.uploadPasswordForm.confirmPassword != this.uploadPasswordForm.newPassword){
          this.uploadPasswordMsg.confirmPasswordMsg = "两次输入密码不一致";
          return;
        }
        updatePassword(this.uploadPasswordForm).then(res=>{
          this.changePasswordFlag = !this.changePasswordFlag
          //弹层给出提示
          this.passwordtip = true
          //退出操作
          this.logOut()
        },error =>{
          this.uploadPasswordMsg.confirmPasswordMsg = error.message;
        })
      },
      logOut(){
        this.$store.dispatch("LogOut")
      },
      goLogin(){
        this.passwordtip = false
        this.$router.push({ path: '/user/login' }).catch(data => {  });
      }
    },
    computed: {
      ...mapGetters(["userInfo","studentInfo","isLogin","website"]),
    },
    mounted:function() {
      let mobile = this.studentInfo.mobile;
      this.usermobile = mobile.substr(0,3)+"******"+mobile.substr(9,2)
      window.addEventListener("message", this.msg, false);
    }
  }
</script>

<style scoped>
  .moco-control-label {
    width: 22%!important;
  }
  .moco-control-input {
    width: 78%!important;
    position: relative;
  }
  .reSend {
    top: 12px;
    right: 14px;
    font-size: 14px;
    color: #9199a1;
    line-height: 14px;
  }
</style>
