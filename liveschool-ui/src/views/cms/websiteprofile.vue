<template>
  <basic-container>
   <avue-form :option="option" v-model="form" @submit="handleSubmit" ></avue-form>
  </basic-container>
</template>

<script>
  import {getList,  add} from "@/api/cms/websiteprofile";
  import {mapGetters} from "vuex";

  export default {
    data() {
      return {
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
          column: [
          ]
        },
        data: []
      };
    },
    computed: {
      ...mapGetters(["permission"])
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
          configType: "web"
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
                action: '/api/common/upload',
                listType: 'picture-img',
                propsHttp: {
                  res: 'data'
                },
                tip: item.tip,
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
                props: {
                  label: "dictValue",
                  value: "dictKey"
                },
                tip: item.tip,
                dicUrl: "/api/crazy-system/dict/dictionary?code="+item.dataKey,
              })
            }else{
              arr.push({
                label: item.dataExplain,
                prop: item.dataKey,
                type: item.formType,
                labelWidth: 200,
                span: 24,
                tip: item.tip,
              })
            }
          })
          formData.configType = "web";
          this.form = formData;
          this.option.column = arr;
        });
      }
    },
    created: function(){
      this.reload();
    }
  };
</script>

<style>

  .avue-upload__avatar{
    width: auto;
    height: auto;
    max-width: 250px;
    max-height: 250px;
    min-width: 70px;
    min-height: 70px;
  }
</style>
