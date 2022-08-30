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
        <el-button type="text" @click="openOrderDetail(scope.row)" >订单详情</el-button>
      </template>
    </avue-crud>
    <el-dialog title="订单详情" width="80%"  :visible.sync="dialogOrderVisible" append-to-body>
      <trxorderdetail v-if="dialogOrderVisible" :orderId="orderId" :orderType="orderType" ></trxorderdetail>
    </el-dialog>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove, updateOrder} from "@/api/edu/ordersUser";
  import {mapGetters} from "vuex";
  import trxorderdetail from "./trxorderdetail";
  import {getToken} from '@/util/auth';

  export default {
    data() {
      return {
        orderId:"",
        orderType:"",
        dialogOrderVisible:false,
        form: {},
        query: {userId:this.userId},
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
          addBtn:false,
          editBtn: false,
          delBtn:false,
          selection: false,
          column: [
            {
              label: "订单号",
              prop: "orderNo",
              search: true,
            },
/*            {
              label: "账号",
              prop: "loginAccount",
              search:true
            },*/
            {
              label: "手机",
              prop: "mobile",
              search: true,
            },
            {
              label: "邮箱",
              prop: "email",
              search: true,
            },
            {
              label: "账号",
              prop: "userName",
              search: true,
            },
            {
              label: "订单总金额",
              prop: "sumMoney",
              type: "number",
              precision: 2,
            },
            {
              label: "订单状态",
              prop: "states",
              type:"select",
              search: true,
              dicData:[
                {
                  label:'未支付',
                  value:"1"
                },{
                  label:'已支付',
                  value:"2"
                },{
                  label:'已取消',
                  value:"3"
                },{
                  label:'退款',
                  value:"4"
                }
              ]
            },
            {
              label: "创建时间",
              prop: "createTime",
              type: "datetime",
              format:"yyyy-MM-dd HH:mm:ss",
              valueFormat:"yyyy-MM-dd HH:mm:ss",
              search: true,
            },
            {
              label: "结束时间",
              prop: "endTime",
              type: "datetime",
              format:"yyyy-MM-dd HH:mm:ss",
              valueFormat:"yyyy-MM-dd HH:mm:ss",
              search: true,
              hide:true,
            },
            {
              label: "支付时间",
              prop: "payTime",

            },
            {
              label: "支付类型",
              prop: "payType",
              type:"select",
              search: true,
              dicData:[
                {
                  label:'支付宝',
                  value:"1"
                },{
                  label:'微信',
                  value:"2"
                },{
                  label:'后台赠送',
                  value:"3"
                },{
                  label:'账号储值',
                  value:"4"
                }
              ]
            },
            /*{
              label: "请求渠道",
              prop: "reqChannel",
              hide:true,
            },*/
            {
              label: "订单原始金额",
              prop: "orderAmount",
              type: "number",
              precision: 2,
              hide: true,
            },
            {
              label: "实际支付金额",
              prop: "cashAmount",
              type: "number",
              precision: 2,
              hide: true,
            },
            {
              label: "实际支付的vm金额",
              prop: "vmAmount",
              type: "number",
              precision: 2,
              hide: true,
            },
            {
              label: "实际支付返现金额",
              prop: "backAmount",
              type: "number",
              precision: 2,
              hide: true,
            },
          /*  {
              label: "优惠券金额",
              prop: "couponAmount",
              hide:true,
              type: "number",
              precision: 2,
            },*/
           /* {
              label: "砍价金额",
              hide:true,
              prop: "bargainAmount",
              type: "number",
              precision: 2,
            },*/
            {
              label: "退款金额",
              prop: "refundAmount",
              type: "number",
              precision: 2,
            },
            {
              label: "订单类型",
              prop: "orderType",
              type:"select",
              search: true,
              dicData:[
                {
                  label:'点播',
                  value:"COURSE"
                },{
                  label:'直播',
                  value:"LIVE"
                },{
                  label:'班级',
                  value:"PACKAGE"
                },{
                  label:'面授',
                  value:"LINECLASS"
                },{
                  label:'充值',
                  value:"ACCOUNT"
                }
              ]
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
          addBtn: this.vaildData(this.permission.orders_add, false),
          viewBtn: this.vaildData(this.permission.orders_view, false),
          delBtn: this.vaildData(this.permission.orders_delete, false),
          editBtn: this.vaildData(this.permission.orders_edit, false)
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
    watch:{
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
        this.orderId = row.id;
        if(row.orderType != 'ACCOUNT'){
          this.orderType = true;
        }else{
          this.orderType = false;
        }

      },
      handleExport() {
        this.$confirm("是否导出用户订单数据?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          window.open(`/api/edu/orders/export-orders?crazy-auth=${getToken()}`);
        });
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
      updateOrder(row, states) {
        this.$confirm("确定修改数据吗?", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            row.states = states;
            return updateOrder(row);
          })
          .then(() => {
            this.onLoad(this.page);
            this.$message({
              type: "success",
              message: "操作成功!"
            });
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
        this.page.currentPage = 1;
        this.onLoad(this.page);
      },
      searchChange(params,done) {
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
      'trxorderdetail':trxorderdetail,
    },
  };
</script>

<style>
</style>
