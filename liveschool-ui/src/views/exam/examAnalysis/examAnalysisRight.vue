<template>
  <div class="co_r" >
    <div class="as_head as_head2">
      <p class="co_r_font"></p>
      <div class=" submit_btn" style="margin-top: 8px" @click="goback1()">查看报告</div>
    </div>
    <div class="ex_time">
      <div class="ex_h">
        <img src="data:image/png;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAMCAgMCAgMDAwMEAwMEBQgFBQQEBQoHBwYIDAoMDAsKCwsNDhIQDQ4RDgsLEBYQERMUFRUVDA8XGBYUGBIUFRT/2wBDAQMEBAUEBQkFBQkUDQsNFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBT/wAARCAASABIDASIAAhEBAxEB/8QAGwABAAEFAQAAAAAAAAAAAAAAAAYBAwQFBwj/xAAmEAABBAICAgIBBQAAAAAAAAABAgMEBQARBhIhMQcIExQiQVFx/8QAFAEBAAAAAAAAAAAAAAAAAAAAAP/EABQRAQAAAAAAAAAAAAAAAAAAAAD/2gAMAwEAAhEDEQA/APfbjI+RqK25RbuPyeOsIfVW0zL6mWn22uw/K8U6KlLUk6HpI/veXarg7TPFK/knDGl0Fs/EamiAh9bkWT2QFfhcQo6876hQ6kE7zJ4EYlfSSvj23dMefFQ7HbDhCDMirKujrR9H9p0QNkFJ3lVx43xLAbCb+0uH/wBOIldRyXW1l5fgNpQlKArxoDfoAneBuan5V47Z1UOYuc3GXIZQ6plZ2psqSD1J17G9YyGU/wBd4CKiCmdLdTNDCA+lvykOdR20f5G94wJj8sVMGy4dMXLhx5S2E9mlPtJWWyfZTseD/mRH6901e3VypqYMZM1K+gkhlIcCTvY7a3rx6xjA7BjGMD//2Q=="
            style="margin-right: 7px;">答题时间
      </div>
      <div class="count_down">{{ formatNum(hour) }}:{{ formatNum(minute) }}:{{ formatNum(second) }}</div>
      <div class="fun_box">
