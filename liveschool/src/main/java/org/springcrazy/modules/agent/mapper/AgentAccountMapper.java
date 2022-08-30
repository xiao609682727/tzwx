
package org.springcrazy.modules.agent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.agent.entity.AgentAccount;
import org.springcrazy.modules.agent.vo.AgentAccountVO;

import java.util.List;

/**
 * 代理商账户信息账户信息，记录用户的代理商账户金额 Mapper 接口
 *
 * @author TongZhou
 * @since 2021-04-30
 */
public interface AgentAccountMapper extends BaseMapper<AgentAccount> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param agentAccount
	 * @return
	 */
	List<AgentAccountVO> selectAgentAccountPage(IPage page, AgentAccountVO agentAccount);

}
