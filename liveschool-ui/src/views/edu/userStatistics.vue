
<template>
  <div>
    <basic-container>
        <avue-form :option="option" @submit="handleSubmit" v-model="form"></avue-form>
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
  import { getDetail , getDataByDay } from "@/api/edu/userStatistics"

  export default {
    name: "wel",
    components: {
      'v-chart': ECharts
    },
    data() {
      return {
        search:{},
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
        option:{
          submitText: '搜索',
          submitIcon:'el-icon-search',
          column: [{
            label: "时间",
            prop: "dateStatis",
            type: "select",
            span:6,
            hide:true,
            search:true,
            value: 1,
            dicData:[
              {
                label:'最近7天',
                value:1
              },{
                label:'最近一个月',
                value:2
              }]
            }]
        }
      };
    },
    computed: {
      ...mapGetters(["userInfo"]),

  },

  created() {
    this.init()
  },
  methods: {
    init() {
      getDetail("statadminuserdata").then(data => {
        let json = data.data.data.data;
        let jsonObj = JSON.parse(json);

        this.onlineOption.xAxis.data = jsonObj.data
        this.onlineOption.series[0].data = jsonObj.loginDataList
        this.onlineOption.series[1].data = jsonObj.viewDataList
        console.log(this.onlineOption)
        this.chartFlag = true
        this.onlineOption1 = this.onlineOption

    })
    },
    handleSubmit(form,done){
        if (this.form.dateStatis == '') {
          this.form.dateStatis = 1;
        }
      getDataByDay(this.form.dateStatis).then(data => {
        let json = data.data.data.data;
        let jsonObj = JSON.parse(json);

        this.onlineOption.xAxis.data = jsonObj.data
        this.onlineOption.series[0].data = jsonObj.loginDataList
        this.onlineOption.series[1].data = jsonObj.viewDataList
        console.log(this.onlineOption)
        this.chartFlag = true
        this.onlineOption1 = this.onlineOption
      })
      done()
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

