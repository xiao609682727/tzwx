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
