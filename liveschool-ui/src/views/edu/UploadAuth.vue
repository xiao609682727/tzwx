<template>
  <div class="container">
    <div class="setting">
      <!--<div class="input-control">
        <label for="region">配置项 region, 默认 cn-shanghai:</label>
        <select v-model="region">
          <option>cn-shanghai</option>
          <option>eu-central-1</option>
          <option>ap-southeast-1</option>
        </select>
      </div>

      <div class="input-control">
        <label for="userId">阿里云账号ID</label>
        <input type="text" class="form-control" v-model="userId" disabled placeholder="输入阿里云账号ID">
        集成产品后需要使用用户自己的账号ID,<a href="https://help.aliyun.com/knowledge_detail/37196.html "target="_blank">如何获取帐号ID</a>
      </div>-->

    </div>

    <div class="upload">
      <el-form ref="form" label-width="80px">
        <el-form-item label="视频云">
          <el-select v-model="type" placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="上传文件"  v-show="type == '3'">
          <el-upload
            class="upload-demo"
            ref="upload"
            action="/api/edu/videosource/fileUpload"
            :limit="1"
            :file-list="fileList"
            :before-upload="beforeUploadVideo">
            <el-button slot="trigger" size="small" type="primary">上传<i class="el-icon-upload el-icon--right"></i></el-button>
          </el-upload>
          <!--          <el-button type="primary" @click="submitUpload()">确定</el-button>-->
        </el-form-item>
        <el-form-item label="上传文件"  v-show="type == '2'">
          <div class="bjy-demo">
            <el-button type="primary" style="width: 110px" :disabled="uploadDisabled">上传<i class="el-icon-upload el-icon--right"></i>
              <input type="file" :disabled="uploadDisabled">
            </el-button>
            <!-- <el-button :disabled="pauseDisabled" type="primary" @click="stopFile">停止上传<i class="el-icon-video-pause el-icon&#45;&#45;right"></i></el-button>
             <el-button :disabled="resumeDisabled" type="primary" @click="continueFile">继续上传<i class="el-icon-video-play el-icon&#45;&#45;right"></i></el-button>-->
          </div>
        </el-form-item>
        <el-form-item label="上传文件"  v-show="type == '1'">
          <el-button @click="choiceFile" type="primary" :disabled="uploadDisabled">上传<i class="el-icon-upload el-icon--right"></i></el-button>
          <input ref="filElem" type="file" v-show="false" id="fileUpload" @change="fileChange($event)" />
          <el-button type="primary" @click="pauseUpload" style="margin-left: 10px;" :disabled="pauseDisabled">停止上传<i class="el-icon-video-pause el-icon--right"></i></el-button>
          <el-button type="primary"  @click="resumeUpload" :disabled="resumeDisabled">继续上传<i class="el-icon-video-play el-icon--right"></i></el-button>
        </el-form-item>
        <el-form-item label="上传进度" v-show="type!=2">
          <el-progress :percentage="authProgress" style="line-height: 35px;" :status="status" :stroke-width="15"></el-progress>
        </el-form-item>
        <el-form-item label="上传进度" v-if="baijiayunInfos.length > 0">
          <div style="padding-left: 20px;overflow: auto; height: 230px;width: 85%;border-radius:5px;border: #d9d9d9 1px dashed">
            <div class="bjy-demo" v-for="(item,index) in  baijiayunInfos">{{item.fileName}}
              <el-progress :percentage="item.authProgress" style="line-height: 30px;" :status="item.status":stroke-width="4"></el-progress>
            </div>
          </div>
        </el-form-item>
      </el-form>

      <!--   <div>


          <label class="status">上传状态: <span>{{statusText}}</span></label>
        </div>
        <div >
         <button @click="authUpload" :disabled="uploadDisabled">开始上传</button>
          <button @click="pauseUpload" :disabled="pauseDisabled">暂停</button>
          <button :disabled="resumeDisabled" @click="resumeUpload">恢复上传</button>

      </div>-->
    </div>
  </div>
