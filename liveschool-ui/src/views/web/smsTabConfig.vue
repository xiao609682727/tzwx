<template>
  <basic-container>
    <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
      <el-tab-pane label="模板" name="template">
        <smsSignConfig type="1" v-if="showFlag"></smsSignConfig>
      </el-tab-pane>
      <el-tab-pane label="签名" name="sign">
        <smsSignConfig type="2" v-if="showFlag"></smsSignConfig>
      </el-tab-pane>
    </el-tabs>
  </basic-container>
</template>

<script>
  import {mapGetters} from "vuex";
  import smsSignConfig from "./smsSignConfig";

  export default {
    data() {
      return {
        showFlag:true,
        activeName: 'template',
      };
    },
    computed: {
      ...mapGetters(["permission"])
    },
    methods: {
      handleClick(tab, event) {
        this.showFlag = false;
        this.activeName = tab.name;
        this.showFlag = true;
      }
    },
    components:{
      'smsSignConfig':smsSignConfig,
    },
  };
</script>

<style>
</style>
