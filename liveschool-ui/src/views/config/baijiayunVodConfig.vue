<template>
  <basic-container>
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="视频云配置" name="baijiaConfig">
        <baijiayunConfig v-if="activeName=='baijiaConfig'"></baijiayunConfig>
      </el-tab-pane>
      <el-tab-pane label="视频云账号信息" name="baijiaDetail">
        <baijiayunDetailsConfig v-if="activeName=='baijiaDetail'"></baijiayunDetailsConfig>
      </el-tab-pane>
    </el-tabs>
  </basic-container>
</template>

<script>
  import {getList,  add} from "@/api/cms/websiteprofile";
  import {mapGetters} from "vuex";
  import baijiayunConfig from "./baijiayunConfig";
  import baijiayunDetailsConfig from "./baijiayunDetailsConfig";

  export default {
    data() {
      return {
        activeName: 'baijiaConfig',
      };
    },
    computed: {
      ...mapGetters(["permission"])
    },
    methods: {
      handleClick(tab, event){
        console.log(tab, event);
      }
    },
    created: function(){
    },
    components:{
      baijiayunDetailsConfig,
      baijiayunConfig
    },
  };
</script>

<style>
</style>
