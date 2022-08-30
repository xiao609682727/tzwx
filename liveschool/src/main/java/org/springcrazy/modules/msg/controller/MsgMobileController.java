
package org.springcrazy.modules.msg.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import org.springcrazy.modules.msg.entity.MsgMobile;
import org.springcrazy.modules.msg.service.IMsgMobileService;
import org.springcrazy.modules.msg.vo.MsgMobileVO;
import org.springcrazy.modules.msg.wrapper.MsgMobileWrapper;
import org.springcrazy.modules.system.entity.UserInfo;
import org.springcrazy.modules.user.service.IStudentService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 手机短信发送记录 控制器
 *
 * @author TongZhou
 * @since 2020-05-18
 */
@RestController
@AllArgsConstructor
@RequestMapping("/msg/msgmobile")
@Api(value = "手机短信发送记录", tags = "手机短信发送记录接口")
public class MsgMobileController extends CrazyController {

	private IMsgMobileService msgMobileService;

	private IStudentService studentService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入msgMobile")
	public R<MsgMobileVO> detail(MsgMobile msgMobile) {
		MsgMobile detail = msgMobileService.getOne(Condition.getQueryWrapper(msgMobile));
		return R.data(MsgMobileWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 手机短信发送记录
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入msgMobile")
	public R<IPage<MsgMobileVO>> list(MsgMobile msgMobile, Query query) {
		String content = msgMobile.getContent();
		msgMobile.setContent(null);
		QueryWrapper<MsgMobile> queryWrapper = Condition.getQueryWrapper(msgMobile);
		queryWrapper.lambda().like(Func.isNotBlank(content),MsgMobile::getContent,content).orderByDesc(MsgMobile::getCreateTime);
		IPage<MsgMobile> pages = msgMobileService.page(Condition.getPage(query), queryWrapper);
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
		return R.data(MsgMobileWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 手机短信发送记录
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入msgMobile")
	public R<IPage<MsgMobileVO>> page(MsgMobileVO msgMobile, Query query) {
		IPage<MsgMobileVO> pages = msgMobileService.selectMsgMobilePage(Condition.getPage(query), msgMobile);
		return R.data(pages);
	}

	/**
	 * 重新发送短信
	 */
	@GetMapping("/sendMobile")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "重新发送短信", notes = "重新发送短信")
	public R sendEmail(String id) {
		MsgMobile msgMobile = msgMobileService.getById(id);
		msgMobileService.sendMsg(msgMobile,msgMobile.getContent());
		return R.data(true);
	}

	/**
	 * 新增 手机短信发送记录
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入msgMobile")
	public R save(@Valid @RequestBody MsgMobile msgMobile) {
		return R.status(msgMobileService.save(msgMobile));
	}

	/**
	 * 修改 手机短信发送记录
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入msgMobile")
	public R update(@Valid @RequestBody MsgMobile msgMobile) {
		return R.status(msgMobileService.updateById(msgMobile));
	}

	/**
	 * 新增或修改 手机短信发送记录
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入msgMobile")
	public R submit(@Valid @RequestBody MsgMobile msgMobile) {
		return R.status(msgMobileService.saveMsgMobile(msgMobile));
	}


	/**
	 * 删除 手机短信发送记录
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(msgMobileService.deleteLogic(Func.toIntList(ids)));
	}


}
