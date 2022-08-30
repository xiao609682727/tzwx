
package org.springcrazy.modules.agent.controller;

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
import org.springcrazy.modules.agent.entity.AgentAccount;
import org.springcrazy.modules.agent.entity.AgentUserAccountHistory;
import org.springcrazy.modules.agent.service.IAgentAccountService;
import org.springcrazy.modules.agent.vo.AgentAccountVO;
import org.springcrazy.modules.agent.wrapper.AgentAccountWrapper;
import org.springcrazy.modules.user.service.IUserAccountService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * 代理商账户信息账户信息，记录用户的代理商账户金额 控制器
 *
 * @author TongZhou
 * @since 2021-04-30
 */
@RestController
@AllArgsConstructor
@RequestMapping("/edu/agentaccount")
@Api(value = "代理商账户信息账户信息，记录用户的代理商账户金额", tags = "代理商账户信息账户信息，记录用户的代理商账户金额接口")
public class AgentAccountController extends CrazyController {

	private IAgentAccountService agentAccountService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入agentAccount")
	public R<AgentAccountVO> detail(AgentAccount agentAccount) {
		AgentAccount detail = agentAccountService.getOne(Condition.getQueryWrapper(agentAccount));
		return R.data(AgentAccountWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 代理商账户信息账户信息，记录用户的代理商账户金额
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入agentAccount")
	public R<IPage<AgentAccountVO>> list(AgentAccount agentAccount, Query query) {
		IPage<AgentAccount> pages = agentAccountService.page(Condition.getPage(query), Condition.getQueryWrapper(agentAccount));
		return R.data(AgentAccountWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 代理商账户信息账户信息，记录用户的代理商账户金额
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入agentAccount")
	public R<IPage<AgentAccountVO>> page(AgentAccountVO agentAccount, Query query) {
		//如果值为-1说明是代理商查询，则设置当登录用户的id设置
		if(Func.isNotEmpty(agentAccount.getAgentUserId())&&-1==agentAccount.getAgentUserId()){
			agentAccount.setAgentUserId(SecureUtil.getUserId());
		}
		IPage<AgentAccountVO> pages = agentAccountService.selectAgentAccountPage(Condition.getPage(query), agentAccount);
		return R.data(pages);
	}

	/**
	 * 新增 代理商账户信息账户信息，记录用户的代理商账户金额
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入agentAccount")
	public R save(@Valid @RequestBody AgentAccount agentAccount) {
		return R.status(agentAccountService.save(agentAccount));
	}

	/**
	 * 修改 代理商账户信息账户信息，记录用户的代理商账户金额
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入agentAccount")
	public R update(@Valid @RequestBody AgentAccount agentAccount) {
		return R.status(agentAccountService.updateById(agentAccount));
	}

	/**
	 * 新增或修改 代理商账户信息账户信息，记录用户的代理商账户金额
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入agentAccount")
	public R submit(@Valid @RequestBody AgentAccount agentAccount) {
		return R.status(agentAccountService.saveOrUpdate(agentAccount));
	}


	/**
	 * 删除 代理商账户信息账户信息，记录用户的代理商账户金额
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(agentAccountService.removeByIds(Func.toIntList(ids)));
	}

	private IUserAccountService userAccountService;
	/**
	 * 储值编号
	 * type 1 充值  2 扣款
	 * @return
	 */
	@PostMapping("/money")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "修改", notes = "传入userAccount")
	public R money(Integer userId,BigDecimal money, String type) {

		agentAccountService.addMoney(userId,money, type, AgentUserAccountHistory.BIZTYPE_SYSTEM,null);
		return R.success("成功");
	}

}
