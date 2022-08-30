
package org.springcrazy.modules.exam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.exam.entity.Question;
import org.springcrazy.modules.exam.vo.QuestionVO;
import org.springcrazy.modules.system.excel.ExamExcel;

import java.util.List;

/**
 * 考试试题表 服务类
 *
 * @author TongZhou
 * @since 2020-12-02
 */
public interface IQuestionService extends IService<Question> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param question
	 * @return
	 */
	IPage<QuestionVO> selectQuestionPage(IPage<QuestionVO> page, QuestionVO question);
	/**
	 * 导入用户数据
	 *
	 * @param data
	 * @return
	 */
	void importExam(List<ExamExcel> data);

	/**
	 * 新增或修改用户
	 * @param user
	 * @return
	 */
	void submit(List<Question> user);

}
