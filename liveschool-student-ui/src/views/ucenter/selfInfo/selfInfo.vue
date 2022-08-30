<template>
  <div class="u-container">
    <div class="setting-right-wrap wrap-boxes settings">

      <div class="formBox">
        <div id="setting-profile" class="setting-wrap setting-profile">
          <div class="common-title">
            个人信息
            <a @click="showEdit()" href="javascript: void(0);" class="pull-right js-edit-info">
              <i class="icon-note"></i>
              编辑
            </a>
          </div>
          <div class="line"></div>
          <div class="info-wapper">
            <div class="clearfix ">
              <div class="tpl-avator">
                  <div class="avator-mode" style="width: 142px;height: 142px">
                    <img style="cursor:pointer"   class="avator-img" id="js-portrait" onerror="this.src='/static/img/user-avatar.png'" :src="studentInfo.headImg" data-portrait="533e4c0500010c7602000200" width="142" height="142">
                  </div>
              </div>
            </div>
            <div class="info-box clearfix">
              <label class="pull-left">昵称</label>
              <div class="pull-left" v-if="studentInfo.showName ==''">未设置</div>
              <div class="pull-left" v-if="studentInfo.showName !=''">{{studentInfo.showName}}</div>
            </div>
            <div class="info-box clearfix">
              <label class="pull-left">真实姓名</label>
              <div class="pull-left" v-if="studentInfo.realName ==''">未设置</div>
              <div class="pull-left" v-if="studentInfo.realName !=''">{{studentInfo.realName}}</div>
            </div>
            <div class="info-box clearfix">
              <label class="pull-left">身份证号</label>
              <div class="pull-left" v-if="studentInfo.idCardNo ==''">未设置</div>
              <div class="pull-left" v-if="studentInfo.idCardNo !=''">{{studentInfo.idCardNo}}</div>
            </div>
      <!--      <div class="info-box clearfix">
              <label class="pull-left">职位</label>
              <div class="pull-left">未设置</div>
            </div>-->
