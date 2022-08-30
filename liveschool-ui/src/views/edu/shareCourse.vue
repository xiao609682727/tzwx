<template>
  <el-tabs type="border-card">
    <el-tab-pane label="讲师">
      <el-row type="flex" justify="center">
        <el-col :span="3"><div style="margin: 10px">参加码:</div></el-col>
        <el-col :span="12">
          <div style="margin: 10px" v-if="linkInfo.teacherCode == ''">----</div>
          <div style="margin: 10px" v-if="linkInfo.teacherCode != ''">{{linkInfo.teacherCode}}</div>
        </el-col>
        <el-col :span="3"></el-col>
      </el-row>
      <el-row type="flex" justify="center">
        <el-col :span="3"><div style="margin: 10px">分享链接:</div></el-col>
        <el-col :span="12">
          <avue-input disabled="disabled"  v-model="linkInfo.teacherCode=='' ? '该教室暂无分享地址' : urlTop+'web/room/prepare?room_id='+linkInfo.roomId+'&code='+linkInfo.teacherCode"></avue-input>
        </el-col>
        <el-col :span="3" style="margin-top: 3px;margin-left: 10px">
        <el-button v-if="linkInfo.teacherCode != ''" size="small" type="primary" @click="copyUrl('teacher')">复 制</el-button>
        </el-col>
      </el-row>
    </el-tab-pane>
    <el-tab-pane label="助教">
      <el-row type="flex" justify="center">
        <el-col :span="3"><div style="margin: 10px">参加码:</div></el-col>
        <el-col :span="12">
          <div style="margin: 10px" v-if="linkInfo.tutorCode == ''">----</div>
          <div style="margin: 10px" v-if="linkInfo.tutorCode != ''">{{linkInfo.tutorCode}}</div>
        </el-col>
        <el-col :span="3"></el-col>
      </el-row>
      <el-row type="flex" justify="center">
        <el-col :span="3"><div style="margin: 10px">分享链接:</div></el-col>
        <el-col :span="12">
          <avue-input disabled="disabled" v-model="linkInfo.tutorCode=='' ? '该教室暂无分享地址' : urlTop+'web/room/prepare?room_id='+linkInfo.roomId+'&code='+linkInfo.tutorCode"></avue-input>
        </el-col>
        <el-col :span="3" style="margin-top: 3px;margin-left: 10px">
          <el-button v-if="linkInfo.tutorCode != ''" size="small" type="primary" @click="copyUrl('tutor')">复 制</el-button>
        </el-col>
      </el-row>
    </el-tab-pane>
    <el-tab-pane label="学员">
      <el-row type="flex" justify="center">
        <el-col :span="3"><div style="margin: 10px">参加码:</div></el-col>
        <el-col :span="12">
          <div style="margin: 10px" v-if="linkInfo.studentCode == ''">----</div>
          <div style="margin: 10px" v-if="linkInfo.studentCode != ''">{{linkInfo.studentCode}}</div>
        </el-col>
        <el-col :span="3"></el-col>
      </el-row>
      <el-row type="flex" justify="center">
        <el-col :span="3"><div style="margin: 10px">分享链接:</div></el-col>
        <el-col :span="12">
          <avue-input disabled="disabled" v-model="linkInfo.studentCode=='' ? '该教室暂无分享地址' : urlTop+'web/room/prepare?room_id='+linkInfo.roomId+'&code='+linkInfo.studentCode"></avue-input>
        </el-col>
        <el-col :span="3" style="margin-top: 3px;margin-left: 10px">
          <el-button v-if="linkInfo.studentCode != ''" size="small" type="primary" @click="copyUrl('student')">复 制</el-button>
        </el-col>
      </el-row>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
  import {getList} from "@/api/cms/websiteprofile";
  export default {
    data(){
      return {
        linkInfo:this.linkInfo,
        urlTop: {}
      }
    },
    props:{
      linkInfo: Object
    },
    watch: {
      linkInfo: {
        deep: true,
        handler(newVal){
          this.linkInfo = newVal;
          this.onLoad(this.page);
        }
      }
    },
    methods:{
      reload(){
        let params = {
          configType: 'baijiayun_video'
        };
        getList(params).then(res => {
          let wdata = res.data.data
          for(let i = 0 ; i < wdata.length ; i++){
            if(wdata[i].dataKey == 'URL'){
              this.urlTop = wdata[i].dataValue
            }
          }
        })
      },
      copyUrl(type) {
        const input = document.createElement('input')
        document.body.appendChild(input)
        if (type == 'teacher'){
          input.setAttribute('value', this.urlTop+"web/room/prepare?room_id="+this.linkInfo.roomId+"&code="+this.linkInfo.teacherCode)
        }else if (type == 'tutor'){
          input.setAttribute('value', this.urlTop+"web/room/prepare?room_id="+this.linkInfo.roomId+"&code="+this.linkInfo.tutorCode)
        }else if (type == 'student'){
          input.setAttribute('value', this.urlTop+"web/room/prepare?room_id="+this.linkInfo.roomId+"&code="+this.linkInfo.studentCode)
        }
        input.select()
        if (document.execCommand('copy')) {
          document.execCommand('copy')
        }
        document.body.removeChild(input)
      }
    },
    created: function(){
      this.reload();
    }
  }
</script>

<style>
</style>