<!--        <div class="fun_b_f">
          <img
              src="data:image/png;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAMCAgMCAgMDAwMEAwMEBQgFBQQEBQoHBwYIDAoMDAsKCwsNDhIQDQ4RDgsLEBYQERMUFRUVDA8XGBYUGBIUFRT/2wBDAQMEBAUEBQkFBQkUDQsNFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBT/wAARCAAfAB4DASIAAhEBAxEB/8QAGAAAAwEBAAAAAAAAAAAAAAAABQYHBAn/xAAwEAABAwMDAwEFCQEAAAAAAAABAgMEBQYRAAchEkFRMRMyYnGxCBQiI1NhcoGRwf/EABYBAQEBAAAAAAAAAAAAAAAAAAQABf/EACARAAEDAgcAAAAAAAAAAAAAAAABBBECAwUUFVJTsbL/2gAMAwEAAhEDEQA/AOmt4XvSbGp6JdUeUn2qw2yw0nrdeV4QnvpW39kqVs1XHmytrrRHPIKVAF9vII9RwcEaHWHFTuBuFX7tnJ9vGpclVMpTS+Ut9HvuAeTkEH4j4GCf2hG1ObP3AEJKjhg4SM8B9sk/5rExydKdxx1+VNjBo1NtPJR6Q30G5mLY2hoVZqAkPsMUqIt4soLjhy2gFWP7yT4ydNNFrUG4aZHqNOkolw309TbrZ4I/4QeCDyCNCtu2inby2WnUEEUqKlSFj0/JTkEancKoRdmtxqrS+UW5VIwqMeOgcR3usIWlI7A+vyCR205jOVtTtp6QG8jM3Y3L2Etl3BQKvd1pyMImQ6iuY0k8FxhwDpUPOMDP8hqqaSL+29euGdDrlEmikXNBHSxLIyh1H6bg7p5PnGTwdKszfWoWWr7peFtuRpqRy7TpDbjbnxBJVlOfBJ00GWHUQveindDdl+mwXEhmjU0IkPn3UvLcBCMjv0/RQ7a3x9yro3NaXFtCkt0mModLtWqD6FKZB7pbTn8Xj1Hy9dPliWNCsOjGHFWuTIdWXpU145ckOn1Uo/Qdv3OSYj//2Q=="
              alt="">
          <p class="fun_b_font">退出答题</p></div>
        <div class="fun_b_f"><img
            src="data:image/png;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAMCAgMCAgMDAwMEAwMEBQgFBQQEBQoHBwYIDAoMDAsKCwsNDhIQDQ4RDgsLEBYQERMUFRUVDA8XGBYUGBIUFRT/2wBDAQMEBAUEBQkFBQkUDQsNFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBT/wAARCAAfABsDASIAAhEBAxEB/8QAHAAAAQQDAQAAAAAAAAAAAAAABgADBAcBAgUI/8QALhAAAQMDAwMCAwkAAAAAAAAAAQIDBAUGEQAHIRIxQVFhE0JxFDI1cnWBkbHC/8QAFQEBAQAAAAAAAAAAAAAAAAAAAAX/xAAiEQABAwIHAQEAAAAAAAAAAAABAAMEAiESEzEyQUJhEVH/2gAMAwEAAhEDEQA/APctq2UN0V3BXajV6nFqrdTfjQ3okgoEVCMBISntjnn1+uTrts3zcO27qYl7xzUKTnpauKC2SkDx8dscpPuP2zydSdifwCv/AK5L/tOrHeZbkNLadQl1pYKVIWMpUD3BHnREzTqlEq8NqXCktS4royh5lYUlQ9iNSdVlUdsajaUx2q2DMTT1rPW/RJJJhyPyj5FfT6cDSpO/tuvQGzVVLpVSSVNyYS0lRacSopUMjvyNETOzdTiUa0rlmzpDcSIzWpanHnVdKUjKe50L17fSsXTPUi0AzS6FDcBl12poAaIz90A9s+EgFZ8Aa0kCda7VxWRLthNwJq09yVEJmJaadStSVJ6+QpJSUg8Y5HfzoptDZNpH2WXdK2Ki7HGY1Jjo6IET2Sj5z6qV385POoU+JNmO0ttvZbPbDvJ+6A6Uj0X4srUKVEiNVOVtZjvGLYB+kdj4bc3SXdVz7pKLNqtLt+3lHC67Lbw68Ox+Aj/R/kHjRJRNprZotKjwzTmpymgeqTKSFuuKJJKlH1JJ0YJSEJCUgJSBgADAGs6uqKv/2Q=="
            alt="">
          <p class="fun_b_font">暂停</p></div>
        <div class="fun_b_f"><img
            src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB8AAAAfCAYAAAAfrhY5AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA0ppVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQ1IDc5LjE2MzQ5OSwgMjAxOC8wOC8xMy0xNjo0MDoyMiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6QkRCQTE1MzU4OUM3MTFFQUIxQjJCMzg2MjcyRjQ5MEQiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6QkRCQTE1MzQ4OUM3MTFFQUIxQjJCMzg2MjcyRjQ5MEQiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTkgKFdpbmRvd3MpIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9ImFkb2JlOmRvY2lkOnBob3Rvc2hvcDo5NTA0ZWEyZi05ZmVhLWM1NDctYWI5OC1lMmU3ZThjZDFiY2MiIHN0UmVmOmRvY3VtZW50SUQ9ImFkb2JlOmRvY2lkOnBob3Rvc2hvcDo5NTA0ZWEyZi05ZmVhLWM1NDctYWI5OC1lMmU3ZThjZDFiY2MiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz7gnjiJAAACSklEQVR42uSXTUhUURTHZ94MaqmFuPGjxIUimIIgKiqMIdQmTEWSFFxYIhgt1E3qUg1qIYjgxwjapoJ2UhKYIKgIaeBCRBcGaqAIiWQgKn71P/AfeTzmvhnhvZnAA7/3cd59/N+5595z73N6vV4HLRVUgXhw7rDHjsA3sCA3bjpbwWsQ5bDfusA78FLEc0EPo+0H28Bpk3AseAbqwbKId/NBLfgUgsiHwDpo1nDIpHNO0TgORFgovsHzXU3XxReKxps29MiZHDTdyHaa5CnbYnHt8hAuc/vx3Qcd7BpfrySCz8z9Pqfmlu4d8beBQoXOd9BpTK0/8cfggcF3E5Tx+gSMGsTl+ROQpRBP46w6CyT+CkzxK0VoAuyAanAb/AUzhnf+gEqQoRBfNQqrxEVw3OA7ALMBUviTXG3U/U8Dzmi9YClc4i12RR7Wbr+W4ofB5tzUGreagm47nDzou5QClaaKXArNCIjW+aR6jYESCyJfk6KjirycdfoeKJK1l/U5mtVv2s87FwEE9avmnlm3i/hXUEDROxSW3uizIPJbwKUS/w3yWS7z6JPFpCHIyAJZirQ3G+3JIFJ3L1vqGxaN9iSzqZYDfvAD3oNFpmKSy6dDkXMz9BZjJi55TQAfQR0o5bJYDJ5bEHmUb8C5/IxW+YFIBwO8l92LB7wAXyzIueyQNDfXb2MvTBC97XIrpCocV17QRHCejpoQlVYPzyvyBe3gKXgLHoFfFtR82TKdMpUuRurk/H5I/xs3/yAq+J/mCUHkx9wdf/gnwADz73Q2EoTWiQAAAABJRU5ErkJggg=="
            alt="">
          <p class="fun_b_font">计算器</p></div>-->
      </div>
    </div>
    <div class="ex_as">
      <div class="as_h"><img src="static/img/exam/exam-dati-datika.png" style="margin-right: 10px; margin-top: -2px;"> <span class="as_title">答题卡</span><span
          class="score_co">（试卷总分{{ examRecord.qstScore }}分）</span></div>
      <div class="as_br">
        <div class="as_br_box">
          <div class="as_box_md as_box_md_error"></div>
          <span class="as_b_font">答错</span></div>
        <div class="as_br_box">
          <div class="as_box_md as_box_md_right"></div>
          <span class="as_b_font">答对</span></div>
        <div class="as_br_box">
          <div class="as_box_md as_box_md1"></div>
          <span class="as_b_font">未做</span></div>
