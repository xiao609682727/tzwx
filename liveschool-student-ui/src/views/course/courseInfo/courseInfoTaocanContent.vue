<template>
  <div>
    <div class="course-info-menu ">
      <div class="w">
        <ul class="course-menu1" style="z-index: 2">
          <li><a @click="showFlag = 0" class="moco-change-big-btn" v-bind:class="{'active':showFlag == 0}" href="javascript:void(0)">课程介绍</a></li>
          <li><a @click="showFlag = 1" class="moco-change-big-btn" v-bind:class="{'active':showFlag == 1}" id="learnOn" href="javascript:void(0)">班级课程</a></li>
          <li><a @click="checkComment()" id="commentOn" class="moco-change-big-btn " v-bind:class="{'active':showFlag == 2}" href="javascript:void(0)">用户评价</a></li>
        </ul>
      </div>
    </div>

    <div class="course-info-main clearfix w">

      <div class="content-wrap clearfix">
        <div class="content" v-show="showFlag == 0">
          <!-- 课程简介 -->
          <div class="course-wrap" style="min-height: 560px" v-html="courseInfo.context">
          </div>
          <!-- 课程简介 end -->
        </div>
        <div class="content" v-show="showFlag == 1">
          <!-- 课程章节 -->
          <div class="course-chapters">
            <div class="chapter course-wrap " v-for="course in courseInfo.list" :key="course.id">
              <ul class="js-recom-course moco-aside-course " style="background:#fff">
                <li style="margin-bottom:0px" >
                  <div class="aside-course-img"><img :src="course.logo" onerror="this.src='/static/img/course-default.png'"></div>
                  <div class="aside-course-content" style="border: 0px">
                    <router-link target="_blank" :to="{path:'/course/courseInfo/'+course.courseId}" :title="course.courseName" class="aside-course-name" style="background:#fff">
                      {{course.courseName}}
                    </router-link>
                   <!-- <p class="aside-course-price"><span>￥{{course.currentPrice}}</span></p>-->
                  </div>
                  <button class="r moco-btn moco-btn-red preview-btn undis" v-trigger @click="showKpoint(course)" style="position: relative;top: -57px;display: none">立即学习</button>
                </li>
              </ul>
              <div v-for="kpoint in course.list" :key="kpoint.id">
                <!-- 章节标题 -->
                <h3>
                  {{kpoint.name}}
                </h3>
                <!-- 章节标题 end -->
                <!-- 章节小节 -->
                <ul class="video" style="padding-top:16px">
                  <li v-for="child in kpoint.children" :key="child.id">
                    <!-- 直播类型  start -->
                    <span v-if="course.sellType == 2" class="clearfix" >
                      <!-- 百家云直播 start -->
                      <span v-if="child.videoType=='baijiayunlive'">
                        <!--直播结束时间之前-->
                        <a v-if="course.sellType == 2 && child.liveEndTime > dateFormat('YYYY-mm-dd HH:MM:SS',new Date())" @click="gotoPlay(child)" href="javascript:void(0)" class="J-media-item">
                          <i class="iconfont icon-zhibo type"></i>
                          <span>
                            【直播】
                          </span>
                          <span>{{child.name}}</span>
                          <span>
                            <span style="color: #999;" v-if="child.liveEndTime <= dateFormat('YYYY-mm-dd HH:MM:SS',new Date())">（{{child.liveBeginTime|dataFormat('MM月dd日 hh:mm')}}-{{child.liveEndTime|dataFormat('hh:mm')}}  已结束）</span>
                            <span style="color: #999;" v-if="child.liveBeginTime > dateFormat('YYYY-mm-dd HH:MM:SS',new Date())">（{{child.liveBeginTime|dataFormat('MM月dd日 hh:mm')}}-{{child.liveEndTime|dataFormat('hh:mm')}} 未开始） </span>
                            <span style="color: #999;" v-if="child.liveBeginTime < dateFormat('YYYY-mm-dd HH:MM:SS',new Date())&&child.liveEndTime > dateFormat('YYYY-mm-dd HH:MM:SS',new Date())">（{{child.liveBeginTime|dataFormat('MM月dd日 hh:mm')}}-{{child.liveEndTime|dataFormat('hh:mm')}}）<span class="flag-orange js-expr-on">正在直播</span></span>
                          </span>
                          <i class="icon-tick-revert done" v-if="isLogin && child.complete==2"></i>
                          <i class="icon-half ing" v-if="isLogin && child.complete==1"></i>
                          <i class="icon-nolearn start" v-if="isLogin && child.complete==0"></i>
                        </a>
                        <!--直播结束时间之后，直播已经结束-->
                        <a v-if="course.sellType == 2 && child.liveEndTime <= dateFormat('YYYY-mm-dd HH:MM:SS',new Date())" @click="gotoPlay(child)" href="javascript:void(0)" class="J-media-item">
                          <i v-if="child.videoType=='baijiayunlive'&& child.openReplay==1" class="imv2-play_circle type"></i>
                          <i v-if="child.videoType=='baijiayunlive'&& child.openReplay==2" class="iconfont icon-zhibo type"></i>
                          <span v-if="child.videoType=='baijiayunlive'&& child.openReplay==1" >
                            【回放】
                          </span>
                          <span v-if="child.videoType=='baijiayunlive'&& child.openReplay==2" >
                            【直播】
                          </span>
                          <span v-if="child.videoType=='baijiayunlive'">{{child.name}}</span>

                          <span v-if="child.videoType=='baijiayunlive'&& child.openReplay==2" >
                            <span style="color: #999;" v-if="child.liveEndTime <= dateFormat('YYYY-mm-dd HH:MM:SS',new Date())">（{{child.liveBeginTime|dataFormat('MM月dd日 hh:mm')}}-{{child.liveEndTime|dataFormat('hh:mm')}}  已结束）</span>
                          </span>
                          <i class="icon-tick-revert done" v-if="isLogin && child.complete==2"></i>
                          <i class="icon-half ing" v-if="isLogin && child.complete==1"></i>
                          <i class="icon-nolearn start" v-if="isLogin && child.complete==0"></i>
                        </a>
                      </span>
                      <!-- 百家 end -->
                      <!-- 阿里云直播 start -->
                      <span v-if="child.videoType=='aliyunlive'">
                        <a @click="gotoPlay(child)" href="javascript:void(0)" class="J-media-item">
                          <i v-if="child.videoType=='aliyunlive'" class="iconfont icon-zhibo type"></i>
                          <span >
                            【直播】
                          </span>
                          <span >{{child.name}}</span>
                          <span v-if="child.videoType=='aliyunlive'" >
                            <span style="color: #999;" v-if="child.liveEndTime <= dateFormat('YYYY-mm-dd HH:MM:SS',new Date())">（{{child.liveBeginTime|dataFormat('MM月dd日 hh:mm')}}-{{child.liveEndTime|dataFormat('hh:mm')}}  已结束）</span>
                            <span style="color: #999;" v-if="child.liveBeginTime > dateFormat('YYYY-mm-dd HH:MM:SS',new Date())">（{{child.liveBeginTime|dataFormat('MM月dd日 hh:mm')}}-{{child.liveEndTime|dataFormat('hh:mm')}} 未开始） </span>
                            <span style="color: #999;" v-if="child.liveBeginTime < dateFormat('YYYY-mm-dd HH:MM:SS',new Date())&&child.liveEndTime > dateFormat('YYYY-mm-dd HH:MM:SS',new Date())">（{{child.liveBeginTime|dataFormat('MM月dd日 hh:mm')}}-{{child.liveEndTime|dataFormat('hh:mm')}}）<span class="flag-orange js-expr-on">正在直播</span></span>
                          </span>
                          <i class="icon-tick-revert done" v-if="isLogin && child.complete==2"></i>
                          <i class="icon-half ing" v-if="isLogin && child.complete==1"></i>
                          <i class="icon-nolearn start" v-if="isLogin && child.complete==0"></i>
                        </a>
                      </span>
                      <!-- 阿里云直播 end -->
                      <!-- 阿里云直播回放 start -->
                      <span v-if="child.videoType=='aliyunReplay'">
                        <a @click="gotoPlay(child)" href="javascript:void(0)" class="J-media-item">
                          <i v-if="child.videoType=='aliyunReplay'" class="imv2-play_circle type"></i>
                          <span v-if="child.videoType=='aliyunReplay'" >
                            【回放】
                          </span>
                          <span>{{child.name}}</span>
                          <i class="icon-tick-revert done" v-if="isLogin && child.complete==2"></i>
                          <i class="icon-half ing" v-if="isLogin && child.complete==1"></i>
                          <i class="icon-nolearn start" v-if="isLogin && child.complete==0"></i>
                        </a>
                      </span>
                      <!-- 阿里云直播回放 end -->
                      <!-- 其他类型直播 start -->
                      <span v-if="child.videoType=='otherlive'">
                        <a @click="gotoPlay(child)" href="javascript:void(0)" class="J-media-item">
                          <i class="iconfont icon-zhibo type"></i>
                          <span>
                            【直播】
                          </span>
                          <span>{{child.name}}</span>
                          <span>
                            <span style="color: #999;" v-if="child.liveEndTime <= dateFormat('YYYY-mm-dd HH:MM:SS',new Date())">（{{child.liveBeginTime|dataFormat('MM月dd日 hh:mm')}}-{{child.liveEndTime|dataFormat('hh:mm')}}  已结束）</span>
                            <span style="color: #999;" v-if="child.liveBeginTime > dateFormat('YYYY-mm-dd HH:MM:SS',new Date())">（{{child.liveBeginTime|dataFormat('MM月dd日 hh:mm')}}-{{child.liveEndTime|dataFormat('hh:mm')}} 未开始） </span>
                            <span style="color: #999;" v-if="child.liveBeginTime < dateFormat('YYYY-mm-dd HH:MM:SS',new Date())&&child.liveEndTime > dateFormat('YYYY-mm-dd HH:MM:SS',new Date())">（{{child.liveBeginTime|dataFormat('MM月dd日 hh:mm')}}-{{child.liveEndTime|dataFormat('hh:mm')}}）<span class="flag-orange js-expr-on">正在直播</span></span>
                          </span>
                          <i class="icon-tick-revert done" v-if="isLogin && child.complete==2"></i>
                          <i class="icon-half ing" v-if="isLogin && child.complete==1"></i>
                          <i class="icon-nolearn start" v-if="isLogin && child.complete==0"></i>
                        </a>
                      </span>
                      <!-- 其他类型直播 end -->
                      <!-- iframe类型直播 start -->
                      <span v-if="child.videoType=='iframelive'">
                        <a @click="gotoPlay(child)" href="javascript:void(0)" class="J-media-item">
                          <i class="iconfont icon-zhibo type"></i>
                          <span>
                            【直播】
                          </span>
                          <span>{{child.name}}</span>
                          <span>
                            <span style="color: #999;" v-if="child.liveEndTime <= dateFormat('YYYY-mm-dd HH:MM:SS',new Date())">（{{child.liveBeginTime|dataFormat('MM月dd日 hh:mm')}}-{{child.liveEndTime|dataFormat('hh:mm')}}  已结束）</span>
                            <span style="color: #999;" v-if="child.liveBeginTime > dateFormat('YYYY-mm-dd HH:MM:SS',new Date())">（{{child.liveBeginTime|dataFormat('MM月dd日 hh:mm')}}-{{child.liveEndTime|dataFormat('hh:mm')}} 未开始） </span>
                            <span style="color: #999;" v-if="child.liveBeginTime < dateFormat('YYYY-mm-dd HH:MM:SS',new Date())&&child.liveEndTime > dateFormat('YYYY-mm-dd HH:MM:SS',new Date())">（{{child.liveBeginTime|dataFormat('MM月dd日 hh:mm')}}-{{child.liveEndTime|dataFormat('hh:mm')}}）<span class="flag-orange js-expr-on">正在直播</span></span>
                          </span>
                          <i class="icon-tick-revert done" v-if="isLogin && child.complete==2"></i>
                          <i class="icon-half ing" v-if="isLogin && child.complete==1"></i>
                          <i class="icon-nolearn start" v-if="isLogin && child.complete==0"></i>
                        </a>
                      </span>
                      <!-- iframe类型直播 end -->
                    </span>
                    <!-- 直播类型  end -->
                    <!-- 点播类型  start -->
                    <span v-if="course.sellType == 1" class="clearfix">
                      <a @click="gotoPlay(child)"  href="javascript:void(0)" class="J-media-item">
                        <i class="imv2-play_circle type"></i>
                        【点播】{{child.name}} ({{child.videoTimeFmt}})
                        <i class="icon-tick-revert done" v-if="isLogin && child.complete==2"></i>
                        <i class="icon-half ing" v-if="isLogin && child.complete==1"></i>
                        <i class="icon-nolearn start" v-if="isLogin && child.complete==0"></i>
                      </a>
                    </span>
                    <!-- 点播类型  end -->
                  </li>
                </ul>
                <!-- 章节小节 end -->
              </div>
            </div>
          </div>
          <!-- 课程章节 end -->
        </div><!--content end-->

        <!-- 评论列表  start -->
        <div class="content" v-show="showFlag == 2">
          <div class="evaluate clearfix" v-show="commentFlag">
            <div class="evaluate-item clearfix">
              <div class="evaluate-title">
                评分：
              </div>
              <ul>
                <li>
                  <h3></h3>
                  <div id="cType1" data-field="内容实用" class="star-box js-star">
                    <i @mouseover="score = 2" class="icon-star2 " v-bind:class="{'active':score >= 2}"></i>
                    <i @mouseover="score = 4" class="icon-star2" v-bind:class="{'active':score >= 4}"></i>
                    <i @mouseover="score = 6" class="icon-star2 " v-bind:class="{'active':score >= 6}"></i>
                    <i @mouseover="score = 8" class="icon-star2 " v-bind:class="{'active':score >= 8}"></i>
                    <i @mouseover="score = 10" class="icon-star2 " v-bind:class="{'active':score == 10}"></i>
                    <div class="star-tip"></div>
                  </div>
                </li>
              </ul>
            </div>
            <div class="evaluate-item clearfix">
              <a href="javascript:void(0)" class="user-head l">
                <img :src="studentInfo.headImg" onerror="this.src='/static/img/user-avatar.png'" :alt="studentInfo.showName" class="js-textarea-head">
              </a>
              <div class="textarea-box">
                <textarea @keyup="computeWordCount()" maxlength="300" v-model="commentContent"  name="evaluation" id="evaluation" placeholder="写下你的对该课程的评价..."></textarea>
                <span class="num-tip"><em>{{wordCount}}</em>/300</span>
              </div>
            </div><!--textarea-box end-->
            <div class="ctrl-box clearfix">
              <div @click="addComment()" class="moco-btn moco-btn-red moco-btn-lg release-evaluation r">提交</div>
              <div class="errtip l"></div>
            </div><!--ctrl-box end-->
          </div><!--evaluate end-->
          <div class="evaluate-succ" v-show="tipFlag">
            <i class="icon-tick2"></i>评价成功，感谢您对本课程的支持！
          </div><!--evaluate-succ end-->

          <div class="evaluation-list">
            <div class="evaluate" v-for="comment in commentList" :key="comment.id">
              <div class="evaluation-con">
                <a href="javascript:void(0)" class="img-box">
                  <img :src="comment.headImg" onerror="this.src='/static/img/user-avatar.png'" :alt="comment.showName">
                </a>
                <div class="content-box">
                  <div class="user-info clearfix">
                    <a href="javascript:void(0)" class="username">{{comment.showName}}</a>
                    <div class="star-box">
                      <i class="icon-star2 " v-bind:class="{'active':comment.commentScore >= 2}"></i>
                      <i class="icon-star2 " v-bind:class="{'active':comment.commentScore >= 4}"></i>
                      <i class="icon-star2 " v-bind:class="{'active':comment.commentScore >= 6}"></i>
                      <i class="icon-star2 " v-bind:class="{'active':comment.commentScore >= 8}"></i>
                      <i class="icon-star2 " v-bind:class="{'active':comment.commentScore >= 10}"></i>
                      <span>{{comment.commentScore}}分</span>
                    </div>
                  </div><!--user-info end-->
                  <p class="content">{{comment.content}}</p>
                  <div class="info clearfix">

                    <span class="time r">时间：{{comment.addtime}}</span>
                  </div>
                </div><!--content end-->
              </div><!--evaluation-con end-->
              <div class="do-reply" v-if="false">
                <div class="textarea-box">
                  <textarea name="teacher-reply" class="teacher-do-reply" placeholder="写下你对评价的回复..."></textarea>
                  <span class="num-tip"><em>0</em>/300</span>
                </div><!--textarea-box end-->
                <div class="ctrl clearfix">
                  <p class="errtip"></p>
                  <div class="btn-box">
                    <div class="moco-btn cancel">取消</div>
                    <div class="moco-btn moco-btn-red sure" data-comment-id="144753">回复</div>
                  </div>
                </div><!--ctrl end-->
              </div><!--do-reply end-->
            </div><!--evaluation end-->

            <page-component :page="page" @goto="goto" v-show="commentList.length >0" ></page-component>
            <div class="nodata" v-if="commentList.length == 0">
              <p><i class="imv2-error_c"></i></p>
              <p>还没有评论哦！</p>
            </div>
          </div>
        </div>
        <!-- 评论列表  end -->

        <div class="aside r">
          <div class="course-wrap course-aside-info js-usercard-box">
            <div class="course-card-price">
              <div  class="price-box tac">
                <span class="price fs20" v-if="courseInfo.currentPrice != '0.00'">价格</span>
                <span class="price red fs20" v-if="courseInfo.currentPrice == '0.00'" >免费</span>
                <span class="price red fs20" v-if="courseInfo.currentPrice != '0.00'" >￥{{courseInfo.currentPrice}}</span>
                <span class="cost-price">￥{{courseInfo.sourcePrice}}</span>
              </div>
            </div>
            <div class="learn-btn mt10">
              <!--              <a @click="gotoPlay(obj)" href="javascript:void(0)" class="moco-btn moco-btn-red moco-btn-lg J-learn-course" v-if="isHave">开始学习</a>-->
              <a @click="gotoPlay(obj)" href="javascript:void(0)" class="moco-btn moco-btn-red moco-btn-lg J-learn-course" v-if="isHave">开始学习</a>
              <a @click="toPay(courseInfo)" href="javascript:void(0)" class="moco-btn moco-btn-red moco-btn-lg J-learn-course" v-if="!isHave">立即购买</a>
            </div>
            <div class="course-info-tip" v-html="courseInfo.title">
            </div>
          </div>

          <div id="coursechapterlistRight" style="display: block;margin-top: 20px;" v-for="img in courseInfoImgList" :key="img.id">
            <div>
              <a :href="img.linkAddress!= ''?img.linkAddress:'javascript:void(0)'" target="_blank"><img :src="img.imageUrl"></a>
            </div>
            <div></div>
          </div>

          <div class="js-related-box">
            <div class="mb40 recom-course-list-box"><h4>相关课程</h4>
              <ul class="js-recom-course moco-aside-course clearfix">
                <li v-for="course in courseInfoList" :key="course.id">
                  <div class="aside-course-img"><img :src="course.logo" onerror="this.src='/static/img/course-default.png'"></div>
                  <div class="aside-course-content"><router-link :to="{path:'/course/courseInfo/'+course.id}" class="aside-course-name"
                                                                 target="_blank">{{course.courseName}}</router-link>
                    <p class="aside-course-people">
                      <i class="imv2-set-sns"></i>
                      <span v-if="course.bogusBuycount == 0">{{course.pageBuycount}}</span>
                      <span v-if="course.bogusBuycount != 0">{{course.bogusBuycount+course.pageBuycount}}</span>
                    </p>
                  </div>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
      <div class="clear"></div>
    </div>
  </div>

