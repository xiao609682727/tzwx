
package org.springcrazy.modules.agent.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.agent.entity.AgentUserAccountHistory;

/**
 * 代理商账户流水记录数据传输对象实体类
 *
 * @author TongZhou
 * @since 2021-04-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AgentUserAccountHistoryDTO extends AgentUserAccountHistory {
	private static final long serialVersionUID = 1L;

}
