<template>
  <div>
    <common-header :headNav="headNav"></common-header>
    <div class="main">
      <router-view/>
    </div>
    <common-footer></common-footer>
  </div>
</template>

<script>
import commonHeader from '@/views/common/header'
import commonFooter from '@/views/common/footer'

export default {
  name: 'articlei',
  data () {
    return {
      subjectIdLevel1:"",
      subjectIdLevel2:"",
      type:"",
      headNav:"/article/list"
    }
  },components:{
    commonHeader,commonFooter
  },
  methods: {
  }
}
</script>

<style scoped>

</style>
