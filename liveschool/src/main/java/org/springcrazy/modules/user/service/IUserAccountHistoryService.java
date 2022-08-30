
package org.springcrazy.modules.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.core.mp.base.BaseService;
import org.springcrazy.modules.user.entity.UserAccountHistory;
import org.springcrazy.modules.user.vo.UserAccountHistoryVO;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 账户流水记录 服务类
 *
 * @author TongZhou
 * @since 2020-05-07
 */
public interface IUserAccountHistoryService extends BaseService<UserAccountHistory> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param userAccountHistory
	 * @return
	 */
	IPage<UserAccountHistoryVO> selectUserAccountHistoryPage(IPage<UserAccountHistoryVO> page, UserAccountHistoryVO userAccountHistory);

	/**
	 * 导出交易记录
	 * @param response
	 */
    void exportUserAccountHistory(HttpServletResponse response , Map<String, Object> userAccountHistory);
}
