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
        <el-button type="primary"
                   size="small"
                   icon="el-icon-plus"
                   @click="dialogCourseVisible = true">新增
        </el-button>
        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   plain
                   @click="handleDelete">删 除
        </el-button>
      </template>
    </avue-crud>
    <el-dialog title="课程列表" width="80%"  :visible.sync="dialogCourseVisible" append-to-body>
      <addcourse @adddata="adddata" v-if="dialogCourseVisible" ></addcourse>
    </el-dialog>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove, saveBatch} from "@/api/web/websiterecommenddetail";
  import {mapGetters} from "vuex";
  import addcourse from "./addcourse";

  export default {
    data() {
      return {
        dialogCourseVisible:false,
        form: {},
        query: {recommendId:this.recommendId},
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
          index: false,
          viewBtn: false,
          editBtn:true,
          selection: true,
          addBtn: false,
          column: [
            {
              label: "主键",
              prop: "id",
              editDisplay: false,
              rules: [{
                required: true,
                message: "请输入主键",
                trigger: "blur"
              }]
            },
            // {
            //   label: "推荐分类的id",
            //   prop: "recommendId",
            //   hide:true,
            //   addDisplay:false,
            //   editDisplay:false,
            //   rules: [{
            //     required: true,
            //     message: "请输入推荐分类的id",
            //     trigger: "blur"
            //   }]
            // },
            // {
            //   label: "业务id  根据推荐类型判断",
            //   prop: "busId",
            //   hide: true,
            //   rules: [{
            //     required: true,
            //     message: "请输入业务id  根据推荐类型判断",
            //     trigger: "blur"
            //   }]
            // },
            {
              label: "课程名称",
              prop: "courseName",
              editDisplay: false,
              search:true,
            },
            {
              label: "课程类型",
              prop: "sellType",
              dataType: "string",
              editDisplay: false,
              value: "1",
              dicUrl: "/api/crazy-system/dict/dictionary?code=course_type",
              props: {
                label: "dictValue",
                value: "dictKey"
              },
            },
            {
              label: "排序",
              prop: "sort",
              rules: [{
                required: true,
                message: "请输入课程显示排序",
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
      recommendId: Number
    },
    methods: {
      adddata(selectionList){
        this.dialogCourseVisible = false;
        let list = [];
        selectionList.forEach(ele => {
          list.push({
            "recommendId":this.recommendId,
            "busId": ele.id,
            "sort":"1"
          });
        });
        saveBatch(list,this.recommendId).then(() => {
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
        }, error => {
          console.log(error);
        });
      },
      rowSave(row, done, loading) {
        row.recommendId = this.recommendId;
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
        this.query = {recommendId:this.recommendId};
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
      onLoad(page, params = {recommendId:this.recommendId}) {
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
      'addcourse':addcourse,
    },
  };
</script>

<style>
</style>
