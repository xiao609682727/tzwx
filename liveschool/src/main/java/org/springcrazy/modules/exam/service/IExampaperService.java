
package org.springcrazy.modules.exam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.exam.entity.Exampaper;
import org.springcrazy.modules.exam.entity.PaperMiddle;
import org.springcrazy.modules.exam.vo.ExampaperVO;
import org.springcrazy.modules.exam.vo.QstmiddleVO;

import java.util.List;
import java.util.Map;

/**
 * 考试试卷表 服务类
 *
 * @author TongZhou
 * @since 2020-12-01
 */
public interface IExampaperService extends IService<Exampaper> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param exampaper
	 * @return
	 */
	IPage<ExampaperVO> selectExampaperPage(IPage<ExampaperVO> page, ExampaperVO exampaper);

	void insertQuestion(List<PaperMiddle> examQuestionDTOList);

	List<PaperMiddle> queryQuestion(String paperId);

	Map<Integer,QstmiddleVO> queryQuestionReturnMap(Integer paperId);

}
