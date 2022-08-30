<template>
  <div>
    <common-header :headNav="headNav"></common-header>
    <div class="main" style="min-height: 500px">
      <div class="container">
        <div class="rt_head">
          <span class="rt_h_title">&nbsp;</span>
          <div class="rt_h_box">
            <div class="rt_h_big" :style="{width:progress+ '%'}"></div>
          </div>
          <span class="rt_num"><span
              style="color: rgb(255, 126, 2);">{{page.current}}</span>/{{page.pages}}</span>
          <div class="float_right">
            <div class="fr_box"  @click="goError"><img
                src="/static/img/exam/exam-fanhui.png"
                alt="">
              <!--<p class="fr_b_font">返回错题集</p>-->
            </div>
            <div class="fr_box" @click="remove(list[0].id)" ><img  src="/static/img/exam/exam-delete.png"   alt="">
              <!--<p class="fr_b_font">移除试题</p>--></div>
            <!--<div class="fr_box"><img
                src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB8AAAAfCAYAAAAfrhY5AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA0ppVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQ1IDc5LjE2MzQ5OSwgMjAxOC8wOC8xMy0xNjo0MDoyMiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6QkRCQTE1MzU4OUM3MTFFQUIxQjJCMzg2MjcyRjQ5MEQiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6QkRCQTE1MzQ4OUM3MTFFQUIxQjJCMzg2MjcyRjQ5MEQiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTkgKFdpbmRvd3MpIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9ImFkb2JlOmRvY2lkOnBob3Rvc2hvcDo5NTA0ZWEyZi05ZmVhLWM1NDctYWI5OC1lMmU3ZThjZDFiY2MiIHN0UmVmOmRvY3VtZW50SUQ9ImFkb2JlOmRvY2lkOnBob3Rvc2hvcDo5NTA0ZWEyZi05ZmVhLWM1NDctYWI5OC1lMmU3ZThjZDFiY2MiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz7gnjiJAAACSklEQVR42uSXTUhUURTHZ94MaqmFuPGjxIUimIIgKiqMIdQmTEWSFFxYIhgt1E3qUg1qIYjgxwjapoJ2UhKYIKgIaeBCRBcGaqAIiWQgKn71P/AfeTzmvhnhvZnAA7/3cd59/N+5595z73N6vV4HLRVUgXhw7rDHjsA3sCA3bjpbwWsQ5bDfusA78FLEc0EPo+0H28Bpk3AseAbqwbKId/NBLfgUgsiHwDpo1nDIpHNO0TgORFgovsHzXU3XxReKxps29MiZHDTdyHaa5CnbYnHt8hAuc/vx3Qcd7BpfrySCz8z9Pqfmlu4d8beBQoXOd9BpTK0/8cfggcF3E5Tx+gSMGsTl+ROQpRBP46w6CyT+CkzxK0VoAuyAanAb/AUzhnf+gEqQoRBfNQqrxEVw3OA7ALMBUviTXG3U/U8Dzmi9YClc4i12RR7Wbr+W4ofB5tzUGreagm47nDzou5QClaaKXArNCIjW+aR6jYESCyJfk6KjirycdfoeKJK1l/U5mtVv2s87FwEE9avmnlm3i/hXUEDROxSW3uizIPJbwKUS/w3yWS7z6JPFpCHIyAJZirQ3G+3JIFJ3L1vqGxaN9iSzqZYDfvAD3oNFpmKSy6dDkXMz9BZjJi55TQAfQR0o5bJYDJ5bEHmUb8C5/IxW+YFIBwO8l92LB7wAXyzIueyQNDfXb2MvTBC97XIrpCocV17QRHCejpoQlVYPzyvyBe3gKXgLHoFfFtR82TKdMpUuRurk/H5I/xs3/yAq+J/mCUHkx9wdf/gnwADz73Q2EoTWiQAAAABJRU5ErkJggg=="
                alt="">
              &lt;!&ndash;<p class="fr_b_font">计算器</p>&ndash;&gt;</div>-->
          </div>
        </div>
        <div class="rt_con"   v-for="(item,index) in list" :key="index">
          <div class="rt_topic"><span class="rt_t_title">{{getQuesType(item.qstType)}}
            <span class="rt_t_br" v-if="item.qstType == '1'">（每题备选答案中，只有一个为正确答案。错选、不选均不得分）</span>
            <span class="rt_t_br" v-if="item.qstType == '2'">（每题备选答案中，有两个或两个以上为正确答案。多选、少选、错选、不选均不得分）</span>
            <span class="rt_t_br" v-if="item.qstType == '3'">（判断每小题的表述是否正确。选错、不选均不得分）</span>
            <span class="rt_t_br" v-if="item.qstType == '4'">（根据题干，将空补充完整。有错别字、不填、漏字等均不得分）</span>
          </span></div> <!---->
          <div>
            <div class="rt_topic_num">
              <div class="float_left num_topic">1</div>
              <div class="float_right">
