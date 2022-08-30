
package org.springcrazy.modules.edu.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.edu.entity.Orders;
import org.springcrazy.modules.edu.entity.TrxorderDetail;

import java.util.List;

/**
 * 课程订单表数据传输对象实体类
 *
 * @author TongZhou
 * @since 2020-05-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrdersDTO extends Orders {
	private static final long serialVersionUID = 1L;

	List<TrxorderDetail> list;

}
