<template>
  <div id="app"  v-wechat-title="headerTitle">
    <router-view :key="$route.fullPath" />
  </div>
</template>

<script>

  export default {
    data () {
      return {
        headerTitle:"",
      }
    },
    watch: {
      $route: {
        deep: true,
        handler(to, from) {
          //获取后台的title信息
          let routeTitle = this.$route.meta.title;
          let routeKeywords = this.$route.meta.keywords;
          let routeDescription = this.$route.meta.description;
          /* 路由发生变化修改页面title,keywords,description,author */
          //从后台取出动态网站信息
          this.$store.dispatch("getWebsiteConfig").then(res =>{
            let title ="";
            let keywords =this.$store.getters.website.keywords;
            let description =this.$store.getters.website.description;
            let author =this.$store.getters.website.author;
            let ico =this.$store.getters.website.ico;
            //如果路由页面自定义则使用自定义的如果没有定义则取全站的
            if (routeTitle) {
              title = routeTitle;
            }
            if (this.$store.getters.website.title) {
              title = title+"-"+this.$store.getters.website.title;
            }
            if (routeKeywords) {
              keywords = routeKeywords;
            }
            if (routeDescription) {
              description = routeDescription;
            }
            if (this.$route.meta.author) {
              author = this.$route.meta.author;
            }
            //js控制页面 title,keywords,description,author
            this.headerTitle = title;
            document.querySelector('meta[name="keywords"]').setAttribute('content',keywords)
            document.querySelector('meta[name="description"]').setAttribute('content', description)
            document.querySelector('meta[name="author"]').setAttribute('content', author)

            var link = document.createElement('link');
            link.type = 'image/x-icon';
            link.rel = 'shortcut icon';
            link.href = ico;
            document.getElementsByTagName('head')[0].appendChild(link);
          })
        }
      }
    },mounted:function() {
      let flag = navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i)
      if(flag){
        //跳转H5的链接
        var urlNew="";
        var urlOld = location.href;  //url整块地址
        if(urlOld.indexOf("courseInfo")!=-1||urlOld.indexOf("info")!=-1||urlOld.indexOf("lineInfo")!=-1||urlOld.indexOf("lineDownCourse")!=-1||urlOld.indexOf("article")!=-1||urlOld.indexOf("courseList")!=-1){
          debugger
          var newUrls = urlOld.replace(/.*\/([^\/]+\/[^\/]+)$/, '$1');//去除掉域名的路径+参数
          if(newUrls!=undefined&&newUrls!=null&&newUrls!=""){
            //如果里面包含？说明是带参数的需要进行带参跳转。
            if(newUrls.indexOf("?")!=-1){
              var newUrlNums = newUrls.indexOf("?");//找到参数的位置
              var  url = newUrls.substring(0,newUrlNums);  //进行裁切。获取路由地址。
              var paramsObj = {};//参数对象
              var params = urlOld.split("?")[1].split("&");//获取参数
              params.map(v => paramsObj[v.split("=")[0]] = v.split("=")[1]);//将参数转为对象后面好获取
              console.log(paramsObj);
              console.log("参数"+params.courseType);
              if(url=="course/courseList"||url=="lineDownCourse/list"){
                var returnParams="";
                if(paramsObj.courseType!=undefined&&paramsObj.courseType!=null&&paramsObj.courseType!=""){
                  returnParams+="sellType="+paramsObj.courseType;
                }
                if(paramsObj.subLevel2!=undefined&&paramsObj.subLevel2!=null&&paramsObj.subLevel2!=""&&paramsObj.subLevel2!="0"){
                  returnParams+="&subjectId="+paramsObj.subLevel2;
                }
                if(paramsObj.addressActive!=undefined&&paramsObj.addressActive!=null&&paramsObj.addressActive!=""){
                  returnParams+="&addressId="+paramsObj.addressActive;
                }
                if(paramsObj.courseType=="4"){
                  urlNew="/schoolapp/#/pages/course/listLine?"+returnParams;
                }else {
                  urlNew="/schoolapp/#/pages/course/list?"+returnParams;
                }
              }
            }
            else {
              //不带？说明是链接里拼接参数，需要在一次进行裁切。
              var paramsNumS = newUrls.indexOf("/");//找到需要裁切的位置
              var urlNoParams = newUrls.substring(0,paramsNumS);
              var params=newUrls.substring(paramsNumS+1,newUrls.length);
              if(urlNoParams=="courseInfo"){
                //课程详情
                urlNew="/schoolapp/#/pages/course/detail?courseId="+params;
              }else if(urlNoParams=="info"){
                //资讯详情
                urlNew="/schoolapp/#/pages/article/detail?id="+params;
              }else if(urlNoParams=="lineInfo"){
                //面授详情
                urlNew="/schoolapp/#/pages/course/detailLine?courseId="+params;
              }else if(urlNoParams=="lineDownCourse"){
                //面授列表
                urlNew="/schoolapp/#/pages/course/listLine?sellType=4";
              }else if(urlNoParams=="article"){
                //资讯列表
                urlNew="/schoolapp/#/pages/article/list";
              }else {
                urlNew="/schoolapp/#/";
              }
            }
          }else {
            urlNew="/schoolapp/#/";
          }
        }else {
          urlNew="/schoolapp/#/";
        }
        //如果本地需要这里加入启动的链接，否则访问不到
        //window.location.href="http://localhost:8081"+urlNew
        window.location.href=urlNew
      }
      this.$store.dispatch("setTime");
    },
  }
</script>

<style lang="scss">

@import "../public/static/css/common-less1.scss";
@import "../public/static/css/exam-scss.scss";

</style>
