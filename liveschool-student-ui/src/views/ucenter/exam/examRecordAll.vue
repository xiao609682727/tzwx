<template>
  <div >
    <examRecord v-if="active==1" :active="active" @active="setActive"></examRecord>
    <examErrorQuestions v-if="active==2" :active="active"  @active="setActive"></examErrorQuestions>
  </div>
</template>

<script>
import {mapGetters} from "vuex";
import examRecord from '@/views/ucenter/exam/examRecord'
import examErrorQuestions from '@/views/ucenter/exam/examErrorQuestions'

export default {
  name: 'examRecordAll',
  data() {
    return {
      active:1
    }
  },
  methods: {
    setActive(i){
      this.active = i
    }
  },
  mounted: function () {
    let active = this.$route.query.active;
    if (typeof(active) != "undefined"){
      this.active = active
    }
  },
  computed: {
    ...mapGetters(["studentInfo"]),
  },
  components: {
    examRecord,
    examErrorQuestions
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
  width: 75%;
  margin-left: 20px;
}
.float_left {
  float: left;
}
.ec_title {
  color: #051119;
  font-size: 20px;
  font-weight: 500;
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
