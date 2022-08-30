<template>
  <basic-container>
    <avue-crud :option="option"
               :table-loading="loading"
               :data="data"
               :page="page"
               :search.sync="search"
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
                   v-if="permission.student_delete"
                   @click="handleDelete">删 除
        </el-button>
        <el-button type="success"
                   size="small"
                   plain
                   icon="el-icon-upload2"
                   @click="handleImport">导入
        </el-button>
        <el-button type="warning"
                   size="small"
                   plain
                   icon="el-icon-download"
                   @click="handleExport">导出
        </el-button>
      </template>
      <template slot-scope="scope" slot="menu">
        <el-dropdown size="small">
          <el-button type="primary" size="small">
            用户信息<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="showPwd(scope.row)" >修改密码</el-dropdown-item>
            <el-dropdown-item @click.native="openCoursefavorites(scope.row)" >收藏管理</el-dropdown-item>
            <el-dropdown-item @click.native="openCoursestudy(scope.row)">学习记录</el-dropdown-item>
            <el-dropdown-item @click.native="openUserloginlog(scope.row)">登录日志</el-dropdown-item>
            <el-dropdown-item @click.native="openCourse(scope.row)">赠送课程</el-dropdown-item>
            <el-dropdown-item @click.native="openOrder(scope.row)">查看订单</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </template>
      <template slot-scope="scope" slot="agentIdSearch">
        <el-select  v-model="search.agentId" placeholder="请选择" >
          <el-option
            v-for="item in agenTree"
            :key="item.id"
            :label="item.name"
            :value="item.id"/>
        </el-select>
      </template>
    </avue-crud>
    <el-dialog title="修改密码" width="30%" :visible.sync="dialogPwdFormVisible">
      <el-form :model="pwdForm">
        <el-form-item label="密码：" label-width="120px" >
          <el-input v-model="pwdForm.password" placeholder="请输入密码" style="width: 70%;" show-password></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogPwdFormVisible = false" >取 消</el-button>
        <el-button type="primary" @click="setPwd">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="收藏管理" width="80%" :visible.sync="dialogFavoritesVisible" @close="dialogFavorites" >
      <coursefavorites v-if="coursefavoritesFlag" :userId="userId"></coursefavorites>
    </el-dialog>
    <el-dialog title="学习记录" width="80%" :visible.sync="dialogCoursestudyVisible" @close="dialogCoursestudy">
      <coursestudyhistory v-if="coursestudyhistoryFlag" :userId="userId"></coursestudyhistory>
    </el-dialog>
    <el-dialog title="登录日志" width="80%" :visible.sync="dialogUserloginlogVisible" >
      <userloginlog v-if="dialogUserloginlogVisible" :userId="userId"></userloginlog>
    </el-dialog>
    <el-dialog title="用户数据导入"
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
    <el-dialog title="课程列表" width="80%"  :visible.sync="dialogCourseVisible" append-to-body>
      <addcourse @adddata="adddata" v-if="dialogCourseVisible" ></addcourse>
    </el-dialog>
    <el-dialog title="订单列表" width="80%"  :visible.sync="dialogOrderVisible" append-to-body>
      <orders @adddata="adddata" v-if="dialogOrderVisible" :userId="userId" ></orders>
    </el-dialog>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove, setPassword,getAdminUserList} from "@/api/user/student";
  import {give} from "@/api/edu/course";
  import {mapGetters} from "vuex";
  import coursefavorites from "../edu/coursefavorites";
  import coursestudylisthistory from "../edu/coursestudylisthistory";
  import userloginlog from "./userloginlog";
  import orders from "../edu/ordersUser";
  import {getToken} from '@/util/auth';
  import addcourse from "../web/addcourse";

  import { isvalidatemobile,cardid } from "@/util/validate";

  export default {
    data() {
      var validateCardid = (rule, value, callback) => {
        let result = cardid(value)
        console.log(result)
        if (value === '') {
          callback(new Error('请输入身份证号'));
        } else if (result[0]) {
          callback(new Error(result[1]));
        } else {
          callback();
        }
      };
      return {
        search:{},
        errorMsg:"",
        excelBox: false,
        agenTree:[],
        userId:"",
        dialogPwdFormVisible:false,
        dialogCourseVisible:false,
        dialogOrderVisible:false,
        dialogUserloginlogVisible:false,
        coursestudyhistoryFlag:false,
        coursefavoritesFlag:false,
        dialogCoursestudyVisible:false,
        dialogFavoritesVisible:false,
        pwdForm: {
          id:"",
          password:""
        },
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
          menuType:'menu',
          viewBtn: true,
          selection: true,
          column: [
            {
              label: "ID",
              prop: "id",
              addDisplay: false,
              editDisplay: false,
              search:true
            },
            {
              label: "手机",
              prop: "mobile",
              search:true,
              maxlength:11,
              rules: [{
                required: false,
                trigger: "blur"
              }]
            },
            {
              label: "邮箱",
              prop: "email",
              search:true,
              rules: [{
                required: false,
                trigger: "blur"
              },{
                type: 'email',
                required: false,
                trigger: "blur"
              }]
            },

            {
              label: "账号",
              prop: "userName",
              search:true,
              rules: [{
                required: false,
                trigger: "blur"
              }]
            },
            {
              label: "密码",
              prop: "password",
              type: "password",
              hide:true,
              editDisplay: false,
              viewDisplay: false,
              rules: [{
                required: true,
                message: "请输入密码",
                trigger: "blur"
              }]
            },
            {
              label: "昵称",
              prop: "showName",
              search:true,
              rules: [{
                required: false,
                trigger: "blur"
              }]
            },
            {
              label: "真实姓名",
              prop: "realName",
              search:true,
            },
            {
              label: "身份证号",
              maxlength:18,
              prop: "idCardNo",
              rules: [{
                // validator: validateCardid,
                trigger: "blur"
              }]
            },
            {
              label: "性别",
              prop: "sex",
              type: "radio",
              dataType: "number",
              value: 1,
              props: {
                label: "dictValue",
                value: "dictKey"
              },
              dicUrl: "/api/crazy-system/dict/dictionary?code=sex",
            },
            {
              label: "年龄",
              prop: "age",
              type: "number",
            },

            {
              label: "用户头像",
              prop: "headImg",
              type:"upload",
              dataType: 'string',
              hide:true,
              action: '/api/common/upload?path=student',
              propsHttp: {
                res: 'data'
              },
              listType: 'picture-img',
            },
            {
              label: "注册来源",
              prop: "registerFrom",
              type: "select",
              search:true,
              addDisplay: false,
              value: "2",
              props: {
                label: "dictValue",
                value: "dictKey"
              },
              dicUrl: "/api/crazy-system/dict/dictionary?code=user_registerFrom",

            },
            /*{
              label: "学校",
              prop: "school",
            },
            {
              label: "专业",
              prop: "subject",
              type: "tree",
              dicUrl: "/api/edu/subject/tree",
              search:true,
              props: {
                label: "title"
              },
            },
            {
              label: "签名简介",
              prop: "userInfo",
              type: "textarea",
            },*/
            {
              label: "注册时间",
              prop: "createTime",
              editDisplay: false,
              addDisplay: false,
              rules: [{
                required: true,
                message: "请输入注册时间",
                trigger: "blur"
              }]
            },

            {
              label: "状态",
              prop: "isAvalible",
              type: "radio",
              dataType: "number",
              value: 2,
              dicData:[
                {
                  label:'正常',
                  value:2
                },{
                  label:'冻结',
                  value:1
                }
              ]
            },
            {
              label: "代理商",
              prop: "agentId",
              editDisplay: false,
              addDisplay: false,
              search:true,
              searchslot:true,
              formatter: function (row,value) {
                if(value==-1){
                  return "";
                }else {
                  return value;
                }
              }
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
              action: "/api/user/student/import-user"
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
          addBtn: this.vaildData(this.permission.student_add, false),
          viewBtn: this.vaildData(this.permission.student_view, false),
          delBtn: this.vaildData(this.permission.student_delete, false),
          editBtn: this.vaildData(this.permission.student_edit, false)
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
      handleExport() {
        this.$confirm("是否导出用户数据?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          window.open(`/api/user/student/export-user?crazy-auth=${getToken()}`+
          `&id=`+this.search.id+`&mobile=`+this.search.mobile+`&email=`+this.search.email+`&registerFrom=`+this.search.registerFrom+
          `&userName=`+this.search.userName+`&showName=`+this.search.showName+`&realName=`+this.search.realName);
        });
      },
      showPwd(row){
        this.dialogPwdFormVisible = true;
        this.pwdForm.id = row.id;
        this.pwdForm.password = "";
      },
      setPwd(){
        if(this.pwdForm.password == ''){
          this.$message({
            type: "warning",
            message: "请填写密码"
          });
          return;
        }

        setPassword(this.pwdForm).then(res=>{
          this.$message({
            type: "success",
            message: "操作成功!"
          });
          this.dialogPwdFormVisible = false;
        })
      },
      openCourse(row){
        this.userId = row.id;
        this.dialogCourseVisible = true;
      },
      openOrder(row){
        this.userId = row.id;
        this.dialogOrderVisible = true;
      },
      adddata(selectionList){
        this.dialogCourseVisible = false;
        let courseIds = "";
        selectionList.forEach(ele => {
          courseIds+=ele.id+",";
        });
        courseIds = courseIds.substring(0,courseIds.length - 1);
        give(this.userId,courseIds).then(() => {
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
        }, error => {
          console.log(error);
        });
      },
      handleTemplate() {
        window.open(`static/template/批量开通用户模板.xlsx`);
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
      dialogCoursestudy(){
        this.coursestudyhistoryFlag = false;
      },
      dialogFavorites(){
        this.coursefavoritesFlag = false;
      },
      openCoursefavorites(row){
        this.userId = row.id;
        this.dialogFavoritesVisible = true;
        this.coursefavoritesFlag = true;
      },
      openCoursestudy(row){
        this.userId = row.id;
        this.dialogCoursestudyVisible = true;
        this.coursestudyhistoryFlag = true;
      },
      openUserloginlog(row){
        this.userId = row.id;
        this.dialogUserloginlogVisible = true;
      },
      rowSave(row, loading, done) {
        if (this.form.mobile == '' && this.form.email == '' && this.form.userName == '') {
          this.$message({
            type: "warning",
            message: "手机、邮箱、账号至少填写一个！"
          });
          done();
          return;
        }else if (this.form.mobile != '' && isvalidatemobile(this.form.mobile)[0]) {
          this.$message({
            type: "warning",
            message: isvalidatemobile(this.form.mobile)[1]
          });
          done();
          return;
        }
        row.registerFrom = 4;
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
        if (this.form.mobile == '' && this.form.email == '' && this.form.userName == '') {
          this.$message({
            type: "warning",
            message: "手机、邮箱、账号至少填写一个！"
          });
          done();
          return;
        }else if (this.form.mobile != '' && isvalidatemobile(this.form.mobile)[0]) {
          this.$message({
            type: "warning",
            message: isvalidatemobile(this.form.mobile)[1]
          });
          done();
          return;
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

        getAdminUserList().then(res => {
          this.agenTree=res.data.data;
          console.log(this.agenTree);
        });
        getList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
          this.page.total = data.total;
          this.data = data.records;
          debugger
          for(let i=0;i<this.data.length;i++){
            for(let y=0;y<this.agenTree.length;y++){
              if(this.data[i].agentId==this.agenTree[y].id){
                this.data[i].agentId=this.agenTree[y].name
              }
            }
          }
          this.loading = false;
          this.selectionClear();
        });
      }
    },
    components:{
      'coursefavorites':coursefavorites,
      'coursestudyhistory':coursestudylisthistory,
      'userloginlog':userloginlog,
      'addcourse':addcourse,
      'orders':orders
    }
  };
</script>

<style>
</style>
