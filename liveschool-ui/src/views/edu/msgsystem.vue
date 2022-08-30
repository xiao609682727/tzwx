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
                   @click="handleAdd">新 增
        </el-button>
        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   plain
                   v-if="permission.msgsystem_delete"
                   @click="handleDelete">删 除
        </el-button>
      </template>
      <template slot-scope="scope" slot="idsForm">
        <el-button
          type="primary"
          icon="el-icon-circle-plus-outline"
          size="small"
          @click.stop="handleShowTeacher(scope.row)"
        >选择用户
        </el-button>
      </template>
      <template slot-scope="scope" slot="sendRangeForm">
        <el-radio-group v-model="scope.row.sendRange" @change="change">
          <el-radio label="1">所有用户</el-radio>
          <el-radio label="2" >按用户</el-radio>
        </el-radio-group>
      </template>
      <template slot-scope="scope" slot="userIdForm">
        <el-input
          type="textarea"
          disabled="true"
          :rows="2"
          placeholder="请输入内容"
          v-model="userIdsStr">
        </el-input>
      </template>
    </avue-crud>
    <el-dialog title="学生列表" width="80%"  :visible.sync="dialogStudentVisible" append-to-body>
      <student  @datasubmit="dataSubmit" v-if="dialogStudentVisible"></student>
    </el-dialog>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove} from "@/api/edu/msgsystem";
  import {mapGetters} from "vuex";
  import student from "../edu/student";

  export default {
    data() {
      return {
        userIdsStr:"",
        userList:[],
        dialogStudentVisible:false,
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
          index: false,
          viewBtn: false,
          selection: true,
          addBtn:false,
          editBtn:false,
          saveBtnText:'发送',
          column: [
            {
              label: "id",
              prop: "id",
              addDisplay:false,
              editDIsplay:false
            },
            {
              label: "消息内容",
              prop: "content",
              type:"ueditor",
              span:24,
              component: "avueUeditor",
              options:{
                //普通图片上传
                action: "/api/common/upload",
                props: {
                  res: "data",
                  url:'url'
                },
              },
              rules: [{
                required: true,
                message: "请输入信内容",
                trigger: "blur"
              }]
            },
            {
              label: "发送范围",
              prop: "sendRange",
              type:"radio",
              search:true,
              value: "1",
              span:24,
              formslot: true,
              dicData:[
                {
                  label:'所有用户',
                  value:"1"
                },{
                  label:'按用户',
                  value:"2"
                }
              ],
            },
            {
              label: "创建时间",
              prop: "createTime",
              addDisplay:false,
              editDisplay:false
            },
            {
              label: "用户列表",
              prop: "userId",
              hide: true,
              showColumn:false,
              span: 24,
              formslot: true,
              addDisplay:false,
            },
            {
              label: "选择用户",
              prop: "ids",
              hide:true,
              addDisplay:false,
              editDisplay:false,
              span: 24,
              formslot: true,
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
          addBtn: this.vaildData(this.permission.msgsystem_add, false),
          viewBtn: this.vaildData(this.permission.msgsystem_view, false),
          delBtn: this.vaildData(this.permission.msgsystem_delete, false),
          editBtn: this.vaildData(this.permission.msgsystem_edit, false)
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
      change(value){
        this.option.column.filter(item => {
          if (item.prop === "ids"||item.prop === "userId") {
            if(value == "1"){
              item.addDisplay = false;
            }
            if(value == "2"){
              item.addDisplay = true;
            }
          }
        });

      },
      dataSubmit(selection){
        console.log(selection)
        let userIds = "";
        let userNames = "";
        let userIdsStrListc=[{},{}];
        if(this.userList!=undefined && this.userList!=null && this.userList!=''){
          userIdsStrListc = selection.concat(this.userList);
        }else {
          userIdsStrListc = selection;
        }
        this.userList = this.uniquelist(userIdsStrListc);
        selection.forEach(ele => {
          userIds+=ele.id+",";
          if(ele.showName!=null&&ele.showName!=''&&ele.showName!=undefined){
            userNames += ele.showName+",";
          }
          else {
            if(ele.mobile!=null&&ele.mobile!=''&&ele.mobile!=undefined){
              userNames += ele.mobile+",";
            }
            else {
              if(ele.realName!=null&&ele.realName!=''&&ele.realName!=undefined){
                userNames += ele.realName+",";
              }
              else {
                if(ele.email!=null&&ele.email!=''&&ele.email!=undefined){
                  userNames += ele.email+",";
                }else {
                  userNames += ele.userName+",";
                }
              }
            }
          }
        })
        userIds = userIds.substring(0,userIds.length - 1);
        userNames = userNames.substring(0,userNames.length - 1);
        let userIdsStrList='';
        if(this.userIdsStr!=null && this.userIdsStr!=undefined && this.userIdsStr!=''){
          userIdsStrList = this.userIdsStr+','+userNames;
        }else {
          userIdsStrList = userNames;
        }
        let userIdsStrc = this.unique(userIdsStrList.trim().split(","));
        this.userIdsStr=userIdsStrc;
        this.dialogStudentVisible = false;
      },
      handleShowTeacher(){
        this.dialogStudentVisible = true;
      },
      uniquelist(arr) {
        const res = new Map();
        return arr.filter((arr) => !res.has(arr.id) && res.set(arr.id, 1));
      },
      unique(arr) {
       return Array.from(new Set(arr));
      },
      rowSave(row, done, loading) {
        let userIds = "";
        this.userList.forEach(ele => {
          userIds+=ele.id+",";
        })
        userIds = userIds.substring(0,userIds.length - 1);
        row.ids = userIds;
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
      handleAdd() {
        this.userIdsStr="";
        this.userList=[];
        this.option.column.filter(item => {
          if (item.prop === "ids"||item.prop === "userId") {
              item.addDisplay = false;
          }
        });
        this.$refs.crud.rowAdd();
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
      'student':student
    },
  };
</script>

<style>
</style>
