<template>
  <basic-container>
    <div v-if="videosource.videoType == '1'" id="video-box" style="width:100%;height:500px">
    </div>
    <div v-if="videosource.videoType == '2'" id="main" style="width:100%;height:500px">
      <div class="bjc-player-wrapper">
        <video class="video video-js vjs-default-skin">
        </video>
      </div>
    </div>
    <div v-if="videosource.videoType == '3'" id="localvideo" style="width:100%;height:500px">
      <video ref='video' controls="controls" style="width:100%;height:500px"></video>
    </div>

  </basic-container>
</template>
<script src="../../../public/static/js/aliyunPlayer/2.8.2/aliplayer-min.js"></script>
<script>
  import {getDetail} from "@/api/edu/videosource";

  export default {

    data() {
      return {

      };
    },
    props:{
      videosource: Object,
    },
    watch: {
      videosource: {
        deep: true,
        handler(newVal){
          this.init();
        }
      },
    },
    mounted:function() {
      const oScript = document.createElement('script');
      oScript.type = 'text/javascript';
      oScript.src = '/admin/static/js/aliyunPlayer/2.8.2/aliplayer-min.js';
      document.body.appendChild(oScript);
      const link  = document.createElement('link');
      link.rel = 'stylesheet';
      link.href = '//g.alicdn.com/de/prismplayer/2.8.2/skins/default/aliplayer-min.css';
      link.type = 'text/css';
      document.body.appendChild(link);
      const bScript = document.createElement('script');
      bScript.type = 'text/javascript';
      bScript.src = '//live-cdn.baijiayun.com/www-video-jssdk/0.0.7/playback/player.js';
      document.body.appendChild(bScript);
      const blink  = document.createElement('link');
      blink.rel = 'stylesheet';
      blink.href = '//live-cdn.baijiayun.com/www-video-jssdk/0.0.7/playback/player.css';
      blink.type = 'text/css';
      document.body.appendChild(blink);
      this.init();
    },
    methods: {
      init(){
        let that = this
        getDetail(this.videosource.id).then(res => {
          let videosource = res.data.data;
          if(videosource.videoType == "1"){
              new Aliplayer({
                id: 'video-box',
                width: '100%',
                autoplay: true,
                height: '100%',
                vid : videosource.idVarchar,
                playauth : videosource.params.playAuth,
                // encryptType: 1,
                format: "mp4",
               "skinLayout": [
              {
                "name": "bigPlayButton",
                "align": "blabs",
                "x": 30,
                "y": 80
              },
              {
                "name": "H5Loading",
                "align": "cc"
              },
              {
                "name": "errorDisplay",
                "align": "tlabs",
                "x": 0,
                "y": 0
              },
              {
                "name": "infoDisplay"
              },
              {
                "name": "tooltip",
                "align": "blabs",
                "x": 0,
                "y": 56
              },
              {
                "name": "thumbnail"
              },
              {
                "name": "controlBar",
                "align": "blabs",
                "x": 0,
                "y": 0,
                "children": [
                  {
                    "name": "progress",
                    "align": "blabs",
                    "x": 0,
                    "y": 44
                  },
                  {
                    "name": "playButton",
                    "align": "tl",
                    "x": 15,
                    "y": 12
                  },
                  {
                    "name": "timeDisplay",
                    "align": "tl",
                    "x": 10,
                    "y": 7
                  },
                  {
                    "name": "fullScreenButton",
                    "align": "tr",
                    "x": 10,
                    "y": 12
                  },
                  {
                    "name": "volume",
                    "align": "tr",
                    "x": 5,
                    "y": 10
                  }
                ]
              }
            ]
              },function(player){
                console.log('播放器创建好了。')
              });
          }
          if(videosource.videoType == "2"){
            var player = new BjcPlayer($('#main').find('.video')[0], {
              token: videosource.params.token,
              vid: videosource.idVarchar,
              onplayfail: function () {
                console.log('onplayfail event');
              },
              onplayfinish: function () {
                console.log('onplayfinish event');
              },
              onpartfinish: function () {
                console.log('onpartfinish event');
              },
              onplaybegin: function () {
                console.log('onplaybegin event');
              },
              onplaypause: function () {
                console.log('onplaypause event');
              },
              ontimeupdate: function (currentTime) {
                console.log('ontimeupdate event');
                console.log('currentTime:' + currentTime);
              },
              onseeked: function (time) {
                console.log('onseek event');
                console.log('seekTime:' + time);
              }
            });
          }
          if(videosource.videoType == "3"){
            this.$refs.video.src = videosource.idVarchar
            this.$refs.video.play()
          }
        });
      },
    }
  };
</script>

<style>
  .bjc-player-wrapper {
    width: 100%;
    height: 100%;
  }
  .bjc-player-wrapper .video-js {
    width: 100%;
    height: 100%;
  }
</style>
