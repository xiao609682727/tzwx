
package org.springcrazy.modules.web.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.web.entity.WebsiteRecommendCategory;

/**
 * 网站推荐分类数据传输对象实体类
 *
 * @author TongZhou
 * @since 2020-05-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WebsiteRecommendCategoryDTO extends WebsiteRecommendCategory {
	private static final long serialVersionUID = 1L;

}
