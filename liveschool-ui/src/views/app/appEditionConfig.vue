<template>
  <basic-container>
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="app版本控制" name="appAndroid">
          <appConfig configType="appAndroid" ></appConfig>
      </el-tab-pane>
    </el-tabs>
  </basic-container>
</template>

<script>
  import {mapGetters} from "vuex";
  import appConfig from "../config/appConfig";

  export default {
    data() {
      return {
        activeName: 'appAndroid',
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
      'appConfig':appConfig,
    },
  };
</script>

<style>
</style>
