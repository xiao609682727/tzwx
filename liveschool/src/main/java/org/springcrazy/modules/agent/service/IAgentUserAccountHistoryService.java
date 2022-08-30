
package org.springcrazy.modules.agent.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.agent.entity.AgentUserAccountHistory;
import org.springcrazy.modules.agent.vo.AgentUserAccountHistoryVO;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 代理商账户流水记录 服务类
 *
 * @author TongZhou
 * @since 2021-04-30
 */
public interface IAgentUserAccountHistoryService extends IService<AgentUserAccountHistory> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param agentUserAccountHistory
	 * @return
	 */
	IPage<AgentUserAccountHistoryVO> selectAgentUserAccountHistoryPage(IPage<AgentUserAccountHistoryVO> page, AgentUserAccountHistoryVO agentUserAccountHistory);
	/**
	 * 导出excel数据记录
	 */
	void exportAgentUserAccountHistory(HttpServletResponse response , Map<String, Object> agentUserAccountHistory);
}
