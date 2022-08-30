
package org.springcrazy.modules.exam.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.modules.exam.entity.Qstmiddle;
import org.springcrazy.modules.exam.mapper.QstmiddleMapper;
import org.springcrazy.modules.exam.service.IQstmiddleService;
import org.springcrazy.modules.exam.vo.QstmiddleVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 考试试卷表 服务实现类
 *
 * @author TongZhou
 * @since 2020-12-15
 */
@Service
public class QstmiddleServiceImpl extends ServiceImpl<QstmiddleMapper, Qstmiddle> implements IQstmiddleService {

	@Override
	public IPage<QstmiddleVO> selectQstmiddlePage(IPage<QstmiddleVO> page, QstmiddleVO qstmiddle) {
		return page.setRecords(baseMapper.selectQstmiddlePage(page, qstmiddle));
	}

	@Override
	public List<QstmiddleVO> selectQstmiddle(String id) {
		return baseMapper.selectQstmiddle(id);
	}

}