</template>
<script>
  import axios from 'axios'
  import { mapGetters } from "vuex";
  import {getList, getDetail, add, update, remove, getWebSiteList} from "@/api/edu/videosource";
  import {getResumeUploadUrl, getUploadUrl} from "@/api/web/baijia";
  import {baijiaUploader } from "@/util/baijiaUpload";
  import {setStore, getStore} from '@/util/store'

  export default {
    data () {
      return {
        baijiayunInfos:[],//视频云多文件信息
        baijiauploader:null,
        type:"2",
        timeout: '',
        partSize: '',
        parallel: '',
        retryCount: '',
        retryDuration: '',
        region: 'cn-shanghai',
        userId: '',
        file: null,
        authProgress: 0,
        uploadDisabled: false,
        resumeDisabled: true,
        pauseDisabled: true,
        uploader: null,
        statusText: '',
        status:"",
        videoId:"",
        options: [{
          value: '1',
          label: '阿里云'
        }, {
          value: '2',
          label: '视频云'
        }, {
          value: '3',
          label: '本地上传'
        }],
      }
    },
    computed: {
      ...mapGetters(["website"]),
    },
    mounted:function() {
      const oScript = document.createElement('script');
      oScript.type = 'text/javascript';
      oScript.src = '/admin/aliyun-upload-sdk/lib/es6-promise.min.js';
      document.body.appendChild(oScript);
      const oScript2 = document.createElement('script');
      oScript2.type = 'text/javascript';
      oScript2.src = '/admin/aliyun-upload-sdk/lib/aliyun-oss-sdk-5.3.1.min.js';
      document.body.appendChild(oScript2);
      const oScript3 = document.createElement('script');
      oScript3.type = 'text/javascript';
      oScript3.src = '/admin/aliyun-upload-sdk/aliyun-upload-sdk-1.5.0.min.js';
      document.body.appendChild(oScript3);
      this.init()
    },
    methods: {

      init(){
        this.options = getStore({name: 'yunconfig'})
        this.baijiayun()
      },
      //上传视频格式限制
      beforeUploadVideo(file) {
        if (file.type != 'video/mp4'){
          this.$message.warning('请上传mp4文件！')
          return false
        }
        if (file.size >= 1073741824){
          this.$message.warning('本地上传最大支持1GB MP4 文件')
          return false
        }
        //获取视频时长
        let videoUrl = URL.createObjectURL(file);
        let audioElement = new Audio(videoUrl);
        audioElement.addEventListener("loadedmetadata", () => {
          let duration = audioElement.duration; //时长为秒，小数，182.36
          this.addVideo(file ,duration);
        });
        return false;
      },
      //上传视频接口
      addVideo(file ,duration) {
        let size = ""
        if(file.size < 0.1 * 1024){                            //小于0.1KB，则转化成B
          size = file.size.toFixed(2) + "B"
        }else if(file.size < 0.1 * 1024 * 1024){            //小于0.1MB，则转化成KB
          size = (file.size/1024).toFixed(2) + "KB"
        }else {
          size = (file.size/(1024 * 1024)).toFixed(2) + "MB"
        }
        //设置formData
        let formData = new FormData();
        formData.append("file", file);
        //在请求头设置上传进度条
        let config = {
          onUploadProgress: progressEvent => {
            var complete =
              ((progressEvent.loaded / progressEvent.total) * 100) | 0;
            this.authProgress = complete;
          },
          headers: {
            "Content-Type": "multipart/form-data"
          }
        };
        this.videoFlag = true;
        //调用后端上传视频的接口
        this.$http.post(`/api/edu/videosource/fileUpload`,formData, config).then(res => {
          this.videoFlag = false;

          if (res.data.code == 200) {
            let videoInfo = res.data.data

            let row = {
              "name":file.name ,
              "videoSize":size ,
              "videoStatus": "finish",
              "idVarchar": videoInfo.url,
              "videoLength": "",
              "videoDuration": duration,
              "videoType": 3 ,
              "fileType": 1 ,
              "initType": 1 ,
              "status": 1 ,
            }
            add(row).then(() => {
              that.$emit('closeupload')
            }, error => {
              window.console.log(error);
              that.$emit('failupload',"文件信息保存失败")
            });
          } else {
            this.$message.error(res.message || "网络出了小差");
          }
        });
        return false;
      },

      choiceFile(){
        this.$refs.filElem.dispatchEvent(new MouseEvent('click'))
      },
      fileChange (e) {
        if(this.type == "1"){
          this.aliyunUpload(e)
        }
      },
      stopFile(){
        this.baijiauploader.stopFile(0);
        this.status="warning"
        this.resumeDisabled = false
        this.pauseDisabled = true
      },
      continueFile(){
        // 继续上传
        var data = this.baijiauploader.currentFiles[0];
        this.status=""
        this.resumeDisabled = true
        this.pauseDisabled = false
        this.baijiauploader.resumeUpload(data);

      },
      baijiayun(){
        let i = 0 //存值是 baijiayunInfos 的下标
        let y = 0 //取值时 baijiayunInfos 的下标
        let that = this
        var uploader = new BJY.VideoUploader({
          mainElement: $('.bjy-demo input[type="file"]'),
          ignoreError: true,
          name: '',
          multiple: true,
          getUploadUrl: function (data) {
            return $.ajax({
              url: '/api/front/web/baijia/getUploadUrl',
              type: 'get',
              data: {
                filename: data.videoName,
                definition: 1
              }
            })
              .then(function (response) {
                var result = response.data;
                that.videoId = result.video_id

                that.baijiayunInfos[i] = {
                  "fileName":data.videoName,
                  "id":result.video_id,
                  "url":result.upload_url,
                  "authProgress":'',
                  "status":''
                }
                i++
                console.log(that.baijiayunInfos)

                return {
                  id: result.video_id,
                  url: result.upload_url
                };
              });
          },
          getResumeUploadUrl: function (data) {
            return $.ajax({
              url: '/api/front/web/baijia/getResumeUploadUrl',
              type: 'get',
              data: {
                videoId: data.uploadId
              }
            }).then(function (response) {
              var result = response.data;
              if (!result) {
                this.$message({
                  type: "error",
                  message: response.msg
                });
                return;
              }
              that.videoId = result.video_id
              return {
                fid: result.video_id,
                id: result.video_id,
                url: result.upload_url,
                uploaded: result.upload_size
              };
            });
          },
          onFileChange: function (files) {
            var currentFiles = uploader.currentFiles;
            if (!currentFiles.length) {
              return;
            }

            if (!uploader.validateFiles(currentFiles)) {
              uploader.reset();
              return;
            }
            that.uploadDisabled = true
            that.pauseDisabled = false
            $.each(
              currentFiles,
              function (index, file) {
                uploader.autoUpload(file);
                this.pauseDisabled = false
              }
            );

          },
          onUploadStart: function (data) {
            console.log('onUploadStart', data.fileItem);
          },
          onUploadProgress: function (data) {
            // console.log('onUploadProgress',data)
            for (let num = 0;that.baijiayunInfos.length > num ;num++){
              if (data.fileItem.file.name == that.baijiayunInfos[num].fileName){
                that.baijiayunInfos[num].authProgress = parseInt(data.percent.replace("%",""))
                that.$set(that.baijiayunInfos[num],"authProgress", parseInt(data.percent.replace("%","")))
                that.$forceUpdate()
              }
            }
            console.log(that.baijiayunInfos)
            // that.authProgress = parseInt(data.percent.replace("%",""))
          },
          onUploadSuccess: function (data) {
            console.log('onUploadSuccess', data.fileItem);
            for (let num = 0;that.baijiayunInfos.length > num ;num++){
              if (data.fileItem.file.name == that.baijiayunInfos[num].fileName){
                that.baijiayunInfos[num].status="success"
                console.log(that.baijiayunInfos)
              }
            }
            that.status="success"
            //文件上传成功后将数据保存到本地
            let row = {
              "idVarchar":that.baijiayunInfos[y].id,
              "videoType":"2"
            }

            y++
            setTimeout(function () {
              add(row).then(() => {
                //取值下标   存值下标
                if (y == i){
                  that.$emit('closeupload')
                }
              }, error => {
                window.console.log(error);
                that.$emit('failupload',"文件信息保存失败")
              });
              that.statusText = '文件上传成功!'
            },1000)
          },
          onChunkUploadError: function (data) {
            // alert('上传错误，请重新上传');
            that.status="exception"
            that.$emit('failupload','上传错误，请重新上传')
          },
          onUploadError: function (data) {
            // alert('上传错误，请重新上传');
            that.status="exception"
            that.$emit('failupload','上传错误，请重新上传')
          },
          onUploadComplete: function (data) {
            console.log("onUploadComplete",data)

          },
          onError: function (data) {
            // alert(data.content);
            that.status="exception"
            that.$emit('failupload',data.content)
          }
        });

        this.baijiauploader = uploader;
      },

      aliyunUpload(e){
        this.userId = this.website.aliyunAccount
        // this.file = e.target.files[0]

        var title = e.target.files[0].name
        title = title.replace("(","")
        title = title.replace(")","")
        this.file =new File([e.target.files[0]],title,{type:e.target.files[0].type});
        if (!this.file) {
          alert("请先选择需要上传的文件!")
          return
        }
        var userData = '{"Vod":{}}'
        if (this.uploader) {
          this.uploader.stopUpload()
          this.authProgress = 0
          this.statusText = ""
        }
        let that = this
        that.uploader = this.createUploader()
        that.uploader.addFile(that.file, null, null, null, userData)
        that.uploadDisabled = false
        that.pauseDisabled = true
        that.resumeDisabled = true
        //添加文件成功开始上传
        that.authUpload ()
      },
      authUpload () {
        // 然后调用 startUpload 方法, 开始上传
        if (this.uploader !== null) {
          this.uploader.startUpload()
          this.uploadDisabled = true
          this.pauseDisabled = false
        }
      },
      // 暂停上传
      pauseUpload () {
        if (this.uploader !== null) {
          this.status="warning"
          this.uploader.stopUpload()
          this.resumeDisabled = false
          this.pauseDisabled = true
        }
      },
      // 恢复上传
      resumeUpload () {
        if (this.uploader !== null) {
          this.status=""
          this.uploader.startUpload()
          this.resumeDisabled = true
          this.pauseDisabled = false
        }
      },
      createUploader (type) {
        let self = this
        let uploader = new AliyunUpload.Vod({
          timeout: self.timeout || 60000,
          partSize: self.partSize || 1048576,
          parallel: self.parallel || 5,
          retryCount: self.retryCount || 3,
          retryDuration: self.retryDuration || 2,
          region: self.region,
          userId: self.userId,
          // 添加文件成功
          addFileSuccess: function (uploadInfo) {
            self.uploadDisabled = false
            self.resumeDisabled = false
            self.statusText = '添加文件成功, 等待上传...'
            console.log("addFileSuccess: " + uploadInfo.file.name)
          },
          // 开始上传
          onUploadstarted: function (uploadInfo) {
            // 如果是 UploadAuth 上传方式, 需要调用 uploader.setUploadAuthAndAddress 方法
            // 如果是 UploadAuth 上传方式, 需要根据 uploadInfo.videoId是否有值，调用点播的不同接口获取uploadauth和uploadAddress
            // 如果 uploadInfo.videoId 有值，调用刷新视频上传凭证接口，否则调用创建视频上传凭证接口
            // 注意: 这里是测试 demo 所以直接调用了获取 UploadAuth 的测试接口, 用户在使用时需要判断 uploadInfo.videoId 存在与否从而调用 openApi
            // 如果 uploadInfo.videoId 存在, 调用 刷新视频上传凭证接口(https://help.aliyun.com/document_detail/55408.html)
            // 如果 uploadInfo.videoId 不存在,调用 获取视频上传地址和凭证接口(https://help.aliyun.com/document_detail/55407.html)
            if (!uploadInfo.videoId) {
              let createUrl = '/api/edu/videosource/createUploadVideo?videoType=1&name='+uploadInfo.file.name+'&fileName='+uploadInfo.file.name
              axios.get(createUrl).then(({data}) => {
                let uploadAuth = data.data.uploadAuth
                let uploadAddress = data.data.uploadAddress
                let videoId = data.data.videoId
                uploader.setUploadAuthAndAddress(uploadInfo, uploadAuth, uploadAddress,videoId)
              })
              self.statusText = '文件开始上传...'
              console.log("onUploadStarted:" + uploadInfo.file.name + ", endpoint:" + uploadInfo.endpoint + ", bucket:" + uploadInfo.bucket + ", object:" + uploadInfo.object)
            } else {
              // 如果videoId有值，根据videoId刷新上传凭证
              // https://help.aliyun.com/document_detail/55408.html?spm=a2c4g.11186623.6.630.BoYYcY
              let refreshUrl = '/api/edu/videosource/refreshUploadVideo?videoType=1&idVarchar=' + uploadInfo.videoId
              axios.get(refreshUrl).then(({data}) => {
                let uploadAuth = data.data.uploadAuth
                let uploadAddress = data.data.uploadAddress
                let videoId = data.data.videoId
                uploader.setUploadAuthAndAddress(uploadInfo, uploadAuth, uploadAddress,videoId)
              })
            }
          },
          // 文件上传成功
          onUploadSucceed: function (uploadInfo) {
            console.log("onUploadSucceed: " + uploadInfo.file.name + ", endpoint:" + uploadInfo.endpoint + ", bucket:" + uploadInfo.bucket + ", object:" + uploadInfo.object)
            self.status="success"
            //文件上传成功后将数据保存到本地
            let row = {
              "idVarchar":uploadInfo.videoId,
              "videoType":"1"
            }
            setTimeout(function () {
              add(row).then(() => {
                self.$emit('closeupload')
              }, error => {
                window.console.log(error);
                self.$emit('failupload',"文件信息保存失败")
              });
              self.statusText = '文件上传成功!'
            },1000)
          },
          // 文件上传失败
          onUploadFailed: function (uploadInfo, code, message) {
            console.log("onUploadFailed: file:" + uploadInfo.file.name + ",code:" + code + ", message:" + message)
            self.$emit('failupload',message)
            self.status="exception"
            self.statusText = '文件上传失败!'
          },
          // 取消文件上传
          onUploadCanceled: function (uploadInfo, code, message) {
            console.log("Canceled file: " + uploadInfo.file.name + ", code: " + code + ", message:" + message)
            self.statusText = '文件已暂停上传'
          },
          // 文件上传进度，单位：字节, 可以在这个函数中拿到上传进度并显示在页面上
          onUploadProgress: function (uploadInfo, totalSize, progress) {
            console.log("onUploadProgress:file:" + uploadInfo.file.name + ", fileSize:" + totalSize + ", percent:" + Math.ceil(progress * 100) + "%")
            let progressPercent = Math.ceil(progress * 100)
            self.authProgress = progressPercent
            self.statusText = '文件上传中...'
          },
          // 上传凭证超时
          onUploadTokenExpired: function (uploadInfo) {
            // 上传大文件超时, 如果是上传方式一即根据 UploadAuth 上传时
            // 需要根据 uploadInfo.videoId 调用刷新视频上传凭证接口(https://help.aliyun.com/document_detail/55408.html)重新获取 UploadAuth
            // 然后调用 resumeUploadWithAuth 方法, 这里是测试接口, 所以我直接获取了 UploadAuth
            let refreshUrl = '/api/edu/videosource/refreshUploadVideo?videoType=1&idVarchar=' + uploadInfo.videoId
            axios.get(refreshUrl).then(({data}) => {
              let uploadAuth = data.data.uploadAuth
              uploader.resumeUploadWithAuth(uploadAuth)
              console.log('upload expired and resume upload with uploadauth ' + uploadAuth)
            })
            self.status="exception"
            self.statusText = '文件超时...'
            self.$emit('failupload','文件超时...')
          },
          // 全部文件上传结束
          onUploadEnd: function (uploadInfo) {
            console.log("onUploadEnd: uploaded all the files")
            self.statusText = '文件上传完毕'
          }
        })
        return uploader
      }
    }
  }
</script>
<style type="text/css">
  .bjy-desc {
    width: 600px;
    position: absolute;
    right: 0;
    border: 1px solid #ccc;
    padding: 10px;
  }
  .bjy-demo {
    width: 500px;
    float: left;
  }
  .bjy-demo .button {
    background-color: #37A4F5;
    display: inline-block;
    color: #fff;
    padding: 10px 15px;
    vertical-align: top;
  }
  .bjy-demo .upload-wrapper {
    position: relative;
    overflow: hidden;
  }
  .bjy-demo object,
  .bjy-demo  input[type="file"] {
    opacity: 0;
    filter: alpha(opacity=0);
    cursor: pointer;
    position: absolute;
    left: 0;
    top: 0;
    width: 110px;
    height: 100%;
    z-index: 2;
  }
  .item-list {
    font-size: 14px;
    color: #2e5580;
    padding: 10px;
    display: block;
    float: left;
    width: 100%;
  }
  .item-name {
    display: inline-block;
    margin-right: 20px;
  }
</style>
