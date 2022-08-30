
package org.springcrazy.modules.web.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.web.entity.WebsiteRecommendCategory;

import java.util.List;

/**
 * 网站推荐分类视图实体类
 *
 * @author TongZhou
 * @since 2020-05-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WebsiteRecommendCategoryVO对象", description = "网站推荐分类")
public class WebsiteRecommendCategoryVO extends WebsiteRecommendCategory {
	private static final long serialVersionUID = 1L;

	List<WebsiteRecommendDetailVO> detailVOList;

}
