
package org.springcrazy.modules.edu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.edu.dto.OrdersDTO;
import org.springcrazy.modules.edu.entity.Orders;
import org.springcrazy.modules.edu.vo.OrdersVO;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 课程订单表 服务类
 *
 * @author TongZhou
 * @since 2020-05-09
 */
public interface IOrdersService extends IService<Orders> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param orders
	 * @return
	 */
	IPage<OrdersVO> selectOrdersPage(IPage<OrdersVO> page, OrdersVO orders);

	/**
	 * 统计详情自定义分页
	 *
	 * @param page
	 * @param orders
	 * @return
	 */
	IPage<OrdersVO> selectOrdersPageList(IPage<OrdersVO> page, OrdersVO orders);

	/**
	 * 自定义分页
	 *
	 * @param
	 * @param orders
	 * @return
	 */
	IPage<OrdersVO> selectOrderStatisticsPage(IPage<OrdersVO> page, OrdersVO orders);

	/**
	 * 用于更新订单状态，订单状态修改，订单详情对应状态也需要更改
	 * @param orders
	 * @return
	 */
	boolean updateOrder(Orders orders);

	boolean createOrder(OrdersDTO order);

	boolean accountPay(Integer orderId);

	Map<String,Object> selectOrderMoney(Map<String,Object> param);

	/**
	 * 获取导出用户数据
	 *
	 * @param response
	 * @return
	 */
	void exportOrder(HttpServletResponse response , String startTime);

	/**
	 * 获取导出用户数据
	 *
	 * @param response
	 * @return
	 */
	void exportOrders(HttpServletResponse response ,Map<String, Object> Orders);


	/**
	 * 获取导出面授用户数据
	 *
	 * @param response
	 * @return
	 */
	void exportLineOrders(HttpServletResponse response ,Map<String, Object> Orders);

}
