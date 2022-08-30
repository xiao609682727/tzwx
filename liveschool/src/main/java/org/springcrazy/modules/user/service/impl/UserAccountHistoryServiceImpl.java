
package org.springcrazy.modules.user.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springcrazy.core.mp.base.BaseServiceImpl;
import org.springcrazy.modules.system.excel.UserAccountHistoryExcel;
import org.springcrazy.modules.user.entity.UserAccountHistory;
import org.springcrazy.modules.user.mapper.UserAccountHistoryMapper;
import org.springcrazy.modules.user.service.IUserAccountHistoryService;
import org.springcrazy.modules.user.vo.UserAccountHistoryVO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 账户流水记录 服务实现类
 *
 * @author TongZhou
 * @since 2020-05-07
 */
@Service
public class UserAccountHistoryServiceImpl extends BaseServiceImpl<UserAccountHistoryMapper, UserAccountHistory> implements IUserAccountHistoryService {

	@Override
	public IPage<UserAccountHistoryVO> selectUserAccountHistoryPage(IPage<UserAccountHistoryVO> page, UserAccountHistoryVO userAccountHistory) {
		return page.setRecords(baseMapper.selectUserAccountHistoryPage(page, userAccountHistory));
	}

	@Override
	public void exportUserAccountHistory(HttpServletResponse response , Map<String, Object> userAccountHistory) {
		ExcelWriter excelWriter = null;
		try {
			Integer size = 3000;

			excelWriter = EasyExcel.write(response.getOutputStream(), UserAccountHistoryExcel.class).build();
			WriteSheet writeSheet = EasyExcel.writerSheet("用户账户记录数据").build();
			//分页写入
			UserAccountHistoryVO userAccountHistoryVO = new UserAccountHistoryVO();
			userAccountHistoryVO.setUserName((String) userAccountHistory.get("userName"));
			userAccountHistoryVO.setMobile((String) userAccountHistory.get("mobile"));
			userAccountHistoryVO.setEmail((String) userAccountHistory.get("email"));
			userAccountHistoryVO.setActHistoryType((String) userAccountHistory.get("actHistoryType"));
			userAccountHistoryVO.setBizType((String) userAccountHistory.get("bizType"));
			userAccountHistoryVO.setCreateTime(userAccountHistory.get("createTime").equals("")? null : (Date) userAccountHistory.get("createTime"));

			List<UserAccountHistoryExcel> list = baseMapper.exportUserAccountHistory(null , userAccountHistoryVO);

			if (list.size() <= size){
				excelWriter.write(list, writeSheet);
			}else {
				Page<UserAccountHistoryExcel> page = new Page<>();
				for (int i = 1; i <= (list.size() / size) + 1; i++) {
					page.setCurrent(i);
					page.setSize(size);
					list = baseMapper.exportUserAccountHistory(page, userAccountHistoryVO);
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
