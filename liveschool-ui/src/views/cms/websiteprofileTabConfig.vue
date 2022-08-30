<template>
  <basic-container>
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="站点配置" name="web">
        <smsConfig configType="web" ></smsConfig>
      </el-tab-pane>
      <el-tab-pane label="h5站点配置" name="h5web">
        <smsConfig configType="h5web" ></smsConfig>
      </el-tab-pane>
      <el-tab-pane label="h5微信分享配置" name="h5WechatWeb">
        <smsConfig configType="h5WechatWeb" ></smsConfig>
      </el-tab-pane>
    </el-tabs>
  </basic-container>
</template>

<script>
  import {mapGetters} from "vuex";
  import smsConfig from "../config/smsConfig";

  export default {
    data() {
      return {
        activeName: 'web',
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
