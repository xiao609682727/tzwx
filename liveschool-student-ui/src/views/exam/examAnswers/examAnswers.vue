<template>
  <div>
    <common-header :headNav="headNav"></common-header>
    <div class="main">
      <div class="container">
        <exam-answer-content ref="content" :examInfo="examInfo"></exam-answer-content>
        <exam-answer-right :examInfo="examInfo" @go="goto"></exam-answer-right>
        <div class="clear"></div>
      </div>
    </div>
    <common-footer></common-footer>

  </div>

</template>

<script>
import commonHeader from '@/views/common/header'
import commonFooter from '@/views/common/footer'
import examAnswerContent from '@/views/exam/examAnswers/examAnswerContent'
import examAnswerRight from '@/views/exam/examAnswers/examAnswerRight'
import {examDetail,favoritesqstListAll} from "@/api";
import {mapGetters} from "vuex";


export default {
  name: 'courseList',
  data() {
    return {
      headNav: "exam/examIndex",
      exSuspendShow:true,
      examInfo:{},
      favList:[]
    }
  }, components: {
    commonHeader, commonFooter, examAnswerContent, examAnswerRight
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
      examDetail(id).then(res=>{
        let examInfo = res.data.data
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
              }
              //处理收藏
              for (let i = 0; i < favArr.length; i++) {
                if(e.qstId == favArr[i].qstId){
                  e.userFavor = true
                  break;
                }
              }
            })
          })

          that.examInfo = examInfo
          console.log(that.examInfo)
        })

      })
    }
  },
  mounted: function () {
    this.init()
  },
  computed: {
    ...mapGetters(["studentInfo","isLogin"]),
  },
}
</script>

<style scoped>

</style>
