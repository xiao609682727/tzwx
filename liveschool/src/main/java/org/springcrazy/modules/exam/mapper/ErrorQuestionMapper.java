
package org.springcrazy.modules.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springcrazy.modules.exam.entity.ErrorQuestion;
import org.springcrazy.modules.exam.vo.ErrorQuestionVO;

import java.util.List;

/**
 * 错题记录表 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-12-17
 */
public interface ErrorQuestionMapper extends BaseMapper<ErrorQuestion> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param errorQuestion
	 * @return
	 */
	List<ErrorQuestionVO> selectErrorQuestionPage(IPage page,@Param("ew") ErrorQuestionVO errorQuestion);


	List<ErrorQuestionVO> selectErrorQuestionList(ErrorQuestionVO errorQuestion);
}
