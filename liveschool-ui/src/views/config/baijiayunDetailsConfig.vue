<template>
  <basic-container>
    <avue-form :option="option" v-model="obj" @submit="submit"></avue-form>
  </basic-container>
</template>

<script>
  import {getAccountInfo} from "@/api/web/baijia";
  import {mapGetters} from "vuex";

  export default {
    data() {
      return {
        obj: {},
        option: {
          detail:false,
          submitBtn:false,
          emptyBtn:false,
          labelWidth:150,
          group: [
            {
              label: '账户信息（数据并非实时）',
              prop: 'jbxx',
              column: [
                {
                  label: '账户状态',
                  prop: 'status',
                  detail:true,
                },
                {
                  label: '失效时间',
                  prop: 'expireTime',
                  detail:true,
                },
                {
                  label: '已用直播分钟',
                  prop: 'usedMediaLength',
                  display: false,
                  detail:true,
                },
                {
                  label: '可用直播分钟',
                  prop: 'usableMediaLength',
                  display: false,
                  detail:true,
                },
                {
                  label: '已用存储',
                  prop: 'usedStorage',
                  display: false,
                  formatter: function (row,value) {
                    if (value < 1024) {
                      return value + 'B';
                    } else if (value < (1024*1024)) {
                      var temp = value / 1024;
                      temp = temp.toFixed(2);
                      return temp + 'KB';
                    } else if (value < (1024*1024*1024)) {
                      var temp = value / (1024*1024);
                      temp = temp.toFixed(2);
                      return temp + 'MB';
                    } else {
                      var temp = value / (1024*1024*1024);
                      temp = temp.toFixed(2);
                      return temp + 'GB';
                    }
                  },
                  detail:true,
                },
                {
                  label: '可用存储',
                  prop: 'usableStorage',
                  display: false,
                  formatter: function (row,value) {
                    if (value < 1024) {
                      return value + 'B';
                    } else if (value < (1024*1024)) {
                      var temp = value / 1024;
                      temp = temp.toFixed(2);
                      return temp + 'KB';
                    } else if (value < (1024*1024*1024)) {
                      var temp = value / (1024*1024);
                      temp = temp.toFixed(2);
                      return temp + 'MB';
                    } else {
                      var temp = value / (1024*1024*1024);
                      temp = temp.toFixed(2);
                      return temp + 'GB';
                    }
                  },
                  detail:true,
                },
                {
                  label: '已用流量',
                  prop: 'usedFlow',
                  display: false,
                  formatter: function (row,value) {
                    if (value < 1024) {
                      return value + 'B';
                    } else if (value < (1024*1024)) {
                      var temp = value / 1024;
                      temp = temp.toFixed(2);
                      return temp + 'KB';
                    } else if (value < (1024*1024*1024)) {
                      var temp = value / (1024*1024);
                      temp = temp.toFixed(2);
                      return temp + 'MB';
                    } else {
                      var temp = value / (1024*1024*1024);
                      temp = temp.toFixed(2);
                      return temp + 'GB';
                    }
                  },
                  detail:true,
                },
                {
                  label: '可用流量',
                  prop: 'usableFlow',
                  display: false,
                  formatter: function (row,value) {
                    if (value < 1024) {
                      return value + 'B';
                    } else if (value < (1024*1024)) {
                      var temp = value / 1024;
                      temp = temp.toFixed(2);
                      return temp + 'KB';
                    } else if (value < (1024*1024*1024)) {
                      var temp = value / (1024*1024);
                      temp = temp.toFixed(2);
                      return temp + 'MB';
                    } else {
                      var temp = value / (1024*1024*1024);
                      temp = temp.toFixed(2);
                      return temp + 'GB';
                    }
                  },
                  detail:true,
                },
              ]
            },
          ]
        }
      };
    },
    computed: {
      ...mapGetters(["permission"])
    },
    methods: {
      formatterValue(value){
        var temp = value / (1024*1024*1024);
        temp = temp.toFixed(2);
        return temp + 'GB';
      },
      reload(){
        let that = this
        getAccountInfo({}).then(res=>{
          this.obj = res.data.data
          if(res.data.code == "200"){
            console.log("1")
            that.option.group[0].column.filter(item => {
              if (item.prop === "usedMediaLength"||item.prop === "usableMediaLength") {
                if(that.obj.largeClassStatus == "1"){
                  item.display = true;
                }else{
                  item.display = false;
                }
              }
              if (item.prop === "usableStorage"||item.prop === "usedStorage"||item.prop === "usedFlow"||item.prop === "usableFlow") {
                if(that.obj.videoStatus == "1"){
                  item.display = true;
                }else{
                  item.display = false;
                }
              }

            })
            that.obj.usedFlow = this.formatterValue(this.obj.usedFlow);
            that.obj.usableFlow = this.formatterValue(this.obj.usableFlow);
            that.obj.usableStorage = this.formatterValue(this.obj.usableStorage);
            that.obj.usedStorage = this.formatterValue(this.obj.usedStorage);
            that.obj.usedMediaLength = this.obj.usedMediaLength +"分钟";
            that.obj.usableMediaLength = this.obj.usableMediaLength +"分钟";
            that.obj.status = this.obj.status == "1"?"正常":"停用";
            that.obj.videoStatus = this.obj.videoStatus == "1"?"正常":"停用";
            that.obj.largeClassStatus = this.obj.largeClassStatus == "1"?"正常":"停用";
          }
        })
      }
    },
    created: function(){
      this.reload();
    }
  };
</script>

<style>
</style>
