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
        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   plain
                   v-if="permission.msgreceive_delete"
                   @click="handleDelete">删 除
        </el-button>
      </template>
    </avue-crud>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove} from "@/api/edu/msgreceive";
  import {mapGetters} from "vuex";

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
        selectionList: [],
        option: {
          height: 'auto',
          calcHeight: 80,
          searchShow: true,
          searchMenuSpan: 6,
          tip: false,
          border: true,
          align: "center",
          index: true,
          viewBtn: true,
          selection: true,
          column: [
            {
              label: "主键",
              prop: "id",
              rules: [{
                required: true,
                message: "请输入主键",
                trigger: "blur"
              }]
            },
            {
              label: "添加时间",
              prop: "createTime",
              rules: [{
                required: true,
                message: "请输入添加时间",
                trigger: "blur"
              }]
            },
            {
              label: "发信人用户id",
              prop: "cusId",
              rules: [{
                required: true,
                message: "请输入发信人用户id",
                trigger: "blur"
              }]
            },
            {
              label: "信内容",
              prop: "content",
              rules: [{
                required: true,
                message: "请输入信内容",
                trigger: "blur"
              }]
            },
            {
              label: "类型1系统通知2站内信5课程过期6优惠券过期",
              prop: "type",
              rules: [{
                required: true,
                message: "请输入类型1系统通知2站内信5课程过期6优惠券过期",
                trigger: "blur"
              }]
            },
            {
              label: "0未读1已读2接受3拒绝4黑名单",
              prop: "status",
              rules: [{
                required: true,
                message: "请输入0未读1已读2接受3拒绝4黑名单",
                trigger: "blur"
              }]
            },
            {
              label: "收信人id",
              prop: "receivingCusid",
              rules: [{
                required: true,
                message: "请输入收信人id",
                trigger: "blur"
              }]
            },
            {
              label: "会员名",
              prop: "showname",
              rules: [{
                required: true,
                message: "请输入会员名",
                trigger: "blur"
              }]
            },
            {
              label: "如果是系统通知 ALL 所有学员 COURSE 课程专业学员 EXAM 考试专业学员 发送范围(CONTENT 学习内容的学员) STUDENT 按学员",
              prop: "sendRange",
              rules: [{
                required: true,
                message: "请输入如果是系统通知 ALL 所有学员 COURSE 课程专业学员 EXAM 考试专业学员 发送范围(CONTENT 学习内容的学员) STUDENT 按学员",
                trigger: "blur"
              }]
            },
            {
              label: "学习内容类别(COURSE课程EXAM考试CLASS班级)",
              prop: "contentType",
              rules: [{
                required: true,
                message: "请输入学习内容类别(COURSE课程EXAM考试CLASS班级)",
                trigger: "blur"
              }]
            },
            {
              label: "学习内容ID",
              prop: "contentId",
              rules: [{
                required: true,
                message: "请输入学习内容ID",
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
          addBtn: this.vaildData(this.permission.msgreceive_add, false),
          viewBtn: this.vaildData(this.permission.msgreceive_view, false),
          delBtn: this.vaildData(this.permission.msgreceive_delete, false),
          editBtn: this.vaildData(this.permission.msgreceive_edit, false)
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
