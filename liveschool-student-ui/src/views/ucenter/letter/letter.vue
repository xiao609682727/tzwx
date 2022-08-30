<template>
  <div class="u-container">
    <div class="setting-right-wrap wrap-boxes settings">

      <div class="formBox">
        <div id="setting-profile" class="setting-wrap setting-profile">
          <div class="common-title">
            消息中心
          </div>
          <div class="line"></div>
          <div class="">
            <div class="u-r-all-box">
              <div class="u-sys-news" v-if="msgList.length > 0">
                <ul class="u-sys-list">

                  <li id="del528" v-for="msg in msgList" :key="msg.id">
                    <div>
                      <section class="u-s-l-txt">
                        <span class="fsize16" style="font-weight: 700">系统消息</span>
                        <span class="fsize14 c-999 f-fM ml20" v-html="msg.createTime"></span>
<!--                        <em class="wd-ico">-->
<!--                          <img src="https://wx.inxedu.com/static/inxweb/img/wd-bg-u.png">-->
<!--                        </em>-->
                        <a href="javascript:void (0)" @click="remove(msg.id)" style="display: block" class="u-s-l-sc-ico" title="删除" onfocus="this.blur()">
                          删除
                        </a>
                      </section>
                      <section class="clearfix hLh30">
                        <span class="fl">
                          <tt class="fsize14 c-999 f-fM" v-html="msg.content"></tt>
                        </span>
                      </section>
                    </div>
                  </li>
                </ul>
              </div>
              <page-component :page="page" @goto="goto" v-show="msgList.length > 0"></page-component>
              <div class="nodata" v-if="msgList.length == 0">
                <p><i class="imv2-error_c"></i></p>
                <p>暂无数据</p>
              </div>
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
  import {msgreceiveList,msgReceiveUpdateState,removeMsgreceive} from "@/api";
  import pageComponent from '@/views/common/pageComponent'
  import { mapGetters } from "vuex";


  export default {
    name: 'myCourse',
    data() {
      return {
        isReg: false,
        name: '',
        password: '',
        repeat: '',
        msgList:[],
        current:1,
        size:20,
        page:{},

      }
    },mounted:function() {
      this.getMsgList(this.current,this.size);
    },
    components: {
      pageComponent
    },
    methods: {
      remove(id){
        this.$layer.confirm(
          "确定要删除信息吗？",
          {
            title: "删除确认",
            btn: ["确定", "取消"],
          },
          layerid => {
            // this.$layer.msg("执行了删除");
            removeMsgreceive(id).then(res=>{
              this.getMsgList(1,this.size);
            });
            this.$layer.close(layerid);
          },
          layerid => {
            this.$layer.close(layerid);
          }
        )
      },
      goto(i){
        this.current = i
        this.getMsgList(i,this.size);
      },
      getMsgList(current,size){
        if (typeof(this.studentInfo.id) == "undefined"){
          return;
        }
        //更新站内信状态
        msgReceiveUpdateState(this.studentInfo.id).then(res =>{
          this.$store.dispatch("updateLetterCount", this.studentInfo)
        })
        let params = {
          "current":current,
          "size":size,
          "receivingCusid":this.studentInfo.id
        }
        msgreceiveList(params).then(res =>{
          this.page = res.data.data
          this.msgList = res.data.data.records

        })
      },
    },
    computed: {
      ...mapGetters(["studentInfo"]),
    },
    watch: {
      studentInfo: function (val, oldVal) {
        this.getMsgList(this.current,this.size);
      }
    },

  }
</script>

<style scoped>


</style>
