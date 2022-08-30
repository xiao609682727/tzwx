<template>
  <div>
    <common-header :headNav="headNav"></common-header>
    <div class="wrap">
      <div class="w1200 pt20">
        <div class="sel_direction">选择方向：
          <div class="sel_dr_bg">{{subjectName}} <img style="width: 18px"
              src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABUAAAANCAYAAABGkiVgAAABdklEQVQ4T53Tv2tTURQH8M+xFf0LSkFcHLu6iJBmEEQnuzQODiIdhKp5QTAiuCrYpUmKFMVBHESs6Cz+GlQ6C04Ozl1EcKpQe+Q1plXbJNW7Xd75ft57554bdrPmctx+L42qmI2vwyIxrMCd3GvVK1TwQhHHh2WGo618JkwJS9K0dF8jzg2CB6PtvIcZvFXEpHYu4whuKOJ6P7g/2s5baAqfrKpqxorbedCaEj4gzKrH4k7wzmg7myjRb8KkenzYDLfzKN5v7FNNI5b+hrejrZwRyt8uQyc04vm2r1nI09Y9wnecVMSb32v+RBdyyronGBHOqseDvgfSyivCHD7jlCI+9mq30E5WNsAwhquKKAODVzs7uIR3Rk27ECtloIt2ckIqezOBeUVcHuZtPt8auafG1dTiRyhvyz6Pfw33Q0Wc2TVYFnYvRzkRh3FXEedD703ptUYc+yewVzyfh4xYlsakm6HsJYv2qLoYX/4L7bawimvWzPwEWqdwygXyLQUAAAAASUVORK5CYII="
              class="sel_dr_down">
            <div class="modular_dt">
              <div class="mo_dt_box " v-on:click="clickSubLevel1(subjectDown.id)" v-bind:class="{'mo_dt_sel' :subLevel1Active == subjectDown.id}" v-for="subjectDown in subjectExamTree" :key="subjectDown.id">{{subjectDown.title}}</div>
            </div>
          </div>
        </div>
        <div class="subject"  v-for="subjectChren in subjectLevel2" :key="subjectChren.id">
          <div class="sj_box" v-on:click="clickSubLevel2(subjectChren.id)"  v-bind:class="{'sj_box_bg' :subjectChren.id == subLevel2Active}">{{subjectChren.title}}</div>
        </div>
        <div class="clear"></div>
      </div>
    </div>
      <div class="w1200">
        <div class="home_u">

          <div class="home_u_box" v-for="paperType in paperTypeList" :key="paperType.id">
            <div style="padding: 0px 35px; text-align: center;"><img :src="paperType.img" class="home_b_img">
              <p class="home_title">{{ paperType.title }}</p>
              <div class="home_sh"><p class="home_br">{{ paperType.describtion }}</p></div>
              <router-link target="_blank" :to="{path:'/exam/examList',query:{examType:paperType.id,subLevel1:subLevel1Active,subLevel2:subLevel2Active}}">
              <div class="btn_home">{{ paperType.buttonTitle }}</div>
              </router-link>
            </div>
          </div>

          <div class="home_u_box">
            <div v-if="isLogin" style="padding: 0px 20px;">
              <div class="u_head">
                <img  :src="studentInfo.headImg"  onerror="this.src='/static/img/user-avatar.png'" class="u_img">
                <span class="u_name">{{studentInfo.showName}}</span>
              </div>
              <p class="u_font_b" v-if="exampaper.addTime==null||exampaper.addTime==''">最近练习：暂未练习</p>
              <p class="u_font_b" v-if="exampaper.addTime!=null && exampaper.addTime!=''">最近练习：{{exampaper.addTime}}</p>
              <p class="u_font_b" v-if="exampaper.examType==null || exampaper.examType== -1"></p>
              <p class="u_font_b" v-if="exampaper.examType==1 && exampaper.examType!= -1">模拟试题-<span class="u_f_bg">{{exampaper.paperName}}</span>
              </p>
              <p class="u_font_b" v-if="exampaper.examType==2 && exampaper.examType!= -1">考前押题-<span class="u_f_bg">{{exampaper.paperName}}</span>
              </p>
              <p class="u_font_b" v-if="exampaper.examType==3 && exampaper.examType!= -1 ">历年真题-<span class="u_f_bg">{{exampaper.paperName}}</span>
              </p>
              <p class="time_s" v-if="exampaper.testTime==null || exampaper.testTime== -1">
                <span  class="float_left u_font_b"></span>
                <span class="float_right u_font_b"></span>
              </p>
              <p class="time_s" >
                <span v-if="exampaper.testTime!=null && exampaper.testTime!= -1" class="float_left u_font_b">用时:{{getTime(exampaper.testTime)}}</span>
                <span v-if="exampaper.testTime!=null && exampaper.testTime!= -1" class="float_right u_font_b">正确率：{{exampaper.accuracy}}%</span>
              </p>
              <div class="u_box">
                <div class="u_box_x" @click="toErrorList()">
                  <img src="data:image/png;base64,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" alt="">
                  <p class="u_box_font">错题集</p>
                </div>
                <div class="u_box_x" @click="toFav()">
                  <img src="data:image/png;base64,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" alt="">
                  <p class="u_box_font">收藏夹</p>
                </div>
                <div class="u_box_x" @click="toExamRecordList()">
                  <img src="data:image/png;base64,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" alt="">
                  <p class="u_box_font">做题记录</p>
                </div>
              </div>
            </div>
            <div v-if="isLogin == false" class="noLogin_mask">
              <div class="go_login_font">您还未登录，无法查看数据</div>
              <div class="go_login" @click="toLogin()">立即登录</div>
            </div>
          </div>
        </div>
        <div class="evaluation">
          <div class="float_left eva_left"><p class="eva_title">测评结果评估</p>
            <p class="eva_br">所有记录的汇总，综合检验学习成果</p>
            <div class="eva_su">
              <div v-if="isLogin">
                <div class="eva_cord">
                  <div class="eva_c_box"><p class="eva_b_num"><span class="eva_num_c1">{{exampaper.questionNums}}</span> 道</p>
                    <p class="eva_b_font">做题总数</p></div>
                  <div class="eva_c_box"><p class="eva_b_num"><span class="eva_num_c2">{{exampaper.questionErrorNums}}</span> 道</p>
                    <p class="eva_b_font">错题总数</p></div>
                  <div class="eva_c_box"><p class="eva_b_num"><span class="eva_num_c3">{{exampaper.paperRecordNums}}</span> 套</p>
                    <p class="eva_b_font">试卷总数</p></div>
                  <div class="eva_c_box"><p class="eva_b_num"><span class="eva_num_c4">{{exampaper.collectionQuestionNums}}</span> 道</p>
                    <p class="eva_b_font">收藏的试题</p></div>
                </div>
                <div id="seven" class="seven_echarts" _echarts_instance_="ec_1603710941535"
                     style="-webkit-tap-highlight-color: transparent; user-select: none; position: relative;">

                  <div
                      style="position: absolute; display: none; border-style: solid; white-space: nowrap; z-index: 9999999; transition: left 0.4s cubic-bezier(0.23, 1, 0.32, 1) 0s, top 0.4s cubic-bezier(0.23, 1, 0.32, 1) 0s; background-color: rgba(50, 50, 50, 0.7); border-width: 0px; border-color: rgb(51, 51, 51); border-radius: 4px; color: rgb(255, 255, 255); font: 14px/21px &quot;Microsoft YaHei&quot;; padding: 5px; left: 327px; top: 175px; pointer-events: none;">
                    2020-10-28<br><span
                      style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:#c23531;"></span>0
                  </div>
                </div>
                <div class="float_left data_num">
                  <div class="data_num_box data_num_box_bg">近一周</div>
                </div>
              </div>
              <div v-if="isLogin == false" class="noLogin_mask">
                <div class="go_login_font">您还未登录，无法查看数据</div>
                <div class="go_login" @click="toLogin()">立即登录</div>
              </div>
            </div>
          </div>
        </div>
      </div>


    <common-footer></common-footer>
  </div>

