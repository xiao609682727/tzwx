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
                   v-if="permission.websiteimages_delete"
                   @click="handleDelete">删 除
        </el-button>
      </template>
    </avue-crud>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove} from "@/api/cms/websiteimages";
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
          align: "center",
          index: false,
          viewBtn: true,
          selection: true,
          column: [
            {
              label: "图片标题",
              prop: "title",
              span: 24,
              search:true,
              rules: [{
                required: true,
                message: "请输入图标题",
                trigger: "blur"
              }]
            },
            {
              label: "图片描述",
              prop: "details",
              span: 24,
              rules: [{
                required: true,
                message: "请输入图片描述",
                trigger: "blur"
              }]
            },
            {
              label: "图片类型",
              prop: "typeId",
              type: "select",
              search: true,
              dataType: "number",
              props: {
                label: "dictValue",
                value: "dictKey"
              },
              dicUrl: "/api/crazy-system/dict/dictionary?code=banner_type",
              span: 24,
              rules: [{
                required: true,
                message: "请输入图片类型",
                trigger: "blur"
              }]
            },
            {
              label: "链接地址",
              prop: "linkAddress",
              span: 24,
              tip:"地址为http://开头",
            },
            {
              label: "排序",
              prop: "seriesNumber",
              type: "number",
              value: 1,
              rules: [{
                required: true,
                message: "请输入序列号",
                trigger: "blur"
              }]
            },
/*            {
              label: "背景色",
              prop: "color",
              type: "color",
            },*/
            {
              label: "图片地址",
              prop: "imageUrl",
              type:"upload",
              dataType: 'string',
              span: 24,
              action: '/api/common/upload/size?path=banner&width=1920&height=480',
              listType: 'picture-img',
              propsHttp: {
                res: 'data'
              },
              tip: "请上传 1920*480(长X宽)像素 的图片",
              rules: [{
                required: true,
                message: "请输入图片地址",
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
          addBtn: this.vaildData(this.permission.websiteimages_add, false),
          viewBtn: this.vaildData(this.permission.websiteimages_view, false),
          delBtn: this.vaildData(this.permission.websiteimages_delete, false),
          editBtn: this.vaildData(this.permission.websiteimages_edit, false)
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
            let tipc = '';
            let actionS = '/api/common/upload/size?path=banner';
            let size = '&width=1920&height=480';
            if(res.data.data.typeId==1){
              tipc='请上传 1920*480(长X宽)像素 的图片'
            }else if(res.data.data.typeId==2){
              tipc='请上传 450*250(长X宽)像素 的图片'
              size='&width=450&height=250'
            }else if(res.data.data.typeId==3){
              tipc='请上传 1920*288(长X宽)像素 的图片'
              size='&width=1920&height=288'
            }else if(res.data.data.typeId==5){
              tipc='请上传 612*250(长X宽)像素 的图片'
              size='&width=612&height=250'
            }else if(res.data.data.typeId==6){
              tipc='请上传 1920*288(长X宽)像素 的图片'
              size='&width=1920&height=288'
            }
            else {
              tipc='请上传 1920*480(长X宽)像素 的图片'
            }
            this.option.column[5].tip= tipc;
            this.option.column[5].action= actionS+size;
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
    watch:{
      'form.typeId':{
        handler(val){
          if(val!=undefined&&val!=null&&val!=0){
            var imageUrlTit =this.findObject(this.option.column,'imageUrl');
            let tipc='';
            let actionS = '/api/common/upload/size?path=banner';
            let size = '&width=1920&height=480';
            debugger
            if(val==1){
              tipc='请上传 1920*480(长X宽)像素 的图片'
              let size = '&width=1920&height=480';
            }else if(val==2){
              tipc='请上传 450*250(长X宽)像素 的图片'
              let size = '&width=450&height=250';
            }else if(val==3){
              tipc='请上传 1920*288(长X宽)像素 的图片'
              let size = '&width=1920&height=288';
            }else if(val==5){
              tipc='请上传 612*250(长X宽)像素 的图片'
              let size = '&width=612*&height=250';
            }else if(val==6){
              tipc='请上传 1920*288(长X宽)像素 的图片'
              let size = '&width=1920&height=288';
            }else {
              tipc='请上传 1920*480(长X宽)像素 的图片'
            }
            imageUrlTit.tip=tipc;
            imageUrlTit.action=actionS+size;
          }
        },
        immediate: true
      },
    }
  };
</script>

<style>
</style>
