
package org.springcrazy.modules.cms.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.cms.entity.WebsiteImages;

/**
 * banner图管理数据传输对象实体类
 *
 * @author TongZhou
 * @since 2020-03-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WebsiteImagesDTO extends WebsiteImages {
	private static final long serialVersionUID = 1L;

}
