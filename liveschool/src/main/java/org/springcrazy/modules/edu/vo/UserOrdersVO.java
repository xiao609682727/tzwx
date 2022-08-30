
package org.springcrazy.modules.edu.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.edu.entity.Orders;

import java.util.List;

/**
 * 课程订单表视图实体类
 *
 * @author TongZhou
 * @since 2020-05-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OrdersVO对象", description = "课程订单表")
public class UserOrdersVO extends Orders {
	private static final long serialVersionUID = 1L;

	/**
	 * 手机号
	 */
	@ApiModelProperty(value = "手机号")
	protected String mobile;
	/**
	 * 邮箱号
	 */
	@ApiModelProperty(value = "邮箱号")
	protected String email;

	/**
	 * 账户名
	 */
	@ApiModelProperty(value = "账户名")
	protected String userName;

	/**
	 * 登录账号
	 */
	@ApiModelProperty(value = "登录账号")
	protected String loginAccount;

	private List<TrxorderDetailVO> detailVOList;
}
