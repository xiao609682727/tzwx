
package org.springcrazy.modules.edu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.google.api.client.util.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.codec.Charsets;
import org.springcrazy.core.boot.ctrl.CrazyController;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.mp.support.Query;
import org.springcrazy.core.secure.utils.SecureUtil;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.edu.entity.Orders;
import org.springcrazy.modules.edu.service.IOrdersService;
import org.springcrazy.modules.edu.service.ITrxorderDetailService;
import org.springcrazy.modules.edu.vo.OrdersVO;
import org.springcrazy.modules.edu.vo.TrxorderDetailVO;
import org.springcrazy.modules.edu.vo.UserOrdersVO;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 课程订单表 控制器
 *
 * @author TongZhou
 * @since 2020-05-09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/edu/orders")
@Api(value = "课程订单表", tags = "课程订单表接口")
public class OrdersController extends CrazyController {

	private IOrdersService ordersService;
	private ITrxorderDetailService trxorderDetailService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入orders")
	public R<UserOrdersVO> detail(Orders orders) {
		Orders detail = ordersService.getOne(Condition.getQueryWrapper(orders));
		UserOrdersVO ordersVO = BeanUtil.copy(detail, UserOrdersVO.class);
		List<Integer> ids = Lists.newArrayList();
		ids.add(detail.getId());
		List<TrxorderDetailVO> detailVOList = trxorderDetailService.selectTrxorderDetailList(ids);
		ordersVO.setDetailVOList(detailVOList);
		return R.data(ordersVO);
	}

	/**
	 * 分页 课程订单表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入orders")
	public R<IPage<Orders>> list(Orders orders, Query query) {
		IPage<Orders> pages = ordersService.page(Condition.getPage(query), Condition.getQueryWrapper(orders));
		return R.data(pages);
	}

	/**
	 * 自定义分页 课程订单表
	 */
	@GetMapping("")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入orders")
	public R<IPage<UserOrdersVO>> orderPage(OrdersVO orders, Query query) {
		//判断为学生 则进行学生id查询
		if(Func.equals(SecureUtil.getUserRole(),"student")){
			Integer userId = SecureUtil.getUserId();
			orders.setUserId(userId);
		}
		IPage<OrdersVO> pages = ordersService.selectOrdersPage(Condition.getPage(query), orders);
		List<UserOrdersVO> collect = pages.getRecords().stream().map(courseKpoint -> BeanUtil.copy(courseKpoint, UserOrdersVO.class)).collect(Collectors.toList());
		List<Integer> ids = Lists.newArrayList();
		collect.forEach(userOrdersVO -> ids.add(userOrdersVO.getId()));
		//重新组合
		if(ids.size() >0){
			List<TrxorderDetailVO> detailVOList = trxorderDetailService.selectTrxorderDetailList(ids);
			collect.forEach(userOrdersVO -> {
				List<TrxorderDetailVO> list = Lists.newArrayList();
				List<TrxorderDetailVO> temp = detailVOList.stream().filter(detail -> Func.equals(detail.getTrxorderId(), userOrdersVO.getId())).collect(Collectors.toList());
				userOrdersVO.setDetailVOList(temp);
			});
		}
		IPage<UserOrdersVO> resultPages = new Page(pages.getCurrent(),pages.getSize(),pages.getTotal());
		resultPages.setRecords(collect);
		return R.data(resultPages);
	}

