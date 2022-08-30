<template>
  <basic-container>
    <avue-crud :option="option"
               :table-loading="loading"
               :data="data"
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
                   v-if="permission.subject_delete"
                   @click="handleDelete">删 除
        </el-button>
      </template>
      <template slot-scope="scope" slot="menu">
        <el-button
          type="text"
          icon="el-icon-circle-plus-outline"
          size="small"
          v-if="scope.row.parentId == 0"
          @click.stop="handleAdd(scope.row,scope.index)"
        >新增子项
        </el-button>
      </template>
    </avue-crud>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove} from "@/api/edu/subject";
  import {mapGetters} from "vuex";

  export default {
    data() {
      return {
        form: {},
        query: {type:"course"},
        loading: true,
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        selectionList: [],
        option: {
          menuWidth:350,
          tip: false,
          tree: true,
          border: true,
          index: false,
          viewBtn: true,
          selection: true,
          columnBtn:false,
          refreshBtn:false,
          column: [
            {
              label: "分类名称",
              prop: "subjectName",
              span: 24,
              rules: [{
                required: true,
                message: "请输入专业名称",
                trigger: "blur"
              }]
            },
            {
              label: "上级类型",
              prop: "parentId",
              hide: true,
              type: "tree",
              span: 24,
              addDisplay: false,
              editDisplay: false,
              value: 0,
              dicUrl: "/api/edu/subject/tree",
              props: {
                label: "title"
              },
            },
            {
              label: "专业类型",
              prop: "type",
              type: "select",
              hide: true,
              addDisplay: false,
              editDisplay: false,
              viewDisplay:false,
              value: "course",
              props: {
                label: "dictValue",
                value: "dictKey"
              },
              dicUrl: "/api/crazy-system/dict/dictionary?code=subject_type",
            },
            {
              label: "排序字段",
              prop: "sort",
              value: "0",
              span: 24,
              type: "number",
              rules: [{
                required: true,
                message: "请输入排序字段",
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
          addBtn: this.vaildData(this.permission.subject_add, false),
          viewBtn: this.vaildData(this.permission.subject_view, false),
          delBtn: this.vaildData(this.permission.subject_delete, false),
          editBtn: this.vaildData(this.permission.subject_edit, false)
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
      handleAdd(row) {
        this.$refs.crud.value.parentId = row.id;
        this.$refs.crud.option.column.filter(item => {
          if (item.prop === "parentId") {
            item.value = row.id;
          }
          if (item.prop === "subjectName") {
            item.value = "";
          }
        });
        this.$refs.crud.rowAdd();
      },
      rowSave(row, loading, done) {
        row.type="course"
        add(row).then(() => {
          loading();
          this.onLoad(this.page);
          this.$refs.crud.option.column.filter(item => {
            if (item.prop === "parentId") {
              item.value = "";
            }
          });
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
      searchChange(params, done) {
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
          // this.page.total = data.total;
          this.data = data;
          this.loading = false;
          this.selectionClear();

        });
      }
    }
  };
</script>

<style>
</style>
