<template>
  <basic-container>
    <avue-crud :option="option"
               :table-loading="loading"
               :data="data"
               :page="page"
               :search.sync="search"
               v-model="form"
               ref="crud"
               @search-change="searchChange"
               @refresh-change="refreshChange"
               @on-load="onLoad">
      <template slot="menuLeft">
        <el-button type="warning"
                   size="small"
                   plain
                   icon="el-icon-download"
                   @click="handleExport">导出
        </el-button>
      </template>
    </avue-crud>
  </basic-container>
</template>

<script>
  import {getList} from "@/api/edu/orderStatistics";
  import {mapGetters} from "vuex";
  import {getToken} from '@/util/auth';

  export default {
    data() {
      return {
        orderId:"",
        orderType:"",
        form: {},
        search:{},
        query: {},
        loading: true,
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        createTime:"",
        selectionList: [],
        option: {
          tip: false,
          showSummary: true,
          border: true,
          align: "center",
          index: false,
          viewBtn: false,
          addBtn:false,
          editBtn: false,
          delBtn:false,
          selection: false,
          sumColumnList: [
            {
              label:'共成交量（单）:',
              name: 'volume',
              type: 'sum'
            },
            {
              label:'共收入金额（元）:',
              name: 'actualIncome',
              type: 'sum'
            }
          ],
          column: [
            {
              label: "时间",
              prop: "createTime",
              type: "datetime",
              format:"yyyy-MM-dd",
              valueFormat:"yyyy-MM-dd",
            },{
              label: "成交量",
              prop: "volume",
            },
            {
              label: "时间",
              prop: "dateStatis",
              type: "month",
              format:"yyyy-MM",
              valueFormat:"yyyy-MM",
              searchValue:this.getFirstDayOfMonth(new Date()),
              search: true,
              hide:true,
              showColumn:false,
            },
            {
              label: "真实交易金额",
              prop: "actualIncome",
            }
          ]
        },
        data: []
      };
    },
    computed: {
      ...mapGetters(["permission"]),
      ids() {
        let ids = [];
        this.selectionList.forEach(ele => {
          ids.push(ele.id);
        });
        return ids.join(",");
      }
    },
    methods: {
      onLoad(page, params = {}) {
        this.loading = true;
        getList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
          this.page.total = data.total;
          this.data = data.records;
          this.loading = false;
        });
      },
      refreshChange() {
        this.onLoad(this.page, this.query);
      },
      handleExport() {
        this.$confirm("是否导出用户订单数据?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          window.open(`/api/edu/orders/export-orders?crazy-auth=${getToken()}&startTime=`+this.search.dateStatis);
        });
      },
      searchChange(params,done) {
        this.query = params;
        this.page.currentPage = 1;
        this.onLoad(this.page, params);
        done()

      },
      timeFormat: function (date) {
        var y = date.getFullYear(); //年
        var m = date.getMonth() + 1; //月
        var d = date.getDate(); //日
        if(m<10){
          if(d<10){
            return y + "-0" + m + "-0" + d;
          }else {
            return y + "-0" + m + "-" + d;
          }
        }else {
          if(d<10){
            return y + "-" + m + "-0" + d;
          }else {
            return y + "-" + m + "-" + d;
          }
        }
      },
      //获取当月第一天
      getFirstDayOfMonth: function (date) {
        date.setDate(1);
        return this.timeFormat(date);
      }
    },
  };
</script>

<style lang="scss">
  #app {
    width: 100%;
    height: 100%;
    overflow: visible;
  }
</style>
