<template>
  <basic-container>
    <avue-crud :search.sync="search" :option="option"
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
                   v-if="permission.course_delete"
                   @click="handleDelete">删 除
        </el-button>
        <el-button type="success"
                   size="small"
                   plain
                   icon="el-icon-upload2"
                   @click="handleImport">批量赠送
        </el-button>
      </template>
      <template slot-scope="scope" slot="menu">
        <el-button type="text" icon="el-icon-edit" size="small" @click="handleEdit(scope.row,scope.index)">编辑</el-button>
        <el-button type="text" icon="el-icon-delete" size="small" @click="handleDel(scope.row)">删除</el-button>
        <el-button type="text" icon="el-icon-notebook-2" size="small" @click="openStudent(scope.row)">赠送面授</el-button>
        <el-button type="text" icon="el-icon-notebook-2" size="small" @click="stduyUsersimb(scope.row)">报名记录</el-button>
      </template>
      <template slot-scope="scope" slot="losetypeForm">
        <el-select v-model="scope.row.losetype" @change="change" placeholder="请选择" >
          <el-option
            v-for="item in losetypeList"
            :key="item.dictKey"
            :label="item.dictValue"
            :value="item.dictKey">
          </el-option>
        </el-select>
      </template>
      <template slot-scope="scope" slot="teacherIdForm">
        <el-tag v-if="tags.length == 1"
                closable @close="handleClose(tag)"
                v-for="tag in tags"
                :key="tag"
                :disable-transitions="false"
        >{{tag.name}}</el-tag>
        <el-button
          type="primary"
          icon="el-icon-circle-plus-outline"
          size="small"
          v-if="tags.length == 0"
          @click.stop="handleShowTeacher(scope.row)"
        >选择讲师
        </el-button>
      </template>
      <template slot-scope="scope" slot="subjectIdForm">
        <el-cascader
          placeholder="专业分类"
          :options="subjectTree"
          v-model="form.subjectId"
          :props="{'label':'title','emitPath':false}"
          filterable></el-cascader>
      </template>
      <template slot-scope="scope" slot="faceTeachingSubjectForm">
        <el-cascader
          placeholder="上课地点"
          :options="faceTeachingsubjectTree"
          v-model="form.faceTeachingSubject"
          :props="{'label':'title','emitPath':false}"
          filterable></el-cascader>
      </template>
      <template slot-scope="scope" slot="subjectIdSearch">
        <el-cascader
          placeholder="专业分类"
          :options="subjectTree"
          v-model="search.subjectId"
          :props="{'label':'title','emitPath':false}"
          filterable clearable></el-cascader>
      </template>
      <template slot-scope="scope" slot="faceTeachingSubjectSearch">
        <el-cascader
          placeholder="上课地点"
          :options="faceTeachingsubjectTree"
          v-model="search.faceTeachingSubject"
          :props="{'label':'title','emitPath':false}"
          filterable clearable></el-cascader>
      </template>
    </avue-crud>
    <el-dialog title="赠送面授" width="80%"  :visible.sync="dialogStudentVisible">
      <student  @datasubmit="dataSubmit" v-if="dialogStudentVisible"></student>
    </el-dialog>
    <el-dialog title="讲师列表" width="80%" :visible.sync="dialogTeacherVisible" append-to-body>
      <teacher v-if="dialogTeacherVisible" @datasubmit="teacherdataSubmit"></teacher>
    </el-dialog>
    <el-dialog title="报名记录" width="80%"  :visible.sync="dialogStudyUser" append-to-body>
      <courseStudyStudenthistory v-if="dialogStudyUser" :popeType="popeType" :courseId="courseId" :courseType="courseType" ></courseStudyStudenthistory>
    </el-dialog>
    <el-dialog title="批量赠送面授导入"
               append-to-body
               :visible.sync="excelBox"
               width="555px">
      <avue-form :option="excelOption" v-model="excelForm" :upload-after="uploadAfter">
        <template slot="excelTemplate">
          <el-button type="primary" @click="handleTemplate()">
            点击下载<i class="el-icon-download el-icon--right"></i>
          </el-button>
        </template>
      </avue-form>
      <el-row v-if="errorMsg != ''" style="top:-45px;left:28px">
        <div>错误信息：</div>
        <el-col :span="2" ></el-col>
        <el-col :span="20" style="color: #f56c6c">
          <div style="margin:15px 0 0 -35px;padding-left: 19px;overflow: auto; height: 150px;width: 420px;border-radius:5px;border: #d9d9d9 1px dashed" v-html="errorMsg"></div>
        </el-col>
      </el-row>
    </el-dialog>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove, getEndTimeType,give,getTeacherDetail,linClasssubjectTree,subjectTree,selectCourseKpoint} from "@/api/edu/course";

  import {mapGetters} from "vuex";
  import student from "@/views/edu/student";
  import teacher from "@/views/edu/teacher";
  import courseStudyStudenthistory from "@/views/edu/lineCourseBao";
  export default {
    data() {
      let _this = this;
      let validateEndTime = (rule, value, callback)=>{
        let nowDateFmt = new Date().Format("yyyy-MM-dd HH:mm:ss");
        if (value === '') {
          callback(new Error('请输入结束时间'));
        } else if (value <= nowDateFmt) {
          callback(new Error('输入的结束时间要大于现在时间!'));
        } else if (value <= _this.form.beginTime) {
          callback(new Error('输入的结束时间要大于开始时间!'));
        }else {
          callback();
        }
      };
      Date.prototype.Format = function (fmt) {
        let o = {
          "M+": this.getMonth() + 1, //月份
          "d+": this.getDate(), //日
          "H+": this.getHours(), //小时
          "m+": this.getMinutes(), //分
          "s+": this.getSeconds(), //秒
          "q+": Math.floor((this.getMonth() + 3) / 3), //季度
          "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (let k in o)
          if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
      }
      return {
        errorMsg:"",
        excelBox: false,
        search:{},
        subjectTree:[],
        faceTeachingsubjectTree:[],
        dialogCommentVisible:false,
        dialogTeacherVisible:false,
        dialogStudyHistory:false,
        dialogStudyUser:false,
        tags:[],
        losetypeList:[],
        courseId:"",
        popeType:"ALL",
        courseType:"",
        kpointCouserId:0,
        dialogStudentVisible:false,
        dialogKpointVisible: false,
        form: {},
        query: {sellType:"4"},
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
          delBtn: false,
          editBtn: false,
          selection: true,
          labelWidth: 150,
          dialogClickModal: false,
          column: [
            {
              label: "面授id",
              prop: "id",
              addDisplay: false,
              search :true,
              editDisplay: false
            },
            {
              label: "面授名称",
              prop: "courseName",
              search :true,
              span: 24,
              rules: [{
                required: true,
                message: "请输入面授名称",
                trigger: "blur"
              }]
            },
            {
              label: "专业分类",
              prop: "subjectId",
              type: "cascader",
              searchLabelWidth:110,
              span: 24,
              search :true,
              formslot:true,
              searchslot:true,
              dicUrl: "/api/edu/subject/tree",
              props: {
                label: "title"
              },
              tip:"面授分类只可以选择二级分类",
              placeholder:"请输入要选择的专业，支持搜索",
              rules: [{
                required: true,
                message: "请输入面授专业分类",
                trigger: "blur"
              }]
            },
            {
              label: "上课地点",
              prop: "faceTeachingSubject",
              type: "cascader",
              searchLabelWidth:110,
              span: 24,
              search :true,
              formslot:true,
              searchslot:true,
              dicUrl: "/api/edu/subject/lineClasstree",
              props: {
                label: "title"
              },
              placeholder:"请输入要选择的上课地址，支持搜索",
              rules: [{
                required: true,
                message: "请输入面授专业分类",
                trigger: "blur"
              }]
            },
            {
              label: "上课地址",
              prop: "faceTeachingAddress",
              hide:true,
              span: 24,
              rules: [{
                required: true,
                message: "请输入上课地址",
                trigger: "blur"
              }]
            },
            {
              label: "原价",
              prop: "sourcePrice",
              type: "number",
              tip: "只显示价格",
              precision: 2,
              rules: [{
                required: true,
                message: "请输入面授原价格",
                trigger: "blur"
              }]
            },
            {
              label: "售价",
              prop: "currentPrice",
              type: "number",
              precision: 2,
              tip: "（实际支付价格）设置为0则可免费观看",
              rules: [{
                required: true,
                message: "请输入面授销售价格",
                trigger: "blur"
              }]
            },
            {
              label: "面授简介",
              prop: "title",
              type:"textarea",
              span: 24,
              hide:true,
              rules: [{
                required: true,
                message: "请输入面授简介",
                trigger: "blur"
              }]
            },
            {
              label: "面授详情",
              prop: "context",
              type:"ueditor",
              component: "avueUeditor",
              options:{
                //普通图片上传
                action: "/api/common/upload?path=course",
                props: {
                  res: "data",
                  url:'url'
                },
              },
              span: 24,
              hide:true,
            },
            {
              label: "图片路径",
              prop: "logo",
              hide:true,
              type:"upload",
              dataType: 'string',
              span: 24,
              action: '/api/common/upload/size?path=course&width=570&height=312',
              propsHttp: {
                res: 'data'
              },
              tip: "建议图片像素：570*312",
              listType: 'picture-img',
              rules: [{
                required: true,
                message: "请输入图片路径",
                trigger: "blur"
              }]
            },
            {
              label: "销售数量",
              addDisplay: false,
              editDisplay: false,
              prop: "pageBuycount",
              type: "number",
            },
            {
              label: "假学习人数",
              prop: "bogusBuycount",
              hide:true,
              precision: 0,
              span:8,
              type: "number",
            },
            {
              label: "评论数",
              prop: "commentNum",
              hide: true,
              addDisplay: false,
              editDisplay: false,
              type: "number",
            },
            {
              label: "浏览数量",
              prop: "pageViewcount",
              addDisplay: false,
              editDisplay: false,
              type: "number",
            },
            {
              label: "假浏览量",
              prop: "bogusViewcount",
              hide:true,
              precision: 0,
              span:8,
              type: "number",
            },

            {
              label: "联系电话",
              prop: "faceTeachingMobile",
              hide:true,
              precision: 0,
              span:8,
              rules: [{
                required: true,
                message: "请输入联系电话",
                trigger: "blur"
              }]
            },
            {
              label: "有效期类型",
              prop: "losetype",
              formslot: true,
              dataType: "string",
              hide: true,
              addDisplay: false,
              editDisplay: false,
              value: "0",
              row: true,
              dicUrl: "/api/crazy-system/dict/dictionary?code=end_time_type",
              props: {
                label: "dictValue",
                value: "dictKey"
              },
            },
            {
              label: "开始时间",
              prop: "faceTeachingTime",
              hide: true,
              type: "datetime",
              format: "yyyy-MM-dd HH:mm:ss",
              valueFormat: "yyyy-MM-dd HH:mm:ss",
              rules: [{
                required: true,
                message: "请输入开始时间",
                trigger: "blur"
              }]
            },
            {
              label: "结束时间",
              prop: "endTime",
              hide: true,
              addDisplay: false,
              editDisplay: false,
              type: "datetime",
              format: "yyyy-MM-dd HH:mm:ss",
              valueFormat: "yyyy-MM-dd HH:mm:ss",
              rules: [{
                required: true,
                validator: validateEndTime,
                trigger: "blur"
              }]
            },
            {
              label: "排序",
              prop: "sort",
              type: "number",
              span: 10,
              value: "1",
              hide:true,
              rules: [{
                required: true,
                message: "请输入排序",
                trigger: "blur"
              }]
            },
            {
              label: "面授状态",
              prop: "isAvaliable",
              type: "radio",
              search :true,
              span: 10,
              dataType: "number",
              value: 1,
              row: true,
              dicUrl: "/api/crazy-system/dict/dictionary?code=course_status",
              props: {
                label: "dictValue",
                value: "dictKey"
              },
            },
            {
              label: "讲师",
              prop: "teacherId",
              hide: true,
              span: 24,
              formslot: true,
              rules: [{
                required: true,
                message: "请选择讲师",
                trigger: "blur"
              }]
            },
            {
              label: "面授安排",
              prop: "faceTeachingContext",
              type:"ueditor",
              component: "avueUeditor",
              options:{
                //普通图片上传
                action: "/api/common/upload?path=course",
                props: {
                  res: "data",
                  url:'url'
                },
              },
              span: 24,
              hide:true,
            },
            {
              label: "课程类型",
              prop: "sellType",
              type: "radio",
              row: true,
              hide: true,
              addDisplay: false,
              editDisplay: false,
              dataType: "string",
              value: "1",
              dicUrl: "/api/crazy-system/dict/dictionary?code=course_type",
              props: {
                label: "dictValue",
                value: "dictKey"
              },
              rules: [{
                required: true,
                message: "请输入面授类型",
                trigger: "blur"
              }]
            },
            {
              label: "创建时间",
              prop: "createTime",
              addDisplay:false,
              editDisplay: false,
              hide: true,
              rules: [{
                required: true,
                message: "请输入添加时间",
                trigger: "blur"
              }]
            },
          ]
        },
        data: [],
        excelForm: {},
        excelOption: {
          submitBtn: false,
          emptyBtn: false,
          column: [
            {
              label: '模板上传',
              prop: 'excelFile',
              type: 'upload',
              drag: true,
              loadText: '模板上传中，请稍等',
              span: 24,
              showFileList:false,
              propsHttp: {
                res: 'data'
              },
              tip: '请上传 .xls,.xlsx 标准格式文件',
              action: "/api/edu/course/import-course-user"
            },
            {
              label: '模板下载',
              prop: 'excelTemplate',
              formslot: true,
              span: 24,
            }
          ]
        }
      };
    },
    computed: {
      ...mapGetters(["permission"]),
      permissionList() {
        return {
          addBtn: this.vaildData(this.permission.course_add, false),
          viewBtn: this.vaildData(this.permission.course_view, false),
          delBtn: this.vaildData(this.permission.course_delete, false),
          editBtn: this.vaildData(this.permission.course_edit, false)
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
      handleClose(tag){
        this.tags.splice(0,1)
        this.form.teacherId = null
      },
      handleShowTeacher(row){
        this.dialogTeacherVisible = true;
      },
      teacherdataSubmit(selection){
        this.form.teacherId=selection.id;
        this.tags[0]= {"name":selection.name}
        this.dialogTeacherVisible = false;
      },
      dataSubmit(selection){
        let userIds = "";
        selection.forEach(ele => {
          userIds+=ele.id+",";
        })
        userIds = userIds.substring(0,userIds.length - 1);
        give(userIds,this.courseId).then(() => {
          this.dialogStudentVisible = false;
          this.$message({
            type: "success",
            message: "操作成功!"
          });
        }, error => {
          console.log(error);
        });
      },
      handleEdit(row,index){
        this.$refs.crud.option.column.filter(item => {
          if (item.prop === "loseTime") {
            if(row.losetype == "0"){
              item.editDisplay = false;
              item.addDisplay = false;
            }
            if(row.losetype == "1"){
              item.editDisplay = true;
              item.addDisplay = true;
            }
          }
          if (item.prop === "endTime") {
            if(row.losetype == "0"){
              item.editDisplay = true;
              item.addDisplay = true;
            }
            if(row.losetype == "1"){
              item.editDisplay = false;
              item.addDisplay = false;
            }
          }
        });
        this.$refs.crud.rowEdit(row,index);
      },
      handleDel(row){
        this.$refs.crud.rowDel(row);
      },
      change(value){
        this.$refs.crud.option.column.filter(item => {
          if (item.prop === "loseTime") {
              item.editDisplay = false;
              item.addDisplay = false;
          }
          if (item.prop === "endTime") {
              item.editDisplay = true;
              item.addDisplay = true;
          }
        });
      },
      openStudent(row){
        this.courseId = row.id ;
        this.dialogStudentVisible = true;
      },
      openComment(row){
        this.courseId = row.id;
        this.dialogCommentVisible = true;
      },
      stduyhistorysimb(row){
        this.courseId = row.id;
        this.courseType="COURSE";
        this.kpointCouserId=0;
        this.dialogStudyHistory = true;
      },
      stduyUsersimb(row){
        this.courseId = row.id;
        this.courseType="LINECLASS";
        this.kpointCouserId=0;
        this.dialogStudyUser = true;
      },
      openCourseKpoint(row){
        this.courseId = row.id;
        this.dialogKpointVisible = true;
      },
      rowSave(row, loading, done) {
        debugger
        row.sellType = "4";
        let losetypepanduan=/^[1-9]\d*$/;
        let losetype=row.losetype;
        //面授有效期为按天数，天数不能是小数和0
        if(losetype == 1){
          let videoTimeT=losetypepanduan.test(row.loseTime);
          if(!videoTimeT){
            this.$message({
              type: "warning",
              message: "按天数不可以输入小数和0"
            });
            done();
            return;
          }
        }else{//如果有效期为时间，时间不能小于当前时间
          let startTime= new Date();
          let endTime=new Date(Date.parse(row.endTime));
          if(startTime>endTime){
            this.$message({
              type: "warning",
              message: "面授有效结束时间应该大于现在的时间"
            });
            done();
            return
          }
        }
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
        row.sellType = "4";
        let losetypepanduan=/^[1-9]\d*$/;
        let losetype=row.losetype;
        //面授有效期为按天数，天数不能是小数和0
        if(losetype == 1){
          let videoTimeT=losetypepanduan.test(row.loseTime);
          if(!videoTimeT){
            this.$message({
              type: "warning",
              message: "按天数不可以输入小数和0"
            });
            done();
            return;
          }
        }else{//如果有效期为时间，时间不能小于当前时间
          let startTime= new Date();
          let endTime=new Date(Date.parse(row.endTime));
          if(startTime>endTime){
            this.$message({
              type: "warning",
              message: "面授有效结束时间应该大于现在的时间"
            });
            done();
            return
          }
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
        console.log(this.form)
        if (["add"].includes(type)) {
          for(let key in this.form){
            this.form[key]=undefined;
          }
          this.change(0);
          this.tags= []
        }
        if (["edit", "view"].includes(type)) {
          getDetail(this.form.id).then(res => {

            this.form = res.data.data;
          });
        }
        let that = this
        if(this.form.teacherId){
          getTeacherDetail(this.form.teacherId).then(res => {
            this.tags= []
            that.tags[0]= {"name":res.data.data.name}
            console.log(that.tags)
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
      handleTemplate() {
        window.open(`static/template/批量赠送课程模板.xlsx`);
        // window.open(`/api/user/student/export-template?crazy-auth=${getToken()}`);
      },
      handleImport() {
        this.excelBox = true;
      },
      uploadAfter(res, done, loading, column) {
        if(Object.prototype.toString.call(res)=="[object Error]"){
          this.errorMsg = res.message
          done();
          return;
        }
        done();
        this.errorMsg = ""
        this.excelBox = false;
        this.refreshChange();
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
      'student':student,
      'teacher':teacher,
      'courseStudyStudenthistory':courseStudyStudenthistory,
    },
    created:function(){
      getEndTimeType().then(res => {
        this.losetypeList = res.data.data;
      });
      subjectTree().then(res =>{
        this.subjectTree = res.data.data;
      })
      linClasssubjectTree().then(res =>{
        this.faceTeachingsubjectTree = res.data.data;
      })
    },
  };
</script>

<style>
</style>
