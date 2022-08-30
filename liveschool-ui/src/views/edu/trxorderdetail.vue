<template>
  <basic-container>
    <avue-crud :option="option"
               :table-loading="loading"
               :data="data"
               :page="page"
               :permission="permissionList"
               :before-open="beforeOpen"
               v-model="form"
               :search.sync="search"
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
        <el-button v-if="scope.row.trxorderType!='ACCOUNT'&&scope.row.authStatus!=4" type="text" @click="updateOrderDetail(scope.row,'4')" >关闭</el-button>
      </template>
    </avue-crud>

  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove} from "@/api/edu/trxorderdetail";
  import {mapGetters} from "vuex";

  export default {
    data() {
      return {
        form: {},
        srarch:{},
        query: {trxorderId:this.orderId},
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
          delBtn:false,
          selection: true,
          editBtnText:"延期",
          editBtn: this.orderType,
          labelWidth: 130,
          column: [
            {
              label: "订单号",
              prop: "orderNo",
              editDisplay:false,
            },
            {
              label: "有效期类型",
              prop: "losetype",
              editDisplay:false,
              hide: true,
            },
            {
              label: "订单过期时间段",
              prop: "loseAbsTime",
              editDisplay:false,
              hide: true,
            },
            {
              label: "订单过期时间点",
              prop: "loseTime",
              editDisplay:false,
              hide: true,
            },
            {
              label: "商品开始时间",
              prop: "beginTime",
              editDisplay:false,
              hide: true,
            },
            {
              label: "商品过期时间",
              prop: "authTime",
              type: "datetime",
              format: "yyyy-MM-dd HH:mm:ss",
              valueFormat: "yyyy-MM-dd HH:mm:ss",
            },
            {
              label: "下单时间",
              prop: "createTime",
              editDisplay:false,
            },
            {
              label: "支付成功时间",
              prop: "payTime",
              editDisplay:false,
              hide: true,
            },
            {
              label: "原价格",
              prop: "sourcePrice",
              editDisplay:false,
              hide: true,
            },
            {
              label: "优惠价格",
              prop: "couponPrice",
              editDisplay:false,
              hide: true,
            },
            {
              label: "销售价格",
              prop: "currentPrice",
              editDisplay:false,
              hide: true,
            },
            {
              label: "课程名称",
              prop: "courseName",
              editDisplay:false,
              search:true,
            },
            {
              label: "课程状态",
              prop: "authStatus",
              type:"select",
              editDisplay:false,
              dicData:[
                {
                  label:'未支付',
                  value:"1"
                },{
                  label:'成功',
                  value:"2"
                },
                {
                  label:'退款',
                  value:"3"
                },{
                  label:'关闭',
                  value:"4"
                }
              ]
            },
            {
              label: "描述",
              prop: "description",
              editDisplay:false,
              hide: true,
            },
            {
              label: "最后更新时间",
              prop: "lastUpdateTime",
              editDisplay:false,
              hide: true,
            },
            {
              label: "过期是否提醒",
              prop: "remindStatus",
              editDisplay:false,
              type:"select",
              hide: true,
              dicData:[
                {
                  label:'未提醒',
                  value:"init"
                },{
                  label:'已提醒',
                  value:"already"
                }
              ]
            },
            {
              label: "流水类型",
              prop: "trxorderType",
              type:"select",
              editDisplay:false,
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
          addBtn: this.vaildData(true, false),
          viewBtn: this.vaildData(true, false),
          delBtn: this.vaildData(true, false),
          editBtn: this.vaildData(true, false)
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
      orderId: Number,
      orderType: Boolean
    },
    methods: {
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
      updateOrderDetail(row, status) {
        this.$confirm("确定修改数据吗?", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            row.authStatus = status;
            return update(row);
          })
          .then(() => {
            this.onLoad(this.page);
            this.$message({
              type: "success",
              message: "操作成功!"
            });
          });
      },
      updateTime(row) {

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
        this.query = {trxorderId:this.orderId};
          this.page.currentPage = 1;
        this.onLoad(this.page);
      },
      searchChange(params,done) {
        this.query.trxorderId=this.orderId;
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
      onLoad(page, params = {trxorderId:this.orderId}) {
        this.loading = true;
        getList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
          this.page.total = data.total;
          this.data = data.records;
          this.loading = false;
          this.selectionClear();
        });
      }
    }
  };
</script>

<style>
</style>
