
package org.springcrazy.modules.coursecard.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.coursecard.entity.CardCode;

/**
 * 课程卡编码表数据传输对象实体类
 *
 * @author TongZhou
 * @since 2021-04-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CardCodeDTO extends CardCode {
	private static final long serialVersionUID = 1L;

}
