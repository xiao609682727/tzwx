<template>
  <basic-container>
    <avue-crud :option="option"
               :table-loading="loading"
               :data="data"
               :permission="permissionList"
               :before-open="beforeOpen"
               :before-close="beforeClose"
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
        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   plain
                   v-if="permission.helpmenu_delete"
                   @click="handleDelete">删 除
        </el-button>
      </template>
      <template slot-scope="scope" slot="menu">
        <el-button
          type="text"
          icon="el-icon-circle-plus-outline"
          size="small"
          @click.stop="handleAdd(scope.row,scope.index)"
        >新增子项
        </el-button>
      </template>
    </avue-crud>
  </basic-container>
</template>

<script>
  import {getList, getDetail,getHelpMenuTree, add, update, remove} from "@/api/web/helpmenu";
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
          tip: false,
          border: true,
          tree: true,
          index: false,
          viewBtn: false,
          selection: true,
          column: [
            {
              label: "父级",
              prop: "parentId",
              hide:true,
              type: "tree",
              dicData: [],
              addDisplay: false,
              editDisplay:false,
              span:24,
              // tip: "如不选择则创建顶层菜单",
              props: {
                label: "title"
              },
            },
            {
              label: "菜单名称",
              prop: "name",
              span:24,
              rules: [{
                required: true,
                message: "请输入菜单名称",
                trigger: "blur"
              }]
            },
            {
              label: "菜单内容",
              prop: "content",
              hide:true,
              span:24,
              type:"ueditor",
              component: "avueUeditor",
              options:{
                //普通图片上传
                action: "/api/common/upload?path=help",
                props: {
                  res: "data",
                  url:'url'
                },
              },
            },
            {
              label: "排序",
              prop: "sort",
              type:"number",
              span:24,
              value:1
            },
            {
              label: "外链",
              prop: "linkBuilding",
              hide:true,
              editDisplay: false,
              viewDisplay: false,
              span:24,
              tip:"(选填)如填写外链则跳转到外链地址!"
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
          addBtn: this.vaildData(this.permission.helpmenu_add, false),
          viewBtn: this.vaildData(this.permission.helpmenu_view, false),
          delBtn: this.vaildData(this.permission.helpmenu_delete, false),
          editBtn: this.vaildData(this.permission.helpmenu_edit, false)
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
            item.addDisabled = true;
            item.addDisplay = true;
          }
          if (item.prop === "subjectName") {
            item.value = "";
          }
        });
        this.$refs.crud.rowAdd();
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
            this.$refs.crud.option.column.filter(item => {
              if (item.prop === "parentId") {
                 item.editDisplay = false ;
              }
            });
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
      refreshChange() {
        this.onLoad(this.page, this.query);
      },
      onLoad(page, params = {}) {
        this.loading = true;
        getList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
          // this.page.total = data.total;
          this.data = data;
          this.loading = false;
          this.selectionClear();
          getHelpMenuTree().then(res => {
            var parentId =this.findObject(this.option.column,'parentId')
            parentId.dicData = res.data.data;
          });
        });
      },
      beforeClose(done, type) {
        if (["add"].includes(type)) {
          this.$refs.crud.value.parentId = '';
          this.$refs.crud.option.column.filter(item => {
            if (item.prop === "parentId") {
              item.value = '';
              item.addDisabled = false;
              item.addDisplay = false;
            }
          });
        }
        if (["edit"].includes(type)) {
          this.$refs.crud.value.parentId = '';
          this.$refs.crud.option.column.filter(item => {
            if (item.prop === "parentId") {
              item.value = '';
              item.addDisabled = false;
              item.editDisplay = false;
            }
          });
        }
        done();
      },
    }
  };
</script>

<style>
</style>
