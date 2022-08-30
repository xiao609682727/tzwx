
package org.springcrazy.modules.exam.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.exam.entity.Point;

/**
 * 考点数据传输对象实体类
 *
 * @author TongZhou
 * @since 2020-12-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PointDTO extends Point {
	private static final long serialVersionUID = 1L;

}
