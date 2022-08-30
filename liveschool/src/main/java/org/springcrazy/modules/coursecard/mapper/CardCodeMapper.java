
package org.springcrazy.modules.coursecard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springcrazy.modules.coursecard.entity.CardCode;
import org.springcrazy.modules.coursecard.vo.CardCodeVO;
import org.springcrazy.modules.system.excel.ExportCardCodeExcel;

import java.util.List;

/**
 * 课程卡编码表 Mapper 接口
 *
 * @author TongZhou
 * @since 2021-04-02
 */
public interface CardCodeMapper extends BaseMapper<CardCode> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param cardCodeVO
	 * @return
	 */
	List<CardCodeVO> selectCardCodePage(IPage page, @Param("cardCodeVO")CardCodeVO cardCodeVO);
	/**
	 * 获取导出用户数据
	 * @return
	 */
	List<ExportCardCodeExcel> exportCardCodeVO(IPage page, @Param("cardCodeVO")CardCodeVO cardCodeVO);

}
