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
                <li class="course-nav-item " v-on:click="clickType(1)" v-bind:class="{'on' :typeActive == 1}">
                  <a href="javascript:void(0)">点播</a>
                </li>
                <li class="course-nav-item " v-on:click="clickType(2)" v-bind:class="{'on' :typeActive == 2}">
                  <a href="javascript:void(0)">直播</a>
                </li>
                <li class="course-nav-item " v-on:click="clickType(3)" v-bind:class="{'on' :typeActive == 3}">
                  <a href="javascript:void(0)">班级</a>
                </li>
                <li class="course-nav-item " v-on:click="clickFaceTeaching()" v-bind:class="{'on' :typeActive == 4}">
                  <a href="javascript:void(0)">面授</a>
                </li>
              </ul>
            </div>
          </div>
          <div class="course-nav-row clearfix">
            <span class="hd l">地域：</span>
            <div class="bd">
              <ul class="">
                <li class="course-nav-item" v-on:click="addressList(0)" v-bind:class="{'on' :addressActive == 0}" >
                  <a href="javascript:void(0)">全部</a>
                </li>
                <li class="course-nav-item" v-on:click="addressList(subject.id)" v-bind:class="{'on' :addressActive == subject.id}" v-for="subject in addressTree" :key="subject.id">
                  <a href="javascript:void(0)" data-ct="fe">{{subject.title}}</a>
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
                <li class="course-nav-item" v-on:click="clickSubLevel1(subject.id)" v-bind:class="{'on' :subLevel1Active == subject.id}" v-for="subject in subjectTree" :key="subject.id">
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
  import {subjectTree,subjectLevel2List,addressTree} from "@/api";

  export default {
    name: 'selectCourseSubject',
    data() {
      return {
        searchCourseName: "",
        subjectTree:getStore({ name: 'subjectTree' }) || [],
        addressTree:getStore({ name: 'addressTree' }) || [],
        subjectLevel2All:[],
        subjectLevel2:[],
        subLevel1Active:0,
        subLevel2Active:0,
        addressActive:0,
        typeActive:-1,
      }
    },
    methods: {
      init(){
        this.searchCourseName = this.$route.query.searchCourseName;
        this.typeActive = 4;
        this.subLevel1Active = this.$route.query.subLevel1;
        this.subLevel2Active = this.$route.query.subLevel2;
        this.addressActive=this.$route.query.addressActive;
        if (typeof(this.subLevel1Active) == "undefined"){
          this.subLevel1Active = "0"
        }
        if (typeof(this.addressActive) == "undefined"){
          this.addressActive = "0"
        }
        if (typeof(this.subLevel2Active) == "undefined"){
          this.subLevel2Active = "0"
        }
        if (typeof(this.searchCourseName) == "undefined"){
          this.searchCourseName = ""
        }

        if(this.subjectTree.length == 0){
          subjectTree().then(res =>{
            let subject2All = []
            this.subjectTree = res.data.data;
            setStore({ name: 'subjectTree', content: this.subjectTree,type: 'session' })
            this.subjectTree.forEach(subject =>{
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
          this.subjectTree.forEach(subject =>{
            if (typeof(subject.children) != "undefined"){
              subject2All = subject2All.concat(subject.children)
              if(this.subLevel1Active == subject.id){
                this.subjectLevel2 = subject.children;
              }
            }
          })
          this.subjectLevel2All = subject2All;
        }
        if(this.addressTree.length == 0){
          addressTree().then(res =>{
            this.addressTree = res.data.data;
            setStore({ name: 'addressTree', content: this.addressTree,type: 'session' })
          })
        }
        if(this.subLevel1Active == "0"){
          subjectLevel2List().then(res =>{
            this.subjectLevel2All = res.data.data;
            this.subjectLevel2 = res.data.data;
          })
        }
      },
      clickType(type){
        this.$router.push({ path: "/course/courseList",query:{searchCourseName:this.searchCourseName,courseType:type,subLevel1:this.subLevel1Active,subLevel2:this.subLevel2Active} });
      },
      clickFaceTeaching(){
        this.$router.push({ path: "/lineDownCourse/list"});
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
        this.$router.push({ path: "/lineDownCourse/list",query:{searchCourseName:this.searchCourseName,courseType:this.typeActive,subLevel1:this.subLevel1Active,subLevel2:this.subLevel2Active,addressActive:this.addressActive} });
      },
      clickSubLevel2(subjectId){
        this.subLevel2Active = subjectId;
        this.$router.push({ path: "/lineDownCourse/list",query:{searchCourseName:this.searchCourseName,courseType:this.typeActive,subLevel1:this.subLevel1Active,subLevel2:this.subLevel2Active,addressActive:this.addressActive} });
      },
      addressList(subjectId){
        this.addressActive = subjectId;
        this.$router.push({ path: "/lineDownCourse/list",query:{searchCourseName:this.searchCourseName,courseType:this.typeActive,subLevel1:this.subLevel1Active,subLevel2:this.subLevel2Active,addressActive:this.addressActive} });
      },
    },
    mounted:function() {
      this.init();
    },
  }
</script>

<style scoped>

</style>
