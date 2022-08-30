
package org.springcrazy.modules.web.controller;

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
import org.springcrazy.modules.web.entity.MsgConfig;
import org.springcrazy.modules.web.service.IMsgConfigService;
import org.springcrazy.modules.web.vo.MsgConfigVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 *  控制器
 *
 * @author TongZhou
 * @since 2020-05-09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/web/msgconfig")
@Api(value = "短信消息模板", tags = "短信消息模板")
public class MsgConfigController extends CrazyController {

	private IMsgConfigService msgConfigService;



	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入msgConfig")
	public R<MsgConfig> detail(MsgConfig msgConfig) {
		MsgConfig detail = msgConfigService.getOne(Condition.getQueryWrapper(msgConfig));
		return R.data(detail);
	}

	/**
	 * 分页
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入msgConfig")
	public R<IPage<MsgConfig>> list(MsgConfig msgConfig, Query query) {
		IPage<MsgConfig> pages = msgConfigService.page(Condition.getPage(query), Condition.getQueryWrapper(msgConfig));
		return R.data(pages);
	}

	/**
	 * 自定义分页
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入msgConfig")
	public R<IPage<MsgConfigVO>> page(MsgConfigVO msgConfig, Query query) {
		IPage<MsgConfigVO> pages = msgConfigService.selectMsgConfigPage(Condition.getPage(query), msgConfig);
		return R.data(pages);
	}

	/**
	 * 新增
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入msgConfig")
	public R save(@Valid @RequestBody MsgConfig msgConfig) {
		return R.status(msgConfigService.save(msgConfig));
	}

	/**
	 * 修改
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入msgConfig")
	public R update(@Valid @RequestBody MsgConfig msgConfig) {
		return R.status(msgConfigService.updateById(msgConfig));
	}

	/**
	 * 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入msgConfig")
	public R submit(@Valid @RequestBody MsgConfig msgConfig) {
		return R.status(msgConfigService.saveOrUpdate(msgConfig));
	}


	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(msgConfigService.removeByIds(Func.toIntList(ids)));
	}

	/**
	 * 获取配置列表
	 */
	@GetMapping("/configList")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "获取配置列表", notes = "传入msgConfig")
	public R<List<MsgConfig>> configList(MsgConfig msgConfig) {
		List<MsgConfig> list = msgConfigService.list(Condition.getQueryWrapper(msgConfig));
		return R.data(list);
	}
}
