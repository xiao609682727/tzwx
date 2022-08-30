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
               @refresh-change="refreshChange"
               @search-change="searchChange"
               @search-reset="searchReset"
               @selection-change="selectionChange"
               @current-change="currentChange"
               @size-change="sizeChange"
               @on-load="onLoad">
      <template slot="menuLeft">
        <el-button type="primary"
                   size="small"
                   icon="el-icon-s-order"
                   @click="dialogQuzrtzLogVisible = true">日 志
        </el-button>
        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   plain
                   v-if="permission.quartzjob_delete"
                   @click="handleDelete">删 除
        </el-button>
      </template>
      <template slot-scope="scope" slot="menu">
        <el-button
          type="text"
          icon="el-icon-video-play"
          size="small"
          @click.stop="execJob(scope.row)"
        >执行
        </el-button>
        <el-button
          type="text"
          icon="el-icon-refresh"
          size="small"
          @click.stop="updateIsPauseJob(scope.row)"
        >{{scope.row.isPause == '1'? '恢复' :'暂停'}}
        </el-button>
      </template>
    </avue-crud>
    <el-dialog title="执行日志" width="80%"  :visible.sync="dialogQuzrtzLogVisible">
      <quartzlog v-if="dialogQuzrtzLogVisible"></quartzlog>
    </el-dialog>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove, exec, updateIsPause} from "@/api/quartz/quartzjob";
  import {mapGetters} from "vuex";
  import quartzlog from "./quartzlog";

  export default {
    data() {
      return {
        dialogQuzrtzLogVisible:false,
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
          align: "center",
          index: true,
          viewBtn: false,
          selection: true,
          column: [
            {
              label: "任务ID",
              prop: "id",
              addDisplay: false,
              editDisplay: false,
              rules: [{
                required: true,
                message: "请输入ID",
                trigger: "blur"
              }]
            },
            {
              label: "任务名称",
              prop: "jobName",
              rules: [{
                required: true,
                message: "请输入任务名称",
                trigger: "blur"
              }]
            },
            {
              label: "Bean名称",
              prop: "beanName",
              rules: [{
                required: true,
                message: "请输入Spring Bean名称",
                trigger: "blur"
              }]
            },
            {
              label: "执行方法",
              prop: "methodName",
              rules: [{
                required: true,
                message: "请输入方法名称",
                trigger: "blur"
              }]
            },

            {
              label: "cron 表达式",
              prop: "cronExpression",
              rules: [{
                required: true,
                message: "请输入cron 表达式",
                trigger: "blur"
              }]
            },

            {
              label: "任务描述",
              prop: "description",
              rules: [{
                required: true,
                message: "请输入备注",
                trigger: "blur"
              }]
            },
            {
              label: "创建时间",
              prop: "createTime",
              editDisplay: false,
              addDisplay: false,
            },
            {
              label: "任务负责人",
              prop: "personInCharge",
              hide:true,
              rules: [{
                required: true,
                message: "请输入负责人",
                trigger: "blur"
              }]
            },
            {
              label: "报警邮箱",
              prop: "email",
              hide:true,
            },
            {
              label: "子任务ID",
              prop: "subTask",
              hide:true,
            },
            {
              label: "任务状态",
              prop: "isPause",
              value:"0",
              type: "radio",
              dataType:"string",
              dicData:[
                {
                  label:'启用',
                  value:"0"
                },{
                  label:'暂停',
                  value:"1"
                }
              ]
            },
            {
              label: "失败后暂停",
              prop: "pauseAfterFailure",
              hide:true,
              value:"2",
              type: "radio",
              dataType:"string",
              dicUrl: "/api/crazy-system/dict/dictionary?code=yes_no",
              props: {
                label: "dictValue",
                value: "dictKey"
              },
            },
            {
              label: "参数",
              prop: "params",
              type:"textarea",
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
          addBtn: this.vaildData(this.permission.quartzjob_add, false),
          viewBtn: this.vaildData(this.permission.quartzjob_view, false),
          delBtn: this.vaildData(this.permission.quartzjob_delete, false),
          editBtn: this.vaildData(this.permission.quartzjob_edit, false)
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
      execJob(row){
        exec(row.id).then(res => {
          this.$message({
            type: "success",
            message: res.data.msg
          });
        }, error => {
          window.console.log(error);
          this.$message({
            type: "error",
            message: "执行失败"
          });
        });
      },
      updateIsPauseJob(row){
        updateIsPause(row.id).then(res => {
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: res.data.msg
          });
        }, error => {
          window.console.log(error);
          this.$message({
            type: "error",
            message: "执行失败"
          });
        });
      },
      refreshChange() {
        this.onLoad(this.page, this.query);
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
      'quartzlog':quartzlog,
    },
  };
</script>

<style>
</style>
