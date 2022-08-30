
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
import org.springcrazy.modules.msg.entity.MsgSystem;
import org.springcrazy.modules.msg.service.IMsgSystemService;
import org.springcrazy.modules.msg.vo.MsgSystemVO;
import org.springcrazy.modules.msg.wrapper.MsgSystemWrapper;
import org.springcrazy.modules.system.entity.UserInfo;
import org.springcrazy.modules.user.service.IStudentService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 系统消息 控制器
 *
 * @author TongZhou
 * @since 2020-05-18
 */
@RestController
@AllArgsConstructor
@RequestMapping("/msg/msgsystem")
@Api(value = "系统消息", tags = "系统消息接口")
public class MsgSystemController extends CrazyController {

	private IMsgSystemService msgSystemService;

	private IStudentService studentService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入msgSystem")
	public R<MsgSystemVO> detail(MsgSystem msgSystem) {
		MsgSystem detail = msgSystemService.getOne(Condition.getQueryWrapper(msgSystem));
		return R.data(MsgSystemWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 系统消息
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入msgSystem")
	public R<IPage<MsgSystemVO>> list(MsgSystem msgSystem, Query query) {
		IPage<MsgSystem> pages = msgSystemService.page(Condition.getPage(query), Condition.getQueryWrapper(msgSystem).lambda().orderByDesc(MsgSystem::getCreateTime));
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
		return R.data(MsgSystemWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 系统消息
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入msgSystem")
	public R<IPage<MsgSystemVO>> page(MsgSystemVO msgSystem, Query query) {
		IPage<MsgSystemVO> pages = msgSystemService.selectMsgSystemPage(Condition.getPage(query), msgSystem);
		return R.data(pages);
	}

	/**
	 * 新增 系统消息
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入msgSystem")
	public R save(@Valid @RequestBody MsgSystem msgSystem) {
		return R.status(msgSystemService.save(msgSystem));
	}

	/**
	 * 修改 系统消息
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入msgSystem")
	public R update(@Valid @RequestBody MsgSystem msgSystem) {
		return R.status(msgSystemService.updateById(msgSystem));
	}

	/**
	 * 新增或修改 系统消息
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入msgSystem")
	public R submit(@Valid @RequestBody MsgSystem msgSystem) {
		return R.status(msgSystemService.saveMsgSystem(msgSystem));
	}


	/**
	 * 删除 系统消息
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(msgSystemService.deleteLogic(Func.toIntList(ids)));
	}


}
