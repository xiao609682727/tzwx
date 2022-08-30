
package org.springcrazy.modules.exam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.exam.entity.ErrorQuestion;
import org.springcrazy.modules.exam.vo.ErrorQuestionVO;

import java.util.List;

/**
 * 错题记录表 服务类
 *
 * @author TongZhou
 * @since 2020-12-17
 */
public interface IErrorQuestionService extends IService<ErrorQuestion> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param errorQuestion
	 * @return
	 */
	IPage<ErrorQuestionVO> selectErrorQuestionPage(IPage<ErrorQuestionVO> page, ErrorQuestionVO errorQuestion);

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param errorQuestion
	 * @return
	 */
	List<ErrorQuestionVO> selectErrorQuestionList(ErrorQuestionVO errorQuestion);

}
