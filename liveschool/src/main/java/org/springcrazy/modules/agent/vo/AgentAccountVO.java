
package org.springcrazy.modules.agent.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.agent.entity.AgentAccount;

/**
 * 代理商账户信息账户信息，记录用户的代理商账户金额视图实体类
 *
 * @author TongZhou
 * @since 2021-04-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "AgentAccountVO对象", description = "代理商账户信息账户信息，记录用户的代理商账户金额")
public class AgentAccountVO extends AgentAccount {
	private static final long serialVersionUID = 1L;
	/**
	 * 手机号
	 */
	@ApiModelProperty(value = "手机号")
	protected String phone;
	/**
	 * 真实姓名
	 */
	@ApiModelProperty(value = "真实姓名")
	protected String realName;

	/**
	 * 昵称
	 */
	@ApiModelProperty(value = "昵称")
	protected String name;

	/**
	 * 账号
	 */
	@ApiModelProperty(value = "账号")
	protected String account;
}
