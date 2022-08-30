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
               @refresh-change="refreshChange"
               @on-load="onLoad">
      <template slot-scope="scope" slot="menu" >
        <el-button type="primary" size="small" @click="openMoney(scope.row,1)">充值</el-button>
        <el-button type="primary" size="small" @click="openMoney(scope.row,2)">扣款</el-button>
        <el-button type="primary" size="small" @click="openAgentUserAccountHistory(scope.row)">交易记录</el-button>
      </template>
      <template slot="menuLeft">
        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   plain
                   v-if="permission.agentaccount_delete"
                   @click="handleDelete">删 除
        </el-button>
      </template>
    </avue-crud>
    <el-dialog title="交易记录" width="80%" :visible.sync="dialogUseraccounthistoryVisible" >
      <agentuseraccounthistory v-if="dialogUseraccounthistoryVisible" :agentUserId="agentUserId"></agentuseraccounthistory>
    </el-dialog>
    <el-dialog :title="formTitleText" :visible.sync="dialogFormVisible">
      <el-form >
        <el-form-item :label="formLabelText" label-width="120px">
          <el-input-number v-model="money" :precision="2" autocomplete="off"></el-input-number>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmMoney()">确 定</el-button>
      </div>
    </el-dialog>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove,money} from "@/api/agent/agentaccount";
  import {mapGetters} from "vuex";
  import agentuseraccounthistory from "./agentuseraccounthistory";

  export default {
    data() {
      return {
        form: {},
        query: {},
        loading: true,
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        money:"",
        moneyUserId:"",
        moneyType:"",
        agentUserId:"",
        formTitleText:"",
        formLabelText:"",
        dialogFormVisible:false,
        dialogUseraccounthistoryVisible:false,
        selectionList: [],
        formLabelWidth: 120,

        option: {
          height: 'auto',
          calcHeight: 80,
          searchLabelWidth:120,
          align: "center",
          searchShow: true,
          searchMenuSpan: 6,
          tip: false,
          border: true,
          index: false,
          viewBtn: true,
          selection: true,

          menuWidth: 260,
          column: [
            {
              label: "id",
              prop: "id",
              width: 70,
              rules: [{
                required: true,
                message: "请输入id",
                trigger: "blur"
              }]
            },
            {
              label: "代理商用户ID",
              prop: "agentUserId",
              hide:true,
              rules: [{
                required: true,
                message: "请输入代理商用户ID",
                trigger: "blur"
              }]
            },
            {
              label: "代理商账号",
              prop: "account",
              search:true,
              rules: [{
                required: true,
                message: "请输入代理商账号",
                trigger: "blur"
              }]
            },
            {
              label: "代理商昵称",
              prop: "name",
              search:true,
              rules: [{
                required: true,
                message: "请输入代理商昵称",
                trigger: "blur"
              }]
            },
            {
              label: "代理商真实姓名",
              prop: "realName",
              search:true,
              rules: [{
                required: true,
                message: "请输入代理商真实姓名",
                trigger: "blur"
              }]
            },
            {
              label: "代理商手机号",
              prop: "phone",
              search:true,
              rules: [{
                required: true,
                message: "请输入代理商手机号",
                trigger: "blur"
              }]
            },
            {
              label: "代理商账户学币",
              prop: "balance",
              rules: [{
                required: true,
                message: "请输入理商账户学币",
                trigger: "blur"
              }]
            },
            {
              label: "创建时间",
              prop: "createTime",
              rules: [{
                required: true,
                message: "请输入创建时间",
                trigger: "blur"
              }]
            },
            {
              label: "最后更新时间",
              prop: "updateTime",
              rules: [{
                required: true,
                message: "请输入最后更新时间",
                trigger: "blur"
              }]
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
          addBtn: this.vaildData(this.permission.agentaccount_add, false),
          viewBtn: this.vaildData(this.permission.agentaccount_view, false),
          delBtn: this.vaildData(this.permission.agentaccount_delete, false),
          editBtn: this.vaildData(this.permission.agentaccount_edit, false)
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
      //打开充值页面
      openMoney(row,type){
        this.money = 0;
        //1 充值  2扣款
        this.moneyType = type;
        this.moneyUserId = row.agentUserId;
        if(type == "1"){
          this.formTitleText="账户充值"
          this.formLabelText="充值学币"
        }
        if(type == "2"){
          this.formTitleText="账户扣款"
          this.formLabelText="扣款学币"
        }
        this.dialogFormVisible= true
      },
      //充值和扣款方法
      confirmMoney(){
        if(this.money==0){
          this.$message.warning("学币不能为0");
          return;
        }
        money(this.moneyUserId,this.money,this.moneyType).then(() => {
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
          this.dialogFormVisible= false
        }, error => {
          console.log(error);
        });
      },
      //打开账户记录
      openAgentUserAccountHistory(row){
        this.agentUserId = row.agentUserId;
        this.dialogUseraccounthistoryVisible = true;
      },
      rowSave(row, done, loading) {
        add(row).then(() => {
          done();
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
        }, error => {
          window.console.log(error);
          loading();
        });
      },
      rowUpdate(row, index, done, loading) {
        update(row).then(() => {
          done();
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
        }, error => {
          window.console.log(error);
          loading();
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
      refreshChange() {
        this.onLoad(this.page, this.query);
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
      }
    },
    components:{
      'agentuseraccounthistory': agentuseraccounthistory
    }
  };
</script>

<style>
</style>
