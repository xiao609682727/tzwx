
package org.springcrazy.modules.web.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.web.entity.WebsiteNavigate;

/**
 * 导航表数据传输对象实体类
 *
 * @author TongZhou
 * @since 2020-05-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WebsiteNavigateDTO extends WebsiteNavigate {
	private static final long serialVersionUID = 1L;

}
