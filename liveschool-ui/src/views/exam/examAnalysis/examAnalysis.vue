<template>
  <div>
    <div class="main">
      <div class="container" >
        <exam-analysis-content ref="content" :examRecord="examRecord" :examInfo="examInfo"></exam-analysis-content>
        <exam-analysis-right :examRecord="examRecord" :examInfo="examInfo"  @goback="goback"></exam-analysis-right>
        <div class="clear"></div>
      </div>
    </div>
<!--    <div class="d_tips">
      &lt;!&ndash;暂停弹出&ndash;&gt;
      <div class="ex_mask " v-bind:class="{ hide: exSuspendShow }" >
        <div class="ex_001"><img src="/static/img/exam/tips002.58825a5.png" class="ex_img">
          <p class="ex_font">休息一会，接着再战~~~</p>
          <div class="ex_btn">
            <div class="ex_b_comm2">继续做题</div>
          </div>
        </div>
      </div>
    </div>-->
  </div>

</template>

<script>
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
     examAnalysisContent, examAnalysisRight
  },
  methods: {
    goback(i){
      this.$emit('goto')
    },
    init(examRecordId){
      let that = this
      exampaperRecordById(examRecordId).then(res=>{
        this.examRecord = res.data.data
        let examInfo = JSON.parse(this.examRecord.analysisJson)
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
            console.log(e)
          })
        })

        that.examInfo = examInfo
      })
    }
  },
  mounted: function () {
  },
  computed: {
    ...mapGetters(["studentInfo"]),
  },
  props:{
    examRecordId: Object,
  },
  watch: {
    examRecordId: {
      immediate: true,
      handler(newVal){
        this.init(newVal)
      }
    },

  },
}
</script>

<style lang="scss" scoped>
@import "../../../../public/static/exam-scss.scss";
.container{
  width: 1200px;
  margin: 0 auto;
}
</style>
