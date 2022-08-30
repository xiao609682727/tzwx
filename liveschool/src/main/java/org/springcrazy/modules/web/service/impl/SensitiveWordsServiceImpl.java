
package org.springcrazy.modules.web.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.modules.web.entity.SensitiveWords;
import org.springcrazy.modules.web.mapper.SensitiveWordsMapper;
import org.springcrazy.modules.web.service.ISensitiveWordsService;
import org.springcrazy.modules.web.vo.SensitiveWordsVO;
import org.springframework.stereotype.Service;

/**
 * 网站敏感词 服务实现类
 *
 * @author TongZhou
 * @since 2020-05-09
 */
@Service
public class SensitiveWordsServiceImpl extends ServiceImpl<SensitiveWordsMapper, SensitiveWords> implements ISensitiveWordsService {

	@Override
	public IPage<SensitiveWordsVO> selectSensitiveWordsPage(IPage<SensitiveWordsVO> page, SensitiveWordsVO sensitiveWords) {
		return page.setRecords(baseMapper.selectSensitiveWordsPage(page, sensitiveWords));
	}

	@Override
	public IPage<SensitiveWords> selectSensitiveWordsPageList(IPage<SensitiveWords> page, SensitiveWords sensitiveWords) {
		return page.setRecords(baseMapper.selectSensitiveWordsPageList(page, sensitiveWords));
	}


}
