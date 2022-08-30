<template>
  <basic-container>
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="支付宝支付配置" name="alipay">
        <smsConfig configType="alipay" ></smsConfig>
      </el-tab-pane>
      <el-tab-pane label="微信支付配置" name="wechatpay">
        <smsConfig configType="wechatpay" ></smsConfig>
      </el-tab-pane>
      <el-tab-pane label="微信app支付配置" name="wechatapppay">
        <smsConfig configType="wechatapppay" ></smsConfig>
      </el-tab-pane>
    </el-tabs>
  </basic-container>
</template>

<script>
  import {mapGetters} from "vuex";
  import smsConfig from "./smsConfig";

  export default {
    data() {
      return {
        activeName: 'alipay',
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
    },
  };
</script>

<style>
</style>
