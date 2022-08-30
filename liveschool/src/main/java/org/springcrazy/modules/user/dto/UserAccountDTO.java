
package org.springcrazy.modules.user.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.user.entity.UserAccount;

/**
 * 账户信息账户信息，记录用户的账户金额数据传输对象实体类
 *
 * @author TongZhou
 * @since 2020-05-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserAccountDTO extends UserAccount {
	private static final long serialVersionUID = 1L;

}
