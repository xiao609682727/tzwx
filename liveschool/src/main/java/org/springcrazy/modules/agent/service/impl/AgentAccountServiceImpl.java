
package org.springcrazy.modules.agent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.common.tool.BigDecimalUtils;
import org.springcrazy.core.log.exception.ServiceException;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.agent.entity.AgentAccount;
import org.springcrazy.modules.agent.entity.AgentUserAccountHistory;
import org.springcrazy.modules.agent.mapper.AgentAccountMapper;
import org.springcrazy.modules.agent.service.IAgentAccountService;
import org.springcrazy.modules.agent.service.IAgentUserAccountHistoryService;
import org.springcrazy.modules.agent.vo.AgentAccountVO;
import org.springcrazy.modules.edu.entity.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 代理商账户信息账户信息，记录用户的代理商账户金额 服务实现类
 *
 * @author TongZhou
 * @since 2021-04-30
 */
@Service
public class AgentAccountServiceImpl extends ServiceImpl<AgentAccountMapper, AgentAccount> implements IAgentAccountService {
	@Autowired
	private IAgentUserAccountHistoryService agentUserAccountHistoryService;
	@Override
	public IPage<AgentAccountVO> selectAgentAccountPage(IPage<AgentAccountVO> page, AgentAccountVO agentAccount) {
		return page.setRecords(baseMapper.selectAgentAccountPage(page, agentAccount));
	}

	/**
	 * 代理商充值，扣款，消费方法
	 */
	@Transactional(rollbackFor = {RuntimeException.class, Error.class})
	public void addMoney(Integer agentUserId, BigDecimal money, String type, String bizType, Orders order){
		String desc = "后台充值";
		if(Func.equals(type,"1")){
			desc = "后台充值";
		}
		if(Func.equals(type,"2")){
			desc = "后台扣款";
		}
		if(Func.equals(type,"3")){
			desc = "开课消费";
		}
		AgentAccount agentAccount = baseMapper.selectOne(new QueryWrapper<AgentAccount>().eq("agent_user_id",agentUserId));
		AgentUserAccountHistory agentUserAccountHistory = new AgentUserAccountHistory();
		if(Func.isNull(agentAccount)){
			throw new ServiceException("代理商账户不存在，请联系管理员");
		}
		//充值
		if(Func.equals("1",type)){
			agentAccount.setBalance(agentAccount.getBalance().add(money));
			agentUserAccountHistory.setActHistoryType(AgentUserAccountHistory.ACE_HISTORYTYPE_ADDMONEY);
			agentUserAccountHistory.setDescription(desc + BigDecimalUtils.toString(money));
		}
		//扣款
		if(Func.equals("2",type)){
			BigDecimal bmoney = agentAccount.getBalance().subtract(money);
			int i = bmoney.compareTo(BigDecimal.ZERO);
			//判断如果储值不够返回错误
			if(i==-1){
				throw new ServiceException("代理商余额不足");
			}
			agentAccount.setBalance(bmoney);
			agentUserAccountHistory.setActHistoryType(AgentUserAccountHistory.ACE_HISTORYTYPE_SUBTRACTMONEY);
			agentUserAccountHistory.setDescription(desc + BigDecimalUtils.toString(money));
		}
		//消费
		if(Func.equals("3",type)){
			BigDecimal bmoney = agentAccount.getBalance().subtract(money);
			int i = bmoney.compareTo(BigDecimal.ZERO);
			//判断如果储值不够返回错误
			if(i==-1){
				throw new ServiceException("余额不足");
			}
			agentAccount.setBalance(bmoney);
			agentUserAccountHistory.setActHistoryType(AgentUserAccountHistory.ACE_HISTORYTYPE_CONSUMPTION);
			agentUserAccountHistory.setDescription(desc + BigDecimalUtils.toString(money));
			//如果为消费类型，设置订单id和订单号
			if(Func.notNull(order)){
				agentUserAccountHistory.setOrderNo(order.getOrderNo());
				agentUserAccountHistory.setOrderId(order.getId());
			}

		}

		//更新更新时间
		agentAccount.setUpdateTime(LocalDateTime.now());
		baseMapper.updateById(agentAccount);
		agentUserAccountHistory.setAgentUserId(agentUserId);
//		agentUserAccountHistory.setBackAmount(agentAccount.getBackAmount());
		agentUserAccountHistory.setBalance(agentAccount.getBalance());
//		agentUserAccountHistory.setCashAmount(agentAccount.getCashAmount());
		agentUserAccountHistory.setBizType(bizType);
		agentUserAccountHistory.setTrxAmount(money);
		agentUserAccountHistory.setCreateTime(LocalDateTime.now());
		agentUserAccountHistoryService.save(agentUserAccountHistory);
//		UserAccount userAccount = baseMapper.selectOne(new QueryWrapper<UserAccount>().eq("user_id",userId));
//		UserAccountHistory userAccountHistory = new UserAccountHistory();
//		if(Func.equals("1",type)){
//			userAccount.setBalance(userAccount.getBalance().add(money));
//			userAccountHistory.setActHistoryType(UserAccountHistory.HISTORYTYPE_ADDMONEY);
//
//			userAccountHistory.setDescription(desc + BigDecimalUtils.toString(money));
//		}
//		if(Func.equals("2",type)){
//			BigDecimal bmoney = userAccount.getBalance().subtract(money);
//			int i = bmoney.compareTo(BigDecimal.ZERO);
//			//判断如果储值不够返回错误
//			if(i==-1){
//				throw new ServiceException("余额不足");
//			}
//			userAccount.setBalance(bmoney);
//			userAccountHistory.setActHistoryType(UserAccountHistory.HISTORYTYPE_SUBTRACTMONEY);
//			userAccountHistory.setDescription(desc + BigDecimalUtils.toString(money));
//		}
//		baseMapper.updateById(userAccount);
//		userAccountHistory.setUserId(userId);
//		userAccountHistory.setBackAmount(userAccount.getBackAmount());
//		userAccountHistory.setBalance(userAccount.getBalance());
//		userAccountHistory.setCashAmount(userAccount.getCashAmount());
//		userAccountHistory.setBizType(bizType);
//		userAccountHistory.setTrxAmount(money);
//		userAccountHistoryService.save(userAccountHistory);
	}
}
