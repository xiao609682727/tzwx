
package org.springcrazy.modules.msg.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.msg.entity.MsgSystem;

/**
 * 系统消息视图实体类
 *
 * @author TongZhou
 * @since 2020-05-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "MsgSystemVO对象", description = "系统消息")
public class MsgSystemVO extends MsgSystem {
	private static final long serialVersionUID = 1L;

	private String userIds;

}
