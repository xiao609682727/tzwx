<template>
  <el-container>
    <el-aside width="250px">
      <div class="box">
        <div v-for="(item,index) of exampaperArr">
          <el-row type="flex" class="row-bg" justify="space-between">
            <el-col :span="16" >{{item.name}}</el-col>
            <el-col :span="8" class="row-bg-btn">
              <el-link type="primary" @click="upExamPaper(index)" :underline="false" style="font-size: 18px;margin-left: 5px;">
                <i class="el-icon-top"  ></i>
              </el-link>
              <el-link type="primary" @click="downExamPaper(index)" :underline="false" style="font-size: 18px;margin-left: 5px;">
                <i class="el-icon-bottom" ></i>
              </el-link>
              <el-link type="primary" @click="delExamPaper(index)" :underline="false" style="font-size: 18px;margin-left: 5px;">
                <i class="el-icon-delete" ></i>
              </el-link>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="space-between">
            <el-col :span="24">共{{ item.num }}题，共{{item.num * item.score}}分数</el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="space-between">
            <el-col :span="24">每题<el-input-number v-model="item.score"  @change="handleChange" :precision="1" :step="0.5" size="mini" style="margin-left:5px;margin-right:5px;width:100px" ></el-input-number>分</el-col>
          </el-row>
          <el-divider></el-divider>
        </div>

        <div>
          <el-row type="flex" class="row-bg" justify="space-between">
            <el-col :span="24">总题数： {{ getTotalNum() }}题</el-col>
            <el-col :span="24">当前总分： {{getTotalScore()}}分</el-col>
          </el-row>
        </div>
      </div>
    </el-aside>
    <el-container>
      <el-main >
        <div class="box" style="margin: 0 10px">
          <h3 class="exam-title">
            {{ exam.name }}
          </h3>

          <el-divider></el-divider>
          <div class="box-question-group" v-for="(item,index) of exampaperArr">
            <h4>
              <el-input v-model="item.name" placeholder="请输入内容" @input="handleChange"  style="width:150px;"></el-input>
              <span class="now_q_type">{{ item.type| examType}}</span>
              <el-button type="primary" class="btn" @click="openAddQuestion(item)" >添加试题</el-button>
            </h4>
            <div v-if="item.questionArr.length > 0" class="box-question" v-for="(question,index) of item.questionArr">
              <!-- 单选题 -->
              <div v-if="item.type == '1'">
                <el-row>
                  <el-col :span="18">
                    <div class="questionbody">
                      <div class="questionNo">{{ index + 1 }}.</div><p  v-html="question.qstContent"></p>
                    </div>
                    <div>
                      <div class="questionbody" v-for="(option,index) of toObject(question.optionList)" :class="{'active':question.isAsr == indexToString(index)}">
                        <div class="questionNo "><div class="optionNo">{{indexToString(index)}}</div></div><p  v-html="option.value"></p>
                      </div>
                    </div>
                    <div class="questionNo">答案：</div><p  v-html="question.isAsr"></p>
                    <div class="questionNo">解析：</div><p  v-html="question.qstAnalyze"></p>

                  </el-col>
                  <el-col class="edit-btn" :span="6" style="text-align: right">
                    <div class="edit-icon">
