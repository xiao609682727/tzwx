<template>

  <div class="active-box js-active-fixed">
    <div class="share-box">
      <div class="small-share">
        <ul class="share-wrap">
          <li class="weichat-posi">
            <!-- 推荐 -->
            <div class="praise-box">
              <button id="js-praise-left">
                <span class="imv2-thumb_up"></span>
              </button>
              <span class="praise-num">159</span>
            </div>
            <!-- 推荐end -->


            <div class="icon-warp">
              <div class="follow-warp">
                <a onclick="_hmt.push(['_trackEvent', 'sjtj_left', 'share', 'pl'])" data-track="pl-1-1" href="#comment" class="icon-nav imv2-chat_bubble" title="评论"></a>
                <span class="comment-num">28</span>
              </div>
            </div>
            <div class="icon-warp">
              <div class="follow-warp">
                <a onclick="_hmt.push(['_trackEvent', 'sjtj_left', 'share', 'sc'])" data-track="sc-1-1" data-id="297630" href="javascript:" class="js-follow dc-follow  icon-nav icon-star2 " title="收藏"></a>
                <span>1225</span>
              </div>
            </div>
            <div class="icon-warp ">
              <a class="js-forward-btn imv2-forward1 icon-nav" href="#">
                <span class="sharetext">转发</span>
              </a>
              <!-- 只在第一次访问显示 -->
            </div>
            <p class="line"></p>

            <div class="bdsharebuttonbox weichat-style bdshare-button-style0-16" data-bd-bind="1590283936872">
              <div class="sharetext">分享</div>
              <div class="icon-warp2">
                <a onclick="_hmt.push(['_trackEvent', 'sjtj_left', 'share', 'wx'])" data-track="letfshare-1-1" href="#" class="bds_weixin icon-nav icon-share-weichat share-icon" data-cmd="weixin" title="分享到微信"></a>
              </div>
              <div class="icon-warp2">
                <a onclick="_hmt.push(['_trackEvent', 'sjtj_left', 'share', 'wb'])" data-track="letfshare-1-2" href="#" class="bds_tsina icon-nav icon-share-weibo share-icon" data-cmd="tsina" title="分享到新浪微博"></a>
              </div>
              <div class="icon-warp2">
                <a onclick="_hmt.push(['_trackEvent', 'sjtj_left', 'share', 'qq'])" data-track="letfshare-1-3" href="#" class="bds_qzone icon-nav icon-share-qq share-icon" data-cmd="qzone" title="分享到QQ空间"></a>
              </div>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>

  export default {
    name: 'articleinfo',
    data() {
      return {
        subjectIdLevel1: "",
        subjectIdLevel2: "",
        type: ""
      }
    }, components: {},
    methods: {}
  }
</script>

<style scoped>


</style>
