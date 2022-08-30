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
          <el-button type="text" v-if="scope.row.parentId!='0'" @click="openKpointStudyStudentHistory(scope.row)" >查看章节详情</el-button>
      </template>
      <template slot-scope="scope"
                slot="studyPope">
        <el-button type="text" v-if="scope.row.studyPope!=-1" @click="openKpointStudyStudentHistory(scope.row)" >{{scope.row.studyPope}}人</el-button>
      </template>
      <template slot-scope="scope"
                slot="studyPopeShould">
        <el-button type="text" v-if="scope.row.studyPopeShould!=-1" @click="openstudyPopeShould(scope.row)" >{{scope.row.studyPopeShould}}人</el-button>
      </template>
      <template slot-scope="scope"
                slot="studyPopeComplete">
        <el-button type="text" v-if="scope.row.studyPopeComplete!=-1" @click="openstudyPopeComplete(scope.row)" >{{scope.row.studyPopeComplete}}人</el-button>
      </template>
      <template slot-scope="scope"
                slot="studyPopeNo">
        <el-button type="text" v-if="scope.row.studyPopeNo!=-1" @click="openstudyPopeNo(scope.row)" >{{scope.row.studyPopeNo}}人</el-button>
      </template>
    </avue-crud>
    <el-dialog title="查看章节全部学员详情" width="80%"  :visible.sync="coursekpointstudyStudenthistory" append-to-body>
      <coursekpointstudentstudy v-if="coursekpointstudyStudenthistory" :kpointId="kpointId" :courseId="courseIds" :popeType="popeType" :courseType="courseTypec" :kpointCourseId="kpointCourseId"></coursekpointstudentstudy>
    </el-dialog>
    <el-dialog title="已学习学员" width="80%"  :visible.sync="openstudyPopeShouldhistory" append-to-body>
      <coursekpointstudentstudy v-if="openstudyPopeShouldhistory" :kpointId="kpointId" :courseId="courseIds" :popeType="popeType" :courseType="courseTypec" :kpointCourseId="kpointCourseId"></coursekpointstudentstudy>
    </el-dialog>
    <el-dialog title="学习完成学员" width="80%"  :visible.sync="openstudyPopeCompletehistory" append-to-body>
      <coursekpointstudentstudy v-if="openstudyPopeCompletehistory" :kpointId="kpointId" :courseId="courseIds" :popeType="popeType" :courseType="courseTypec" :kpointCourseId="kpointCourseId"></coursekpointstudentstudy>
    </el-dialog>
    <el-dialog title="未学习学员" width="80%"  :visible.sync="openstudyPopeNohistory" append-to-body>
      <coursekpointstudentstudy v-if="openstudyPopeNohistory" :kpointId="kpointId" :courseId="courseIds" :popeType="popeType" :courseType="courseTypec" :kpointCourseId="kpointCourseId"></coursekpointstudentstudy>
    </el-dialog>
    <el-dialog title="查看章节全部学员详情" width="80%"  :visible.sync="LivekpointstudyStudenthistory" append-to-body>
      <Livekpointstudentstudy v-if="LivekpointstudyStudenthistory" :kpointId="kpointId" :courseId="courseIds" :popeType="popeType" :courseType="courseTypec" :kpointCourseId="kpointCourseId"></Livekpointstudentstudy>
    </el-dialog>
    <el-dialog title="已学习学员" width="80%"  :visible.sync="LivestudyPopeShouldhistory" append-to-body>
      <Livekpointstudentstudy v-if="LivestudyPopeShouldhistory" :kpointId="kpointId" :courseId="courseIds" :popeType="popeType" :courseType="courseTypec" :kpointCourseId="kpointCourseId"></Livekpointstudentstudy>
    </el-dialog>
    <el-dialog title="学习完成学员" width="80%"  :visible.sync="LivestudyPopeCompletehistory" append-to-body>
      <Livekpointstudentstudy v-if="LivestudyPopeCompletehistory" :kpointId="kpointId" :courseId="courseIds" :popeType="popeType" :courseType="courseTypec" :kpointCourseId="kpointCourseId"></Livekpointstudentstudy>
    </el-dialog>
    <el-dialog title="未学习学员" width="80%"  :visible.sync="LivestudyPopeNohistory" append-to-body>
      <Livekpointstudentstudy v-if="LivestudyPopeNohistory" :kpointId="kpointId" :courseId="courseIds" :popeType="popeType" :courseType="courseTypec" :kpointCourseId="kpointCourseId"></Livekpointstudentstudy>
    </el-dialog>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove} from "@/api/edu/coursestudyKpointhistory";
  import {mapGetters} from "vuex";
  import  coursekpointstudentstudy from "./coursestudyStudentKpointhistory";
  import  Livekpointstudentstudy from "./livestudyStudentKpointhistory";
  export default {
    data() {
      return {
        form: {},
        query: {courseId:this.courseId,courseType:this.courseType,kpointCourseId:this.kpointCourseId},
        loading: true,
        kpointId:0,
        courseIds:0,
        courseTypec:'',
        popeType:'',
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        coursekpointstudyStudenthistory:false,
        openstudyPopeShouldhistory:false,
        openstudyPopeCompletehistory:false,
        openstudyPopeNohistory:false,
        LivekpointstudyStudenthistory:false,
        LivestudyPopeShouldhistory:false,
        LivestudyPopeCompletehistory:false,
        LivestudyPopeNohistory:false,
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
              label: "章节名称",
              prop: "kpointName",
            },
            {
              label: "播放总次数",
              prop: "playercount",
              formatter: function (row,value) {
                if(value!=-1){
                  return value+"次";
                }else {
                  return "";
                }
              }
            },
            {
              label: "应学习时长",
              prop: "videoTime",
              formatter: function (row,value) {
                if(row.parentId!=0){
                  if (value != 0 && value != '') {
                    let videoTimeminType1 = /^([0-9][0-9][0-9]):([0-5][0-9])$/;
                    let videoTimeminType2 = /^([0-9][0-9]):([0-5][0-9])$/;
                    let valueType = 0;
                    if (value.length == 6) {
                      let videoTimeT = videoTimeminType1.test(value);
                      if (!videoTimeT) {
                        valueType = 1;
                      }
                    }
                    else if (value.length == 5) {
                      let videoTimeT = videoTimeminType2.test(value);
                      if (!videoTimeT) {
                        valueType = 1;
                      }
                    }
                    else {
                      valueType = 1;
                    }
                    if (valueType == 1) {
                      let timeStr = null;
                      let minute;
                      let second = 0;
                      if (parseInt(value) <= 0) {
                        timeStr = "00:00";
                      } else {
                        minute = parseInt(value) / 60;
                        second = parseInt(value) % 60;
                        if (second < 10) {
                          second = "0" + second;
                        }
                        if (parseInt(minute) < 10) {
                          timeStr = "0" + parseInt(minute) + ":" + second;
                        } else {
                          timeStr = parseInt(minute) + ":" + second;
                        }
                      }
                      return timeStr;
                    } else {
                      return value;
                    }
                  } else {
                    return "";
                  }
                }

              }
            },
            {
              label: "应学习人数",
              prop: "studyPope",
              slot:true,
            },
            {
              label: "未学习人数",
              prop: "studyPopeNo",
              slot:true,
            },
            {
              label: "已学习人数",
              prop: "studyPopeShould",
              slot:true,
            },
            {
              label: "学习完成人数",
              prop: "studyPopeComplete",
              append:"人",
              slot:true,
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
      courseType:Number,
      kpointCourseId:Number
    },
    watch: {
      courseId: {
        deep: true,
        handler(newVal){
          this.query.courseId = newVal;
          this.onLoad(this.page);
        }
      },
      courseType:{
        deep: true,
        handler(newVal){
          this.query.courseType = newVal;
          this.onLoad(this.page);
        }
      },
      kpointCourseId:{
        deep: true,
        handler(newVal){
          this.query.kpointCourseId = newVal;
          this.onLoad(this.page);
        }
      }
    },
    methods: {
      /*全部学员*/
      openKpointStudyStudentHistory(row){
        this.kpointId=row.id;
        this.courseIds=row.courseId;
        this.popeType="ALL";
        this.courseTypec=this.courseType;
        if(row.popeType==1){
          this.coursekpointstudyStudenthistory=true;
        }else if(row.popeType==2){
          this.LivekpointstudyStudenthistory=true;
        }
      },
      /*已学习*/
      openstudyPopeShould (row){
        this.kpointId=row.id;
        this.courseIds=row.courseId;
        this.courseTypec=this.courseType;
        this.popeType="SHOULD";
        if(row.popeType==1){
          this.openstudyPopeShouldhistory=true;
        }else if(row.popeType==2){
          this.LivestudyPopeShouldhistory=true;
        }
      },
      /*完成学员*/
      openstudyPopeComplete(row){
        this.kpointId=row.id;
        this.courseIds=row.courseId;
        this.courseTypec=this.courseType;
        this.popeType="COMPLETE";
        if(row.popeType==1){
          this.openstudyPopeCompletehistory=true;
        }else if(row.popeType==2){
          this.LivestudyPopeCompletehistory=true;
        }
      },
      /*未学习*/
      openstudyPopeNo(row){
        this.kpointId=row.id;
        this.courseIds=row.courseId;
        this.courseTypec=this.courseType;
        this.popeType="NO";
        if(row.popeType==1){
          this.openstudyPopeNohistory=true;
        }else if(row.popeType==2){
          this.LivestudyPopeNohistory=true;
        }
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
        this.query = {courseId:this.courseId,courseType:this.courseType,kpointCourseId:this.kpointCourseId};
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
      onLoad(page, params = {courseId:this.courseId,courseType:this.courseType,kpointCourseId:this.kpointCourseId}) {
        this.loading = true;
        getList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
          /*this.page.total = data.total;*/
          this.data = data;
          this.loading = false;
          this.selectionClear();
        });
      }
    },
    components:{
      'coursekpointstudentstudy':coursekpointstudentstudy,
      'Livekpointstudentstudy':Livekpointstudentstudy
    }
  };
</script>

<style>
</style>
