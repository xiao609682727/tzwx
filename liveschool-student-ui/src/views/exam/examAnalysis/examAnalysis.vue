<template>
  <div>
    <common-header :headNav="headNav"></common-header>
    <div class="main">
      <div class="container">
        <exam-analysis-content ref="content" :examRecord="examRecord" :examInfo="examInfo"></exam-analysis-content>
        <exam-analysis-right :examRecord="examRecord" :examInfo="examInfo"  @go="goto"></exam-analysis-right>
        <div class="clear"></div>
      </div>
    </div>
    <common-footer></common-footer>
    <div class="d_tips">
      <!--暂停弹出-->
      <div class="ex_mask " v-bind:class="{ hide: exSuspendShow }" >
        <div class="ex_001"><img src="/static/img/exam/tips002.58825a5.png" class="ex_img">
          <p class="ex_font">休息一会，接着再战~~~</p>
          <div class="ex_btn">
            <div class="ex_b_comm2">继续做题</div>
          </div>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
import commonHeader from '@/views/common/header'
import commonFooter from '@/views/common/footer'
import examAnalysisContent from '@/views/exam/examAnalysis/examAnalysisContent'
import examAnalysisRight from '@/views/exam/examAnalysis/examAnalysisRight'
import {exampaperRecordById, favoritesqstListAll} from "@/api";
import {mapGetters} from "vuex";


export default {
  name: 'courseList',
  data() {
    return {
      headNav: "exam/examIndex",
      exSuspendShow:true,
      examRecord:{},
      examInfo:{}
    }
  }, components: {
    commonHeader, commonFooter, examAnalysisContent, examAnalysisRight
  },
  methods: {
    goto(i){
      this.$refs.content.go(i)
    },
    init(){
      //判断是否登录如果没有登录则去登录
      if(!this.isLogin){
        this.$router.push({ path: '/user/login' })
        return;
      }
      let that = this
      let id = this.$route.query.id;
      exampaperRecordById(id).then(res=>{
        this.examRecord = res.data.data
        let examInfo = JSON.parse(this.examRecord.analysisJson)

        let param = {}
        favoritesqstListAll(param).then(res=>{
          let favArr = res.data.data
          let num = 1
          examInfo.list.forEach(j=>{
            j.questionArr.forEach(e=>{
              e.userFavor = false
              e.signFlag = false
              e.num = num;
              num++;

              if(e.qstType == 1 || e.qstType == 2){
                e.optionList = JSON.parse(e.optionList);
              }
              //判断如果是填空题添加填空数组
              if(e.qstType == 4){
                e.answerArr = []
                if(e.useranswer != ''){
                  e.answerArr = e.useranswer.split(",")
                }
              }
              //处理收藏
              for (let i = 0; i < favArr.length; i++) {
                if(e.qstId == favArr[i].qstId){
                  e.userFavor = true
                  break;
                }
              }
              //判断试题状态  1 答错   2答对  3 未答
              if(e.useranswer !=e.isAsr){
                e.qstStatus = 1
              }
              if(e.useranswer ==e.isAsr){
                e.qstStatus = 2
              }
              if(e.useranswer ==''){
                e.qstStatus = 3
              }
            })
          })

          that.examInfo = examInfo
        })
      })
    }
  },
  mounted: function () {
    this.init();
  },
  computed: {
    ...mapGetters(["studentInfo","isLogin"]),
  },
}
</script>

<style scoped>

</style>
