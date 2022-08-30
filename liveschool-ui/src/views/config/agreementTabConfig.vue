<template>
  <basic-container>
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="隐私协议" name="sms">
        <smsConfig configType="privacy" ></smsConfig>
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
    },
  };
</script>

<style>
</style>
