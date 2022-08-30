<template>
  <basic-container>
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="短信配置" name="sms">
        <smsServerConfig configType="sms" ></smsServerConfig>
      </el-tab-pane>
      <el-tab-pane label="邮箱配置" name="email">
        <smsConfig configType="email" ></smsConfig>
      </el-tab-pane>
    </el-tabs>
  </basic-container>
</template>

<script>
  import {mapGetters} from "vuex";
  import smsConfig from "./smsConfig";
  import smsServerConfig from "./smsServerConfig";

  export default {
    data() {
      return {
        activeName: 'sms',
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
      'smsServerConfig':smsServerConfig
    },
  };
</script>

<style>
</style>
