<template>
  <div>
    <div class="course-info-menu">
      <div class="w">
        <ul class="course-menu1" style="z-index: 2">
          <li><a @click="showFlag = 0" class="moco-change-big-btn" v-bind:class="{'active':showFlag == 0}" href="javascript:void(0)">课程介绍</a></li>
          <li><a @click="showFlag = 1" class="moco-change-big-btn" v-bind:class="{'active':showFlag == 1}" id="learnOn" href="javascript:void(0)">课程章节</a></li>
          <li><a @click="checkComment()" id="commentOn" class="moco-change-big-btn " v-bind:class="{'active':showFlag == 2}" href="javascript:void(0)">用户评价</a></li>
        </ul>
      </div>
    </div>
    <div class="course-info-main clearfix w">
      <div class="content-wrap clearfix"  >
        <div class="content" v-show="showFlag == 0">
          <!-- 课程简介 -->
          <div class="course-wrap" v-html="courseInfo.context" style="min-height: 560px">
          </div>
          <!-- 课程简介 end -->
        </div>
        <div class="content" v-show="showFlag == 1">
          <!-- 课程章节 -->
          <div class="course-chapters">
            <div class="chapter course-wrap " v-for="courseKpoint in courseInfo.list" :key="courseKpoint.id">
              <!-- 章节标题 -->
              <h3>
                {{courseKpoint.name}}
              </h3>
              <!-- 章节标题 end -->
              <!-- 章节小节 -->
              <ul class="video">
                <li v-for="child in courseKpoint.children" :key="child.id" :class="(courseInfo.sellType == 2 && child.liveEndTime <= dateFormat('YYYY-mm-dd HH:MM:SS',new Date()))?'unclick':''">
                  <!-- 直播类型  start -->
                  <span v-if="courseInfo.sellType == 2" >
                    <!-- 百家云直播 start -->
                    <span v-if="child.videoType=='baijiayunlive'">
                      <!--直播结束时间之前-->
                      <a v-if="courseInfo.sellType == 2 && child.liveEndTime > dateFormat('YYYY-mm-dd HH:MM:SS',new Date())" @click="gotoPlay(child)" href="javascript:void(0)" class="J-media-item">
                        <i class="iconfont icon-zhibo type fl"></i>
                        <span class="fl">
                          【直播】
                        </span>
                        <span class="course-name">{{child.name}}</span>
                        <span class="fl">
                          <span style="color: #999;" v-if="child.liveEndTime <= dateFormat('YYYY-mm-dd HH:MM:SS',new Date())">（{{child.liveBeginTime|dataFormat('MM月dd日 hh:mm')}}-{{child.liveEndTime|dataFormat('hh:mm')}}  已结束）</span>
                          <span style="color: #999;" v-if="child.liveBeginTime > dateFormat('YYYY-mm-dd HH:MM:SS',new Date())">（{{child.liveBeginTime|dataFormat('MM月dd日 hh:mm')}}-{{child.liveEndTime|dataFormat('hh:mm')}} 未开始） </span>
                          <span style="color: #999;" v-if="child.liveBeginTime < dateFormat('YYYY-mm-dd HH:MM:SS',new Date())&&child.liveEndTime > dateFormat('YYYY-mm-dd HH:MM:SS',new Date())">（{{child.liveBeginTime|dataFormat('MM月dd日 hh:mm')}}-{{child.liveEndTime|dataFormat('hh:mm')}}）<span class="flag-orange js-expr-on">正在直播</span></span>
                        </span>
                        <i class="icon-tick-revert done" v-if="isLogin && child.complete==2"></i>
                        <i class="icon-half ing" v-if="isLogin && child.complete==1"></i>
                        <i class="icon-nolearn start" v-if="isLogin && child.complete==0"></i>
                      </a>
                      <!--直播结束时间之后，直播已经结束-->
                      <a v-if="courseInfo.sellType == 2 && child.liveEndTime <= dateFormat('YYYY-mm-dd HH:MM:SS',new Date())" @click="gotoPlay(child)" href="javascript:void(0)" class="J-media-item">
                        <i v-if="child.videoType=='baijiayunlive'&& child.openReplay==1" class="imv2-play_circle type fl"></i>
                        <i v-if="child.videoType=='baijiayunlive'&& child.openReplay==2" class="iconfont icon-zhibo type fl"></i>
                        <span v-if="child.videoType=='baijiayunlive'&& child.openReplay==1" class="fl">
                          【回放】
                        </span>
                        <span v-if="child.videoType=='baijiayunlive'&& child.openReplay==2" class="fl">
                          【直播】
                        </span>
                        <span v-if="child.videoType=='baijiayunlive'" class="course-name">{{child.name}}</span>

                        <span v-if="child.videoType=='baijiayunlive'&& child.openReplay==2" class="fl">
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
                        <i v-if="child.videoType=='aliyunlive'" class="iconfont icon-zhibo type fl"></i>
                        <span class="fl">
                          【直播】
                        </span>
                        <span class="course-name">{{child.name}}</span>
                        <span v-if="child.videoType=='aliyunlive'" class="fl">
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
                        <i v-if="child.videoType=='aliyunReplay'" class="imv2-play_circle type fl"></i>
                        <span v-if="child.videoType=='aliyunReplay'" class="fl">
                          【回放】
                        </span>
                        <span class="course-name">{{child.name}}</span>
                        <i class="icon-tick-revert done" v-if="isLogin && child.complete==2"></i>
                        <i class="icon-half ing" v-if="isLogin && child.complete==1"></i>
                        <i class="icon-nolearn start" v-if="isLogin && child.complete==0"></i>
                      </a>
                    </span>
                    <!-- 阿里云直播回放 end -->
                    <!-- 其他类型直播 start -->
                    <span v-if="child.videoType=='otherlive'">
                      <a @click="gotoPlay(child)" href="javascript:void(0)" class="J-media-item">
                        <i class="iconfont icon-zhibo type fl"></i>
                        <span>
                          【直播】
                        </span>
                        <span class="course-name">{{child.name}}</span>
                        <span class="fl">
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
                        <i class="iconfont icon-zhibo type fl"></i>
                        <span class="fl">
                          【直播】
                        </span>
                        <span class="course-name">{{child.name}}</span>
                        <span class="fl">
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
                  <span v-if="courseInfo.sellType == 1">
                    <a @click="gotoPlay(child)"  href="javascript:void(0)" class="J-media-item ">
                      <i class="imv2-play_circle type fl"></i>
                      <span class="fl">
                        【点播】
                      </span>
                      <span class="course-name">
                        {{child.name}}
                      </span>
                      <span class="fl">
                        (学习时长:{{child.videoTimeFmt}})
                      </span>
                      <!--<button class="r moco-btn moco-btn-red preview-btn">开始学习</button>-->
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
                    <i @mouseover="score = 1" class="icon-star2 " v-bind:class="{'active':score >= 1}"></i>
                    <i @mouseover="score = 2" class="icon-star2" v-bind:class="{'active':score >= 2}"></i>
                    <i @mouseover="score = 3" class="icon-star2 " v-bind:class="{'active':score >= 3}"></i>
                    <i @mouseover="score = 4" class="icon-star2 " v-bind:class="{'active':score >= 4}"></i>
                    <i @mouseover="score = 5" class="icon-star2 " v-bind:class="{'active':score == 5}"></i>
                    <span class="undis ml15" v-bind:class="{'disinline':score == 1}"><span style="color: #ffb200;padding-right: 5px">1星</span>差评！课程太差了，我要吐槽！</span>
                    <span class="undis ml15" v-bind:class="{'disinline':score == 2}"><span style="color: #ffb200;padding-right: 5px">2星</span>勉强中评！课程不满意</span>
                    <span class="undis ml15" v-bind:class="{'disinline':score == 3}"><span style="color: #ffb200;padding-right: 5px">3星</span>中评！课程一般</span>
                    <span class="undis ml15" v-bind:class="{'disinline':score == 4}"><span style="color: #ffb200;padding-right: 5px">4星</span>好评！课程还不错，继续保持！</span>
                    <span class="undis ml15" v-bind:class="{'disinline':score == 5}"><span style="color: #ffb200;padding-right: 5px">5星</span>好评！课程非常棒，点赞！</span>

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
            <div class=" evaluate" v-for="comment in commentList" :key="comment.id">
              <div class="evaluation-con">
                <a href="javascript:void(0)" class="img-box">
                  <img :src="comment.headImg" onerror="this.src='/static/img/user-avatar.png'" :alt="comment.showName">
                </a>
                <div class="content-box">
                  <div class="user-info clearfix">
                    <a href="javascript:void(0)" class="username">{{comment.showName}}</a>
                    <div class="star-box">
                      <i class="icon-star2 " v-bind:class="{'active':comment.commentScore >= 1}"></i>
                      <i class="icon-star2 " v-bind:class="{'active':comment.commentScore >= 2}"></i>
                      <i class="icon-star2 " v-bind:class="{'active':comment.commentScore >= 3}"></i>
                      <i class="icon-star2 " v-bind:class="{'active':comment.commentScore >= 4}"></i>
                      <i class="icon-star2 " v-bind:class="{'active':comment.commentScore >= 5}"></i>
                      <span class="ml10 fs12" style="color:#ffb200">{{comment.commentScore}}分</span>
                    </div>
                  </div>
                  <!--user-info end-->
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
              <a @click="gotoPlay(obj)" href="javascript:void(0)" class="moco-btn moco-btn-red moco-btn-lg J-learn-course" v-if="isHave">开始学习</a>
              <a @click="toPay(courseInfo)" href="javascript:void(0)" class="moco-btn moco-btn-red moco-btn-lg J-learn-course" v-if="!isHave">立即购买</a>
            </div>
            <div class="course-info-tip" v-html="courseInfo.title">
            </div>
          </div>

          <div id="coursechapterlistRight" style="display: block;margin-top: 20px" v-for="img in courseInfoImgList" :key="img.id">
            <div>
              <a v-if="img.linkAddress!= ''" :href="img.linkAddress" target="_blank"><img :src="img.imageUrl"></a>
              <a v-if="img.linkAddress== ''" href="javascript:void(0);" ><img :src="img.imageUrl"></a>
            </div>
            <div></div>
          </div>
          <div class="js-related-box">
            <div class="mb40 recom-course-list-box"><h4>相关课程</h4>
              <ul class="js-recom-course moco-aside-course clearfix">
                <li v-for="course in courseInfoList" :key="course.id">
                  <router-link :to="{path:'/course/courseInfo/'+course.id}" class="aside-course-name"
                               target="_blank">
                    <div class="aside-course-img"><img :src="course.logo" onerror="this.src='/static/img/course-default.png'"></div>
                  </router-link>
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
  import {getBannerList,course,commentList,checkComment,courseKpoint} from "@/api";
  import {getDetail,checkCourse,createFreeCourseOrder,commentSubmit,createCourseOrder} from "@/api/user";
  import { mapGetters } from "vuex";
  import pageComponent from '@/views/common/pageComponent'

  export default {
    name: 'courseInfoContent',
    data() {
      return {
        isHave:false,//是否拥有课程权限
        isToLook:false,
        obj:{},
        courseInfoImgList:getStore({ name: 'courseInfoImgList' }) || [],
        courseInfoList:getStore({ name: 'courseInfoList' }) ||[],
        showFlag:0,
        commentList:[],
        commentCount:0,
        current:1,//评论分页
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
      checkComment(){//点击评论按钮
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
        //检查是否已评论
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
      goto(i){//评论分页
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
      computeWordCount(){//评论输入数字回显方法
        this.wordCount = this.commentContent.length
      },
      toPay(courseInfo){//立即购买
        //未登录则跳转到登录页面
        if(!this.isLogin){
          this.$router.push({ path: '/user/login' })
          return;
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
        // this.$router.push({ path: '/course/confirmOrder/'+courseInfo.id })
        let courseId = this.courseInfo.id;
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
        //判断如果没有登录则前往登录
        if(!this.isLogin){
          this.$router.push({ path: '/user/login' })
          return;
        }
        if(this.isToLook){
          this.$layer.alert(
            "当前课程并无可学习章节，请联系管理员！",
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
            "您还未拥有课程，请您购买后观看",
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
        if(obj.videoType == 'baijiayunlive'||obj.videoType == 'iframelive'){
          //修改时间提前10分钟进场
          let d = new Date();
          let addDate = d.setMinutes(d.getMinutes()+5);
          new Date(addDate)
          let ti = this.dateFormat("YYYY-mm-dd HH:MM:SS",new Date(addDate) );
          if(obj.liveBeginTime > ti){
            this.$layer.alert(
              "课程还未开始",
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
              "课程还未开始",
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

        //判断如果为百家云则进行判断  是客户端打开还是web打开
        if(obj.videoType == 'baijiayunlive'){
          if(obj.openType == 'APP'){
            courseKpoint(obj.id).then(res =>{
              let courseKpoint = res.data.data
              this.openApp(courseKpoint.params.clienturl);
            })
            return;
          }
        }
        if(obj.videoType == 'iframelive'){
          window.open(obj.videoUrl)
          return;
        }
        this.$router.push({ path: '/uc/player/'+obj.id })
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
      openApp(enterClassroomClientURL){
        let that = this
        var userAgent = navigator.userAgent;
        var isOpera = userAgent.indexOf("Opera") > -1;
        if (userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera || userAgent.indexOf("Firefox") > -1) {
          // IE和火狐用window.open打开
          // 调起客户端 5秒之后自动关闭调起窗口
          that.$layer.confirm(
            "请先<a href='https://www.baijiayun.com/classroomdown/' style='color: #00a0dc' target='_blank'>下载</a>直播客户端再进入直播",
            {
              title: "进入直播",
              btn: ["进入直播", "取消"],
            },
            layerid => {
              var client = window.open(enterClassroomClientURL);
              setTimeout(function () {
                if (client) {
                  client.close(); //关闭新打开的浏览器窗口，避免留下一个空白窗口
                }
              }, 5000);
              that.$layer.close(layerid);
            },
            layerid => {
              that.$layer.close(layerid);
            }
          )
        } else {
          //其它浏览器使用模拟<a>标签`click`事件的形式调起
          var a = document.createElement("a");
          a.setAttribute("href", enterClassroomClientURL);
          document.body.appendChild(a);
          that.$layer.confirm(
            "请先<a href='https://www.baijiayun.com/classroomdown/' style='color: #00a0dc' target='_blank'>下载</a>直播客户端再进入直播",
            {
              title: "进入直播",
              btn: ["进入直播", "取消"],
            },
            layerid => {
              a.click();
              that.$layer.close(layerid);
            },
            layerid => {
              that.$layer.close(layerid);
            }
          )
        }

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
        let that = this
        setTimeout(function () {
          console.log(that.courseInfo)
        },2000)

      },
      addComment(){//添加评论
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

          //查询广告位
          if(this.courseInfoImgList.length == 0){
            getBannerList("2").then(res => {
              this.courseInfoImgList = res.data.data;
              setStore({ name: 'courseInfoImgList', content: res.data.data,type: 'session' })
            });
          }
          //查询相关课程
          if(this.courseInfoList.length == 0){
            course(1,5,"",1,newVal.subject.id,null).then(res =>{
              this.courseInfoList = res.data.data.records;
              setStore({ name: 'courseInfoList', content: res.data.data.records,type: 'session' })
            })
          }
          //查询评论
          let params = {
            "current": this.current,
            "size": this.size,
            "otherId":newVal.id,
            "type":2
          }
          commentList(params).then(res=>{
            this.commentList = res.data.data.records
            this.page = res.data.data
            this.commentCount = res.data.data.total
          })
          let klist = newVal.list
          if(klist.length >0){
            let child = klist[0].children
            if(child!=undefined&&child!=null){
              if(child.length > 0){
                this.obj = child[0]
              }
            }else {
              this.isToLook=true;
            }
          }
          if(newVal.currentPrice == "0.00"){
            this.isHave = true;
            let param = {
              courseId:newVal.id,
            }
            if(this.isLogin){
              checkCourse(param).then(res =>{
                if(res.data.data){
                  this.showFlag = 1
                }
              })
            }

            return
          }
          //判断用户是否拥有此课程  如果没有登录则不验证
          if(this.isLogin){
            let param = {
              courseId:newVal.id,
            }
            checkCourse(param).then(res =>{
              this.isHave = res.data.data;
              //如果已经拥有了这个课程则默认展示套餐班级
              if(this.isHave){
                this.showFlag = 1
              }
            })
          }
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
    },
  }
</script>

<style scoped>
  .flag-orange{
    color: #e85308;
    border: 1px solid;
    padding: 1px;
    line-height: 1;
    font-size: 12px;
    border-radius: 2px;
    margin-left: 4px;
    display: inline-block;
    vertical-align: 0;
  }
</style>
