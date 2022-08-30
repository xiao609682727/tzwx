
package org.springcrazy.modules.partyLogin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springcrazy.core.boot.ctrl.CrazyController;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.mp.support.Query;
import org.springcrazy.core.secure.utils.SecureUtil;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.partyLogin.entity.UserProfile;
import org.springcrazy.modules.partyLogin.service.IUserProfileService;
import org.springcrazy.modules.partyLogin.vo.UserProfileVO;
import org.springcrazy.modules.partyLogin.wrapper.UserProfileWrapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户第三方绑定 控制器
 *
 * @author TongZhou
 * @since 2021-03-15
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/partyLogin/userprofile")
@Api(value = "用户第三方绑定", tags = "用户第三方绑定接口")
public class UserProfileController extends CrazyController {

	private IUserProfileService userProfileService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入userProfile")
	public R<UserProfileVO> detail(UserProfile userProfile) {
		UserProfile detail = userProfileService.getOne(Condition.getQueryWrapper(userProfile));
		return R.data(UserProfileWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 用户第三方绑定
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入userProfile")
	public R<IPage<UserProfileVO>> list(UserProfile userProfile, Query query) {
		IPage<UserProfile> pages = userProfileService.page(Condition.getPage(query), Condition.getQueryWrapper(userProfile));
		return R.data(UserProfileWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 用户第三方绑定
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入userProfile")
	public R<IPage<UserProfileVO>> page(UserProfileVO userProfile, Query query) {
		IPage<UserProfileVO> pages = userProfileService.selectUserProfilePage(Condition.getPage(query), userProfile);
		return R.data(pages);
	}

	/**
	 * 新增 用户第三方绑定
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入userProfile")
	public R save(@Valid @RequestBody UserProfile userProfile) {
		return R.status(userProfileService.save(userProfile));
	}

	/**
	 * 修改 用户第三方绑定
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入userProfile")
	public R update(@Valid @RequestBody UserProfile userProfile) {
		return R.status(userProfileService.updateById(userProfile));
	}

	/**
	 * 新增或修改 用户第三方绑定
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入userProfile")
	public R submit(@Valid @RequestBody UserProfile userProfile) {
		Integer userId = SecureUtil.getUserId();
		userProfile.setUserid(userId);
		userProfile.setProfiledate(LocalDateTime.now());
		return R.status(userProfileService.saveOrUpdate(userProfile));
	}


	/**
	 * 删除 用户第三方绑定
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@Valid @RequestBody UserProfile userProfile) {
		String idsString= "";
		List<UserProfile> userProfileList =  new ArrayList<>();
		if(("wxPC").equals(userProfile.getProfiletype())){
			 userProfileList = userProfileService.getStudetWxLise(userProfile.getUserid());
		}else if(("qqPC").equals(userProfile.getProfiletype())){
			 userProfileList = userProfileService.getStudetQqLise(userProfile.getUserid());
		}else {
			 userProfileList = userProfileService.getStudetWeiboLise(userProfile.getUserid());
		}
		int i=0;
		for(UserProfile userProfile1:userProfileList){
			if(i==0){
				idsString+=userProfile1.getId();
			}else {
				idsString+=","+userProfile1.getId();
			}
			i+=1;
		}
		log.info("用户绑定信息id："+idsString);
		return R.status(userProfileService.removeByIds(Func.toIntList(idsString)));
	}


}
