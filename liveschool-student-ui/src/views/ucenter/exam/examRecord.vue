<template>
  <div class="u-container">
    <div class="c-tab clearfix">
      <div class="tool-left l">
        <a href="javascript:void(0)" @click="openActive(1)" class="sort-item active">做题记录
        </a>
        <a href="javascript:void(0)" @click="openActive(2)" class="sort-item ">错题本
        </a>
      </div>
    </div>

    <div class="" v-show="detailList.length >0">
      <div class="allcourse-content js-course-list ">
        <div class="system_wrap">
          <div class="system_list">
            <div class="exam_box" v-for="(record,index) in detailList" :key="index">
              <div class="float_left exam_label">{{ record.examTypeName }}</div>
              <div class="float_left exam_ct">
                <div class="ec_title">{{record.paperName}}</div>
                <div class="ec_br">
                  <span class="ec_br_box">共{{record.qstCount}}题，总分{{record.qstScore}}分，成绩{{record.userScore}}分</span>
                </div>
                <div  class="ec_time">交卷时间：
                  <span  class="co_f72">{{record.addTime}}</span>
                </div>
              </div>
              <div class="float_right mt10" >
                <div  class="btn_comm" @click="doAgain(record)">再做一次</div>
                <div  class="btn_comm mt10" @click="goResult(record)">查看结果</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 分页 -->
    <page-component :page="page" @goto="goto" v-show="detailList.length >0"></page-component>
    <div class="nodata" v-if="detailList.length == 0">
      <p><i class="imv2-error_c"></i></p>
      <p>还没有数据</p>
    </div>
  </div>
</template>

<script>
import {mapGetters} from "vuex";
import {getExampaperRecord,getExampaperType} from "@/api/exam";
import pageComponent from '@/views/common/pageComponent'
import {courseInfo} from "@/api";

export default {
  name: 'myCourse',
  data() {
    return {
      detailList: [],
      page: {},
      current: 1,
      size: 20,
      exampaperType:new Map()
    }
  },
  methods: {
    doAgain(examRecord){
      this.$router.push({ path: "/exam/examAnswer", query:{id:examRecord.epId}});
    },
    goResult(examRecord){
      this.$router.push({ path: "/exam/examResult", query:{id:examRecord.id}});
    },
    goto(i) {
      this.current = i
      this.getList();
    },
    getList() {
      let that = this
      if (typeof (this.studentInfo.id) == "undefined") {
        return;
      }
      getExampaperType().then(res => {
        let typeList = res.data.data;
        let params = {
          current: that.current,
          size: that.size,
        }
        getExampaperRecord(params).then(res => {
          typeList.forEach(e=>{
            that.exampaperType.set(e.id,e.title)
          })
          that.detailList = res.data.data.records
          that.detailList.forEach(e=>{
            typeList.forEach(j=>{
              if(j.id == e.examType){
                e.examTypeName = j.title
              }
            })
          })
          that.page = res.data.data
        })
      })
    },
    paperType() {

    },
    getType(id) {
      return this.exampaperType.get(id);
    },
    openActive(i){
      this.$emit('active',i)
    }
  },
  mounted: function () {
    this.getList();
    this.paperType();
  },
  computed: {
    ...mapGetters(["studentInfo"]),
  },
  components: {
    pageComponent
  },
  props:{
    active: Number,
  },
  watch: {
    active: {
      deep: true,
      handler(newVal){
        console.log(newVal)
      }
    },
    studentInfo: function (val, oldVal) {
      this.getList();
    }
  },
}
</script>

<style scoped>
.system_wrap{
  min-height: 500px;
}
.system_list{
  min-height: 150px;
  padding-top: 10px;
}
.exam_box {
  margin-top: 20px;
  padding: 30px 48px 0 22px;
  height: 121px;
  background: #fff;
  -webkit-box-shadow: 0 2px 20px 0 rgba(65,65,75,.1);
  box-shadow: 0 2px 20px 0 rgba(65,65,75,.06);
  border-radius: 5px;
}
.exam_box:hover {
  box-shadow: 0 0 8px rgba(0,0,0,0.2);
}
.exam_box:hover .exam_ct .ec_title{
  color: #0091ff;

}
.exam_label {
  width: 75px;
  height: 21px;
  background: #0091ff;
  border-radius: 10px;
  text-align: center;
  line-height: 21px;
  font-size: 14px;
  color: #fff;
  font-weight: 500;
}
.exam_ct {
  width: 73%;
  margin-left: 20px;
}
.float_left {
  float: left;
}
.ec_title {
  color: #051119;
  font-size: 20px;
  font-weight: 700;
}
.ec_br {
  margin-top: 19px;
}
.ec_br_box {
  margin-right: 20px;
  color: #939191;
  font-size: 14px;
}
.ec_time {
  margin-top: 10px;
  color: #939191;
  font-size: 14px;
}
.co_f72 {
  color: #0091ff;
}
.btn_comm,.btn_comm:hover {
  background-color: #0091ff;
  color: #fff;
  -webkit-transition: background-color .2s,color .3s;
  transition: background-color .2s,color .3s;
}
.btn_comm {
  width: 96px;
  height: 36px;
  background-color: #fff;
  border: 1px solid #0091ff;
  border-radius: 18px;
  color: #0091ff;
  text-align: center;
  line-height: 36px;
  font-size: 14px;
  cursor: pointer;
}
.float_right {
  float: right;
}
</style>