<!--            <div class="info-box clearfix">-->
<!--              <label class="pull-left">城市</label>-->
<!--              <div class="pull-left" v-if="provinceName == ''">-->
<!--                未设置-->
<!--              </div>-->
<!--              <div class="pull-left" v-if="provinceName != ''">-->
<!--                {{provinceName}} {{cityName}}  {{areaName}}-->
<!--              </div>-->
<!--            </div>-->
            <div class="info-box clearfix">
              <label class="pull-left">性别</label>
              <div class="pull-left" v-if="studentInfo.sex == 0||studentInfo.sex == 1">
                男
              </div>
              <div class="pull-left" v-if="studentInfo.sex == 2">
                女
              </div>
            </div>
            <div class="info-box clearfix">
              <label class="pull-left">个性签名</label>
              <div class="pull-left" v-if="studentInfo.userInfo !=''">{{studentInfo.userInfo}}</div>
              <div class="pull-left" v-if="studentInfo.userInfo ==''">未设置</div>
            </div>
          </div>
        </div>
      </div>

      <div v-if="tancengFlag" class="moco-modal-overlay" style="z-index: 99999; opacity: 1;"><div class="moco-modal-layer" style="z-index: 901; visibility: visible;top:50%; left:50%;width: 500px;">
        <div class="moco-modal-title">
          <div>
            <span>编辑个人信息</span>
            <a @click="showEdit()" href="javascript:void(0)" class="moco-icon-close moco-modal-close js-modal-close"></a>
          </div>
        </div>
        <div class="moco-modal-inner">
          <div class="moco-modal-dialog" style="display: block;">
            <div class="edit-info">
              <form class="js-wapper-form" id="profile">
                <div class="tpl-avator">
                <div class="moco-form-group avator-wapper">
                  <div class="avator-mode">
                    <img style="cursor:pointer"  @click="onPickFile()"  class="avator-img" id="js-portrait" onerror="this.src='/static/img/user-avatar.png'" :src="uploadImg" data-portrait="533e4c0500010c7602000200" width="192" height="192">
                  </div>
                  <div class="clearfix w140">
                    <form target="uploadtarget" action="postpic" method="post" enctype="multipart/form-data" class="pull-right">
                      <input ref="fileInput" @change="uploadFile" class="hide" type="file" title="上传头像" name="fileField" id="upload" accept="image/gif,image/jpeg,image/jpg,image/png">
                      <input class="hide" type="hidden" name="type" value="1">
                    </form>
                  </div>
                </div>
                </div>
                <div class="moco-form-group">
                  <label for="" class="moco-control-label">昵称：</label>
                  <div class="moco-control-input">
                    <input type="text" v-model="studentInfo.showName" name="nickname" id="nick" autocomplete="off" data-validate="require-nick" class="moco-form-control" value="慕瓜9118686" placeholder="请输入昵称">
                    <div class="rlf-tip-wrap errorHint color-red"></div>
                  </div>
                </div>
                <div class="moco-form-group">
                  <label for="" class="moco-control-label">真实姓名：</label>
                  <div class="moco-control-input">
                    <input type="text" v-model="studentInfo.realName" name="realName" id="realName" autocomplete="off" data-validate="require-nick" class="moco-form-control" value="" placeholder="请输入真实姓名">
                    <div class="rlf-tip-wrap errorHint color-red"></div>
                  </div>
                </div>
                <div class="moco-form-group">
                  <label for="" class="moco-control-label">身份证号：</label>
                  <div class="moco-control-input">
                    <input type="text" v-model="studentInfo.idCardNo" name="idCardNo" id="idCardNo" autocomplete="off" data-validate="require-nick" class="moco-form-control" value="" placeholder="请输入身份证号">
                    <div class="rlf-tip-wrap errorHint color-red"></div>
                  </div>
                </div>
                <!--<div class="moco-form-group">
                  <label for="" class="moco-control-label">职位：</label>
                  <div class="moco-control-input">
                    <select class="moco-form-control rlf-select" name="job" hidefocus="true" id="job" data-validate="require-select">
                      <option value="">请选择职位</option>
                      <option value="18">Python工程师</option>
                      <option value="1">页面重构设计</option>
                      <option value="6">Web前端工程师</option>
                      <option value="5">JS工程师</option>
                      <option value="8">PHP开发工程师</option>
                      <option value="11">JAVA开发工程师</option>
                      <option value="7">移动开发工程师</option>
                      <option value="9">软件测试工程师</option>
                      <option value="10">Linux系统工程师</option>
                      <option value="3">产品经理</option>
                      <option value="15">数据库工程师</option>
                      <option value="21">软件工程师</option>
                      <option value="4">UI设计师</option>
                      <option value="2">交互设计师</option>
                      <option value="17">CG影视动画师</option>
                      <option value="16">全栈工程师</option>
                      <option value="20">算法工程师</option>
                      <option value="26">架构师</option>
                      <option value="25">学生</option>
                    </select>
                    <div class="rlf-tip-wrap errorHint color-red"></div>
                  </div>
                </div>-->
<!--                <div class="moco-form-group wlfg-wrap">-->
<!--                  <label for="" class="moco-control-label">所在地区：</label>-->
<!--                  <div class="moco-control-input profile-address">-->
<!--                    <select v-model="studentInfo.province" id="province-select" @change="getAreaList(2)" class="moco-form-control" hidefocus="true">-->
<!--                      <option value="-1">省</option>-->
<!--                      <option :value="p.id" v-for="p in province" :key="p.id">{{p.name}}</option>-->
<!--                    </select>-->
<!--                    <select v-model="studentInfo.city" class="moco-form-control" @change="getAreaList(3)" id="city-select" hidefocus="true">-->
<!--                      <option value="-1">市</option>-->
<!--                      <option :value="c.id" v-for="c in city" :key="c.id">{{c.name}}</option>-->
<!--                    </select>-->
<!--                    <select v-model="studentInfo.area" class="moco-form-control mr0" id="area-select" hidefocus="true">-->
<!--                      <option value="-1">区县</option>-->
<!--                      <option :value="a.id" v-for="a in area" :key="a.id">{{a.name}}</option>-->
<!--                    </select>-->
<!--                    <div class="rlf-tip-wrap errorHint color-red"></div>-->
<!--                  </div>-->
<!--                </div>-->
                <div class="moco-form-group wlfg-wrap">
                  <label for="" class="moco-control-label h16 lh16">性别：</label>
                  <div class="moco-control-input rlf-group rlf-radio-group">
