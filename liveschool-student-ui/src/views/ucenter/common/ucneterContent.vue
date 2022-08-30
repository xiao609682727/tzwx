<template>
  <div class="uc-wrap mb50">
    <div class="slider">
      <ul>
        <li>
          <router-link :to="{path:'/uc/myCourse',query:{courseType:''}}" >
            <i class="imv2-war"></i>
            <span>我的课程</span>
            <b class="icon-drop_right"></b>
            </router-link>
        </li>
        <li>
          <router-link :to="{path:'/uc/myLive',query:{courseType:'LIVE'}}" >
            <i class="iconfont icon-zhibo"></i>
            <span>我的直播</span>
            <b class="icon-drop_right"></b>
          </router-link>
        </li>
        <li>
          <router-link :to="{path:'/uc/myPackage',query:{courseType:'PACKAGE'}}" >
            <i class="iconfont-tongzhouwx icon-banji"></i>
            <span>我的班级</span>
            <b class="icon-drop_right"></b>
          </router-link>
        </li>
        <li>
          <router-link :to="{path:'/uc/myLineDownCourse',query:{courseType:'LINECLASS'}}" >
            <i class="iconfont-tongzhouwx icon-mianshou"></i>
            <span>我的面授</span>
            <b class="icon-drop_right"></b>
          </router-link>
        </li>
        <li>
          <router-link :to="{path:'/uc/examRecord'}" >
            <i class="iconfont-tongzhouwx icon-kaoshi"></i>
            <span>我的考试</span>
            <b class="icon-drop_right"></b>
          </router-link>
        </li>
        <li>
          <router-link :to="{path:'/uc/courseCollection'}">
            <i class="new-icn"></i>
            <i class="moco-icon-heart-revert"></i>
            <span>我的收藏</span>
            <b class="icon-drop_right"></b>
            </router-link>
        </li>
        <li>
          <router-link :to="{path:'/uc/myOrder'}">
            <i class="new-icn"></i>
            <i class="imv2-list_numbered"></i>
            <span>我的订单</span>
            <b class="icon-drop_right"></b>
          </router-link>
        </li>
        <li>
          <router-link :to="{path:'/uc/myAccount'}">
            <i class="new-icn"></i>
            <i class="iconfont icon-yue"></i>
            <span>我的学币</span>
            <b class="icon-drop_right"></b>
          </router-link>
        </li>
        <li>
          <router-link :to="{path:'/uc/selfInfo'}">
            <i class="imv2-tick"></i>
            <span>个人信息</span>
            <b class="icon-drop_right"></b>
          </router-link>
        </li>
        <!-- 只能自己看的 -->
        <li>
          <router-link :to="{path:'/uc/accountBinding'}">
            <i class="imv2-nav_note"></i>
            <span>账号绑定</span>
            <b class="icon-drop_right"></b>
          </router-link>
        </li>
        <li>
          <router-link :to="{path:'/uc/letter'}">
            <i class="moco-icon-notifi"></i>
            <span>系统消息</span>
            <b class="icon-drop_right"></b>
          </router-link>
        </li>
      </ul>
    </div>
    <router-view/>
  </div>
</template>

<script>
  export default {
    name: 'ucneterContent',
    data() {
      return {
        isReg: false,
        name: '',
        password: '',
        repeat: ''
      }
    },
    methods: {}
  }
</script>

<style scoped>
body {
  background: #fff!important;
}
</style>
