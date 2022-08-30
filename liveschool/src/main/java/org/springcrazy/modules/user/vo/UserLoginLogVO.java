
package org.springcrazy.modules.user.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.user.entity.UserLoginLog;

/**
 * 视图实体类
 *
 * @author TongZhou
 * @since 2020-05-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UserLoginLogVO对象", description = "UserLoginLogVO对象")
public class UserLoginLogVO extends UserLoginLog {
	private static final long serialVersionUID = 1L;

}
