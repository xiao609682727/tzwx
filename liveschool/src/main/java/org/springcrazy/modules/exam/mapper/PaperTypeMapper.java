
package org.springcrazy.modules.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.exam.entity.PaperType;
import org.springcrazy.modules.exam.vo.PaperTypeVO;

import java.util.List;

/**
 * 试卷类型表 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-12-01
 */
public interface PaperTypeMapper extends BaseMapper<PaperType> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param paperType
	 * @return
	 */
	List<PaperTypeVO> selectPaperTypePage(IPage page, PaperTypeVO paperType);

}
