<template>
  <div>
    <basic-container>
      <avue-data-tabs :option="option1"></avue-data-tabs>
    </basic-container>
<!--    <basic-container>
      <avue-data-rotate :option="option2"></avue-data-rotate>
    </basic-container>-->
    <basic-container>
      <div>
        <v-chart v-if="chartFlag" theme="light" :options="onlineOption1" />
      </div>

    </basic-container>

  </div>
</template>

<script>
  import {mapGetters} from "vuex";
  import ECharts from 'vue-echarts'
  import 'echarts/lib/chart/line'
  import 'echarts/lib/component/polar'
  // 提示
  import 'echarts/lib/component/tooltip'
  // 图例
  import 'echarts/lib/component/legend'
  // 标题
  import 'echarts/lib/component/title'
  import { getDetail } from '@/api/web/statdata'

  export default {
    name: "wel",
    components: {
      'v-chart': ECharts
    },
    data() {
      return {
        chartFlag:false,
        height:"400px",
        msg: 'Welcome to Your Vue.js App',
        onlineOption1:{},
        onlineOption:{
          title: {
            text: '在线用户统计',
            subtext: '人数'
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data:['登录人数','游客人数']
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: []
          },

          yAxis: {
            type: 'value'
          },
          series: [{
            name: '登录人数',
            type: 'line',
            smooth: true,
            data: []
          },
            {
              name: '游客人数',
              type: 'line',
              smooth: true,
              data: []
            }]
        },
        option1: {
          span:6,
          data: []
        },
        option2: {
          span: 8,
          data: [
            {
              click: function (item) {
                alert(JSON.stringify(item));
              },
              count: '150',
              title: '提现申请',
              icon: 'el-icon-warning',
              color: 'rgb(49, 180, 141)'
            }, {
              click: function (item) {
                alert(JSON.stringify(item));
              },
              count: '5',
              title: '意见建议',
              icon: 'el-icon-view',
              color: '#00a65a'
            }, {
              click: function (item) {
                alert(JSON.stringify(item));
              },
              count: '44',
              title: '今日直播',
              icon: 'el-icon-setting',
              color: '#f39c12'
            }
          ]
        },
      };
    },
    computed: {
      ...mapGetters(["userInfo"]),

    },
    mounted(){
    },
    created() {
      this.init()
    },
    methods: {
      init() {
        getDetail("statadmindata").then(data => {
          let json = data.data.data.data;
          let jsonObj = JSON.parse(json);
          this.option1.data = jsonObj
        })
        getDetail("statadminuserdata").then(data => {
          let json = data.data.data.data;
          let jsonObj = JSON.parse(json);

          this.onlineOption.xAxis.data = jsonObj.data
          this.onlineOption.series[0].data = jsonObj.loginDataList
          this.onlineOption.series[1].data = jsonObj.viewDataList
          this.chartFlag = true
          this.onlineOption1 = this.onlineOption

        })
      }
    }
  };
</script>

<style>
  .el-divider--horizontal {
    margin: 12px 0 !important;
  }

  .el-font-size {
    font-size: 14px;
  }
  .echarts {
    width: 100%;
    height: 400px;
  }
</style>