</template>

<script>
  import { setStore, getStore } from '@/util/store'
  import {getBannerList,course,commentList,checkComment} from "@/api";
  import {getDetail,checkCourse,createFreeCourseOrder,commentSubmit,createCourseOrder} from "@/api/user";
  import {courseInfo} from "@/api";
  import { mapGetters } from "vuex";
  import pageComponent from '@/views/common/pageComponent'

  export default {
    name: 'courseInfoContent',
    data() {
      return {
        isHave:false,
        obj:{},
        courseInfoImgList:getStore({ name: 'courseInfoImgList' }) || [],
        courseInfoList:getStore({ name: 'courseInfoList' }) ||[],
        showFlag:0,
        commentList:[],
        commentCount:0,
        current:1,
        size:20,
        page:{},
        score:0,
        commentContent:"",
        wordCount:0,
        commentFlag:false,
        tipFlag:false,
      }
    },
    methods: {
      checkComment(){
        let that = this
        //如果没有登录是不能评论的
        if(!this.studentInfo){
          that.showFlag = 2
          return;
        }
        if(this.studentInfo){
          if(!this.studentInfo.id){
            that.showFlag = 2
            return;
          }
        }

        let params = {
          "courseId":this.courseInfo.id,
        }
        checkComment(params).then(res=>{
          that.showFlag = 2
          if(res.data.data){
            that.commentFlag = true
          }else{
            that.commentFlag = false
          }
        })
      },
      goto(i){
        this.current = i
        //查询评论
        let params = {
          "current": this.current,
          "size": this.size,
          "otherId":this.courseInfo.id,
          "type":2
        }
        commentList(params).then(res=>{
          this.commentList = res.data.data.records
          this.page = res.data.data
          this.commentCount = res.data.data.total
        })
      },
      computeWordCount(){
        this.wordCount = this.commentContent.length
      },
      showKpoint(course){
        let that = this
        //查询班级下的章节信息
        courseInfo(course.courseId,this.courseInfo.id).then(res =>{
          course.list = res.data.data.list
          that.$forceUpdate();
        })
      },
      toPay(courseInfo){
        //判断课程是否过期
        if(this.courseInfo.losetype == '0'){
          if(Date.parse(this.courseInfo.endTime) < new Date()){
            this.$layer.alert(
              "课程已过期，请学习其他课程",
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
        }
        // this.$router.push({ path: '/course/confirmOrder/'+courseInfo.id })

        let courseId = this.courseInfo.id;
        let studentId = this.studentInfo.id
        let param = {
          courseId: courseId,
          payType: "",
          reqChannel:"web"
        }
        createCourseOrder(param).then(res =>{
          let order = res.data.data
          this.$router.push({ path: '/course/payCenter/'+order.id })
        })
      },
      gotoPlay(obj){
        if(obj == null){
          this.$layer.alert(
            "暂无课程",
            {
              shade: true,
              title: "提示"
            },
            laeryid => {
              this.$layer.close(laeryid);
            }
          );
          return
        }
        //判断课程是否过期
        if(this.courseInfo.losetype == '0'){
          if(Date.parse(this.courseInfo.endTime) < new Date()){
            this.$layer.alert(
              "课程已过期，请学习其他课程",
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
        }
        if(!this.isHave){
          this.$layer.alert(
            "您还未拥有课程",
            {
              shade: true,
              title: "提示"
            },
            laeryid => {
              this.$layer.close(laeryid);
            }
          );
          return
        }
        //判断日期 如果是直播 则进行时间判断 如果是回放则不进行时间判断
        if(obj.videoType == 'baijiayunlive'){
          //修改时间提前10分钟进场
          let d = new Date();
          let addDate = d.setMinutes(d.getMinutes()+5);
          new Date(addDate)
          let ti = this.dateFormat("YYYY-mm-dd HH:MM:SS",new Date(addDate) );
          if(obj.liveBeginTime > ti){
            this.$layer.alert(
              "课程还未开始，可提前5分钟进入。",
              {
                shade: true,
                title: "提示"
              },
              laeryid => {
                this.$layer.close(laeryid);
              }
            );
            return
          }
          ti = this.dateFormat("YYYY-mm-dd HH:MM:SS",new Date());
          if(obj.liveEndTime < ti){
            if(obj.openReplay==2){
              this.$layer.alert(
                "课程已结束",
                {
                  shade: true,
                  title: "提示"
                },
                laeryid => {
                  this.$layer.close(laeryid);
                }
              );
              return
            }
          }
        }
        //判断日期 如果是直播 则进行时间判断 如果是回放则不进行时间判断
        if(obj.videoType == 'polyvlive'||obj.videoType == 'aliyunlive'||obj.videoType == 'otherlive'){
          //修改时间提前10分钟进场
          let d = new Date();
          let addDate = d.setMinutes(d.getMinutes()+5);
          new Date(addDate)
          let ti = this.dateFormat("YYYY-mm-dd HH:MM:SS",new Date(addDate) );
          if(obj.liveBeginTime > ti){
            this.$layer.alert(
              "课程还未开始，可提前5分钟进入。",
              {
                shade: true,
                title: "提示"
              },
              laeryid => {
                this.$layer.close(laeryid);
              }
            );
            return
          }
          ti = this.dateFormat("YYYY-mm-dd HH:MM:SS",new Date());
          if(obj.liveEndTime < ti){
            this.$layer.alert(
              "课程已结束",
              {
                shade: true,
                title: "提示"
              },
              laeryid => {
                this.$layer.close(laeryid);
              }
            );
            return
          }
        }
        //判断当课程为免费时创建课程订单
        if(this.courseInfo.currentPrice == "0.00"){
          let param = {
            courseId: this.courseInfo.id,
            payType: "",
            reqChannel:"web"
          }
          createFreeCourseOrder(param).then(res=>{
            console.log(res)
          })
        }

        this.$router.push({ path: '/uc/player/'+obj.id,query:{"taocanId":this.courseInfo.id} })

        /*if(obj.videoType == "baijiayunlive"){
          getDetail(obj.id,"student").then(res=>{
            window.open(res.data.data.params.url);
          })
        }else if(obj.videoType == "baijiayunReplay"){
          getDetail(obj.id,"student").then(res=>{
            window.open(res.data.data.params.url);
          })
        }else{
          this.$router.push({ path: '/uc/player/'+obj.id })
        }*/
      },
      dateFormat(fmt, date){
        let ret;
        const opt = {
          "Y+": date.getFullYear().toString(),        // 年
          "m+": (date.getMonth() + 1).toString(),     // 月
          "d+": date.getDate().toString(),            // 日
          "H+": date.getHours().toString(),           // 时
          "M+": date.getMinutes().toString(),         // 分
          "S+": date.getSeconds().toString()          // 秒
          // 有其他格式化字符需求可以继续添加，必须转化成字符串
        };
        for (let k in opt) {
          ret = new RegExp("(" + k + ")").exec(fmt);
          if (ret) {
            fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
          }
        }
        return fmt;
      },
      init(){
        //查询广告位
        if(this.courseInfoImgList.length == 0){
          getBannerList("2").then(res => {
            this.courseInfoImgList = res.data.data;
            setStore({ name: 'courseInfoImgList', content: res.data.data,type: 'session' })
          });
        }
        //查询相关课程
        if(this.courseInfoList.length == 0){
          course(1,5,"",1,this.courseInfo.subject.id,null).then(res =>{
            this.courseInfoList = res.data.data.records;
            setStore({ name: 'courseInfoList', content: res.data.data.records,type: 'session' })
          })
        }
        //查询评论
        let params = {
          "current": this.current,
          "size": this.size,
          "otherId":this.courseInfo.id,
          "type":2
        }
        commentList(params).then(res=>{
          this.commentList = res.data.data.records
          this.page = res.data.data
          this.commentCount = res.data.data.total
        })
        let klist = this.courseInfo.list
        courseInfo(klist[0].courseId).then(res =>{
          let courseInfo1 = res.data.data
          let klist1 = courseInfo1.list
          if(klist1.length >0){
            let obj = klist1[0]
            let child = obj.children
            if(child.length > 0){
              this.obj = child[0]
            }
          }
        })

        if(this.courseInfo.currentPrice == "0.00"){
          this.isHave = true;
          return
        }
        //判断用户是否拥有此课程
        if(this.isLogin){
          let param = {
            courseId:this.courseInfo.id,
          }
          checkCourse(param).then(res =>{
            this.isHave = res.data.data
            if(this.isHave){
              this.showFlag = 1
            }
          })
        }


      },
      addComment(){
        if(this.commentContent.length == 0){
          this.$layer.alert(
            "请填写评论内容",
            {
              shade: true,
              title: "提示"
            },
            laeryid => {
              this.$layer.close(laeryid);
            }
          );
          return
        }
        let that = this
        let params = {
          parentId:0,
          content:this.commentContent,
          otherId:this.courseInfo.id,
          praiseCount:0,
          replyCount:0,
          type:2,
          roleType:"user",
          commentScore:this.score,
          addTime:new Date()
        }
        commentSubmit(params).then(res=>{
          that.commentFlag = false
          that.tipFlag = true
          that.commentList = []
          //清空评论进行查询
          setTimeout(function () {
            that.tipFlag = false
            //查询评论
            let params = {
              "current": that.current,
              "size": that.size,
              "otherId":that.courseInfo.id,
              "type":2
            }
            commentList(params).then(res=>{
              that.commentList = res.data.data.records
              that.page = res.data.data
              that.commentCount = res.data.data.total
            })
          },3000)
        })
      }
    },
    props:{
      courseInfo: Object,
    },
    watch: {
      courseInfo: {
        deep: true,
        handler(newVal){

        }
      }
    },
    mounted:function() {
      this.init()
    },
    computed: {
      ...mapGetters(["studentInfo","isLogin"]),
    },
    components: {
      pageComponent
    },directives:{
      trigger:{
        inserted(el,binging){
          el.click()
        }
      }
    }
  }
</script>

<style scoped>
  .course-chapters .chapter .video li a .type{
    top: 4px;
  }
</style>
