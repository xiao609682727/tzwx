
package org.springcrazy.modules.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.exam.entity.Question;
import org.springcrazy.modules.exam.vo.QuestionVO;

import java.util.List;

/**
 * 考试试题表 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-12-02
 */
public interface QuestionMapper extends BaseMapper<Question> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param question
	 * @return
	 */
	List<QuestionVO> selectQuestionPage(IPage page, QuestionVO question);

}
