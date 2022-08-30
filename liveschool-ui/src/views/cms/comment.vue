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
                   v-if="permission.comment_delete"
                   @click="handleDelete">删 除
        </el-button>
      </template>
      <template slot-scope="scope" slot="menu">
        <el-button type="text"
                   size="small"
                   icon="el-icon-edit"
                   plain
                   @click="showReply(scope.row,scope.index)">回复
        </el-button>
        <el-button type="text"
                   size="small"
                   icon="el-icon-edit"
                   plain
                   @click="showComment(scope.row,scope.index)">回复列表
        </el-button>
      </template>


    </avue-crud>
    <el-dialog
      title="回复"
      :visible.sync="dialogVisible"
      width="30%">
        <el-input type="textarea" v-model="replyTextarea"></el-input>
      <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="reply()">确 定</el-button>
        </span>
    </el-dialog>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove, reply} from "@/api/cms/comment";
  import {mapGetters} from "vuex";

  export default {
    data() {
      return {
        replyData:{},
        dialogVisible: false,
        replyTextarea:"",
        form: {},
        query: {
          parentId:this.$route.query.pCommentId,
          otherId:this.$route.query.otherId,
          type:this.$route.query.type
        },
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
          editBtn: false,
          addBtn: false,
          selection: true,
          column: [
            {
              label: "id",
              prop: "id",
            },
            {
              label: "用户id",
              prop: "userId",
              rules: [{
                required: true,
                message: "请输入用户id",
                trigger: "blur"
              }]
            },
            {
              label: "用户昵称",
              prop: "showName",
            },
/*            {
              label: "父级评论id(为0则是一级评论,不为0则是回复)",
              prop: "pCommentId",
              rules: [{
                required: true,
                message: "请输入父级评论id(为0则是一级评论,不为0则是回复)",
                trigger: "blur"
              }]
            },*/
            {
              label: "评论内容",
              prop: "content",
              rules: [{
                required: true,
                message: "请输入评论内容",
                trigger: "blur"
              }]
            },
            {
              label: "创建时间",
              prop: "addtime",
              rules: [{
                required: true,
                message: "请输入创建时间",
                trigger: "blur"
              }]
            },
            {
              label: "点赞数量",
              prop: "praiseCount",
              rules: [{
                required: true,
                message: "请输入点赞数量",
                trigger: "blur"
              }]
            },
/*            {
              label: "角色类型",
              prop: "roleType",
              rules: [{
                required: true,
                message: "请输入",
                trigger: "blur"
              }]
            },*/
           /* {
              label: "评分",
              prop: "commentScore",
              rules: [{
                required: true,
                message: "请输入评分",
                trigger: "blur"
              }]
            },*/
            {
              label: "排序",
              prop: "commentSort",
              rules: [{
                required: true,
                message: "请输入排序",
                trigger: "blur"
              }]
            },
            /*{
              label: "是否置顶，0不置顶，1置顶",
              prop: "commentTop",
              hide: true,
              rules: [{
                required: true,
                message: "请输入是否置顶，0不置顶，1置顶",
                trigger: "blur"
              }]
            },*/
          ]
        },
        data: []
      };
    },
    computed: {
      ...mapGetters(["permission"]),
      permissionList() {
        return {
          addBtn: this.vaildData(this.permission.comment_add, false),
          viewBtn: this.vaildData(this.permission.comment_view, false),
          delBtn: this.vaildData(this.permission.comment_delete, false),
          editBtn: this.vaildData(this.permission.comment_edit, false)
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
          });
        }
        done();
      },
      searchReset() {
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
      onLoad(page, params = {}) {
        this.loading = true;
        getList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
          this.page.total = data.total;
          this.data = data.records;
          this.loading = false;
          this.selectionClear();
        });
      },
      showComment(row){
        this.$router.push({
          path: '/cms/replyComment', query: {
            pCommentId: row.id,
            type: 1
          }
        });
      },
      reply(){
        let data = {
          id:this.replyData.id,
          content: this.replyTextarea
        }
        reply(data).then(() => {
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
          this.dialogVisible = false;
        }, error => {
          console.log(error);
        });
      },
      showReply(row){
        this.replyTextarea = "";
        this.replyData = row;
        this.dialogVisible = true;
      }
    }
  };
</script>

<style>
</style>
