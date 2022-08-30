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
                   v-if="permission.article_delete"
                   @click="handleDelete">删 除
        </el-button>
      </template>
      <template slot-scope="scope" slot="menu">
<!--        <el-button type="text"
                   size="small"
                   icon="el-icon-edit"
                   plain
                   @click="showComment(scope.row,scope.index)">查看评价
        </el-button>-->
      </template>
    </avue-crud>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove} from "@/api/cms/article";
  import {mapGetters} from "vuex";

  export default {
    data() {
      return {
        buttonTitle:"修改排序",
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
          align: "center",
          cellBtn:false,
          editBtn:true,
          selection: true,
          dialogClickModal: false,
          column: [
            {
              label: "标题",
              prop: "title",
              span: 24,
              search: true,
              rules: [{
                required: true,
                message: "请输入标题",
                trigger: "blur"
              }]
            },
            {
              label: "文章摘要",
              prop: "summary",
              hide:true,
              span: 24,
              type: "textarea",
            },
            {
              label: "文章图片",
              prop: "imageUrl",
              type:"upload",
              dataType: 'string',
              span: 24,
              action: '/api/common/upload/size?path=article&width=872&height=540',
              propsHttp: {
                res: 'data'
              },
              tip:"（请上传872*540（长×宽）像素的图片）",
              listType: 'picture-img',
              rules: [{
                required: true,
                message: "请上传文章图片",
                trigger: "blur"
              }]
            },
            {
              label: "资讯类型",
              prop: "type",
              type: "select",
              value:"article",
              hide:true,
              display:false,
              rules: [{
                required: true,
                message: "请输入资讯类型",
                trigger: "blur"
              }]
            },
            {
              label: "专业分类",
              prop: "subjectId",
              type: "select",
              span: 24,
              search: true,
              props: {
                label: 'subjectName',
                value: 'id'
              },
              dicUrl:'/api/cms/subject/dictionary?type=article',
              rules: [{
                required: true,
                message: "请输入专业分类id",
                trigger: "blur"
              }]
            },
/*
            {
              label: "文章发布时间",
              prop: "publishTime",
              type: "datetime",
              format: "yyyy-MM-dd HH:mm:ss",
              valueFormat: "yyyy-MM-dd HH:mm:ss",
              rules: [{
                required: true,
                message: "请输入文章发布时间",
                trigger: "blur"
              }]
            },*/
            {
              label: "点击量",
              prop: "clickNum",
              type: "number",
              addDisplay:false,
              rules: [{
                required: true,
                message: "请输入点击量",
                trigger: "blur"
              }]
            },
           /* {
              label: "点赞数量",
              prop: "praiseCount",
              type: "number",
              addDisplay:false,
              hide:true,
              rules: [{
                required: true,
                message: "请输入点赞数量",
                trigger: "blur"
              }]
            },*/

            /*{
              label: "评论数",
              prop: "commentNum",
              type: "number",
              addDisplay:false,
              hide:true,
              rules: [{
                required: true,
                message: "请输入评论数",
                trigger: "blur"
              }]
            },*/
            {
              label: "排序值",
              prop: "sort",
              type: "number",
              cell:true,
              rules: [{
                required: true,
                message: "请输入排序值",
                trigger: "blur"
              }]
            },{
              label: "文章内容",
              prop: "content",
              hide:true,
              type:"ueditor",
              component: "avueUeditor",
              options:{
                //普通图片上传
                action: "/api/common/upload?path=article",
                props: {
                  res: "data",
                  url:'url'
                },
              },
              span: 24,
              rules: [{
                required: true,
                message: "请输入文章内容",
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
          addBtn: this.vaildData(this.permission.article_add, false),
          viewBtn: this.vaildData(this.permission.article_view, false),
          delBtn: this.vaildData(this.permission.article_delete, false),
          editBtn: this.vaildData(this.permission.article_edit, false)
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
      },
      handleEdit(row, index){
        if(this.buttonTitle == "保存"){
          this.buttonTitle = "修改排序";
        }else{
          this.buttonTitle = "保存";
        }
        this.$refs.crud.rowCell(row, index);
      },
      showComment(row){
        this.$router.push({
          path: '/cms/comment', query: {
            pCommentId: 0,
            otherId: row.id,
            type: 1
          }
        });
      }
    }
  };
</script>

<style>
</style>
