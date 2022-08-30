<template>
  <div>
    <common-header></common-header>
    <div class="main">
<!--      <ucenter-header></ucenter-header>-->
      <ucneter-content></ucneter-content>
      <div class="clear"></div>
    </div>
    <common-footer></common-footer>
  </div>
</template>

<script>
import commonHeader from '@/views/common/header'
import commonFooter from '@/views/common/footer'
import ucneterContent from '@/views/ucenter/common/ucneterContent'
export default {
  name: 'myCourse',
  data () {
    return {
      isReg: false,
      name: '',
      password: '',
      repeat: ''
    }
  },components:{
    commonHeader,commonFooter,ucneterContent
  },
  methods: {

  }
}
</script>

<style scoped>

</style>
