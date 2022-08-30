
package org.springcrazy.modules.msg.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.msg.entity.MsgEmail;

/**
 * 邮件发送记录数据传输对象实体类
 *
 * @author TongZhou
 * @since 2020-05-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MsgEmailDTO extends MsgEmail {
	private static final long serialVersionUID = 1L;

}
