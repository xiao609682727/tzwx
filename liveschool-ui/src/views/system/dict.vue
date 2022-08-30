<template>
  <basic-container>
    <avue-crud :option="option"
               :table-loading="loading"
               :data="data"
               ref="crud"
               :page="page"
               v-model="form"
               :permission="permissionList"
               :before-open="beforeOpen"
               @row-del="rowDel"
               @row-update="rowUpdate"
               @row-save="rowSave"
               @search-change="searchChange"
               @search-reset="searchReset"
               @selection-change="selectionChange"
               @current-change="currentChange"
               @size-change="sizeChange"
               @refresh-change="refreshChange"
               @on-load="onLoad">
      <template slot="menuLeft">
        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   v-if="permission.dict_delete"
                   plain
                   @click="handleDelete">删 除
        </el-button>
      </template>
      <template slot-scope="scope" slot="menu">
        <el-button
          type="text"
          icon="el-icon-circle-plus-outline"
          size="small"
          @click.stop="handleAdd(scope.row)"
        >字典详情
        </el-button>
      </template>
    </avue-crud>
    <el-drawer
      title="字典详情"
      :visible.sync="drawer"
      size="70%"
      direction="rtl">
      <dictdetails v-if="drawer" :dict="dict" ></dictdetails>
    </el-drawer>
  </basic-container>
</template>

<script>
  import {add, getDict, getDictTree, getList, remove, update} from "@/api/system/dict";
  import {mapGetters} from "vuex";
  import dictdetails from "./dictdetails";

  export default {
    data() {
      return {
        dict: {},
        drawer:false,
        form: {},
        selectionList: [],
        loading: true,
        query: {parentId:"0"},
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        option: {
          height: 'auto',
          calcHeight: 80,
          searchShow: true,
          searchMenuSpan: 6,
          tip: false,
          tree: true,
          border: true,
          index: false,
          selection: true,
          viewBtn: false,
          menuWidth: 300,
          column: [
            {
              label: "字典编号",
              prop: "code",
              search: true,
              span: 24,
              rules: [{
                required: true,
                message: "请输入字典编号",
                trigger: "blur"
              }]
            },
            {
              label: "字典名称",
              prop: "dictValue",
              search: true,
              rules: [{
                required: true,
                message: "请输入字典名称",
                trigger: "blur"
              }]
            },
            {
              label: "上级字典",
              prop: "parentId",
              type: "tree",
              dicData: [],
              hide: true,
              addDisplay:false,
              editDisplay:false,
              props: {
                label: "title"
              },
              rules: [{
                required: false,
                message: "请选择上级字典",
                trigger: "click"
              }]
            },
            {
              label: "字典键值",
              prop: "dictKey",
              type: "number",
              addDisplay:false,
              editDisplay:false,
              hide: true,
              rules: [{
                required: true,
                message: "请输入字典键值",
                trigger: "blur"
              }]
            },
            {
              label: "字典排序",
              prop: "sort",
              type: "number",
              rules: [{
                required: true,
                message: "请输入字典排序",
                trigger: "blur"
              }]
            },
            {
              label: "字典备注",
              prop: "remark",
              /*addDisplay:false,
              editDisplay:false,*/
              span: 24,
            },
          ]
        },
        data: []
      };
    },
    computed: {
      ...mapGetters(["userInfo", "permission"]),
      permissionList() {
        return {
          addBtn: this.vaildData(this.permission.dict_add, false),
          viewBtn: this.vaildData(this.permission.dict_view, false),
          delBtn: this.vaildData(this.permission.dict_delete, false),
          editBtn: this.vaildData(this.permission.dict_edit, false)
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
        this.dict = row;
        this.drawer = true;
      },
      rowSave(row, done, loading) {
        row.dictKey = "-1";
        row.parentId = "0";
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
      searchReset() {
        this.page.currentPage = 1;
        this.onLoad(this.page);
      },
      searchChange(params, done) {
        this.query = params;
        this.page.currentPage = 1;
        this.onLoad(this.page, params);
        done();
      },
      selectionChange(list) {
        this.selectionList = list;
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
          getDict(this.form.id).then(res => {
            this.form = res.data.data;
          });
        }
        done();
      },
      refreshChange() {
        this.onLoad(this.page, this.query);
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
          getDictTree().then(res => {
            const column = this.findObject(this.option.column, "parentId");
            column.dicData = res.data.data;
          });
        });
      }
    },
    components:{
      'dictdetails':dictdetails,
    },
  };
</script>

<style>
</style>
