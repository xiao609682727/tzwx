<template>
  <basic-container>
    <avue-form v-if="formFlag" :option="option" v-model="form" @submit="handleSubmit" :upload-after="uploadAfter">
      <template slot-scope="scope" slot="updateUrl">
        <el-upload
            class="upload-demo"
            :action="uploadUrl"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :before-remove="beforeRemove"
            :limit="1"
            :on-exceed="handleExceed"
            :headers="headers"
            :on-success="uploadSuccess"
            :file-list="fileList">
          <el-button size="small" type="primary">点击上传</el-button>
        </el-upload>
      </template>
    </avue-form>
  </basic-container>
</template>

<script>
  import {getList,  add} from "@/api/cms/websiteprofile";
  import {mapGetters} from "vuex";
  import {getToken} from '@/util/auth';

  export default {
    data() {
      return {
        headers:{

        },
        formFlag:false,
        form: {},
        submitForm:{},
        query: {},
        uploadUrl:"/api/cms/websiteprofile/uploadAppPack?crazy-auth=" + getToken(),
        isUpdate:0,
        updateUrl: "",
        dataMap: new Map(),
        loading: true,
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        fileList: [],
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
      handleRemove(file, fileList) {
        console.log(file, fileList);
      },
      handlePreview(file) {
        console.log(file);
      },
      handleExceed(files, fileList) {
        this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },
      beforeRemove(file, fileList) {
        return this.$confirm(`确定移除 ${ file.name }？`);
      },
      uploadSuccess(res, file) {
        this.isUpdate=2;
        this.updateUrl = res.data.url
        console.log(res)
      },
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
        if(this.isUpdate==2){
          form["updateUrl"]=this.updateUrl;
        }
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
            }
            else if(item.formType == "webico"){
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
            }
            else if(item.formType == "select"){
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
            }
            else if(item.formType == "switch"){
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
            }
            else if(item.formType == "ueditor"){
              arr.push({
                label: item.dataExplain,
                prop: item.dataKey,
                type: item.formType,
                labelWidth: 200,
                span: 24,
                tip: item.tip,
                options:{
                  //普通图片上传
                  action: "/api/common/upload",
                  props: {
                    res: "data",
                    url:'url'
                  },
                },
              })
            }
            else if(item.formType == "textarea"){
              arr.push({
                label: item.dataExplain,
                prop: item.dataKey,
                type: item.formType,
                labelWidth: 200,
                span: 24,
              })
            }
            else if(item.formType == "updateApi"){
              arr.push({
                label: item.dataExplain,
                formslot:true,
                prop: "updateUrl",
                type:"upload",
                dataType: 'string',
                labelWidth: 200,
                span: 24,
                action: '/api/cms/websiteprofile/uploadAppPack',
                propsHttp: {
                  res: 'data'
                },
                limit:1,
                tip: item.tip,
              })

              let url = item.dataValue;
              let name = url.substring(url.lastIndexOf("/") + 1, url.length);
              this.fileList = [{
                name:name,
                url:url
              }]

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
