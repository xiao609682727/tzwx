
package org.springcrazy.modules.edu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springcrazy.modules.edu.entity.Orders;
import org.springcrazy.modules.edu.vo.OrdersVO;
import org.springcrazy.modules.system.excel.ExportOrderExcel;
import org.springcrazy.modules.system.excel.ExportOrdersExcel;

import java.util.List;
import java.util.Map;

/**
 * 课程订单表 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-05-09
 */
public interface OrdersMapper extends BaseMapper<Orders> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param orders
	 * @return
	 */
	List<OrdersVO> selectOrdersPage(IPage page,@Param("orders") OrdersVO orders);

	/**
	 * 统计详情自定义分页
	 *
	 * @param page
	 * @param orders
	 * @return
	 */
	List<OrdersVO> selectOrdersPageList(IPage page,@Param("orders") OrdersVO orders);

	/**
	 * 自定义分页
	 *
	 * @param
	 * @param orders
	 * @return
	 */
	List<OrdersVO> selectOrderStatisticsPage(@Param("orders") OrdersVO orders);

	/**
	 * 根据订单查询收入
	 * @return
	 */
	Map<String,Object> selectOrderMoney(Map<String,Object> param);

	/**
	 * 获取导出用户数据
	 *
	 * @return
	 */
	List<ExportOrderExcel> exportOrder(IPage page , @Param("startTime")String startTime, @Param("endTime")String endTime);

	/**
	 * 获取导出用户数据
	 *
	 * @param ordersVO
	 * @return
	 */
	List<ExportOrdersExcel> exportOrders(IPage page, @Param("ew") OrdersVO ordersVO);

}
