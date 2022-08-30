
package org.springcrazy.modules.cms.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.cms.entity.WebsiteProfile;

/**
 * 系统配置表数据传输对象实体类
 *
 * @author TongZhou
 * @since 2020-03-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WebsiteProfileDTO extends WebsiteProfile {
	private static final long serialVersionUID = 1L;

}
