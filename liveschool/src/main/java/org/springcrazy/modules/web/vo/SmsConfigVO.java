
package org.springcrazy.modules.web.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.web.entity.SmsConfig;

/**
 * 视图实体类
 *
 * @author TongZhou
 * @since 2020-05-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SmsConfigVO对象", description = "SmsConfigVO对象")
public class SmsConfigVO extends SmsConfig {
	private static final long serialVersionUID = 1L;

}
