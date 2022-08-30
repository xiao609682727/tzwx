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
               @submit="submit"
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
                   v-if="permission.courseCard_delete"
                   @click="handleDelete">删 除
        </el-button>
      </template>
      <template slot-scope="scope" slot="menu">
        <!--        <el-button type="text"
                           size="small"
                           icon="el-icon-edit"
                           plain
                           @click="showComment(scope.row,scope.index)">查看评价
                </el-button>-->
      </template>
      <template slot-scope="scope" slot="courseIdForm">
        <div>
          <div>
            <el-tag v-if="courseList.length >= 1"
                    closable @close="courseList.splice(index,1)"
                    v-for="(tag,index) in courseList"
                    :key="tag"
                    :disable-transitions="false"
                    :class="index>0?'ml10':''"
            >{{tag.courseName}}</el-tag>
          </div>
        <div>
          <el-button type="primary"
                     icon="el-icon-circle-plus-outline"
                     size="small"
                     @click.stop="handleShowSelectCourse(scope.row)">选择课程
          </el-button>
        </div>

        </div>
      </template>
      <template slot-scope="scope" slot="menu">
        <el-button type="text" icon="el-icon-notebook-2" size="small" @click="openCourseCard(scope.row)">卡管理</el-button>
        <el-button type="text" icon="el-icon-notebook-2"  size="small" @click="addCourseCardNums(scope.row)">加卡数量</el-button>
        <el-button type="text" icon="el-icon-notebook-2" v-if="scope.row.status==1" size="small" @click="handleInvalid(scope.row)">作废</el-button>
      </template>
    </avue-crud>

    <el-dialog title="添加课程" width="80%"  :visible.sync="handleShowSelectCourseVisible" @close="dialogCourse" append-to-body>
      <addcourse @adddata="adddata" ></addcourse>
    </el-dialog>
    <el-dialog title="增加卡的数量" width="30%"  :visible.sync="addCardIdIsOpen">
      <el-input-number v-model="cardNums" placeholder="添加数量" style="width: 45%;" :max="2000"></el-input-number>
      <span class="now_q_type" style="color:red">一次可添加卡的数量最高为2000</span>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addCardNums">确 定</el-button>
        <el-button @click="addCardIdIsOpen = false" >取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog title="卡管理" width="80%"  :visible.sync="dialogCourseCardVisible" @close="dialogCourseCard" >
      <cardcode :cardId="cardId"></cardcode>
    </el-dialog>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove,getCardCourseList,updateCourseCardInvalid,addCourseCardNums} from "@/api/courseCard/courseCard";
  import {mapGetters} from "vuex";
  import addcourse from "@/views/edu/classAddCourse";
  import cardcode from "@/views/coursecard/cardcode";
  export default {
    data() {
      let _this = this;
      //结束时间验证，结束时间要大于开始时间
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
      //结束时间验证，结束时间要大于开始时间
      let validateBeginTime = (rule, value, callback)=>{
        let nowDateFmt = new Date().Format("yyyy-MM-dd HH:mm:ss");
        if (value === '') {
          callback(new Error('请输入开始时间'));
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
        form: {},
        handleShowSelectCourseVisible: false,
        dialogCourseCardVisible: false,
        query: {},
        cardId: 0,
        courseList: [],
        addCardId:0,
        addCardIdIsOpen:false,
        cardNums:0,
        loading: true,
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        selectionList: [],
        option: {
          height: 'auto',
          align: "center",
          calcHeight: 80,
          searchShow: true,
          searchMenuSpan: 6,
          labelWidth: 150,
          tip: false,
          border: true,
          addBtn: true,
          index: false,
          viewBtn: false,
          editBtn:true,
          selection: true,
          column: [
            {
              label: "id",
              prop: "id",
              search: true,
              addDisplay:false,
              editDisplay:false,
              rules: [{
                required: true,
                message: "请输入主键id",
                trigger: "blur"
              }]
            },
            {
              label: "卡名称",
              prop: "name",
              search: true,
              rules: [{
                required: true,
                message: "请输入学习卡名称",
                trigger: "blur"
              }]
            },
            {
              label: "卡金额",
              prop: "money",
              rules: [{
                required: true,
                message: "请输入学习卡金额",
                trigger: "blur"
              }]
            },
            {
              label: "卡类型(1学习卡2学员卡3充值卡)",
              prop: "type",
              value:"1",
              addDisplay:false,
              editDisplay:false,
              hide:true,
              rules: [{
                required: true,
                message: "请输入卡类型(1学习卡2学员卡3充值卡)",
                trigger: "blur"
              }]
            },
            {
              label: "卡数量",
              prop: "num",
              type: "number",
              editDetail:true,
              maxRows:2000,
              rules: [{
                required: true,
                message: "请输入卡数量",
                trigger: "blur"
              }],
              tip:"添加学习卡时最大为2000张，如需在加可单独加卡",
            },
            {
              label: "课程",
              prop: "courseId",
              hide: true,
              span: 24,
              formslot: true,
              rules: [{
                // required: true,
                message: "请选择课程",
                trigger: "blur"
              }]
            },
            {
              label: "有效期开始时间",
              prop: "beginTime",
              type: "datetime",
              format: "yyyy-MM-dd HH:mm:ss",
              valueFormat: "yyyy-MM-dd HH:mm:ss",
              rules: [{
                required: true,
                validator: validateBeginTime,
                trigger: "blur"
              }]
            },
            {
              label: "有效期结束时间",
              prop: "endTime",
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
              label: "备注",
              prop: "remark",
              rules: [{
                required: false,
                message: "请输入备注",
                trigger: "blur"
              }]
            },
            {
              label: "创建人",
              prop: "createUser",
              addDisplay:false,
              editDisplay:false,
              hide:true,
              rules: [{
                required: true,
                message: "请输入创建人",
                trigger: "blur"
              }]
            },
            {
              label: "创建时间",
              prop: "createTime",
              addDisplay:false,
              editDisplay:false,
              rules: [{
                required: true,
                message: "请输入创建时间",
                trigger: "blur"
              }]
            },
            {
              label: "状态",
              prop: "status",
              addDisplay:false,
              editDisplay:false,
              type: "select",
              value:"1",
              search: true,
              dicData:[
                {
                  label:'正常',
                  value:1
                },{
                  label:'已作废',
                  value:2
                }
              ],
              rules: [{
                required: true,
                message: "请输入1正常 2已过期",
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
          addBtn: this.vaildData(this.permission.courseCard_add, false),
          viewBtn: this.vaildData(this.permission.courseCard_view, false),
          delBtn: this.vaildData(this.permission.courseCard_delete, false),
          editBtn: this.vaildData(this.permission.courseCard_edit, false)
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
      dialogCourse(){
        this.handleShowSelectCourseVisible = false;
      },
      dialogCourseCard(){
        this.dialogCourseCardVisible = false;
      },
      openCourseCard(row){
        this.cardId = row.id;
        this.dialogCourseCardVisible = true;
      },
      unique(arr) {//list去重
        const res = new Map();
        return arr.filter((arr) => !res.has(arr.courseId) && res.set(arr.courseId, 1));
      },
      adddata(selectionList){
        this.handleShowSelectCourseVisible = false;
        //把课程数据放到list中
        selectionList.forEach(ele => {
          this.courseList.push({
            "courseName":ele.courseName,
            "courseId": ele.id
          });
        });
        //list去重

        this.courseList = this.unique(this.courseList);
        // addList(list,this.courseId).then(() => {
        //   this.onLoad(this.page);
        //   this.$message({
        //     type: "success",
        //     message: "操作成功!"
        //   });
        // }, error => {
        //   console.log(error);
        // });
        this.$message({
          type: "success",
          message: "操作成功!"
        });
      },
      rowSave(row, done, loading) {
        let courseIds = "";
        if (this.courseList.length === 0) {
          this.$message.warning("请至少选择一个课程");
          // done();
          loading();
          return;
        }
        //把课程数据放到list中
        this.courseList.forEach(ele => {
          courseIds+=ele.courseId+",";
        });
        add(row,courseIds).then(() => {
          done();
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
          this.courseList =[];
        }, error => {
          window.console.log(error);
          loading();
        });
      },
      rowUpdate(row, index, done, loading) {
        let courseIds = "";
        if (this.courseList.length === 0) {
          this.$message.warning("请至少选择一个课程");
          // done();
          loading();
          return;
        }
        //把课程数据放到list中
        this.courseList.forEach(ele => {
          courseIds+=ele.courseId+",";
        });
        update(row,courseIds).then(() => {
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
          this.courseList=[];
          console.log(this.courseList);
          getCardCourseList(this.form.id).then(res => {
            //把课程数据放到list中
            res.data.data.forEach(ele => {
              this.courseList.push({
                "courseName":ele.courseName,
                "courseId": ele.courseId
              });
            });
            //list去重
            this.courseList = this.unique(this.courseList);
            console.log(this.courseList);
          });
        }
        if (["add"].includes(type)) {
          this.courseList = [];
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
      },
      submit(form,done){
        this.$message.success(JSON.stringify(form));
        done()
      },
      handleShowSelectCourse(){
        this.handleShowSelectCourseVisible = true;
      },
      handleInvalid(row) {
        this.$confirm("确定要作废吗？", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          // return remove(this.ids);
          // this.$message({
          //   type: "success",
          //   message: "确定!"
          // });
          let params={
            "id":row.id
          }
          updateCourseCardInvalid(params).then(res => {
            const data = res.data.data;
            this.onLoad(this.page);
            this.$message({
              type: "success",
              message: "成功!"
            });
            this.$refs.crud.toggleSelection();
          });
        }).then(() => {

        });
        // this.onLoad(this.page, this.query);
      },
      addCourseCardNums(row) {
        this.addCardId=0;
        this.cardNums=0;
        this.addCardId=row.id;
        this.addCardIdIsOpen=true;
      },
      addCardNums(){
        if(this.cardNums>2000){
          this.$message.warning("最大数量不可超过2000张");
          return;
        }
        let params={
          "num":this.cardNums,
          "id":this.addCardId
        }
        addCourseCardNums(params).then(res => {
          const data = res.data.data;
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "成功!"
          });
          this.addCardIdIsOpen=false;
          this.$refs.crud.toggleSelection();
        });
      },
    },
    components:{
      'addcourse':addcourse,
      'cardcode':cardcode,
    }
  };
</script>

<style>
  .ml10{margin-left: 10px}
  .now_q_type{
    margin-left: 15px;
    font-size: 10px;
  }
</style>
