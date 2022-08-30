
package org.springcrazy.modules.web.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.modules.web.entity.SmsConfig;
import org.springcrazy.modules.web.mapper.SmsConfigMapper;
import org.springcrazy.modules.web.service.ISmsConfigService;
import org.springcrazy.modules.web.vo.SmsConfigVO;
import org.springframework.stereotype.Service;

/**
 *  服务实现类
 *
 * @author TongZhou
 * @since 2020-05-09
 */
@Service
public class SmsConfigServiceImpl extends ServiceImpl<SmsConfigMapper, SmsConfig> implements ISmsConfigService {

	@Override
	public IPage<SmsConfigVO> selectSmsConfigPage(IPage<SmsConfigVO> page, SmsConfigVO smsConfig) {
		return page.setRecords(baseMapper.selectSmsConfigPage(page, smsConfig));
	}


}
