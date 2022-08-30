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
               @search-change="searchChange"
               @search-reset="searchReset"
               @selection-change="selectionChange"
               @current-change="currentChange"
               @size-change="sizeChange"
               @refresh-change="refreshChange"
               @on-load="onLoad">
      <template v-if="coursestudyhistorybtn" slot-scope="scope" slot="menu">
        <el-button v-if="coursestudyhistorybtn" type="text" @click="openStudyHistory(scope.row)" >学习详情</el-button>
        <el-button v-if="coursestudyhistorybtn" type="text" @click="openCourseOrderRecordbtn(scope.row)" >查看订单</el-button>
      </template>
    </avue-crud>
    <el-dialog title="学习详情" width="80%"  :visible.sync="coursestudyhistory" append-to-body>
      <coursestudy v-if="coursestudyhistory" :userId="userId" :courseId="courseId" ></coursestudy>
    </el-dialog>
    <el-dialog title="查看订单" width="80%"  :visible.sync="openCourseOrderRecord" append-to-body>
      <orders v-if="openCourseOrderRecord" :userId="userId" :courseId="courseId" ></orders>
    </el-dialog>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove, getCourseStudentlist} from "@/api/edu/livestudyStudentKpointhistory";
  import coursestudy from "./coursestudyhistory";
  import orders from "./orders";
  import {mapGetters} from "vuex";
  export default {
    data() {
      return {
        form: {},
        query: {courseId:this.courseId,popeType:this.popeType,courseType:this.courseType},
        loading: true,
        //课程学习记录标识
        coursestudyhistory:false,
        //是否是课程标识
        coursestudyhistorybtn:true,
        //课程订单记录的标识
        openCourseOrderRecord:false,
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        selectionList: [],
        option: {
          tip: false,
          tree: true,
          border: true,
          align: "center",
          index: false,
          addBtn:false,
          viewBtn: true,
          selection: false,
          menu:true,
          defaultExpandAll:true,
          column: [
            {
              label: "手机",
              search:true,
              prop: "mobile",
            },
            {
              label: "账号",
              search:true,
              prop: "userName",
            },
            {
              label: "昵称",
              search:true,
              prop: "showName",
            },
            {
              label: "真实姓名",
              search:true,
              prop: "realName",
            },
            {
              label: "购买时间",
              prop: "payTime",
              type: "datetime",
              format: "yyyy-MM-dd HH:mm:ss",
              valueFormat: "yyyy-MM-dd HH:mm:ss",
            },
            {
              label: "有效期",
              prop: "authTime",
              type: "datetime",
              format: "yyyy-MM-dd HH:mm:ss",
              valueFormat: "yyyy-MM-dd HH:mm:ss",
            },
            {
              label: "学习进度",
              prop: "studyLearning",
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
      courseId: Number,
      popeType: String,
      userId: Number,
      courseType:String
    },
    watch: {
      courseId: {
        deep: true,
        handler(newVal){
          this.query.courseId = newVal;
          this.onLoad(this.page);
        }
      },
      popeType: {
        deep: true,
        handler(newVal){
          this.query.popeType = newVal;
          this.onLoad(this.page);
        }
      },
      courseType: {
        deep: true,
        handler(newVal){
          this.query.courseType = newVal;
          this.onLoad(this.page);
        }
      }
    },
    methods: {
      openStudyHistory(row){
        this.coursestudyhistory = true;
        this.userId = row.userId;
        this.courseId = row.courseId;
      },openCourseOrderRecordbtn(row){
        this.openCourseOrderRecord = true;
        this.userId = row.userId;
        this.courseId = row.courseId;
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
        this.query = {courseId:this.courseId,popeType:this.popeType,courseType:this.courseType};
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
      onLoad(page, params = {courseId:this.courseId,popeType:this.popeType,courseType:this.courseType}) {
        this.loading = true;


        getCourseStudentlist(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
          this.page.total = data.total;
          this.data = data.records;
          this.loading = false;
          this.selectionClear();
          if("PACKAGE"===this.courseType){
            this.coursestudyhistorybtn = false;
          }
        });
      }
    },
    components:{
      'coursestudy':coursestudy,
      'orders':orders,
    },
  };
</script>

<style>
</style>
