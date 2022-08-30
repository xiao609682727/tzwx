
package org.springcrazy.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.common.tool.BigDecimalUtils;
import org.springcrazy.core.log.exception.ServiceException;
import org.springcrazy.core.tool.utils.DateUtil;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.user.entity.UserAccount;
import org.springcrazy.modules.user.entity.UserAccountHistory;
import org.springcrazy.modules.user.mapper.UserAccountMapper;
import org.springcrazy.modules.user.service.IUserAccountHistoryService;
import org.springcrazy.modules.user.service.IUserAccountService;
import org.springcrazy.modules.user.vo.UserAccountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账户信息账户信息，记录用户的账户金额 服务实现类
 *
 * @author TongZhou
 * @since 2020-05-07
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements IUserAccountService {

	@Autowired
	private IUserAccountHistoryService userAccountHistoryService;

	@Override
	public IPage<UserAccountVO> selectUserAccountPage(IPage<UserAccountVO> page, UserAccountVO userAccount) {
		return page.setRecords(baseMapper.selectUserAccountPage(page, userAccount));
	}

	@Override
	public void money(Integer userId, BigDecimal money,String type,String bizType,String desc,String orderNO) {
		UserAccount userAccount = baseMapper.selectOne(new QueryWrapper<UserAccount>().eq("user_id",userId));
		UserAccountHistory userAccountHistory = new UserAccountHistory();
		if(Func.equals("1",type)){
			userAccount.setBalance(userAccount.getBalance().add(money));
			userAccountHistory.setActHistoryType(UserAccountHistory.HISTORYTYPE_ADDMONEY);

			userAccountHistory.setDescription(desc + BigDecimalUtils.toString(money));
		}
		if(Func.equals("2",type)){
			BigDecimal bmoney = userAccount.getBalance().subtract(money);
			int i = bmoney.compareTo(BigDecimal.ZERO);
			//判断如果储值不够返回错误
			if(i==-1){
				throw new ServiceException("余额不足");
			}
			userAccount.setBalance(bmoney);
			userAccountHistory.setActHistoryType(UserAccountHistory.HISTORYTYPE_SUBTRACTMONEY);
			userAccountHistory.setDescription(desc + BigDecimalUtils.toString(money));
		}
		//更新更新时间
		userAccount.setUpdateTime(LocalDateTime.now());
		baseMapper.updateById(userAccount);
		userAccountHistory.setUserId(userId);
		userAccountHistory.setOrderNo(orderNO);
		userAccountHistory.setBackAmount(userAccount.getBackAmount());
		userAccountHistory.setBalance(userAccount.getBalance());
		userAccountHistory.setCashAmount(userAccount.getCashAmount());
		userAccountHistory.setBizType(bizType);
		userAccountHistory.setTrxAmount(money);
		userAccountHistory.setCreateTime(DateUtil.now());
		userAccountHistoryService.save(userAccountHistory);
	}

}
