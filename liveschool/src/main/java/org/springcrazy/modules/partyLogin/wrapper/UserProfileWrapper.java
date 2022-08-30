
package org.springcrazy.modules.partyLogin.wrapper;

import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.modules.partyLogin.entity.UserProfile;
import org.springcrazy.modules.partyLogin.vo.UserProfileVO;

/**
 * 用户第三方绑定包装类,返回视图层所需的字段
 *
 * @author TongZhou
 * @since 2021-03-15
 */
public class UserProfileWrapper extends BaseEntityWrapper<UserProfile, UserProfileVO>  {

    public static UserProfileWrapper build() {
        return new UserProfileWrapper();
    }

	@Override
	public UserProfileVO entityVO(UserProfile userProfile) {
		UserProfileVO userProfileVO = BeanUtil.copy(userProfile, UserProfileVO.class);

		return userProfileVO;
	}

}
