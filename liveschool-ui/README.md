# 安装依赖
yarn install
# 启动
yarn run serve     
# 编译
npm run build
#项目结构
```
|-- liveschool-ui
    |-- package-lock.json
    |-- package.json //模块的描述文件
    |-- vue.config.js //vue配置文件
    |-- dist //编译后的目录文件夹
    |-- public //静态资源路径
    |   |-- index.html
    |-- src //
        |-- main.js
        |-- config
            |-- env.js //系统接口请求配置
```
#### 文件结构

#### 核心技术
- 前端技术栈 `vue`、`vuex`、`vue-router`、`vue-cli`、`axios`

2022-10-14 李桤翔
解决了试题判断题添加后出现的显示错误。
2022-10-28 南辰夕
解决更换网址管理端登录cookie值覆盖用户端cookie值的问题。
2022-10-28 南辰夕
1、解决"财务管理-用户账号管理-交易记录"的导出问题。
2、解决"财务管理-订单管理"有筛选条件或清空后导出文件异常。
3、解决学生管理批量导入的用户手机检测问题。
