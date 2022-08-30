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
  import {getList, getDetail, add, update, remove} from "@/api/agent/agentuseraccounthistory";
  import {mapGetters} from "vuex";
  import {getToken} from '@/util/auth';
  import trxorderdetail from "@/views/user/orderTrxorderdetail";

  export default {
    data() {
      return {
        form: {},
        search:{},
        query: {agentUserId:this.agentUserId},
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
          height: 'auto',
          calcHeight: 80,
          searchShow: true,
          searchMenuSpan: 6,
          searchLabelWidth:120,
          align: "center",
          tip: false,
          border: true,
          index: false,
          viewBtn: true,
          selection: true,

          menu:false,
          column: [

            {
              label: "代理商用户id",
              prop: "agentUserId",
              hide:true,
              rules: [{
                required: true,
                message: "请输入代理商用户id",
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
              label: "订单ID",
              prop: "orderId",
              hide:true,
              rules: [{
                required: true,
                message: "请输入订单ID",
                trigger: "blur"
              }]
            },
            {
              label: "订单号",
              prop: "orderNo",
              minWidth: 100,
              /*hide:true,*/
              slot:true,
            },
            {
              label: "当前学币",
              prop: "balance",
              rules: [{
                required: true,
                message: "请输入当前学币",
                trigger: "blur"
              }]
            },
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
              rules: [{
                required: true,
                message: "请输入描述",
                trigger: "blur"
              }]
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
              dicUrl: "/api/crazy-system/dict/dictionary?code=agent_act_history_type",
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
              dicUrl: "/api/crazy-system/dict/dictionary?code=agent_biz_type",
            },
            {
              label: "创建时间",
              minWidth: 100,
              prop: "createTime",
              type: "date",
              format:"yyyy-MM-dd HH:mm:ss",
              valueFormat:"yyyy-MM-dd HH:mm:ss",
              rules: [{
                required: true,
                message: "请输入创建时间",
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
          addBtn: this.vaildData(this.permission.agentuseraccounthistory_add, false),
          viewBtn: this.vaildData(this.permission.agentuseraccounthistory_view, false),
          delBtn: this.vaildData(this.permission.agentuseraccounthistory_delete, false),
          editBtn: this.vaildData(this.permission.agentuseraccounthistory_edit, false)
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
      agentUserId: Number
    },
    watch: {
      agentUserId: {
        deep: true,
        handler(newVal){
          this.query.agentUserId = newVal;
          this.onLoad(this.page);
        }
      }
    },
    methods: {
      openOrderDetail(row){
        this.dialogOrderVisible = true;
        this.orderNo = row;
        this.orderType = false;
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
      //导出数据
      handleExport() {
        window.open(`/api/edu/agentuseraccounthistory/export-agentUserAccountHistory?crazy-auth=${getToken()}`+
                `&account=`+ this.search.account+
                `&realName=`+ this.search.realName+
                `&phone=`+ this.search.phone+
                `&actHistoryType=`+ this.search.actHistoryType+
                `&bizType=`+ this.search.bizType
        );
      },
      searchReset() {
        this.query = {agentUserId:this.agentUserId};
        this.page.currentPage = 1;
        this.onLoad(this.page);
      },
      searchChange(params,done) {
        this.query = params;
        this.query.agentUserId = this.agentUserId;
        this.page.currentPage = 1;
        this.onLoad(this.page, params);
        done()
      },
      selectionChange(list) {
        this.selectionList = list;
      },
      refreshChange() {
        this.onLoad(this.page, this.query);
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
      onLoad(page, params = {agentUserId:this.agentUserId}) {
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
      'trxorderdetail':trxorderdetail,
    },
  };
</script>

<style>
</style>
