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
      <template slot-scope="scope" slot="menu">
        <el-button type="primary" size="small" @click="openMoney(scope.row,1)">充值</el-button>
        <el-button type="primary" size="small" @click="openMoney(scope.row,2)">扣款</el-button>
        <el-button type="primary" size="small" @click="openUserloginlog(scope.row)">交易记录</el-button>
      </template>
    </avue-crud>
    <el-dialog title="交易记录" width="80%" :visible.sync="dialogUseraccounthistoryVisible" >
      <useraccounthistory v-if="dialogUseraccounthistoryVisible" :userId="userId"></useraccounthistory>
    </el-dialog>
    <el-dialog :title="formTitleText" :visible.sync="dialogFormVisible">
      <el-form >
        <el-form-item :label="formLabelText" label-width="120px">
          <el-input-number :min="0" v-model="money" :precision="2" autocomplete="off"></el-input-number>
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
  import {getList, getDetail, add, update, remove,money} from "@/api/user/useraccount";
  import {mapGetters} from "vuex";
  import useraccounthistory from "./useraccounthistory";

  export default {
    data() {
      return {
        moneyUserId:"",
        moneyType:"",
        money:"",
        formTitleText:"",
        formLabelText:"",
        formLabelWidth:120,
        dialogFormVisible:false,
        userId:"",
        dialogUseraccounthistoryVisible:false,
        form: {},
        query: {},
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
          index: false,
          viewBtn: false,
          editBtn:false,
          delBtn:false,
          addBtn:false,
          selection: false,
          column: [
            {
              label: "ID",
              prop: "id",
            },
            {
              label: "账号",
              prop: "userName",
              search:true,
            },
            {
              label: "手机",
              prop: "mobile",
              search:true,
            },
            {
              label: "邮箱",
              prop: "email",
              search:true,
            },
            {
              label: "用户id",
              prop: "userId",
              hide:true
            },
            {
              label: "账号学币",
              prop: "balance",
            },
            {
              label: "冻结金额",
              prop: "forzenAmount",
              hide: true,
            },
/*            {
              label: "银行支付充值金额",
              prop: "cashAmount",
            },
            {
              label: "充值卡充值金额",
              prop: "vmAmount",
            },
            {
              label: "分销返现充值金额",
              prop: "backAmount",
            },*/
            {
              label: "创建时间",
              prop: "createTime",
              type: "datetime",
              format: "yyyy-MM-dd HH:mm:ss",
              valueFormat: "yyyy-MM-dd HH:mm:ss",
            },
            {
              label: "最后更新时间",
              prop: "updateTime",
              type: "datetime",
              format: "yyyy-MM-dd HH:mm:ss",
              valueFormat: "yyyy-MM-dd HH:mm:ss",
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
          addBtn: this.vaildData(this.permission.useraccount_add, false),
          viewBtn: this.vaildData(this.permission.useraccount_view, false),
          delBtn: this.vaildData(this.permission.useraccount_delete, false),
          editBtn: this.vaildData(this.permission.useraccount_edit, false)
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
      openMoney(row,type){
        this.money = 0;
        //1 充值  2扣款
        this.moneyType = type;
        this.moneyUserId = row.userId;
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
      openUserloginlog(row){
        this.userId = row.userId;
        this.dialogUseraccounthistoryVisible = true;
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
      refreshChange() {
        this.onLoad(this.page, this.query);
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
      }
    },
    components:{
      'useraccounthistory':useraccounthistory
    }
  };
</script>

<style>
</style>
