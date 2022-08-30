
package org.springcrazy.modules.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.web.entity.WebsiteRecommendCategory;
import org.springcrazy.modules.web.vo.WebsiteRecommendCategoryVO;

import java.util.List;

/**
 * 网站推荐分类 服务类
 *
 * @author TongZhou
 * @since 2020-05-13
 */
public interface IWebsiteRecommendCategoryService extends IService<WebsiteRecommendCategory> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param websiteRecommendCategory
	 * @return
	 */
	IPage<WebsiteRecommendCategoryVO> selectWebsiteRecommendCategoryPage(IPage<WebsiteRecommendCategoryVO> page, WebsiteRecommendCategoryVO websiteRecommendCategory);

	List<WebsiteRecommendCategoryVO> getRecommendCategoryList(Integer frontType);
}
