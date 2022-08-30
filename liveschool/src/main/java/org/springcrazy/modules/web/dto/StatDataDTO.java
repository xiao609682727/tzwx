
package org.springcrazy.modules.web.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.web.entity.StatData;

/**
 * 数据传输对象实体类
 *
 * @author TongZhou
 * @since 2020-05-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StatDataDTO extends StatData {
	private static final long serialVersionUID = 1L;

}
