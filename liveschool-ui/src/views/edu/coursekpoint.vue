<template>
  <basic-container>
    <avue-crud :option="option"
               :table-loading="loading"
               :data="data"
               :page="page"
               :permission="permissionList"
               :before-open="beforeOpen"
               :upload-before="uploadBefore"
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
                   @click="showKpointDialo">章节目录
        </el-button>
        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   plain
                   v-if="permission.coursekpoint_delete"
                   @click="handleDelete">删 除
        </el-button>
      </template>
      <template slot-scope="scope" slot="chooseVideoForm">
        <el-tag
          v-if="form.videoUrl != ''"
          @close="closeVideo"
          closable>
          {{videoName}}
        </el-tag>
        <el-button type="primary" v-if="form.videoUrl == ''" @click="dialogVideoVisible = true">选择视频</el-button>
      </template>
      <template slot-scope="scope" slot="menu">
        <el-button
          type="text"
          icon="el-icon-edit"
          size="small"
          v-if="scope.row.kpointType=='0'"
          @click.stop="handleEdit2(scope.row,scope.index)"
        >编辑
        </el-button>
        <el-button
          type="text"
          icon="el-icon-edit"
          size="small"
          v-if="scope.row.kpointType=='1'"
          @click.stop="handleEdit1(scope.row,scope.index)"
        >编辑
        </el-button>
        <el-button
          type="text"
          icon="el-icon-delete"
          size="small"
          @click.stop="handleDel(scope.row,scope.index)"
        >删除
        </el-button>
        <el-button
          type="text"
          icon="el-icon-circle-plus-outline"
          size="small"
          v-if="scope.row.kpointType=='0'"
          @click.stop="handleAdd(scope.row,scope.index)"
        >新增子项
        </el-button>
        <el-button
          type="text"
          icon="el-icon-notebook-2"
          size="small"
          v-if="scope.row.parentId!='0'"
          @click="openMaterial(scope.row)"
        >资料管理
        </el-button>
      </template>
      <template slot-scope="scope" slot="videoTypeForm">
        <el-select v-model="scope.row.videoType" @change="change" placeholder="请选择" >
          <el-option
            v-for="item in supplierList"
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
    </avue-crud>
    <el-dialog title="添加章节目录" :modal="true" :visible.sync="dialogFormVisible" append-to-body>
      <el-form :model="form">
        <el-form-item label="目录名称" :label-width="formLabelWidth">
          <el-input v-model="name" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="addKponint">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="讲师列表" width="80%" :visible.sync="dialogTeacherVisible" append-to-body>
      <teacher v-if="teaFlag" @datasubmit="dataSubmit"></teacher>
    </el-dialog>
    <el-dialog title="视频列表" width="80%" :visible.sync="dialogVideoVisible" append-to-body>
      <choose-videosource v-if="dialogVideoVisible" @videodata="videodata"></choose-videosource>
    </el-dialog>
    <el-dialog title="资料管理" width="80%" :visible.sync="dialogDataVisible" append-to-body>
      <courseMaterial :kpointId="kpointId" :courseId="courseId"></courseMaterial>
    </el-dialog>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove, getSupplier,getUserDetail} from "@/api/edu/coursekpoint";
  import {getDetailByIdVarchar} from "@/api/edu/videosource";
  import {mapGetters} from "vuex";
  import teacher from "./teacher";
  import courseMaterial from "./courseMaterial";
  import chooseVideosource from "./chooseVideosource";

  export default {
    data() {
      return {
        videoName:"",
        dialogVideoVisible:false,
        dialogDataVisible:false,
        teaFlag: false,
        tags:[],
        kpointId:"",
        supplierList:[{
          dictValue:"阿里云点播",
          dictKey:"aliyunvod"
        },{
          dictValue:"视频云点播",
          dictKey:"baijiayunvod"
        }/*,{
          dictValue:"保利威点播",
          dictKey:"polyvvod"
        }*/,{
          dictValue:"其他",
          dictKey:"othervod"
        }],
        name: "",
        formLabelWidth: "120px",
        dialogTeacherVisible:false,
        dialogFormVisible:false,
        form: {},
        query: {courseId:this.courseId},
        loading: true,
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        selectionList: [],
        option: {
          tip: false,
          tree: true,
          border: true,
          index: false,
          addBtn: false,
          editBtn: false,
          delBtn: false,
          viewBtn: false,
          selection: false,
          labelWidth: 150,
          defaultExpandAll:true,
          columnBtn:false,
          column: [
            {
              label: "选择视频",
              prop: "chooseVideo",
              formslot:true,
              span: 24,
              hide: true,
            },
            {
              label: "名称",
              prop: "name",
              span: 24,
              rules: [{
                required: true,
                message: "请输入节点名称",
                trigger: "blur"
              }]
            },
            {
              label: "视频来源",
              prop: "videoType",
              hide: true,
              display:false,
              span: 24,
              value: "",
              dataType: "string",
              formslot: true,
            },
            {
              label: "视频id",
              prop: "videoUrl",
              span: 24,
              hide: true,
            },
            {
              label: "视频时长",
              prop: "videoTime",
              span: 24,
              hide: true,
              rules: [{
                required: true,
                message: "请输入视频时长",
                trigger: "blur"
              }]
            },

            {
              label: "上级类型",
              prop: "parentId",
              hide: true,
              addDisplay: false,
              editDisplay: false,
              type: "select",
              span: 24,
              dicUrl: "/api/edu/coursekpoint/tree",
              props: {
                label: "title"
              },
            },
            {
              label: "课程章节类型",
              prop: "kpointType",
              hide: true,
              span: 24,
              addDisplay: false,
              editDisplay: false,
            },
            {
              label: "排序",
              prop: "sort",
              type: "number",
              value: 1,
              span: 24,
              rules: [{
                required: true,
                message: "请输入显示排序",
                trigger: "blur"
              }]
            },
            /*{
              label: "是否可以试听",
              prop: "isFree",
              type: "radio",
              hide: true,
              value: 2,
              span: 24,
              dataType: "number",
              props: {
                label: "dictValue",
                value: "dictKey"
              },
              dicUrl: "/api/crazy-system/dict/dictionary?code=yes_no",
            },*/
            // {
            //   label: "讲师",
            //   prop: "teacherId",
            //   hide: true,
            //   span: 24,
            //   formslot: true,
            // },
            /*{
              label: "video视频 ",
              prop: "fileType",
              hide: true,
              type: "select",
              value: "1",
              dataType: "string",
              props: {
                label: "dictValue",
                value: "dictKey"
              },
              dicUrl: "/api/crazy-system/dict/dictionary?code=file_type",
              rules: [{
                required: true,
                message: "请输入video视频 ",
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
          addBtn: this.vaildData(true, false),
          viewBtn: this.vaildData(true, false),
          delBtn: this.vaildData(true, false),
          editBtn: this.vaildData(true, false)
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
      courseId: Number
    },
    watch: {
      courseId: {
        deep: true,
        handler(newVal){
          this.query.courseId = newVal;
          this.onLoad(this.page);
        }
      }
    },

    methods: {

      closeVideo(){
        this.form.videoUrl = ""
        this.form.videoTime = ""
        this.videoName = "";
      },
      videodata(row){
        this.form.name = row.name
        this.videoName = row.name
        this.form.videoUrl = row.idVarchar;
        let vtype = row.videoType;
        if(row.videoType == "1"){
          vtype = "aliyunvod"
        }
        if(row.videoType == "2"){
          vtype = "baijiayunvod"
        }
        this.form.videoType = vtype;

        let value=row.videoDuration;
        let timeStr = null;
        let hour;
        let minute ;
        let second = 0;
        if (value <= 0) {
          return "00:00";
        } else {
          minute = value / 60;
          second = value % 60;
            if(parseInt(minute)<10){
              if(second<10){
                timeStr = "0"+parseInt(minute) + ":0" + second;
              }else {
                timeStr = "0"+parseInt(minute) + ":" + second;
              }
            }else {
              if(second<10){
                timeStr = parseInt(minute) + ":0" + second;
              }else {
                timeStr = parseInt(minute) + ":" + second;
              }
            }
        }
        this.form.videoTime = timeStr;
        this.dialogVideoVisible = false
      },
      handleClose(tag){
        this.tags.splice(0,1)
      },
      dataSubmit(selection){
        this.form.teacherId=selection.id;
        this.tags[0]= {"name":selection.name}
        this.dialogTeacherVisible = false;
        this.teaFlag = false;
      },
      handleShowTeacher(row){
        this.dialogTeacherVisible = true;
        this.teaFlag = true;
      },
      uploadBefore(file,done,loading){
        //视频为mp4格式时则可以上传
        if("video/mp4" == file.type){
          done()
        }else{
          this.$message({
            type: "error",
            message: "请上传mp4格式视频"
          });
          loading();
        }

      },
      change(value){
        console.log(value)
        this.option.column.filter(item => {
          if (item.prop === "videoUrl") {
            if(value == "aliyunvod"){
              item.label = "视频id";
              item.prop = "videoUrl";
              item.type = "input";
              item.hide = true;
            }
            if(value == "baijiayunvod"){
              item.label = "视频id";
              item.prop = "videoUrl";
              item.type = "input";
              item.hide = true;
            }
            if(value == "polyvvod"){
              item.label = "视频id";
              item.prop = "videoUrl";
              item.type = "input";
              item.hide = true;
            }
            if(value == "othervod"){
              item.label = "视频地址";
              item.type = "input";
              item.prop = "videoUrl";
              item.hide = true;
            }
          }

        });

      },
      handleEdit1(row,index) {
        this.$refs.crud.value.parentId = row.id;
        this.$refs.crud.option.column.filter(item => {
          item.editDisplay = true;
          if (item.prop === "parentId") {
            item.value = row.id;
            item.editDisplay = false;
          }
          if (item.prop === "kpointType") {
            item.value = 1;
            item.editDisplay = false;
          }
          if (item.prop === "videoUrl") {
            if(row.videoType == "aliyunvod"){
              item.label = "视频id";
              item.prop = "videoUrl";
              item.type = "input";
              item.hide = true;
            }
            if(row.videoType == "baijiayunvod"){
              item.label = "视频id";
              item.prop = "videoUrl";
              item.type = "input";
              item.hide = true;
            }
            if(row.videoType == "polyvvod"){
              item.label = "视频id";
              item.prop = "videoUrl";
              item.type = "input";
              item.hide = true;
            }
            if(row.videoType == "othervod"){
              item.label = "视频地址";
              item.type = "input";
              item.prop = "videoUrl";
              item.hide = true;
            }
          }

        });
        getUserDetail(row.teacherId).then(res => {
          if(res.data.msg != "暂无承载数据"){
            this.tags[0]= {"name":res.data.data.name}
          }
        });

        this.$refs.crud.rowEdit(row,index);
      },
      handleEdit2(row,index) {
        this.$refs.crud.value.parentId = row.id;
        this.$refs.crud.option.column.filter(item => {
          item.editDisplay = false;
          if (item.prop === "name") {
            item.editDisplay = true;
          }
          if (item.prop === "sort") {
            item.editDisplay = true;
          }
        });
        this.$refs.crud.rowEdit(row,index);
      },
      handleDel(row) {
        this.$refs.crud.rowDel(row);
      },
      handleAdd(row) {
        this.$refs.crud.value.parentId = row.id;
        this.$refs.crud.option.column.filter(item => {
          if (item.prop === "parentId") {
            item.value = row.id;
          }
          if (item.prop === "kpointType") {
            item.value = 1;
          }
        });
        this.$refs.crud.rowAdd();
      },
      openMaterial(row){
        this.kpointId = row.id;
        this.dialogDataVisible = true;
      },
      rowSave(row, loading, done) {
        let videoTimeminType1=/^([0-9][0-9][0-9]):([0-5][0-9])$/;
        let videoTimeminType2=/^([0-9][0-9]):([0-5][0-9])$/;
        let videoTime=row.videoTime;
        if(videoTime.length == 6){
          let videoTimeT=videoTimeminType1.test(videoTime);
          if(!videoTimeT){
            this.$message({
              type: "warning",
              message: "时间格式不正确，例如xxx:xx或xx:xx或0x:xx 注意：冒号为英文下的"
            });
            done();
            return;
          }
        }else if(videoTime.length == 5){
          let videoTimeT=videoTimeminType2.test(videoTime);
          if(!videoTimeT){
            this.$message({
              type: "warning",
              message: "时间格式不正确，例如xxx:xx或xx:xx或0x:xx 注意：冒号为英文下的"
            });
            done();
            return;
          }

          let videoTimeminType3=/^([0-0][0-0]):([0-0][0-0])$/;
          let defaultTime = videoTimeminType3.test(videoTime);
          if (defaultTime){
            this.$message({
              type: "warning",
              message: "视频时长不能为空"
            });
            done();
            return;
          }
        }else {
          this.$message({
            type: "warning",
            message: "时间格式不正确，例如xxx:xx或xx:xx或0x:xx 注意：冒号为英文下的"
          });
          done();
          return;
        }
        row.courseId = this.courseId;
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
        let parentId = row.parentId;
        if(parentId!=0){
          let videoTimeminType1=/^([0-9][0-9][0-9]):([0-5][0-9])$/;
          let videoTimeminType2=/^([0-9][0-9]):([0-5][0-9])$/;
          let videoTime=row.videoTime;
          if(videoTime.length == 6){
            let videoTimeT=videoTimeminType1.test(videoTime);
            if(!videoTimeT){
              this.$message({
                type: "warning",
                message: "时间格式不正确，例如xxx:xx或xx:xx或0x:xx 注意：冒号为英文下的"
              });
              done();
              return;
            }
          }else if(videoTime.length == 5){
            let videoTimeT=videoTimeminType2.test(videoTime);
            if(!videoTimeT){
              this.$message({
                type: "warning",
                message: "时间格式不正确，例如xxx:xx或xx:xx或0x:xx 注意：冒号为英文下的"
              });
              done();
              return;
            }

            let videoTimeminType3=/^([0-0][0-0]):([0-0][0-0])$/;
            let defaultTime = videoTimeminType3.test(videoTime);
            if (defaultTime){
              this.$message({
                type: "warning",
                message: "视频时长不能为空"
              });
              done();
              return;
            }
          }else {
            this.$message({
              type: "warning",
              message: "时间格式不正确，例如xxx:xx或xx:xx或0x:xx 注意：冒号为英文下的"
            });
            done();
            return;
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
        this.form.videoUrl = ""
        this.form.videoTime = ""
        this.videoName = "";
        if (["edit", "view"].includes(type)) {
          getDetail(this.form.id).then(res => {
            this.form = res.data.data;
            if (this.form.videoUrl !== '') {
              getDetailByIdVarchar(this.form.videoUrl).then(res => {
                this.videoName = res.data.data.name
                if (typeof(this.videoName) == "undefined"){
                  this.form.videoUrl = ""
                  this.form.videoTime = ""
                  this.videoName = "";
                }
              }, error => {
                this.form.videoUrl = ""
                this.form.videoTime = ""
                this.videoName = "";
              });
            }
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
          // this.page.total = data.total;
          this.data = data;
          this.loading = false;
          this.selectionClear();
        });
      },
      refreshChange() {
        this.onLoad(this.page, this.query);
      },
      showKpointDialo(){
        this.dialogFormVisible = true;
      },
      addKponint(){
        let row = {
          "courseId": this.courseId,
          "name":this.name,
          "parentId": "0",
          "sort": "0",
          "kpointType":"0"
        };
        add(row).then(() => {
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
        }, error => {
          console.log(error);
        });
        this.name = "";
        this.dialogFormVisible = false;
      }
    },
    components:{
      'teacher':teacher,
      'chooseVideosource':chooseVideosource,
      'courseMaterial':courseMaterial
    },
    created:function(){

    },
  };
</script>

<style>
</style>
