<template>

  <div>
    <div id="head" class="sig-head">
      <a href="/" target="_self" class="">
        <img :src="website.loginLogo">
      </a>
    </div>
    <div id="main">
      <div class="login-wrap">
        <div class="rl-modal">
          <div class="forgot-send-result forgot-modal">
                    <span class="icon-send-success">
                        <i class="imv2-close"></i>
                    </span>
            <div class="get-info" style="padding-top:20px">{{msg}}
            </div>
            <a class="moco-btn moco-btn-red moco-btn-lg" href="javascript:window.open('','_self');window.close()">关闭窗口</a>
          </div>
        </div>
      </div>
    </div>
    <div class="vright" v-html="website.copyright"></div>
  </div>

</template>

<script>
import store from '@/store'
import { mapGetters } from "vuex";
export default {
  name: 'Add',
  store,
  data () {
    return {
      type: '',
      userFlag: '',
      value: '',
      name: '',
      msg:""
    }
  },
  methods: {
    init () {

      let type = this.$route.query.type;
      let userFlag = this.$route.query.userFlag;
      let value = this.$route.query.value;
      this.msg = this.$route.query.msg;
      window.opener.postMessage(type+","+userFlag+","+value, window.location);
      if(this.msg=='登录成功'){
        window.close()
      }
      if (typeof(this.msg) == "undefined"||this.msg==''){
       window.close()
      }

    }
  },
  mounted:function() {
    this.init()
  },
  computed: {
    ...mapGetters(["website"]),
  },
}
</script>

<style scoped>
  html, body {
    min-width: 1px!important;
  }
</style>
