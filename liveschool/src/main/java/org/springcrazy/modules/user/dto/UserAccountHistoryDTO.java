
package org.springcrazy.modules.user.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.user.entity.UserAccountHistory;

/**
 * 账户流水记录数据传输对象实体类
 *
 * @author TongZhou
 * @since 2020-05-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserAccountHistoryDTO extends UserAccountHistory {
	private static final long serialVersionUID = 1L;

}
