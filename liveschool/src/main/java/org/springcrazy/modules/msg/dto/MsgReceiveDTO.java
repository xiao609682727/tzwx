
package org.springcrazy.modules.msg.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.msg.entity.MsgReceive;

/**
 * 站内信数据传输对象实体类
 *
 * @author TongZhou
 * @since 2020-05-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MsgReceiveDTO extends MsgReceive {
	private static final long serialVersionUID = 1L;

}
