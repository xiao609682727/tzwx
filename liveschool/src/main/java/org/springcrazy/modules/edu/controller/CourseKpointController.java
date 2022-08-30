
package org.springcrazy.modules.edu.controller;

import cn.hutool.core.util.ObjectUtil;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springcrazy.common.tool.AliyunLiveUtils;
import org.springcrazy.common.tool.BajiayunUtil;
import org.springcrazy.common.tool.MsgVodFactory;
import org.springcrazy.common.tool.ProfileConfig;
import org.springcrazy.core.boot.ctrl.CrazyController;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.mp.support.Query;
import org.springcrazy.core.secure.utils.SecureUtil;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.core.tool.vod.AliVodUtil;
import org.springcrazy.modules.cms.entity.WebsiteProfile;
import org.springcrazy.modules.edu.entity.CourseKpoint;
import org.springcrazy.modules.edu.entity.CourseStudyhistory;
import org.springcrazy.modules.edu.service.ICourseKpointService;
import org.springcrazy.modules.edu.service.ICourseStudyhistoryService;
import org.springcrazy.modules.edu.vo.CourseKpointPlayVO;
import org.springcrazy.modules.edu.vo.CourseKpointVO;
import org.springcrazy.modules.edu.wrapper.CourseKpointWrapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

/**
 * 知识点的基本信息 控制器
 *
 * @author TongZhou
 * @since 2020-04-25
 */
@RestController
@AllArgsConstructor
@RequestMapping("/edu/coursekpoint")
@Api(value = "知识点的基本信息", tags = "知识点的基本信息接口")
public class CourseKpointController extends CrazyController {

	private ICourseKpointService courseKpointService;
	private ICourseStudyhistoryService courseStudyhistoryService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入courseKpoint")
	public R<CourseKpointPlayVO> detail(CourseKpoint courseKpoint,@RequestParam(defaultValue = "teacher")String userType,String courseIds) {
		CourseKpoint detail = courseKpointService.getOne(Condition.getQueryWrapper(courseKpoint));
		CourseKpointPlayVO courseKpointPlayVO = BeanUtil.copy(detail, CourseKpointPlayVO.class);
		String playProgress = "2";//进度条
		String rateComponent = "2";//倍速
		//阿里云点播
		if(Func.equals(detail.getVideoType(),"aliyunvod")||Func.equals(detail.getVideoType(),"aliyunReplay")){
			DefaultAcsClient client = MsgVodFactory.getMsgSender(WebsiteProfile.ALIYUN);
			Map<String, String> config = ProfileConfig.getConfig(WebsiteProfile.ALIYUN_VOD);
			playProgress=config.get("play_progress");
			rateComponent=config.get("rate_component");
			GetVideoPlayAuthResponse response = AliVodUtil.getVideoPlayAuth(client, detail.getVideoUrl());
			GetPlayInfoResponse playInfoResponse = AliVodUtil.getPlayInfo(client, detail.getVideoUrl());
			if (Func.notNull(response)) {
				Map<String,Object> params = Maps.newHashMap();
				params.put("playAuth",response.getPlayAuth());
				if(Func.isNotEmpty(playInfoResponse)){
					params.put("playURL",playInfoResponse.getPlayInfoList());
				}
				courseKpointPlayVO.setParams(params);
			}
		}
		//百家云点播
		if(Func.equals(detail.getVideoType(),"baijiayunvod")){
			Map<String, String> config = ProfileConfig.getConfig(WebsiteProfile.BAIJIA_VOD);
			playProgress=config.get("play_progress");
			rateComponent=config.get("rate_component");
			String token = BajiayunUtil.getUrl(detail.getVideoUrl());
			String token1 = BajiayunUtil.getToken(detail.getVideoUrl());
			Map<String,Object> params = Maps.newHashMap();
			params.put("playURL",token);
			params.put("token",token1);
			courseKpointPlayVO.setParams(params);
		}
		//百家云直播
		if(Func.equals(detail.getVideoType(),"baijiayunlive")){
			//当直播时间未结束
			if(detail.getLiveEndTime().compareTo(LocalDateTime.now()) >= 0){
				Map<String,String> userMap = Maps.newHashMap();
				userMap.put("userId", SecureUtil.getUser().getUserId()+"");
				userMap.put("userName",SecureUtil.getUser().getUserName());
				String token = BajiayunUtil.roomUrl(detail.getVideoUrl(),"WEB",userType,userMap);
				String clienturl = BajiayunUtil.roomUrl(detail.getVideoUrl(),"APP",userType,userMap);
				Map<String,Object> params = Maps.newHashMap();
				params.put("url",token);
				params.put("clienturl",clienturl);
				courseKpointPlayVO.setParams(params);
			}else{//当直播时间已结束
				if(Func.equals(detail.getOpenReplay(),2)){
					Map<String,String> userMap = Maps.newHashMap();
					userMap.put("userId", SecureUtil.getUser().getUserId()+"");
					userMap.put("userName",SecureUtil.getUser().getUserName());
					String token = BajiayunUtil.roomUrl(detail.getVideoUrl(),"WEB",userType,userMap);
					String clienturl = BajiayunUtil.roomUrl(detail.getVideoUrl(),"APP",userType,userMap);
					Map<String,Object> params = Maps.newHashMap();
					params.put("url",token);
					params.put("clienturl",clienturl);
					courseKpointPlayVO.setParams(params);
				}
				//百家云回放
				if(Func.equals(detail.getOpenReplay(),1)){
					//房间号
					Map<String,Object> params = Maps.newHashMap();
					params.put("url",BajiayunUtil.getBackToken(detail.getVideoUrl()));
					courseKpointPlayVO.setParams(params);
				}
			}
		}

		if(("1").equals(playProgress)){
			int userId=SecureUtil.getUserId();
			CourseStudyhistory courseStudyhistory = new CourseStudyhistory();
			if(ObjectUtil.isNotNull(courseIds) && ("").equals(courseIds)){
				courseStudyhistory=courseStudyhistoryService.getOnesStudyHistory(userId,courseKpoint.getId(),2,Integer.valueOf(courseIds));
			}else {
				courseStudyhistory=courseStudyhistoryService.getOnesStudyHistory(userId,courseKpoint.getId(),1,0);
			}
			if(ObjectUtil.isNotNull(courseStudyhistory)){
				if(("2").equals(courseStudyhistory.getComplete())){
					playProgress="2";
				}
			}
		}
		courseKpointPlayVO.setPlayProgress(playProgress);
		if(Func.equals(courseKpointPlayVO.getVideoType(),"aliyunvod")||Func.equals(courseKpointPlayVO.getVideoType(),"baijiayunvod")||Func.equals(courseKpointPlayVO.getVideoType(),"polyvvod")||Func.equals(courseKpointPlayVO.getVideoType(),"othervod")){
			if(courseKpointPlayVO.getVideoTime()!=null&&!"".equals(courseKpointPlayVO.getVideoTime())){
				int value= Integer.valueOf(courseKpointPlayVO.getVideoTime());
				String timeStr = null;
				int hour;
				int minute ;
				int second = 0;
				if (value > 0) {
					minute = value / 60;
					second = value % 60;
					if(minute<10){
						if(second<10){
							timeStr = "0"+minute + ":0" + second;
						}else {
							timeStr = "0"+minute + ":" + second;
						}
					}else {
						timeStr = minute + ":" + second;
					}
					courseKpointPlayVO.setVideoTime(timeStr);
				}
			}
		}
		courseKpointPlayVO.setRateComponent(rateComponent);
		return R.data(courseKpointPlayVO);
	}




