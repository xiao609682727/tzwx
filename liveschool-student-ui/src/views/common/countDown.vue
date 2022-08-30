<template>
  <div class="count_down">{{ hourString+':'+minuteString+':'+secondString  }}</div>
</template>
<script>
  import { validatenull } from '@/util/validate';


  export default {
    name: 'countDown',
    data() {
      return {
        hour: '',
        minute: '',
        second: '',
        promiseTimer: '',
        time:0
      }
    },
    methods: {

      countDowm () {
        var self = this
        clearInterval(this.promiseTimer)
        this.promiseTimer = setInterval(function () {
          self.time ++;
          self.$emit('timer')
          if (self.hour === 0) {
            if (self.minute !== 0 && self.second === 0) {
              self.second = 59
              self.minute -= 1
            } else if (self.minute === 0 && self.second === 0) {
              self.second = 0
              self.time = self.remainTime
              self.$emit('countDowmEnd', true)
              clearInterval(self.promiseTimer)
            } else {
              self.second -= 1
            }
          } else {
            if (self.minute !== 0 && self.second === 0) {
              self.second = 59
              self.minute -= 1
            } else if (self.minute === 0 && self.second === 0) {
              self.hour -= 1
              self.minute = 59
              self.second = 59
            } else {
              self.second -= 1
            }
          }
        }, 1000)
      },
      play(){
        this.countDowm()
      },
      pause(){
        clearInterval(this.promiseTimer)
      },
      formatNum (num) {
        return num < 10 ? '0' + num : '' + num
      },

    },
    props: {
      remainTime: {    // 倒计时间总秒数
        default: ''
      }
    },
    mounted:function() {
      console.log(this.remainTime)
      if (this.remainTime > 0) {
        this.hour = Math.floor((this.remainTime / 3600) % 24)
        this.minute = Math.floor((this.remainTime / 60) % 60)
        this.second = Math.floor(this.remainTime % 60)
        this.countDowm()
      }
    },
    computed: {
      hourString () {
        return this.formatNum(this.hour)
      },
      minuteString () {
        return this.formatNum(this.minute)
      },
      secondString () {
        return this.formatNum(this.second)
      }
    }
  }
</script>

<style scoped>



</style>
