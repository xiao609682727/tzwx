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
               @on-load="onLoad">
      <template slot="menuLeft">
<!--        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   plain
                   v-if="permission.exampaperrecord_delete"
                   @click="handleDelete">删 除
        </el-button>-->
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
        <el-button icon="el-icon-info" :size="size" :type="type" @click="openInfo(row)">查看详情</el-button>
      </template>
    </avue-crud>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove} from "@/api/exam/exampaperrecord";
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
          height: 'auto',
          calcHeight: 80,
          searchShow: true,
          searchMenuSpan: 6,
          tip: false,
          border: true,
          index: false,
          viewBtn: true,
          selection: true,
          refreshBtn:false,
          menu:false,
          align:"center",
          column: [
/*            {
              label: "主键id",
              prop: "id",
              rules: [{
                required: true,
                message: "请输入主键id",
                trigger: "blur"
              }]
            },*/
            {
              label: "账号",
              prop: "userName",
              search:true,
            },
            {
              label: "手机",
              prop: "mobile",
              search:true,
            },
            {
              label: "邮箱",
              prop: "email",
              search:true,
            },
            {
              label: "真实姓名",
              prop: "realName",
              search:true,
            },
            /*{
              label: "试卷名称",
              prop: "paperName",
              search:true,
              rules: [{
                required: true,
                message: "请输入试卷名字",
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
                message: "请输入专业id",
                trigger: "blur"
              }]
            },*/
            {
              label: "成绩",
              prop: "userScore",
              rules: [{
                required: true,
                message: "请输入用户得分",
                trigger: "blur"
              }]
            },
            {
              label: "正确率",
              prop: "accuracy",
              formatter: function (row,value) {
                return value+"%";
              },
              rules: [{
                required: true,
                message: "请输入正确率",
                trigger: "blur"
              }]
            },
            /*{
              label: "用户id",
              prop: "userId",
              rules: [{
                required: true,
                message: "请输入用户id",
                trigger: "blur"
              }]
            },
            {
              label: "试卷id",
              prop: "epId",
              rules: [{
                required: true,
                message: "请输入试卷id",
                trigger: "blur"
              }]
            },*/

            {
              label: "作答时长",
              prop: "testTime",
              formatter: function (row,s) {
                //计算分钟
                //算法：将秒数除以60，然后下舍入，既得到分钟数
                var h;
                h  =   Math.floor(s/60);
                //计算秒
                //算法：取得秒%60的余数，既得到秒数
                s  =   s%60;
                //将变量转换为字符串
                h    +=    '';
                s    +=    '';
                //如果只有一位数，前面增加一个0
                h  =   (h.length==1)?'0'+h:h;
                s  =   (s.length==1)?'0'+s:s;
                return h+':'+s;
              },
              rules: [{
                required: true,
                message: "请输入该用户考试所用时间单位是秒",
                trigger: "blur"
              }]
            },
            {
              label: "考试题数",
              prop: "qstCount",
              rules: [{
                required: true,
                message: "请输入考试题数",
                trigger: "blur"
              }]
            },
            {
              label: "已做题数",
              prop: "doneCount",
              rules: [{
                required: true,
                message: "请输入已做题数",
                trigger: "blur"
              }]
            },
            {
              label: "正确题数",
              prop: "correctNum",
              rules: [{
                required: true,
                message: "请输入正确题数",
                trigger: "blur"
              }]
            },
/*
            {
              label: "组卷方式",
              prop: "type",
              dicData:[
                {
                  label:'手动',
                  value:1
                },{
                  label:'智能',
                  value: 2
                },{
                  label:'随机',
                  value: 3
                }],
              rules: [{
                required: true,
                message: "请输入1随机2系统生成试卷",
                trigger: "blur"
              }]
            },
            {
              label: "考试状态",
              prop: "status",
              dicData:[
                {
                  label:'完成',
                  value:0
                },{
                  label:'未考完',
                  value: 1
                }],
              rules: [{
                required: true,
                message: "请输入0为默认完成，1为未考完",
                trigger: "blur"
              }]
            },*/
            {
              label: "交卷时间",
              prop: "addTime",
              rules: [{
                required: true,
                message: "请输入添加时间",
                trigger: "blur"
              }]
            },
            /*{
              label: "更新时间",
              prop: "updateTime",
              rules: [{
                required: true,
                message: "请输入最后更新时间",
                trigger: "blur"
              }]
            },*/
            /*{
              label: "是否阅卷",
              prop: "read",
              dicData:[
                {
                  label:'只要交卷',
                  value:"false"
                },{
                  label:'只要阅卷',
                  value: "true"
                }],
              rules: [{
                required: true,
                message: "请输入是否阅卷 只要交卷false 只要阅卷true",
                trigger: "blur"
              }]
            },*/
          ]
        },
        data: []
      };
    },
    computed: {
      ...mapGetters(["permission"]),
      permissionList() {
        return {
          addBtn: this.vaildData(false),
          viewBtn: this.vaildData(false),
          delBtn: this.vaildData(false),
          editBtn: this.vaildData(false)
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
      openInfo(row){
        console.log(row)
        window.open("/exam/examResult");
      },
      rowSave(row, done, loading) {
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
      sizeChange(pageSize){
        this.page.pageSize = pageSize;
      },
      onLoad(page, params = {}) {
        this.loading = true;
        this.query.epId = this.exam.id
        getList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
          this.page.total = data.total;
          this.data = data.records;
          this.loading = false;
          this.selectionClear();
        });
      }
    },
    props:{
      exam: Object,
    },
    watch: {
      exam: {
        deep: true,
        handler(newVal){
          console.log(newVal)
        }
      }
    },
  };
</script>

<style>
</style>
