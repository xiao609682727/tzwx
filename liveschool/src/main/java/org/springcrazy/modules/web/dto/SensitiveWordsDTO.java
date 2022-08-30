
package org.springcrazy.modules.web.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.web.entity.SensitiveWords;

/**
 * 网站敏感词数据传输对象实体类
 *
 * @author TongZhou
 * @since 2020-05-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SensitiveWordsDTO extends SensitiveWords {
	private static final long serialVersionUID = 1L;

}
