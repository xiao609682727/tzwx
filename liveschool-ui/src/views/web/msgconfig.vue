<template>
  <basic-container>
    <avue-crud :option="option"
               :table-loading="loading"
               :data="data"
               :page="page"
               :permission="permissionList"
               :before-open="beforeOpen"
               v-model="form"
               ref="crud"
               @row-update="rowUpdate"
               @row-save="rowSave"
               @row-del="rowDel"
               @search-change="searchChange"
               @search-reset="searchReset"
               @selection-change="selectionChange"
               @current-change="currentChange"
               @size-change="sizeChange"
               @on-load="onLoad">
      <template slot="menuLeft">
        <!--<el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   plain
                   v-if="permission.msgconfig_delete"
                   @click="handleDelete">删 除
        </el-button>-->
        <el-button type="primary"
                   size="small"
                   plain
                   icon="el-icon-s-opportunity"
                   @click="msgTest">短信测试发送
        </el-button>
      </template>
    </avue-crud>
    <el-dialog title="测试短信发送" :visible.sync="msgTestIs" width="555px">
      <el-form :model="msgSignAndTemp">
        <el-form-item label="手机号(必填)：" label-width="140px" >
          <el-input v-model="msgSignAndTemp.moblie" placeholder="请输入手机号" style="width: 70%;" ></el-input>
      </el-form-item>
      <el-form-item label="短信模板(必填)：" label-width="140px" >
          <el-select v-model="msgSignAndTemp.tempId"  placeholder="请选择" style="width: 70%;" >
            <el-option
              v-for="item in smsTemplate"
              :key="item.id"
              :label="item.name"
              :value="item.otherId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="短信签名(必填)：" label-width="140px" >
          <el-select v-model="msgSignAndTemp.signId"  placeholder="请选择" style="width: 70%;" >
            <el-option
              v-for="item in smsSignList"
              :key="item.id"
              :label="item.name"
              :value="item.otherId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="参数添加(非必填)：" label-width="140px" >
          <el-input v-model="msgSignAndTemp.paramsString" placeholder="例：code=111&code1=3333" style="width: 70%;" ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="sendSMS">确 定</el-button>
        <el-button @click="dialogPwdFormVisible = false" >取 消</el-button>
      </div>
    </el-dialog>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove,getSmsList,testMsgSignAndTemp} from "@/api/web/msgconfig";
  import {mapGetters} from "vuex";

  export default {
    data() {
      return {
        form: {},
        query: {},
        loading: true,
        msgTestIs:false,
        smsSignList:[],
        smsTemplate:[],
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        msgSignAndTemp:{
          moblie:"",
          signId:"",
          tempId:"",
          paramsString:""
        },
        selectionList: [],
        option: {
          tip: false,
          border: true,
          align: "center",
          index: false,
          refreshBtn:false,
          viewBtn: false,
          delBtn: false,
          selection: false,
          addBtn: false,
          column: [
            {
              label: "名称",
              prop: "name",
              span:24,
              rules: [{
                required: true,
                message: "请输入名称",
                trigger: "blur"
              }]
            },
            {
              label: "code",
              prop: "code",
              span:24,
              rules: [{
                required: true,
                message: "请输入消息的code",
                trigger: "blur"
              }]
            },
            {
              label: "消息类型",
              prop: "msgType",
              type:"radio",
              dataType:"string",
              span:24,
              dicData:[
                {
                  label:'站内信',
                  value:"1"
                },{
                  label:'邮件',
                  value:"2"
                },{
                  label:'短信',
                  value:"3"
                }
              ]
            },
            {
              label: "内容",
              prop: "content",
              span:24,
              type:"textarea"
            },
            {
              label: "短信签名",
              prop: "smsSign",
              type:"select",
              dataType:"string",
              dicUrl:'/api/web/smsconfig?type=2',
              tip:"非必填，当勾选短信类型则短信签名必须选择",
              props: {
                label: 'name',
                value: 'otherId'
              },
            },
            {
              label: "短信模板",
              prop: "smsTemplate",
              type:"select",
              dataType:"string",
              dicUrl:'/api/web/smsconfig?type=1',
              tip:"非必填，当勾选短信类型则短信模板必须选择",
              props: {
                label: 'name',
                value: 'otherId'
              },
            },
          ]
        },
        data: []
      };
    },
    computed: {
      ...mapGetters(["permission"]),
      permissionList() {
        return {
          addBtn: this.vaildData(this.permission.msgconfig_add, false),
          viewBtn: this.vaildData(this.permission.msgconfig_view, false),
          delBtn: this.vaildData(this.permission.msgconfig_delete, false),
          editBtn: this.vaildData(this.permission.msgconfig_edit, false)
        };
      },
      ids() {
        let ids = [];
        this.selectionList.forEach(ele => {
          ids.push(ele.id);
        });
        return ids.join(",");
      }
    },
    methods: {
      sendSMS(){
        if(this.msgSignAndTemp.moblie == ''){
          this.$message({
            type: "warning",
            message: "请填写手机号"
          });
          return;
        }
        if(this.msgSignAndTemp.tempId == ''){
          this.$message({
            type: "warning",
            message: "请选择模板"
          });
          return;
        }
        if(this.msgSignAndTemp.signId == ''){
          this.$message({
            type: "warning",
            message: "请选择签名"
          });
          return;
        }
        testMsgSignAndTemp(this.msgSignAndTemp.moblie,this.msgSignAndTemp.tempId,this.msgSignAndTemp.signId,this.msgSignAndTemp.paramsString).then(()=>{
          this.$message({
            type: "success",
            message: "操作成功!"
          });
          this.msgTestIs = false;
        })
      },
      rowSave(row, loading, done) {
        add(row).then(() => {
          loading();
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
        }, error => {
          done();
          console.log(error);
        });
      },
      msgTest() {
        this.msgSignAndTemp.moblie="";
        this.msgSignAndTemp.tempId="";
        this.msgSignAndTemp.signId="";
        this.msgSignAndTemp.paramsString="";
        this.msgTestIs = true;
      },
      rowUpdate(row, index, loading, done) {
        update(row).then(() => {
          loading();
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
        }, error => {
          done();
          console.log(error);
        });
      },
      rowDel(row) {
        this.$confirm("确定将选择数据删除?", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            return remove(row.id);
          })
          .then(() => {
            this.onLoad(this.page);
            this.$message({
              type: "success",
              message: "操作成功!"
            });
          });
      },
      handleDelete() {
        if (this.selectionList.length === 0) {
          this.$message.warning("请选择至少一条数据");
          return;
        }
        this.$confirm("确定将选择数据删除?", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            return remove(this.ids);
          })
          .then(() => {
            this.onLoad(this.page);
            this.$message({
              type: "success",
              message: "操作成功!"
            });
            this.$refs.crud.toggleSelection();
          });
      },
      beforeOpen(done, type) {
        if (["edit", "view"].includes(type)) {
          getDetail(this.form.id).then(res => {
            this.form = res.data.data;
          });
        }
        done();
      },
      searchReset() {
        this.query = {};
        this.page.currentPage = 1;
        this.onLoad(this.page);
      },
      searchChange(params,done) {
        this.query = params;
        this.page.currentPage = 1;
        this.onLoad(this.page, params);
        done()
      },
      selectionChange(list) {
        this.selectionList = list;
      },
      selectionClear() {
        this.selectionList = [];
        this.$refs.crud.toggleSelection();
      },
      currentChange(currentPage){
        this.page.currentPage = currentPage;
      },
      sizeChange(pageSize){
        this.page.pageSize = pageSize;
      },
      onLoad(page, params = {}) {
        this.loading = true;
        getList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
          this.page.total = data.total;
          this.data = data.records;
          this.loading = false;
          this.selectionClear();
        });

        getSmsList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          let test = res.data.data.records;
          test.forEach(sms => {
            if(sms.type==1){
              this.smsTemplate.push(sms);
            }else {
              this.smsSignList.push(sms);
            }
          });
        });
      }
    }
  };
</script>

<style>
</style>
