
package org.springcrazy.modules.web.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.modules.web.entity.WebsiteRecommendDetail;
import org.springcrazy.modules.web.mapper.WebsiteRecommendDetailMapper;
import org.springcrazy.modules.web.service.IWebsiteRecommendDetailService;
import org.springcrazy.modules.web.vo.WebsiteRecommendDetailVO;
import org.springframework.stereotype.Service;

/**
 * 推荐详情表 服务实现类
 *
 * @author TongZhou
 * @since 2020-05-13
 */
@Service
public class WebsiteRecommendDetailServiceImpl extends ServiceImpl<WebsiteRecommendDetailMapper, WebsiteRecommendDetail> implements IWebsiteRecommendDetailService {

	@Override
	public IPage<WebsiteRecommendDetailVO> selectWebsiteRecommendDetailPage(IPage<WebsiteRecommendDetailVO> page, WebsiteRecommendDetailVO websiteRecommendDetail) {
		return page.setRecords(baseMapper.selectWebsiteRecommendDetailPage(page, websiteRecommendDetail));
	}

}
