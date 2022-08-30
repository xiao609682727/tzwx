<template>
  <div class="container">
    <div class="course-tool-bar clearfix">
      <div class="tool-left l">
        <a href="javascript:void(0)" @click="toOrderBy('default')" :class="orderBy=='default' ? 'active' : ''" class="moco-change-smalle-btn">默认</a>
        <a href="javascript:void(0)" @click="toOrderBy('update')" :class="orderBy=='update' ? 'active' : ''" class="moco-change-smalle-btn">最新</a>
      </div>
    </div>
    <div class="course-list">
      <div class="mt20">
        <div class="clearfix">
          <div class="list_box" v-for="(exam,index) in examList" :key="index">
            <div class=" list_box2" >
              <span class="l_b2_font" v-for="paperType in paperTypeList" :key="paperType.id">{{exam.type == paperType.id?paperType.title:''}}</span>
              <p class="li_title">{{exam.name}}</p>
              <div class="l_box">
                <span class="l_box_w">题数：{{exam.qstCount}}题</span>
              </div>
              <div class="l_box" v-for="(subject,index) in examSubjectList" :key="index">
                <span class="l_box_w txtOf" style="width: 110%" v-if="exam.subjectId == subject.id">分类：{{subject.subjectName}}</span>
              </div>
              <div class="l_box">
                <span class="l_box_w">分数：{{exam.score}}分</span>
              </div>
              <div class="l_box">
                <span class="l_box_w">限时：{{exam.replyTime}}分钟</span>
              </div>
              <div class="l_time1">
                随时可参加考试
              </div>
              <div class="l_btn_comm l_btn1" @click="goExam(exam)">
                参加考试
              </div>
            </div>
          </div>
          <div class="clear"></div>
        </div>

      </div>
      <page-component :page="page" @goto="goto" v-show="examList.length > 0" ></page-component>
      <div class="nodata" v-if="examList.length == 0">
        <p><i class="imv2-error_c"></i></p>
        <p>暂无数据</p>
      </div>
    </div>

  </div>
</template>

<script>
  import {exam,getExamSubjectList} from "@/api";
  import pageComponent from '@/views/common/pageComponent'
  import { mapGetters } from "vuex";

  export default {
    name: 'examListContent',
    data() {
      return {
        examList:[],
        examSubjectList:[],
        current:1,
        size:8,
        sellType:"",
        level:"",
        subjectId:"",
        subject1Id:"",
        pageList:[],
        pages:0,
        page:{},
        levelFlag:false,
        orderBy:"default",
      }
    },
    components: {
      pageComponent
    },
    methods: {
      goExam(exam){
        if(!this.isLogin){
          this.$router.push({ path: '/user/login' })
          return
        }
        let routeData = this.$router.resolve({ path: "/exam/examInfo",query:{id:exam.id} });
        window.open(routeData.href, '_blank');
      },
      goto(i){
        this.current = i
        this.selectExamList();
      },
      toOrderBy(orderBy1){//排序
          this.orderBy = orderBy1;
          this.selectExamList();
      },
      selectExamList(){
        let subjectId = "";
        let level = "";
        let sellType = "";
        let examName = this.$route.query.searchExamName;
        let typeActive = this.$route.query.examType;
        let subLevel1Active = this.$route.query.subLevel1;
        let subLevel2Active = this.$route.query.subLevel2;
        if (typeof(subLevel1Active) == "undefined"){
          subLevel1Active = 0
        }
        if (typeof(subLevel2Active) == "undefined"){
          subLevel2Active = 0
        }

        if(typeActive != 0){
          sellType = typeActive
        }
        if(subLevel2Active == 0&&subLevel1Active == 0){
          level = "";
          subjectId = "";
        }else if(subLevel2Active == 0&&subLevel1Active != 0){
          level = "1";
          subjectId = subLevel1Active;
        }else{
          level = "2";
          subjectId = subLevel2Active;
        }
        exam(this.current,this.size,sellType,level,subjectId,examName,this.orderBy).then(res =>{
          this.examList = res.data.data.records;
          this.page = res.data.data
          this.pages = res.data.data.pages
          this.computePage(res.data.data.pages)
        })
      },
      computePage(pages){
        this.pageList = []
        if(pages<=7){
          for (let i = 1; i <= pages; i++) {
            this.pageList.push(i)
          }
        }else{
          if(this.current <= 4){
            this.pageList = [1,2,3,4,5,6,7];
          }else if(this.current+3<=pages) {
            let n = this.current
            this.pageList = [n-3,n-2,n-1,n,n+1,n+2,n+3];
          }else{
            this.pageList = [pages-6,pages-5,pages-4,pages-3,pages-2,pages-1,pages];
          }
        }
      },
      getExamSubject(){
        getExamSubjectList().then(res =>{
          this.examSubjectList = res.data;
          console.log(this.examSubjectList);
        })
      },
    },

    mounted:function() {
      this.selectExamList();
      this.getExamSubject();
    },
    computed: {
      ...mapGetters(["website","navigation","userInfo","studentInfo","isLogin"]),
    },
    props:{
      paperTypeList: Object,
    },
    watch: {
      paperTypeList: {
        deep: true,
        handler(newVal){
          console.log(newVal)
        }
      },
    },
  }
</script>

<style scoped>

</style>
