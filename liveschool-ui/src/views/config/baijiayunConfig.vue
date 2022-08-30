<template>
  <basic-container>
   <avue-form v-if="formFlag" :option="option" v-model="form" @submit="handleSubmit" ></avue-form>
  </basic-container>
</template>

<script>
  import {getList,  add} from "@/api/cms/websiteprofile";
  import {mapGetters} from "vuex";

  export default {
    data() {
      return {
        formFlag:false,
        form: {},
        query: {},
        dataMap: new Map(),
        loading: true,
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        selectionList: [],
        option: {
          tip: false,
          border: true,
          align: "center",
          index: true,
          viewBtn: true,
          selection: true,
          emptyBtn:false,
          column: [
          ]
        },
        data: []
      };
    },
    computed: {
      ...mapGetters(["permission"])
    },
    props:{
      configType: String
    },
    watch: {
      configType: {
        deep: true,
        handler(newVal){
          this.formFlag = false;
          this.reload();
          this.formFlag = true;
        }
      }
    },
    methods: {
      rowSave(row) {
        add(row).then(() => {
          this.$message({
            type: "success",
            message: "操作成功!"
          });
        }, error => {
          console.log(error);
        });
      },
      handleSubmit(form,done){
        for(var key in form){
          if(this.dataMap.get(key) == form[key]){
            delete form[key];
          }
        }
        add(form).then(() => {
          this.$message({
            type: "success",
            message: "操作成功!"
          });
          this.reload();
          done()
        }, error => {
          console.log(error);
        });
      },
      reload(){
        let params = {
          configType: "baijiayun_vod"
        };
        getList(params).then(res => {
          const data = res.data.data;
          let arr = [];
          let formData = {};
          data.forEach((item)=>{
            this.dataMap.set(item.dataKey,item.dataValue)
            formData[item.dataKey] = item.dataValue;
            if(item.formType == "upload"){
              arr.push({
                label: item.dataExplain,
                prop: item.dataKey,
                type: item.formType,
                labelWidth: 200,
                span: 24,
                action: '/api/common/upload/size?width=500&height=332',
                listType: 'picture-img',
                propsHttp: {
                  res: 'data'
                },
                tip: '请上传宽高为： 500*332 的图片',
                rules: [{
                  required: true,
                  message: "请输入" + item.dataExplain,
                  trigger: "blur"
                }]
              })
            }else if(item.formType == "switch"){
              arr.push({
                label: item.dataExplain,
                prop: item.dataKey,
                type: item.formType,
                labelWidth: 200,
                span: 24,
                dataType: "string",
                value:"1",
                tip: item.tip,
                dicData: [{
                  label: '关',
                  value: "2"
                }, {
                  label: '开',
                  value: "1"
                }],
              })
            }else if(item.formType == "select"){
              arr.push({
                label: item.dataExplain,
                prop: item.dataKey,
                type: item.formType,
                labelWidth: 200,
                span: 24,
                dataType: "string",
                props: {
                  label: "dictValue",
                  value: "dictKey"
                },
                dicUrl: "/api/crazy-system/dict/dictionary?code="+item.dataKey,
              })
            }else{
              console.log(item.dataKey == "URL")
              if(item.dataKey == "URL"){
                arr.push({
                  label: item.dataExplain,
                  prop: item.dataKey,
                  type: item.formType,
                  labelWidth: 200,
                  span: 24,
                  placeholder:"请输入百家域名 例：https://*******.at.baijiayun.com/",
                  rules: [{
                    required: true,
                    message: "请输入百家域名",
                    trigger: "blur"
                  }]
                })
              }else{
                arr.push({
                  label: item.dataExplain,
                  prop: item.dataKey,
                  type: item.formType,
                  labelWidth: 200,
                  span: 24,
                  rules: [{
                    required: true,
                    message: "请输入" + item.dataExplain,
                    trigger: "blur"
                  }]
                })
              }

            }
          })
          formData.configType = "baijiayun_vod";
          this.form = formData;
          this.option.column = arr;
          this.formFlag = true;
        });
      }
    },
    created: function(){
      this.reload();
    }
  };
</script>

<style>
</style>
