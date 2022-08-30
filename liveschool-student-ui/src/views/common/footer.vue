<template>
  <div>
    <div class="footer idx-minwidth">
      <div class="container">
        <div class="footer-wrap idx-width fl col-20">
          <div class="footer-sns clearfix">
            <div class="l tac">
              <img :src="website.bottonLogo" class="" width="140px" height="140px">
              <p>官方公众号</p>
            </div>
          </div>
        </div>
        <div class="fl col-80 tar ">
          <div class="find-box">
            <div class="footer-link">
              <span class="friend">友情链接</span>
            </div>
            <div class="footer-link ml10">
              <a :href="link.url" target="_blank" :title="link.name" v-for="link in friendLink" :key="link.id">{{link.name}}</a>
            </div>
          </div>
          <div class="footer-link mt10 ml-10" >
            <a :href="link.url" target="_blank" :title="link.name" v-for="link in bottomLink" :key="link.id">{{link.name}}</a>

            <a href="javascript:void(0)" :title="website.phone" >服务热线：{{website.phone}}</a>
            <a href="javascript:void(0)" :title="website.email" >Email：{{website.email}}</a>
          </div>
          <div class="footer-copyright ml-10 footer-link ">
            <!--版权及网站底部-->
            <p v-html="website.copyright">
            </p>
            <!--统计代码-->
            <p v-append="this.website.censusCodeString" >
            </p>
          </div>
        </div>
        <div class="clear"></div>
      </div>
    </div>
    <div id="J_GotoTop" class="elevator">
      <a :href="website.information" class="elevator-msg" target="_blank">
        <i class="icon-feedback"></i>
        <span class="">在线咨询</span>
      </a>
      <a href="/help" class="elevator-faq" target="_blank">
        <i class="icon-ques"></i>
        <span class="">帮助中心</span>
      </a>
      <a href="javascript:void(0)" class="elevator-weixin no-goto" id="js-elevator-weixin">
        <i class="icon-wxgzh"></i>
        <span class="">官方微信</span>
        <img :src="website.bottonLogo" class="elevator-weixin-box">
      </a>
      <a href="javascript:void(0)" @click="backToTop"  v-show="visible" class="elevator-top no-goto" id="backTop">
        <i class="icon-up2"></i>
        <span class="">返回顶部</span>
      </a>
    </div>
  </div>
</template>
<script>
  import { mapGetters } from "vuex";
  import { validatenull } from '@/util/validate';
  import Vue from 'vue'
  import VueAppend from 'vue-append'
  Vue.use(VueAppend)

  export default {
    name: 'pageFooter',
    data() {
      return {
        visible: true,
        censusCodeString: '',
        visibilityHeight:100,
        backPosition:0,
        interval: null
      }
    },
    methods: {
      init(){

        //判断如果website 为空则更新数据
        if (validatenull(this.bottomLink)){
          this.$store.dispatch("getBottomLink")
        }
        //判断如果website 为空则更新数据
        if (validatenull(this.friendLink)){
          this.$store.dispatch("getFriendLink")
        }
        //加载统计代码
        this.censusCodeString = this.website.censusCodeString;
      },

      backToTop() {
        let distanceY = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop
        let i = 0
        let that = this
        this.interval = setInterval(() => {
          let next = Math.floor(this.easeInOutQuad(10 * i, distanceY, -distanceY, 500))
          if(next <= that.backPosition) {
            window.scrollTo(0, that.backPosition)
            clearInterval(this.interval)
          } else{
            document.documentElement.scrollTop = document.body.scrollTop = next
          }
          i++
        }, 17)
      },
      easeInOutQuad(t, b, c, d) {
        // 判断当前时间是否总在总时间的一半以内，是的话执行缓入函数，否则的话执行缓出函数
        if ((t /= d / 2) < 1) {
          return c / 2 * t * t + b
        } else {
          // 将总长度设置为一半，并且时间从当前开始递减，对图像进行垂直向上平移
          return -c / 2 * (--t * (t - 2) - 1) + b
        }
      }
    },
    mounted:function() {
      this.init();
    },
    computed: {
      ...mapGetters(["website","bottomLink","friendLink"]),
    }
  }
</script>

<style scoped>



</style>
