
package org.springcrazy.modules.exam.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.exam.entity.ErrorCheck;

/**
 * 试题纠错表数据传输对象实体类
 *
 * @author TongZhou
 * @since 2020-12-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ErrorCheckDTO extends ErrorCheck {
	private static final long serialVersionUID = 1L;

}
