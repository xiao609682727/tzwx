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
                   v-if="permission.point_delete"
                   @click="handleDelete">删 除
        </el-button>
      </template>
      <template slot-scope="scope" slot="menu">
        <el-button
          type="text"
          icon="el-icon-edit"
          size="small"
          v-if="scope.row.parentId!='0'"
          @click.stop="handleEdit1(scope.row,scope.index)"
        >编辑
        </el-button>
        <el-button
          type="text"
          icon="el-icon-edit"
          size="small"
          v-if="scope.row.parentId=='0'"
          @click.stop="handleEdit2(scope.row,scope.index)"
        >编辑
        </el-button>
        <el-button
          type="text"
          icon="el-icon-circle-plus-outline"
          size="small"
          v-if="scope.row.parentId == 0"
          @click.stop="handleAdd(scope.row,scope.index)"
        >新增子项
        </el-button>
      </template>
      <template slot-scope="scope" slot="subjectIdForm">
        <el-cascader
          placeholder="专业分类"
          :options="subjectExamTreeList"
          v-model="form.subjectId"
          v-if="scope.row.parentId == 0"
          :props="{'label':'title','emitPath':false}"
          filterable></el-cascader>
      </template>
      <template slot-scope="scope" slot="subjectIdSearch">
        <el-cascader
          placeholder="专业分类"
          :options="subjectExamTreeList"
          v-model="search.subjectId"
          v-if="scope.row.parentId == 0"
          :props="{'label':'title','emitPath':false}"
          filterable clearable></el-cascader>
      </template>
    </avue-crud>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove, subjectExamTree} from "@/api/exam/point";
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
        subjectExamTreeList:[],
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
          editBtn: false,
          column: [
            {
              label: "名称",
              prop: "name",
              rules: [{
                required: true,
                message: "请输入名称",
                trigger: "blur"
              }]
            },
            {
              label: "父级id",
              prop: "parentId",
              hide: true,
              type: "tree",
              span: 24,
              addDisplay: false,
              editDisplay: false,
              value: 0,
              dicUrl: "/api/exam/point/tree",
              props: {
                label: "title"
              },
            },{
              label: "排序值",
              prop: "sort",
              rules: [{
                required: true,
                message: "请输入排序值",
                trigger: "blur"
              }]
            },
            {
              label: "专业分类",
              prop: "subjectId",
              type: "cascader",
              searchLabelWidth:110,
              span: 24,
              search :true,
              formslot:true,
              searchslot:true,
              value:0,
              dicUrl: "/api/edu/subject/exam/tree",
              props: {
                label: "title"
              },
              tip:"考点分类只可以选择二级分类",
              placeholder:"请输入要选择的专业，支持搜索",
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
          addBtn: this.vaildData(this.permission.point_add, false),
          viewBtn: this.vaildData(this.permission.point_view, false),
          delBtn: this.vaildData(this.permission.point_delete, false),
          editBtn: this.vaildData(this.permission.point_edit, false)
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

        });
        this.$refs.crud.rowAdd();
      },
      handleEdit1(row,index) {
        this.$refs.crud.option.column.filter(item => {
          if (item.prop === "subjectId") {
            item.editDisplay = false;
          }
        });
        this.$refs.crud.rowEdit(row,index);
      },
      handleEdit2(row,index) {
        this.$refs.crud.option.column.filter(item => {
          if (item.prop === "subjectId") {
            item.editDisplay =true;
          }
        });
        this.$refs.crud.rowEdit(row,index);
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
      onLoad(page, params = {}) {
        this.loading = true;
        getList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
          /*this.page.total = data.total;*/
          this.data = data;
          this.loading = false;
          this.selectionClear();
        });
      }
    },
    created:function(){
      subjectExamTree().then(res =>{
        this.subjectExamTreeList = res.data.data;
        console.log(this.subjectExamTreeList);
      })
    },
  };
</script>

<style>
</style>
