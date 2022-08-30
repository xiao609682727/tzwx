
package org.springcrazy.modules.agent.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.agent.entity.AgentUserAccountHistory;

/**
 * 代理商账户流水记录视图实体类
 *
 * @author TongZhou
 * @since 2021-04-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "AgentUserAccountHistoryVO对象", description = "代理商账户流水记录")
public class AgentUserAccountHistoryVO extends AgentUserAccountHistory {
	private static final long serialVersionUID = 1L;
	/**
	 * 代理商手机号
	 */
	@ApiModelProperty(value = "手机号")
	protected String phone;
	/**
	 * 代理商真实姓名
	 */
	@ApiModelProperty(value = "真实姓名")
	protected String realName;

	/**
	 * 代理商昵称
	 */
	@ApiModelProperty(value = "昵称")
	protected String name;

	/**
	 * 代理商账号
	 */
	@ApiModelProperty(value = "账号")
	protected String account;

}
