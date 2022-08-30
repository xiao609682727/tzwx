<template>
  <div>
    <el-row>
      <el-col :span="24">
        <h3>{{ exam.name }}（{{ exam.typeTitle }}）</h3>
        <el-divider></el-divider>
      </el-col>

    </el-row>
    <el-row>
      <el-col :span="12">专业分类：{{ exam.subjectTitle }}</el-col>
<!--      <el-col :span="12">应考人数：{{ exam.joinNum }}人（实考{{ examinfo.actualJoinNum }}人，参考率{{exam.joinNum == 0?0: Math.round(examinfo.actualJoinNum/exam.joinNum * 100)/100 }}%）</el-col>-->
      <el-col :span="12">实考人数：{{ exam.actualJoinNum }}人</el-col>
    </el-row>
    <el-row>
      <el-col :span="12">总分：{{ exam.score }}分</el-col>
      <el-col :span="12">平均分：{{ examinfo.avgScore }}分</el-col>
    </el-row>
    <el-row>
      <el-col :span="12">最高分：{{ examinfo.maxScore }}分</el-col>
      <el-col :span="12">最低分：{{ examinfo.minScore }}分</el-col>
    </el-row>
    <el-row>
      <el-col :span="12">答题时长：{{exam.replyTime}}分钟</el-col>
      <!--      <el-col :span="12">及格分：70分（及格率12%）</el-col>-->
    </el-row>

    <el-tabs >
      <el-tab-pane label="练习记录">
        <exampaperrecordById :exam="exam"></exampaperrecordById>
      </el-tab-pane>
      <el-tab-pane label="试题分析">
        <questionrecord :exam="exam"></questionrecord>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
  import {getList, getDetail, add, update, remove,subjectTree,getPapertypeList} from "@/api/exam/exampaper";
  import {mapGetters} from "vuex";
  import exampaperrecordById from "./exampaperrecordById";
  import questionrecord from "./questionrecord";

  export default {
    components:{
      'exampaperrecordById':exampaperrecordById,
      'questionrecord':questionrecord
    },
    data() {
      return {
        examinfo:{},
        papertypeList:[],
        subjectList:[]
      };
    },
    filters: {
      examType: function (value) {
        switch (value){
          case "1":
            return "单选题";
          case "2":
            return "多选题";
          case "3":
            return "判断题";
          case "4":
            return "填空题";
        }
      }
    },
    computed: {
      ...mapGetters(["permission"]),

    },
    methods: {
      getSubject(subjectId){
        console.log()

      },
      getTime(s){
        //计算分钟
        //算法：将秒数除以60，然后下舍入，既得到分钟数
        var h;
        h  =   Math.floor(s/60);
        //计算秒
        //算法：取得秒%60的余数，既得到秒数
        s  =   s%60;
        //将变量转换为字符串
        h    +=    '';
        s    +=    '';
        //如果只有一位数，前面增加一个0
        h  =   (h.length==1)?'0'+h:h;
        s  =   (s.length==1)?'0'+s:s;
        return h+':'+s;
      },
      init(){
        getDetail(this.exam.id).then(res => {
          this.examinfo = res.data.data
        });
        getPapertypeList().then(res => {
          this.papertypeList = res.data.data
          this.papertypeList.forEach(e=>{
            console.log(this.exam.type)
            console.log(e.id)
            if(e.id == this.exam.type){
              this.exam.typeTitle = e.title
            }
          })
        });
        subjectTree().then(res => {
          this.subjectList = res.data.data
          this.subjectList.forEach(e=>{
            e.children.forEach(j=>{
              if(j.id == this.exam.subjectId){
                this.exam.subjectTitle = j.title
              }
            })
          })
        });
      }
    },
    created:function(){
      this.init()
    },
    props:{
      exam: Object,
    },
    watch: {
      exam: {
        deep: true,
        handler(newVal){
          console.log(newVal)
        }
      }
    },
  };
</script>

<style>
  .box{
    padding: 10px;
    border-radius:4px;
    border: 1px solid #ebeef5;
  }
  .row-bg{
    margin-top: 10px;
  }
  .exam-title{
    text-align: center;
  }
  .box .box-question-group{
    border: 1px solid #ebeef5;
    margin-bottom: 10px;
  }
  .box .box-question-group h4{
    margin:0px;
    background: #F5F7FA;
    padding:10px
  }
  .box .box-question-group .now_q_type{
    margin-left: 15px;
    font-size: 18px;
  }
  .box .btn{
    margin-left: 15px;
  }
  .box .box-question{
    padding:10px 20px;
  }

  .box .box-question .edit-btn .edit-icon{
    margin-top:15px;
  }
  .box .box-question .edit-btn .edit-icon i{
    font-size: 20px;
    margin-left: 15px;
  }
  .box .box-question .edit-btn .edit-score{
    margin-top:15px;
  }
  .row-bg-btn{
    font-size: 20px;
    margin-left: 15px;
  }
  .add-question{
    padding: 50px 10px;
    border-radius:4px;
    border: 1px solid #ebeef5;
    text-align: center;
  }
  .no-question{
    padding: 40px;
    text-align: center;
  }
  .questionNo {
    display: inline;

  }
  .questionNo + p {
    display: inline-block;
    width: calc(100% - 50px);
    margin: 0px;
  }
  .questionbody p{
    margin: 2px 0;
  }

  .active{
    color: #409EFF;
  }
  .optionNo{
    vertical-align: middle;
    margin-right: 10px;
    border: 1px solid #C1C1CB;
    text-align: center;
    width: 20px!important;
    height: 20px!important;
    line-height: 20px;
    display: inline!important;
    font-family: PingFangSC-Regular!important;
    border-radius: 100%;
    font-size: 14px;
    font-style: unset;
    color: #27274A;
    box-sizing: border-box;
    display: inline-block!important;
  }
  .active .optionNo{
    color: #fff!important;
    background-color: #1A8CFE;
    border-color: #1A8CFE!important;
  }

  element.style {
  }
  .el-row {
    margin-bottom: 0px;
  }
</style>
