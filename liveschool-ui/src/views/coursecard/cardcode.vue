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
      <template slot="menuLeft">

        <el-button type="warning"
                   size="small"
                   plain
                   icon="el-icon-download"
                   @click="handleExport">导出
        </el-button>
      </template>
      <template slot="menuLeft">
        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   plain
                   v-if="permission.cardcode_delete"
                   @click="handleDelete">删 除
        </el-button>
      </template>
      <template slot-scope="scope" slot="menu">
        <el-button type="text" v-if="scope.row.status=='init'" icon="el-icon-edit" size="small" @click="handleInvalid(scope.row)">作废</el-button>

      </template>
    </avue-crud>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove,updateInvalidCourseCardCode} from "@/api/courseCard/cardcode";
  import {mapGetters} from "vuex";
  import {getToken} from '@/util/auth';
  export default {
    data() {
      return {
        form: {},
        query: {cardId:this.cardId},
        loading: true,
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        selectionList: [],
        option: {
          height: 'auto',
          calcHeight: 80,
          align: "center",
          searchShow: true,
          searchMenuSpan: 6,
          tip: false,
          border: true,
          index: false,
          viewBtn: true,
          selection: true,
          column: [
            {
              label: "主键id",
              prop: "id",

              rules: [{
                required: true,
                message: "请输入主键id",
                trigger: "blur"
              }]
            },
            {
              label: "card_id",
              prop: "cardId",
              hide:true,
              rules: [{
                required: true,
                message: "请输入card_id",
                trigger: "blur"
              }]
            },
            {
              label: "卡编码",
              prop: "cardCode",
              search: true,
              rules: [{
                required: true,
                message: "请输入卡编码",
                trigger: "blur"
              }]
            },
            {
              label: "密码",
              prop: "cardCodePassword",
              rules: [{
                required: true,
                message: "请输入密码",
                trigger: "blur"
              }]
            },
            {
              label: "状态",
              prop: "status",
              type: "select",
              search: true,
              dicData:[
                {
                  label:'未使用',
                  value:'init'
                },{
                  label:'已使用',
                  value:'used'
                },{
                  label:'已作废',
                  value:'invalid'
                }
              ],
              rules: [{
                required: true,
                message: "请输入学习卡init 未使用,used 已经使用,overdue 已过期",
                trigger: "blur"
              }]
            },
            {
              label: "学员",
              prop: "displayName",
              search: true,
              rules: [{
                required: true,
                message: "请输入手机/账号",
                trigger: "blur"
              }]
            },
            {
              label: "用户id",
              prop: "userId",
              search: true,
              rules: [{
                required: true,
                message: "请输入用户id",
                trigger: "blur"
              }]
            },
            {
              label: "订单号",
              prop: "requestId",
              search: true,
              rules: [{
                required: true,
                message: "请输入订单号",
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
              label: "使用时间",
              prop: "useTime",
              rules: [{
                required: true,
                message: "请输入使用时间",
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
          addBtn: this.vaildData(this.permission.cardcode_add, false),
          viewBtn: this.vaildData(this.permission.cardcode_view, false),
          delBtn: this.vaildData(this.permission.cardcode_delete, false),
          editBtn: this.vaildData(this.permission.cardcode_edit, false)
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
      cardId: Number
    },
    watch: {
      cardId: {
        deep: true,
        handler(newVal){
          this.query = {cardId:newVal};
          this.page.currentPage = 1;
          this.onLoad(this.page);
          this.$refs.crud.searchReset();
        }
      }
    },
    methods: {
      handleExport() {
        this.$confirm("是否导出数据?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          window.open(`/api/coursecard/cardcode/export-cardCode?crazy-auth=${getToken()}`+
                  `&cardId=`+this.cardId
          );
        });
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
        this.query = {cardId:this.cardId};
        this.page.currentPage = 1;
        this.onLoad(this.page);
      },
      searchChange(params,done) {
        this.query = params;
        this.query.cardId = this.cardId;
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
      handleInvalid(row) {
        this.$confirm("确定要作废吗？", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          // return remove(this.ids);
          let params={
            "id":row.id
          }
          updateInvalidCourseCardCode(params).then(res => {
            this.onLoad(this.page);
            this.$message({
              type: "success",
              message: "成功!"
            });
            this.$refs.crud.toggleSelection();
          });
        }).then(() => {

        });
        // this.onLoad(this.page, this.query);
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
    }
  };
</script>

<style>
</style>
