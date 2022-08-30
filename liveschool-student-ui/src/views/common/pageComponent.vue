<template>
  <div class="page" v-if="this.page.pages != 0" >
    <span class="disabled_page" v-if="page.current == 1" >首页</span>
    <span class="disabled_page" v-if="page.current == 1" >上一页</span>
    <a  v-if="page.current != 1" href="javascript:void(0)" v-on:click="goto(1)">首页</a>
    <a  v-if="page.current != 1" href="javascript:void(0)" v-on:click="goto(page.current-1)" >上一页</a>
    <a v-on:click="goto(i)" v-bind:class="{'active' :page.current == i}" href="javascript:void(0)" class="text-page-tag" v-for="i in pageList" :key="i">{{i}}</a>
    <a href="javascript:void(0)" v-if="page.current < page.pages" v-on:click="goto(page.current +1)">下一页</a>
    <a href="javascript:void(0)" v-if="page.current < page.pages" v-on:click="goto(page.pages)">尾页</a>
    <span class="disabled_page" v-if="page.current >= page.pages" >下一页</span>
    <span class="disabled_page" v-if="page.current >= page.pages" >尾页</span>
  </div>
</template>

<script>
  export default {
    name: 'pageComponent',
    data() {
      return {
        pageList:[]
      }
    },
    methods: {
      init(){

      },
      goto(i) {
        this.$emit('goto',i)
      },
      computePage(){
        this.pageList = []
        if(this.page.pages<=7){
          for (let i = 1; i <= this.page.pages; i++) {
            this.pageList.push(i)
          }
        }else{
          if(this.page.current <= 4){
            this.pageList = [1,2,3,4,5,6,7];
          }else if(this.page.current+3<=this.page.pages) {
            let n = this.page.current
            this.pageList = [n-3,n-2,n-1,n,n+1,n+2,n+3];
          }else{
            this.pageList = [this.page.pages-6,this.page.pages-5,this.page.pages-4,this.page.pages-3,this.page.pages-2,this.page.pages-1,this.page.pages];
          }
        }
      }
    },
    mounted:function() {
    },
    props:{
      page: Object,
    },
    watch: {
      page: {
        deep: true,
        handler(newVal){
          this.computePage()
        }
      }
    },
  }
</script>

<style scoped>
</style>
