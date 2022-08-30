<template>

  <div class="co_l mt10">
    <div class="as_head as_head2">
      <span class="as_h_title">{{ examRecord.subjectName }}</span>
      <span class="as_h_br">{{ examRecord.typeName }}</span>
      <span class="as_h_br">{{ examRecord.paperName }}</span>
    </div>
    <div class="as_box_topic" v-for="(paperMiddle,index) in examInfo.list" :key="index">
      <div class=""><span class="rt_t_title">{{ paperMiddle.name }}
         <span class="rt_t_br" v-if="paperMiddle.type == 1">（每题备选答案中，只有一个为正确答案。错选、不选均不得分）</span>
        <span class="rt_t_br" v-if="paperMiddle.type == 2">（每题备选答案中，有两个或两个以上为正确答案。多选、少选、错选、不选均不得分）</span>
        <span class="rt_t_br" v-if="paperMiddle.type == 3">（判断每小题的表述是否正确。选错、不选均不得分）</span>
        <span class="rt_t_br" v-if="paperMiddle.type == 4">（根据题干，将空补充完整。有错别字、不填、漏字等均不得分）</span>
      </span></div>
      <div class="type" :ref="qstmiddle.id"  v-for="(qstmiddle,index) in paperMiddle.questionArr" :key="index" >
        <div class="rt_topic_num">
          <div class="float_left num_topic d_jump">{{qstmiddle.num}}</div><span class="cm_score">（{{ paperMiddle.score }}分）</span>
          <div class="float_right">
<!--            <div class="float_left dia_love" @click="tanshow()">
              <img
                  src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAOCAYAAAAfSC3RAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQ1IDc5LjE2MzQ5OSwgMjAxOC8wOC8xMy0xNjo0MDoyMiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTkgKFdpbmRvd3MpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjYzOUQ3OTZCOEE4MzExRUE4QjVDQzcxQUUzNjdBNDY2IiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOjYzOUQ3OTZDOEE4MzExRUE4QjVDQzcxQUUzNjdBNDY2Ij4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6NjM5RDc5Njk4QTgzMTFFQThCNUNDNzFBRTM2N0E0NjYiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6NjM5RDc5NkE4QTgzMTFFQThCNUNDNzFBRTM2N0E0NjYiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz5dhX3WAAABVElEQVR42nTSOyjFYRjH8f85HZdiZzApsRlkYJEsDBiULISyuw6ysCgnnQzEf8BwLArFkVOKlEWUcs2lUDYkFgO5fB/9/vX6l6c+ndP7f5/38rxPxPd9z4kCDKMCWRp7xyFGcB1MjDpJ7bhHMcbRiAaMIg9X6A4mx/TbiVm0Ien9jXMsaJFVZCIe02qW1IJF7/9YQw22sGxHTeBISbm4Q4eTYDs9IR/b2MSM7ViFfk16wxjmcItnHW9QyRZxLFniB/Y0+IVpnGBXY/VYd05wYTlBVT9D93lw/j+Gvn0Hz5GBcg1GVKRLNKFap+nSN4siy7HEAwxoMAdT6MMKdvREkyqORQ/O7I69uEEd0ijEq3M0e9cUXnQya4zSqKpnO2ygNpQUhCVVYl9VPw46J6H7pvXQ8+oYq3IJWtGs9htyW85Tf6a04gSyVUF7rlOUqdl/40eAAQBd71CL3ZFeUwAAAABJRU5ErkJggg=="
                  class="dia_err_img">纠错
            </div>-->
<!--            <div class="float_left dia_love" v-if="qstmiddle.userFavor" @click="removeFav(qstmiddle)">
              <div class="love_bg love_bg_ed" ></div>
              已收藏
            </div>
            <div class="float_left dia_love" v-if="qstmiddle.userFavor == false" @click="saveFav(qstmiddle)">
              <div class="love_bg " ></div>
              收藏
            </div>-->
            <div class="float_left dia_love" v-if="qstmiddle.signFlag == true" @click="clickSignFalse(qstmiddle)">
              <div class="sign_bg sign_bg_ed"></div>
              标记
            </div>
