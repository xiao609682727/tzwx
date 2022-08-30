
package org.springcrazy.modules.exam.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.modules.exam.entity.PaperMiddle;
import org.springcrazy.modules.exam.mapper.PaperMiddleMapper;
import org.springcrazy.modules.exam.service.IPaperMiddleService;
import org.springcrazy.modules.exam.vo.PaperMiddleVO;
import org.springframework.stereotype.Service;

/**
 * 大题（试卷试题类型中间表） 服务实现类
 *
 * @author TongZhou
 * @since 2020-12-15
 */
@Service
public class PaperMiddleServiceImpl extends ServiceImpl<PaperMiddleMapper, PaperMiddle> implements IPaperMiddleService {

	@Override
	public IPage<PaperMiddleVO> selectPaperMiddlePage(IPage<PaperMiddleVO> page, PaperMiddleVO paperMiddle) {
		return page.setRecords(baseMapper.selectPaperMiddlePage(page, paperMiddle));
	}

}
