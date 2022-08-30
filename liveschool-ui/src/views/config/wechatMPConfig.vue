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
          configType: this.configType
        };
        let that = this
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
                action: '/api/common/upload',
                listType: 'picture-img',
                propsHttp: {
                  res: 'data'
                },
                tip: item.tip,
                rules: [{
                  required: true,
                  message: "请输入" + item.dataExplain,
                  trigger: "blur"
                }]
              })
            }else if(item.formType == "webico"){
              arr.push({
                label: item.dataExplain,
                prop: item.dataKey,
                type: "upload",
                labelWidth: 200,
                span: 24,
                listType: 'picture-img',
                action: '/api/cms/websiteprofile/uploadIcon',
                propsHttp: {
                  res: 'data'
                },
                tip: item.tip,
              })
            }else if(item.formType == "select"){
              arr.push({
                label: item.dataExplain,
                prop: item.dataKey,
                type: item.formType,
                labelWidth: 200,
                span: 24,
                dataType: "string",
                tip: item.tip,
                props: {
                  label: "dictValue",
                  value: "dictKey"
                },
                dicUrl: "/api/crazy-system/dict/dictionary?code="+item.dataKey,
              })
            }else if(item.formType == "switch"){
              arr.push({
                label: item.dataExplain,
                prop: item.dataKey,
                type: item.formType,
                labelWidth: 200,
                span: 24,
                dataType: "string",
                disabled:false,
                value:"1",
                tip: item.tip,
                change:function(value){
                  var forceLoginFlag =this.findObject(that.option.column,'forceLoginFlag')
                  console.log(forceLoginFlag)
                  if(value.column.prop == 'wechatmpOpenFlag'){
                    if(value.value == '1'){
                      forceLoginFlag.disabled = false
                    }else{
                      forceLoginFlag.disabled = true
                    }
                  }
                },
                dicData: [{
                  label: '关',
                  value: "2"
                }, {
                  label: '开',
                  value: "1"
                }],
              })
            }else if(item.formType == "ueditor"){
              arr.push({
                label: item.dataExplain,
                prop: item.dataKey,
                type: item.formType,
                labelWidth: 200,
                span: 24,
                tip: item.tip,
                component: "avueUeditor",
                options:{
                  //普通图片上传
                  action: "/api/common/upload",
                  props: {
                    res: "data",
                    url:'url'
                  },
                },
              })
            }else if(item.formType == "textarea"){
              arr.push({
                label: item.dataExplain,
                prop: item.dataKey,
                type: item.formType,
                labelWidth: 200,
                span: 24,
              })
            }
            else{
              arr.push({
                label: item.dataExplain,
                prop: item.dataKey,
                type: item.formType,
                labelWidth: 200,
                span: 24,
              })
            }
          })
          formData.configType = this.configType;
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
