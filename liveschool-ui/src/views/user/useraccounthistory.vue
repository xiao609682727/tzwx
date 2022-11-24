<template>
  <basic-container>
    <avue-crud :option="option"
               :table-loading="loading"
               :data="data"
               :page="page"
               :search.sync="search"
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
      <template slot="menuLeft">
        <el-button type="warning"
                   size="small"
                   plain
                   icon="el-icon-download"
                   @click="handleExport">导出
        </el-button>
      </template>
      <template slot="orderNo" slot-scope="scope">
        <el-button width="150%" size="small" type="text" @click="openOrderDetail(scope.row.orderNo)" >{{scope.row.orderNo}}</el-button>
      </template>
    </avue-crud>
    <el-dialog title="订单详情" width="80%"  :visible.sync="dialogOrderVisible" append-to-body>
      <trxorderdetail v-if="dialogOrderVisible" :orderNo="orderNo" :orderType="orderType" ></trxorderdetail>
    </el-dialog>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove} from "@/api/user/useraccounthistory";
  import {mapGetters} from "vuex";
  import {getToken} from '@/util/auth';
  import trxorderdetail from "./orderTrxorderdetail";
  export default {
    data() {
      return {
        form: {},
        search:{},
        query: {userId:this.userId},
        loading: true,
        orderType: false,
        dialogOrderVisible:false,
        orderNo:0,
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
          menu:false,
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
              label: "当前学币",
              prop: "balance",
            },
            {
              label: "现金学币",
              hide: true,
              prop: "cashAmount",
            },
            {
              label: "订单号",
              prop: "orderNo",
              slot:true,
            },
            /*{
              label: "充值卡学币",
              prop: "vmAmount",
            },*/
            /*{
              label: "分销返现学币",
              prop: "backAmount",
            },*/
            {
              label: "交易学币金额",
              prop: "trxAmount",
              rules: [{
                required: true,
                message: "请输入交易学币金额",
                trigger: "blur"
              }]
            },
            {
              label: "描述",
              prop: "description",
            },
            {
              label: "交易类型",
              prop: "actHistoryType",
              dataType: "string",
              search: true,
              type:"select",
              props: {
                label: "dictValue",
                value: "dictKey"
              },
              dicUrl: "/api/crazy-system/dict/dictionary?code=transaction_type",
            },
            {
              label: "业务类型",
              prop: "bizType",
              dataType: "string",
              type:"select",
              search: true,
              props: {
                label: "dictValue",
                value: "dictKey"
              },
              dicUrl: "/api/crazy-system/dict/dictionary?code=bus_type",
            },
            {
              label: "创建时间",
              prop: "createTime",
              type: "date",
              format:"yyyy-MM-dd HH:mm:ss",
              valueFormat:"yyyy-MM-dd HH:mm:ss",
              search: true,
              rules: [{
                required: true,
                message: "请输入登录时间",
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
          addBtn: this.vaildData(this.permission.useraccounthistory_add, false),
          viewBtn: this.vaildData(this.permission.useraccounthistory_view, false),
          delBtn: this.vaildData(this.permission.useraccounthistory_delete, false),
          editBtn: this.vaildData(this.permission.useraccounthistory_edit, false)
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
    props:{
      userId: Number
    },
    watch: {
      userId: {
        deep: true,
        handler(newVal){
          this.query.userId = newVal;
          this.onLoad(this.page);
        }
      }
    },
    methods: {
      openOrderDetail(row){
        this.dialogOrderVisible = true;
        this.orderNo = row;
        this.orderType = true;
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
        this.query = {userId:this.userId};
        this.page.currentPage = 1;
        this.onLoad(this.page);
      },
      searchChange(params,done) {
        this.query.userId = this.userId;
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
      onLoad(page, params = {userId:this.userId}) {
        this.loading = true;
        getList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
          this.page.total = data.total;
          this.data = data.records;
          this.loading = false;
          this.selectionClear();
        });
      },
      handleExport() {
        this.$confirm("是否导出用户交易数据?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          window.open(`/api/user/useraccounthistory/export-userAccountHistory?crazy-auth=${getToken()}`+
          `&userId=`+ this.query.userId+`&userName=`+ this.search.userName+`&mobile=`+this.search.mobile+
          `&email=`+this.search.email+`&actHistoryType=`+this.search.actHistoryType+
           `&bizType=`+this.search.bizType+`&createTime=`+this.search.createTime);

        });
      }
    },
    components:{
      'trxorderdetail':trxorderdetail,
    },
  };
</script>

<style>
</style>