	/**
	 * 自定义分页 课程订单表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入orders")
	public R<IPage<OrdersVO>> page(OrdersVO orders, Query query) {
		IPage<OrdersVO> pages = ordersService.selectOrdersPage(Condition.getPage(query), orders);
		return R.data(pages);
	}

	/**
	 * 自定义分页 课程订单表
	 */
	@GetMapping("/user/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入orders")
	public R<IPage<OrdersVO>> userPage(OrdersVO orders, Query query) {
		IPage<OrdersVO> pages = ordersService.selectOrdersPage(Condition.getPage(query), orders);
		return R.data(pages);
	}
	/**
	 * 自定义分页 课程订单表
	 */
	@GetMapping("/pageList")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入orders")
	public R<IPage<OrdersVO>> pageList(OrdersVO orders, Query query) {
		try {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date=simpleDateFormat.format(orders.getCreateTime());
		//当前时间的年
		String year=date.substring(0,4);
		//获取当前的月份
		String month=date.substring(5,7);
		String days=date.substring(8,10);
		if(Integer.valueOf(days)+1<10){
			days="0"+(Integer.valueOf(days)+1);
		}else {
			days=(Integer.valueOf(days)+1)+"";
		}
		orders.setEndTime(simpleDateFormat.parse(year+"-"+month+"-"+days));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		IPage<OrdersVO> pages = ordersService.selectOrdersPageList(Condition.getPage(query), orders);
		return R.data(pages);
	}

	/**
	 * 新增 课程订单表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入orders")
	public R save(@Valid @RequestBody Orders orders) {
		return R.status(ordersService.save(orders));
	}

	/**
	 * 修改 课程订单表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入orders")
	public R update(@Valid @RequestBody Orders orders) {
		return R.status(ordersService.updateById(orders));
	}

	/**
	 * 用于修改 课程订单状态
	 * 该接口连同订单详情中的状态一起修改
	 */
	@PostMapping("/updateOrder")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入orders")
	public R updateOrder(@Valid @RequestBody Orders orders) {
		return R.status(ordersService.updateOrder(orders));
	}


	/**
	 * 新增或修改 课程订单表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入orders")
	public R submit(@Valid @RequestBody Orders orders) {
		return R.status(ordersService.saveOrUpdate(orders));
	}


	/**
	 * 删除 课程订单表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(ordersService.removeByIds(Func.toIntList(ids)));
	}
	/**
	 * 导出订单
	 */
	@SneakyThrows
	@GetMapping("export-orders")
	@ApiOperationSupport(order = 13)
	@ApiOperation(value = "导出用户订单数据", notes = "传入orders")
	public void exportUser(@ApiIgnore @RequestParam Map<String, Object> Orders, HttpServletResponse response,@RequestParam String startTime) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String date=simpleDateFormat.format(new Date());

		Orders.remove("crazy-auth");
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding(Charsets.UTF_8.name());
		String fileName = URLEncoder.encode("收益数据导出", Charsets.UTF_8.name());
		response.setHeader("Content-disposition", "attachment;filename=" + (fileName+date) + ".xlsx");
		ordersService.exportOrder(response ,startTime+"-01");

	}
	/**
	 * 导出用户
	 */
	@SneakyThrows
	@GetMapping("export-order")
	@ApiOperationSupport(order = 15)
	@ApiOperation(value = "导出用户订单数据", notes = "传入orders")
	public void exportOrders(@ApiIgnore @RequestParam Map<String, Object> Orders, HttpServletResponse response) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String date=simpleDateFormat.format(new Date());

