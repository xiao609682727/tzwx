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
      <template slot-scope="scope" slot="menu">
        <el-button type="text" @click="openKpointStudyStudentHistory(scope.row)" >查看课程记录</el-button>
      </template>
    </avue-crud>
    <el-dialog title="课程学习详情" width="80%"  :visible.sync="coursestudyhistory" append-to-body>
      <coursestudy v-if="coursestudyhistory" :userId="userId" :courseId="courseIds" :coursePackageId="coursePackageId" ></coursestudy>
    </el-dialog>
  </basic-container>
</template>

<script>
import {getList, getDetail, add, update, remove,getpackageStudentStudyRecordlist} from "@/api/edu/packagestudyKpointhistory";
import {mapGetters} from "vuex";
import coursestudy from "./coursestudyhistory";
import  coursekpointstudentstudy from "./coursestudyKpointhistory";
export default {
  data() {
    return {
      form: {},
      query: {coursePackageId:this.coursePackageId,userId:this.userId},
      loading: true,
      kpointId:0,
      courseIds:0,
      popeType:'',
      courseType:'',
      kpointCouserId:0,
      page: {
        pageSize: 10,
        currentPage: 1,
        total: 0
      },
      coursekpointstudyStudenthistory:false,
      coursestudyhistory:false,
      selectionList: [],
      option: {
        tip: false,
        border: true,
        align: "center",
        index: false,
        addBtn:false,
        viewBtn: true,
        selection: false,
        defaultExpandAll:true,
        column: [
          {
            label: "课程名称",
            prop: "courseName",
          },
          {
            label: "课程类型",
            prop: "popeType",
            formatter: function (row,value) {
              if(value==1){
                return "录播";
              }else if(value==2){
                return "直播";
              }
            }
          },
          {
            label: "学习进度",
            prop: "studyLearning",
            formatter: function (row,value) {
              if(row.parentId!=0){
                if(value!=null &&value!=''){
                  return value;
                }else {
                  return "0%";
                }
              }
            }
          },
          {
            label: "学习时长",
            prop: "watchStingTime",
          },
          {
            label: "最后学习时间",
            prop: "updateTime",
            formatter: function (row,value) {
              if(value!=null &&value!=''){
                return value;
              }else {
                return "暂未学习"
              }
            }
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
        addBtn: this.vaildData(this.permission.coursestudyhistory_add, false),
        viewBtn: this.vaildData(this.permission.coursestudyhistory_view, false),
        delBtn: this.vaildData(this.permission.coursestudyhistory_delete, false),
        editBtn: this.vaildData(this.permission.coursestudyhistory_edit, false)
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
    coursePackageId: Number,
    userId: Number
  },
  watch: {
    coursePackageId: {
      deep: true,
      handler(newVal){
        this.query.coursePackageId = newVal;
        this.onLoad(this.page);
      }
    },userId: {
      deep: true,
      handler(newVal){
        this.query.userId = newVal;
        this.onLoad(this.page);
      }
    }
  },
  methods: {
    openKpointStudyStudentHistory(row){
      this.courseIds=row.courseId;
      this.coursestudyhistory=true;
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
        });
      }
      done();
    },
    searchReset() {
      this.query = {coursePackageId:this.coursePackageId};
      this.query = {userId:this.userId};
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
    onLoad(page, params = {coursePackageId:this.coursePackageId,userId:this.userId,}) {
      this.loading = true;
      getpackageStudentStudyRecordlist(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
        const data = res.data.data;
        this.data = data.records;
        this.loading = false;
        this.selectionClear();
      });
    }
  },
  components:{
    'coursekpointstudentstudy':coursekpointstudentstudy,
    'coursestudy':coursestudy
  }
};
</script>

<style>
</style>
