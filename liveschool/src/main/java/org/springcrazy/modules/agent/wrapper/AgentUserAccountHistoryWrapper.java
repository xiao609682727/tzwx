
package org.springcrazy.modules.agent.wrapper;

import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.modules.agent.entity.AgentUserAccountHistory;
import org.springcrazy.modules.agent.vo.AgentUserAccountHistoryVO;

/**
 * 代理商账户流水记录包装类,返回视图层所需的字段
 *
 * @author TongZhou
 * @since 2021-04-30
 */
public class AgentUserAccountHistoryWrapper extends BaseEntityWrapper<AgentUserAccountHistory, AgentUserAccountHistoryVO>  {

    public static AgentUserAccountHistoryWrapper build() {
        return new AgentUserAccountHistoryWrapper();
    }

	@Override
	public AgentUserAccountHistoryVO entityVO(AgentUserAccountHistory agentUserAccountHistory) {
		AgentUserAccountHistoryVO agentUserAccountHistoryVO = BeanUtil.copy(agentUserAccountHistory, AgentUserAccountHistoryVO.class);

		return agentUserAccountHistoryVO;
	}

}
