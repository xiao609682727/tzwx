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
                   icon="el-icon-upload2"
                   @click="dialogFileUploadVisible = true">上传
        </el-button>
        <el-button type="primary"
                   size="small"
                   icon="el-icon-upload2"
                   @click="syncVideo"
              v-show="false">同步数据

        </el-button>

        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   plain
                   @click="handleDelete">删 除
        </el-button>
      </template>
      <template slot-scope="scope" slot="menu">
        <el-button type="text" size="small" @click="updateStatus(scope.row.id)" v-show="false" >同步</el-button>
        <el-button type="text" size="small" @click="playVideo(scope.row)" >预览</el-button>
        <el-button type="text" size="small" @click="handleDel(scope.row)" >删除</el-button>
      </template>
    </avue-crud>
    <el-dialog title="视频上传" width="50%" :visible.sync="dialogFileUploadVisible" :close-on-click-modal="false" :close-on-press-escape="false">
      <UploadAuth @closeupload="closeupload" @failupload="failupload" v-if="dialogFileUploadVisible"  />
    </el-dialog>
    <el-dialog title="预览" width="80%" :visible.sync="dialogPlayVisible">
      <play :videosource="videosource"  v-if="dialogPlayVisible"></play>
    </el-dialog>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove, sync, getSyncById,getWebSiteList} from "@/api/edu/videosource";
  import {setStore, getStore} from '@/util/store'
  import {mapGetters} from "vuex";
  import UploadAuth from './UploadAuth'
  import play from './play'
  import {baseUrl} from '@/config/env';

  export default {
    components: {
      UploadAuth,
      play
    },
    data() {
      return {
        videosource:{},
        dialogPlayVisible:false,
        dialogFileUploadVisible:false,
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
          editBtn:false,
          selection: true,
          labelWidth:140,
          column: [
            {
              label: "id",
              prop: "id",
              addDisplay:false,
              rules: [{
                required: true,
                message: "请输入视频id",
                trigger: "blur"
              }]
            },
            {
              label: "视频名称",
              prop: "name",
              search:true,
              addDisplay:false,
              rules: [{
                required: true,
                message: "请输入视频名称",
                trigger: "blur"
              }]
            },
            {
              label: "视频原片大小",
              prop: "videoSize",
              addDisplay:false,
              rules: [{
                required: true,
                message: "请输入视频原片大小",
                trigger: "blur"
              }]
            },
            {
              label: "转码状态",
              prop: "videoStatus",
              search: true,
              addDisplay:false,
              type:"select",
              dicData:[
                {
                  label:'待转码',
                  value:'init'
                },{
                  label:'转码中',
                  value:'progress'
                },
                {
                  label:'转码成功',
                  value:'finish'
                },{
                  label:'转码失败',
                  value:'error'
                },{
                  label:'已回收',
                  value:'delete'
                }
              ],
              rules: [{
                required: true,
                message: "请输入转码状态",
                trigger: "blur"
              }]
            },
            {
              label: "转码后大小",
              prop: "videoLength",
              addDisplay:false,
              rules: [{
                required: true,
                message: "请输入转码后大小",
                trigger: "blur"
              }]
            },

            {
              label: "视频时长",
              prop: "videoDuration",
              addDisplay:false,
              formatter: function (row,value,label,column) {
                let timeStr = null;
                let hour;
                let minute ;
                let second = 0;
                if (value <= 0) {
                  return "00:00";
                } else {
                  minute = value / 60;
                  if (parseInt(minute) < 60) {
                    second = value % 60;
                    if (second > 9){
                      timeStr = parseInt(minute) > 9 ? parseInt(minute) + ":" + second : "0"+ parseInt(minute) + ":" + second;
                    }else {
                      timeStr = parseInt(minute) > 9 ? parseInt(minute) + ":0" + second : "0"+ parseInt(minute) + ":0" + second;
                    }
                  } else {
                    hour = parseInt(minute) / 60;
                    minute = minute % 60;
                    second = value - parseInt(hour) * 3600 - parseInt(minute) * 60;
                    if (second > 9){
                      timeStr = parseInt(hour) + ":"+parseInt(minute) > 9 ? parseInt(minute) + ":" + second : "0"+ parseInt(minute) + ":" + second;
                    }else {
                      timeStr = parseInt(hour) + ":"+parseInt(minute) > 9 ? parseInt(minute) + ":0" + second : "0"+ parseInt(minute) + ":0" + second;
                    }
                  }
                }
                return timeStr;
              },
              rules: [{
                required: true,
                message: "请输入视频时长（s）",
                trigger: "blur"
              }]
            },
            {
              label: "封面图片地址",
              type:"img",
              dataType:"string",
              prop: "imageUrl",
              addDisplay:false,
              rules: [{
                required: true,
                message: "请输入封面图片地址",
                trigger: "blur"
              }]
            },
            /*{
              label: "播放文件类型",
              prop: "fileType",
              addDisplay:false,
              dicData:[
                {
                  label:'视频',
                  value:1
                },{
                  label:'音频',
                  value:2
                }
              ],
              rules: [{
                required: true,
                message: "请输入播放文件类型 1：视频 2：音频",
                trigger: "blur"
              }]
            },
            {
              label: "源文件类型",
              prop: "initType",
              addDisplay:false,
              dicData:[
                {
                  label:'视频',
                  value:1
                },{
                  label:'音频',
                  value:2
                }
              ],
              rules: [{
                required: true,
                message: "请输入源文件类型 1：视频 2：音频",
                trigger: "blur"
              }]
            },*/
            {
              label: "视频类型",
              prop: "videoType",
              type:"select",
              value:"1",
              span:24,
              search:true,
              dicData:[
                {
                  label:'阿里云',
                  value:'1'
                },
                {
                  label:'视频云',
                  value:'2'
                },
                {
                  label:'本地上传',
                  value:'3'
                }
              ],
              rules: [{
                required: true,
                message: "请输入视频类型(ninetySixkoo96刻....)",
                trigger: "blur"
              }]
            },
            {
              label: "视频id",
              prop: "idVarchar",
              search: true,
              span:24,
              rules: [{
                required: true,
                message: "请输入视频播放码",
                trigger: "blur"
              }]
            },
            {
              label: "创建时间",
              prop: "createTime",
              addDisplay:false,
              rules: [{
                required: true,
                message: "请输入添加时间",
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
          addBtn: this.vaildData(this.permission.videosource_add, false),
          viewBtn: this.vaildData(this.permission.videosource_view, false),
          delBtn: this.vaildData(this.permission.videosource_delete, false),
          editBtn: this.vaildData(this.permission.videosource_edit, false)
        };
      },
      ids() {
        let ids = [];
        this.selectionList.forEach(ele => {
          ids.push(ele.id);
        });
        return ids.join(",");
      },
    },
    mounted:function() {
      this.init()
    },
    methods: {
      init(){
        let params = {
          dataKey: "openFlag"
        }
        getWebSiteList(params).then(res=>{
          let arr = []
          res.data.data.forEach(ele => {
            if(ele.configType == "aliyun_vod"&& ele.dataValue == "1"){
              arr.push({
                label:'阿里云',
                value:'1'
              })
            }
            if(ele.configType == "baijiayun_vod"&& ele.dataValue == "1"){
              arr.push({
                label:'视频云',
                value:'2'
              })
            }
            if(ele.configType == "local_vod"){
              arr.push({
                label:'本地上传',
                value:'3'
              })
            }
          });
          setStore({name: 'yunconfig', content: arr, type: 'session'})

        })
      },
      playVideo(row){
        this.videosource = row
        this.dialogPlayVisible = true
      },
      handleDel(row){
        this.$refs.crud.rowDel(row);
      },
      syncVideo(){
        sync().then(res=> {
          this.$message({
            type: "success",
            message: "开始同步，请耐心等待稍后尝试刷新！"
          });
        });
      },
      updateStatus(id){
        getSyncById(id).then(res=>{
          this.$message({
            type: "success",
            message: "已同步!"
          });
          this.onLoad(this.page);
        });
      },
      closeupload(){
        this.dialogFileUploadVisible = false;
        this.$message({
          type: "success",
          message: "上传成功!"
        });
        this.onLoad(this.page);
      },
      failupload(msg){
        this.dialogFileUploadVisible = false;
        this.$message({
          type: "error",
          message: msg
        });
      },
      rowSave(row, done, loading) {
        row.imageUrl = "";
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
      refreshChange() {
        this.onLoad(this.page, this.query);
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
    }
  };
</script>

  <style>

  </style>