<!--                <div class="float_left dia_love"><img
                    src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAOCAYAAAAfSC3RAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQ1IDc5LjE2MzQ5OSwgMjAxOC8wOC8xMy0xNjo0MDoyMiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTkgKFdpbmRvd3MpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjYzOUQ3OTZCOEE4MzExRUE4QjVDQzcxQUUzNjdBNDY2IiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOjYzOUQ3OTZDOEE4MzExRUE4QjVDQzcxQUUzNjdBNDY2Ij4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6NjM5RDc5Njk4QTgzMTFFQThCNUNDNzFBRTM2N0E0NjYiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6NjM5RDc5NkE4QTgzMTFFQThCNUNDNzFBRTM2N0E0NjYiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz5dhX3WAAABVElEQVR42nTSOyjFYRjH8f85HZdiZzApsRlkYJEsDBiULISyuw6ysCgnnQzEf8BwLArFkVOKlEWUcs2lUDYkFgO5fB/9/vX6l6c+ndP7f5/38rxPxPd9z4kCDKMCWRp7xyFGcB1MjDpJ7bhHMcbRiAaMIg9X6A4mx/TbiVm0Ien9jXMsaJFVZCIe02qW1IJF7/9YQw22sGxHTeBISbm4Q4eTYDs9IR/b2MSM7ViFfk16wxjmcItnHW9QyRZxLFniB/Y0+IVpnGBXY/VYd05wYTlBVT9D93lw/j+Gvn0Hz5GBcg1GVKRLNKFap+nSN4siy7HEAwxoMAdT6MMKdvREkyqORQ/O7I69uEEd0ijEq3M0e9cUXnQya4zSqKpnO2ygNpQUhCVVYl9VPw46J6H7pvXQ8+oYq3IJWtGs9htyW85Tf6a04gSyVUF7rlOUqdl/40eAAQBd71CL3ZFeUwAAAABJRU5ErkJggg=="
                    class="dia_err_img">纠错
                </div>-->
