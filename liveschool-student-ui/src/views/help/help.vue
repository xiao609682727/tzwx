<template>
  <div>
    <!-- 头部开始 -->
    <common-header></common-header>
    <!-- 头部结束 -->
    <!-- 主干开始 -->
    <div id="main">
      <div id="help-title" v-bind:style="{backgroundImage:'url(' + userHelpImgList[0].imageUrl + ')',backgroundRepeat:'no-repeat', backgroundPosition:'center', backgroundSize:'cover'}">
        <div class="wrap">
          <h2>帮助中心</h2>
        </div>
      </div>
      <div class="help-layout clearfix">
        <div class="help-aside">
          <div class="nav-line" style="position: relative; top: 0px;">
            <h2 class="title">常见问题分类</h2>
            <div class="class-box" v-for="help in list" :key="help.id">
              <h3 class="class-title active"><span>{{help.name}}</span><i class="icon-drop_right "></i></h3>
              <ul class="menus active">
                <li class="" :class="{'active': id == child.id}" v-for="child in help.children" :key="child.id" @click="clickHelp(child)"><a href="javascript:void(0)" data-status="1" :title="child.name">{{child.name}}</a></li>
              </ul>
            </div>
          </div>
        </div>
        <div class="help-main">

          <div class="help-detail">
            <div class="detail-content">
              <div class="detail-title" style="border-bottom: 1px solid #d8d8d8;font-size: 18px;">{{help.name}}</div>
            </div>
            <div v-html="help.content">
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 主干结束 -->
    <!-- 底部开始 -->
    <common-footer></common-footer>
    <!-- 底部结束 -->
  </div>
</template>

<script>
  import commonHeader from '@/views/common/header'
  import commonFooter from '@/views/common/footer'
  import {helpMenu} from "@/api";
  import { validatenull} from '@/util/validate';
  import {getBannerList} from "@/api";
  import { setStore, getStore } from '@/util/store';
  export default {
    name: 'help',
    data() {
      return {
        list:[],
        id:"",
        userHelpImgList:getStore({ name: 'userHelpImgList' }) ||[],
        help:{}
      }
    },
    methods: {
      init(){
        this.id = this.$route.params.id
        let flag = true;
        helpMenu().then(res =>{
          this.list = res.data.data
          this.list.forEach((item,index)=>{
            if(item.children){
              item.children.forEach((item,index)=>{
                //判断当没有传入id的时候
                if(validatenull(this.id)){
                  if(flag){
                    flag = false
                    this.help = item
                    this.id = item.id
                  }
                }
                if(this.id == item.id){
                  this.help = item
                }
              })
            }
          })
        })

        //查询广告位
        if(this.userHelpImgList.length == 0){
          getBannerList("6").then(res => {
            this.userHelpImgList = res.data.data;
          setStore({ name: 'userHelpImgList', content: res.data.data,type: 'session' })
        });
        }
      },
      clickHelp(help){
        this.$router.push('/help/'+help.id)
      }
    },
    mounted:function() {
      this.init()
    },
    computed: {
    },components:{
      commonHeader,commonFooter
    }
  }
</script>

<style scoped>

</style>