<!--            <div class="float_left dia_love" v-if="qstmiddle.signFlag == false" @click="clickSignTrue(qstmiddle)">
              <div class="sign_bg"></div>
              标记
            </div>-->
          </div>
          <div class="clear_b"></div>
        </div>
       <div class="rt_topic_con" v-if="paperMiddle.type == 1">
          <div class="cm_t_title"><span v-html="qstmiddle.qstContent"></span>
          </div>
          <div class="cm_options">
            <div class="cm_op_box" v-for="(ques,index) in qstmiddle.optionList" :key="index" @click="radioboxChange(qstmiddle,index)">
              <div class="cm_op_l " :class="{'cm_op_bg':indexToString(index)==qstmiddle.useranswer}">{{indexToString(index)}}</div>
              <span class="cm_op_font" v-html="ques.value"></span></div>
          </div>
        </div>
        <div class="rt_topic_con" v-if="paperMiddle.type == 2">
          <div class="cm_t_title"><span v-html="qstmiddle.qstContent"></span>
          </div>
          <div class="cm_options">
            <div class="cm_op_box" v-for="(ques,index) in qstmiddle.optionList" :key="index" @click="checkboxChange(qstmiddle,index)">
              <div class="cm_op_d " :class="{'cm_op_bg':check(index,qstmiddle.useranswer)}">{{indexToString(index)}}</div>
              <span class="cm_op_font" v-html="ques.value"></span></div>
          </div>
        </div>
        <div class="rt_topic_con" v-if="paperMiddle.type == 3">
          <div class="cm_t_title"><span v-html="qstmiddle.qstContent"></span>
          </div>
          <div class="cm_options">
            <div class="cm_op_box" @click="radioboxChange(qstmiddle,0)">
              <div class="cm_op_l" :class="{'cm_op_bg':indexToString(0)==qstmiddle.useranswer}">A</div>
              <span class="cm_op_font">是</span></div>
            <div class="cm_op_box" @click="radioboxChange(qstmiddle,1)">
              <div class="cm_op_l" :class="{'cm_op_bg':indexToString(1)==qstmiddle.useranswer}">B</div>
              <span class="cm_op_font">不是</span></div>
          </div>
        </div>
       <div class="rt_topic_con" v-if="paperMiddle.type == 4">
           <div class="cm_t_title"><span v-html="qstmiddle.qstContent"></span>
          </div>
         <div class="cm_options">
           <div class="cm_d_font float_left">答案：</div>
           <div class="float_left">
             <div class="tk_number" v-for="(option,index) of qstmiddle.isAsr.split(',')" :key="index">（{{ index+1 }}）
               <input type="text" class="tk_number_input" v-model="qstmiddle.answerArr[index]" @blur="textInput(qstmiddle)">
             </div>
           </div>
           <div class="clear_b"></div>
         </div>
        </div>
        <div>
          <div class="rt_jx">
            <div class="rt_comm_d">正确答案：<span class="rt_answer">{{ qstmiddle.isAsr }}</span></div>
            <div class="rt_comm_d">我的答案：<span class="rt_answer rt_answer_bg">{{ qstmiddle.useranswer?qstmiddle.useranswer:'未作答' }}</span>
            </div>
<!--            <div class="float_right rt_r_t">做错次数<span class="rc_cord_bg">6</span>;累计作答<span
                class="rc_cord_bg1">10</span>次
            </div>-->
          </div>
          <div class="reSou">
<!--            <div class="re_s_box"><span class="re_l_mo">考点</span>:
              <div class="re_l_con c333 bge6">听力1,强化2</div>
            </div>-->
            <div class="re_s_box"><span class="re_l_mo">解析</span>:
              <div class="re_l_con c333" v-html="qstmiddle.qstAnalyze"></div>
            </div>
