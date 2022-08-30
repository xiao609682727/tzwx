
package org.springcrazy.modules.edu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springcrazy.common.tool.BajiayunUtil;
import org.springcrazy.core.boot.ctrl.CrazyController;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.mp.support.Query;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.edu.entity.LivePlayback;
import org.springcrazy.modules.edu.service.ILivePlaybackService;
import org.springcrazy.modules.edu.vo.LivePlaybackVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * 直播回放管理 控制器
 *
 * @author TongZhou
 * @since 2020-11-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/edu/liveplayback")
@Api(value = "直播回放管理", tags = "直播回放管理接口")
public class LivePlaybackController extends CrazyController {

	private ILivePlaybackService livePlaybackService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入livePlayback")
	public R<LivePlayback> detail(LivePlayback livePlayback) {
		LivePlayback detail = livePlaybackService.getOne(Condition.getQueryWrapper(livePlayback));
		return R.data(detail);
	}

	/**
	 * 分页 直播回放管理
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入livePlayback")
	public R<IPage<LivePlayback>> list(LivePlayback livePlayback, Query query) {
		IPage<LivePlayback> pages = livePlaybackService.page(Condition.getPage(query), Condition.getQueryWrapper(livePlayback));
		return R.data(pages);
	}

	/**
	 * 自定义分页 直播回放管理
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入livePlayback")
	public R<IPage<LivePlaybackVO>> page(LivePlaybackVO livePlayback, Query query) {
		IPage<LivePlaybackVO> pages = livePlaybackService.selectLivePlaybackPage(Condition.getPage(query), livePlayback);
		return R.data(pages);
	}

	/**
	 * 新增 直播回放管理
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入livePlayback")
	public R save(@Valid @RequestBody LivePlayback livePlayback) {
		return R.status(livePlaybackService.save(livePlayback));
	}

	/**
	 * 修改 直播回放管理
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入livePlayback")
	public R update(@Valid @RequestBody LivePlayback livePlayback) {
		return R.status(livePlaybackService.updateById(livePlayback));
	}

	/**
	 * 新增或修改 直播回放管理
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入livePlayback")
	public R submit(@Valid @RequestBody LivePlayback livePlayback) {
		return R.status(livePlaybackService.saveOrUpdate(livePlayback));
	}


	/**
	 * 删除 直播回放管理
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		List<Integer> list = Func.toIntList(ids);
		for (Integer id : list) {
			LivePlayback livePlayback = livePlaybackService.getById(id);
			BajiayunUtil.playbackDelete(livePlayback.getLiveRoomId());
			livePlaybackService.removeById(livePlayback.getId());
		}
		return R.status(true);
	}

	@GetMapping("/sync")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "同步", notes = "同步")
	public R sync(String roomId) {

		LivePlayback livePlayback = livePlaybackService.getOne(new QueryWrapper<LivePlayback>().lambda().eq(LivePlayback::getLiveRoomId, roomId));
		if(Func.isEmpty(livePlayback)){
			livePlayback = new LivePlayback();
		}
		Map<String,Object> map = BajiayunUtil.getBasicInfo(roomId);
		livePlayback.setLiveRoomId(roomId);
		livePlayback.setName(Func.toStr(map.get("name")));
		livePlayback.setPrefaceUrl(Func.toStr(map.get("preface_url")));
		livePlayback.setTotalSize(Func.toStr(map.get("total_size")));
		livePlayback.setStatus(Func.toStr(map.get("status")));
		livePlayback.setVideoId(Func.toStr(map.get("video_id")));
		livePlayback.setVideoDuration(Func.toStr(map.get("length")));
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		livePlayback.setCreateTime(LocalDateTime.parse(map.get("create_time")+"", df));
		livePlaybackService.saveOrUpdate(livePlayback);
		return R.status(true);
	}


	@GetMapping("/getUrl")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "获取回放的url", notes = "获取回放的url")
	public R getUrl(LivePlayback livePlayback) {
		LivePlayback detail = livePlaybackService.getOne(Condition.getQueryWrapper(livePlayback));
		//房间号
		Map<String,Object> params = Maps.newHashMap();
		params.put("url",BajiayunUtil.getBackToken(detail.getLiveRoomId()));
		return R.data(params);
	}
}
