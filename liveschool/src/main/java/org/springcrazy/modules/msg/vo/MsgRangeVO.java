
package org.springcrazy.modules.msg.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.msg.entity.MsgRange;

/**
 * 消息范围 一对多中间表视图实体类
 *
 * @author TongZhou
 * @since 2020-05-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "MsgRangeVO对象", description = "消息范围 一对多中间表")
public class MsgRangeVO extends MsgRange {
	private static final long serialVersionUID = 1L;

}
