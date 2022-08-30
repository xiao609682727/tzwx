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
        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   plain
                   v-if="permission.websiterecommendcategory_delete"
                   @click="handleDelete">删 除
        </el-button>
      </template>
      <template slot-scope="scope" slot="menu">
        <el-button type="text" @click="showRecommend(scope.row)" >推荐详情</el-button>
      </template>
    </avue-crud>
    <el-dialog title="推荐详情" width="80%"  :visible.sync="dialogRecommendVisible">
      <websiterecommenddetail v-if="dialogRecommendVisible" :recommendId="recommendId"></websiterecommenddetail>
    </el-dialog>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove} from "@/api/web/websiterecommendcategory";
  import {mapGetters} from "vuex";
  import websiterecommenddetail from "./websiterecommenddetail";

  export default {
    data() {
      return {
        recommendId:"",
        dialogRecommendVisible:false,
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
          index: false,
          viewBtn: false,
          selection: true,
          labelWidth: 130,
          column: [
            {
              label: "ID",
              prop: "id",
              addDisplay:false,
              editDisplay:false,
            },
            {
              label: "推荐名称",
              prop: "name",
              search:true,
              rules: [{
                required: true,
                message: "请输入推荐名称",
                trigger: "blur"
              }]
            },
            {
              label: "链接",
              prop: "link",
              tip:"根据需要添加跳转连接"
            },
            // {
            //   label: "限制数量",
            //   prop: "coursenum",
            //   type: "number",
            //   span: 24
            // },
            // {
            //   label: "类型",
            //   prop: "type",
            //   value:"1",
            //   type: "select",
            //   search:true,
            //   span: 24,
            //   dicData:[{
            //       label:'课程',
            //       value: "1"
            //     }]
            // },
            {
              label: "推荐位置",
              prop: "frontType",
              search:true,
              type: "select",
              value: 1,
              span: 24,
              dicData:[{
                label:'首页推荐',
                value: 1
              }]
            },
            {
              label: "说明",
              prop: "description",
              span: 24,
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
          addBtn: this.vaildData(this.permission.websiterecommendcategory_add, false),
          viewBtn: this.vaildData(this.permission.websiterecommendcategory_view, false),
          delBtn: this.vaildData(this.permission.websiterecommendcategory_delete, false),
          editBtn: this.vaildData(this.permission.websiterecommendcategory_edit, false)
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
      showRecommend(row){
        this.recommendId = row.id;
        this.dialogRecommendVisible = true;
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
      'websiterecommenddetail':websiterecommenddetail,
    },
  };
</script>

<style>
</style>