<!--                <div class="float_left dia_love" v-if="item.favFlag =='2'" @click="removeFav(item)">
                  <div class="love_bg love_bg_ed"></div>
                    已收藏
                </div>
                <div class="float_left dia_love" v-if="item.favFlag =='1'" @click="saveFav(item)">
                  <div class="love_bg"></div>
                  收藏
                </div>-->
              </div>
              <div class="clear_b"></div>
            </div>
            <div class="rt_topic_con" v-if="item.qstType===1 || item.qstType===2">
              <div class="cm_t_title" v-html="item.qstContent"></div>
              <div class="cm_options">
                <div class="cm_op_box" v-for="(option,index) of toObject(item.optionList)" :key="index">
                  <div class="cm_op_d">{{indexToString(index)}}</div>
                  <span class="cm_op_font" v-html="option.value"></span></div>
              </div>
            </div>

            <div class="rt_topic_con" v-if="item.qstType===3">
              <div class="cm_t_title" v-html="item.qstContent"></div>
              <div class="cm_options">
                <div class="cm_op_box"  >
                  <div class="cm_op_d">A</div>
                  <span class="cm_op_font" >对</span></div>
                <div class="cm_op_box"  >
                  <div class="cm_op_d">B</div>
                  <span class="cm_op_font" >错</span></div>
              </div>
            </div>

            <div class="rt_topic_con" v-if="item.qstType===4">
              <div class="cm_t_title" v-html="item.qstContent"></div>
              <div class="cm_options">
                <div class="cm_op_box" v-for="(option,index) of item.isAsr.split(',')" :key="index" >
                  <div class="cm_op_d">({{index+1}})</div>
                  <span class="cm_op_font" >________________</span></div>
              </div>
            </div>
          </div>
          <div>
            <div>
              <div class="rt_jx">
                <div class="rt_comm_d">正确答案：<span class="rt_answer" v-html="item.isAsr"></span>
                </div>
                <!--<div class="rt_comm_d">我的答案：<span class="rt_answer rt_answer_bg">C</span>
                  <img
                      src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAOCAYAAAAfSC3RAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQ1IDc5LjE2MzQ5OSwgMjAxOC8wOC8xMy0xNjo0MDoyMiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTkgKFdpbmRvd3MpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjU4N0I2NEM2OEE4NDExRUE5RDJBODI0RjJGMDJEMjk1IiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOjU4N0I2NEM3OEE4NDExRUE5RDJBODI0RjJGMDJEMjk1Ij4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6NTg3QjY0QzQ4QTg0MTFFQTlEMkE4MjRGMkYwMkQyOTUiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6NTg3QjY0QzU4QTg0MTFFQTlEMkE4MjRGMkYwMkQyOTUiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz7REHikAAABKElEQVR42oTSu0oDQRTG8ckGROzyKBZWG0GQaIKieCksfIqgECQSTFAJCJInsBYVAl4KX0KwsAmCeE1ILSgS9X/gWzgsXgZ+yc7sfLOzZyfzHMezIYQ6qrgIf7cMKihG/OxhFOeY+yfYxA7GLbjmbhxiQSv/FFrXddeCZ7DtvmIYx1h0gSx23QO6GIvUsW2u6jpSeF79mt7Lxu8xiafIrdzW5IH6B9rNpvo95HGTrO7bqQr0ghxmNH6NGA/Bbcu3L32SKzf2qaff+onpoBWigenUnKYK+GswOQi2QAdl7WJIr1FMB+1/GxuuEFPYRwnvGj/Bsg9WXejRTgbu1L/ECt4wgiNbzIItbLnQhLbpC9ZOnpR8OgsW8KFS59PVc80OyRL6VulvAQYAIXdAnuDzlwEAAAAASUVORK5CYII="
                      style="margin-top: -7px; margin-left: 20px;"> &lt;!&ndash;&ndash;&gt;</div>-->
                <!--<div class="float_right rt_r_t">做错次数<span
                    class="rc_cord_bg">2</span>;累计作答<span
                    class="rc_cord_bg1">2</span>次
                </div>-->
              </div>
              <div class="reSou">
                <!--<div class="re_s_box"><span class="re_l_mo">考点</span>:
                  <div class="re_l_con c333 bge6">听力1,强化2</div>
                </div>-->
                <div class="re_s_box"><span class="re_l_mo">解析</span>:
                  <div class="re_l_con c333" v-html="item.qstAnalyze"></div>
                </div>
                <!--<div class="re_s_box"><span class="re_l_mo ver_top">笔记</span><span
                    class="ver_top c999">:</span>
                  <div class="re_l_con re_l_con1"><textarea
                      placeholder="好记性不如烂笔头，记录试题结果和分析"
                      class="re_note"></textarea>
                    <div class="note_save">保存</div>
                  </div>
                </div>-->
              </div>
            </div> <!----></div>
          <div class="topic_btn">
