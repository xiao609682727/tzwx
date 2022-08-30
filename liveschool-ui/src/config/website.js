/**
 * 全局配置文件
 */
export default {
  title: "网校系统",
  indexTitle: '网校管理平台',
  clientId: 'school', // 客户端id
  clientSecret: 'school_secret', // 客户端密钥
  tenantMode: false, // 是否开启租户模式
  captchaMode: true, // 是否开启验证码模式
  logo: "S",
  key: 'school',//配置主键,目前用于存储
  lockPage: '/lock',
  tokenTime: 3600,
  //http的status默认放行不才用统一处理的,
  statusWhiteList: [],
  //配置首页不可关闭
  isFirstPage: false,
  aliyunAccount: "1367689850580798",
  fistPage: {
    label: "首页",
    value: "/wel/index",
    params: {},
    query: {},
    meta: {
      i18n: 'dashboard'
    },
    group: [],
    close: false
  },
  //配置菜单的属性
  menu: {
    iconDefault: 'iconfont icon-caidan',
    props: {
      label: 'name',
      path: 'path',
      icon: 'source',
      children: 'children'
    }
  }
}
