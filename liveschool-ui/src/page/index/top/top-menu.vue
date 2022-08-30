<template>
  <div class="top-menu">
    <el-menu :default-active="activeIndex"
             mode="horizontal"
             text-color="#333">
      <template v-for="(item,index) in items">
        <el-menu-item :index="item.parentId+''"
                      @click.native="openMenu(item)"
                      :key="index">
          <template slot="title">
            <i :class="item.icon"></i>
            <span>{{generateTitle(item)}}</span>
          </template>
        </el-menu-item>
      </template>
    </el-menu>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
export default {
  name: "top-menu",
  data() {
    return {
      activeIndex: "0",
      items: []
    };
  },
  created() {
    this.getMenu();
  },
  computed: {
    ...mapGetters(["tagCurrent", "menu"])
  },
  methods: {
    getMenu() {
      this.$store.dispatch("GetTopMenu").then(res => {
        this.items = res;
        if(res.length > 0){
          this.$store.dispatch("GetMenu", res[0].parentId).then(data => {
            if (data.length !== 0) {
              this.$router.$avueRouter.formatRoutes(data, true);
            }
          });
        }

      });
    },
    generateTitle(item) {
      return this.$router.$avueRouter.generateTitle(
        item.label,
        (item.meta || {}).i18n
      );
    },
    openMenu(item) {
      this.$store.dispatch("GetMenu", item.parentId).then(data => {
        if (data.length !== 0) {
          this.$router.$avueRouter.formatRoutes(data, true);
        }
        //判断当为首页项时跳转到欢迎页面
        if (item.path == "/wel/index") {
          this.$router.push({
            path: this.$router.$avueRouter.getPath({
              name: item.label,
              src: item.path,
              // i18n: itemActive.meta.i18n
            })
          });
        }

      });
    }
  }
};
</script>
