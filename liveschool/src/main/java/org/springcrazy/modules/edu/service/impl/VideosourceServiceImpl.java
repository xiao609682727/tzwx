
package org.springcrazy.modules.edu.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetVideoInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoListResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.common.tool.BajiayunUtil;
import org.springcrazy.common.tool.MsgVodFactory;
import org.springcrazy.core.mp.base.BaseServiceImpl;
import org.springcrazy.core.tool.utils.FileUtil;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.core.tool.vod.AliVodUtil;
import org.springcrazy.modules.edu.entity.Videosource;
import org.springcrazy.modules.edu.mapper.VideosourceMapper;
import org.springcrazy.modules.edu.service.IVideosourceService;
import org.springcrazy.modules.edu.vo.VideosourceVO;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 录播视频表 服务实现类
 *
 * @author TongZhou
 * @since 2020-06-29
 */
@Service
public class VideosourceServiceImpl extends BaseServiceImpl<VideosourceMapper, Videosource> implements IVideosourceService {

	@Resource(name = "taskExecutor")
	private ThreadPoolTaskExecutor taskExecutor;

	@Override
	public IPage<VideosourceVO> selectVideosourcePage(IPage<VideosourceVO> page, VideosourceVO videosource) {
		return page.setRecords(baseMapper.selectVideosourcePage(page, videosource));
	}

	@Override
	public void syncVideoSource() {
		//阿里云视频更新
		DefaultAcsClient client = MsgVodFactory.getMsgSender("1");
		taskExecutor.execute(()->{
			updateVideSourse(client,1);
		});
	}

	public void updateVideSourse(DefaultAcsClient client,Integer pageNo){
		//只查询正常的视频
		GetVideoListResponse getVideoListResponse = AliVodUtil.getVideoList(client, pageNo, "Normal", null, null);
		List<GetVideoListResponse.Video> videoList = getVideoListResponse.getVideoList();
		for (GetVideoListResponse.Video video : videoList) {
			Videosource temp = new Videosource();
			temp.setIdVarchar(video.getVideoId());
			Videosource one = this.getOne(new QueryWrapper<>(temp));
			//如果存在则不添加
			if(Func.isNotEmpty(one)){
				continue;
			}
			temp.setImageUrl(video.getCoverURL());
			temp.setName(video.getTitle());
			temp.setVideoType("1");
			temp.setVideoSize(FileUtil.getSize(video.getSize()));
			temp.setVideoStatus(convert(video.getStatus()));
			temp.setVideoLength(FileUtil.getSize(video.getSize()));
			float duration = video.getDuration();
			temp.setVideoDuration((int) duration);
			save(temp);
		}
		//计算是否还需要继续查询
		Integer total = getVideoListResponse.getTotal();
		Integer page = total/10+1;
		if((pageNo+1)<=page){
			updateVideSourse(client,pageNo+1);
		}
	}


	@Override
	public void updateVideoSourseStatus() {
		QueryWrapper<Videosource> queryWrapper = new QueryWrapper<Videosource>();
		queryWrapper.lambda().in(Videosource::getVideoStatus,"progress","init");
		List<Videosource> videosourceList = baseMapper.selectList(queryWrapper);
		videosourceList.forEach(vs -> {
			Videosource videosource = baseMapper.selectById(vs.getId());
			//更新阿里云视频信息
			if(Func.equals(videosource.getVideoType(),"1")){
				DefaultAcsClient client = MsgVodFactory.getMsgSender(videosource.getVideoType());
				GetVideoInfoResponse videoInfo = AliVodUtil.getVideoInfo(client, videosource.getIdVarchar());
				GetVideoInfoResponse.Video video = videoInfo.getVideo();
				videosource.setImageUrl(video.getCoverURL());
				videosource.setName(video.getTitle());
				videosource.setVideoSize(FileUtil.getSize(video.getSize()));
				videosource.setVideoStatus(convert(video.getStatus()));
				videosource.setVideoLength(FileUtil.getSize(video.getSize()));
				float duration = video.getDuration();
				videosource.setVideoDuration((int) duration);
			}
			//更新百家云视频信息
			if(Func.equals(videosource.getVideoType(),"2")){
				Map<String,Object> videoInfo = BajiayunUtil.getInfo(videosource.getIdVarchar());
				videosource.setImageUrl(videoInfo.get("preface_url")+"");
				videosource.setName(videoInfo.get("name")+"");

				videosource.setVideoSize(FileUtil.getSize(Func.toLong(videoInfo.get("total_transcode_size"))));
				videosource.setVideoLength(FileUtil.getSize(Func.toLong(videoInfo.get("total_size"))));
				videosource.setVideoStatus(convert(videoInfo.get("status")+""));
				videosource.setVideoDuration(Func.toInt(videoInfo.get("length")));
			}
			baseMapper.updateById(videosource);
		});
	}

	@Override
	public String convert(String status){
		String s = "";
		switch (status){
			case "Uploading":
				s = "init";
				break;
			case "UploadFail":
				s = "init";
				break;
			case "UploadSucc":
				s = "init";
				break;
			case "Transcoding":
				s = "progress";
				break;
			case "TranscodeFail":
				s = "error";
				break;
			case "Checking":
				s = "progress";
				break;
			case "Blocked":
				s = "error";
				break;
			case "Normal":
				s = "finish";
				break;
			//百家云状态
			case "10":
				s = "init";
				break;
			case "20":
				s = "progress";
				break;
			case "30":
				s = "error";
				break;
			case "31":
				s = "error";
				break;
			case "32":
				s = "error";
				break;
			case "100":
				s = "finish";
				break;
		}
		return s;
	}
}
