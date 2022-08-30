<template>
  <div class="u-container">
    <div class="c-tab clearfix">
      <div class="tool-left l">
        <a href="javascript:void(0)" @click="openActive(1)" class="sort-item">做题记录
        </a>
        <a href="javascript:void(0)" @click="openActive(2)" class="sort-item active">错题本
        </a>
      </div>
    </div>

    <div class="all-course-main" v-show="detailList.length >0">
      <div class="allcourse-content js-course-list ">
        <div class="system_wrap">
          <div class="system_list">

            <div class="cl_new_box"  v-for="(error,index) in detailList" :key="index">
              <div class="cl_n_l float_left"><p class="cl_n_ops">【{{getQuesType(error.qstType)}}】</p>
                <p class="cl_n_title" v-html="error.qstContent"></p>
<!--                <p class="cl_n_cord">做错 <span class="rc_cord_bg">2</span> 次，累计作答-->
<!--                  <span class="rc_cord_bg1">2</span> 次</p>-->
<!--                <p  v-for="(option,index) of toObject(error.optionList)" :key="index">{{indexToString(index)}}<span v-html="option.value" ></span></p>-->
              </div>
              <div class="cl_n_r float_right">
                <p class="cl_n_remove" @click="remove(error.id)">
                  <span><img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABIAAAASCAYAAABWzo5XAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA0ppVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQ1IDc5LjE2MzQ5OSwgMjAxOC8wOC8xMy0xNjo0MDoyMiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6Njc4MkREN0I4OUM0MTFFQUE5NzI5OENGQzk2RUE3NTkiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6Njc4MkREN0E4OUM0MTFFQUE5NzI5OENGQzk2RUE3NTkiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTkgKFdpbmRvd3MpIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9ImFkb2JlOmRvY2lkOnBob3Rvc2hvcDo5NTA0ZWEyZi05ZmVhLWM1NDctYWI5OC1lMmU3ZThjZDFiY2MiIHN0UmVmOmRvY3VtZW50SUQ9ImFkb2JlOmRvY2lkOnBob3Rvc2hvcDo5NTA0ZWEyZi05ZmVhLWM1NDctYWI5OC1lMmU3ZThjZDFiY2MiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4HhwIcAAABS0lEQVR42qzTu0oDQRiG4U02IZWoEaMgeAIrxTKIWNiL3oRFSkGwTWOjYCEWroWQCxARsbAQQRvt9BIECxUJHgpB8RDfgW9hGGeTVfzhYf6ZyX472WUzURQFnppHFUWE8oUnrGPVvSAX+GsbezhXiPndO4awghru3aAtjOARdUxorx2Dzg26NF7iQPMSrk1QH8bQjYzCTjGMcSfoTXsDqGjtFh8maFYLc9hXYJpqYAlrZpK1Njo1FoP01RY3dlDcP2MDm5qb17qjfhm7nmu8b+0TMxpNTaFf/SSmfUdLev03egaBXnNefV17Pyob/FM1C8qkXPvTiRpJG0nPKHRuFlrrud8Elay+B73qOzRvGpS3TlCx/vYCCuqr+vrjKviCXqyP9dhaP7L6M+cgr76gQ4Xd4aTFQy9rrPmCHjCqo5dbBF1gEVfxwrcAAwAPTzcaZzXSnAAAAABJRU5ErkJggg==" class="remove_img">移除</span>
              </p>
<!--                <div class="rc_btn_comm1 cl_n1">回顾错题</div>-->
                <router-link class="pic-a-n" :to="{path:'/exam/examErrorQuestionInfo',query:{current:index+1}}" target="_blank">
                  <div class="rc_btn_comm1 cl_n2" >查看解析</div>
                </router-link>
              </div>
              <div style="clear: both"></div>
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
import {getErrorquestion,removeErrorquestion} from "@/api/exam";
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
      courseType: ""
    }
  },
  methods: {

    goto(i) {
      this.current = i
      this.getList();
    },
    getList() {
      if (typeof (this.studentInfo.id) == "undefined") {
        return;
      }
      let params = {
        current: this.current,
        size: this.size,
      }
      getErrorquestion(params).then(res => {
        this.detailList = res.data.data.records
        this.page = res.data.data
      })
    },
    remove(id) {
      this.$layer.confirm(
        "确定要删除吗？",
        {
          title: "删除试题",
          btn: ["确定", "取消"]
        },
        layerid => {
          // this.$layer.msg("执行了删除");
          removeErrorquestion(id).then(res => {
            this.current = 1
            this.getList();
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
    indexToString(index){
      var charcode;
      return index.toString(26).split("").map(function(item,i){
        charcode = item.charCodeAt(0);
        charcode+=(charcode>=97?10:49);
        return String.fromCharCode(charcode)
      }).join("").toUpperCase();
    },
    openActive(i){
      this.$emit('active',i)
    },
    goErrorInfo(i){
      i = (this.current - 1) * this.size + i
      this.$router.push({ path: "/exam/examErrorQuestionInfo",query:{current:i}});
    }
  },
  mounted: function () {
    this.courseType = this.$route.query.courseType;
    this.getList();
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
.system_wrap {
  min-height: 500px;
}
.system_list {
  min-height: 150px;
  padding-top: 10px;
}
.float_left {
  float: left;
}
.float_right {
  float: right;
}
.cl_new_box {
  margin-top: 20px;
  border: 1px solid #e6e6e6;
  border-radius: 10px;
}
.cl_n_l {
  width: 75%;
  margin-left: 31px;
  border-right: 1px solid #e6e6e6;
  min-height: 225px;
}
.cl_n_ops {
  color: #0091ff;
  margin: 25px 0 0;
  font-size: 16px;
}
.cl_n_title {
  margin-top: 33px;
  color: #000;
  font-size: 16px;
}
.cl_n_cord {
  color: #999;
  font-size: 14px;
  margin-top: 90px;
}
.rc_cord_bg {
  color: #0091ff;
}
.cl_n_r {
  width: 181px;
  height: 100%;

}
.cl_n_remove {
  text-align: center;
  color: #666;
  font-size: 14px;
  margin-top: 54px;
  cursor: pointer;
}
.cl_n_remove:hover {
  color: #0091ff;
}
.remove_img {
  margin-right: 6px;
  width: 13px;
  height: 13px;
  margin-top: -2px;
}
.cl_n1, .cl_n2 {
  margin: 19px auto 0;
}
.rc_btn_comm1 {
  width: 96px;
  height: 32px;
  background: #f5f5f5;
  border: 1px solid #e6e6e6;
  border-radius: 4px;
  text-align: center;
  line-height: 32px;
  color: #333;
  font-size: 14px;
  cursor: pointer;
}
.rc_btn_comm1:hover {
  background-color: #0091ff;
  color: #fff;
}
</style>
