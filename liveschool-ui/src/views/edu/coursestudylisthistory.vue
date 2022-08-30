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
          <template slot-scope="scope" slot="menu">
            <el-button type="text" @click="openStudyHistory(scope.row)" >学习详情</el-button>
          </template>
    </avue-crud>
    <el-dialog title="课程学习详情" width="80%"  :visible.sync="coursestudyhistory" append-to-body>
      <coursestudy v-if="coursestudyhistory" :userId="userId" :courseId="courseId" ></coursestudy>
    </el-dialog>
    <el-dialog title="学习详情" width="80%"  :visible.sync="coursePackageStudyhistory" append-to-body>
      <coursePackageStudylisthistory v-if="coursePackageStudyhistory" :userId="userId" :coursePackageId="courseId" ></coursePackageStudylisthistory>
    </el-dialog>
  </basic-container>
</template>

<script>
  import {getList, getDetail } from "@/api/edu/coursestudylisthistory";
  import {mapGetters} from "vuex";
  import coursestudy from "./coursestudyhistory";
  import coursePackageStudylisthistory from "./coursePackageStudylisthistory";
  export default {
    data() {
      return {
        form: {},
        query: {userId:this.userId},
        loading: true,
        coursestudyhistory:false,
        coursePackageStudyhistory:false,
        courseId:"",
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
          addBtn:false,
          viewBtn: true,
          selection: false  ,
          menu:true,
          column: [
            {
              label: "课程id",
              prop: "courseId",
              hide:true,
              rules: [{
                required: true,
                message: "请输入课程id",
                trigger: "blur"
              }]
            },
            {
              label: "课程名称",
              prop: "courseName",
              search:true,
              rules: [{
                required: true,
                message: "请输入课程名称",
                trigger: "blur"
              }]
            },
            {
              label: "课程类型",
              prop: "sellType",
              type: "select",
              search:true,
              dicData:[
                {
                  label:'点播',
                  value:"1"
                },{
                  label:'直播',
                  value:"2"
                },{
                  label:'班级',
                  value:"3"
                }
              ],
              formatter: function (row,value) {
                value = "点播";
                if(row.sellType==1){
                  value = "点播";
                }
                if(row.sellType==2){
                  value = "直播";
                }
                if(row.sellType==3){
                  value = "班级";
                }
                return value;

              }
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
      userId: Number
    },
    watch: {
      userId: {
        deep: true,
        handler(newVal){
          this.query.userId = newVal;
          this.onLoad(this.page);
        }
      }
    },
    methods: {
      openStudyHistory(row){
        if(row.sellType==3){
          this.coursePackageStudyhistory = true;
        }else{
          this.coursestudyhistory = true;
        }
        this.userId = row.userId;
        this.courseId = row.courseId;
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
      onLoad(page, params = {userId:this.userId}) {
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
      'coursestudy':coursestudy,
      'coursePackageStudylisthistory':coursePackageStudylisthistory,
    },
  };
</script>

<style>
</style>
