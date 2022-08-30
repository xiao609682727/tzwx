<template>
  <div>



    <div id="studyMain">
      <div id="bgarea" class="video-con">
        <div id="courseLayout" class="course-layout">
          <div id="js-ques-box"></div>    <!--此处结构为练习题，视频，编程公用的侧栏-->
          <div class="section-list " v-bind:class="{'active':sectionListFlag == true }">
            <div class="nano has-scrollbar">
              <div class="nano-content chapter-list" tabindex="0" style="right: 2px;">
                <ul v-for="kpoint in courseInfo.list" :key="kpoint.id">
                  <li class="sec-title">
                    <span>{{kpoint.name}}</span>
                  </li>
                  <li class="sec-li" data-id="2147" v-for="child in kpoint.children" :key="child.id" @click="toPlay(child)">
                    <a href="javascript:void(0)"><em class="imv2-play_circle type "></em>{{child.name}}
                      <span v-if="child.videoType=='aliyunlive'||child.videoType=='baijiayunlive'||child.videoType=='polyvlive'||child.videoType=='otherlive'" >
                        <span v-if="child.liveEndTime <= dateFormat('YYYY-mm-dd HH:MM:SS',new Date())">({{child.liveBeginTime|dataFormat('MM月dd日 hh:mm')}}-{{child.liveEndTime|dataFormat('hh:mm')}}  已结束)</span>
                        <span v-if="child.liveEndTime > dateFormat('YYYY-mm-dd HH:MM:SS',new Date())">({{child.liveBeginTime|dataFormat('MM月dd日 hh:mm')}}-{{child.liveEndTime|dataFormat('hh:mm')}})</span>
                      </span>
                      <i class="imv2-empty" v-if="child.id != courseKpoint.id && child.complete==0">
                        未学完
                      </i>
                      <i class="imv2-empty" v-if="child.id != courseKpoint.id && child.complete==1">
                        学习中
                      </i>
                      <i class="imv2-empty" v-if="child.id != courseKpoint.id && child.complete==2">
                        已学完
                      </i>
                      <i class="half" v-if="child.id == courseKpoint.id">正在学<span class="imv2-history"></span>
                      </i>
                    </a>

                  </li>
                </ul>
              </div>
            </div>
          </div>
          <!--资料列表-->
          <div class="section-list " v-bind:class="{'active':materialListFlag == true }">
            <div class="nano has-scrollbar">
              <div class="nano-content chapter-list" tabindex="0" style="right: 2px;" v-if="materialInfo != ''">
                <ul>
                  <li class="sec-title">资料下载</li>
                </ul>
                <ul v-for="material in materialInfo" :key="material.id">
                  <li class="sec-li" data-id="2148">
                    <a :href="material.fileUrl" :download="material.fileName" target="_blank" >
                      <em class="imv2-folder type "></em>{{material.name}}
                      <i class="imv2-arrow3_d" style="color: rgba(255,255,255,.6);">立即下载</i>
                    </a>

                  </li>
                </ul>
              </div>
              <div class="nano-content chapter-list" tabindex="0" style="right: 2px;" v-if="materialInfo == ''">
                <ul>
                  <li class="sec-title">资料下载</li>
                </ul>
                <div class="nodata">
                  <p><i class="imv2-error_c"></i></p>
                  <p>暂无资料</p>
                </div>
              </div>
            </div>
          </div>

          <div class="course-sidebar-layout" id="courseSidebar">
            <dl>
              <dd class="openchapter" @click="showKpoint">
                <i class="imv2-nav_chapter"></i>
                <span>章节</span>
              </dd>
              <!--<dd class="openpanel" data-panel="qa">
                <i class="imv2-yuanwen"></i>
                <span>问答</span>
              </dd>
              <dd class="opentick">
                <i class="imv2-tick"></i>
                <span>课签</span>
              </dd>
              <dd class="openpanel" data-panel="note">
                <i class="imv2-homework"></i>
                <span>笔记</span>
              </dd>
              <dd class="opencomment">
                <i class="imv2-chat_bubble"></i>
                <span>评论</span>
              </dd>-->
              <dd class="opencomment" @click="showMaterial">
                <i class="imv2-folder type"></i>
                <span>资料</span>
              </dd>
              <dd class="vhidden">
                <span>占位</span>
              </dd>
              <dd class="vhidden">
                <span>占位</span>
              </dd>
              <dd class="vhidden">
                <span>占位</span>
              </dd>
              <dd class="vhidden">
                <span>占位</span>
              </dd>
              <dd class="vhidden">
                <span>占位</span>
              </dd>
              <dd class="vhidden">
                <span>占位</span>
              </dd>
            </dl>
          </div>

          <div class="course-center-layout" id="courseCenter" data-minw="500">

            <div class="js-box-wrap course-video-wrap" ref="homePage" style="width: 100%; height: 796px;">
              <div id="J_Box" class="course-video-box">
                <span class="moco-tick hide"></span>
                <div id="video-box" class="video-wrap" style="width:100%;height:100%">

                </div>
                <div id="video-box-mocoplayer" class="mocoplayer">
                  <div class="bjc-player-wrapper">
                    <video class="video video-js vjs-default-skin">
                    </video>
                  </div>
                </div>
                <div class="pause-box J_pause-box hide">
                  <div class="pause-box-inner">
                    <div class="pause-box-wrap">
                      <div id="courseVideoPause">
                        <div class="close-pause-box js-close-pause"><i class="icon-close2"></i></div>
                        <div class="show-select">
                          <input type="checkbox" name="stopPauseAd">
                          <span>今日不再显示</span>
                        </div>
                        <div><img src="//img.mukewang.com/5ea6507109b99bd803480172.jpg"></div><div></div></div>
                    </div>
                  </div>
                </div>
                <div class="next-box J_next-box hide" style="display: none;">
                  <div class="next-box-inner">

                    <div class="course-tip-layer J-next-course" data-next-src="/video/2149">
                      <!--<div class="wechat-box js-wechat-box">
                          <a href="javascript:void(0)" class="moco-icon-close moco-modal-close wechat-close js-wechat-close"></a>
                          检测到您还没有关注慕课网服务号，无法接收课程更新通知。请扫描二维码即可绑定<div style="text-align:center">
                          <img width="100%" src=""></div>
                      </div>-->

                      <h2>
                        下一节课程： PS之视图菜单
                        <span class="course-duration"> (08:59)</span>
                        <span class="J-next-btn hide next-auto moco-btn moco-btn-green">下一节</span>
                        <a href="/video/2147/0" class="review-course">重新观看</a>
                      </h2>
                      <div class="J-next-auto hide next-auto"><em>3</em> 秒后播放下一节</div>

                      <div class="video_over_ad J-next-ad">
                        <div class="line"></div>
                        <h4>为你推荐</h4>
                        <div id="courseVideoNext"><div><img src="//img.mukewang.com/5ea65155099c258403480172.jpg"></div><div></div></div>
                      </div>
                    </div>

                    <div class="qqQunAdd-box js-qqQunAdd">

                    </div>

                  </div>
                </div>
              </div>
            </div>

          </div>

          <!--<div class="course-right-layout" id="courseRight" data-minw="414">
            <div class="course-right-nano has-scrollbar" style="height: 796px;">
              <div class="nano-right-content nano-content" tabindex="0" style="right: -17px;">


                <div class="c-panel video-panel current">
                  <i class="imv2-close video-panel-close"></i>
                  <i class="imv2-arrow3_l video-panel-show"></i>
                  <div class="panel-container">
                    <div class="v-teachers clearfix">
                      <a href="/u/141256/courses?sort=publish" class="v-teachers-img" target="_blank">
                        <img src="//img2.mukewang.com/53855e6f0001034501400140-100-100.jpg">
                      </a>
                      <dl>
                        <dd class="v-t-nickname">
                          <a href="/u/141256/courses?sort=publish" target="_blank">Oeasy</a><i class="imv2-imooc" title="慕课网认证讲师"></i>
                        </dd>
                        <dd class="v-t-title">页面重构设计</dd>
                      </dl>
                    </div>
                    <p class="v-teachers-info">他，授课风趣幽默，激情四射，自称屌丝青年，中国传媒大学计算机教师；他，专注于网页制作、平面设计、多媒体等多个领域的软件以及应用的挖掘与创新，热爱分享，是新鲜热门软件和应用的导航标，他就是众粉丝心中的“Oeasy老湿”</p>
                    <input type="hidden" id="teacher_id" value="141256">
                    &lt;!&ndash;广告位 &ndash;&gt;
                    <div id="courseVideoRight"></div>

                    &lt;!&ndash; 专栏推荐 js append&ndash;&gt;

                    &lt;!&ndash; 讲师课程、相关课程 js append &ndash;&gt;


                    <div class="v-course-wrap">     <h3>讲师课程</h3>     <div class="v-course-list clearfix">                           <a class="v-course" href="/learn/682" target="_blank">                      <img src="//img4.mukewang.com/577f7e700001ef7a06000338-228-128.jpg">              <div class="v-course-name">ps入门教程Ⅱ-进阶篇-图层、矢量、路径</div>             <div class="v-course-info clearfix">                                      <span>                                                      免费                                              </span>                                                       <span>入门</span>                                  <span><i class="imv2-set-sns"></i>61335</span>             </div>         </a>                                <a class="v-course" href="/learn/603" target="_blank">                      <img src="//img3.mukewang.com/5707795b00018b7006000338-228-128.jpg">              <div class="v-course-name">oeasy教你玩转css禅意花园</div>             <div class="v-course-info clearfix">                                      <span>                                                      免费                                              </span>                                                       <span>初级</span>                                  <span><i class="imv2-set-sns"></i>46636</span>             </div>         </a>                                <a class="v-course" href="/learn/574" target="_blank">                      <img src="//img.mukewang.com/579f2721000128bf06000338-228-128.jpg">              <div class="v-course-name">ps教程之趣味外传</div>             <div class="v-course-info clearfix">                                      <span>                                                      免费                                              </span>                                                       <span>中级</span>                                  <span><i class="imv2-set-sns"></i>60092</span>             </div>         </a>                                <a class="v-course" href="/learn/470" target="_blank">                      <img src="//img2.mukewang.com/574669500001461606000338-228-128.jpg">              <div class="v-course-name">Oeasy教你玩转后期剪辑Premiere</div>             <div class="v-course-info clearfix">                                      <span>                                                      免费                                              </span>                                                       <span>入门</span>                                  <span><i class="imv2-set-sns"></i>59641</span>             </div>         </a>                           </div> </div>  </div>
                </div>


                <div class="c-panel qa-panel">

                  <i class="imv2-close panel-close"></i>

                  <div id="jsPutqa" class="putqa panel-t">
                    <h3>提问题</h3>
                    <input type="text" placeholder="请一句话说明你的问题">
                    <p class="error-tip"></p>
                    <textarea id="qa-panel-editor">请输入问题内容...</textarea>
                    <p class="error-tip qa-panel-editor-error-tip"></p>
                    <div class="clearfix">
                      <a href="javascript:;" class="moco-btn moco-btn-red putqa-submit">提交</a>
                      <div class="r errortip-btm js-panel-error-big-tip"></div>
                    </div>
                    <div class="captcha-verify-box js-qa-panel-verify-box hide"></div>
                  </div>

                  <div class="panel-list">
                    <h3>其他问答</h3>
                    <div id="panelQaList" class=""></div>
                    <div class="panel-btn-wrap">
                      <a href="javascript:;" class="moco-btn moco-btn-gray-l js-link-qa">查看更多本节问答</a>
                    </div>
                  </div>

                </div>                <div class="c-panel note-panel">
                <i class="imv2-close panel-close"></i>
                <div id="jsPutNote" class="putnote panel-t">
                  <h3>写笔记</h3>
                  <textarea id="note-panel-editor">请输入笔记内容...</textarea>
                  <p class="note-panel-error-tip error-tip"></p>
                  <div class="clearfix">
                    <div title="截图" class="js-shot-pic screen-btn" data-type="note">
                      <span>截图</span>
                    </div>
                    <a href="javascript:;" class="moco-btn moco-btn-red putnote-submit">提交</a>
                  </div>
                  <div class="captcha-verify-box js-note-panel-verify-box hide"></div>
                </div>
                <div class="panel-list">
                  <h3>我的笔记</h3>
                  <div id="panelNoteList" class=""></div>
                  <div class="panel-btn-wrap">
                    <a href="/u/8891510/notepad/139" class="moco-btn moco-btn-gray-l" target="_blank">查看更多我的笔记</a>
                  </div>
                </div>
              </div>

              </div>
              <div class="nano-pane" style="display: none;"><div class="nano-right-slider" style="height: 779px; transform: translate(0px, 0px);"></div></div></div>

            <div id="courseDrag" class="course-drag"><div class="drag"></div></div>
          </div>-->
        </div>
      </div>

    </div>
    <!--   <div class="publicnumber-block js-publicnumber-block"></div>
       <div class="tipoff-block js-tipoff-block"></div>
       <div class="tipoff-box js-tipoff-box">
         <div class="tipoff-top-box clearfix">
           <p class="l tipoff-title">举报</p>
           <span class="r tipoff-close-btn icon-close2 js-tipoff-close"></span>
         </div>
         <div class="tipoff-type-box js-tipoff-typebox clearfix">
           <div class="tipoff-loading">
             <div class="bounce1"></div>
             <div class="bounce2"></div>
             <div class="bounce3"></div>
           </div>
         </div>
         <div class="tipoff-content">
           <textarea name="tipoff-content" class="tipoff-desc js-tipoff-desc" placeholder="写下举报理由"></textarea>
           <div class="tipoff-text"><span class="js-tipoff-text">0</span>/150</div>
         </div>
         <div class="tipoff-btn-box clearfix">
           <div class="r tipoff-submit-btn js-tipoff-submit" data-tipofftype="">提交</div>
           <div class="r tipoff-cancel-btn js-tipoff-close">取消</div>
         </div>
       </div>-->
    <!--   <div class="subcontainer">

         <div class="course-subnav js-course-subnav">
           <ul class="clearfix">
             <li data-type="qa" class="current" data-index="0">问答</li>
             <li data-type="comment" data-index="1">评论</li>
             <li data-type="note" data-index="2">同学笔记</li>
           </ul>
         </div>

         <div class="course-subcontainer clearfix">
           <div class="course-left">

             <div id="tabwrap">
               <div class="js-tab-item tab-item c-qalist current" data-type="qa">
                 <div class="comp-filter-bar clearfix">
                   <a href="javascript:;" class="moco-change-smalle-btn active" data-t="/t/2">全部</a>
                   <a href="javascript:;" class="moco-change-smalle-btn" data-t="/t/4">精华</a>
                   <span class="js-pub-btn moco-btn moco-btn-gray-l">我要发布</span>
                 </div>
                 <div id="qaContainer" class="answertabcon">   <div class="course_quescon mod-post">                    <div class="wenda-listcon mod-qa-list clearfix">                   <div class="icon-wenda"></div>                  <div class="headslider qa-medias l">             <a href="/u/5235732/courses" class="media" target="_blank" title="慕码人2176133"><img src="//img1.mukewang.com/545865da00012e6402200220-40-40.jpg" width="40" height="40"></a>         </div>         <div class="wendaslider qa-content">             <h2 class="wendaquetitle qa-header">                 <div class="wendatitlecon qa-header-cnt clearfix">                     <a href="/qadetail/345220" target="_blank" class="qa-tit">                         我的疑问嘘什么                     </a>                                      </div>             </h2>             <div class="replycont qa-body clearfix">                                          <a href="/qadetail/345220" class="moco-btn moco-btn-gray-l" target="_blank">+ 我来回答</a>                         <span class="wait-answer icon-drop_left">回答最高可+2积分</span>                              </div>             <div class="replymegfooter qa-footer clearfix">                 <div class="l-box l">                     <a href="/qadetail/345220" target="_blank" class="replynumber static-count ">                         <span class="static-item answer">                             0 回答                         </span>                         <span class="static-item">                             6 浏览                         </span>                     </a>                                      </div>                  <em class="r">3天前</em>             </div>         </div>     </div>                         <div class="wenda-listcon mod-qa-list clearfix">                   <div class="icon-wenda"></div>                  <div class="headslider qa-medias l">             <a href="/u/7329609/courses" class="media" target="_blank" title="慕丝6107686"><img src="//img3.mukewang.com/54584ca90001a85802200220-40-40.jpg" width="40" height="40"></a>         </div>         <div class="wendaslider qa-content">             <h2 class="wendaquetitle qa-header">                 <div class="wendatitlecon qa-header-cnt clearfix">                     <a href="/qadetail/339332" target="_blank" class="qa-tit">                         初学者用PS6还是pscc 好？                     </a>                                      </div>             </h2>             <div class="replycont qa-body clearfix">                                                               <div class="l replydes">                             <span class="replysign">最新回答 / <a href="/u/6426115/courses" target="_blank" title="htmLouis" class="nickname">htmLouis</a></span>                             <div class="replydet">ps6 吧</div>                         </div>                                                   </div>             <div class="replymegfooter qa-footer clearfix">                 <div class="l-box l">                     <a href="/qadetail/339332" target="_blank" class="replynumber static-count ">                         <span class="static-item answer">                             4 回答                         </span>                         <span class="static-item">                             382 浏览                         </span>                     </a>                                      </div>                  <em class="r">2020-04-18</em>             </div>         </div>     </div>                         <div class="wenda-listcon mod-qa-list clearfix">                   <div class="icon-wenda"></div>                  <div class="headslider qa-medias l">             <a href="/u/8325276/courses" class="media" target="_blank" title="Chewer"><img src="//img.mukewang.com/5458666300017f2102200220-40-40.jpg" width="40" height="40"></a>         </div>         <div class="wendaslider qa-content">             <h2 class="wendaquetitle qa-header">                 <div class="wendatitlecon qa-header-cnt clearfix">                     <a href="/qadetail/335533" target="_blank" class="qa-tit">                         苹果logo？                     </a>                                      </div>             </h2>             <div class="replycont qa-body clearfix">                                                               <div class="l replydes">                             <span class="replysign">最新回答 / <a href="/u/5211446/courses" target="_blank" title="慕盖茨5534640" class="nickname">慕盖茨5534640</a></span>                             <div class="replydet"><img draggable="false" class="moco-emoji" alt="😀" src="https://www.imooc.com/static/moco/v1.0/images/face/36x36/1f600.png"><img draggable="false" class="moco-emoji" alt="😀" src="https://www.imooc.com/static/moco/v1.0/images/face/36x36/1f600.png"><img draggable="false" class="moco-emoji" alt="😀" src="https://www.imooc.com/static/moco/v1.0/images/face/36x36/1f600.png">嘿 点赞</div>                         </div>                                                   </div>             <div class="replymegfooter qa-footer clearfix">                 <div class="l-box l">                     <a href="/qadetail/335533" target="_blank" class="replynumber static-count ">                         <span class="static-item answer">                             1 回答                         </span>                         <span class="static-item">                             236 浏览                         </span>                     </a>                                      </div>                  <em class="r">2019-12-10</em>             </div>         </div>     </div>                         <div class="wenda-listcon mod-qa-list clearfix">                   <div class="icon-wenda"></div>                  <div class="headslider qa-medias l">             <a href="/u/8283909/courses" class="media" target="_blank" title="weixin_慕尼黑8154648"><img src="//img2.mukewang.com/5b8cdad90001c64e02000200-40-40.jpg" width="40" height="40"></a>         </div>         <div class="wendaslider qa-content">             <h2 class="wendaquetitle qa-header">                 <div class="wendatitlecon qa-header-cnt clearfix">                     <a href="/qadetail/334564" target="_blank" class="qa-tit">                         这个软件叫什么                     </a>                                      </div>             </h2>             <div class="replycont qa-body clearfix">                                                               <div class="l replydes">                             <span class="replysign">最新回答 / <a href="/u/8290812/courses" target="_blank" title="qq_慕村8471362" class="nickname">qq_慕村8471362</a></span>                             <div class="replydet">photoshop</div>                         </div>                                                   </div>             <div class="replymegfooter qa-footer clearfix">                 <div class="l-box l">                     <a href="/qadetail/334564" target="_blank" class="replynumber static-count ">                         <span class="static-item answer">                             1 回答                         </span>                         <span class="static-item">                             218 浏览                         </span>                     </a>                                      </div>                  <em class="r">2019-11-04</em>             </div>         </div>     </div>                         <div class="wenda-listcon mod-qa-list clearfix">                   <div class="icon-wenda"></div>                  <div class="headslider qa-medias l">             <a href="/u/8240050/courses" class="media" target="_blank" title="慕粉4328675"><img src="//img4.mukewang.com/545862370001b03502200220-40-40.jpg" width="40" height="40"></a>         </div>         <div class="wendaslider qa-content">             <h2 class="wendaquetitle qa-header">                 <div class="wendatitlecon qa-header-cnt clearfix">                     <a href="/qadetail/333412" target="_blank" class="qa-tit">                         这是哪个版本的PS                     </a>                                      </div>             </h2>             <div class="replycont qa-body clearfix">                                                               <div class="l replydes">                             <span class="replysign">最新回答 / <a href="/u/8225793/courses" target="_blank" title="慕设计9444551" class="nickname">慕设计9444551</a></span>                             <div class="replydet">cs4</div>                         </div>                                                   </div>             <div class="replymegfooter qa-footer clearfix">                 <div class="l-box l">                     <a href="/qadetail/333412" target="_blank" class="replynumber static-count ">                         <span class="static-item answer">                             1 回答                         </span>                         <span class="static-item">                             188 浏览                         </span>                     </a>                                      </div>                  <em class="r">2019-10-17</em>             </div>         </div>     </div>                         <div class="wenda-listcon mod-qa-list clearfix">                   <div class="icon-wenda"></div>                  <div class="headslider qa-medias l">             <a href="/u/8123180/courses" class="media" target="_blank" title="慕函数5391197"><img src="//img4.mukewang.com/54586653000151cd02200220-40-40.jpg" width="40" height="40"></a>         </div>         <div class="wendaslider qa-content">             <h2 class="wendaquetitle qa-header">                 <div class="wendatitlecon qa-header-cnt clearfix">                     <a href="/qadetail/330872" target="_blank" class="qa-tit">                         &#8203;photoshop CC2018里这个排列图片的窗口在哪里呢？                     </a>                                      </div>             </h2>             <div class="replycont qa-body clearfix">                                                               <div class="l replydes">                             <span class="replysign">最新回答 / <a href="/u/8125709/courses" target="_blank" title="慕丝4063535" class="nickname">慕丝4063535</a></span>                             <div class="replydet">由图可知&lt;...图片...&gt;</div>                         </div>                                                   </div>             <div class="replymegfooter qa-footer clearfix">                 <div class="l-box l">                     <a href="/qadetail/330872" target="_blank" class="replynumber static-count ">                         <span class="static-item answer">                             2 回答                         </span>                         <span class="static-item">                             404 浏览                         </span>                     </a>                                      </div>                  <em class="r">2019-10-28</em>             </div>         </div>     </div>                         <div class="wenda-listcon mod-qa-list clearfix">                   <div class="icon-wenda"></div>                  <div class="headslider qa-medias l">             <a href="/u/8079932/courses" class="media" target="_blank" title="慕粉8429611"><img src="//img.mukewang.com/533e4d710001322402000200-40-40.jpg" width="40" height="40"></a>         </div>         <div class="wendaslider qa-content">             <h2 class="wendaquetitle qa-header">                 <div class="wendatitlecon qa-header-cnt clearfix">                     <a href="/qadetail/329621" target="_blank" class="qa-tit">                         老师用的是那一个版本呀？                     </a>                                      </div>             </h2>             <div class="replycont qa-body clearfix">                                                               <div class="l replydes">                             <span class="replysign">最新回答 / <a href="/u/8080902/courses" target="_blank" title="qq_慕码人458742" class="nickname">qq_慕码人458742</a></span>                             <div class="replydet">cs4</div>                         </div>                                                   </div>             <div class="replymegfooter qa-footer clearfix">                 <div class="l-box l">                     <a href="/qadetail/329621" target="_blank" class="replynumber static-count ">                         <span class="static-item answer">                             1 回答                         </span>                         <span class="static-item">                             232 浏览                         </span>                     </a>                                      </div>                  <em class="r">2019-08-25</em>             </div>         </div>     </div>                         <div class="wenda-listcon mod-qa-list clearfix">                   <div class="icon-wenda"></div>                  <div class="headslider qa-medias l">             <a href="/u/8002744/courses" class="media" target="_blank" title="charlesrose"><img src="//img1.mukewang.com/5d33da0e00018c8005090574-40-40.jpg" width="40" height="40"></a>         </div>         <div class="wendaslider qa-content">             <h2 class="wendaquetitle qa-header">                 <div class="wendatitlecon qa-header-cnt clearfix">                     <a href="/qadetail/326537" target="_blank" class="qa-tit">                         新版本讲解课程                     </a>                                      </div>             </h2>             <div class="replycont qa-body clearfix">                                                               <div class="l replydes">                             <span class="replysign">最新回答 / <a href="/u/8011803/courses" target="_blank" title="慕虎6366719" class="nickname">慕虎6366719</a></span>                             <div class="replydet">学习新版本PS</div>                         </div>                                                   </div>             <div class="replymegfooter qa-footer clearfix">                 <div class="l-box l">                     <a href="/qadetail/326537" target="_blank" class="replynumber static-count ">                         <span class="static-item answer">                             2 回答                         </span>                         <span class="static-item">                             489 浏览                         </span>                     </a>                                      </div>                  <em class="r">2019-07-24</em>             </div>         </div>     </div>                         <div class="wenda-listcon mod-qa-list clearfix">                   <div class="icon-wenda"></div>                  <div class="headslider qa-medias l">             <a href="/u/7900370/courses" class="media" target="_blank" title="weixin_慕圣6069165"><img src="//img2.mukewang.com/5b8ce8ff00019d6c02000200-40-40.jpg" width="40" height="40"></a>         </div>         <div class="wendaslider qa-content">             <h2 class="wendaquetitle qa-header">                 <div class="wendatitlecon qa-header-cnt clearfix">                     <a href="/qadetail/320177" target="_blank" class="qa-tit">                         这视频里的PS版本是哪个版本啊                     </a>                                      </div>             </h2>             <div class="replycont qa-body clearfix">                                                               <div class="l replydes">                             <span class="replysign">最新回答 / <a href="/u/6963500/courses" target="_blank" title="江7856" class="nickname">江7856</a></span>                             <div class="replydet">我是Windows10，用的是cs5版本，老师用的cs4版本。</div>                         </div>                                                   </div>             <div class="replymegfooter qa-footer clearfix">                 <div class="l-box l">                     <a href="/qadetail/320177" target="_blank" class="replynumber static-count ">                         <span class="static-item answer">                             1 回答                         </span>                         <span class="static-item">                             270 浏览                         </span>                     </a>                                      </div>                  <em class="r">2019-06-12</em>             </div>         </div>     </div>                         <div class="wenda-listcon mod-qa-list clearfix">                   <div class="icon-wenda"></div>                  <div class="headslider qa-medias l">             <a href="/u/6766737/courses" class="media" target="_blank" title="weixin_七里香_3"><img src="//img1.mukewang.com/533e4d470001a00a02000200-40-40.jpg" width="40" height="40"></a>         </div>         <div class="wendaslider qa-content">             <h2 class="wendaquetitle qa-header">                 <div class="wendatitlecon qa-header-cnt clearfix">                     <a href="/qadetail/314119" target="_blank" class="qa-tit">                         12335456                     </a>                                      </div>             </h2>             <div class="replycont qa-body clearfix">                                                               <div class="l replydes">                             <span class="replysign">最新回答 / <a href="/u/7912838/courses" target="_blank" title="qq_慕桂英6593386" class="nickname">qq_慕桂英6593386</a></span>                             <div class="replydet">95614456</div>                         </div>                                                   </div>             <div class="replymegfooter qa-footer clearfix">                 <div class="l-box l">                     <a href="/qadetail/314119" target="_blank" class="replynumber static-count ">                         <span class="static-item answer">                             2 回答                         </span>                         <span class="static-item">                             268 浏览                         </span>                     </a>                                      </div>                  <em class="r">2019-06-15</em>             </div>         </div>     </div>                         <div class="wenda-listcon mod-qa-list clearfix">                   <div class="icon-wenda"></div>                  <div class="headslider qa-medias l">             <a href="/u/7764844/courses" class="media" target="_blank" title="weixin_慕的地6196514"><img src="//img2.mukewang.com/5333a1360001954902000200-40-40.jpg" width="40" height="40"></a>         </div>         <div class="wendaslider qa-content">             <h2 class="wendaquetitle qa-header">                 <div class="wendatitlecon qa-header-cnt clearfix">                     <a href="/qadetail/307107" target="_blank" class="qa-tit">                         版本的问题                     </a>                                      </div>             </h2>             <div class="replycont qa-body clearfix">                                          <a href="/qadetail/307107" class="moco-btn moco-btn-gray-l" target="_blank">+ 我来回答</a>                         <span class="wait-answer icon-drop_left">回答最高可+2积分</span>                              </div>             <div class="replymegfooter qa-footer clearfix">                 <div class="l-box l">                     <a href="/qadetail/307107" target="_blank" class="replynumber static-count ">                         <span class="static-item answer">                             1 回答                         </span>                         <span class="static-item">                             399 浏览                         </span>                     </a>                                      </div>                  <em class="r">2019-04-27</em>             </div>         </div>     </div>                         <div class="wenda-listcon mod-qa-list clearfix">                   <div class="icon-wenda"></div>                  <div class="headslider qa-medias l">             <a href="/u/3969057/courses" class="media" target="_blank" title="慕粉1473462966"><img src="//img3.mukewang.com/545846160001674602200220-40-40.jpg" width="40" height="40"></a>         </div>         <div class="wendaslider qa-content">             <h2 class="wendaquetitle qa-header">                 <div class="wendatitlecon qa-header-cnt clearfix">                     <a href="/qadetail/304807" target="_blank" class="qa-tit">                         目前大家都在用那个版本呢?                     </a>                                      </div>             </h2>             <div class="replycont qa-body clearfix">                                                               <div class="l replydes">                             <span class="replysign">最赞回答 / <a href="/u/7843967/courses" target="_blank" title="被白发少年控制的猫" class="nickname">被白发少年控制的猫</a></span>                             <div class="replydet">CS6</div>                         </div>                                                   </div>             <div class="replymegfooter qa-footer clearfix">                 <div class="l-box l">                     <a href="/qadetail/304807" target="_blank" class="replynumber static-count ">                         <span class="static-item answer">                             4 回答                         </span>                         <span class="static-item">                             460 浏览                         </span>                     </a>                                      </div>                  <em class="r">2020-01-16</em>             </div>         </div>     </div>                         <div class="wenda-listcon mod-qa-list clearfix">                   <div class="icon-wenda"></div>                  <div class="headslider qa-medias l">             <a href="/u/7633116/courses" class="media" target="_blank" title="慕丝4533212"><img src="//img.mukewang.com/5b8ceb7e0001f30f02000200-40-40.jpg" width="40" height="40"></a>         </div>         <div class="wendaslider qa-content">             <h2 class="wendaquetitle qa-header">                 <div class="wendatitlecon qa-header-cnt clearfix">                     <a href="/qadetail/304007" target="_blank" class="qa-tit">                         界面不一样                     </a>                                      </div>             </h2>             <div class="replycont qa-body clearfix">                                          <a href="/qadetail/304007" class="moco-btn moco-btn-gray-l" target="_blank">+ 我来回答</a>                         <span class="wait-answer icon-drop_left">回答最高可+2积分</span>                              </div>             <div class="replymegfooter qa-footer clearfix">                 <div class="l-box l">                     <a href="/qadetail/304007" target="_blank" class="replynumber static-count ">                         <span class="static-item answer">                             1 回答                         </span>                         <span class="static-item">                             364 浏览                         </span>                     </a>                                      </div>                  <em class="r">2019-04-27</em>             </div>         </div>     </div>                         <div class="wenda-listcon mod-qa-list clearfix">                   <div class="icon-wenda"></div>                  <div class="headslider qa-medias l">             <a href="/u/7622721/courses" class="media" target="_blank" title="JZ_jin"><img src="//img2.mukewang.com/5b8cf1810001c0b302000200-40-40.jpg" width="40" height="40"></a>         </div>         <div class="wendaslider qa-content">             <h2 class="wendaquetitle qa-header">                 <div class="wendatitlecon qa-header-cnt clearfix">                     <a href="/qadetail/303738" target="_blank" class="qa-tit">                         有学分吗？                     </a>                                      </div>             </h2>             <div class="replycont qa-body clearfix">                                                               <div class="l replydes">                             <span class="replysign">最新回答 / <a href="/u/7140531/courses" target="_blank" title="qq_可可西里_fDIJT4" class="nickname">qq_可可西里_fDIJT4</a></span>                             <div class="replydet">没有的</div>                         </div>                                                   </div>             <div class="replymegfooter qa-footer clearfix">                 <div class="l-box l">                     <a href="/qadetail/303738" target="_blank" class="replynumber static-count ">                         <span class="static-item answer">                             1 回答                         </span>                         <span class="static-item">                             653 浏览                         </span>                     </a>                                      </div>                  <em class="r">2019-04-06</em>             </div>         </div>     </div>                         <div class="wenda-listcon mod-qa-list clearfix">                   <div class="icon-wenda"></div>                  <div class="headslider qa-medias l">             <a href="/u/7534207/courses" class="media" target="_blank" title="慕仰3259493"><img src="//img.mukewang.com/54584dad0001dd7802200220-40-40.jpg" width="40" height="40"></a>         </div>         <div class="wendaslider qa-content">             <h2 class="wendaquetitle qa-header">                 <div class="wendatitlecon qa-header-cnt clearfix">                     <a href="/qadetail/301278" target="_blank" class="qa-tit">                         哪里下ps啊                     </a>                                      </div>             </h2>             <div class="replycont qa-body clearfix">                                                               <div class="l replydes">                             <span class="replysign">最新回答 / <a href="/u/5977744/courses" target="_blank" title="qq_MrSugar_0" class="nickname">qq_MrSugar_0</a></span>                             <div class="replydet">http://www.php.cn/xiazai/gongju/683 &lt;...图片...&gt;</div>                         </div>                                                   </div>             <div class="replymegfooter qa-footer clearfix">                 <div class="l-box l">                     <a href="/qadetail/301278" target="_blank" class="replynumber static-count ">                         <span class="static-item answer">                             1 回答                         </span>                         <span class="static-item">                             607 浏览                         </span>                     </a>                                      </div>                  <em class="r">2019-02-14</em>             </div>         </div>     </div>           </div>  </div>
                 <div class="paginationwrap">
                   <div class="pagination"><span class="current prev disabled">首页</span><span class="current prevclass prev">上一页</span><span class="current p_interval">1</span><a href="javascript:;" class="p_interval">2</a><a href="javascript:;" class="p_interval">3</a><span class="p_interval">...</span><a href="javascript:;" class="p_interval">28</a><a href="javascript:;" class="nextclass next">下一页</a><a href="javascript:;" class="prev disabled lastclass">尾页</a></div>
                 </div>
               </div>

               <div class="js-tab-item tab-item c-comment" data-type="comment">
                 <div class="comp-filter-bar clearfix">
                   <a href="javascript:;" class="moco-change-smalle-btn active">全部</a>
                   <span class="js-pub-btn moco-btn moco-btn-gray-l">我要发布</span>
                 </div>
                 <div id="commentContainer" class="answertabcon"></div>
                 <div class="paginationwrap">
                   <div class="pagination"></div>
                 </div>
               </div>

               <div class="js-tab-item tab-item c-notelist" data-type="note">
                 <div class="comp-filter-bar clearfix">
                   <a href="javascript:;" class="moco-change-smalle-btn active" data-sort="last">最新</a>
                   <a href="javascript:;" class="moco-change-smalle-btn" data-sort="sugg">点赞</a>

                   <div class="tool-right r">
                     <a href="javascript:;" class="hide-learned tool-chk l">
                       <div class="switch js-lookme">
                         <div class="switch-line"></div>
                         <div class="round js-lookme-round"></div>
                       </div>
                     </a>
                     <span class="tool-item hide-learned tool-chk" href="javascript:;">只看我的</span>
                   </div>
                 </div>

                 <div id="noteContainer" class="answertabcon"></div>
                 <div class="paginationwrap">
                   <div class="pagination"></div>
                 </div>
               </div>
             </div>
           </div>
           <div class="course-right clearfix">
             <div id="courseVideoWendalistRight"></div>

             <div class="js-commend-box">
               <div class="mb40 recom-course-list-box">                        <h4>推荐课程</h4>                        <ul class="js-recom-course moco-aside-course clearfix"><li><div class="aside-course-img"><img src="//img2.mukewang.com/szimg/59eeb022000162bc05400300-360-202.jpg"><p class="aside-course-type">实战</p></div><div class="aside-course-content"><a href="//coding.imooc.com/class/71.html" class="aside-course-name" target="_blank">算法与数据结构-综合提升 C++版</a><p class="aside-course-price"><span>￥166.00</span></p><div class="aside-course-dot"><i class="imv2-dot_samll"></i></div><p class="aside-course-grade">中级</p><div class="aside-course-dot"><i class="imv2-dot_samll"></i></div><p class="aside-course-people"><i class="imv2-set-sns"></i><span>9583</span></p></div></li><li><div class="aside-course-img"><img src="//img1.mukewang.com/szimg/5becd5ad0001b89306000338-360-202.jpg"><p class="aside-course-type">实战</p></div><div class="aside-course-content"><a href="//coding.imooc.com/class/74.html" class="aside-course-name" target="_blank">Vue.js2.5+cube-ui重构饿了么App（经典再升级）</a><p class="aside-course-price"><span>￥198.00</span></p><div class="aside-course-dot"><i class="imv2-dot_samll"></i></div><p class="aside-course-grade">中级</p><div class="aside-course-dot"><i class="imv2-dot_samll"></i></div><p class="aside-course-people"><i class="imv2-set-sns"></i><span>9461</span></p></div></li><li><div class="aside-course-img"><img src="//img4.mukewang.com/szimg/5c0493ce000126b106000338-360-202.jpg"><p class="aside-course-type">实战</p></div><div class="aside-course-content"><a href="//coding.imooc.com/class/75.html" class="aside-course-name" target="_blank">微信小程序入门与实战 常用组件API开发技巧项目实战</a><p class="aside-course-price"><span>￥149.00</span></p><div class="aside-course-dot"><i class="imv2-dot_samll"></i></div><p class="aside-course-grade">初级</p><div class="aside-course-dot"><i class="imv2-dot_samll"></i></div><p class="aside-course-people"><i class="imv2-set-sns"></i><span>20080</span></p></div></li><li><div class="aside-course-img"><img src="//img1.mukewang.com/szimg/59b8a486000107fb05400300-360-202.jpg"><p class="aside-course-type">实战</p></div><div class="aside-course-content"><a href="//coding.imooc.com/class/136.html" class="aside-course-name" target="_blank">全面系统Python3.8入门+进阶  (程序员必备第二语言)</a><p class="aside-course-price"><span>￥366.00</span></p><div class="aside-course-dot"><i class="imv2-dot_samll"></i></div><p class="aside-course-grade">初级</p><div class="aside-course-dot"><i class="imv2-dot_samll"></i></div><p class="aside-course-people"><i class="imv2-set-sns"></i><span>11340</span></p></div></li><li><div class="aside-course-img"><img src="//img2.mukewang.com/szimg/5d1032ab08719e0906000338-360-202.jpg"><p class="aside-course-type">实战</p></div><div class="aside-course-content"><a href="//coding.imooc.com/class/355.html" class="aside-course-name" target="_blank">编程必备基础 计算机组成原理+操作系统+计算机网络</a><p class="aside-course-price"><span>￥236.00</span></p><div class="aside-course-dot"><i class="imv2-dot_samll"></i></div><p class="aside-course-grade">中级</p><div class="aside-course-dot"><i class="imv2-dot_samll"></i></div><p class="aside-course-people"><i class="imv2-set-sns"></i><span>4270</span></p></div></li></ul></div></div>
             <div class="js-tag-box"><div class="box mb40 all-attention-box">                        <h4>热门专题标签</h4>                            <div class="js-all-attention all-attention"><a href="//www.imooc.com/topic/fangzhan" target="_blank" data-id="27" class="orange fs14 normal">仿站教程</a><a href="//www.imooc.com/topic/fullstack" target="_blank" data-id="29" class="yellow fs20 bold">全栈工程师是怎样炼成的</a><a href="//www.imooc.com/topic/azmianshi" target="_blank" data-id="28" class="lake-blue fs18 normal">Android面试题有哪些</a><a href="//www.imooc.com/topic/nodejs" target="_blank" data-id="26" class="green fs16 normal">Node.js进阶教程</a><a href="//www.imooc.com/topic/spring" target="_blank" data-id="23" class="orange fs14 bold">Java Spring进阶</a><a href="//www.imooc.com/topic/mysql" target="_blank" data-id="25" class="blue fs20 bold">电商项目sql优化</a><a href="//www.imooc.com/topic/jquery" target="_blank" data-id="24" class="yellow fs18 normal">用jQuery实现一个小应用</a></div></div>
             </div>
             <div class="js-related-box">
               <div class="mb40 recom-course-list-box">                        <h4>相关课程</h4>                        <ul class="js-recom-course moco-aside-course clearfix"><li><div class="aside-course-img"><img src="//img2.mukewang.com/59cefd4c0001633206000338-240-135.jpg"></div><div class="aside-course-content"><a href="//www.imooc.com/learn/900" class="aside-course-name" target="_blank">Sketch基础入门</a><p class="aside-course-grade">入门</p><div class="aside-course-dot"><i class="imv2-dot_samll"></i></div><p class="aside-course-people"><i class="imv2-set-sns"></i><span>14752</span></p></div></li><li><div class="aside-course-img"><img src="//img3.mukewang.com/5a4ef7ad0001bdc706000338-240-135.jpg"></div><div class="aside-course-content"><a href="//www.imooc.com/learn/932" class="aside-course-name" target="_blank">Adobe After Effects软件基础入门</a><p class="aside-course-grade">入门</p><div class="aside-course-dot"><i class="imv2-dot_samll"></i></div><p class="aside-course-people"><i class="imv2-set-sns"></i><span>21854</span></p></div></li><li><div class="aside-course-img"><img src="//img2.mukewang.com/5ce7bdd9088df7dc06000338-240-135.jpg"></div><div class="aside-course-content"><a href="//www.imooc.com/learn/1131" class="aside-course-name" target="_blank">5G时代Vlog原型系统开发</a><p class="aside-course-grade">中级</p><div class="aside-course-dot"><i class="imv2-dot_samll"></i></div><p class="aside-course-people"><i class="imv2-set-sns"></i><span>10926</span></p></div></li><li><div class="aside-course-img"><img src="//img2.mukewang.com/5a015e400001914706000338-240-135.jpg"></div><div class="aside-course-content"><a href="//www.imooc.com/learn/914" class="aside-course-name" target="_blank">UI必学photoshop基础课程和配色</a><p class="aside-course-grade">入门</p><div class="aside-course-dot"><i class="imv2-dot_samll"></i></div><p class="aside-course-people"><i class="imv2-set-sns"></i><span>36171</span></p></div></li><li><div class="aside-course-img"><img src="//img3.mukewang.com/5c6b7cff08d6895906000338-240-135.jpg"></div><div class="aside-course-content"><a href="//www.imooc.com/learn/1101" class="aside-course-name" target="_blank">3小时极简春节抢红包之Go的实战</a><p class="aside-course-grade">入门</p><div class="aside-course-dot"><i class="imv2-dot_samll"></i></div><p class="aside-course-people"><i class="imv2-set-sns"></i><span>9044</span></p></div></li></ul></div></div>
             <div class="articlelist js-right-article" style="display: block;">
               <div class="course-right-title">
                 <h4>手记推荐</h4>
                 <a href="/article/?block_id=tuijian_wz" target="_blank" class="more">更多</a>
               </div>
               <div class="course-right-content">

               </div>
               <ul><li><a href="/article/305099?block_id=tuijian_wz" target="_blank" class="clearfix"><div style="width: 100%;">IDEA奇淫小技巧</div></a></li><li><a href="/article/304532?block_id=tuijian_wz" target="_blank" class="clearfix"><img src="//img.mukewang.com/5bf3a11300012c1a02720272-40-40.jpg"><div>如何使用 sketch 智能化一键生成多端口代码</div></a></li><li><a href="/article/304246?block_id=tuijian_wz" target="_blank" class="clearfix"><img src="//img.mukewang.com/5eb008660001fd5106880630-40-40.jpg"><div>关于写作那些事之快速上手Mermaid流程图</div></a></li><li><a href="/article/302954?block_id=tuijian_wz" target="_blank" class="clearfix"><img src="//img.mukewang.com/5bf3a0390001c53102720272-40-40.jpg"><div>《吊打面试官》系列-分布式锁之Zk（Zookeeper）</div></a></li><li><a href="/article/301872?block_id=tuijian_wz" target="_blank" class="clearfix"><img src="//img.mukewang.com/5bf39fdb0001ba0702720272-40-40.jpg"><div>Typora原生集成PicGo图床工具！</div></a></li></ul>
             </div>      </div>
         </div>
       </div>-->


  </div>
