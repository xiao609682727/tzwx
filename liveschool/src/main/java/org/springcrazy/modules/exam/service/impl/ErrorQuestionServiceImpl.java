
package org.springcrazy.modules.exam.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.modules.exam.entity.ErrorQuestion;
import org.springcrazy.modules.exam.mapper.ErrorQuestionMapper;
import org.springcrazy.modules.exam.service.IErrorQuestionService;
import org.springcrazy.modules.exam.vo.ErrorQuestionVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 错题记录表 服务实现类
 *
 * @author TongZhou
 * @since 2020-12-17
 */
@Service
public class ErrorQuestionServiceImpl extends ServiceImpl<ErrorQuestionMapper, ErrorQuestion> implements IErrorQuestionService {

	@Override
	public IPage<ErrorQuestionVO> selectErrorQuestionPage(IPage<ErrorQuestionVO> page, ErrorQuestionVO errorQuestion) {
		return page.setRecords(baseMapper.selectErrorQuestionPage(page, errorQuestion));
	}

	@Override
	public List<ErrorQuestionVO> selectErrorQuestionList(ErrorQuestionVO errorQuestion) {
		return baseMapper.selectErrorQuestionList(errorQuestion);
	}

}
