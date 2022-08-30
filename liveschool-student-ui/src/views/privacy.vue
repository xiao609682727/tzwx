<template>
  <div v-html="content" class="privacy">

  </div>
</template>

<script>
  import {websiteProfile} from "@/api";

export default {
  name: 'private',
  data () {
    return {
      content:""
    }
  },
  methods: {
    init() {
      websiteProfile("privacy").then(res =>{
        this.content = res.data.data.privacy
      })
    }
  },
  mounted:function() {
    this.init()
  },
}
</script>

<style scoped>
.privacy{
  margin: 20px;
}
</style>
