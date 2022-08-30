
package org.springcrazy.modules.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springcrazy.modules.web.entity.WebsiteRecommendDetail;
import org.springcrazy.modules.web.vo.WebsiteRecommendDetailVO;

import java.util.List;

/**
 * 推荐详情表 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-05-13
 */
public interface WebsiteRecommendDetailMapper extends BaseMapper<WebsiteRecommendDetail> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param websiteRecommendDetail
	 * @return
	 */
	List<WebsiteRecommendDetailVO> selectWebsiteRecommendDetailPage(IPage page,@Param("recommendDetail") WebsiteRecommendDetailVO websiteRecommendDetail);

	List<WebsiteRecommendDetailVO> selectWebsiteRecommendDetailList(WebsiteRecommendDetailVO websiteRecommendDetail);
}