</template>

<script>
import commonHeader from '@/views/common/header'
import echarts from '@/components/echars/echars'
import commonFooter from '@/views/common/footer'
import {subjectExamTree,examPaperByUserId,paperTypeList} from "@/api";
import { setStore, getStore } from '@/util/store'
import { mapGetters } from "vuex";

export default {
  name: 'examIndex',
  data() {
    return {
      headNav: "/exam/examIndex",
      exSuspendShow: true,
      subjectExamTree:getStore({ name: 'subjectExamTree' }) || [],
      subjectLevel2All:[],
      subjectLevel2:[],
      subLevel1Active:getStore({ name: 'subLevel1Active' }) || 0,
      subLevel2Active:getStore({ name: 'subLevel2Active' }) || 0,
      exampaper:[],
      subjectName:"",
      paperTypeList:[]
    }
  }, components: {
    commonHeader, commonFooter
  },
  methods: {
    toErrorList(){
      this.$router.push({ path: "/uc/examRecord", query:{active:2}});
    },
    toExamRecordList(){
      this.$router.push({ path: "/uc/examRecord", query:{active:1}});
    },
    toFav(){
      this.$router.push({ path: "/uc/courseCollection", query:{courseType:"4"}});
    },
    toLogin(){
      this.$router.push({ path: "/user/login", query:{courseType:"4"}});
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
      return h+'分'+s+"秒";
    },
    myEcharts(){
      // 基于准备好的dom，初始化echarts实例
      var myChart = echarts.init(document.getElementById('seven'),'light');
      let that = this

      // 指定图表的配置项和数据
      var option = {
        title: {
          text: ''
        },
        tooltip: {trigger: 'axis'},
        legend: {
          data:['做题总数','错题总数']
        },
        xAxis: {
          data: that.exampaper.quesData.days
        },
        yAxis: {},
        series: [{
          name: '做题总数',
          type: 'line',
          data: that.exampaper.quesData.data1
        },{
          name: '错题总数',
          type: 'line',
          data: that.exampaper.quesData.data2
        }]
      };

      // 使用刚指定的配置项和数据显示图表。
      myChart.setOption(option);

    },
    init(){

      paperTypeList().then(res =>{
        this.paperTypeList = res.data.data
      })
      let subLevel1 = this.$route.query.subLevel1;
      let subLevel2 = this.$route.query.subLevel2;
      if (typeof(subLevel1) == "undefined"){
        let store = getStore({ name: 'subLevel1Active' });
        if (typeof(store) != "undefined"){
          subLevel1 = store
        }
      }
      if (typeof(subLevel2) == "undefined"){
        let store = getStore({ name: 'subLevel2Active' });
        if (typeof(store) != "undefined"){
          subLevel2 = store
        }
      }
      this.subLevel1Active = subLevel1;
      this.subLevel2Active = subLevel2;
      if(this.subjectExamTree.length == 0){
        subjectExamTree().then(res =>{
          let subject2All = []
          this.subjectExamTree = res.data.data;

          setStore({ name: 'subjectExamTree', content: this.subjectExamTree,type: 'session' })
          this.subjectExamTree.forEach(subject =>{
            if (typeof(this.subLevel1Active) == "undefined"){
              this.subLevel1Active =this.subjectExamTree[0].id;
              this.subjectName=subject.title;
            }
            if (typeof(subject.children) != "undefined"){
              subject2All = subject2All.concat(subject.children)
              if(this.subLevel1Active == subject.id){
                this.subjectLevel2 = subject.children;
              }
            }
          })
          this.subjectLevel2All = subject2All;

          if (typeof(this.subLevel2Active) == "undefined"||this.subLevel2Active==0){
            this.subLevel2Active =this.subjectLevel2[0].id;
          }
          this.subjectExamTree.forEach(subject =>{
            if(this.subLevel1Active == subject.id){
              this.subjectName=subject.title;
            }
          })
        })
      }else{
        let subject2All = []
        this.subjectExamTree.forEach(subject =>{
          if (typeof(this.subLevel1Active) == "undefined"){
            this.subLevel1Active =this.subjectExamTree[0].id;
          }
          if (typeof(subject.children) != "undefined"){
            subject2All = subject2All.concat(subject.children)
            if(this.subLevel1Active == subject.id){
              this.subjectLevel2 = subject.children;
            }
          }
        })
        this.subjectLevel2All = subject2All;

        if (typeof(this.subLevel2Active) == "undefined"||this.subLevel2Active==0){
          this.subLevel2Active =this.subjectLevel2[0].id;
        }
        this.subjectExamTree.forEach(subject =>{
          if(this.subLevel1Active == subject.id){
            this.subjectName=subject.title;
          }
        })
      }

      let that = this
      setTimeout(function () {
        if(that.isLogin){
          examPaperByUserId(that.subLevel2Active).then(res=>{
            that.exampaper=res.data.data;
            that.myEcharts()
            console.log(that.exampaper)
          })
        }
      },500);
    },
    clickSubLevel1(subjectId){
      this.subLevel1Active = subjectId;
      this.subLevel2Active=0;
      setStore({ name: 'subLevel1Active', content: subjectId,type: 'session' })
      this.$router.push({ path: "/exam/examIndex",query:{subLevel1:this.subLevel1Active,subLevel2:this.subLevel2Active} });
    },
    clickSubLevel2(subjectId){
      this.subLevel2Active = subjectId;
      setStore({ name: 'subLevel2Active', content: subjectId,type: 'session' })
      this.$router.push({ path: "/exam/examIndex",query:{subLevel1:this.subLevel1Active,subLevel2:this.subLevel2Active} });
    }
  },
  computed: {
    ...mapGetters(["studentInfo","isLogin"]),
  },
  mounted: function () {
    // this.init();
  },
  watch: {
    studentInfo: {
      deep: true,
      immediate: true,
      handler(newVal){
        console.log(newVal)
        this.init();
      }
    },
  },
}
</script>

<style scoped>

</style>
