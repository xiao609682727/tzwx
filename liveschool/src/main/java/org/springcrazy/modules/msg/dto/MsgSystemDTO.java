
package org.springcrazy.modules.msg.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.msg.entity.MsgSystem;

/**
 * 系统消息数据传输对象实体类
 *
 * @author TongZhou
 * @since 2020-05-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MsgSystemDTO extends MsgSystem {
	private static final long serialVersionUID = 1L;

}
