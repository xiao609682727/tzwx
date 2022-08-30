<template>
  <div>
      <div class="dialogWrap" >
          <div class="dialog-ele">
              <div class="of bg-fff">
                  <div id="dcWrap" class="mt20 mb20 ml20 mr20 ">
                      <div class="d-tips-10">
                          <ul class="l-r-w-Inpt">
                              <li>
                                  <label class="vam">卡号：</label>
                                  <input type="text" placeholder="请输入卡号" id="cardCourseCode" maxlength="20" name="" value="" class="lTxt" v-model="form.cardCode">
                              </li>
                              <li class="mt20">
                                  <label class="vam">密码：</label>
                                  <input type="password" placeholder="请输入密码" id="cardCoursePassword" maxlength="20" name="" value="" class="lTxt" v-model="form.cardCodePassword">
                              </li>
                              <li class="mt20 btn-list tac">
                                  <span class="login-btn">
                                      <input type="button" value="激 活" @click="courseCardActive()">
                                  </span>
                              </li>
                          </ul>
                      </div>
                  </div>
              </div>
          </div>
      </div>
  </div>
</template>

<script>
    import {courseCardActivation} from "@/api/user";
export default {
  name: 'courseCardLayer',
  data () {
    return {
      isReg: false,
      name: '',
      password: '',
      repeat: '',
        form:{
            cardCode:"",
            cardCodePassword:"",
        }
    }
  },
    props: {
        layerid: {//自动注入的layerid
            type: String,
            default: ''
        }
    },components:{

  },
  methods: {
      courseCardActive(){//学习卡激活
            if(this.form.cardCode==""){
                this.$layer.alert(
                    "请输入卡号",
                    {
                        shade: true,
                        title: "提示"
                    },
                    laeryid => {
                        this.$layer.close(laeryid);
                    }
                );
                return;
            }
          if(this.form.cardCodePassword==""){
              this.$layer.alert(
                  "请输入密码",
                  {
                      shade: true,
                      title: "提示"
                  },
                  laeryid => {
                      this.$layer.close(laeryid);
                  }
              );
              return;
          }
          let params = {
              cardCode:this.form.cardCode,
              cardCodePassword:this.form.cardCodePassword,
          }
          courseCardActivation(params).then(res =>{

              if("cardError"==res.data.data){
                  this.$layer.alert(
                      "卡号和密码错误，请重新输入！",
                      {
                          shade: true,
                          title: "提示"
                      },
                      laeryid => {
                          this.$layer.close(laeryid);
                      }
                  );
                  return;
              }if("cardCodeIsNull"==res.data.data){
                  this.$layer.alert(
                      "请输入卡号！",
                      {
                          shade: true,
                          title: "提示"
                      },
                      laeryid => {
                          this.$layer.close(laeryid);
                      }
                  );
                  return;
              }if("cardCodePasswordIsNull"==res.data.data){
                  this.$layer.alert(
                      "请输入密码！",
                      {
                          shade: true,
                          title: "提示"
                      },
                      laeryid => {
                          this.$layer.close(laeryid);
                      }
                  );
                  return;
              }if("cardUse"==res.data.data){
                  this.$layer.alert(
                      "卡已激活！",
                      {
                          shade: true,
                          title: "提示"
                      },
                      laeryid => {
                          this.$layer.close(laeryid);
                      }
                  );
                  return;
              }if("cardUnBegin"==res.data.data){
                  this.$layer.alert(
                      "卡有效期未开始！",
                      {
                          shade: true,
                          title: "提示"
                      },
                      laeryid => {
                          this.$layer.close(laeryid);
                      }
                  );
                  return;
              }if("cardEnd"==res.data.data){
                  this.$layer.alert(
                      "卡有效期已结束！",
                      {
                          shade: true,
                          title: "提示"
                      },
                      laeryid => {
                          this.$layer.close(laeryid);
                      }
                  );
                  return;
              }if("cardInvalid"==res.data.data){
                  this.$layer.alert(
                      "卡已作废！",
                      {
                          shade: true,
                          title: "提示"
                      },
                      laeryid => {
                          this.$layer.close(laeryid);
                      }
                  );
                  return;
              }
              if("success"==res.data.data){

                  this.$layer.msg(
                      "激活成功",
                      {
                          time: 5
                      },
                      laeryid => {
                          this.$layer.close(laeryid);
                      }
                  );
                  this.$layer.close(this.layerid);
                  return;
              }

          })
      }
  }
}
</script>

<style scoped>

    .dialog-ele {
        border-radius: 6px;
        overflow: hidden;
    }
    .bg-fff {
        background-color: #fff;
    }
    .of {
        overflow: hidden;
    }
    .l-r-w-Inpt li label {
        color: #666;
        display: inline-block;
        font-size: 14px;
        text-align: right;
        vertical-align: middle;
    }
    .l-r-w-Inpt li textarea.lTxt, .l-r-w-Inpt li input.lTxt, .l-r-w-Inpt li select.lTxt {
        background: #fff none repeat scroll 0 0;
        border: 1px solid #ddd;
        box-shadow: 1px 1px 4px rgba(0, 0, 0, 0.05) inset;
        color: #666;
        height: 32px;
        line-height: 32px;
        padding: 0 10px;
        width: 260px;
        vertical-align: middle;
    }
    .l-r-w-Inpt .btn-list {
        margin-top: 30px;
    }
    .tac {
        text-align: center;
    }
    .login-btn input {
        background: rgba(0, 0, 0, 0) none repeat scroll 0 0;
        border: medium none;
        color: #fff;
        font: 16px/28px "Hiragino Sans GB","Microsoft YaHei","΢���ź�";
        height: 28px;
        text-align: center;
        width: 160px;
    }
    .login-btn {
        background-color: #0091ff;
        border-color: #0091ff;
        border-radius: 4px;
        overflow: hidden;
        display: inline-block;
        height: 28px;
        text-align: left;
        width: 160px;
        border-width: 1px;
        border-style: solid;
    }
</style>
