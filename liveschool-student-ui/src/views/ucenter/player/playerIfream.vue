<template>
   <iframe :src="url" frameborder="0" allow="microphone;camera;midi;encrypted-media; " allowfullscreen="true" style="width:100%;height:100%;position: fixed"></iframe>
</template>
<script>
  import {courseInfo,checkHaveCourse} from "@/api";
  import {addCoursestudyhistory,getDetail,updateCoursestudyhistory} from "@/api/user";

  export default {
    name: 'playerContent',
    data() {
      return {
        sectionListFlag:false,
        url:"",
        childertiems:0,
      }
    },
    components: {
      // eslint-disable-next-line vue/no-unused-components
      'remote-css': {
        render(createElement) {
          return createElement('link', { attrs: { rel: 'stylesheet', href: this.href }});
        },
        props: {
          href: { type: String, required: true },
        },
      },
      // eslint-disable-next-line vue/no-unused-components
      'remote-js': {
        render(createElement) {
          return createElement('script', { attrs: { type: 'text/javascript', src: this.src }});
        },
        props: {
          src: { type: String, required: true },
        },
      },
    },
    props:{
      courseKpoint: Object,
      courseInfo: Object,
      times:Number
    },
    watch: {
      courseKpoint: {
        deep: true,
        handler(newVal){
          this.init();
        }
      },
      times: {
        deep: true,
        handler(newVal){
          this.childertiems=newVal;
        }
      },
    },
    methods: {
      showKpoint(){
        this.sectionListFlag = !this.sectionListFlag
      },
      init(){
        let that = this
        checkHaveCourse(this.courseKpoint.courseId).then(res =>{
          let checkCourseFlag = res.data.data;
            if(checkCourseFlag == false){
              this.$layer.alert(
                "您还未拥有此课程",
                {
                  shade: true,
                  title: "提示"
                },
                laeryid => {
                  this.$layer.close(laeryid);
                }
              );
              return;
            }
            //直播学习记录添加  按照时间计算完成
          let taocanId = this.$route.query.taocanId;
          let courseType=1;
          if(taocanId!=undefined&&taocanId!=''&&taocanId!=0){
            courseType=2;
          }else {
            taocanId=0;
          }
          let param = {
            courseId:this.courseKpoint.courseId,
            kpointId:this.courseKpoint.id,
            courseName:this.courseInfo.courseName,
            kpointName:this.courseKpoint.name,
            kpointCourseId:taocanId,
            courseType:courseType,
            complete:1
          }
          addCoursestudyhistory(param).then(res =>{
            //console.log("创建播放记录完成")
          })

            if(that.courseKpoint.videoType == "baijiayunlive"||that.courseKpoint.videoType == "baijiayunReplay"){
              getDetail(that.courseKpoint.id,"student").then(res=>{
                that.url = res.data.data.params.url
              }).catch(function (error) {
                console.log(error)
              });
            }else{
              that.url = that.courseKpoint.videoUrl
            }

        })
      },
      toPlay(id){
        let taocanIdc = this.$route.query.taocanId;
        let courseType=1;
        let courseId=this.courseKpoint.courseId;
        if(taocanIdc!=undefined&&taocanIdc!=''&&taocanIdc!=0){
          courseType=2;
        }else {
          taocanIdc=0;
        }
        let param = {
          courseId:this.courseKpoint.courseId,
          kpointId:this.courseKpoint.id,
          kpointCourseId:taocanIdc,
          courseType:courseType,
          watchStingTime:this.childertiems
        }
        updateCoursestudyhistory(param).then(res =>{
          this.childertiems=0;
        })
        this.$router.push('/uc/player/'+id)
        location.reload()
      },

    },
    mounted:function() {
      this.init();
    }
  }
</script>

<style scoped>
  iframe {
    margin: 0 auto;
    display: block;
    width: 100%;
    height: 100%;
  }

</style>
