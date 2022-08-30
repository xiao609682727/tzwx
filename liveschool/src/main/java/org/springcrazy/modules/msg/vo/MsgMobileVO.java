
package org.springcrazy.modules.msg.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.msg.entity.MsgMobile;

/**
 * 手机短信发送记录视图实体类
 *
 * @author TongZhou
 * @since 2020-05-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "MsgMobileVO对象", description = "手机短信发送记录")
public class MsgMobileVO extends MsgMobile {
	private static final long serialVersionUID = 1L;

}
