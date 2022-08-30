<template>
  <div class="wrap">
    <div class="top">
      <!--<div class="course-top clearfix">
        <div class="course-top-search">
          <div class="search-area" data-search="top-banner">
            <input class="search-input" data-suggest-trigger="suggest-trigger" placeholder="搜索感兴趣的内容" type="text"
                   autocomplete="off">
            <input type="hidden" class="btn_search" data-search-btn="search-btn">
            <ul class="search-area-result js-search-wrap" data-suggest-result="suggest-result">
            </ul>
          </div>
          <div class="showhide-search" data-show="no"><i class="icon-search"></i></div>
        </div>
      </div>-->
      <div class="course-content">
        <div class="course-nav-box">
          <div class="course-nav-row clearfix ">
            <span class="hd l">类型：</span>
            <div class="bd">
              <ul class="">
                <li class="course-nav-item" v-on:click="clickType(0)" v-bind:class="{'on' :typeActive == 0}">
                  <a href="javascript:void(0)">全部</a>
                </li>
                <li class="course-nav-item " v-on:click="clickType(paperType.id)" v-bind:class="{'on' :typeActive == paperType.id}" v-for="paperType in paperTypeList" :key="paperType.id">
                  <a href="javascript:void(0)">{{ paperType.title }}</a>
                </li>
              </ul>
            </div>
          </div>
          <div class="course-nav-row clearfix">
            <span class="hd l">分类：</span>
            <div class="bd">
              <ul class="">
                <li class="course-nav-item" v-on:click="clickSubLevel1(0)" v-bind:class="{'on' :subLevel1Active == 0}" >
                  <a href="javascript:void(0)">全部</a>
                </li>
                <li class="course-nav-item" v-on:click="clickSubLevel1(subject.id)" v-bind:class="{'on' :subLevel1Active == subject.id}" v-for="subject in subjectExamTree" :key="subject.id">
                  <a href="javascript:void(0)" data-ct="fe">{{subject.title}}</a>
                </li>
              </ul>
            </div>
          </div>

          <div class="course-nav-row course-nav-skills clearfix border_bottom_none">
            <div class="js-course-skills " v-bind:class="{'course-skills-box' :subjectLevel2.length > 29}">
              <span class="hd l"></span>
              <div class="bd">
                <ul class="">
                  <li class="course-nav-item"  v-on:click="clickSubLevel2(0)" v-bind:class="{'on' :subLevel2Active == 0}">
                    <a href="javascript:void(0)">全部</a>
                  </li>
                  <li class="course-nav-item" v-on:click="clickSubLevel2(subject.id)" v-bind:class="{'on' :subLevel2Active == subject.id}" v-for="subject in subjectLevel2" :key="subject.id">
                    <a href="javascript:void(0)" data-id="6303" data-ct="tyscript">{{subject.title}}</a>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import { setStore, getStore } from '@/util/store'
  import {subjectExamTree, subjectExamList, paperTypeList} from "@/api";

  export default {
    name: 'selectCourseSubject',
    data() {
      return {
        searchExamName: "",
        subjectExamTree:getStore({ name: 'subjectExamTree' }) || [],
        subjectLevel2All:[],
        subjectLevel2:[],
        subLevel1Active:0,
        subLevel2Active:0,
        typeActive:0,

      }
    },
    methods: {
      init(){

        this.searchExamName = this.$route.query.searchExamName;
        this.typeActive = this.$route.query.examType;
        this.subLevel1Active = this.$route.query.subLevel1;
        this.subLevel2Active = this.$route.query.subLevel2;
        if (typeof(this.subLevel1Active) == "undefined"){
          this.subLevel1Active = "0"
        }
        if (typeof(this.subLevel2Active) == "undefined"){
          this.subLevel2Active = "0"
        }
        if (typeof(this.searchExamName) == "undefined"){
          this.searchExamName = ""
        }

        if(this.subjectExamTree.length == 0){
          subjectExamTree().then(res =>{
            let subject2All = []
            this.subjectExamTree = res.data.data;
            setStore({ name: 'subjectExamTree', content: this.subjectExamTree,type: 'session' })
            this.subjectExamTree.forEach(subject =>{
              if (typeof(subject.children) != "undefined"){
                subject2All = subject2All.concat(subject.children)
                if(this.subLevel1Active == subject.id){
                  this.subjectLevel2 = subject.children;
                }
              }
            })

            this.subjectLevel2All = subject2All;
          })
        }else{
          let subject2All = []
          this.subjectExamTree.forEach(subject =>{
            if (typeof(subject.children) != "undefined"){
              subject2All = subject2All.concat(subject.children)
              if(this.subLevel1Active == subject.id){
                this.subjectLevel2 = subject.children;
              }
            }
          })
          this.subjectLevel2All = subject2All;
        }

        if(this.subLevel1Active == "0"){
          subjectExamList().then(res =>{
            this.subjectLevel2All = res.data.data;
            this.subjectLevel2 = res.data.data;
          })
        }
      },
      clickType(type){
        this.$router.push({ path: "/exam/examList",query:{searchExamName:this.searchExamName,examType:type,subLevel1:this.subLevel1Active,subLevel2:this.subLevel2Active} });
      },
      sortList(subject2All){
        let all = []
        for (let subject2AllKey in subject2All) {
          let subj = subject2AllKey;
          for (let key in subject2All) {
            if(subj.sort < key.sort){
              subj = key;
            }
          }
          all.push(subj)
        }
        return all;
      },
      clickSubLevel1(subjectId){
        this.subLevel1Active = subjectId;
        this.subLevel2Active = 0;

        this.$router.push({ path: "/exam/examList",query:{searchExamName:this.searchExamName,examType:this.typeActive,subLevel1:this.subLevel1Active,subLevel2:this.subLevel2Active} });
      },
      clickSubLevel2(subjectId){
        this.subLevel2Active = subjectId;
        this.$router.push({ path: "/exam/examList",query:{searchExamName:this.searchExamName,examType:this.typeActive,subLevel1:this.subLevel1Active,subLevel2:this.subLevel2Active} });
      }
    },
    mounted:function() {
      this.init();
    },
    props:{
      paperTypeList: Object,
    },
    watch: {
      paperTypeList: {
        deep: true,
        handler(newVal){
          console.log(newVal)
        }
      },
    },
  }
</script>

<style scoped>

</style>
