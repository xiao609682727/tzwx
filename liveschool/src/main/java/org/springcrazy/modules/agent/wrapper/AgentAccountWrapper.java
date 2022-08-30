
package org.springcrazy.modules.agent.wrapper;

import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.modules.agent.entity.AgentAccount;
import org.springcrazy.modules.agent.vo.AgentAccountVO;

/**
 * 代理商账户信息账户信息，记录用户的代理商账户金额包装类,返回视图层所需的字段
 *
 * @author TongZhou
 * @since 2021-04-30
 */
public class AgentAccountWrapper extends BaseEntityWrapper<AgentAccount, AgentAccountVO>  {

    public static AgentAccountWrapper build() {
        return new AgentAccountWrapper();
    }

	@Override
	public AgentAccountVO entityVO(AgentAccount agentAccount) {
		AgentAccountVO agentAccountVO = BeanUtil.copy(agentAccount, AgentAccountVO.class);

		return agentAccountVO;
	}

}
