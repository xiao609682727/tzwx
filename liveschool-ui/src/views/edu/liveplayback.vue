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
        <!--<el-button
                   size="small"
                   plain
                   @click="sync">同步
        </el-button>-->
      </template>
      <template slot-scope="scope" slot="menu">
        <el-button
          type="text"
          icon="el-icon-video-play"
          size="small"
          @click.stop="playback(scope.row)"
        >回放播放
        </el-button>
      </template>
    </avue-crud>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove, sync, getUrl} from "@/api/edu/liveplayback";
  import {mapGetters} from "vuex";

  export default {
    data() {
      return {
        form: {},
        query: {liveRoomId:this.liveRoomId},
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
          viewBtn: false,
          editBtn: false,
          addBtn: false,
          refreshBtn: false,
          selection: true,
          column: [
            {
              label: "房间号",
              prop: "liveRoomId",
              rules: [{
                required: true,
                message: "请输入房间号",
                trigger: "blur"
              }]
            },
            {
              label: "回放名称",
              prop: "name",
              rules: [{
                required: true,
                message: "请输入回放名称",
                trigger: "blur"
              }]
            },
            {
              label: "视频ID",
              prop: "videoId",
              rules: [{
                required: true,
                message: "请输入视频id",
                trigger: "blur"
              }]
            },
            {
              label: "视频时长",
              prop: "videoDuration",
              formatter: function (row,value) {
                let minute = value / 60;
                let second = value % 60;
                let timeStr = ""
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
                return timeStr;
              },
              rules: [{
                required: true,
                message: "请输入视频时长",
                trigger: "blur"
              }]
            },
            {
              label: "封面",
              prop: "prefaceUrl",
              type:"img",
              dataType:"string",
              rules: [{
                required: true,
                message: "请输入回放视频封面地址",
                trigger: "blur"
              }]
            },
            {
              label: "视频大小",
              prop: "totalSize",
              formatter: function (row,value) {
                if (value < 1024) {
                  return value + 'B';
                } else if (value < (1024*1024)) {
                  var temp = value / 1024;
                  temp = temp.toFixed(2);
                  return temp + 'KB';
                } else if (value < (1024*1024*1024)) {
                  var temp = value / (1024*1024);
                  temp = temp.toFixed(2);
                  return temp + 'MB';
                } else {
                  var temp = value / (1024*1024*1024);
                  temp = temp.toFixed(2);
                  return temp + 'GB';
                }
              },
              rules: [{
                required: true,
                message: "请输入回放视频大小",
                trigger: "blur"
              }]
            },
            {
              label: "状态",
              prop: "status",
              dicData:[
                {
                  label:'回放未生成',
                  value:'0'
                },
                {
                  label:'回放生成中',
                  value:'10'
                },
                {
                  label:'转码中',
                  value:'20'
                },
                {
                  label:'回放生成失败',
                  value:'30'
                },
                {
                  label:'回放生成成功',
                  value:'100'
                },
              ],
              rules: [{
                required: true,
                message: "请输入回放状态码",
                trigger: "blur"
              }]
            },
            /*{
              label: "默认状态",
              prop: "defaultStatus",
              rules: [{
                required: true,
                message: "请输入默认状态",
                trigger: "blur"
              }]
            },*/
            {
              label: "回放时间",
              prop: "createTime",
              rules: [{
                required: true,
                message: "请输入回放时间",
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
      ids() {
        let ids = [];
        this.selectionList.forEach(ele => {
          ids.push(ele.id);
        });
        return ids.join(",");
      }
    },
    props:{
      liveRoomId: Number
    },
    watch: {
      liveRoomId: {
        deep: true,
        handler(newVal){
          this.query.liveRoomId = newVal;
          this.onLoad(this.page);
        }
      }
    },
    methods: {
      playback(row){
        getUrl(row.id).then(res=>{
          window.open(res.data.data.url);
        })

      },
      sync(){
        sync(this.liveRoomId).then(res=>{
          this.onLoad(this.page);
        })
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
    }
  };
</script>

<style>
</style>
