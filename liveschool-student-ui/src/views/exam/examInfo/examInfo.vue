<template>
  <div v-wechat-title="examInfoTitle">
    <common-header :headNav="headNav"></common-header>
    <div class="main">
      <div class="container">
        <!--<div class="as_head"><span class="as_h_title">{{ examInfo.subjectName }}</span> <span
            class="as_h_br">{{ examInfo.typeName }}</span><span class="as_h_br"></span>
        </div>-->

        <div class="as_con">

          <p class="as_head_title pt30">{{ examInfo.name }}</p>
          <div class="n-ex-time-in-boy">
            <span class="n-ex-time-tx">

              <img :src="studentInfo.headImg"  onerror="this.src='/static/img/user-avatar.png'" >
            </span>
            <div class="n-ex-time-dl">
              <dl>
                <dt class="f-fM txtOf" style="width: 200%;" :title="'试卷类型：'+examInfo.subjectName">试卷类型：{{ examInfo.subjectName }}</dt>
              </dl>
              <dl>
                <dt class="f-fM ">试卷分类：{{ examInfo.typeName }}</dt>
              </dl>
              <dl>
                <dt class="f-fM ">试卷满分：{{ examInfo.score }}分</dt>
              </dl>
              <dl>
                <dt class="f-fM ">试卷题数：{{ examInfo.qstCount }}道</dt>
              </dl>
              <dl>
                <dt class="f-fM ">试卷限时：{{ examInfo.replyTime }}分钟</dt>
              </dl>
              <dl>
                <dt class="f-fM ">已做人数：{{ examInfo.actualJoinNum }}人</dt>
              </dl>
              <dl>
                <dt class="f-fM ">参&nbsp;考&nbsp;人：{{ studentInfo.showName }}</dt>
              </dl>
            </div>
            <!--<div class="tac n-ex-time-nub">
              <span class="f-fM c-333 vam">考试开始倒计时：</span>
              <span class="f-fM c-master vam time" id="time">00:00:00</span>
            </div>-->

            <div class="tac n-ex-time-btn" id="permitExam"  style="" >
              <a href="javascript:void(0)" @click="go" title="立即考试" class="f-fM cou-nocou-btn fsize16 " onfocus="this.blur()">
                开始考试
              </a>
            </div>

<!--            <div class="as_c_btn">-->
<!--              <a href="javascript:void(0)" title="立即考试" class="f-fM cou-nocou-btn fsize16 " onfocus="this.blur()">-->
<!--                <div class="tac n-ex-time-btn btn_home" id="notPermitExam" >-->
<!--                立即考试-->
<!--                </div>-->
<!--              </a>-->
<!--            </div>-->
          </div>

        </div>
        <div class="clear"></div>
      </div>
    </div>
    <common-footer></common-footer>
  </div>

</template>

<script>
import commonHeader from '@/views/common/header'
import commonFooter from '@/views/common/footer'
import {exampaperRecordById,examById} from "@/api";
import {mapGetters} from "vuex";
import { setStore, getStore ,removeStore} from '@/util/store'

export default {
  name: 'courseList',
  data() {
    return {
      examInfoTitle: "",
      headNav: "/exam/examIndex",
      exSuspendShow: true,
      examInfo:{}
    }
  }, components: {
    commonHeader, commonFooter
  },
  methods: {
    go(){
      removeStore({name: this.examInfo.id + "_" + this.studentInfo.id + '_remainTime', type: 'session'})
      let routeData = this.$router.resolve({ path: "/exam/examAnswer",query:{id:this.examInfo.id} });
      window.open(routeData.href, '_blank');
    },
    init(){
      //判断是否登录如果没有登录则去登录
      if(!this.isLogin){
        this.$router.push({ path: '/user/login' })
        return;
      }
      let id = this.$route.query.id;
      examById(id).then(res=>{
        this.examInfo = res.data.data
        console.log(this.examInfo)
        //把教师名字动态赋值给title
        let title = "";
        if (this.$store.getters.website.title) {
          title = this.examInfo.name+"-"+this.$store.getters.website.title;
        }
        this.examInfoTitle = title;
      })
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
    toErrorList(){
      this.$router.push({ path: "/uc/examRecord", query:{active:2}});
    },
    toIndex(){
      this.$router.push({ path: "/exam/examIndex" });
    },
    toAnswer(){
      this.$router.push({ path: "/exam/examAnswer",query:{id:this.examInfo.epId} });
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
  .n-ex-time-tx {
    width: 120px;
    height: 120px;
    display: block;
    margin: 20px auto 35px;
    overflow: hidden;
    border-radius: 50%;
  }
  .n-ex-time-tx img {
    display: block;
    width: 100%;
  }
  .n-ex-time-dl {
    margin: 0 auto;
    width: 30%;
  }
  .n-ex-time-dl dl {
    padding-left: 90px;
    position: relative;
    min-height: 30px;
    margin-bottom: 20px;
  }
  .n-ex-time-dl dl dt {
    position: absolute;
    top: 0;
    left: 0;
    text-align: left;
    line-height: 30px;
    font-size: 18px;
  }
  .n-ex-time-nub {
    font-size: 16px;
    line-height: 30px;
    font-weight: bold;
    margin-top: 40px;
  }
  .vam {
    vertical-align: middle;
  }
  .n-ex-time-nub .time {
    font-size: 26px;
    font-weight: bold;
  }
  .n-ex-time-btn a {
    border: 1px solid transparent;
    display: block;
    width: 150px;
    height: 40px;
    line-height: 40px;
    border-radius: 4px;
    text-align: center;
    margin: 0 auto;
    background: #0091ff;
    border: 1px solid #0091ff;
    box-shadow: 0 2px 15px 0 #b3d4ff;
    border-radius: 20px;
    color: #fff;
    text-decoration: none;
  }
  .n-ex-time-in-boy{
    padding-top: 20px;
    padding-bottom: 50px;
  }
  .n-ex-time-btn {
    margin-top: 50px;
  }
</style>
