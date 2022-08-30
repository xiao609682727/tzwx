
package org.springcrazy.modules.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springcrazy.core.secure.CrazyUser;

import java.io.Serializable;
import java.util.List;

/**
 * 用户信息
 *

 */
@Data
@ApiModel(description = "用户信息")
public class UserInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户基础信息
	 */
	@ApiModelProperty(value = "用户")
	private CrazyUser user;

	/**
	 * 权限标识集合
	 */
	@ApiModelProperty(value = "权限集合")
	private List<String> permissions;

	/**
	 * 角色集合
	 */
	@ApiModelProperty(value = "角色集合")
	private List<String> roles;

}