</template>
<script>
  import {courseInfo  , materialInfo  ,checkHaveCourse} from "@/api";
  import {addCoursestudyhistory,updateCoursestudyhistory} from "@/api/user";
  import Cookies from 'js-cookie';

  export default {
    name: 'playerContent',
    data() {
      return {
        sectionListFlag:false,
        materialListFlag:false,
        clientHeight:'',
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
      materialInfo: Array,
      times:Number
    },
    watch: {
      courseKpoint: {
        deep: true,
        handler(newVal){
          this.init();
        }
      },// 如果 `clientHeight` 发生改变，这个函数就会运行
      clientHeight: function () {
        this.changeFixed(this.clientHeight)
      },
      times: {
        deep: true,
        handler(newVal){
          this.childertiems=newVal;
        }
      },
    },
    methods: {
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
      changeFixed(clientHeight){
        //动态修改样式
        this.$refs.homePage.style.height = (clientHeight-60)+'px';

      },
      initHeight(){  //获取高度
        //获取浏览器可视区域高度
        this.clientHeight = document.body.clientHeight;
        // console.log($(document).height());//浏览器可视区域对象宽度
        window.onresize = () => {  //当窗口或框架发生改变时触发
          //console.log("onresize进来了");
          this.clientHeight = document.body.clientHeight;
        };
      },
      showKpoint(){
        this.materialListFlag = false ;
        this.sectionListFlag = !this.sectionListFlag
      },
      showMaterial(){
        this.sectionListFlag = false ;
        this.materialListFlag = !this.materialListFlag
      },
      init(){
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
          let player
          let that = this
          let userId=that.$parent.studentInfo.id;
          let videoTime=0;
          let videoTimeOld=Cookies.get("video_play"+userId+""+that.courseKpoint.courseId+""+that.courseKpoint.id);
          if(videoTimeOld!=undefined&&videoTimeOld!=""&&videoTimeOld!=0){
            videoTime=videoTimeOld;
          }
          if(this.courseKpoint.videoType == "baijiayunvod"){
            let taocanId = this.$route.query.taocanId;
            let courseType=1;
            if(taocanId!=undefined&&taocanId!=''&&taocanId!=0){
              courseType=2;
            }else {
              taocanId=0;
            }
            let playProgress = 0;
            if(that.courseKpoint.playProgress=='1'){
              playProgress=2;
            }
            let param = {
              courseId:that.courseKpoint.courseId,
              kpointId:that.courseKpoint.id,
              courseName:that.courseInfo.courseName,
              kpointName:that.courseKpoint.name,
              kpointCourseId:taocanId,
              courseType:courseType,
              complete:1
            }
            addCoursestudyhistory(param).then(res =>{
              // console.log("创建播放记录完成")
            })
            // eslint-disable-next-line no-undef
            if (that.courseKpoint.rateComponent == '2'){
              // eslint-disable-next-line no-undef
              player = new BjcPlayer($('#video-box-mocoplayer').find('.video')[0],  {
                token: this.courseKpoint.params.token,
                vid: this.courseKpoint.videoUrl,
                hideProgress:playProgress,
                playbackRates:"",
                currentTime:videoTime,
                onerror: function () {
                  console.log('onplayerror event');
                },
                onended: function () {

                },
                onplay: function () {
                  console.log('onplaybegin event');
                },
                onpause: function () {
                  console.log('onplaypause event');
                },
                ontimeupdate: function (data) {
                   videoTime = parseInt(data. currentTime);
                  Cookies.set("video_play"+userId+""+that.courseKpoint.courseId+""+that.courseKpoint.id,videoTime,7*24*3600)
                },
                onseeked: function (data) {
                  console.log('onseek event');
                  console.log('seekTime:' + data.currentTime);
                },
              });
            }else {
              // eslint-disable-next-line no-undef
              player = new BjcPlayer($('#video-box-mocoplayer').find('.video')[0],  {
                token: this.courseKpoint.params.token,
                vid: this.courseKpoint.videoUrl,
                hideProgress:playProgress,
                currentTime:videoTime,
                onerror: function () {
                  console.log('onplayerror event');
                },
                onended: function () {

                },
                onplay: function () {
                  console.log('onplaybegin event');
                },
                onpause: function () {
                  console.log('onplaypause event');
                },
                ontimeupdate: function (data) {
                  videoTime = parseInt(data. currentTime);
                  Cookies.set("video_play"+userId+""+that.courseKpoint.courseId+""+that.courseKpoint.id,videoTime,7*24*3600)
                },
                onseeked: function (data) {
                  console.log('onseek event');
                  console.log('seekTime:' + data.currentTime);
                },
              });
            }
          }
          /*if(this.courseKpoint.videoType == "baijiayunlive"){
            let param = {
              courseId:that.courseKpoint.courseId,
              kpointId:that.courseKpoint.id,
              courseName:that.courseInfo.courseName,
              kpointName:that.courseKpoint.name,
              complete:1
            }
            addCoursestudyhistory(param).then(res =>{
              console.log("创建播放记录完成")
            })
            // eslint-disable-next-line no-undef
            BJYPlayer.init({
              class: {
                id: '18060537167627'
              },
              env: 'production',
              //账号所处环境，客户统一填写production
              privateDomainPrefix:'production',
              //专属域名，如果客户有专属域名，则提前专属域名前缀
              sign: '07cbd5276959912d2632444023cbc482',
              //进入直播教室的用户签名
              element: $('#live-player'),
              // 默认为false, 可省略
              showControls: true,
              // 默认为true，可省略
              canSwitchServer: false
            });
          }*/
        })
      },
      toPlay(obj){
        let taocanIdc = this.$route.query.taocanId;
        let courseType=1;
        let courseId=this.courseKpoint.id;
        if(taocanIdc!=undefined&&taocanIdc!=''&&taocanIdc!=0){
          courseType=2;
        }else {
          taocanIdc=0;
        }
        let param = {
          courseId:obj.courseId,
          kpointId:this.courseKpoint.id,
          kpointCourseId:taocanIdc,
          courseType:courseType,
          watchStingTime:this.childertiems
        }
        updateCoursestudyhistory(param).then(res =>{
          this.childertiems=0;
        })
        //判断日期 如果是直播 则进行时间判断 如果是回放则不进行时间判断
        if(obj.videoType == 'polyvlive'||obj.videoType == 'aliyunlive'||obj.videoType == 'baijiayunlive'||obj.videoType == 'otherlive'){
          //修改时间提前10分钟进场
          let d = new Date();
          let addDate = d.setMinutes(d.getMinutes()+10);
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
        let taocanId = this.$route.query.taocanId;
        this.$router.push({ path: '/uc/player/'+obj.id,query:{"taocanId":taocanId} })
        location.reload()
      },

    },
    mounted:function() {
      // 通过$once来监听定时器，在beforeDestroy钩子可以被清除。
      this.$once('hook:beforeDestroy', () => {
        clearInterval(that.$parent.cookieTime)
      });
      window.addEventListener('beforeunload',this.beforeUnloadHandler,true);
      let that = this
      setTimeout(function () {
        that.init();
      },500)
      this.initHeight();
    }
  }
