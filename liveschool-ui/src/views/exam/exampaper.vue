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
               @sort-change="sortChange"
               @on-load="onLoad">
      <template slot="menuLeft">
        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   plain
                   v-if="permission.exampaper_delete"
                   @click="handleDelete">删 除
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
      <template slot-scope="{type,size,row}" slot="menu">
        <el-button icon="el-icon-reading" :size="size" :type="type" @click="openExam(row)">组卷</el-button>
        <el-button icon="iconfont iconicon_doc" :size="size" :type="type" @click="openExamRecord(row)">考试记录</el-button>
        <el-button icon="el-icon-s-data" :size="size" :type="type" @click="openExamAnalysis(row)">试卷分析</el-button>
      </template>
    </avue-crud>
    <el-dialog class="el-scrollbar__wrap" title="组卷管理" width="80%" :visible.sync="dialogComposeVisible" append-to-body>
      <composeExampaper v-if="dialogComposeVisible" :exampaperArr="exampaperArr" :exam="exam" @saveExam="saveExamQuestion"></composeExampaper>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogComposeVisible = false">取 消</el-button>
<!--        <el-button type="primary" @click="saveExamQuestion">保 存</el-button>-->
      </span>
    </el-dialog>
    <el-dialog class="el-scrollbar__wrap" title="试卷分析" :fullscreen="true" :visible.sync="dialogAnalysisVisible" style="padding-top: 0px" append-to-body>
      <exampaperAnalysis v-if="dialogAnalysisVisible"  :exam="exam"></exampaperAnalysis>
    </el-dialog>
    <el-dialog class="el-scrollbar__wrap" title="考试记录" :fullscreen="true" :visible.sync="dialogRecordVisible" style="padding-top: 0px" append-to-body>
      <exampaperrecordById v-if="dialogRecordVisible"  :exam="exam"></exampaperrecordById>
    </el-dialog>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove,subjectTree,saveQuestion,queryQuestion} from "@/api/exam/exampaper";
  import {mapGetters} from "vuex";
  import composeExampaper from "./composeExampaper";
  import exampaperAnalysis from "./exampaperAnalysis";
  import exampaperrecordById from "./exampaperrecordById";

  export default {
    components:{
      'composeExampaper':composeExampaper,
      'exampaperAnalysis':exampaperAnalysis,
      'exampaperrecordById':exampaperrecordById,
    },
    data() {
      return {
        exampaperArr:[],
        exam:{},
        dialogRecordVisible:false,
        dialogAnalysisVisible:false,
        dialogComposeVisible:false,
        subjectTree:[],
        form: {},
        query: {descs:"addTime"},
        loading: true,
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        selectionList: [],
        option: {
          height: 'auto',
          calcHeight: 80,
          align: "center",
          searchShow: true,
          searchMenuSpan: 6,
          tip: false,
          refreshBtn: false,
          border: true,
          index: false,
          viewBtn: false,
          selection: true,
          labelWidth:150,
          column: [
            /*{
              label: "试卷id",
              prop: "id",
              search:true,
              addDisplay: false,
              editDisplay: false,
              rules: [{
                required: true,
                message: "请输入主键",
                trigger: "blur"
              }]
            },*/
            {
              label: "试卷名称",
              prop: "name",
              search:true,
              rules: [{
                required: true,
                message: "请输入试卷名称",
                trigger: "blur"
              }]
            },
            {
              label: "专业分类",
              prop: "subjectId",
              type:"cascader",
              search:true,
              formslot:true,
              dicUrl:'/api/edu/subject/exam/tree',
              props: {
                label: "title"
              },
              rules: [{
                required: true,
                message: "请选择专业分类",
                trigger: "blur"
              }]
            },

            {
              label: "答题时间（分钟）",
              prop: "replyTime",
              type:"number",
              tip:"单位分钟",
              rules: [{
                required: true,
                message: "请输入答题时间",
                trigger: "blur"
              }]
            },
            {
              label: "试卷难度",
              prop: "level",
              type:"select",
              value: "simple",
              search:true,
              dicData:[
                {
                  label:'简单',
                  value:"simple"
                },{
                  label:'中等',
                  value: "commonly"
                },{
                  label:'困难',
                  value: "difficulty"
                }],
              rules: [{
                required: true,
                message: "请输入试卷难度",
                trigger: "blur"
              }]
            },
            {
              label: "参考人数",
              addDisplay: false,
              editDisplay: false,
              prop: "joinNum",
              type:"number",
              rules: [{
                required: true,
                message: "请输入参加考试人数",
                trigger: "blur"
              }]
            },
            {
              label: "平均分数",
              prop: "avgScore",
              display:false,
            },
            {
              label: "试卷类型",
              search:true,
              prop: "type",
              type:'select',
              dicUrl:'/api/exam/papertype/dictionary',
              props: {
                label: 'title',
                value: 'id'
              },
              rules: [{
                required: true,
                message: "请输入试卷类型",
                trigger: "blur"
              }]
            },
            {
              label: "试题总数",
              prop: "qstCount",
              display:false,
              rules: [{
                required: true,
                message: "请输入试题总数",
                trigger: "blur"
              }]
            },
            /*{
              label: "最后编辑时间",
              prop: "updateTime",
              hide:true,
              display:false,
              rules: [{
                required: true,
                message: "请输入最后编辑时间",
                trigger: "blur"
              }]
            },*/
            {
              label: "试卷总分",
              prop: "score",
              display:false,
              rules: [{
                required: true,
                message: "请输入试卷总分",
                trigger: "blur"
              }]
            },
            // {
            //   label: "及格分数比例",
            //   prop: "passRate",
            //   display:false,
            //   rules: [{
            //     required: true,
            //     message: "请输入及格分数比例",
            //     trigger: "blur"
            //   }]
            // },
            {
              label: "组卷方式",
              prop: "assemblyMode",
              type: "select",
              editDisplay: false,
              search:false,
              value:"manual",
              dicData:[
                {
                  label:'选题组卷',
                  value:"manual"
                }/*,{
                  label:'智能',
                  value: "automatic"
                }*/,{
                  label:'随机组卷',
                  value: "random"
                }],
              rules: [{
                required: true,
                message: "请输入组卷方式",
                trigger: "blur"
              }]
            },
            // {
            //   label: "试题类型",
            //   prop: "questionsType",
            //   display:false,
            //   rules: [{
            //     required: true,
            //     message: "请输入试题类型",
            //     trigger: "blur"
            //   }]
            // },
            /*{
              label: "及格/完成/通关人数",
              prop: "passNum",
              display:false,
              hide:true,
              rules: [{
                required: true,
                message: "请输入及格/完成/通关人数",
                trigger: "blur"
              }]
            },
            {
              label: "实际参加考试人数",
              prop: "actualJoinNum",
              display:false,
              hide:true,
              rules: [{
                required: true,
                message: "请输入实际参加考试人数（去重）",
                trigger: "blur"
              }]
            },*/
            {
              label: "排序值",
              prop: "sortNum",
              type:"number",
              sortable:true,
              value:"0",
              rules: [{
                required: true,
                message: "请输入排序值",
                trigger: "blur"
              }]
            },
            {
              label: "状态",
              prop: "status",
              type:"radio",
              value:"normal",
              dicData:[
                {
                  label:'正常',
                  value:"normal"
                },{
                  label:'冻结',
                  value: "frozen"
                }],
              rules: [{
                required: true,
                message: "请输入状态",
                trigger: "blur"
              }]
            },
            {
              label: "考试说明",
              prop: "info",
              span: 24,
              type:"textarea",
              rules: [{
                required: true,
                message: "请填写考试说明",
                trigger: "blur"
              }]
            },
            {
              label: "创建时间",
              prop: "addTime",
              display:false,
              rules: [{
                required: true,
                message: "创建时间",
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
          addBtn: this.vaildData(this.permission.exampaper_add, false),
          viewBtn: this.vaildData(this.permission.exampaper_view, false),
          delBtn: this.vaildData(this.permission.exampaper_delete, false),
          editBtn: this.vaildData(this.permission.exampaper_edit, false)
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
      saveExamQuestion(){
        saveQuestion(this.exampaperArr).then((res) => {
          // this.exampaperArr = []
          // this.dialogComposeVisible = false
          // this.onLoad(this.page);
        });
      },
      openExamAnalysis(exam){
        this.exam = exam
        this.dialogAnalysisVisible = true
      },
      openExamRecord(exam){
        this.exam = exam
        this.dialogRecordVisible = true
      },
      openExam(exam){
        this.exampaperArr = []
        queryQuestion(exam.id).then(res=>{
          this.exampaperArr = res.data.data
          this.dialogComposeVisible = true
        })
        this.exam = exam

      },
      rowSave(row, done, loading) {
        row.assemblyMode = "manual"
        add(row).then(() => {
          done();
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
        }, error => {
          window.console.log(error);
          loading();
        });
      },
      rowUpdate(row, index, done, loading) {
        row.assemblyMode = "manual"
        update(row).then(() => {
          done();
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
        }, error => {
          window.console.log(error);
          loading();
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
        this.$refs.crud.resetForm
        if (["edit", "view"].includes(type)) {
          getDetail(this.form.id).then(res => {
            this.form = res.data.data;
          });
        }

        console.log(this.form)
        this.form = {}
        console.log(this.form)
        done();
      },
      beforeClose(done, type) {
        console.log("关闭")
        this.form = {}
        done();
      },
      refreshChange() {
        this.onLoad(this.page, this.query);
      },
      searchReset() {
        this.query = {descs:"addTime"};
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
      onLoad(page, params = {}) {
        this.loading = true;
        if (typeof(this.query.subjectId) != "undefined"){
          this.query.subjectId = this.query.subjectId[1]
        }
        getList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
          this.page.total = data.total;
          this.data = data.records;
          this.loading = false;
          this.selectionClear();
        });
      },
      sortChange(val){
        console.log(val);
        if(val.order == null){
          this.query = {descs:"addTime"};
        }
        console.log(val.order)
        if(val.order == "ascending"){
          this.query = {ascs:val.prop};
        }
        if(val.order == "descending"){
          this.query = {descs:val.prop};
        }
        this.page.currentPage = 1;
        this.onLoad(this.page);
      }
    },
    created:function(){
      subjectTree().then(res =>{
        this.subjectTree = res.data.data;
      })
    },
  };
</script>

<style>
.el-dialog__body {
  padding: 0px 20px;
  color: #606266;
  font-size: 14px;
  word-break: break-all;
}
</style>
