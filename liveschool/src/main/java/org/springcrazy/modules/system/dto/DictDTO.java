
package org.springcrazy.modules.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.system.entity.Dict;

/**
 * 数据传输对象实体类

 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DictDTO extends Dict {
	private static final long serialVersionUID = 1L;

}
