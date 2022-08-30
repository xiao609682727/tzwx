
package org.springcrazy.modules.agent.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.agent.entity.AgentAccount;
import org.springcrazy.modules.agent.vo.AgentAccountVO;
import org.springcrazy.modules.edu.entity.Orders;

import java.math.BigDecimal;

/**
 * 代理商账户信息账户信息，记录用户的代理商账户金额 服务类
 *
 * @author TongZhou
 * @since 2021-04-30
 */
public interface IAgentAccountService extends IService<AgentAccount> {

	/**
	 * 自定义分页
	 */
	IPage<AgentAccountVO> selectAgentAccountPage(IPage<AgentAccountVO> page, AgentAccountVO agentAccount);
	/**
	 * 代理商账户充值
	 * Orders非必要属性，如果是填写，则说明是代理商开课，需要保存订单号和订单id
	 */
	void addMoney(Integer agentUserId, BigDecimal money, String type, String bizType, Orders order);
}
