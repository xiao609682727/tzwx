
package org.springcrazy.modules.partyLogin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.partyLogin.entity.UserProfile;

/**
 * 用户第三方绑定数据传输对象实体类
 *
 * @author TongZhou
 * @since 2021-03-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserProfileDTO extends UserProfile {
	private static final long serialVersionUID = 1L;

}
