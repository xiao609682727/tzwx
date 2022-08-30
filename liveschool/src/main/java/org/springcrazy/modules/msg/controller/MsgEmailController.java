
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
import org.springcrazy.modules.msg.entity.MsgEmail;
import org.springcrazy.modules.msg.service.IMsgEmailService;
import org.springcrazy.modules.msg.vo.MsgEmailVO;
import org.springcrazy.modules.msg.wrapper.MsgEmailWrapper;
import org.springcrazy.modules.system.entity.UserInfo;
import org.springcrazy.modules.user.service.IStudentService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 邮件发送记录 控制器
 *
 * @author TongZhou
 * @since 2020-05-18
 */
@RestController
@AllArgsConstructor
@RequestMapping("/msg/msgemail")
@Api(value = "邮件发送记录", tags = "邮件发送记录接口")
public class MsgEmailController extends CrazyController {

	private IMsgEmailService msgEmailService;

	private IStudentService studentService;
	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入msgEmail")
	public R<MsgEmailVO> detail(MsgEmail msgEmail) {
		MsgEmail detail = msgEmailService.getOne(Condition.getQueryWrapper(msgEmail));
		return R.data(MsgEmailWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 邮件发送记录
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入msgEmail")
	public R<IPage<MsgEmailVO>> list(MsgEmail msgEmail, Query query) {
		IPage<MsgEmail> pages = msgEmailService.page(Condition.getPage(query), Condition.getQueryWrapper(msgEmail).lambda().orderByDesc(MsgEmail::getCreateTime));
		for(int y=0;y<=pages.getRecords().size()-1;y++){
			if(!("").equals(pages.getRecords().get(y).getIds())&&pages.getRecords().get(y).getIds()!=null){
				String[] userId=pages.getRecords().get(y).getIds().split(",");
				String userName = "";
				for(int i=0;i<=userId.length-1;i++){
					UserInfo user =studentService.userInfos(Integer.valueOf(userId[i]));
					if(user!=null){
						userName+=user.getUser().getUserName()+",";
					}else {
						userName+="用户已删除,";
					}
				}
				pages.getRecords().get(y).setIds(userName);
			}else {
				pages.getRecords().get(y).setIds("全部用户");
			}
		}
		return R.data(MsgEmailWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 邮件发送记录
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入msgEmail")
	public R<IPage<MsgEmailVO>> page(MsgEmailVO msgEmail, Query query) {
		IPage<MsgEmailVO> pages = msgEmailService.selectMsgEmailPage(Condition.getPage(query), msgEmail);
		return R.data(pages);
	}

	/**
	 * 重新发送邮件
	 */
	@GetMapping("/sendEmail")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "重新发送邮件", notes = "重新发送邮件")
	public R sendEmail(String id) {
		MsgEmail msgEmail = msgEmailService.getById(id);
		msgEmailService.sendMsg(msgEmail);
		return R.data(true);
	}

	/**
	 * 新增 邮件发送记录
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入msgEmail")
	public R save(@Valid @RequestBody MsgEmail msgEmail) {
		return R.status(msgEmailService.save(msgEmail));
	}

	/**
	 * 修改 邮件发送记录
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入msgEmail")
	public R update(@Valid @RequestBody MsgEmail msgEmail) {
		return R.status(msgEmailService.updateById(msgEmail));
	}

	/**
	 * 新增或修改 邮件发送记录
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入msgEmail")
	public R submit(@Valid @RequestBody MsgEmail msgEmail) {
		return R.status(msgEmailService.saveMsgEmail(msgEmail));
	}


	/**
	 * 删除 邮件发送记录
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(msgEmailService.deleteLogic(Func.toIntList(ids)));
	}


}
