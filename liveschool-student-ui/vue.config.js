const PrerenderSPAPlugin = require('prerender-spa-plugin');
const Renderer = PrerenderSPAPlugin.PuppeteerRenderer;
const path = require('path');
// 代码压缩
const UglifyJsPlugin = require('uglifyjs-webpack-plugin')
function resolve(dir) {
    return path.join(__dirname, dir)
}
// gzip压缩
const CompressionPlugin = require('compression-webpack-plugin')
const productionGzipExtensions = /\.(js|css|json|txt|html|ico|svg)(\?.*)?$/i;
const Version = new Date().getTime();
const cdn = {
    js: [
        // vue必须在第一个
        'cdn/vue/2.6.11/vue.min.js',
        'cdn/vuex/3.1.3/vuex.min.js',
        'cdn/vue-router/3.1.6/vue-router.min.js',
        'cdn/axios/0.19.2/axios.min.js',
    ]
}
module.exports = {
    //部署应用包时的基本 URL
    publicPath: '/',
    //当运行 vue-cli-service build 时生成的生产环境构建文件的目录
    outputDir: 'dist',
    //放置生成的静态资源 (js、css、img、fonts) 的 (相对于 outputDir 的) 目录
    assetsDir: './' + Version + '/assets',
    // eslint-loader 是否在保存的时候检查 安装@vue/cli-plugin-eslint有效
    lintOnSave: false,
    //是否使用包含运行时编译器的 Vue 构建版本。设置true后你就可以在使用template
    runtimeCompiler: true,
    // 生产环境是否生成 sourceMap 文件 sourceMap的详解请看末尾
    productionSourceMap: false,
    /** 去掉hash */
    filenameHashing: false,
    chainWebpack: config => {

        if (process.env.NODE_ENV === 'tongzhou') {
            config.plugins.delete("prefetch").delete("preload")
            //js文件最小化处理
            config.optimization.minimize(true)
            config.plugin('html').tap(args => {
                args[0].cdn = cdn
                return args
            })

            // ============压缩图片 start============
            config.module
                .rule('images')
                .test(/\.(png|jpe?g|gif|svg)(\?.*)?$/)
                .use('image-webpack-loader')
                .loader('image-webpack-loader')
                .options({ bypassOnDebug: true })
                .end()
            // ============压缩图片 end============
        }

        /* 添加分析工具*/
            // config
            //     .plugin('webpack-bundle-analyzer')
            //     .use(require('webpack-bundle-analyzer').BundleAnalyzerPlugin)
            //     .end();
            // config.plugins.delete('prefetch')

    },
  css: {
  modules: false,  // 启用 CSS modules
  extract: true,// 是否使用css分离插件
  sourceMap: false,// 开启 CSS source maps?
    requireModuleExtension: true,
    loaderOptions: {
      scss: {
        prependData: '@import "~@/assets/css/base-scss.scss";'
      }
    }
  },configureWebpack: config => {
        if (process.env.NODE_ENV === 'tongzhou') {
            config.plugins.push(
                new CompressionPlugin({
                    filename: '[path].gz[query]',
                    algorithm: 'gzip',
                    test: productionGzipExtensions,
                    threshold: 10240, // 只有大小大于该值的资源会被处理 10240
                    minRatio: 0.8, // 只有压缩率小于这个值的资源才会被处理
                    deleteOriginalAssets: false // 删除原文件
                })
            )
            config.plugins.push(
                // 使用UglifyJsPlugin去掉console 可以略微降低文件大小
                new UglifyJsPlugin({
                    uglifyOptions: {
                        compress: {
                            drop_debugger: true,
                            drop_console: true,//生产环境自动删除console
                            pure_funcs: ['console.log']
                        }
                    },
                    sourceMap: false,
                    parallel: true//使用多进程并行运行来提高构建速度
                })

            )
            config.externals = {
                'vue': 'Vue',
                'vuex': 'Vuex',
                'vue-router': 'VueRouter',
                'axios': 'axios'
            }
            //seo预渲染插件，渲染首页和资讯列表
            return {
                plugins: [
                    // new PrerenderSPAPlugin({
                    //     // 生成文件的路径，也可以与webpakc打包的一致。
                    //     // 下面这句话非常重要！！！
                    //     // 这个目录只能有一级，如果目录层次大于一级，在生成的时候不会有任何错误提示，在预渲染的时候只会卡着不动。
                    //     staticDir: path.join(__dirname,'dist'),
                    //     // 对应自己的路由文件，比如a有参数，就需要写成 /a/param1。
                    //     routes: ['/', '/article/list'],
                    //     //压缩html
                    //     minify: {
                    //         collapseBooleanAttributes: true,
                    //         collapseWhitespace: true,
                    //         decodeEntities: true,
                    //         keepClosingSlash: true,
                    //         sortAttributes: true
                    //     },
                    //     // 这个很重要，如果没有配置这段，也不会进行预编译
                    //     renderer: new Renderer({
                    //         inject: {
                    //             foo: 'bar'
                    //         },
                    //         headless: false,
                    //         renderAfterTime: 5000,
                    //         // 在 main.js 中 document.dispatchEvent(new Event('render-event'))，两者的事件名称要对应上。
                    //         renderAfterDocumentEvent: 'render-event'
                    //     })
                    // }),
                ],
            };
        }



        // 公共代码抽离
        // config.optimization = {
        //     splitChunks: {
        //         cacheGroups: {
        //             vendor: {
        //                 chunks: 'all',
        //                 test: /node_modules/,
        //                 name: 'vendor',
        //                 minChunks: 1,
        //                 maxInitialRequests: 5,
        //                 minSize: 0,
        //                 priority: 100
        //             },
        //             common: {
        //                 chunks: 'all',
        //                 test: /[\\/]src[\\/]js[\\/]/,
        //                 name: 'common',
        //                 minChunks: 2,
        //                 maxInitialRequests: 5,
        //                 minSize: 0,
        //                 priority: 60
        //             },
        //             styles: {
        //                 name: 'styles',
        //                 test: /\.(sa|sc|c)ss$/,
        //                 chunks: 'all',
        //                 enforce: true
        //             },
        //             runtimeChunk: {
        //                 name: 'manifest'
        //             }
        //         }
        //     }
        // }
    }

}
