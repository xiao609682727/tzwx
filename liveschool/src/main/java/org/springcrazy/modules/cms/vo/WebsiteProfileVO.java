
package org.springcrazy.modules.cms.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.cms.entity.WebsiteProfile;

/**
 * 系统配置表视图实体类
 *
 * @author TongZhou
 * @since 2020-03-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WebsiteProfileVO对象", description = "系统配置表")
public class WebsiteProfileVO extends WebsiteProfile {
	private static final long serialVersionUID = 1L;

}
