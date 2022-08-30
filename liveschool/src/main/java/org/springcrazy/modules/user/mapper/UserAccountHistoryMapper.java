
package org.springcrazy.modules.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springcrazy.modules.system.excel.UserAccountHistoryExcel;
import org.springcrazy.modules.user.entity.UserAccountHistory;
import org.springcrazy.modules.user.vo.UserAccountHistoryVO;

import java.util.List;

/**
 * 账户流水记录 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-05-07
 */
public interface UserAccountHistoryMapper extends BaseMapper<UserAccountHistory> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param userAccountHistory
	 * @return
	 */
	List<UserAccountHistoryVO> selectUserAccountHistoryPage(IPage page,@Param("userAccountHistory") UserAccountHistoryVO userAccountHistory);

	/**
	 * 导出交易记录
	 * @param page
	 * @return
	 */
	List<UserAccountHistoryExcel> exportUserAccountHistory(Page<UserAccountHistoryExcel> page, @Param("ew")UserAccountHistoryVO userAccountHistoryVO);
}
