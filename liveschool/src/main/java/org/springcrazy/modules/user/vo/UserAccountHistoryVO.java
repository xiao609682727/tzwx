
package org.springcrazy.modules.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.user.entity.UserAccountHistory;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 账户流水记录视图实体类
 *
 * @author TongZhou
 * @since 2020-05-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UserAccountHistoryVO对象", description = "账户流水记录")
public class UserAccountHistoryVO extends UserAccountHistory {
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

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date createTime;

}
