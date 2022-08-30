<template>
  <basic-container>
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="qq快捷登录配置" name="qqlogin">
        <smsConfig configType="qqlogin" ></smsConfig>
      </el-tab-pane>
      <el-tab-pane label="微信快捷登录配置" name="wechatlogin">
        <smsConfig configType="wechatlogin" ></smsConfig>
      </el-tab-pane>
      <el-tab-pane label="微博快捷登录配置" name="weibologin">
        <smsConfig configType="weibologin" ></smsConfig>
      </el-tab-pane>
      <el-tab-pane label="微信公众平台配置" name="wechatmp">
        <wechatMPConfig configType="wechatmp" ></wechatMPConfig>
      </el-tab-pane>
      <el-tab-pane label="注册配置" name="webregister">
        <smsConfig configType="webregister" ></smsConfig>
      </el-tab-pane>
      <el-tab-pane label="登录配置" name="networklogin">
        <smsConfig configType="networklogin" ></smsConfig>
      </el-tab-pane>
    </el-tabs>
  </basic-container>
</template>

<script>
  import {mapGetters} from "vuex";
  import smsConfig from "./smsConfig";
  import wechatMPConfig from "./wechatMPConfig";

  export default {
    data() {
      return {
        activeName: 'qqlogin',
      };
    },
    computed: {
      ...mapGetters(["permission"])
    },
    methods: {
      handleClick(tab, event) {
        this.activeName = tab.name;
      }
    },
    components:{
      'smsConfig':smsConfig,
      'wechatMPConfig':wechatMPConfig
    },
  };
</script>

<style>
</style>
