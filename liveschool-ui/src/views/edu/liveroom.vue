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
        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   plain
                   v-if="permission.liveroom_delete"
                   @click="handleDelete">删 除
        </el-button>
      </template>
      <template slot-scope="scope"
                slot="menu">
        <el-button  type="primary" v-if="scope.row.liveType==2"  size="small" @click="openUrl(scope.row)" >预览</el-button>
        <el-dropdown v-if="scope.row.liveType==1" size="small">
          <el-button type="primary" size="small">
            菜单<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="openBack(scope.row)" >回放</el-dropdown-item>
            <el-dropdown-item @click.native="openDialog2(scope.row)" >进入直播间</el-dropdown-item>
            <el-dropdown-item @click.native="shareCourse(scope.row)" >分享链接</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </template>
      <template slot-scope="scope" slot="liveTypeForm">
        <el-select v-model="scope.row.liveType" @change="change" placeholder="请选择" >
          <el-option
            v-for="item in supplierList"
            :key="item.dictKey"
            :label="item.dictValue"
            :value="item.dictKey">
          </el-option>
        </el-select>
      </template>
    </avue-crud>
    <el-dialog title="回放列表" width="80%" :visible.sync="dialogVideoVisible" append-to-body>
      <liveplayback v-if="dialogVideoVisible" :liveRoomId="liveRoomId" @videodata="videodata"></liveplayback>
    </el-dialog>
    <el-dialog title="讲师进入教室" :visible.sync="dialogVideoVisible1" append-to-body>

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
    <el-dialog title="分享链接" :visible.sync="shareCourseLink" append-to-body>
      <shareCourse :linkInfo="linkInfo"></shareCourse>
    </el-dialog>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove} from "@/api/edu/liveroom";
  import {mapGetters} from "vuex";
  import liveplayback from "./liveplayback";
  import shareCourse from "./shareCourse";

  export default {
    data() {
      return {
        liveRoomId:"",
        linkInfo: {},
        supplierList:[{
          dictValue:"大班课",
          dictKey:"1"
        },{
          dictValue:"iframe",
          dictKey:"2"
        }],
        shareCourseLink: false,
        dialogVideoVisible2: false,
        dialogVideoVisible1: false,
        dialogVideoVisible: false,
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
          align: "center",
          calcHeight: 80,
          searchShow: true,
          searchMenuSpan: 6,
          tip: false,
          border: true,
          index: false,
          menuType:'menu',
          viewBtn: false,
          labelWidth: 140,
          refreshBtn: true,
          selection: true,
          column: [
            {
              label: "教室名称",
              prop: "name",
              search:true,

              span: 24,
              rules: [{
                required: true,
                message: "请输入房间名称",
                trigger: "blur"
              }]
            },
            {
              label: "类型",
              prop: "liveType",
              type:"select",
              editDetail: true,
              value: "1",
              span: 24,
              search:true,
              dicData:[
                {
                  label:'大班课',
                  value:'1'
                },
                {
                  label:'iframe',
                  value:'2'
                }
              ],
              formslot: true,
            },
            {
              label: "教室地址",
              prop: "classroomId",
              search:true,
              display: false,
              overHidden: true,
              span: 24,
              rules: [{
                required: true,
                message: "请输入教室id",
                trigger: "blur"
              }]
            },
            // {
            //   label: "类型",
            //   prop: "roomType",
            //   display: false,
            //   value: "2",
            //   span: 24,
            //   dicData:[
            //     {
            //       label:'大班课',
            //       value:'2'
            //     }
            //   ],
            //   rules: [{
            //     required: true,
            //     message: "请输入房间类型2 大班课",
            //     trigger: "blur"
            //   }]
            // },

            {
              label: "回放状态",
              prop: "playbackStatus",
              display: false,
              value: "1",
              span: 24,
              dicData:[
                {
                  label:'未录制',
                  value:'1'
                },
                {
                  label:'已录制',
                  value:'2'
                }
              ],
            },
            {
              label: "开始时间",
              prop: "startTime",
              type: "datetime",
              format:"yyyy-MM-dd HH:mm:ss",
              valueFormat:"yyyy-MM-dd HH:mm:ss",
              span: 24,
              rules: [{
                required: true,
                message: "请输入开始时间",
                trigger: "blur"
              }]
            },
            {
              label: "结束时间",
              prop: "endTime",
              type: "datetime",
              format:"yyyy-MM-dd HH:mm:ss",
              valueFormat:"yyyy-MM-dd HH:mm:ss",
              span: 24,
              rules: [{
                required: true,
                message: "请输入结束时间",
                trigger: "blur"
              }]
            },
            {
              label: "自动录制",
              prop: "autoPlaybackRecord",
              type:"radio",
              editDetail: true,
              value: "1",
              span: 24,
              search:false,
              hide:true,
              dicData:[
                {
                  label:'开',
                  value:'1'
                },
                {
                  label:'关',
                  value:'2'
                }
              ],
            },
            {
              label: "PC&APP模板属性",
              prop: "isVideoMain",
              type:"radio",
              editDetail: true,
              value: "2",
              span: 24,
              search:false,
              hide:true,
              dicData:[
                {
                  label:'以PPT为主',
                  value:'2'
                },
                {
                  label:'以视频为主',
                  value:'1'
                }
              ],
            },
            {
              label: "手机网页模板属性",
              prop: "mobileIsVideoMain",
              type:"radio",
              editDetail: true,
              value: "1",
              span: 24,
              search:false,
              hide:true,
              dicData:[
                {
                  label:'以视频为主',
                  value:'1'
                },
                {
                  label:'以PPT为主',
                  value:'2'
                }
              ],
            },
            {
              label: "超时强制关闭教室",
              prop: "endDelayTime",
              type:"select",
              editDetail: true,
              value: "0",
              span: 24,
              search:false,
              hide:true,
              dicData:[
                {
                  label:'不限制',
                  value:'0'
                },
                {
                  label:'拖堂5分钟',
                  value:'300'
                },
                {
                  label:'拖堂10分钟',
                  value:'600'
                },
                {
                  label:'拖堂15分钟',
                  value:'900'
                },
                {
                  label:'拖堂20分钟',
                  value:'1200'
                },
                {
                  label:'拖堂30分钟',
                  value:'1800'
                }
              ],
            }
           /* {
              label: "课程状态",
              prop: "roomStatus",
              display: false,
              value: "1",
              rules: [{
                required: true,
                message: "请输入课程状态",
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
          addBtn: this.vaildData(this.permission.liveroom_add, false),
          viewBtn: this.vaildData(this.permission.liveroom_view, false),
          delBtn: this.vaildData(this.permission.liveroom_delete, false),
          editBtn: this.vaildData(this.permission.liveroom_edit, false)
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
          //大班课
          if(value == "1"){//大班课
            if(item.prop === "classroomId"){
              item.display = false;
            }
            if(item.prop === "autoPlaybackRecord"){
              item.display = true;
            }if(item.prop === "isVideoMain"){
              item.display = true;
            }if(item.prop === "mobileIsVideoMain"){
              item.display = true;
            }if(item.prop === "endDelayTime"){
              item.display = true;
            }

          }else if(value == "2"){//iframe

            if(item.label === "教室地址"){
              item.label = "url地址";
            }
            if(item.prop === "classroomId"){
              item.display = true;
            }
            if(item.prop === "autoPlaybackRecord"){
              item.display = false;
            }
            if(item.prop === "isVideoMain"){
              item.display = false;
            }
            if(item.prop === "mobileIsVideoMain"){
              item.display = false;
            }
            if(item.prop === "endDelayTime"){
              item.display = false;
            }

          }
        });
      },
      openDialog1(row){
        this.row = row
        this.dialogVideoVisible1 = true
      },
      openDialog2(row){
        this.row = row
        this.dialogVideoVisible2 = true
      },
      shareCourse(row){
        this.row = row
        this.shareCourseLink = true
        this.linkInfo = {
          "roomId":row.classroomId,
          "teacherCode":row.teacherCode ,
          "tutorCode":row.tutorCode ,
          "studentCode":row.studentCode
        }
      },
      openBack(row){
        this.dialogVideoVisible = true
        this.liveRoomId = row.classroomId
      },
      openUrl(row){
        window.open(row.classroomId);
      },
      joinRoom(row,userType,type){
        getDetail(row.id).then(res => {
          if(userType == "teacher"){
            if(type == "web"){
              window.open(res.data.data.params.teacherWebUrl);
            }else{
              window.open(res.data.data.params.teacherAppUrl);
            }
          }
          if(userType == "admin"){
            if(type == "web"){
              window.open(res.data.data.params.adminWebUrl);
            }else{
              window.open(res.data.data.params.adminAppUrl);
            }
          }
        });
      },
      rowSave(row, done, loading) {
        row.roomType = "2"
        row.playbackStatus = 1
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
        this.change(1);
        if (["edit", "view"].includes(type)) {
          getDetail(this.form.id).then(res => {
            if(res.data.data.liveType==2){
              this.change(2);
            }
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
        this.page.currentPage = 1;
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
      'liveplayback':liveplayback,
      'shareCourse':shareCourse,
    },
  };
</script>

<style>
</style>
