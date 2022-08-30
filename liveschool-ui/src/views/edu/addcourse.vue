<template>
  <basic-container>
    <avue-crud  :search.sync="search"
      :option="option"
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
                   @click="handleAdd">添加
        </el-button>
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
      <template slot-scope="scope" slot="subjectIdForm">
        <el-cascader
          placeholder="专业分类"
          :options="subjectTree"
          v-model="form.subjectId"
          :props="{'label':'title','emitPath':false}"
          filterable></el-cascader>
      </template>
      <template slot-scope="scope" slot="subjectIdSearch">
        <el-cascader
          placeholder="专业分类"
          :options="subjectTree"
          v-model="search.subjectId"
          :props="{'label':'title','emitPath':false}"
          filterable></el-cascader>
      </template>
    </avue-crud>
  </basic-container>
</template>

<script>
  import {getDetail, add, update, remove, getEndTimeType,gePage,subjectTree} from "@/api/edu/course";
  import {mapGetters} from "vuex";
  import coursepackage from "./coursepackage";

  export default {
    data() {
      return {
        search:{},
        subjectTree:[],
        losetypeList:[],
        courseId:"",
        dialogKpointVisible: false,
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
          viewBtn: false,
          delBtn: false,
          editBtn: false,
          addBtn: false,
          menu:false,
          selection: true,
          labelWidth: 150,
          column: [
            {
              label: "课程名称",
              prop: "courseName",
              search :true,
              span: 24,
              rules: [{
                required: true,
                message: "请输入课程名称",
                trigger: "blur"
              }]
            },
            {
              label: "课程专业分类",
              prop: "subjectId",
              type: "cascader",
              tip:"课程分类只可以选择二级分类",
              placeholder:"请输入要选择的专业，支持搜索",
              filterable: true,
              searchLabelWidth:110,
              span: 24,
              search :true,
              formslot:true,
              searchslot:true,
              dicUrl: "/api/edu/subject/tree",
              props: {
                label: "title"
              },
              rules: [{
                required: true,
                message: "请输入课程专业分类",
                trigger: "blur"
              }]
            },
            {
              label: "课程原价格",
              prop: "sourcePrice",
              type: "number",
              tip: "只显示价格",
              precision: 2,
              rules: [{
                required: true,
                message: "请输入课程原价格",
                trigger: "blur"
              }]
            },
            {
              label: "课程销售价格",
              prop: "currentPrice",
              type: "number",
              precision: 2,
              tip: "（实际支付价格）设置为0则可免费观看",
              rules: [{
                required: true,
                message: "请输入课程销售价格",
                trigger: "blur"
              }]
            },
            {
              label: "课程简介",
              prop: "title",
              type: "textarea",
              span: 24,
              hide:true,
              rules: [{
                required: true,
                message: "请输入课程简介",
                trigger: "blur"
              }]
            },
            {
              label: "课程详情",
              prop: "context",
              type: "textarea",
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
              action: '/api/common/upload?path=course',
              propsHttp: {
                res: 'data'
              },
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
              type: "number",
            },
            {
              label: "评论数",
              prop: "commentNum",
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
              type: "number",
            },
            {
              label: "有效期类型",
              prop: "losetype",
              formslot: true,
              dataType: "string",
              value: "0",
              row: true,
              dicUrl: "/api/crazy-system/dict/dictionary?code=end_time_type",
              props: {
                label: "dictValue",
                value: "dictKey"
              },
            },
            {
              label: "课程有效期",
              prop: "loseTime",
              type: "number",
              hide: true,
              addDisplay: false,
              editDisplay: false,
              tip:"商品订单过期时间点",
              rules: [{
                required: true,
                message: "请输入课程有效期",
                trigger: "blur"
              }]
            },
            {
              label: "有效结束时间",
              prop: "endTime",
              hide: true,
              type: "datetime",
              format: "yyyy-MM-dd HH:mm:ss",
              valueFormat: "yyyy-MM-dd HH:mm:ss",
              rules: [{
                required: true,
                message: "请输入有效结束时间",
                trigger: "blur"
              }]
            },
            {
              label: "排序",
              prop: "sort",
              type: "number",
              value: "1",
              rules: [{
                required: true,
                message: "请输入排序",
                trigger: "blur"
              }]
            },
            {
              label: "课程类型",
              prop: "sellType",
              type: "radio",
              row: true,
              addDisplay: false,
              editDisplay: false,
              search: true,
              dataType: "string",
              value: "1",
              dicData:[
                {
                  label:'点播',
                  value:"1"
                },{
                  label:'直播',
                  value:"2"
                }
              ],
              rules: [{
                required: true,
                message: "请输入课程类型",
                trigger: "blur"
              }]
            },
            {
              label: "课程状态",
              prop: "isAvaliable",
              type: "radio",
              search :true,
              dataType: "number",
              value: 1,
              row: true,
              dicUrl: "/api/crazy-system/dict/dictionary?code=course_status",
              props: {
                label: "dictValue",
                value: "dictKey"
              },
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
            if(value == "0"){
              item.editDisplay = false;
              item.addDisplay = false;
            }
            if(value == "1"){
              item.editDisplay = true;
              item.addDisplay = true;
            }
          }
          if (item.prop === "endTime") {
            if(value == "0"){
              item.editDisplay = true;
              item.addDisplay = true;
            }
            if(value == "1"){
              item.editDisplay = false;
              item.addDisplay = false;
            }
          }
        });
      },
      openCourseKpoint(row){
        this.courseId = row.id;
        this.dialogKpointVisible = true;
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
      handleAdd() {
        if (this.selectionList.length === 0) {
          this.$message.warning("请选择至少一条数据");
          return;
        }
        this.$emit('adddata',this.selectionList);
        //清空选中的数据
        this.selectionClear();
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
      refreshChange() {
        this.onLoad(this.page, this.query);
      },
      sizeChange(pageSize){
        this.page.pageSize = pageSize;
      },
      onLoad(page, params = {}) {
        this.loading = true;
        gePage(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
          this.page.total = data.total;
          this.data = data.records;
          this.loading = false;
          this.selectionClear();
        });
      }
    },
    components:{
      'coursepackage':coursepackage,
    },
    created:function(){
      getEndTimeType().then(res => {
        this.losetypeList = res.data.data;
      });
      subjectTree().then(res =>{
        this.subjectTree = res.data.data;
      })
    },
  };
</script>

<style>
</style>