<!--            <div class="btn_comm submit_btn">提交答案</div>-->
            <div class="btn_box" v-if="page.pages > 1">

              <div class="btn_comm float_right" v-if="page.current < page.pages" @click="next()">下一题</div>
              <div class="btn_comm float_right" v-if="page.current > 1"  @click="previous()">上一题</div>
              <div class="clear_b"></div>
            </div>
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
import {getFavoritesqst, removeErrorquestion,saveFavoritesqst,removeFavoritesqst,removeFavoritesqstById} from "@/api/exam";
import { mapGetters } from "vuex";

export default {
  name: 'courseList',
  data() {
    return {
      headNav: "",
      list:[],
      current: 1,
      size: 1,
      page: {},
      progress:0
    }
  }, components: {
    commonHeader, commonFooter
  },
  methods: {
    getList() {
      if (typeof (this.studentInfo.id) == "undefined") {
        return;
      }
      let current = this.$route.query.current;
      if (typeof(current) != "undefined"){
        this.current = current
      }
      let params = {
        current: this.current,
        size: this.size,
        favFlag: "1"
      }
      getFavoritesqst(params).then(res => {
        this.list = res.data.data.records
        this.page = res.data.data
        this.progress = this.current/this.page.pages*100
      })
    },
    indexToString(index){
      var charcode;
      return index.toString(26).split("").map(function(item,i){
        charcode = item.charCodeAt(0);
        charcode+=(charcode>=97?10:49);
        return String.fromCharCode(charcode)
      }).join("").toUpperCase();
    },
    remove(id) {
      let that = this
      this.$layer.confirm(
        "确定要删除吗？",
        {
          title: "删除试题",
          btn: ["确定", "取消"]
        },
        layerid => {
          // this.$layer.msg("执行了删除");
          removeFavoritesqstById(id).then(res => {
            console.log(that.page.pages)
            if(that.page.pages == 1){
              that.goError()
              return;
            }
            if(that.page.current == that.page.pages){
              this.$router.push({ path: "/exam/examFavQuestion",query:{current:parseInt(this.current) - 1} });
            }
            that.getList()
          })
          this.$layer.close(layerid);
        },
        layerid => {
          this.$layer.close(layerid);
        }
      )
    },
    getQuesType(i){
      switch (i){
        case 1:
          return "单选题";
        case 2:
          return "多选题";
        case 3:
          return "判断题";
        case 4:
          return "填空题";
      }
    },
    toObject(str){
      return JSON.parse(str);
    },
    next(){
      this.$router.push({ path: "/exam/examFavQuestion",query:{current:parseInt(this.current) + 1} });
    },
    previous(){
      this.$router.push({ path: "/exam/examFavQuestion",query:{current:parseInt(this.current) - 1} });
    },
    goError(){
      this.$router.push({ path: "/uc/courseCollection",query:{courseType:4}});
    },
    saveFav(item){
      let data = {
        qstId:item.qstId
      }
      saveFavoritesqst(data).then(res=>{
        item.favFlag = "2"
      })
    },
    removeFav(item){
      let data = {
        qstId:item.qstId
      }
      removeFavoritesqst(data).then(res=>{
        item.favFlag = "1"
      })
    }
  },
  computed: {
    ...mapGetters(["studentInfo"]),
  },
  mounted: function () {
    this.getList()
  },
  watch: {
    studentInfo: function (val, oldVal) {
      this.getList();
    }
  },
}
</script>

<style scoped>
  .btn_comm{
    width: 100px;
    height: 40px;
    background: #0091ff;
    border-radius: 5px;
    color: #fff;
    font-size: 14px;
    text-align: center;
    line-height: 40px;
    cursor: pointer;
    margin-left: 20px;
  }
</style>
