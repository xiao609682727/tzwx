
package org.springcrazy.modules.web.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.web.entity.Area;

/**
 * 数据传输对象实体类
 *
 * @author TongZhou
 * @since 2020-05-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AreaDTO extends Area {
	private static final long serialVersionUID = 1L;

}
