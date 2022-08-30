
package org.springcrazy.modules.web.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.web.entity.MsgConfig;

/**
 * 视图实体类
 *
 * @author TongZhou
 * @since 2020-05-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "MsgConfigVO对象", description = "MsgConfigVO对象")
public class MsgConfigVO extends MsgConfig {
	private static final long serialVersionUID = 1L;

}
