
package org.springcrazy.modules.edu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
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
import org.springcrazy.modules.edu.entity.LiveRoom;
import org.springcrazy.modules.edu.service.ILiveRoomService;
import org.springcrazy.modules.edu.vo.LiveRoomVO;
import org.springcrazy.modules.edu.wrapper.LiveRoomWrapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

/**
 * 直播房间 控制器
 *
 * @author TongZhou
 * @since 2020-11-09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/edu/liveroom")
@Api(value = "直播房间", tags = "直播房间接口")
public class LiveRoomController extends CrazyController {

	private ILiveRoomService liveRoomService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入liveRoom")
	public R<LiveRoomVO> detail(LiveRoom liveRoom) {
		LiveRoom detail = liveRoomService.getOne(Condition.getQueryWrapper(liveRoom));
		if(Func.isEmpty(detail)){
			return R.data(null);
		}
		return R.data(LiveRoomWrapper.build().entityVO(detail));
	}

	/**
	 * 详情
	 */
	@GetMapping("/detailByRoomId")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入liveRoom")
	public R<LiveRoomVO> detailByRoomId(LiveRoom liveRoom) {
		LiveRoom detail = liveRoomService.getOne(Condition.getQueryWrapper(liveRoom));
		return R.data(LiveRoomWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 直播房间
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入liveRoom")
	public R<IPage<LiveRoomVO>> list(LiveRoom liveRoom, Query query) {
		QueryWrapper<LiveRoom> queryWrapper =  Condition.getQueryWrapper(liveRoom);
		queryWrapper.lambda().orderByDesc(LiveRoom::getCreateTime);
		//直播房间名称
		queryWrapper.lambda().like(Func.isNotBlank(liveRoom.getName()), LiveRoom::getName,liveRoom.getName());
		liveRoom.setName(null);
		//房间id
		queryWrapper.lambda().like(Func.isNotBlank(liveRoom.getClassroomId()), LiveRoom::getClassroomId,liveRoom.getClassroomId());
		liveRoom.setClassroomId(null);

		IPage<LiveRoom> pages = liveRoomService.page(Condition.getPage(query),queryWrapper);
		return R.data(LiveRoomWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 直播房间
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入liveRoom")
	public R<IPage<LiveRoomVO>> page(LiveRoomVO liveRoom, Query query) {
		IPage<LiveRoomVO> pages = liveRoomService.selectLiveRoomPage(Condition.getPage(query), liveRoom);
		return R.data(pages);
	}

	/**
	 * 新增 直播房间
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入liveRoom")
	public R save(@Valid @RequestBody LiveRoom liveRoom) {
		return R.status(liveRoomService.save(liveRoom));
	}

	/**
	 * 修改 直播房间
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入liveRoom")
	public R update(@Valid @RequestBody LiveRoom liveRoom) {
		long liveBegin =0L;
		long liveEnd=0L;
		liveBegin=liveRoom.getStartTime().toEpochSecond(ZoneOffset.of("+8"));
		liveEnd=liveRoom.getEndTime().toEpochSecond(ZoneOffset.of("+8"));
		if((liveEnd-liveBegin)>= 86400){
			return R.fail("直播时长不能大于24小时");
		}
		if("1".equals(liveRoom.getLiveType())) {
			Map room = BajiayunUtil.updateRoom(liveRoom.getClassroomId(), liveRoom.getName(), liveBegin, liveEnd);
		}
		return R.status(liveRoomService.updateById(liveRoom));
	}

	/**
	 * 新增或修改 直播房间
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入liveRoom")
	public R submit(@Valid @RequestBody LiveRoom liveRoom) {
		long liveBegin =0L;
		long liveEnd=0L;
		liveBegin=liveRoom.getStartTime().toEpochSecond(ZoneOffset.of("+8"));
		liveEnd=liveRoom.getEndTime().toEpochSecond(ZoneOffset.of("+8"));
		if((liveEnd-liveBegin)>= 86400){
			return R.fail("直播时长不能大于24小时");
		}
		if("1".equals(liveRoom.getLiveType())){
			Map room = BajiayunUtil.createRoom(liveRoom);
			liveRoom.setClassroomId(Func.toStr(room.get("room_id")));
			liveRoom.setTeacherCode(Func.toStr(room.get("teacher_code")));
			liveRoom.setTutorCode(Func.toStr(room.get("admin_code")));
			liveRoom.setStudentCode(Func.toStr(room.get("student_code")));
		}else {
			liveRoom.setRoomType(null);
			liveRoom.setPlaybackStatus(null);
		}
		return R.status(liveRoomService.saveOrUpdate(liveRoom));
	}


	/**
	 * 删除 直播房间
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		List<Integer> list = Func.toIntList(ids);
		for (Integer id : list) {
			LiveRoom liveRoom = liveRoomService.getById(id);
			if("1".equals(liveRoom.getLiveType())){
				BajiayunUtil.deleteRoom(liveRoom.getClassroomId());
			}
			liveRoomService.removeById(liveRoom.getId());
		}
		return R.status(true);
	}


}
