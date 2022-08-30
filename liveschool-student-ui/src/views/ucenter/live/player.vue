<template>
  <div style="height: 100%">
    <player-header :courseKpoint="this.courseKpoint"></player-header>
    <player-content :courseKpoint="this.courseKpoint"></player-content>
    <common-footer></common-footer>
  </div>
</template>

<script>
import playerHeader from '@/views/ucenter/player/playerHeader.vue'
import playerContent from '@/views/ucenter/player/playerContent.vue'
import commonFooter from '@/views/ucenter/player/footer'
import {courseInfo,courseKpoint} from "@/api";

export default {
  name: 'player',
  data () {
    return {
      id:"",
      courseKpoint:{}
    }
  },components:{
    playerHeader,playerContent,commonFooter
  },
  methods: {
    init(){
      let id = this.$route.params.id;
      this.id = id
      courseKpoint(id).then(res =>{
        this.courseKpoint = res.data.data
      })
    }
  },
  mounted:function() {
    this.init()
  }
}
</script>

<style scoped>
</style>
