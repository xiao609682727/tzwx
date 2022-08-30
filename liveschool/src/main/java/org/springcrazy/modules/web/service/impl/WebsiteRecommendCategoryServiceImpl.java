
package org.springcrazy.modules.web.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springcrazy.modules.web.entity.WebsiteRecommendCategory;
import org.springcrazy.modules.web.mapper.WebsiteRecommendCategoryMapper;
import org.springcrazy.modules.web.mapper.WebsiteRecommendDetailMapper;
import org.springcrazy.modules.web.service.IWebsiteRecommendCategoryService;
import org.springcrazy.modules.web.vo.WebsiteRecommendCategoryVO;
import org.springcrazy.modules.web.vo.WebsiteRecommendDetailVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 网站推荐分类 服务实现类
 *
 * @author TongZhou
 * @since 2020-05-13
 */
@Service
@AllArgsConstructor
public class WebsiteRecommendCategoryServiceImpl extends ServiceImpl<WebsiteRecommendCategoryMapper, WebsiteRecommendCategory> implements IWebsiteRecommendCategoryService {

	WebsiteRecommendDetailMapper websiteRecommendDetailMapper;

	@Override
	public IPage<WebsiteRecommendCategoryVO> selectWebsiteRecommendCategoryPage(IPage<WebsiteRecommendCategoryVO> page, WebsiteRecommendCategoryVO websiteRecommendCategory) {
		return page.setRecords(baseMapper.selectWebsiteRecommendCategoryPage(page, websiteRecommendCategory));
	}

	@Override
	public List<WebsiteRecommendCategoryVO> getRecommendCategoryList(Integer frontType) {
		WebsiteRecommendCategoryVO websiteRecommendCategoryVO = new WebsiteRecommendCategoryVO();
		websiteRecommendCategoryVO.setFrontType(frontType);
		List<WebsiteRecommendCategoryVO> categoryVOList = baseMapper.selectWebsiteRecommendCategoryList(websiteRecommendCategoryVO);
		categoryVOList.forEach(vo ->{
			WebsiteRecommendDetailVO websiteRecommendDetail = new WebsiteRecommendDetailVO();
			websiteRecommendDetail.setRecommendId(vo.getId());
			List<WebsiteRecommendDetailVO> list = websiteRecommendDetailMapper.selectWebsiteRecommendDetailList(websiteRecommendDetail);
			vo.setDetailVOList(list);
		});
		return categoryVOList;
	}

}
