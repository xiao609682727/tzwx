
package org.springcrazy.modules.msg.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springcrazy.core.boot.ctrl.CrazyController;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.mp.support.Query;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.msg.entity.MsgRange;
import org.springcrazy.modules.msg.service.IMsgRangeService;
import org.springcrazy.modules.msg.vo.MsgRangeVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 消息范围 一对多中间表 控制器
 *
 * @author TongZhou
 * @since 2020-05-18
 */
@RestController
@AllArgsConstructor
@RequestMapping("/edu/msgrange")
@Api(value = "消息范围 一对多中间表", tags = "消息范围 一对多中间表接口")
public class MsgRangeController extends CrazyController {

	private IMsgRangeService msgRangeService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入msgRange")
	public R<MsgRange> detail(MsgRange msgRange) {
		MsgRange detail = msgRangeService.getOne(Condition.getQueryWrapper(msgRange));
		return R.data(detail);
	}

	/**
	 * 分页 消息范围 一对多中间表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入msgRange")
	public R<IPage<MsgRange>> list(MsgRange msgRange, Query query) {
		IPage<MsgRange> pages = msgRangeService.page(Condition.getPage(query), Condition.getQueryWrapper(msgRange));
		return R.data(pages);
	}

	/**
	 * 自定义分页 消息范围 一对多中间表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入msgRange")
	public R<IPage<MsgRangeVO>> page(MsgRangeVO msgRange, Query query) {
		IPage<MsgRangeVO> pages = msgRangeService.selectMsgRangePage(Condition.getPage(query), msgRange);
		return R.data(pages);
	}

	/**
	 * 新增 消息范围 一对多中间表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入msgRange")
	public R save(@Valid @RequestBody MsgRange msgRange) {
		return R.status(msgRangeService.save(msgRange));
	}

	/**
	 * 修改 消息范围 一对多中间表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入msgRange")
	public R update(@Valid @RequestBody MsgRange msgRange) {
		return R.status(msgRangeService.updateById(msgRange));
	}

	/**
	 * 新增或修改 消息范围 一对多中间表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入msgRange")
	public R submit(@Valid @RequestBody MsgRange msgRange) {
		return R.status(msgRangeService.saveOrUpdate(msgRange));
	}


	/**
	 * 删除 消息范围 一对多中间表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(msgRangeService.removeByIds(Func.toIntList(ids)));
	}


}
