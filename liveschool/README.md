w# crazy

#### 运行系统

1. clone 后台项目
2. 菜单 File -> Import，然后选择 Maven -> Existing Maven Projects，点击 Next> 按钮，选择工作目录，然后点击 Finish 按钮，即可成功导入
Eclipse会自动加载Maven依赖包，初次加载会比较慢（根据自身网络情况而定）
3. 创建数据库crazy并导入数据脚本sql/db.sql
4. 编辑resources目录下的application-dev.yml mysql 信息鱼redis 信息
5. 打开运行org.springcrazy.Application.java
6. 前端部署 打开前端项目目录
7. npm install
8. npm run serve 启动

#### 部署系统

mvn install 后编译出jar包

1. jar部署方式
使用命令行执行：java –jar crazy.jar 


#项目结构
```
|-- liveschool
    ├─pom.xml    //maven 配置文件
    ├─src
    │  └─main
    │      └─java     //项目代码     
    │      └─resources      //项目配置文件
```
#### 文件结构

#### 核心技术
- 后端技术栈 `spring boot`、`mybatis`、`mybatis plus`、`hutool`、`jwt`

2022-10-08（李桤翔）
1.修改学习记录中用户id无法获取到的问题。
2020-10-24(李桤翔)
1.修改试卷中删除最后一个报错的情况。
2022-10-28 南辰夕
1、解决"财务管理-用户账号管理-交易记录"的导出问题。
2、解决"财务管理-订单管理"有筛选条件或清空后导出文件异常。
3、解决学生管理批量导入的用户手机检测问题。
