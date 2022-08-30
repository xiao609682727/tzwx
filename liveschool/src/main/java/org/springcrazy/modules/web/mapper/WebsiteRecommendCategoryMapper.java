
package org.springcrazy.modules.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.web.entity.WebsiteRecommendCategory;
import org.springcrazy.modules.web.vo.WebsiteRecommendCategoryVO;

import java.util.List;

/**
 * 网站推荐分类 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-05-13
 */
public interface WebsiteRecommendCategoryMapper extends BaseMapper<WebsiteRecommendCategory> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param websiteRecommendCategory
	 * @return
	 */
	List<WebsiteRecommendCategoryVO> selectWebsiteRecommendCategoryPage(IPage page, WebsiteRecommendCategoryVO websiteRecommendCategory);

	List<WebsiteRecommendCategoryVO> selectWebsiteRecommendCategoryList(WebsiteRecommendCategoryVO websiteRecommendCategory);
}
