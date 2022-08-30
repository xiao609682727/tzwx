<template>
  <basic-container>
    <avue-crud :option="option"
               :table-loading="loading"
               :data="data"
               :page="page"
               :permission="permissionList"
               :before-open="beforeOpen"
               :upload-before="uploadBefore"
               :upload-after="uploadAfter"
               :upload-preview="uploadPreview"
               v-model="form"
               ref="crud"
               @row-update="rowUpdate"
               @row-save="rowSave"
               @row-del="rowDel"
               @search-change="searchChange"
               @selection-change="selectionChange"
               @current-change="currentChange"
               @size-change="sizeChange"
               @refresh-change="refreshChange"
               @on-load="onLoad">
      <template slot-scope="scope" slot="menu">
        <el-button type="text" size="small" icon="el-icon-view" @click="playVideo(scope.row)" >预览</el-button>
        <el-button type="text" size="small" icon="el-icon-edit" @click="handleEdit(scope.row,scope.index)" >编辑</el-button>
        <el-button type="text" size="small" icon="el-icon-delete" @click="rowDel(scope.row)" >删除</el-button>
      </template>
    </avue-crud>
  </basic-container>
</template>


<script>

import {getList , add , update , remove} from "@/api/edu/courseMaterial";

  export default {
    data() {
      return {
        query: {
          kpointId:this.kpointId,
          courseId:this.courseId
        },
        loading: true,
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        form: {
          imgUrl:[]
        },
        data: [],
        option:{
          align:'center',
          menuAlign:'center',
          editBtn:false,
          delBtn:false,
          column:[
            {
              label: '文件上传',
              prop: 'fileUrl',
              type: 'upload',
              span: 24,
              limit: 1,
              drag: true,
              showColumn:false,
              propsHttp: {
                res: 'data',
                name: 'fileName',
                url: 'url'
              },
              action: '/api/edu/coursematerial/fileUpload'
            },
            {
              label:'资料名称',
              prop:'name',
              search:true,
              span:15,
              rules: [{
                required: true,
                message: "请输入资料名称",
                trigger: "blur"
              }]
            },
            {
              label: "排序",
              prop: "sort",
              type: "number",
              value: 1,
              span:15,
              rules: [{
                required: true,
                message: "请输入显示排序",
                trigger: "blur"
              }]
            }
          ]
        }
      }
    },
    props:{
      kpointId: Number,
      courseId: Number
    },
    watch: {
      kpointId: {
        deep: true,
        handler(newVal){
          this.query.kpointId = newVal;
          this.onLoad(this.page);
        }
      },
      courseId: {
        deep: true,
        handler(newVal){
          this.query.courseId = newVal;
          this.onLoad(this.page);
        }
      }
    },
    methods:{
      selectionChange(list) {
        this.selectionList = list;
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
        let paramsNew = {
          "kpointId" : this.kpointId ,
          "courseId": this.courseId
        }
        getList(page.currentPage, page.pageSize, Object.assign(paramsNew, this.query)).then(res => {
          const data = res.data.data;
          this.page.total = data.total;
          this.data = data.records;
          this.loading = false;
          this.selectionClear();
        });
      },
      selectionClear() {
        this.selectionList = [];
        this.$refs.crud.toggleSelection();
      },
      playVideo(row){
        window.open(row.fileUrl)
      },
      searchChange(params,done) {
        let paramsNew = {
          "kpointId" : this.kpointId ,
          "name" : params.name
        }
        this.query = params;
        this.page.currentPage = 1;
        this.onLoad(this.page, paramsNew);
        done()

      },
      rowSave(row, loading, done) {
        let rowNew = {
          "kpointId": this.kpointId ,
          "courseId": this.courseId ,
          "name":row.name ,
          "fileName":row.fileUrl[0].label ,
          "fileUrl":row.fileUrl[0].value ,
          "sort":row.sort
        }
        add(rowNew).then(() => {
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
        row = {
          "id":this.form.id ,
          "name":row.name ,
          "sort":row.sort
        }
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
      handleEdit(row,index){
        this.$refs.crud.rowEdit(row,index);
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
      uploadPreview(file,column,done){
        window.open(file.url);
      },
      uploadBefore(file, done, loading,column) {
        done(file)
      },
      uploadAfter(res, done, loading,column) {
        this.form.name = res.fileName;
        done()
      }
    }
  }
</script>