<!--        <div class="as_br_box">
          <div class="as_box_md as_box_md2"></div>
          <span class="as_b_font">标记</span></div>-->
      </div>
      <div class="as_all_ops">
        <div class="pb60">
          <div class="as_ops_box" v-for="(paperMiddle,index) in examInfo.list" :key="index">
            <div class="as_ops_md"><p class="as_o_h"><span class="as_o_title">【{{ paperMiddle.name }}】</span><span
                class="score_co">（共 <span class="c999">{{ paperMiddle.questionArr.length }}</span> 题，一共{{ paperMiddle.questionArr.length * paperMiddle.score }}分）</span></p>
              <div class="as_c_num">
<!--                <div class="as_ops_num as_ops_num1 as_ops_bg as_box_md_error">1</div>-->
<!--                <div class="as_ops_num as_ops_num1 as_ops_bg as_box_md_right">1</div>-->
                <div id="1" class="as_ops_num as_ops_num1" @click="go(qstmiddle.id)" :class="{'as_box_md_error':qstmiddle.qstStatus == 1,'as_box_md_right':qstmiddle.qstStatus == 2,'as_ops_bg':qstmiddle.signFlag}" v-for="(qstmiddle,index) in paperMiddle.questionArr" :key="index">{{ qstmiddle.num }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {course} from "@/api";

export default {
  name: 'courseListContent',
  data() {
    return {
      hour: 0,
      minute: 0,
      second: 0,
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
    goback1(){
      this.$emit('goback')
    },
    formatNum (num) {
      return num < 10 ? '0' + num : '' + num
    },
    init(){
      let hour = Math.floor((this.examRecord.testTime / 3600) % 24);
      let minute = Math.floor((this.examRecord.testTime / 60) % 60)
      let second = Math.floor(this.examRecord.testTime % 60)
      this.hour = isNaN(hour)?0:hour
      this.minute = isNaN(minute)?0:minute
      this.second = isNaN(second)?0:second
    }
  },
  mounted: function () {
    // this.init()
  },
  props:{
    examRecord: Object,
    examInfo: Object
  },
  watch: {
    examRecord: {
      deep: true,
      handler(newVal){
        this.init()
      }
    },
    examInfo: {
      deep: true,
      handler(newVal){
        console.log(newVal)
      }
    },
  },
}
</script>

<style lang="scss" scoped>
@import "../../../../public/static/exam-scss.scss";
</style>