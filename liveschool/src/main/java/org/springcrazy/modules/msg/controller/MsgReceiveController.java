
package org.springcrazy.modules.msg.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springcrazy.core.boot.ctrl.CrazyController;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.mp.support.Query;
import org.springcrazy.core.secure.utils.SecureUtil;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.msg.entity.MsgReceive;
import org.springcrazy.modules.msg.service.IMsgReceiveService;
import org.springcrazy.modules.msg.vo.MsgReceiveVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 站内信 控制器
 *
 * @author TongZhou
 * @since 2020-05-18
 */
@RestController
@AllArgsConstructor
@RequestMapping("/msg/msgreceive")
@Api(value = "站内信", tags = "站内信接口")
public class MsgReceiveController extends CrazyController {

	private IMsgReceiveService msgReceiveService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入msgReceive")
	public R<MsgReceive> detail(MsgReceive msgReceive) {
		MsgReceive detail = msgReceiveService.getOne(Condition.getQueryWrapper(msgReceive));
		return R.data(detail);
	}

	/**
	 * 分页 站内信
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入msgReceive")
	public R<IPage<MsgReceive>> list(MsgReceive msgReceive, Query query) {
		//判断为学生 则进行学生id查询
		if(Func.equals(SecureUtil.getUserRole(),"student")){
			Integer userId = SecureUtil.getUserId();
			msgReceive.setReceivingCusid(userId);
		}
		IPage<MsgReceive> pages = msgReceiveService.page(Condition.getPage(query), Condition.getQueryWrapper(msgReceive).lambda().orderByDesc(MsgReceive::getCreateTime));
		return R.data(pages);
	}

	/**
	 * 自定义分页 站内信
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入msgReceive")
	public R<IPage<MsgReceiveVO>> page(MsgReceiveVO msgReceive, Query query) {
		IPage<MsgReceiveVO> pages = msgReceiveService.selectMsgReceivePage(Condition.getPage(query), msgReceive);
		return R.data(pages);
	}

	/**
	 * 新增 站内信
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入msgReceive")
	public R save(@Valid @RequestBody MsgReceive msgReceive) {
		return R.status(msgReceiveService.save(msgReceive));
	}

	/**
	 * 修改 站内信
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入msgReceive")
	public R update(@Valid @RequestBody MsgReceive msgReceive) {
		return R.status(msgReceiveService.updateById(msgReceive));
	}

	/**
	 * 新增或修改 站内信
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入msgReceive")
	public R submit(@Valid @RequestBody MsgReceive msgReceive) {
		return R.status(msgReceiveService.saveOrUpdate(msgReceive));
	}


	/**
	 * 删除 站内信
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(msgReceiveService.removeByIds(Func.toIntList(ids)));
	}


	/**
	 * 更新站内信状态
	 */
	@PostMapping("/updateState")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入msgReceive")
	public R update() {
		Integer userId = SecureUtil.getUserId();
		UpdateWrapper<MsgReceive> msgReceiveUpdateWrapper = new UpdateWrapper<>();
		msgReceiveUpdateWrapper.set("status",MsgReceive.STATUS_READ).eq("receiving_cusid",userId);
		return R.status(msgReceiveService.update(msgReceiveUpdateWrapper));
	}

}