<!--                  <el-link type="primary" @click="delExamPaper(index)" :underline="false" style="font-size: 18px;margin-left: 5px;">
                        <i class="el-icon-edit"></i>
                      </el-link>-->
                      <el-link type="primary" @click="upQuestion(item.questionArr,index)" :underline="false" style="font-size: 18px;margin-left: 5px;">
                        <i class="el-icon-top"></i>
                      </el-link>
                      <el-link type="primary" @click="downQuestion(item.questionArr,index)" :underline="false" style="font-size: 18px;margin-left: 5px;">
                        <i class="el-icon-bottom"></i>
                      </el-link>
                      <el-link type="primary" @click="delQuestion(item.questionArr,index)" :underline="false" style="font-size: 18px;margin-left: 5px;">
                        <i class="el-icon-delete"></i>
                      </el-link>
                    </div>
                  </el-col>
                </el-row>
                <el-divider></el-divider>
              </div>
              <!-- 多选题 -->
              <div v-if="item.type == '2'">
                <el-row>
                  <el-col :span="18">
                    <div class="questionbody">
                      <div class="questionNo">{{ index + 1 }}.</div><p  v-html="question.qstContent"></p>
                    </div>
                    <div>
                      <div class="questionbody" v-for="(option,index) of toObject(question.optionList)" v-bind:key="index" :class="{'active':question.isAsr.indexOf(indexToString(index)) != -1 }">
                        <div class="questionNo"><div class="optionNo">{{indexToString(index)}}</div></div><p  v-html="option.value"></p>
                      </div>
                    </div>
                    <div class="questionNo">答案：</div><p  v-html="question.isAsr"></p>
                    <div class="questionNo">解析：</div><p  v-html="question.qstAnalyze"></p>

                  </el-col>
                  <el-col class="edit-btn" :span="6" style="text-align: right">
                    <div class="edit-icon">
                      <!--<el-link type="primary" @click="delExamPaper(index)" :underline="false" style="font-size: 18px;margin-left: 5px;">
                        <i class="el-icon-edit"></i>
                      </el-link>-->
                      <el-link type="primary" @click="upQuestion(item.questionArr,index)" :underline="false" style="font-size: 18px;margin-left: 5px;">
                        <i class="el-icon-top"></i>
                      </el-link>
                      <el-link type="primary" @click="downQuestion(item.questionArr,index)" :underline="false" style="font-size: 18px;margin-left: 5px;">
                        <i class="el-icon-bottom"></i>
                      </el-link>
                      <el-link type="primary" @click="delQuestion(item.questionArr,index)" :underline="false" style="font-size: 18px;margin-left: 5px;">
                        <i class="el-icon-delete"></i>
                      </el-link>
                    </div>
                  </el-col>
                </el-row>
                <el-divider></el-divider>
              </div>
              <!-- 判断题 -->
              <div v-if="item.type == '3'">
                <el-row>
                  <el-col :span="18">
                    <div class="questionbody">
                      <div class="questionNo">{{ index + 1 }}.</div><p  v-html="question.qstContent"></p>
                    </div>
                    <div>
                      <span :class="{'active':question.isAsr == 'A'}"><i class="el-icon-check"></i>正确</span><br/>
                      <span :class="{'active':question.isAsr == 'B'}"><i class="el-icon-close"></i>错误</span>
                    </div>
                    <div class="questionNo">答案：</div><p v-if="question.isAsr == 'A'" >正确</p><p v-if="question.isAsr == 'B'" >错误</p>
                    <div class="questionNo">解析：</div><p  v-html="question.qstAnalyze"></p>
                  </el-col>
                  <el-col class="edit-btn" :span="6" style="text-align: right">
                    <div class="edit-icon">
                      <!--<el-link type="primary" @click="delExamPaper(index)" :underline="false" style="font-size: 18px;margin-left: 5px;">
                        <i class="el-icon-edit"></i>
                      </el-link>-->
                      <el-link type="primary" @click="upQuestion(item.questionArr,index)" :underline="false" style="font-size: 18px;margin-left: 5px;">
                        <i class="el-icon-top"></i>
                      </el-link>
                      <el-link type="primary" @click="downQuestion(item.questionArr,index)" :underline="false" style="font-size: 18px;margin-left: 5px;">
                        <i class="el-icon-bottom"></i>
                      </el-link>
                      <el-link type="primary" @click="delQuestion(item.questionArr,index)" :underline="false" style="font-size: 18px;margin-left: 5px;">
                        <i class="el-icon-delete"></i>
                      </el-link>
                    </div>
                  </el-col>
                </el-row>
                <el-divider></el-divider>
              </div>
              <!-- 填空题 -->
              <div v-if="item.type == '4'">
                <el-row>
                  <el-col :span="18">
                    <div class="questionbody">
                      <div class="questionNo">{{ index + 1 }}.</div><p  v-html="question.qstContent"></p>
                    </div>
                    <div class="questionNo">答案：</div><p  v-html="question.isAsr"></p>
                    <div class="questionNo">解析：</div><p  v-html="question.qstAnalyze"></p>
                  </el-col>
                  <el-col class="edit-btn" :span="6" style="text-align: right">
                    <div class="edit-icon">
                      <!--<el-link type="primary" @click="delExamPaper(index)" :underline="false" style="font-size: 18px;margin-left: 5px;">
                        <i class="el-icon-edit"></i>
                      </el-link>-->
                      <el-link type="primary" @click="upQuestion(item.questionArr,index)" :underline="false" style="font-size: 18px;margin-left: 5px;">
                        <i class="el-icon-top"></i>
                      </el-link>
                      <el-link type="primary" @click="downQuestion(item.questionArr,index)" :underline="false" style="font-size: 18px;margin-left: 5px;">
                        <i class="el-icon-bottom"></i>
                      </el-link>
                      <el-link type="primary" @click="delQuestion(item.questionArr,index)" :underline="false" style="font-size: 18px;margin-left: 5px;">
                        <i class="el-icon-delete"></i>
                      </el-link>
                    </div>
                  </el-col>
                </el-row>
                <el-divider></el-divider>
              </div>
            </div>

            <div class="no-question" v-if="item.questionArr.length == 0">
              <span>此大题还未添加试题，请点击"<el-link type="primary" @click="openAddQuestion(item)" >添加试题</el-link>"按钮选择试题</span>
            </div>
          </div>

          <div class="add-question" @mouseover="addModel = true" @mouseleave="addModel = false">
            <div v-show="addModel == false" style="height: 30px">
              创建新的大题
            </div>
            <div v-show="addModel" style="height: 30px">
              <el-button type="primary" @click="addExampaper('1')" plain>单选题</el-button>
              <el-button type="primary" @click="addExampaper('2')" plain>多选题</el-button>
              <el-button type="primary" @click="addExampaper('3')" plain>判断题</el-button>
              <el-button type="primary" @click="addExampaper('4')" plain>填空题</el-button>
            </div>
          </div>
        </div>
      </el-main>
    </el-container>
    <el-dialog title="添加试题" width="80%" :visible.sync="addQuestionVisible" append-to-body>
      <chooseQuestion :examPaperOption="examPaperOption" :paperId="exam.id" @closeQuestion="closeQuestion" v-if="addQuestionVisible"></chooseQuestion>
    </el-dialog>

  </el-container>
