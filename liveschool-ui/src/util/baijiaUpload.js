import {getResumeUploadUrl, getUploadUrl,uploadFile} from "@/api/web/baijia";
import axios from 'axios'


export const baijiaUploader = ({ file, pieceSize = 2, progress, success, error }) => {
  // if (!file || !file.length) return
  // 上传过程中用到的变量
  let start = 0
  const chunkSize = pieceSize * 1024 * 1024 // 5MB一片
  const chunkCount = Math.ceil(file.size / chunkSize) // 总片数

  const checkUpload = () => {
    //获取 上传地址
    let param = {
      "filename":file.name,
    }
    getUploadUrl(param).then(res=>{
      let data = res.data.data;
      console.log(data)
      //检测是否上传完成
      param = {
        "videoId":data.video_id,
      }
      data.currentChunk = 0
      // uploadChunk(data)
      uploadChunk1(data)
     /* getResumeUploadUrl(param).then(res=>{
        let data = res.data.data;
        console.log(data)
        start = data.upload_size
        readChunkMD5()
      })*/
    })

    //
  }

  const uploadChunk1 = (data) => {

    let that = uploadChunk
    // if(data.currentChunk < chunkCount){
      // 针对单个文件进行chunk上传
      // const chunk = getChunkInfo(file, data.currentChunk, chunkSize)
      let param = new FormData();
      param.append('data',file);
      let config = {
        headers:{'Content-Type':'multipart/form-data'} //这里是重点，需要和后台沟通好请求头，Content-Type不一定是这个值
      };

      axios.post(data.upload_url,param,config).then(res=>{
        console.log(res)
        if(res.data.code == 1){
          // progress(100)
          success(data.video_id)
          console.log("上传成功")
        }else{
          error(data.video_id)
          console.log("上传失败")
        }
      })
    // }

  }


  checkUpload()
  const getChunkInfo = (file, currentChunk, chunkSize) => {
    let start = currentChunk * chunkSize
    let end = Math.min(file.size, start + chunkSize)
    let chunk = file.slice(start, end)
    return {start:start,end:end,file:chunk}
  }



  const uploadChunk = (data) => {
    var xhr = new XMLHttpRequest();

    if(data.currentChunk < chunkCount){
      // 针对单个文件进行chunk上传

      let start = data.currentChunk * chunkSize
      let end = Math.min(file.size, start + chunkSize)

      xhr.open('post', data.upload_url, true);
      var header = {
        'Content-Type': '',
        'X_FILENAME': encodeURIComponent(file.name),
        'Content-Range': 'bytes ' + start + '-' + end + '/' + chunkSize
      };
      $.each(header,function(name, value) {
          xhr.setRequestHeader(name, value);
        });
      xhr.send(file.slice(start, end));
      data.currentChunk = data.currentChunk +1
      // uploadChunk(data)


    }

  }
}