</script>

<style scoped>
  .bjc-player-wrapper {
    width: 100%;
    height: 100%;
  }
  .bjc-player-wrapper .video-js {
    width: 100%;
    height: 100%;
  }
  #studyMain {
    position: relative;
    z-index: 1;
  }
  .video-con {
    position: relative;
    background-color: #000;
    overflow: hidden;
    width:100%;
  }
  .course-layout {
    display: flex;
    display: -webkit-flex;
    position: relative;
    zindex: 1;
  }
  #js-ques-box {
    position: absolute;
    bottom: 14px;
    z-index: 100;
  }
  .section-list {
    position: absolute;
    left: -400px;
    top: 0;
    bottom: 0;
    width: 400px;
    background: #26292c;
    box-shadow: 0 8px 16px 0 #1c1f21;
    z-index: 9999;
    transition: .3s all linear;
    opacity: 0;
    padding: 8px 0;
    box-sizing: border-box;
  }
  .nano {
    position: relative;
    width: 100%;
    height: 100%;
    overflow: hidden;
  }
  .nano>.nano-content {
    position: absolute;
    overflow: scroll;
    overflow-x: hidden;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
  }
  .section-list .nano-content {
    padding-bottom: 20px;
  }
  ol, ul {
    list-style: none;
  }

  .section-list ul {
    padding: 0 24px;
  }
  .section-list .sec-title {
    padding-top: 20px;
    font-size: 14px;
    color: #fff;
    line-height: 22px;
    margin-bottom: 8px;
  }
  .section-list .sec-title {
    padding-top: 20px;
    font-size: 14px;
    color: #fff;
    line-height: 22px;
    margin-bottom: 8px;
  }
  .section-list .sec-li {
    height: 30px;
  }
  .section-list .sec-li a {
    padding: 6px 0 6px 28px;
    width: 100%;
    height: 30px;
    box-sizing: border-box;
    overflow: hidden;
    display: block;
    position: relative;
    font-size: 12px;
    color: rgba(255,255,255,.6);
    line-height: 20px;
  }
  .clearfix:after {
    content: '\0020';
    display: block;
    height: 0;
    clear: both;
    visibility: hidden;
  }
  .section-list .sec-li em {
    position: absolute;
    left: 8px;
    top: 6px;
    font-size: 16px;
    line-height: 18px;
  }
  .imv2-video_circle:before {
    content: "\e923";
  }
  .section-list .sec-li i {
    float: right;
    font-size: 12px;
    color: rgba(255,255,255,.2);
    line-height: 20px;
  }
  .section-list.active {
    left: 60px;
    opacity: 1;
  }
  .nano>.nano-pane {
    position: absolute;
    width: 10px;
    right: 10px;
    top: 0;
    bottom: 0;
    visibility: hidden\9;
    -webkit-transition: .2s;
    -moz-transition: .2s;
    -o-transition: .2s;
    transition: .2s;
    -moz-border-radius: 5px;
    -webkit-border-radius: 5px;
    border-radius: 5px;
  }
  .nano>.nano-pane>.nano-slider {
    background: #4e5a5e;
    position: relative;
    margin: 0 1px;
    -moz-border-radius: 3px;
    -webkit-border-radius: 3px;
    border-radius: 3px;
  }
  .course-sidebar-layout {
    background: #1c1f21;
    width: 60px;
    display: flex;
    display: -webkit-flex;
    align-items: center;
    z-index: 10000;
  }
  .course-sidebar-layout dl {
    flex: 1;
    width: 100%;
  }
  .course-sidebar-layout dd {
    display: flex;
    display: -webkit-flex;
    height: 72px;
    flex-direction: column;
    text-align: center;
    justify-content: center;
    cursor: pointer;
  }
  .course-sidebar-layout i {
    font-size: 24px;
    color: rgba(255,255,255,.6);
    line-height: 24px;
  }
  .course-sidebar-layout span {
    font-size: 12px;
    color: rgba(255,255,255,.4);
    line-height: 18px;
  }
  .course-sidebar-layout dd.vhidden {
    display: none;
  }
  .course-center-layout {
    position: relative;
    flex: 1;
    background: #1c1f21;
  }
  .course-video-wrap {
    padding-top: 16px;
    padding-bottom: 16px;
    box-sizing: border-box;
  }
  .js-box-wrap {
    width: 100%;
    height: 300px;
  }
  .course-video-box {
    position: relative;
    margin: 0 auto;
    height: 100%;
    background: #0e1011;
    box-shadow: 0 8px 16px 0 rgba(14,16,17,.8);
    overflow: hidden;
  }
  .course-video-box .moco-tick.hide {
    display: none;
  }
  .course-video-box .moco-tick {
    top: 0;
    right: 40px;
    z-index: 1;
  }
  .hide {
    display: none;
  }
  .video-wrap {
    width: 100%;
    height: 100%;
  }
  .mocoplayer {
    width: 100%;
    height: 100%;
    background: #000;
  }
  .pause-box {
    position: absolute;
    left: 0;
    top: 0;
    right: 0;
    bottom: 50px;
  }
  .course-right-layout {
    position: relative;
    background: #1c1f21;
    width: 384px;
    padding: 0;
    right: 0;
    z-index: 1;
  }
  .course-right-layout .course-right-nano, .course-right-scroll .course-right-nano {
    position: relative;
    width: 100%;
    height: 100%;
    overflow: hidden;
    z-index: 2;
  }
  .course-right-layout .nano-right-content, .course-right-scroll .nano-right-content {
    position: absolute;
    overflow: scroll;
    overflow-x: hidden;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
  }
  .c-panel.current {
    display: block;
    z-index: 1;
  }
  .c-panel.current {
    display: block;
    z-index: 1;
  }
  .video-panel {
    padding: 32px;
    background: #1c1f21;
  }
  .video-panel .video-panel-close, .video-panel .video-panel-show {
    position: absolute;
    right: 24px;
    top: 16px;
    font-size: 24px;
    color: #93999f;
    line-height: 24px;
    cursor: pointer;
    z-index: 1;
  }
  .video-panel .video-panel-close {
    display: inline;
  }
  .imv2-close:before {
    content: "\e5cd";
  }
  .video-panel .v-teachers {
    margin-bottom: 16px;
  }
  .video-panel .v-teachers-info {
    font-size: 12px;
    color: rgba(255,255,255,.6);
    line-height: 22px;
  }

  #courseVideoRight {
    max-height: 160px;
    min-height: 60px;
    width: 320px;
    overflow: hidden;
    margin-top: 24px;
    cursor: pointer;
    position: relative;
    z-index: 899;
    display: none;
    line-height: 0;
    border-radius: 12px;
  }
  #courseVideoRight img {
    width: 100%;
  }
  .video-panel .v-course-wrap {
    padding-top: 24px;
  }
  .video-panel .v-course-wrap h3 {
    font-size: 16px;
    color: #fff;
    line-height: 24px;
  }
  .video-panel .v-course-wrap .v-course-list {
    padding-top: 16px;
  }
  .video-panel .v-course-wrap .v-course:last-child {
    margin-bottom: 0;
  }
  .video-panel .v-course-wrap .v-course {
    float: left;
    width: 272px;
    position: relative;
    margin-bottom: 20px;
    padding-left: 112px;
    box-sizing: border-box;
    margin-right: 15px;
    height: 64px;
    min-height: 64px;
  }
  .video-panel .v-course-wrap img {
    position: absolute;
    left: 0;
    top: 0;
    width: 96px;
    height: 64px;
    border-radius: 8px;
  }
  .video-panel .v-course-wrap .v-course-name {
    font-size: 14px;
    color: rgba(255,255,255,.6);
    line-height: 20px;
    transition: .2s all linear;
    max-height: 40px;
    overflow: hidden;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
  }
  .video-panel .v-course-wrap .v-course-info span {
    float: left;
    position: relative;
    font-size: 12px;
    color: rgba(255,255,255,.4);
    line-height: 18px;
    padding-right: 14px;
    transition: .2s all linear;
  }
  .video-panel .v-course-wrap .v-course-info span::before {
    content: "·";
    position: absolute;
    right: 4px;
    top: 0;
    width: 6px;
    text-align: center;
    line-height: 18px;
  }

  #app{
    height: 100%;
  }
</style>
