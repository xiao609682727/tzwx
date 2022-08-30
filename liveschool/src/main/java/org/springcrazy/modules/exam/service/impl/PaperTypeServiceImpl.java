
package org.springcrazy.modules.exam.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.modules.exam.entity.PaperType;
import org.springcrazy.modules.exam.mapper.PaperTypeMapper;
import org.springcrazy.modules.exam.service.IPaperTypeService;
import org.springcrazy.modules.exam.vo.PaperTypeVO;
import org.springframework.stereotype.Service;

/**
 * 试卷类型表 服务实现类
 *
 * @author TongZhou
 * @since 2020-12-01
 */
@Service
public class PaperTypeServiceImpl extends ServiceImpl<PaperTypeMapper, PaperType> implements IPaperTypeService {

	@Override
	public IPage<PaperTypeVO> selectPaperTypePage(IPage<PaperTypeVO> page, PaperTypeVO paperType) {
		return page.setRecords(baseMapper.selectPaperTypePage(page, paperType));
	}

}
