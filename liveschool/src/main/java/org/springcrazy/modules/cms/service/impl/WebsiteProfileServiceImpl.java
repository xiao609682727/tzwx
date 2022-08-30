
package org.springcrazy.modules.cms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.modules.cms.entity.WebsiteProfile;
import org.springcrazy.modules.cms.mapper.WebsiteProfileMapper;
import org.springcrazy.modules.cms.service.IWebsiteProfileService;
import org.springcrazy.modules.cms.vo.WebsiteProfileVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * banner图管理 服务实现类
 *
 * @author TongZhou
 * @since 2020-03-06
 */
@Service
public class WebsiteProfileServiceImpl extends ServiceImpl<WebsiteProfileMapper, WebsiteProfile> implements IWebsiteProfileService {

	@Override
	public IPage<WebsiteProfileVO> selectWebsiteProfilePage(IPage<WebsiteProfileVO> page, WebsiteProfileVO websiteProfile) {
		return page.setRecords(baseMapper.selectWebsiteProfilePage(page, websiteProfile));
	}

	@Override
	public Map<String, String> getConfig(String configType) {
		Map<String, String> result = Maps.newHashMap();
		WebsiteProfile websiteProfile = new WebsiteProfile();
		websiteProfile.setConfigType(configType);
		List<WebsiteProfile> list = list(Condition.getQueryWrapper(websiteProfile).lambda().orderByDesc(WebsiteProfile::getConfigType));
		list.stream().forEach(p-> result.put(p.getDataKey(),p.getDataValue()));
		return result;
	}

}
