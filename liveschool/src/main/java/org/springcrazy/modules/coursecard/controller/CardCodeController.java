
package org.springcrazy.modules.coursecard.controller;

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
import org.springcrazy.modules.coursecard.entity.CardCode;
import org.springcrazy.modules.coursecard.service.ICardCodeService;
import org.springcrazy.modules.coursecard.vo.CardCodeVO;
import org.springcrazy.modules.coursecard.wrapper.CardCodeWrapper;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 课程卡编码表 控制器
 *
 * @author TongZhou
 * @since 2021-04-02
 */
@RestController
@AllArgsConstructor
@RequestMapping("/coursecard/cardcode")
@Api(value = "课程卡编码表", tags = "课程卡编码表接口")
public class CardCodeController extends CrazyController {

	private ICardCodeService cardCodeService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入cardCode")
	public R<CardCodeVO> detail(CardCode cardCode) {
		CardCode detail = cardCodeService.getOne(Condition.getQueryWrapper(cardCode));
		return R.data(CardCodeWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 课程卡编码表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入cardCode")
	public R<IPage<CardCodeVO>> list(CardCode cardCode, Query query) {
		IPage<CardCode> pages = cardCodeService.page(Condition.getPage(query), Condition.getQueryWrapper(cardCode));
		return R.data(CardCodeWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 课程卡编码表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入cardCode")
	public R<IPage<CardCodeVO>> page(CardCodeVO cardCode, Query query) {
		IPage<CardCodeVO> pages = cardCodeService.selectCardCodePage(Condition.getPage(query), cardCode);
		return R.data(pages);
	}

	/**
	 * 新增 课程卡编码表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入cardCode")
	public R save(@Valid @RequestBody CardCode cardCode) {
		return R.status(cardCodeService.save(cardCode));
	}

	/**
	 * 修改 课程卡编码表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入cardCode")
	public R update(@Valid @RequestBody CardCode cardCode) {
		return R.status(cardCodeService.updateById(cardCode));
	}

	/**
	 * 新增或修改 课程卡编码表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入cardCode")
	public R submit(@Valid @RequestBody CardCode cardCode) {
		return R.status(cardCodeService.saveOrUpdate(cardCode));
	}


	/**
	 * 删除 课程卡编码表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(cardCodeService.removeByIds(Func.toIntList(ids)));
	}

	/**
	 * 导出用户
	 */
	@SneakyThrows
	@GetMapping("export-cardCode")
	@ApiOperationSupport(order = 13)
	@ApiOperation(value = "导出用户", notes = "传入user")
	public void exportCardCode(CardCodeVO cardCode, HttpServletResponse response) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String date=simpleDateFormat.format(new Date());

		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding(Charsets.UTF_8.name());
		String fileName = URLEncoder.encode("用户数据导出"+date, Charsets.UTF_8.name());
		response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
		cardCodeService.exportCardCode(response , cardCode);

	}
}