		Orders.remove("crazy-auth");
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding(Charsets.UTF_8.name());
		String fileName = URLEncoder.encode("用户订单数据导出", Charsets.UTF_8.name());
		response.setHeader("Content-disposition", "attachment;filename=" + fileName+date + ".xlsx");
		//stirng 转 data
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if (Func.isNotEmpty(Orders.get("createTime"))) {
			Date createTime = format.parse(String.valueOf(Orders.get("createTime")));
			//修改value值
			if (Orders.containsKey("createTime")){
				Orders.put("createTime",createTime);
			}
		}
		if (Func.isNotEmpty(Orders.get("endTime"))) {
			Date endTime = format.parse(String.valueOf(Orders.get("endTime")));
			if (Orders.containsKey("endTime")) {
				Orders.put("endTime", endTime);
			}
		}
		ordersService.exportOrders(response , Orders);
	}

	/**
	 * 导出用户
	 */
	@SneakyThrows
	@GetMapping("export-lineOrder")
	@ApiOperationSupport(order = 15)
	@ApiOperation(value = "导出用户订单数据", notes = "传入orders")
	public void exportLineOrders(@ApiIgnore @RequestParam Map<String, Object> Orders, HttpServletResponse response) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String date=simpleDateFormat.format(new Date());
		Orders.remove("crazy-auth");
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding(Charsets.UTF_8.name());
		String fileName = URLEncoder.encode("面授订单数据导出", Charsets.UTF_8.name());
		response.setHeader("Content-disposition", "attachment;filename=" + fileName+date + ".xlsx");
		ordersService.exportLineOrders(response , Orders);
	}

	/**
	 * 自定义分页 课程订单表
	 */
	@GetMapping("/statistics/page")
	@ApiOperationSupport(order = 14)
	@ApiOperation(value = "查询当前月的用户订单数据", notes = "传入orders")
	public R<IPage<OrdersVO>> statisticsPage(OrdersVO orders, Query query) {
		String year="";
		String month="";
		String yaerLast = "";
		String monthLast = "";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if(Func.isNull(orders.getDateStatis())){
			String date=simpleDateFormat.format(new Date());
			//当前时间的年
			year=date.substring(0,4);
			//获取当前的月份
			month=date.substring(5,7);
		}else {
			String date=simpleDateFormat.format(orders.getDateStatis());
			//当前时间的年
			year=date.substring(0,4);
			//获取当前的月份
			month=date.substring(5,7);
		}
		try {
			int years=Integer.valueOf(year);
			int months=Integer.valueOf(month);
			if(months==12){
				yaerLast=Integer.toString(years+1);
				monthLast="01";
			}else {
				yaerLast=Integer.toString(years);
				monthLast=Integer.toString(months+1);
			}
			orders.setCreateTimeStatis(simpleDateFormat.parse(year+"-"+month+"-"+"01"));
			orders.setEndTimeStatis(simpleDateFormat.parse(yaerLast+"-"+monthLast+"-"+"01"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		query.setSize(40);
		IPage<OrdersVO> pages = ordersService.selectOrderStatisticsPage(Condition.getPage(query), orders);
		return R.data(pages);
	}

	/**
	 * 自定义分页 课程订单表
	 */
	@GetMapping("/waiStatistics/page")
	@ApiOperationSupport(order = 14)
	@ApiOperation(value = "查询当前月的用户订单数据", notes = "传入orders")
	public R<IPage<OrdersVO>> waiStatisticsPage(OrdersVO orders, Query query) {
		String year="";
		String month="";
		String yaerLast = "";
		String monthLast = "";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if(Func.isNull(orders.getDateStatis())){
			String date=simpleDateFormat.format(new Date());
			//当前时间的年
			year=date.substring(0,4);
			//获取当前的月份
			month=date.substring(5,7);
		}else {
			String date=simpleDateFormat.format(orders.getDateStatis());
			//当前时间的年
			year=date.substring(0,4);
			//获取当前的月份
			month=date.substring(5,7);
		}
		try {
			int years=Integer.valueOf(year);
			int months=Integer.valueOf(month);
			if(months==12){
				yaerLast=Integer.toString(years+1);
				monthLast="01";
			}else {
				yaerLast=Integer.toString(years);
				monthLast=Integer.toString(months+1);
			}
			orders.setCreateTimeStatis(simpleDateFormat.parse(year+"-"+month+"-"+"01"));
			orders.setEndTimeStatis(simpleDateFormat.parse(yaerLast+"-"+monthLast+"-"+"01"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		query.setSize(40);
		IPage<OrdersVO> pages = ordersService.selectOrderStatisticsPage(Condition.getPage(query), orders);
		return R.data(pages);
	}

}
