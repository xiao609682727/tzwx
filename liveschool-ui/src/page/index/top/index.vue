<template>
  <div class="avue-top">
    <div class="top-bar__left">
      <div class="avue-breadcrumb"
           :class="[{ 'avue-breadcrumb--active': isCollapse }]"
           v-if="showCollapse">
        <i class="icon-navicon"
           @click="setCollapse"></i>
      </div>
    </div>
    <div class="top-bar__title">
      <div class="top-bar__item top-bar__item--show"
           v-if="showMenu">
        <top-menu></top-menu>
      </div>
      <span class="top-bar__item"
            v-if="showSearch">
        <top-search></top-search>
      </span>
    </div>
    <div class="top-bar__right">

      <el-tooltip
          effect="dark"
          content="移动端"
          placement="left">
        <div class="top-bar__item mobileCode" >
          <i class="icon-shouji1"></i>
          <i class="icon-shouji111 mobileCode1" >
            <qrcode :value="studentBaseUrl" :options="{ width: 172 }"></qrcode>
          </i>
        </div>
      </el-tooltip>

      <el-tooltip
          effect="dark"
          content="学员端"
          placement="bottom">
        <div class="top-bar__item">
          <a :href="studentBaseUrl" target="_blank" class="el-dropdown-link" title="系统前台">
            <i class="icon-diannao">

            </i>
          </a>

        </div>
      </el-tooltip>
      <el-tooltip v-if="showColor"
                  effect="dark"
                  :content="$t('navbar.color')"
                  placement="bottom">
        <div class="top-bar__item">
          <top-color></top-color>
        </div>
      </el-tooltip>
      <el-tooltip v-if="showDebug"
                  effect="dark"
                  :content="logsFlag?$t('navbar.bug'):logsLen+$t('navbar.bugs')"
                  placement="bottom">
        <div class="top-bar__item">
          <top-logs></top-logs>
        </div>
      </el-tooltip>
      <el-tooltip v-if="showLock"
                  effect="dark"
                  :content="$t('navbar.lock')"
                  placement="bottom">
        <div class="top-bar__item">
          <top-lock></top-lock>
        </div>
      </el-tooltip>
      <el-tooltip v-if="showTheme"
                  effect="dark"
                  :content="$t('navbar.theme')"
                  placement="bottom">
        <div class="top-bar__item top-bar__item--show">
          <top-theme></top-theme>
        </div>
      </el-tooltip>
<!--      <el-tooltip effect="dark"
                  :content="$t('navbar.language')"
                  placement="bottom">
        <div class="top-bar__item top-bar__item&#45;&#45;show">
          <top-lang></top-lang>
        </div>
      </el-tooltip>-->
      <el-tooltip v-if="showFullScren"
                  effect="dark"
                  :content="isFullScren?$t('navbar.screenfullF'):$t('navbar.screenfull')"
                  placement="bottom">
        <div class="top-bar__item">
          <i :class="isFullScren?'icon-tuichuquanping':'icon-quanping'"
             @click="handleScreen"></i>
        </div>
      </el-tooltip>
      <el-dialog title="重置密码" width="30%"  :visible.sync="dialogPwdVisible">
        <el-form :model="userPwd">
          <el-form-item label="新密码：" label-width="120px" >
            <el-input  v-model="userPwd.password" placeholder="请输入密码" style="width: 70%;" show-password></el-input>
          </el-form-item>
          <el-form-item label="确认密码：" label-width="120px" >
            <el-input  v-model="userPwd.passwords" placeholder="请在一次输入密码" style="width: 70%;" show-password></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="updatePwd">确 定</el-button>
          <el-button  @click="dialogPwdVisible = false" >取 消</el-button>
        </div>
      </el-dialog>

      <el-dropdown style="margin-left: 10px">
        <span class="el-dropdown-link">
          {{userInfo.userName}}
          <i class="el-icon-arrow-down el-icon--right"></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item>
            <router-link to="/">{{$t('navbar.dashboard')}}</router-link>
          </el-dropdown-item>
          <el-dropdown-item @click.native="updatePassWord"
                            divided>{{$t('navbar.updatePassWord')}}
          </el-dropdown-item>
          <!--<el-dropdown-item>
            <router-link to="/info/index">{{$t('navbar.userinfo')}}</router-link>
          </el-dropdown-item>-->
          <el-dropdown-item @click.native="logout"
                            divided>{{$t('navbar.logOut')}}</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>
