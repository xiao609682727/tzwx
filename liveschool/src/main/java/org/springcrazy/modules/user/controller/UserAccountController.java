
package org.springcrazy.modules.user.controller;

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
import org.springcrazy.modules.user.entity.UserAccount;
import org.springcrazy.modules.user.entity.UserAccountHistory;
import org.springcrazy.modules.user.service.IUserAccountService;
import org.springcrazy.modules.user.vo.UserAccountVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * 账户信息账户信息，记录用户的账户金额 控制器
 *
 * @author TongZhou
 * @since 2020-05-07
 */
@RestController
@AllArgsConstructor
@RequestMapping("/user/useraccount")
@Api(value = "账户信息账户信息，记录用户的账户金额", tags = "账户信息账户信息，记录用户的账户金额接口")
public class UserAccountController extends CrazyController {

	private IUserAccountService userAccountService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入userAccount")
	public R<UserAccount> detail(UserAccount userAccount) {
		UserAccount detail = userAccountService.getOne(Condition.getQueryWrapper(userAccount));
		return R.data(detail);
	}

	/**
	 * 分页 账户信息账户信息，记录用户的账户金额
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入userAccount")
	public R<IPage<UserAccount>> list(UserAccount userAccount, Query query) {
		//判断为学生 则进行学生id查询
		if(Func.equals(SecureUtil.getUserRole(),"student")){
			Integer userId = SecureUtil.getUserId();
			userAccount.setUserId(userId);
		}
		IPage<UserAccount> pages = userAccountService.page(Condition.getPage(query), Condition.getQueryWrapper(userAccount));
		return R.data(pages);
	}

	/**
	 * 自定义分页 账户信息账户信息，记录用户的账户金额
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入userAccount")
	public R<IPage<UserAccountVO>> page(UserAccountVO userAccount, Query query) {
		IPage<UserAccountVO> pages = userAccountService.selectUserAccountPage(Condition.getPage(query), userAccount);
		return R.data(pages);
	}

	/**
	 * 新增 账户信息账户信息，记录用户的账户金额
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入userAccount")
	public R save(@Valid @RequestBody UserAccount userAccount) {
		return R.status(userAccountService.save(userAccount));
	}

	/**
	 * 修改 账户信息账户信息，记录用户的账户金额
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入userAccount")
	public R update(@Valid @RequestBody UserAccount userAccount) {
		return R.status(userAccountService.updateById(userAccount));
	}

	/**
	 * 新增或修改 账户信息账户信息，记录用户的账户金额
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入userAccount")
	public R submit(@Valid @RequestBody UserAccount userAccount) {
		return R.status(userAccountService.saveOrUpdate(userAccount));
	}


	/**
	 * 删除 账户信息账户信息，记录用户的账户金额
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(userAccountService.removeByIds(Func.toIntList(ids)));
	}


	/**
	 * 储值编号
	 * type 1 充值  2 扣款
	 * @return
	 */
	@PostMapping("/money")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入userAccount")
	public R money(Integer userId, BigDecimal money,String type) {
		String desc = "后台充值";
		if(Func.equals(type,"2")){
			desc = "后台扣款";
		}
		userAccountService.money(userId,money, type,UserAccountHistory.BIZTYPE_SYSTEM,desc,"");
		return R.success("成功");
	}
}
