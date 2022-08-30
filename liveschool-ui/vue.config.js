module.exports = {
  publicPath:"/admin",
  lintOnSave: true,
  productionSourceMap: false,
  chainWebpack: (config) => {
    //忽略的打包文件
    config.externals({
      'vue': 'Vue',
      'vue-router': 'VueRouter',
      'vuex': 'Vuex',
      'axios': 'axios',
      'element-ui': 'ELEMENT',
    })
    const entry = config.entry('app')
    entry
      .add('babel-polyfill')
      .end()
    entry
      .add('classlist-polyfill')
      .end()
    entry
      // .add('@/mock')
      .end()
  },
  devServer: {
    //设置静态路径
    //contentBase: 'D\:\\uploadfile',
    // 端口配置
    port: 1888,
    // 反向代理配置
    proxy: {
      '/api': {
        target: 'http://localhost:81',
        // target: 'https://wx.tongzhoushidai.com/api/',
        ws: true,
        pathRewrite: {
          '^/api': '/'
        }
      }
      ,'/upload': {
        target: 'http://localhost:81',
        //target: 'https://wx.tongzhoushidai.com/api/',
        ws: true,
        pathRewrite: {
          '^/upload': '/upload'
        }
      }
    }
  }
}
