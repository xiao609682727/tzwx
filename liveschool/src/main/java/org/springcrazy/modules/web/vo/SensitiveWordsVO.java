
package org.springcrazy.modules.web.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.web.entity.SensitiveWords;

/**
 * 网站敏感词视图实体类
 *
 * @author TongZhou
 * @since 2020-05-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SensitiveWordsVO对象", description = "网站敏感词")
public class SensitiveWordsVO extends SensitiveWords {
	private static final long serialVersionUID = 1L;

}