	/**
	 * 分页 知识点的基本信息
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入courseKpoint")
	public R<List<CourseKpointVO>> list(CourseKpoint courseKpoint, Query query) {
		List<CourseKpoint> list = courseKpointService.list(Condition.getQueryWrapper(courseKpoint).lambda().orderByDesc(CourseKpoint::getSort));
			for(CourseKpoint courseKpoint1:list){
				if(Func.equals(courseKpoint1.getVideoType(),"aliyunvod")||Func.equals(courseKpoint1.getVideoType(),"baijiayunvod")||Func.equals(courseKpoint1.getVideoType(),"polyvvod")||Func.equals(courseKpoint1.getVideoType(),"othervod")){
					if(courseKpoint1.getVideoTime()!=null&&!"".equals(courseKpoint1.getVideoTime())){
						int value= Integer.valueOf(courseKpoint1.getVideoTime());
						String timeStr = null;
						int hour;
						int minute ;
						int second = 0;
						if (value > 0) {
							minute = value / 60;
							second = value % 60;
							if(minute<10){
								if(second<10){
									timeStr = "0"+minute + ":0" + second;
								}else {
									timeStr = "0"+minute + ":" + second;
								}
							}else {
								timeStr = minute + ":" + second;
							}
							courseKpoint1.setVideoTime(timeStr);
						}
					}
				}
			}
		return R.data(CourseKpointWrapper.build().listNodeVO(list));
	}

	/**
	 * 自定义分页 知识点的基本信息
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入courseKpoint")
	public R<IPage<CourseKpointVO>> page(CourseKpointVO courseKpoint, Query query) {
		IPage<CourseKpointVO> pages = courseKpointService.selectCourseKpointPage(Condition.getPage(query), courseKpoint);
		return R.data(pages);
	}

	/**
	 * 新增 知识点的基本信息
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入courseKpoint")
	public R save(@Valid @RequestBody CourseKpoint courseKpoint) {
		return R.status(courseKpointService.save(courseKpoint));
	}

	/**
	 * 修改 知识点的基本信息
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入courseKpoint")
	public R update(@Valid @RequestBody CourseKpoint courseKpoint) {
		//判断如果为百家直播则创建房间
		long liveBegin =0L;
		long liveEnd=0L;
		if(Func.equals(courseKpoint.getVideoType(),"aliyunlive")||Func.equals(courseKpoint.getVideoType(),"aliyunReplay")||Func.equals(courseKpoint.getVideoType(),"baijiayunlive")||Func.equals(courseKpoint.getVideoType(),"baijiayunReplay")||Func.equals(courseKpoint.getVideoType(),"polyvlive")||Func.equals(courseKpoint.getVideoType(),"polyvReplay")||Func.equals(courseKpoint.getVideoType(),"otherlive")){
			liveBegin=courseKpoint.getLiveBeginTime().toEpochSecond(ZoneOffset.of("+8"));
			liveEnd=courseKpoint.getLiveEndTime().toEpochSecond(ZoneOffset.of("+8"));
			courseKpoint.setVideoTime((liveEnd-liveBegin)+"");
		}else {
			if(courseKpoint.getParentId()!=0){
				if(Func.isNotEmpty(courseKpoint.getVideoTime())&&!("").equals(courseKpoint.getVideoTime())&&!("0").equals(courseKpoint.getVideoTime())){
					int mintime =Integer.valueOf(courseKpoint.getVideoTime().substring(0,courseKpoint.getVideoTime().indexOf(":")));
					int sintime =Integer.valueOf(courseKpoint.getVideoTime().substring(courseKpoint.getVideoTime().lastIndexOf(":")+1));
					String times=Integer.toString(mintime*60+sintime);
					courseKpoint.setVideoTime(times);
				}
			}
		}
/*		if(Func.equals(courseKpoint.getVideoType(),"baijiayunlive")){
			if((liveEnd-liveBegin)>= 86400){
				return R.fail("直播时长不能大于24小时");
			}
			Map room = BajiayunUtil.updateRoom(courseKpoint.getVideoUrl(),courseKpoint.getName(), liveBegin, liveEnd);
		}*/
		return R.status(courseKpointService.updateById(courseKpoint));
	}

	/**
	 * 新增或修改 知识点的基本信息
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入courseKpoint")
	public R submit(@Valid @RequestBody CourseKpoint courseKpoint) {
		if(Func.isNull(courseKpoint.getParentId())){
			courseKpoint.setParentId(0);
		}
		long liveBegin =0L;
		long liveEnd=0L;
		if(Func.equals(courseKpoint.getVideoType(),"aliyunlive")||Func.equals(courseKpoint.getVideoType(),"aliyunReplay")||Func.equals(courseKpoint.getVideoType(),"baijiayunlive")||Func.equals(courseKpoint.getVideoType(),"baijiayunReplay")||Func.equals(courseKpoint.getVideoType(),"polyvlive")||Func.equals(courseKpoint.getVideoType(),"polyvReplay")||Func.equals(courseKpoint.getVideoType(),"otherlive")){
			liveBegin=courseKpoint.getLiveBeginTime().toEpochSecond(ZoneOffset.of("+8"));
			liveEnd=courseKpoint.getLiveEndTime().toEpochSecond(ZoneOffset.of("+8"));
			courseKpoint.setVideoTime((liveEnd-liveBegin)+"");
		}else {
			if(Func.isNotEmpty(courseKpoint.getVideoTime())){
				int mintime =Integer.valueOf(courseKpoint.getVideoTime().substring(0,courseKpoint.getVideoTime().indexOf(":")));
				int sintime =Integer.valueOf(courseKpoint.getVideoTime().substring(courseKpoint.getVideoTime().lastIndexOf(":")+1));
				String times=Integer.toString(mintime*60+sintime);
				courseKpoint.setVideoTime(times);
			}
		}
		//判断如果为百家直播则创建房间
/*		if(Func.equals(courseKpoint.getVideoType(),"baijiayunlive")){
			if((liveEnd-liveBegin)>= 86400){
				return R.fail("直播时长不能大于24小时");
			}
			Map room = BajiayunUtil.createRoom(courseKpoint.getName(), liveBegin, liveEnd);
			courseKpoint.setVideoUrl(Func.toStr(room.get("room_id")));

		}*/
		return R.status(courseKpointService.saveOrUpdate(courseKpoint));
	}


	/**
	 * 删除 知识点的基本信息
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(courseKpointService.deleteIds(ids));
	}

	/**
	 * 获取菜单树形结构
	 */
	@GetMapping("/tree")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "树形结构", notes = "树形结构")
	public R<List<CourseKpointVO>> tree() {
		List<CourseKpointVO> tree = courseKpointService.tree();
		return R.data(tree);
	}


	/**
	 * 获取直播地址
	 * @param streamName
	 * @return
	 */
	@RequestMapping("createUrl")
	@ResponseBody
	public R createUrl(String streamName){
		String livePushUrl = AliyunLiveUtils.createLivePushUrl(streamName);
		Map<String, String> livePullUrl = AliyunLiveUtils.createLivePullUrl(streamName);
		Map resultMap = Maps.newHashMap();
		resultMap.put("livePushUrl",livePushUrl);
		resultMap.put("livePullUrl",livePullUrl.get("m3u8Url"));
		return R.data(resultMap);
	}
}