<!--                    <label><input v-model="studentInfo.sex" type="radio" hidefocus="true" value="0" name="sex">保密</label>-->
                    <label><input v-model="studentInfo.sex" type="radio" hidefocus="true" value="1" checked="checked" name="sex">男</label>
                    <label><input v-model="studentInfo.sex" type="radio" hidefocus="true" value="2" name="sex">女</label>
                    <div class="rlf-tip-wrap errorHint color-red"></div>
                  </div>
                </div>
                <div class="moco-form-group wlfg-wrap">
                  <label for="" class="moco-control-label">个性签名：</label>
                  <div class="moco-control-input">
                    <div class="pr">
                      <textarea v-model="studentInfo.userInfo" name="aboutme" id="aboutme" rows="5" maxlength="30" class="noresize js-sign moco-form-control"></textarea>
                    </div>
                  </div>
                </div>
                <div class="wlfg-wrap clearfix">
                  <label class="moco-control-label" for=""></label>
                  <div class="moco-control-input">
                    <a @click="save()" href="javascript:void(0);" id="profile-submit" class="moco-btn moco-btn-blue marR10">确定</a>
                    <a @click="showEdit()" href="javascript:void(0);" class="moco-btn moco-btn-normal js-modal-close">取消</a>
                    <p class="js-gerror g_error"></p>
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
      </div>

      <div v-if="tancengFlag" class="moco-modal-blackout js-moco-modal-cancel" style="z-index: 900; opacity: 0.7; width: 100%; height: 100%; display: block;"></div>

    </div>
  </div>
</template>

<script>
  import { mapGetters } from "vuex";
  import {getAreaList,updateUser,uploadFile} from "@/api/user";

  export default {
    name: 'myCourse',
    data() {
      return {
        tancengFlag:false,
        province:[],
        city:[],
        area:[],
        provinceName:"",
        cityName:"",
        areaName:"",
        uploadImg:""
      }
    },
    methods: {
      uploadFile(event){
        event.preventDefault();
        let fd = new FormData()              //内置方法new FormData()  新建一个表格
        const files = event.target.files
        fd.append('file',files[0])
        uploadFile(fd).then(res =>{
          this.uploadImg = res.data.data.url
        })
      },
      onPickFile () {
        this.$refs.fileInput.click()
      },
      showEdit(){
        this.tancengFlag = !this.tancengFlag
      },
      init(){
        let that = this
        this.uploadImg = this.studentInfo.headImg
        let params = {
          arealevel:"1"
        }
        getAreaList(params).then(res =>{
          that.province = res.data.data
          let p=that.province.filter(function (p) {
            return p.id == that.studentInfo.province;
          });
          if(p.length >0){
            that.provinceName = p[0].name
          }
        })
        //获取城市
        this.getAreaList(2)
        //获取区域
        this.getAreaList(3)

      },
      getAreaList(arealevel){
        let that = this
        if(arealevel == 2){
          let params = {
            parent:this.studentInfo.province,
            arealevel:arealevel
          }
          getAreaList(params).then(res =>{
            that.city = res.data.data
            let p=that.city.filter(function (p) {
              return p.id == that.studentInfo.city;
            });
            if(p.length >0){
              that.cityName = p[0].name
            }

          })
        }
        if(arealevel == 3){
          let params = {
            parent:that.studentInfo.city,
            arealevel:arealevel
          }
          getAreaList(params).then(res =>{
            that.area = res.data.data
            let p=that.area.filter(function (p) {
              return p.id == that.studentInfo.area;
            });
            if(p.length >0){
              that.areaName = p[0].name
            }

          })
        }
      },
      save(){
        let that = this
        this.studentInfo.headImg = this.uploadImg
        //判断邮箱 如果未 '' 则设置未null
        if(this.studentInfo.email == ''){
          this.studentInfo.email = null
        }
        let si = this.studentInfo;
        si.headImg = this.uploadImg
        updateUser(si).then(res =>{
          //更新本地缓存
          this.$store.dispatch("GetUserInfo").then(res =>{
            //获取省份
            let p=that.province.filter(function (p) {
              return p.id == that.studentInfo.province;
            });
            if(p.length >0){
              that.provinceName = p[0].name
            }
            //获取市
            let c=that.city.filter(function (p) {
              return p.id == that.studentInfo.city;
            });
            if(c.length >0){
              that.cityName = c[0].name
            }
            //获取区域
            let a=that.area.filter(function (p) {
              return p.id == that.studentInfo.area;
            });
            if(a.length >0){
              that.areaName = a[0].name
            }

            this.showEdit()
          })

        })
      }
    },
    computed: {
      ...mapGetters(["userInfo","studentInfo","isLogin"]),
    },
    mounted:function() {
      this.init()
    },
  }
</script>

<style scoped>
  .moco-control-label {
     width: 20%!important;
  }
  .moco-control-input {
     width: 80%!important;
    position: relative;
  }
  .wlfg-wrap{
    margin-top: 10px;
  }
</style>
