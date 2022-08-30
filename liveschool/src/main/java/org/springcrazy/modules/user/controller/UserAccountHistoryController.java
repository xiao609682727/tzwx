
package org.springcrazy.modules.user.controller;

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
import org.springcrazy.core.secure.utils.SecureUtil;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.user.entity.UserAccountHistory;
import org.springcrazy.modules.user.service.IUserAccountHistoryService;
import org.springcrazy.modules.user.vo.UserAccountHistoryVO;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 账户流水记录 控制器
 *
 * @author TongZhou
 * @since 2020-05-07
 */
@RestController
@AllArgsConstructor
@RequestMapping("/user/useraccounthistory")
@Api(value = "账户流水记录", tags = "账户流水记录接口")
public class UserAccountHistoryController extends CrazyController {

	private IUserAccountHistoryService userAccountHistoryService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入userAccountHistory")
	public R<UserAccountHistory> detail(UserAccountHistory userAccountHistory) {
		UserAccountHistory detail = userAccountHistoryService.getOne(Condition.getQueryWrapper(userAccountHistory));
		return R.data(detail);
	}

	/**
	 * 分页 账户流水记录
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入userAccountHistory")
	public R<IPage<UserAccountHistory>> list(UserAccountHistory userAccountHistory, Query query) {
		//判断为学生 则进行学生id查询
		if(Func.equals(SecureUtil.getUserRole(),"student")){
			Integer userId = SecureUtil.getUserId();
			userAccountHistory.setUserId(userId);
		}
		IPage<UserAccountHistory> pages = userAccountHistoryService.page(Condition.getPage(query), Condition.getQueryWrapper(userAccountHistory).lambda().orderByDesc(UserAccountHistory::getCreateTime));
		return R.data(pages);
	}

	/**
	 * 自定义分页 账户流水记录
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入userAccountHistory")
	public R<IPage<UserAccountHistoryVO>> page(UserAccountHistoryVO userAccountHistory, Query query) {
		IPage<UserAccountHistoryVO> pages = userAccountHistoryService.selectUserAccountHistoryPage(Condition.getPage(query), userAccountHistory);
		return R.data(pages);
	}

	/**
	 * 新增 账户流水记录
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入userAccountHistory")
	public R save(@Valid @RequestBody UserAccountHistory userAccountHistory) {
		return R.status(userAccountHistoryService.save(userAccountHistory));
	}

	/**
	 * 修改 账户流水记录
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入userAccountHistory")
	public R update(@Valid @RequestBody UserAccountHistory userAccountHistory) {
		return R.status(userAccountHistoryService.updateById(userAccountHistory));
	}

	/**
	 * 新增或修改 账户流水记录
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入userAccountHistory")
	public R submit(@Valid @RequestBody UserAccountHistory userAccountHistory) {
		return R.status(userAccountHistoryService.saveOrUpdate(userAccountHistory));
	}


	/**
	 * 删除 账户流水记录
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(userAccountHistoryService.deleteLogic(Func.toIntList(ids)));
	}

	/**
	 * 导出用户订单
	 */
	@SneakyThrows
	@GetMapping("export-userAccountHistory")
	@ApiOperationSupport(order = 13)
	@ApiOperation(value = "导出用户账户记录数据", notes = "传入UserAccountHistory")
	public void exportUserAccountHistory(@ApiIgnore @RequestParam Map<String, Object> UserAccountHistory, HttpServletResponse response) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String date=simpleDateFormat.format(new Date());
		UserAccountHistory.remove("crazy-auth");
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding(Charsets.UTF_8.name());
		String fileName = URLEncoder.encode("用户交易数据导出"+date, Charsets.UTF_8.name());
		response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
		//stirng 转 data
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if (Func.isNotEmpty(UserAccountHistory.get("createTime"))) {
			Date createTime = format.parse(String.valueOf(UserAccountHistory.get("createTime")));
			//修改value值
			if (UserAccountHistory.containsKey("createTime")) {
				UserAccountHistory.put("createTime", createTime);
			}
		}
		userAccountHistoryService.exportUserAccountHistory(response , UserAccountHistory);

	}
}
