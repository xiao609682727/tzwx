
package org.springcrazy.modules.agent.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.agent.entity.AgentAccount;

/**
 * 代理商账户信息账户信息，记录用户的代理商账户金额数据传输对象实体类
 *
 * @author TongZhou
 * @since 2021-04-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AgentAccountDTO extends AgentAccount {
	private static final long serialVersionUID = 1L;

}
