<template>
  <div>
    <div id="head" class="sig-head">
      <a href="/" target="_self" class="">
        <img :src="website.loginLogo">
      </a>
    </div>
    <div id="main">
      <router-view/>
    </div>
    <div class="vright" v-html="website.copyright"></div>
  </div>
</template>

<script>
  import { mapGetters } from "vuex";


  export default {
    name: 'Login',
    data() {
      return {

      }
    },
    methods: {

    },
    computed: {
      ...mapGetters(["website"]),
    }
  }
</script>

<style scoped>

</style>