<!--            <div class="re_s_box"><span class="re_l_mo ver_top">笔记</span><span
                class="ver_top c999">:</span>
              <div class="re_l_con re_l_con1"><textarea placeholder="好记性不如烂笔头，记录试题结果和分析"
                                                        class="re_note"></textarea>
                <div class="note_save">保存</div>
              </div>
            </div>-->
          </div>
        </div>
      </div>

    </div>



  </div>
</template>

<script>
import {course, favoritesqstRemove, favoritesqstSave} from "@/api";
import {indexToString} from "@/util/common";
import {mapGetters} from "vuex";

export default {
  name: 'courseListContent',
  data() {
    return {
      courseList: [],
      current: 1,
      size: 10,
      sellType: "",
      level: "",
      subjectId: "",
      subject1Id: "",
      pageList: [],
      pages: 0,
      page: {},
      levelFlag: false,
      orderBy: "default",
    }
  },
  components: {},
  methods: {
    textInput(qstmiddle){
      qstmiddle.useranswer = qstmiddle.answerArr.toString();
    },
    go(index){
      let anchor = this.$refs[index][0].offsetTop;
      this.$nextTick(() => {
        document.body.scrollTop = anchor;
      });
    },
    radioboxChange(qstmiddle,index){
      qstmiddle.useranswer = ''
      qstmiddle.useranswer = indexToString(index);
    },
    checkboxChange(qstmiddle,index){
      let answer = indexToString(index);
      let useranswer = qstmiddle.useranswer;
      let arr = useranswer.split(",");
      if(arr.indexOf(answer) == -1){
        arr.push(answer)
        arr.sort();
        let str = "";
        for (let i = 0; i < arr.length; i++) {
          if(i == 0){
            str += arr[i]
          }else{
            str += ","+arr[i]
          }
        }
        qstmiddle.useranswer = str
      }else{
        let str = ""
        for (let i = 0; i < arr.length; i++) {
          if(answer != arr[i]){
            str += arr[i]+","
          }
        }
        str = str.substr(0,str.length - 1)
        qstmiddle.useranswer = str
      }
      console.log(qstmiddle.useranswer)

    },
    check(index,useranswer){
      let arr = useranswer.split(",");
      if(arr.indexOf(indexToString(index)) !=-1){
        return true
      }else{
        return false;
      }
    },
    clickSignFalse(qstmiddle){
      qstmiddle.signFlag = false;
    },
    clickSignTrue(qstmiddle){
      qstmiddle.signFlag = true;
    },
    indexToString(str){
      return indexToString(str);
    },
    saveFav(qstmiddle){
      let param = {
        userId:this.studentInfo.id,
        qstId:qstmiddle.qstId
      }
      favoritesqstSave(param).then(res=>{
        qstmiddle.userFavor = true
        this.$layer.alert(
            "收藏成功！",
            {
              shade: true,
              title: "提示"
            },
            laeryid => {
              this.$layer.close(laeryid);
            }
        );
      })
    },
    removeFav(qstmiddle){
      let param = {
        userId:this.studentInfo.id,
        qstId:qstmiddle.qstId
      }
      favoritesqstRemove(param).then(res=>{
        qstmiddle.userFavor = false
        this.$layer.alert(
            "取消收藏！",
            {
              shade: true,
              title: "提示"
            },
            laeryid => {
              this.$layer.close(laeryid);
            }
        );
      })
    },
  },
  mounted: function () {
  },
  props:{
    examRecord: Object,
    examInfo: Object
  },
  watch: {
    examRecord: {
      deep: true,
      handler(newVal){

        console.log(this.examInfo)
      }
    },
    examInfo: {
      deep: true,
      handler(newVal){
        console.log(this.examInfo)
      }
    },
  },
  computed: {
    ...mapGetters(["studentInfo"]),
  },
}
</script>

<style lang="scss" >
@import "../../../../public/static/exam-scss.scss";
p{
  margin: 0;
  padding: 0;
}
.cm_t_title p{
  float: none;
}
</style>
