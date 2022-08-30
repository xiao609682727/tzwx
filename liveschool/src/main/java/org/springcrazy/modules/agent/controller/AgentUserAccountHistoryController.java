
package org.springcrazy.modules.agent.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.codec.Charsets;
import org.springcrazy.core.boot.ctrl.CrazyController;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.mp.support.Query;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.agent.entity.AgentUserAccountHistory;
import org.springcrazy.modules.agent.service.IAgentUserAccountHistoryService;
import org.springcrazy.modules.agent.vo.AgentUserAccountHistoryVO;
import org.springcrazy.modules.agent.wrapper.AgentUserAccountHistoryWrapper;
import org.springcrazy.modules.user.service.IUserAccountHistoryService;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 代理商账户流水记录 控制器
 *
 * @author TongZhou
 * @since 2021-04-30
 */
@RestController
@AllArgsConstructor
@RequestMapping("/edu/agentuseraccounthistory")
@Api(value = "代理商账户流水记录", tags = "代理商账户流水记录接口")
public class AgentUserAccountHistoryController extends CrazyController {

	private IAgentUserAccountHistoryService agentUserAccountHistoryService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入agentUserAccountHistory")
	public R<AgentUserAccountHistoryVO> detail(AgentUserAccountHistory agentUserAccountHistory) {
		AgentUserAccountHistory detail = agentUserAccountHistoryService.getOne(Condition.getQueryWrapper(agentUserAccountHistory));
		return R.data(AgentUserAccountHistoryWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 代理商账户流水记录
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入agentUserAccountHistory")
	public R<IPage<AgentUserAccountHistoryVO>> list(AgentUserAccountHistory agentUserAccountHistory, Query query) {
		QueryWrapper<AgentUserAccountHistory> queryWrapper =  Condition.getQueryWrapper(agentUserAccountHistory);
		queryWrapper.lambda().orderByDesc(AgentUserAccountHistory::getCreateTime);
		IPage<AgentUserAccountHistory> pages = agentUserAccountHistoryService.page(Condition.getPage(query),queryWrapper);
		return R.data(AgentUserAccountHistoryWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 代理商账户流水记录
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入agentUserAccountHistory")
	public R<IPage<AgentUserAccountHistoryVO>> page(AgentUserAccountHistoryVO agentUserAccountHistory, Query query) {
		IPage<AgentUserAccountHistoryVO> pages = agentUserAccountHistoryService.selectAgentUserAccountHistoryPage(Condition.getPage(query), agentUserAccountHistory);
		return R.data(pages);
	}

	/**
	 * 新增 代理商账户流水记录
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入agentUserAccountHistory")
	public R save(@Valid @RequestBody AgentUserAccountHistory agentUserAccountHistory) {
		return R.status(agentUserAccountHistoryService.save(agentUserAccountHistory));
	}

	/**
	 * 修改 代理商账户流水记录
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入agentUserAccountHistory")
	public R update(@Valid @RequestBody AgentUserAccountHistory agentUserAccountHistory) {
		return R.status(agentUserAccountHistoryService.updateById(agentUserAccountHistory));
	}

	/**
	 * 新增或修改 代理商账户流水记录
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入agentUserAccountHistory")
	public R submit(@Valid @RequestBody AgentUserAccountHistory agentUserAccountHistory) {
		return R.status(agentUserAccountHistoryService.saveOrUpdate(agentUserAccountHistory));
	}


	/**
	 * 删除 代理商账户流水记录
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(agentUserAccountHistoryService.removeByIds(Func.toIntList(ids)));
	}


	private IUserAccountHistoryService userAccountHistoryService;
	/**
	 * 导出代理商账户记录
	 */
	@SneakyThrows
	@GetMapping("export-agentUserAccountHistory")
	@ApiOperationSupport(order = 13)
	@ApiOperation(value = "导出代理商账户记录信息", notes = "传入agentUserAccountHistory")
	public void exportUserAccountHistory(@ApiIgnore @RequestParam Map<String, Object> agentUserAccountHistory, HttpServletResponse response) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String date=simpleDateFormat.format(new Date());
		agentUserAccountHistory.remove("crazy-auth");
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding(Charsets.UTF_8.name());
		String fileName = URLEncoder.encode("代理商账户记录数据导出"+date, Charsets.UTF_8.name());
		response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
		agentUserAccountHistoryService.exportAgentUserAccountHistory(response,agentUserAccountHistory);
	}
}
