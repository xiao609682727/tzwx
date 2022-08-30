
package org.springcrazy.modules.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springcrazy.common.tool.MsgSMSFactory;
import org.springcrazy.core.boot.ctrl.CrazyController;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.mp.support.Query;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.web.entity.SmsConfig;
import org.springcrazy.modules.web.service.ISmsConfigService;
import org.springcrazy.modules.web.vo.SmsConfigVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 *  控制器
 *
 * @author TongZhou
 * @since 2020-05-09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/web/smsconfig")
@Api(value = "系统短信配置", tags = "系统短信配置")
public class SmsConfigController extends CrazyController {

	private ISmsConfigService smsConfigService;



	/**
	 * 分页
	 */
	@GetMapping("")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入msgConfig")
	public R<List<SmsConfig>> getList(SmsConfig smsConfig, Query query) {
		List<SmsConfig> lists = smsConfigService.list(Condition.getQueryWrapper(smsConfig));
		return R.data(lists);
	}

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入smsConfig")
	public R<SmsConfig> detail(SmsConfig smsConfig) {
		SmsConfig detail = smsConfigService.getOne(Condition.getQueryWrapper(smsConfig));
		return R.data(detail);
	}

	/**
	 * 分页
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入smsConfig")
	public R<IPage<SmsConfig>> list(SmsConfig smsConfig, Query query) {
		QueryWrapper<SmsConfig> queryWrapper = Condition.getQueryWrapper(new SmsConfig());
		queryWrapper.lambda().eq(Func.isNotBlank(smsConfig.getType()),SmsConfig::getType,smsConfig.getType())
		.like(Func.isNotBlank(smsConfig.getName()),SmsConfig::getName,smsConfig.getName())
		.like(Func.isNotBlank(smsConfig.getOtherId()),SmsConfig::getOtherId,smsConfig.getOtherId());
		IPage<SmsConfig> pages = smsConfigService.page(Condition.getPage(query), queryWrapper);
		return R.data(pages);
	}

	/**
	 * 自定义分页
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入smsConfig")
	public R<IPage<SmsConfigVO>> page(SmsConfigVO smsConfig, Query query) {
		IPage<SmsConfigVO> pages = smsConfigService.selectSmsConfigPage(Condition.getPage(query), smsConfig);
		return R.data(pages);
	}

	/**
	 * 新增
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入smsConfig")
	public R save(@Valid @RequestBody SmsConfig smsConfig) {
		return R.status(smsConfigService.save(smsConfig));
	}

	/**
	 * 修改
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入smsConfig")
	public R update(@Valid @RequestBody SmsConfig smsConfig) {
		return R.status(smsConfigService.updateById(smsConfig));
	}

	/**
	 * 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入smsConfig")
	public R submit(@Valid @RequestBody SmsConfig smsConfig) {
		return R.status(smsConfigService.saveOrUpdate(smsConfig));
	}


	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(smsConfigService.removeByIds(Func.toIntList(ids)));
	}


	/**
	 * 删除
	 */
	@PostMapping("/testMsgSignAndTemp")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "测试短信", notes = "传入短信信息")
	public R testMsgSignAndTemp(
								@ApiParam(value = "手机号", required = true) @RequestParam String moblie,
								@ApiParam(value = "模板id", required = true) @RequestParam String tempId,
								@ApiParam(value = "签名id", required = true) @RequestParam String signId,
								@ApiParam(value = "参数列表", required = true) @RequestParam String paramsString
								) {
		Map<String,String> params = Maps.newLinkedHashMap();
		if(Func.isNotBlank(paramsString)) {
			String[] split = paramsString.split("&");
			for (int i = 0; i < split.length; i++) {
				String paramsName=split[i].substring(0, split[i].indexOf("="));
				String paramsValue=split[i].substring(paramsName.length()+1, split[i].length());
				System.out.println("参数名称："+paramsName+"参数值："+paramsValue);
				params.put(paramsName,paramsValue);
			}
		}
		MsgSMSFactory.getMsgSender().sendBatchSMS(Func.toStrList(moblie),signId,tempId,params);
		return R.status(true);
	}

}
