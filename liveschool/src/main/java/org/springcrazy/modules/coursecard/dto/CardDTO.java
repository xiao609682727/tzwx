
package org.springcrazy.modules.coursecard.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.coursecard.entity.Card;

/**
 * 课程卡主表数据传输对象实体类
 *
 * @author TongZhou
 * @since 2021-03-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CardDTO extends Card {
	private static final long serialVersionUID = 1L;

}
