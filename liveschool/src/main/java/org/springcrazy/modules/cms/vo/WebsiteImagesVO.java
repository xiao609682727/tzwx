
package org.springcrazy.modules.cms.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.cms.entity.WebsiteImages;

/**
 * banner图管理视图实体类
 *
 * @author TongZhou
 * @since 2020-03-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WebsiteImagesVO对象", description = "banner图管理")
public class WebsiteImagesVO extends WebsiteImages {
	private static final long serialVersionUID = 1L;

}
