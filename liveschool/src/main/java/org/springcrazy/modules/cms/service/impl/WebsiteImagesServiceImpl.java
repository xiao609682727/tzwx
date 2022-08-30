
package org.springcrazy.modules.cms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.modules.cms.entity.WebsiteImages;
import org.springcrazy.modules.cms.mapper.WebsiteImagesMapper;
import org.springcrazy.modules.cms.service.IWebsiteImagesService;
import org.springcrazy.modules.cms.vo.WebsiteImagesVO;
import org.springframework.stereotype.Service;

/**
 * banner图管理 服务实现类
 *
 * @author TongZhou
 * @since 2020-03-06
 */
@Service
public class WebsiteImagesServiceImpl extends ServiceImpl<WebsiteImagesMapper, WebsiteImages> implements IWebsiteImagesService {

	@Override
	public IPage<WebsiteImagesVO> selectWebsiteImagesPage(IPage<WebsiteImagesVO> page, WebsiteImagesVO websiteImages) {
		return page.setRecords(baseMapper.selectWebsiteImagesPage(page, websiteImages));
	}

}
