
package org.springcrazy.modules.agent.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.modules.agent.entity.AgentUserAccountHistory;
import org.springcrazy.modules.agent.mapper.AgentUserAccountHistoryMapper;
import org.springcrazy.modules.agent.service.IAgentUserAccountHistoryService;
import org.springcrazy.modules.agent.vo.AgentUserAccountHistoryVO;
import org.springcrazy.modules.system.excel.AgentUserAccountHistoryExcel;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 代理商账户流水记录 服务实现类
 *
 * @author TongZhou
 * @since 2021-04-30
 */
@Service
public class AgentUserAccountHistoryServiceImpl extends ServiceImpl<AgentUserAccountHistoryMapper, AgentUserAccountHistory> implements IAgentUserAccountHistoryService {

	@Override
	public IPage<AgentUserAccountHistoryVO> selectAgentUserAccountHistoryPage(IPage<AgentUserAccountHistoryVO> page, AgentUserAccountHistoryVO agentUserAccountHistory) {
		return page.setRecords(baseMapper.selectAgentUserAccountHistoryPage(page, agentUserAccountHistory));
	}
	/**
	 * 导出excel数据记录
	 */
	public void exportAgentUserAccountHistory(HttpServletResponse response , Map<String, Object> agentUserAccountHistory){
		ExcelWriter excelWriter = null;
		try {
			Integer size = 3000;

			excelWriter = EasyExcel.write(response.getOutputStream(), AgentUserAccountHistoryExcel.class).build();
			WriteSheet writeSheet = EasyExcel.writerSheet("用户账户记录数据").build();
			//分页写入
			AgentUserAccountHistoryVO agentUserAccountHistoryVO = new AgentUserAccountHistoryVO();
			agentUserAccountHistoryVO.setAccount((String) agentUserAccountHistory.get("account"));
			agentUserAccountHistoryVO.setRealName((String) agentUserAccountHistory.get("realName"));
			agentUserAccountHistoryVO.setPhone((String) agentUserAccountHistory.get("phone"));
			agentUserAccountHistoryVO.setActHistoryType((String) agentUserAccountHistory.get("actHistoryType"));
			agentUserAccountHistoryVO.setBizType((String) agentUserAccountHistory.get("bizType"));

			List<AgentUserAccountHistoryExcel> list = baseMapper.exportAgentUserAccountHistory(null , agentUserAccountHistoryVO);

			if (list.size() <= size){
				excelWriter.write(list, writeSheet);
			}else {
				Page<AgentUserAccountHistoryExcel> page = new Page<>();
				for (int i = 1; i <= (list.size() / size) + 1; i++) {
					page.setCurrent(i);
					page.setSize(size);
					list = baseMapper.exportAgentUserAccountHistory(page, agentUserAccountHistoryVO);
					excelWriter.write(list, writeSheet);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (excelWriter != null) {
				excelWriter.finish();
			}
		}
	}
}
