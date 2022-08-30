
package org.springcrazy.modules.partyLogin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.util.CollectionUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.modules.partyLogin.entity.UserProfile;
import org.springcrazy.modules.partyLogin.mapper.UserProfileMapper;
import org.springcrazy.modules.partyLogin.service.IUserProfileService;
import org.springcrazy.modules.partyLogin.vo.UserProfileVO;
import org.springcrazy.modules.user.entity.Student;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户第三方绑定 服务实现类
 *
 * @author TongZhou
 * @since 2021-03-15
 */
@Service
public class UserProfileServiceImpl extends ServiceImpl<UserProfileMapper, UserProfile> implements IUserProfileService {

	@Override
	public IPage<UserProfileVO> selectUserProfilePage(IPage<UserProfileVO> page, UserProfileVO userProfile) {
		return page.setRecords(baseMapper.selectUserProfilePage(page, userProfile));
	}

	@Override
	public Student getStudetByOpintId(Student student, String type, String openid, String unionid) {
		Student student1 = new Student();
		student1.setId(1);
		UserProfile userProfile =  new UserProfile();
		if(type.equals("qqPC")||type.equals("qqApp")){
			userProfile.setProfiletype("qqPC");
			userProfile.setUnionid(unionid);
			UserProfile userProfileVO =baseMapper.getStudetByOpintId(userProfile);
			if(ObjectUtil.isNotNull(userProfileVO)){
				if(ObjectUtil.isNotNull(userProfileVO.getUserid())){
					student1.setId(userProfileVO.getUserid());
					return student1;
				}
			}
			UserProfile userProfile1 =  new UserProfile();
			userProfile1.setProfiletype("qqPC");
			userProfile1.setOpenid(openid);
			UserProfile userProfileVO1 =baseMapper.getStudetByOpintId(userProfile1);
			if(ObjectUtil.isNotNull(userProfileVO1)){
				if(ObjectUtil.isNotNull(userProfileVO1.getUserid())) {
					student1.setId(userProfileVO1.getUserid());
					return student1;
				}
			}
			userProfile.setProfiletype("qqApp");
			UserProfile userProfileVOApp =baseMapper.getStudetByOpintId(userProfile);
			if(ObjectUtil.isNotNull(userProfileVOApp)){
				if(ObjectUtil.isNotNull(userProfileVOApp.getUserid())){
					student1.setId(userProfileVOApp.getUserid());
					return student1;
				}
			}
			UserProfile userProfile1App =  new UserProfile();
			userProfile1App.setProfiletype("qqApp");
			userProfile1App.setOpenid(openid);
			UserProfile userProfile1App1 =baseMapper.getStudetByOpintId(userProfile1App);
			if(ObjectUtil.isNotNull(userProfile1App1)){
				if(ObjectUtil.isNotNull(userProfile1App1.getUserid())) {
					student1.setId(userProfile1App1.getUserid());
					return student1;
				}
			}
		}
		if(type.equals("wxPC")||type.equals("wxH5")||type.equals("wxApp")){
			userProfile.setProfiletype("wxPC");
			userProfile.setUnionid(unionid);
			UserProfile userProfileVO =baseMapper.getStudetByOpintId(userProfile);
			if(ObjectUtil.isNotNull(userProfileVO)){
				if(ObjectUtil.isNotNull(userProfileVO.getUserid())){
					student1.setId(userProfileVO.getUserid());
					return student1;
				}
			}
			UserProfile userProfile1 =  new UserProfile();
			userProfile1.setProfiletype("wxPC");
			userProfile1.setOpenid(openid);
			UserProfile userProfileVO1 =baseMapper.getStudetByOpintId(userProfile1);
			if(ObjectUtil.isNotNull(userProfileVO1)){
				if(ObjectUtil.isNotNull(userProfileVO1.getUserid())) {
					student1.setId(userProfileVO1.getUserid());
					return student1;
				}
			}
			userProfile.setProfiletype("wxApp");
			UserProfile userProfileVOApp =baseMapper.getStudetByOpintId(userProfile);
			if(ObjectUtil.isNotNull(userProfileVOApp)){
				if(ObjectUtil.isNotNull(userProfileVOApp.getUserid())){
					student1.setId(userProfileVOApp.getUserid());
					return student1;
				}
			}
			UserProfile userProfile1App =  new UserProfile();
			userProfile1App.setProfiletype("wxApp");
			userProfile1App.setOpenid(openid);
			UserProfile userProfile1App1 =baseMapper.getStudetByOpintId(userProfile1App);
			if(ObjectUtil.isNotNull(userProfile1App1)){
				if(ObjectUtil.isNotNull(userProfile1App1.getUserid())) {
					student1.setId(userProfile1App1.getUserid());
					return student1;
				}
			}
			userProfile.setProfiletype("wxH5");
			UserProfile userProfileVOH5 =baseMapper.getStudetByOpintId(userProfile);
			if(ObjectUtil.isNotNull(userProfileVOH5)){
				if(ObjectUtil.isNotNull(userProfileVOH5.getUserid())){
					student1.setId(userProfileVOH5.getUserid());
					return student1;
				}
			}
			UserProfile userProfile1WxApp =  new UserProfile();
			userProfile1WxApp.setProfiletype("wxH5");
			userProfile1WxApp.setOpenid(openid);
			UserProfile userProfile1Wx =baseMapper.getStudetByOpintId(userProfile1WxApp);
			if(ObjectUtil.isNotNull(userProfile1Wx)){
				if(ObjectUtil.isNotNull(userProfile1Wx.getUserid())) {
					student1.setId(userProfile1Wx.getUserid());
					return student1;
				}
			}
		}

		if(type.equals("weibo")){
			userProfile.setProfiletype("weibo");
			UserProfile userProfileVOApp =baseMapper.getStudetByOpintId(userProfile);
			if(ObjectUtil.isNotNull(userProfileVOApp)){
				if(ObjectUtil.isNotNull(userProfileVOApp.getUserid())){
					student1.setId(userProfileVOApp.getUserid());
					return student1;
				}
			}
			UserProfile userProfile1App =  new UserProfile();
			userProfile1App.setProfiletype("weibo");
			userProfile1App.setOpenid(openid);
			UserProfile userProfile1App1 =baseMapper.getStudetByOpintId(userProfile1App);
			if(ObjectUtil.isNotNull(userProfile1App1)){
				if(ObjectUtil.isNotNull(userProfile1App1.getUserid())) {
					student1.setId(userProfile1App1.getUserid());
					return student1;
				}
			}
		}
		/*if(type.equals("iosApp")){
			userProfile.setProfiletype("iosApp");
			UserProfile userProfileVOApp =baseMapper.getStudetByOpintId(userProfile);
			if(ObjectUtil.isNotNull(userProfileVOApp)){
				if(ObjectUtil.isNotNull(userProfileVOApp.getUserid())){
					student1.setId(userProfileVOApp.getUserid());
					return student1;
				}
			}
		}*/
		return student1;
	}

	@Override
	public List<UserProfile> getStudetThreeLise(Integer userId) {
		UserProfile userProfile = new UserProfile();
		userProfile.setUserid(userId);
		return baseMapper.getStudetThreeLise(userProfile);
	}

	@Override
	public void delectUserProfile(List<Integer> userId) {
		if(CollectionUtils.isEmpty(userId)){

		}
		for(Integer id:userId){
			UserProfile userProfile = new UserProfile();
			userProfile.setUserid(id);
			baseMapper.delectUserProfile(userProfile);
		}
	}

	@Override
	public List<UserProfile> getStudetWxLise(Integer userId) {
		UserProfile userProfile = new UserProfile();
		userProfile.setUserid(userId);
		return baseMapper.getStudetWxLise(userProfile);
	}

	@Override
	public List<UserProfile> getStudetQqLise(Integer userId) {
		UserProfile userProfile = new UserProfile();
		userProfile.setUserid(userId);
		return baseMapper.getStudetQqLise(userProfile);
	}

	@Override
	public List<UserProfile> getStudetWeiboLise(Integer userId) {
		UserProfile userProfile = new UserProfile();
		userProfile.setUserid(userId);
		return baseMapper.getStudetWeiboLise(userProfile);
	}

}
