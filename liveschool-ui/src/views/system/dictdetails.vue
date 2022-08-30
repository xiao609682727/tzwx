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

      </template>
    </avue-crud>
  </basic-container>
</template>

<script>
  import {add, getDict, getDictTree, getList, remove, update} from "@/api/system/dict";
  import {mapGetters} from "vuex";

  export default {
    data() {
      return {
        drawer:true,
        form: {},
        selectionList: [],
        loading: true,
        query: {parentId:this.dict.id},
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
          index: true,
          selection: true,
          viewBtn: false,
          menuWidth: 300,
          column: [
            {
              label: "字典编号",
              prop: "code",
              disabled:true,
              value:this.dict.code,
              span: 24,
              hide: true,
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
              hide: false,
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
              hide: true,
            },
          ]
        },
        data: []
      };
    },
    props:{
      dict: Object
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
        this.drawer = true;
      },
      rowSave(row, done, loading) {
        row.parentId = this.dict.id;
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
        this.query.parentId=this.dict.id
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
          getDictTree().then(res => {
            const column = this.findObject(this.option.column, "parentId");
            column.dicData = res.data.data;
          });
        });
      }
    }
  };
</script>

<style>
</style>