</template>

<script>
  import {getList, getDetail, add, update, remove,subjectTree} from "@/api/exam/exampaper";
  import {mapGetters} from "vuex";
  import chooseQuestion from "./chooseQuestion";

  export default {
    components:{
      'chooseQuestion':chooseQuestion,
    },
    data() {
      return {
        addQuestionVisible:false,
        addModel:false,
        examPaperOption:{},
        totalScore: 0,
        totalNum: 0,
      };
    },
    props:{
      exampaperArr: Object,
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
      handleChange(){
        this.$emit('saveExam')
      },
      getTotalNum(){
        let totalNum = 0;
        this.exampaperArr.forEach(function (e){
          totalNum +=e.questionArr.length
        })
        return totalNum;
      },
      getTotalScore(){
        let totalScore = 0;
        this.exampaperArr.forEach(function (e){
          totalScore +=(e.questionArr.length * e.score)
        })
        return totalScore;
      },
      indexToString(index){
        var charcode;
        return index.toString(26).split("").map(function(item,i){
          charcode = item.charCodeAt(0);
          charcode+=(charcode>=97?10:49);
          return String.fromCharCode(charcode)
        }).join("").toUpperCase();
      },
      toObject(str){
        return JSON.parse(str);
      },
      openAddQuestion(obj){
        this.examPaperOption = obj
        this.addQuestionVisible = true
      },
      delQuestion(arr,i){
        let that = this
        this.$confirm("确定将选择删除?", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
        .then(() => {
          arr.splice(i, 1);
          that.$emit('saveExam')
          return true;
        })
        .then(() => {
          this.$message({
            type: "success",
            message: "操作成功!"
          });
        });
      },
      upQuestion(arr,i){
        if(i == 0) {
          return;
        }
        let index1 = i;
        let index2 = i-1;
        arr[index1] = arr.splice(index2, 1, arr[index1])[0];
        this.$emit('saveExam')
      },
      downQuestion(arr,i){
        if(i == arr.length -1) {
          return;
        }
        let index1 = i;
        let index2 = i+1;
        arr[index1] = arr.splice(index2, 1, arr[index1])[0];
        this.$emit('saveExam')
      },
      delExamPaper(i){
        let that = this
        this.$confirm("确定将选择删除?", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
            .then(() => {
              that.exampaperArr.splice(i, 1);
              that.$emit('saveExam')
              return true;
            })
            .then(() => {
              this.$message({
                type: "success",
                message: "操作成功!"
              });
            });

      },
      upExamPaper(i){
        if(i == 0) {
          return;
        }
        let index1 = i;
        let index2 = i-1;
        this.exampaperArr[index1] = this.exampaperArr.splice(index2, 1, this.exampaperArr[index1])[0];
        this.$emit('saveExam')
      },
      downExamPaper(i){
        console.log("1231")
        if(i == this.exampaperArr.length -1) {
          return;
        }
        let index1 = i;
        let index2 = i+1;
        this.exampaperArr[index1] = this.exampaperArr.splice(index2, 1, this.exampaperArr[index1])[0];
        this.$emit('saveExam')
      },
      addExampaper(type){
        let name = this.$options.filters['examType'](type)
        let data = {
          "name":name,
          "score":"1",
          "num":"0",
          "type":type,
          "questionArr":[],
          "paperId":this.exam.id,
          "title":"",
        }
        this.exampaperArr.push(data);
        this.$emit('saveExam')
      },
      closeQuestion(){
        this.addQuestionVisible = false;
        this.$emit('saveExam')
      }
    },
    created:function(){
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
    line-height: 18px;
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
</style>
