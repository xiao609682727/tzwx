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
        <el-button type="primary"
                   size="small"
                   icon="el-icon-plus"
                   @click="handleAddCourse">新增
        </el-button>
      </template>
    </avue-crud>
    <el-dialog title="班级课程添加" width="80%"  :visible.sync="dialogCourseVisible" @close="dialogCourse" append-to-body>
      <addcourse @adddata="adddata" v-if="addcourseFlag" ></addcourse>
    </el-dialog>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove, addList} from "@/api/edu/coursepackage";
  import {mapGetters} from "vuex";
  import addcourse from "./addcourse";

  export default {
    data() {
      return {
        addcourseFlag:false,
        dialogCourseVisible:false,
        form: {},
        query: {parentCourseId:this.courseId},
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
          addBtn: false,
          selection: false,
          editBtnText: "排序",
          column: [

            {
              label: "课程名称",
              prop: "courseName",
              addDisplay:false,
              editDisplay:false,
            },
            {
              label: "课程类型",
              prop: "sellType",
              row: true,
              addDisplay:false,
              editDisplay:false,
              dataType: "string",
              dicUrl: "/api/crazy-system/dict/dictionary?code=course_type",
              props: {
                label: "dictValue",
                value: "dictKey"
              },
            },
            {
              label: "销售价格（元）",
              prop: "currentPrice",
              type: "number",
              addDisplay:false,
              editDisplay:false,
              precision: 2,
              formatter: function (row,value,label,column) {
                return Number(value).toFixed(2) +"";
              }
            },
            {
              label: "排序",
              prop: "sort",
              type: "number",
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
          addBtn: this.vaildData(true, false),
          viewBtn: this.vaildData(true, false),
          delBtn: this.vaildData(true, false),
          editBtn: this.vaildData(true, false)
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
    props:{
      courseId: Number
    },
    watch: {

      courseId: {
        deep: true,
        handler(newVal){
          this.query.courseId = newVal;
          this.onLoad(this.page);
        }
      }
    },
    methods: {
      dialogCourse(){
        this.addcourseFlag = false;
      },
      adddata(selectionList){
        this.dialogCourseVisible = false;
        let list = [];
        selectionList.forEach(ele => {
          list.push({
            "parentCourseId":this.courseId,
            "courseId": ele.id
          });
        });
        addList(list,this.courseId).then(() => {
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
        }, error => {
          console.log(error);
        });
      },
      rowSave(row, loading, done) {
        row.parentCourseId = this.courseId;
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
      handleAddCourse() {
        this.dialogCourseVisible = true;
        this.addcourseFlag = true;
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
      }
    },
    components:{
      'addcourse':addcourse,
    },
  };
</script>

<style>
</style>
