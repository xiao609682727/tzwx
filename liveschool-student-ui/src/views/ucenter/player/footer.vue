<template>
  <div id="footer" st>
    <div class="waper">
      <div class="footerwaper clearfix">
        <div class="followus r">
          <a class="followus-weixin" href="javascript:;" target="_blank" title="微信">
            <div class="flw-weixin-box"></div>
          </a>
          <a class="followus-weibo" href="http://weibo.com/u/3306361973" target="_blank" title="新浪微博"></a>
          <a class="followus-qzone" href="http://user.qzone.qq.com/1059809142/" target="_blank" title="QQ空间"></a>
        </div>
        <div class="footer_intro l">
          <div class="footer_link">
            <ul>
              <li v-for="link in bottomLink" :key="link.id" ><a :href="link.url" target="_blank" :title="link.name">{{link.name}}</a></li>
            </ul>
          </div>
          <p v-html="website.copyright"></p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import { mapGetters } from "vuex";
  import { validatenull } from '@/util/validate';

  export default {
    name: 'pageFooter',
    data() {
      return {
      }
    },
    methods: {
      init(){
        //判断如果website 为空则更新数据
        if (validatenull(this.bottomLink)){
          this.$store.dispatch("getBottomLink")
        }
      }
    },
    mounted:function() {
      this.init();
    },
    computed: {
      ...mapGetters(["website","bottomLink"]),
    }
  }
</script>

<style scoped>
  #footer {
    background: #1c1f21;
    border-top: 0px solid #e2e4e6;
    font-size: 12px;
    color: #787d82;
    padding: 27px 0;
    min-width: 620px;
  }
</style>
