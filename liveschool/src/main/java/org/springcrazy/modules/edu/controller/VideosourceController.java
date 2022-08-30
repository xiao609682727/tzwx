
package org.springcrazy.modules.edu.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoInfoResponse;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springcrazy.common.tool.BajiayunUtil;
import org.springcrazy.common.tool.MsgVodFactory;
import org.springcrazy.core.boot.ctrl.CrazyController;
import org.springcrazy.core.boot.file.CrazyFile;
import org.springcrazy.core.launch.props.CrazyProperties;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.mp.support.Query;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.constant.SystemConstant;
import org.springcrazy.core.tool.utils.FileUtil;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.core.tool.vod.AliVod;
import org.springcrazy.core.tool.vod.AliVodUtil;
import org.springcrazy.modules.edu.dto.VideosourceDTO;
import org.springcrazy.modules.edu.entity.Videosource;
import org.springcrazy.modules.edu.service.IVideosourceService;
import org.springcrazy.modules.edu.vo.VideosourceVO;
import org.springcrazy.modules.edu.wrapper.VideosourceWrapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 录播视频表 控制器
 *
 * @author TongZhou
 * @since 2020-06-29
 */
@RestController
@AllArgsConstructor
@RequestMapping("/edu/videosource")
@Api(value = "录播视频表", tags = "录播视频表接口")
@Slf4j
public class VideosourceController extends CrazyController {

	private IVideosourceService videosourceService;
	private CrazyProperties crazyProperties;



	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入videosource")
	public R<VideosourceVO> detail(Videosource videosource) {
		Videosource detail = videosourceService.getOne(Condition.getQueryWrapper(videosource));
		return R.data(VideosourceWrapper.build().entityVO(detail));
	}




	/**
	 * 分页 录播视频表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入videosource")
	public R<IPage<VideosourceVO>> list(Videosource videosource, Query query) {
		QueryWrapper<Videosource> queryWrapper =  Condition.getQueryWrapper(videosource);
		queryWrapper.lambda().orderByDesc(Videosource::getCreateTime);
		//视频名称like搜索
		queryWrapper.lambda().like(Func.isNotBlank(videosource.getName()), Videosource::getName,videosource.getName());
		videosource.setName(null);
		//视频idlike搜索
		queryWrapper.lambda().like(Func.isNotBlank(videosource.getIdVarchar()), Videosource::getIdVarchar,videosource.getIdVarchar());
		videosource.setIdVarchar(null);

		IPage<Videosource> pages = videosourceService.page(Condition.getPage(query),queryWrapper );
		return R.data(VideosourceWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 录播视频表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入videosource")
	public R<IPage<VideosourceVO>> page(VideosourceVO videosource, Query query) {
		IPage<VideosourceVO> pages = videosourceService.selectVideosourcePage(Condition.getPage(query), videosource);
		return R.data(pages);
	}

	/**
	 * 新增 录播视频表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入videosource")
	public R save(@Valid @RequestBody Videosource videosource) {
		//根据videoid判断本地是否已存储相同视频
		Videosource temp = new Videosource();
		temp.setIdVarchar(videosource.getIdVarchar());
		List<Videosource> list = videosourceService.list(new QueryWrapper<>(temp));
		if(list.size()>0){
			return R.fail("视频已存在");
		}

		//当为阿里云时
		if(Func.equals(videosource.getVideoType(),"1")){
			DefaultAcsClient client = MsgVodFactory.getMsgSender(videosource.getVideoType());
			GetVideoInfoResponse videoInfo = AliVodUtil.getVideoInfo(client, videosource.getIdVarchar());
			if(Func.isEmpty(videoInfo)){
				return R.fail("视频获取失败，请确认视频播放码是否正确");
			}
			GetVideoInfoResponse.Video video = videoInfo.getVideo();
			videosource.setImageUrl(video.getCoverURL());
			videosource.setName(video.getTitle());

			if(Func.isNotEmpty(video.getSize())){
				videosource.setVideoSize(FileUtil.getSize(video.getSize()));
				videosource.setVideoLength(FileUtil.getSize(video.getSize()));
			}
			videosource.setVideoStatus(videosourceService.convert(video.getStatus()));

			float duration = video.getDuration();
			videosource.setVideoDuration((int) duration);
		}
		//当为百家云时
		if(Func.equals(videosource.getVideoType(),"2")){
			Map<String,Object> videoInfo = BajiayunUtil.getInfo(videosource.getIdVarchar());
			videosource.setImageUrl(videoInfo.get("preface_url")+"");
			videosource.setName(videoInfo.get("name")+"");

			videosource.setVideoSize(FileUtil.getSize(Func.toLong(videoInfo.get("total_transcode_size"))));
			videosource.setVideoLength(FileUtil.getSize(Func.toLong(videoInfo.get("total_size"))));
			videosource.setVideoStatus(videosourceService.convert(videoInfo.get("status")+""));
			videosource.setVideoDuration(Func.toInt(videoInfo.get("length")));
		}

//		videosource.setFileType();
//		videosource.setInitType();
		return R.status(videosourceService.save(videosource));
	}


	/**
	 * 新增 录播视频表
	 */
	@GetMapping("/createUploadVideo")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入videosource")
	public R CreateUploadVideo(VideosourceDTO videosource) {
		DefaultAcsClient client = MsgVodFactory.getMsgSender(videosource.getVideoType());
		AliVod aliVod = new AliVod();
		aliVod.setFileName(videosource.getFileName());
		aliVod.setTitle(videosource.getName());
		CreateUploadVideoResponse uploadVideo = AliVodUtil.createUploadVideo(client, aliVod);
		return R.data(uploadVideo);
	}


