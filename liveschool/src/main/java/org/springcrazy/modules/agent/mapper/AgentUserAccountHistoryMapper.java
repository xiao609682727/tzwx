
package org.springcrazy.modules.agent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springcrazy.modules.agent.entity.AgentUserAccountHistory;
import org.springcrazy.modules.agent.vo.AgentUserAccountHistoryVO;
import org.springcrazy.modules.system.excel.AgentUserAccountHistoryExcel;

import java.util.List;

/**
 * 代理商账户流水记录 Mapper 接口
 *
 * @author TongZhou
 * @since 2021-04-30
 */
public interface AgentUserAccountHistoryMapper extends BaseMapper<AgentUserAccountHistory> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param agentUserAccountHistory
	 * @return
	 */
	List<AgentUserAccountHistoryVO> selectAgentUserAccountHistoryPage(IPage page, AgentUserAccountHistoryVO agentUserAccountHistory);

	/**
	 * 导出记录
	 */
	List<AgentUserAccountHistoryExcel> exportAgentUserAccountHistory(Page<AgentUserAccountHistoryExcel> page, @Param("agentUserAccountHistory") AgentUserAccountHistoryVO agentUserAccountHistory);
}
