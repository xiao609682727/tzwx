
package org.springcrazy.modules.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.user.entity.UserAccount;

/**
 * 账户信息账户信息，记录用户的账户金额视图实体类
 *
 * @author TongZhou
 * @since 2020-05-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UserAccountVO对象", description = "账户信息账户信息，记录用户的账户金额")
public class UserAccountVO extends UserAccount {
	private static final long serialVersionUID = 1L;

	/**
	 * 手机号
	 */
	@ApiModelProperty(value = "手机号")
	protected String mobile;
	/**
	 * 邮箱号
	 */
	@ApiModelProperty(value = "邮箱号")
	protected String email;

	/**
	 * 账户名
	 */
	@ApiModelProperty(value = "账户名")
	protected String userName;

	/**
	 * 登录账号
	 */
	@ApiModelProperty(value = "登录账号")
	protected String loginAccount;
}