	/**
	 * 新增 录播视频表
	 */
	@GetMapping("/sync")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入videosource")
	public R sync() {
		videosourceService.syncVideoSource();
		return R.status(true);
	}

	/**
	 * 新增 录播视频表
	 */
	@GetMapping("/sync/{id}")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入videosource")
	public R syncById(@PathVariable("id")Integer id) {
		Videosource videosource = videosourceService.getById(id);
		//更新视频信息
		DefaultAcsClient client = MsgVodFactory.getMsgSender(videosource.getVideoType());
		GetVideoInfoResponse videoInfo = AliVodUtil.getVideoInfo(client, videosource.getIdVarchar());
		GetVideoInfoResponse.Video video = videoInfo.getVideo();
		videosource.setImageUrl(video.getCoverURL());
		videosource.setName(video.getTitle());
		videosource.setVideoSize(FileUtil.getSize(video.getSize()));
		videosource.setVideoStatus(videosourceService.convert(video.getStatus()));
		videosource.setVideoLength(FileUtil.getSize(video.getSize()));
		float duration = video.getDuration();
		videosource.setVideoDuration((int) duration);

		return R.status(videosourceService.updateById(videosource));
	}


	/**
	 * 新增 录播视频表
	 */
	@GetMapping("/refreshUploadVideo")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入videosource")
	public R refreshUploadVideo(VideosourceDTO videosource) {
		DefaultAcsClient client = MsgVodFactory.getMsgSender(videosource.getVideoType());
		RefreshUploadVideoResponse refreshUploadVideoResponse = AliVodUtil.refreshUploadVideo(client, videosource.getIdVarchar());
		return R.data(refreshUploadVideoResponse);
	}



	/**
	 * 修改 录播视频表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入videosource")
	public R update(@Valid @RequestBody Videosource videosource) {
		return R.status(videosourceService.updateById(videosource));
	}

	/**
	 * 新增或修改 录播视频表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入videosource")
	public R submit(@Valid @RequestBody Videosource videosource) {

		return R.status(videosourceService.saveOrUpdate(videosource));
	}


	/**
	 * 删除 录播视频表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		//删除视频
		List<Integer> list = Func.toIntList(ids);
		for (Integer id : list) {
			Videosource videosource = videosourceService.getById(id);
			if(Func.equals(videosource.getVideoType(),"1")) {
				DefaultAcsClient client = MsgVodFactory.getMsgSender(videosource.getVideoType());
				AliVodUtil.deleteVideo(client,videosource.getIdVarchar());
			}
			if(Func.equals(videosource.getVideoType(),"2")) {
				BajiayunUtil.delete(videosource.getIdVarchar());
			}
			videosourceService.removeById(videosource.getId());
		}
		return R.status(true);
	}

	/**
	 * 本地视频上传请求
	 */
	@PostMapping("/fileUpload")
	@ApiOperation(value = "本地视频上传", notes = "本地视频上传")
	public R uploadIcon(MultipartFile file){

		SystemConstant me = SystemConstant.me();

		String fileName = file.getOriginalFilename();
		CrazyFile crazyFile = getFile(file,"localVideo");
		crazyFile.transfer();
		//上传文件
		Map<String,String> result = Maps.newHashMap();
		result.put("url", me.getDomain() + crazyFile.getUploadVirtualPath());
		result.put("fileName" , fileName);
		return R.data(result);
	}
}
