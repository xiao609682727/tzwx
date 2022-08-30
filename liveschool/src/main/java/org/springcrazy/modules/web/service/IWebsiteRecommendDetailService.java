
package org.springcrazy.modules.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.web.entity.WebsiteRecommendDetail;
import org.springcrazy.modules.web.vo.WebsiteRecommendDetailVO;

/**
 * 推荐详情表 服务类
 *
 * @author TongZhou
 * @since 2020-05-13
 */
public interface IWebsiteRecommendDetailService extends IService<WebsiteRecommendDetail> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param websiteRecommendDetail
	 * @return
	 */
	IPage<WebsiteRecommendDetailVO> selectWebsiteRecommendDetailPage(IPage<WebsiteRecommendDetailVO> page, WebsiteRecommendDetailVO websiteRecommendDetail);

}
