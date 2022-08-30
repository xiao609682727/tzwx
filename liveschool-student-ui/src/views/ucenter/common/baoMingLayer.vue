<template>
  <div>
      <div class="dialogWrap" >
          <div class="dialog-ele">
              <div class="of bg-fff">
                  <div id="dcWrap" class="mt20 mb20 ml20 mr20 ">
                      <div class="d-tips-10">
                          <ul class="l-r-w-Inpt">
                              <li>
                                  <label class="vam">姓名：</label>
                                  <input type="text" placeholder="请输入姓名" id="userName" maxlength="20" name="" value="" class="lTxt" v-model="form.userName">
                              </li>
                              <li class="mt20">
                                  <label class="vam">手机：</label>
                                  <input  type="text" placeholder="请输入手机" id="mobile" maxlength="11" name="" value="" class="lTxt" v-model="form.mobile">
                              </li>
                              <li class="mt20 btn-list tac">
                                  <span class="login-btn">
                                      <input type="button" value="报 名" @click="courseCardActive()">
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
    import {createCourseOrder,courseFaceTeacherBaoMing,createFreeCourseOrder} from "@/api/user";
    import {isMobile} from '@/util/validate';
export default {
  name: 'courseCardLayer',
  data () {
    return {
      isReg: false,
      name: '',
      password: '',
      repeat: '',
        form:{
          userName:"",
          mobile:"",
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
      courseCardActive(){
          if (this.form.userName == "") {
            this.$layer.alert(
              "请输入姓名",
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
          if (this.form.mobile == "") {
            this.$layer.alert(
              "请输入手机号",
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
        if(!isMobile(this.form.mobile)) {
          this.$layer.alert(
            "请输入正确的手机号",
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
        let courseId = this.$parent.courseInfo.id;
        let param = {
          courseId: courseId,
          userName:this.form.userName,
          mobile:this.form.mobile,
        }
        courseFaceTeacherBaoMing(param).then(res =>{
          console.log(res.data.data);
          this.$layer.close(this.layerid);
          if(res.data.data==2){
            this.$parent.statie=2;
            this.$layer.alert(
              "报名成功！",
              {
                shade: true,
                title: "提示"
              },
              laeryid => {
                this.$layer.close(laeryid);
                this.$forceUpdate();
              }
            );
            return;
          }else {
             if(this.$parent.courseInfo.currentPrice == "0.00"){
               //如果是免费直接生成订单，完成报名
              let params = {
                courseId: this.$parent.courseInfo.id,
                payType: "",
                reqChannel:"web"
              }
              createFreeCourseOrder(params).then(res=>{
                this.$parent.statie=2;
                this.$layer.alert(
                  "报名成功！",
                  {
                    shade: true,
                    title: "提示"
                  },
                  laeryid => {
                    this.$layer.close(laeryid);
                    this.$forceUpdate();
                  }
                );
              })
            }else {
               //如果不是免费进入购物车进行购买。
               let paramb = {
                 courseId: this.$parent.courseInfo.id,
                 payType: "",
                 reqChannel:"web"
               }
               createCourseOrder(paramb).then(res =>{
                 let order = res.data.data
                 this.$router.push({ path: '/course/payCenter/'+order.id })
               })
             }
          }
        })


          console.log( this.$parent.courseInfo.id)
      }
  },
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
