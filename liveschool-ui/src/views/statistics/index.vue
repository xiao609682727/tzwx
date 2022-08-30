<template>
  <div v-loading="!show" element-loading-text="数据加载中..." :style="!show ? 'height: 500px' : 'height: 100%'" class="app-container">
    <div v-if="show">
      <div>
        <el-card class="box-card">
          <div>
            <v-chart :options="chinaInfo" />
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
import ECharts from 'vue-echarts'
import 'echarts/lib/chart/map'
import { getDetail } from '@/api/web/statdata'
// built-in theme
import 'echarts/theme/dark'
// custom theme
import theme from './theme.json'

// Map of China
import chinaMap from './china.json'

// registering map data
ECharts.registerMap('china', chinaMap)

// registering custom theme
ECharts.registerTheme('ovilia-green', theme)



export default {
  name: 'ServerMonitor',
  components: {
    'v-chart': ECharts
  },
  data() {
    return {
      cpuChart:null,
      memoryChart:null,
      show: false,
      monitor: null,
      max:0,
      data: {},
      chinaInfo: {
        title: {
          text: '用户地域分布',
          subtext: '部分用户无法获取地址,该图仅供参考',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        toolbox: {
          show: false,
          orient: 'vertical',
          left: 'right',
          top: 'center',
          feature: {
            dataView: {readOnly: true},
          }
        },
        visualMap: {
          min: 0,
          max: this.max,//根据用户数量调整此值
          left: 'left',
          top: 'bottom',
          text: ['高','低'],           // 文本，默认为数值文本
          calculable: true
        },
        series: [
          {
            name: '用户数量',
            type: 'map',
            mapType: 'china',
            roam: false,
            label: {
              normal: {
                show: true
              },
              emphasis: {
                show: true
              }
            },
            data:this.data
          }
        ]
      },
    }
  },
  created() {
    this.init()
  },
  destroyed() {
    clearInterval(this.monitor)
  },
  methods: {
    init() {
      getDetail("areadata").then(data => {
        this.show = true;
        let json = data.data.data.data;
        let jsonObj = JSON.parse(json);
        this.chinaInfo.series[0].data = jsonObj.areadata;
        this.chinaInfo.visualMap.max = jsonObj.total;
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .echarts {
    width: 100%;
    height: 600px;
  }
</style>
