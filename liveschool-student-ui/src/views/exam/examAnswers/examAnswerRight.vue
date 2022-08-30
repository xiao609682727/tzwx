<template>
  <div class="co_r" >
    <div :class="{'is_fixed':isFixed}">
      <div class="as_head as_head2 as_head3"><p class="co_r_font">测试进度</p>
        <div class="co_r_big">
          <div class="co_r_b" :style="{'width': doCount/examInfo.qstCount*100+'%'}"></div>
        </div>
        <span class="float_right co_r_font1">
        <span class="rc_cord_bg1">{{ doCount }}</span>/{{ examInfo.qstCount }}</span>
      </div>
      <div class="ex_time">
        <div class="ex_h">
          <img
              src="data:image/png;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAMCAgMCAgMDAwMEAwMEBQgFBQQEBQoHBwYIDAoMDAsKCwsNDhIQDQ4RDgsLEBYQERMUFRUVDA8XGBYUGBIUFRT/2wBDAQMEBAUEBQkFBQkUDQsNFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBT/wAARCAASABIDASIAAhEBAxEB/8QAGwABAAEFAQAAAAAAAAAAAAAAAAYBAwQFBwj/xAAmEAABBAICAgIBBQAAAAAAAAABAgMEBQARBhIhMQcIExQiQVFx/8QAFAEBAAAAAAAAAAAAAAAAAAAAAP/EABQRAQAAAAAAAAAAAAAAAAAAAAD/2gAMAwEAAhEDEQA/APfbjI+RqK25RbuPyeOsIfVW0zL6mWn22uw/K8U6KlLUk6HpI/veXarg7TPFK/knDGl0Fs/EamiAh9bkWT2QFfhcQo6876hQ6kE7zJ4EYlfSSvj23dMefFQ7HbDhCDMirKujrR9H9p0QNkFJ3lVx43xLAbCb+0uH/wBOIldRyXW1l5fgNpQlKArxoDfoAneBuan5V47Z1UOYuc3GXIZQ6plZ2psqSD1J17G9YyGU/wBd4CKiCmdLdTNDCA+lvykOdR20f5G94wJj8sVMGy4dMXLhx5S2E9mlPtJWWyfZTseD/mRH6901e3VypqYMZM1K+gkhlIcCTvY7a3rx6xjA7BjGMD//2Q=="
              style="margin-right: 7px;">答题时间
        </div>
        <countDown ref="countDown" v-if="remainTime > 0" @timer="timer" :remainTime="remainTime" @countDowmEnd="countDowmEnd"></countDown>
        <div class="submit_btn" @click="showJiaojuan">交卷</div>
        <div class="fun_box">
          <div class="fun_b_f" @click="showJiaojuan">
            <img src="/static/img/exam/exam-dati-tuichu.png" alt="">
            <p class="fun_b_font">退出答题</p></div>
          <div class="fun_b_f" @click="pause()" >
            <img src="/static/img/exam/exam-dati-zanting.png" alt="">
            <p class="fun_b_font">暂停</p></div>
          <!--        <div class="fun_b_f" @click="play()" v-if="timeflag">
                    <img src="/static/img/exam/exam-dati-zanting.png" alt="">
                    <p class="fun_b_font">开始</p></div>-->
          <!--        <div class="fun_b_f">
                    <img src="/static/img/exam/exam-dati-jisuanqi.png" alt="">
                    <p class="fun_b_font">计算器</p></div>-->
        </div>
      </div>
      <div class="ex_as">
        <div class="as_h"><img src="/static/img/exam/exam-dati-datika.png" style="margin-right: 10px; margin-top: -2px;"> <span class="as_title">答题卡</span><span
            class="score_co">（试卷总分{{ examInfo.score }}分）</span></div>
        <div class="as_br">
          <div class="as_br_box">
            <div class="as_box_md"></div>
            <span class="as_b_font">已做</span></div>
          <div class="as_br_box">
            <div class="as_box_md as_box_md1"></div>
            <span class="as_b_font">未做</span></div>
          <div class="as_br_box">
            <div class="as_box_md as_box_md2 "></div>
            <span class="as_b_font">标记</span></div>
        </div>
        <div class="as_all_ops">
          <div class="pb60">
            <div class="as_ops_box" v-for="(paperMiddle,index) in examInfo.list" :key="index">
              <div class="as_ops_md"><p class="as_o_h"><span class="as_o_title">【{{ paperMiddle.name }}】</span><span
                  class="score_co">（共 <span class="c999">{{ paperMiddle.questionArr.length }}</span> 题，一共{{ paperMiddle.questionArr.length*paperMiddle.score }}分）</span></p>
                <div class="as_c_num"  >

                  <div id="1" class="as_ops_num as_ops_num_hover" @click="go(qstmiddle.id)" :class="{'as_box_md_blue':qstmiddle.useranswer != '','as_ops_bg':qstmiddle.signFlag}" v-for="(qstmiddle,index) in paperMiddle.questionArr" :key="index">
                    {{ qstmiddle.num }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!--交卷弹出-->
      <div class="ex_mask" v-if="endFlag">
        <div class="ex_001"><img src="/static/img/exam/exam-jiaojuan.png" class="ex_img">
          <p class="ex_font" >时间已用完请交卷</p>
          <div class="ex_btn">
            <!--          <div class="float_left ex_b_comm submit_b" @click="hideJiaojuan(1)">取消</div>-->
            <div class=" ex_b_comm close_b" style="margin: 10px auto;" @click="submit()">交卷</div>
          </div>
        </div>
      </div>
      <!--交卷弹出-->
      <div class="ex_mask" v-bind:class="{'hide' :1 == jiaojuantanflag}">
        <div class="ex_001"><img src="/static/img/exam/exam-jiaojuan.png"
                                 class="ex_img">
          <p class="ex_font"  v-if="(examInfo.qstCount - doCount) > 0">您还有{{ examInfo.qstCount - doCount }}试题未完成</p>
          <p class="ex_font"  v-if="(examInfo.qstCount - doCount) == 0">您确定要交卷吗？</p>
          <div class="ex_btn">
            <div class="float_left ex_b_comm  close_b"  @click="hideJiaojuan(1)">取消</div>
            <div class="float_right ex_b_comm  submit_b" @click="submit()">交卷</div>
          </div>
        </div>
      </div>
      <!--暂停弹出-->
      <div class="ex_mask"  :class="{'hide':timeflag == true}">
        <div class="ex_001"><img src="/static/img/exam/exam-zanting.png"
                                 class="ex_img">
          <p class="ex_font">休息一会，接着再战~~~</p>
          <div class="ex_btn">
            <div class="ex_b_comm2" @click="play()">继续做题</div>
          </div>
        </div>
      </div>
      <!--退出考试-->
      <div class="ex_mask hide">
        <div class="ex_001"><img src="/static/img/exam/exam-exit.png"
                                 class="ex_img">
          <p class="ex_font">你确定要退出考试吗？</p>
          <div class="ex_btn">
            <div class="float_left ex_b_comm submit_b">确定</div>
            <div class="float_right ex_b_comm close_b">取消</div>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import {examRecordSave} from "@/api";
import countDown from '@/views/common/countDown'
import {mapGetters} from "vuex";
import { setStore, getStore } from '@/util/store'

export default {
  name: 'courseListContent',

  data() {
    return {
      endFlag:false,
      jiaojuantanflag: 1,
      timeShowFlag:false,
      remainTime:0,
      timeflag:true,
      doCount:0,
      isFixed:false,
      submitFlag:true,
      offsetTop: 80,
    }
  },
  components: {
    countDown
  },
  methods: {
    initHeight() {
// 设置或获取位于对象最顶端和窗口中可见内容的最顶端之间的距离 (被卷曲的高度)
      var scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop
//如果被卷曲的高度大于吸顶元素到顶端位置 的距离
      this.isFixed = scrollTop > this.offsetTop ? true : false;
    },
    timer(){
      let remainTime = getStore({name: this.examInfo.id + "_" + this.studentInfo.id + '_remainTime'});
      remainTime--;
      setStore({ name: this.examInfo.id + "_" + this.studentInfo.id + '_remainTime', content: remainTime,type: 'session' })
    },
    countDowmEnd(){
      this.submit()
      // this.endFlag = true
    },
    showJiaojuan(){
      this.jiaojuantanflag = 0
    },
    hideJiaojuan(){
      this.jiaojuantanflag = 1
    },
    submit(){
      // 防止重复点击，点击标识，如果标识为false，则再点击不执行,f
      if(!this.submitFlag){
        return;
      }
      this.submitFlag = false;
      //去掉关闭事件提醒
      // window.removeEventListener('beforeunload', e => this.beforeunloadHandler(e))
      this.examInfo.userId = this.studentInfo.id
      let time = this.$refs.countDown.time
      if(time > this.examInfo.replyTime * 60){
        time = this.examInfo.replyTime * 60
      }
      this.examInfo.testTime = this.$refs.countDown.time
      //复制examInfo对象然后交卷，防止对象数据错乱，影响页面显示
      let examInfoDate = JSON.stringify(this.examInfo);
      examInfoDate = JSON.parse(examInfoDate)
      examInfoDate.list.forEach(e=>{
        e.questionArr.forEach(e=>{
          if(e.qstType == 1 || e.qstType == 2){
            e.optionList = "";
            e.qstContent ="";
            // e.optionList = JSON.stringify(e.optionList)
            console.log(e.optionList)
          }
        })
      })
      let param = examInfoDate
      examRecordSave(param).then(res=>{
        let data = res.data.data;
        //当请求成功之后设置为true
        this.submitFlag = true;
        this.$router.push({ path: "/exam/examResult",query:{id:data.id} });
      })
    },
    init(){
      let remainTime = getStore({name: this.examInfo.id + "_" + this.studentInfo.id + '_remainTime'});
      if (remainTime && remainTime != 0) {
        this.remainTime = remainTime
      }else{
        this.remainTime = this.examInfo.replyTime * 60
        setStore({ name: this.examInfo.id + "_" + this.studentInfo.id + '_remainTime', content: this.remainTime,type: 'session' })
      }
    },
    play(){
      this.timeflag = true
      this.$refs.countDown.play()
    },
    pause(){
      this.timeflag = false
      this.$refs.countDown.pause()
    },
    go(i){
      this.$emit('go',i)
    },
    compute(){
      let that = this
      that.doCount = 0
      this.examInfo.list.forEach(e=>{
        e.questionArr.forEach(e=>{
          if(e.useranswer != ''){
            that.doCount ++;
          }
        })
      })
    },
    beforeunloadHandler (e) {
      console.log('刷新或关闭')
      e.returnValue = '关闭提示';
      // ...
    }
  },

  mounted: function () {
    this.init()
    window.addEventListener('scroll', this.initHeight,true);
    /*this.$nextTick(() => {
      //获取对象相对于版面或由 offsetTop 属性指定的父坐标的计算顶端位置
      this.offsetTop = document.querySelector('#boxFixed').offsetTop;
    })*/
  },
  props:{
    examInfo: Object,
  },
  watch: {
    examInfo: {
      deep: true,
      handler(newVal){
        console.log(newVal)
        this.init()
        this.compute()
      }
    },
  },
  computed: {
    ...mapGetters(["studentInfo"]),
  },
  created() {
    // window.addEventListener('beforeunload', e => this.beforeunloadHandler(e))
  },
  destroyed() {
    window.removeEventListener('scroll', this.initHeight)
    // window.removeEventListener('beforeunload', e => this.beforeunloadHandler(e))
  }

}
</script>

<style   lang="scss" >
.is_fixed{
  position: fixed;
  top: 0;
  z-index: 999;
}
</style>
