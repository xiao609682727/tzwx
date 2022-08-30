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
          @close="closeClassRoom"
          closable>
          {{roomName}}
        </el-tag>
        <el-button type="primary" v-if="form.videoUrl == ''" @click="dialogVideoVisible = true">选择直播房间</el-button>
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
          icon="el-icon-circle-plus-outline"
          size="small"
          v-if="scope.row.videoType=='baijiayunlive'"
          @click.stop="openDialog2(scope.row)"
        >进入直播间
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
      <template slot-scope="scope" slot="joinRoomForm">
        <el-row type="flex">
          <el-col :span="4"><el-button type="primary" @click="joinRoom()">加入直播间</el-button></el-col>
        </el-row>
      </template>
      <template slot-scope="scope" slot="downloadForm">
        <el-row type="flex">
          <el-col :span="4"><el-link href="https://cdn-fastly.obsproject.com/downloads/OBS-Studio-25.0.4-Full-Installer-x64.exe" target="_blank"><el-button type="primary">windows</el-button></el-link></el-col>
          <el-col :span="4"><el-link href="https://cdn-fastly.obsproject.com/downloads/obs-mac-25.0.7.dmg" target="_blank"><el-button type="primary">macOS 10.13+</el-button></el-link></el-col>
        </el-row>
      </template>
     <!-- <template slot-scope="scope" slot="baijiayunteadownloadForm">
        <el-row type="flex">
          <el-col :span="4"><el-button @click.stop="joinRoom(scope.row,'teacher','web')"  type="primary">进入网页端</el-button></el-col>
          <el-col :span="4"><el-button @click.stop="joinRoom(scope.row,'teacher','app')" type="primary">进入客户端</el-button></el-col>
          <el-col :span="4"><el-link href="https://www.baijiayun.com/classroomdown/" target="_blank"><el-button type="primary">下载客户端</el-button></el-link></el-col>
        </el-row>
      </template>
      <template slot-scope="scope" slot="baijiayunadmindownloadForm">
        <el-row type="flex">
          <el-col :span="4"><el-button @click.stop="joinRoom(scope.row,'admin','web')" type="primary">进入网页端</el-button></el-col>
          <el-col :span="4"><el-button @click.stop="joinRoom(scope.row,'admin','app')" type="primary">进入客户端</el-button></el-col>
          <el-col :span="4"><el-link href="https://www.baijiayun.com/classroomdown/" target="_blank"><el-button type="primary">下载客户端</el-button></el-link></el-col>
        </el-row>
      </template>-->
      <template slot-scope="scope" slot="createUrlForm">
        <el-row type="flex">
          <el-col :span="4"><el-button type="primary" @click="createUrl()">生成地址</el-button></el-col>
        </el-row>
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
    <el-dialog title="房间列表" width="80%" :visible.sync="dialogVideoVisible" append-to-body>
      <chooseLiveRoom v-if="dialogVideoVisible" @videodata="videodata"></chooseLiveRoom>
    </el-dialog>
    <el-dialog title="讲师进入教室" :visible.sync="dialogVideoVisible1" append-to-body>
      <el-row type="flex" justify="center">
        <el-col :span="24" style="text-align: center;font-size: 20px;">如果未安装或不能打开客户端，请下载最新客户端，客户端观看直播更流畅，功能更强大</el-col>
      </el-row>
      <el-row type="flex" justify="center" style="margin-top: 50px;">
        <el-col :span="4"><el-button type="primary" @click.native="joinRoom(row,'teacher','web')">网页端</el-button></el-col>
        <el-col :span="4"><el-button type="primary" @click.native="joinRoom(row,'teacher','app')">客户端</el-button></el-col>
      </el-row>
    </el-dialog>
    <el-dialog title="进入直播间" :visible.sync="dialogVideoVisible2" append-to-body>
      <el-tabs type="border-card">
        <el-tab-pane label="讲师">
          <el-row type="flex" justify="center">
            <el-col :span="24" style="text-align: center;font-size: 20px;">如果未安装或不能打开客户端，请下载最新客户端，客户端观看直播更流畅，功能更强大</el-col>
          </el-row>
          <el-row type="flex" justify="center" style="margin-top: 50px;">
            <el-col :span="6"><el-button type="primary" @click.native="joinRoom(row,'teacher','web')">讲师网页端</el-button></el-col>
            <el-col :span="6"><el-button type="primary" @click.native="joinRoom(row,'teacher','app')">讲师客户端</el-button></el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane label="助教">
          <el-row type="flex" justify="center">
            <el-col :span="24" style="text-align: center;font-size: 20px;">如果未安装或不能打开客户端，请下载最新客户端，客户端观看直播更流畅，功能更强大</el-col>
          </el-row>
          <el-row type="flex" justify="center" style="margin-top: 50px;">
            <el-col :span="6"><el-button type="primary" @click.native="joinRoom(row,'admin','web')">助教网页端</el-button></el-col>
            <el-col :span="6"><el-button type="primary" @click.native="joinRoom(row,'admin','app')">助教客户端</el-button></el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane label="下载客户端">
          <el-row type="flex" justify="center">
            <el-col :span="24" style="text-align: center;font-size: 20px;">请下载最新客户端</el-col>
          </el-row>
          <el-row type="flex" justify="center" style="margin-top: 20px">
            <el-col :span="12" style="text-align: center;font-size: 20px;"><el-link href="https://www.baijiayun.com/default/home/liveclientDownload?type=mac&partner_id=20000000" target="_blank"><el-button type="primary" >mac</el-button></el-link></el-col>
            <el-col :span="12" style="text-align: center;font-size: 20px;"><el-link href="https://www.baijiayun.com/default/home/liveclientDownload?type=windows&partner_id=20000000" target="_blank"><el-button type="primary" >windows</el-button></el-link></el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove, getLiveSupplier,getUserDetail,createUrl,getRoomDetail} from "@/api/edu/coursekpoint";
  import {mapGetters} from "vuex";
  import teacher from "./teacher";
  import chooseLiveRoom from "./chooseLiveRoom";

  let supplierList = [/*{
          dictValue:"阿里云直播",
          dictKey:"aliyunlive"
        },{
          dictValue:"阿里云回放",
          dictKey:"aliyunReplay"
        },*/{
          dictValue:"视频云直播",
          dictKey:"baijiayunlive"
        },
        {
          dictValue:"iframe直播",
          dictKey:"iframelive",
        }/*,{
          dictValue:"百家云回放",
          dictKey:"baijiayunReplay"
        },{
          dictValue:"保利威直播",
          dictKey:"polyvlive"
        },{
          dictValue:"保利威回放",
          dictKey:"polyvReplay"
        }*/,{
    dictValue:"其他",
    dictKey:"otherlive"
  }]

  export default {
    data() {
      return {
        dialogVideoVisible2: false,
        dialogVideoVisible1: false,
        dialogVideoVisible:false,
        teaFlag: false,
        tags:[],
        roomName:"",
        supplierList: supplierList,
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
              label: "选择直播房间",
              prop: "chooseVideo",
              formslot:true,
              display: false,
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
              label: "视频云",
              prop: "videoType",
              hide: false,
              display: false,
              span: 24,
              value: "baijiayunlive",
              dataType: "string",
              formslot: true,
              formatter: function (row,value,label,column) {
                let str = ""
                supplierList.forEach((e, index) =>{
                  if(e.dictKey == value){
                    str = e.dictValue
                  }
                })
                return str
              },
              rules: [{
                required: true,
                message: "请选择直播供应商",
                trigger: "blur"
              }]
            },
            {
              label: "直播开始时间",
              prop: "liveBeginTime",
              type: "datetime",
              format: "yyyy-MM-dd HH:mm:ss",
              valueFormat: "yyyy-MM-dd HH:mm:ss",
              hide:false,
              rules: [{
                required: true,
                message: "请输入直播开始时间",
                trigger: "blur"
              }]
            },
            {
              label: "直播结束时间",
              prop: "liveEndTime",
              type: "datetime",
              format: "yyyy-MM-dd HH:mm:ss",
              valueFormat: "yyyy-MM-dd HH:mm:ss",
              hide:false,
              rules: [{
                required: true,
                message: "请输入直播结束时间",
                trigger: "blur"
              }]
            },
            {
              label: "直播地址",
              prop: "videoUrl",
              span: 24,
              hide: true,
              display: false,
            },
            {
              label: "直播推流地址",
              prop: "replayUrl",
              addDisplay: false,
              editDisplay: false,
              span: 24,
              hide: true,
            },
            {
              label: "生成地址",
              prop: "createUrl",
              addDisplay: false,
              editDisplay: false,
              hide: true,
              span: 24,
              formslot: true,
            },
            {
              label: "推流工具下载",
              prop: "download",
              addDisplay: false,
              editDisplay: false,
              hide: true,
              span: 24,
              formslot: true,
            },
            /*{
              label: "讲师进入直播间",
              prop: "baijiayunteadownload",
              addDisplay: false,
              editDisplay: false,
              hide: true,
              span: 24,
              formslot: true,
            },
            {
              label: "助教进入直播间",
              prop: "baijiayunadmindownload",
              addDisplay: false,
              editDisplay: false,
              hide: true,
              span: 24,
              formslot: true,
            },*/
            {
              label: "自动回放",
              prop: "openReplay",
              hide: true,
              type:"switch",
              span: 24,
              addDisplay: false,
              editDisplay: false,
              value:2,
              dicData: [{
                label: '关',
                value: 2
              }, {
                label: '开',
                value: 1
              }],
              // formslot: true,
            },
            {
              label: "学员端打开方式",
              prop: "openType",
              type:"select",
              span: 24,
              hide: true,
              addDisplay: false,
              editDisplay: false,
              value: 'WEB',
              dicData:[
                {
                  label:'WEB网页',
                  value:'WEB'
                },{
                  label:'客户端',
                  value:'APP'
                }]
            },
            // {
            //   label: "直播间",
            //   prop: "joinRoom",
            //   hide: true,
            //   span: 24,
            //   formslot: true,
            // },
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
              label: "显示排序",
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
      openDialog1(row){
        this.row = row
        this.dialogVideoVisible1 = true
      },
      openDialog2(row){
        this.row = row
        this.dialogVideoVisible2 = true
      },
      closeClassRoom(){
        this.form.videoUrl = ""
        this.roomName = "";
      },
      videodata(row){
        console.log(row)
        this.form.name = row.name;
        this.form.liveBeginTime = row.startTime;
        this.form.liveEndTime = row.endTime;
        if(row.liveType==1){
          this.form.videoType = "baijiayunlive";
        }else {
          this.form.videoType = "iframelive";
          }
        this.form.videoUrl = row.classroomId
        this.roomName = row.name
        this.dialogVideoVisible = false
        this.$refs.crud.option.column.filter(item => {
          if(row.liveType!=1){
             if (item.prop == "openType") {
              item.addDisplay = false;
              }
          }
        })
      },
      createUrl(){
        var streamName = Number(Date.now() + Math.random().toString().substr(3,length)).toString(36)
        createUrl(streamName).then(res=>{
          this.form.videoUrl = res.data.data.livePullUrl;
          this.form.replayUrl = res.data.data.livePushUrl;
        })
      },
      download(type){
        if(type == "mac"){
          window.open('https://www.baijiayun.com/default/home/liveclientDownload?type=mac&partner_id=20000000');
        }else{
          window.open('https://www.baijiayun.com/default/home/liveclientDownload?type=windows&partner_id=20000000');
        }
      },
      joinRoom(row,userType,type){
        getDetail(row.id,userType).then(res => {
          if(type == "web"){
            window.open(res.data.data.params.url);
          }else{
            window.open(res.data.data.params.clienturl);
          }
        });
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
        this.option.column.filter(item => {
          if(item.prop === "openReplay"){
            if(value == "baijiayunlive"){
              item.addDisplay = true;
              item.editDisplay = true;
            }else{
              item.addDisplay = false;
              item.editDisplay = false;
            }
          }
          if(item.prop === "createUrl"){
            if(value == "aliyunlive"){
              item.addDisplay = true;
              item.editDisplay = true;
            }else{
              item.addDisplay = false;
              item.editDisplay = false;
            }
          }
         // if(item.prop === "joinRoom"){
         //    if(value == "baijiayunlive"){
         //      var joinRoom = this.findObject(this.option.column,'joinRoom')
         //      item.label= "进入直播间";
         //      item.addDisplay = true;
         //      item.editDisplay = true;
         //    }else{
         //      item.addDisplay = false;
         //      item.editDisplay = false;
         //    }
         //  }
          if(item.prop === "chooseVideo"){
            if(value == "baijiayunlive"){
              item.hide = true;
              item.display = true;
            }else{
              item.hide = true;
              item.display = false;
            }
          }
          if (item.prop === "videoUrl") {
            item.rules=[]
            if(value == "aliyunlive"){
              item.label = "视频直播地址";
              item.prop = "videoUrl";
              item.type = "input";
              item.hide = true;
              item.display = true;
            }
            if(value == "aliyunReplay" ){
              item.label = "直播录制视频id";
              item.type = "input";
              item.prop = "videoUrl";
              item.hide = true;
              item.display = true;
            }
            if(value == "baijiayunlive"){
              item.label = "直播房间id";
              item.type = "input";
              item.prop = "videoUrl";
              item.hide = true;
              item.display = false;
            }
            if(value == "baijiayunReplay"  ){
              item.label = "直播房间id";
              item.type = "input";
              item.prop = "videoUrl";
              item.hide = true;
              item.display = true;
              item.rules=[{
                required: true,
                message: "直播房间id",
                trigger: "blur"
              }]
            }
            if(value == "polyvlive" ||value == "otherlive" ){
              item.label = "url地址";
              item.type = "input";
              item.prop = "videoUrl";
              item.hide = true;
              item.display = true;
              item.rules=[{
                required: true,
                message: "请输入url地址",
                trigger: "blur"
              }]
            }
            if(value == "polyvReplay" ){
              item.label = "点播视频id";
              item.type = "input";
              item.prop = "videoUrl";
              item.hide = true;
              item.display = true;
            }
          }
          if (item.prop === "replayUrl") {
            if(value == "aliyunlive"){
              item.addDisplay = true;
              item.editDisplay = true;
            }else{
              item.addDisplay = false;
              item.editDisplay = false;
            }
          }
          if (item.prop === "download") {
            if(value == "aliyunlive"){
              item.addDisplay = true;
              item.editDisplay = true;
            }else{
              item.addDisplay = false;
              item.editDisplay = false;
            }
          }
          if (item.prop === "baijiayunteadownload") {
            if(value == "baijiayunlive"){
              item.addDisplay = true;
              item.editDisplay = true;
            }else{
              item.addDisplay = false;
              item.editDisplay = false;
            }
          }
          if (item.prop === "baijiayunadmindownload") {
            if(value == "baijiayunlive"){
              item.addDisplay = true;
              item.editDisplay = true;
            }else{
              item.addDisplay = false;
              item.editDisplay = false;
            }
          }
          if (item.prop === "openType") {
            if(value == "baijiayunlive"){
              item.addDisplay = true;
              item.editDisplay = true;
            }else{
              item.addDisplay = false;
              item.editDisplay = false;
            }
          }

        });
        if (this.form.videoUrl !== '' && value == "baijiayunlive") {
          getRoomDetail(this.form.videoUrl).then(res=>{
            this.roomName = res.data.data.name
            if (typeof(this.roomName) == "undefined"){
              this.form.videoUrl = ''
              this.roomName = "";
            }
          }, error => {
            this.form.videoUrl = ''
            this.roomName = "";
          })
        }
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
          if(item.prop === "openReplay"){
            if(row.videoType == "baijiayunlive"||row.videoType == "iframelive"){
              item.addDisplay = true;
              item.editDisplay = true;
            }else{
              item.addDisplay = false;
              item.editDisplay = false;
            }
          }
          if (item.prop === "createUrl") {
            if(row.videoType == "aliyunlive"){
              item.editDisplay = true;
            }else{
              item.editDisplay = false;
            }
          }
         // if (item.prop === "joinRoom") {
         //    if(row.videoType == "baijiayunlive"){
         //      item.editDisplay = true;
         //    }else{
         //      item.editDisplay = false;
         //    }
         //  }
          if(item.prop === "chooseVideo"){
            if(row.videoType == "baijiayunlive"||row.videoType == "iframelive"){
              item.hide = true;
              item.display = true;
            }else{
              item.hide = true;
              item.display = false;
            }
          }
          if (item.prop === "videoUrl") {
            item.rules=[]
            if(row.videoType == "aliyunlive"){
              item.label = "直播地址";
              item.prop = "videoUrl";
              item.type = "input";
              item.hide = true;
              item.addDisplay = true;
              item.editDisplay = true;
              item.display = true;
            }
            if(row.videoType == "aliyunReplay"){
              item.label = "直播录制视频id";
              item.type = "input";
              item.prop = "videoUrl";
              item.hide = true;
              item.addDisplay = true;
              item.editDisplay = true;
              item.display = true;
            }
            if(row.videoType == "baijiayunlive"||row.videoType == "iframelive"){

              item.label = "直播房间id";
              item.type = "input";
              item.prop = "videoUrl";
              item.hide = true;
              item.addDisplay = false;
              item.editDisplay = false;
              item.display = false;
            }
            if(row.videoType == "baijiayunReplay"  ){
              item.label = "直播房间id";
              item.type = "input";
              item.prop = "videoUrl";
              item.hide = true;
              item.addDisplay = true;
              item.editDisplay = true;
              item.display = true;
              item.rules=[{
                required: true,
                message: "直播房间id",
                trigger: "blur"
              }]
            }
            if(row.videoType == "polyvlive"||row.videoType == "otherlive" ){
              item.label = "url地址";
              item.type = "input";
              item.prop = "videoUrl";
              item.hide = true;
              item.addDisplay = true;
              item.editDisplay = true;
              item.display = true;
              item.rules=[{
                required: true,
                message: "url地址",
                trigger: "blur"
              }]
            }
            if(row.videoType == "polyvReplay" ){
              item.label = "点播视频id";
              item.type = "input";
              item.prop = "videoUrl";
              item.hide = true;
              item.addDisplay = true;
              item.editDisplay = true;
              item.display = true;
            }
          }
          if (item.prop === "replayUrl") {
            if(row.videoType == "aliyunlive"){
              item.addDisplay = true;
              item.editDisplay = true;
            }else{
              item.addDisplay = false;
              item.editDisplay = false;
            }
          }
          if (item.prop === "download") {
            if(row.videoType == "aliyunlive"){
              item.addDisplay = true;
              item.editDisplay = true;
            }else{
              item.addDisplay = false;
              item.editDisplay = false;
            }
          }
          if (item.prop === "baijiayunteadownload") {
            if(row.videoType == "baijiayunlive"||row.videoType == "iframelive"){
              item.addDisplay = true;
              item.editDisplay = true;
            }else{
              item.addDisplay = false;
              item.editDisplay = false;
            }
          }
          if (item.prop === "baijiayunadmindownload") {
            if(row.videoType == "baijiayunlive"||row.videoType == "iframelive"){
              item.addDisplay = true;
              item.editDisplay = true;
            }else{
              item.addDisplay = false;
              item.editDisplay = false;
            }
          }
          if (item.prop === "openType") {
            if(row.videoType == "baijiayunlive"){
              item.addDisplay = true;
              item.editDisplay = true;
            }else{
              item.addDisplay = false;
              item.editDisplay = false;
            }
          }
        });
        getUserDetail(row.teacherId).then(res => {
          if(res.data.msg != "暂无承载数据"){
            this.tags[0]= {"name":res.data.data.name}
          }
        });

        console.log(this.option.column)
        this.$refs.crud.rowEdit(row,index);
      },
      handleEdit2(row,index) {
        this.$refs.crud.value.parentId = row.id;
        this.$refs.crud.option.column.filter(item => {
          item.editDisplay = false;
          if(item.prop === "openReplay"){
            if(row.videoType == "baijiayunlive"){
              item.addDisplay = true;
              item.editDisplay = true;
            }else{
              item.addDisplay = false;
              item.editDisplay = false;
            }
          }
          if(item.prop === "chooseVideo"){
            if(row.videoType == "baijiayunlive"){
              item.hide = true;
              item.display = true;
            }else{
              item.hide = true;
              item.display = false;
            }
          }
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
        this.$refs.crud.value.kpointType = 1;
        this.$refs.crud.option.column.filter(item => {
          if (item.prop === "parentId") {
            item.value = row.id;
          }
          if (item.prop === "kpointType") {
            item.value = 1;
          }
/*          if (item.prop === "joinRoom") {
            if(row.videoType == "baijiayunlive"){
              item.editDisplay = true;
            }
            if(row.videoType == "aliyunReplay" ||row.videoType == "aliyunlive" ||row.videoType == "baijiayunReplay"){
              item.addDisplay = false;
              item.editDisplay = false;
            }
          }*/
          if(item.prop === "openReplay"){
            // if(row.videoType == "baijiayunlive"){
              item.addDisplay = true;
              item.editDisplay = true;
            // }else{
            //   item.addDisplay = false;
            //   item.editDisplay = false;
            // }
          }
          if(item.prop === "chooseVideo"){
            // if(row.videoType == "baijiayunlive"){
              item.hide = true;
              item.display = true;
            // }else{
            //   item.hide = true;
            //   item.display = false;
            // }
          }
          if (item.prop === "videoUrl") {
            if(row.videoType == "1"){
              item.label = "视频直播地址";
              item.prop = "videoUrl";
              item.type = "input";
              item.hide = true;
            }
            if(row.videoType == "2"){
              item.label = "直播录制视频id";
              item.type = "input";
              item.prop = "videoUrl";
              item.hide = true;
            }
          }
          if (item.prop === "replayUrl") {
            if(row.videoType == "1"){
              item.addDisplay = true;
              item.editDisplay = true;
            }
            if(row.videoType == "2"){
              item.addDisplay = false;
              item.editDisplay = false;
            }
          }
          if (item.prop === "download") {
            if(row.videoType == "1"){
              item.addDisplay = true;
              item.editDisplay = true;
            }
            if(row.videoType == "2"){
              item.addDisplay = false;
              item.editDisplay = false;
            }
          }
          if (item.prop === "baijiayunteadownload") {
            // if(row.videoType == "baijiayunlive"){
              item.addDisplay = true;
              item.editDisplay = true;
            // }else{
            //   item.addDisplay = false;
            //   item.editDisplay = false;
            // }
          }
          if (item.prop === "baijiayunadmindownload") {
            // if(row.videoType == "baijiayunlive"){
              item.addDisplay = true;
              item.editDisplay = true;
            // }else{
            //   item.addDisplay = false;
            //   item.editDisplay = false;
            // }
          }
          if (item.prop === "openType") {
            // if(row.videoType == "baijiayunlive"){
              item.addDisplay = true;
              item.editDisplay = true;
            // }else{
            //   item.addDisplay = false;
            //   item.editDisplay = false;
            // }
          }
        });
        this.$refs.crud.rowAdd();
      },
      rowSave(row, loading, done) {
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
        this.form.videoUrl = ''
        this.roomName = "";
        if (["edit", "view"].includes(type)) {
          getDetail(this.form.id).then(res => {
            this.form = res.data.data;
            console.log(this.form.videoUrl)
            if (this.form.videoUrl !== '') {
              if(this.form.videoType == "baijiayunlive"){
                getRoomDetail(this.form.videoUrl).then(res=>{
                  this.roomName = res.data.data.name
                  if (typeof(this.roomName) == "undefined"){
                    this.form.videoUrl = ''
                    this.roomName = "";
                  }
                }, error => {
                  this.form.videoUrl = ''
                  this.roomName = "";
                })
              }
              if(this.form.videoType == "iframelive"){
                this.roomName = this.form.name
              }
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
      'chooseLiveRoom':chooseLiveRoom
    },
    created:function(){

    },
  };
</script>

<style>
</style>
