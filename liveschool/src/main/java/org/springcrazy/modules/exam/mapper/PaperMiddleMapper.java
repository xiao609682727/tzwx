
package org.springcrazy.modules.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.exam.entity.PaperMiddle;
import org.springcrazy.modules.exam.vo.PaperMiddleVO;

import java.util.List;

/**
 * 大题（试卷试题类型中间表） Mapper 接口
 *
 * @author TongZhou
 * @since 2020-12-15
 */
public interface PaperMiddleMapper extends BaseMapper<PaperMiddle> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param paperMiddle
	 * @return
	 */
	List<PaperMiddleVO> selectPaperMiddlePage(IPage page, PaperMiddleVO paperMiddle);

}
