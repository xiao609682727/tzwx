<template>
  <basic-container>
    <avue-form :option="option" v-model="form" @submit="handleSubmit" >
      <template slot-scope="scope" slot="sms_supplier">
        <el-select v-model="form.sms_supplier" @change="change" placeholder="请选择" >
          <el-option
            v-for="item in smsSupplier"
            :key="item.dictKey"
            :label="item.dictValue"
            :value="item.dictKey">
          </el-option>
        </el-select>
      </template>
    </avue-form>
  </basic-container>
</template>

<script>
  import {getList,  add} from "@/api/cms/websiteprofile";
  import {dictionary} from "@/api/system/dict";
  import {mapGetters} from "vuex";

  export default {
    data() {
      return {
        formFlag:false,
        form: {},
        query: {},
        smsSupplier:[],
        dataMap: new Map(),
        loading: true,
        configType:"sms",
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
          labelWidth:"150",
          column: [
            {
              label: "提供商",
              prop: "sms_supplier",
              hide: true,
              span: 24,
              value: "",
              dataType: "string",
              formslot: true,
            },
            {
              label: "AccessKey ID",
              prop: "appid",
              span: 24,
            },
            {
              label: "AccessKey Secret",
              prop: "appkey",
              span: 24,
            },
            {
              label: "SDKAppID",
              prop: "sdkAppId",
              span: 24,
              display:false,
            },
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
        let that = this
        //获取字典表信息
        dictionary("sms_supplier").then(res =>{
          this.smsSupplier = res.data.data
        })
        //获取数据回显
        let params = {
          configType: this.configType
        };
        getList(params).then(res => {
          const data = res.data.data;
          let formData = {};
          data.forEach((item)=>{
            if(item.dataKey == "sms_supplier"&&item.dataValue == "2"){
              that.option.column.filter(i => {
                if (i.prop === "sdkAppId") {
                    i.display = true;
                }
              });
            }
            this.dataMap.set(item.dataKey,item.dataValue)
            formData[item.dataKey] = item.dataValue;
          })
          this.form = formData;
          formData.configType = this.configType;
        })
      },
      change(value){
        this.option.column.filter(item => {
          if (item.prop === "appid") {
            if(value == "1"){
              item.label = "AccessKey ID";
            }
            if(value == "2"){
              item.label = "SecretId";
            }
          }
          if (item.prop === "appkey") {
            if(value == "1"){
              item.label = "AccessKey Secret";
            }
            if(value == "2"){
              item.label = "SecretKey";
            }
          }
          if (item.prop === "sdkAppId") {
            if(value == "1"){
              item.display = false;
            }
            if(value == "2"){
              item.display = true;
            }
          }
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
