
package org.springcrazy.modules.partyLogin.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.partyLogin.entity.UserProfile;

/**
 * 用户第三方绑定视图实体类
 *
 * @author TongZhou
 * @since 2021-03-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UserProfileVO对象", description = "用户第三方绑定")
public class UserProfileVO extends UserProfile {
	private static final long serialVersionUID = 1L;

}
