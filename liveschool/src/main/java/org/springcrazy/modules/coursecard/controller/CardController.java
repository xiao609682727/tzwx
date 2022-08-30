
package org.springcrazy.modules.coursecard.controller;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import org.springcrazy.modules.coursecard.entity.Card;
import org.springcrazy.modules.coursecard.entity.CardCode;
import org.springcrazy.modules.coursecard.entity.CardCourse;
import org.springcrazy.modules.coursecard.service.ICardCodeService;
import org.springcrazy.modules.coursecard.service.ICardService;
import org.springcrazy.modules.coursecard.vo.CardCourseVO;
import org.springcrazy.modules.coursecard.vo.CardVO;
import org.springcrazy.modules.coursecard.wrapper.CardWrapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 课程卡主表 控制器
 *
 * @author TongZhou
 * @since 2021-03-31
 */
@RestController
@AllArgsConstructor
@RequestMapping("/edu/card")
@Api(value = "课程卡主表", tags = "课程卡主表接口")
public class CardController extends CrazyController {

	private ICardService cardService;

	private ICardCodeService cardCodeService;
	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入card")
	public R<CardVO> detail(Card card) {
		Card detail = cardService.getOne(Condition.getQueryWrapper(card));
		return R.data(CardWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 课程卡主表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入card")
	public R<IPage<CardVO>> list(Card card, Query query) {
		QueryWrapper<Card> queryWrapper = Condition.getQueryWrapper(card);
		queryWrapper.lambda().orderByDesc(Card::getId);
		IPage<Card> pages = cardService.page(Condition.getPage(query), queryWrapper);
		return R.data(CardWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 课程卡主表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入card")
	public R<IPage<CardVO>> page(CardVO card, Query query) {
		IPage<CardVO> pages = cardService.selectCardPage(Condition.getPage(query), card);
		return R.data(pages);
	}

	/**
	 * 新增 课程卡主表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入card")
	public R save(@Valid @RequestBody Card card) {
		card.setCreateTime(LocalDateTime.now());
		return R.status(cardService.save(card));
	}

	/**
	 * 修改 课程卡主表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入card")
	public R update(@Valid @RequestBody Card card, @RequestParam("courseIds") String courseIds) {

		cardService.updateCard(card,courseIds);
		return R.status(true);
	}

	/**
	 * 新增或修改 课程卡主表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入card")
	public R submit(@Valid @RequestBody Card card, @RequestParam("courseIds") String courseIds) {

		if(Func.isEmpty(courseIds)){
			return R.fail("courseIds不能为空");
		}

		cardService.addCard(card,courseIds);
		return R.status(true);
	}


	/**
	 * 删除 课程卡主表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(cardService.removeByIds(Func.toIntList(ids)));
	}
	/**
	 * 课程卡关联的课程数据
	 */
	@GetMapping("/courseList")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "课程卡关联的课程数据", notes = "传入cardId")
	public R<List<CardCourseVO>> courseList(CardCourse cardCourse) {
		List<CardCourseVO> cardCourseVOList =  cardService.queryCourseCardCourseList(cardCourse);
		return R.data(cardCourseVOList);
	}
	/**
	 * 课程卡激活
	 */
	@GetMapping("/courseCardActivation")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "课程卡激活方法", notes = "传入cardCode和cardCodePassword")
	public R<String> courseCardActivation(CardCode cardCode) {
		if(Func.isEmpty(cardCode.getCardCode())){
			return R.data("cardCodeIsNull","cardCodeIsNull");
		}
		if(Func.isEmpty(cardCode.getCardCodePassword())){
			return R.data("cardCodePasswordIsNull","cardCodePasswordIsNull");
		}
		cardCode.setUserId(SecureUtil.getUserId());
		cardCode.setCardCode(cardCode.getCardCode().trim());
		cardCode.setCardCodePassword(cardCode.getCardCodePassword().trim());
		String msg = cardService.queryCourseCardCourseActivation(cardCode);
		return R.data(msg,msg);
	}
	/**
	 * 课程卡主表作废
	 */
	@PostMapping("/updateInvalid")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入card")
	public R updateInvalid(@Valid @RequestBody Card card) {
		Card card1 = cardService.getById(card.getId());
		//设置课程卡主卡作废
		card1.setStatus(2);
		cardService.updateById(card1);
		UpdateWrapper<CardCode> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("card_id",card.getId()).set("status", "invalid");

		cardCodeService.update(null,updateWrapper);
		return R.status(true);
	}


	/**
	 * 课程卡主表作废
	 */
	@PostMapping("/addCourseCardNums")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入card")
	public R addCourseCardNums(@Valid @RequestBody Card card) {
		Integer courseCardNums=card.getNum();
		Card card1 = cardService.getById(card.getId());
		card1.setNum(card1.getNum()+courseCardNums);
		cardService.updateById(card1);
		List<CardCode> list = new ArrayList<>();
		for(int i=0;i<courseCardNums;i++){
			CardCode cardCode = new CardCode();
			cardCode.setCardId(card.getId());
			String ccode="1"+getFixString(Integer.valueOf(card.getId()+"").intValue(),3)+getFixString(i,3)+ RandomUtil.randomNumbers(4);
			cardCode.setCardCode(ccode);
			cardCode.setCardCodePassword(RandomUtil.randomNumbers(6));// 生成10位密码
			cardCode.setCreateTime(LocalDateTime.now());
			cardCode.setStatus("init");
			cardCode.setUserId(0);
			list.add(cardCode);
		}
		cardCodeService.saveBatch(list);
		return R.status(true);
	}
	/**
	 * 课程卡,单卡表作废
	 */
	@PostMapping("/updateInvalidCourseCardCode")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入card")
	public R updateInvalidCourseCardCode(@Valid @RequestBody CardCode cardCode) {
		UpdateWrapper<CardCode> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("id",cardCode.getId()).set("status", "invalid");

		cardCodeService.update(null,updateWrapper);
		return R.status(true);
	}

	/**
	 * 长度补冲，前面加0
	 *
	 * @param num
	 * @param len
	 * @return String
	 */
	static String getFixString(int num, int len) {
		String tp = "" + num;
		if (len == 0) {
			return tp;
		}
		if (tp.length() == len) {
			return tp;
		}
		if (tp.length() > len) {
			return tp.substring(0, len);
		}
		for (int i = 0; i <= len / 4 + 1; i++) {
			tp = "00000" + tp;
		}
		return tp.substring(tp.length() - len);
	}
}