<script>
import { mapGetters, mapState } from "vuex";
import {updatePasswordUserName} from "@/api/system/user";
import { fullscreenToggel, listenfullscreen } from "@/util/util";
import topLock from "./top-lock";
import topMenu from "./top-menu";
import topSearch from "./top-search";
import topTheme from "./top-theme";
import topLogs from "./top-logs";
import topColor from "./top-color";
import topLang from "./top-lang";
import {studentBaseUrl} from '@/config/env';

export default {
  components: {
    topLock,
    topMenu,
    topSearch,
    topTheme,
    topLogs,
    topColor,
    topLang
  },
  name: "top",
  data() {
    return {
      dialogPwdVisible:false,
      userPwd: {
        userName:"",
        password:"",
        passwords:""
      }
    };
  },
  filters: {},
  created() {},
  mounted() {
    listenfullscreen(this.setScreen);
  },
  computed: {
    ...mapState({
      userInfo: state => state.common.showDebug,
      showTheme: state => state.common.showTheme,
      showLock: state => state.common.showLock,
      showFullScren: state => state.common.showFullScren,
      showCollapse: state => state.common.showCollapse,
      showSearch: state => state.common.showSearch,
      showMenu: state => state.common.showMenu,
      showColor: state => state.common.showColor
    }),
    ...mapGetters([
      "userInfo",
      "isFullScren",
      "tagWel",
      "tagList",
      "isCollapse",
      "tag",
      "logsLen",
      "logsFlag"
    ]),
    timeOut: {
      set (val) {
        this.$store.state.timeout = val;
      },
      get() {
        return this.$store.state.timeout;
      }
    },
  },
  methods: {
    handleScreen() {
      fullscreenToggel();
    },
    setCollapse() {
      this.$store.commit("SET_COLLAPSE");
    },
    setScreen() {
      this.$store.commit("SET_FULLSCREN");
    },
    updatePassWord(){
      this.dialogPwdVisible = true;
      this.userPwd.userName = this.userInfo.account;
      this.userPwd.password = "";
      this.userPwd.passwords = "";
    },
    updatePwd(){
      if(this.userPwd.password == ''){
        this.$message({
          type: "warning",
          message: "请填写新密码"
        });
        return;
      }
      if(this.userPwd.passwords == ''){
        this.$message({
          type: "warning",
          message: "请填写确认密码"
        });
        return;
      }
      updatePasswordUserName(this.userPwd.userName,this.userPwd.password,this.userPwd.passwords).then(()=>{
        this.$message({
          type: "success",
          message: "操作成功!"
        });
        this.dialogPwdVisible = false;
      })
    },
    logout() {
      this.$confirm(this.$t("logoutTip"), this.$t("tip"), {
        confirmButtonText: this.$t("submitText"),
        cancelButtonText: this.$t("cancelText"),
        type: "warning"
      }).then(() => {
        //清除刷新定时
        if ( this.timeOut ) {
          clearInterval(this.timeOut);
        }
        this.$store.dispatch("LogOut").then(() => {
          this.$router.push({ path: "/login" });
        });
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.mobileCode:hover .mobileCode1{display: block;}
.mobileCode .mobileCode1{
  position: absolute;
  display: none;
  width: 172px;
  height: 212px;
  left: -77px;
  bottom: -198px;
  z-index: 999999;
}
</style>
