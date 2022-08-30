
package org.springcrazy.modules.edu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springcrazy.common.tool.CourseUtils;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.tool.utils.DateUtil;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.core.tool.utils.ObjectUtil;
import org.springcrazy.modules.edu.dto.OrdersDTO;
import org.springcrazy.modules.edu.entity.Course;
import org.springcrazy.modules.edu.entity.Orders;
import org.springcrazy.modules.edu.entity.TrxorderDetail;
import org.springcrazy.modules.edu.mapper.CourseMapper;
import org.springcrazy.modules.edu.mapper.OrdersMapper;
import org.springcrazy.modules.edu.service.IOrdersService;
import org.springcrazy.modules.edu.service.ITrxorderDetailService;
import org.springcrazy.modules.edu.vo.OrdersVO;
import org.springcrazy.modules.lineclass.entity.LineclassEnroll;
import org.springcrazy.modules.lineclass.service.ILineclassEnrollService;
import org.springcrazy.modules.system.excel.ExportOrderExcel;
import org.springcrazy.modules.system.excel.ExportOrdersExcel;
import org.springcrazy.modules.user.entity.UserAccountHistory;
import org.springcrazy.modules.user.service.IUserAccountService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 课程订单表 服务实现类
 *
 * @author TongZhou
 * @since 2020-05-09
 */
@Service
@AllArgsConstructor
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {

	private ITrxorderDetailService trxorderDetailService;

	private IUserAccountService userAccountService;

	private CourseMapper courseMapper;

	private ILineclassEnrollService lineclassEnrollService;

	@Override
	public IPage<OrdersVO> selectOrdersPage(IPage<OrdersVO> page, OrdersVO orders) {
		return page.setRecords(baseMapper.selectOrdersPage(page, orders));
	}

	@Override
	public IPage<OrdersVO> selectOrdersPageList(IPage<OrdersVO> page, OrdersVO orders) {
		return page.setRecords(baseMapper.selectOrdersPageList(page, orders));
	}

	@Override
	public IPage<OrdersVO> selectOrderStatisticsPage(IPage<OrdersVO> page, OrdersVO orders)  {
		String year="";
		String month="";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date=simpleDateFormat.format(orders.getCreateTimeStatis());
		//当前时间的年
		year=date.substring(0,4);
		//获取当前的月份
		month=date.substring(5,7);
		List<OrdersVO> ordersVOList = baseMapper.selectOrderStatisticsPage(orders);
		List<String> dateList= new ArrayList<>();
		List<OrdersVO> ordersNewList = new ArrayList<>();
		try {
		int days=0;
		if(Integer.valueOf(month)==1||Integer.valueOf(month)==3||Integer.valueOf(month)==5||Integer.valueOf(month)==7||Integer.valueOf(month)==8||Integer.valueOf(month)==10||Integer.valueOf(month)==12){
			days=31;
		}else if(Integer.valueOf(month)==4||Integer.valueOf(month)==6||Integer.valueOf(month)==9||Integer.valueOf(month)==11){
			days=30;
		}else if(Integer.valueOf(month)==2){
			if(Integer.valueOf(year)%4==0 && Integer.valueOf(year)%100!=0){
				days=29;
			}else if(Integer.valueOf(year)%400==0){
				days=29;
			}else {
				days=28;
			}
		}
		for(int i=1;i<=days;i++){
			String daty="";
			if(i<10){
				daty="0"+i;
			}else {
				daty=i+"";
			}
			OrdersVO ordersVO = new OrdersVO();
			String time =year+"-"+month+"-"+daty;
			ordersVO.setCreateTime(simpleDateFormat.parse(time));
			int y=0;
			for(OrdersVO vo:ordersVOList){
				String voYear = simpleDateFormat.format(vo.getCreateTime()).substring(0,4)+"-"+simpleDateFormat.format(vo.getCreateTime()).substring(5,7)+"-"+simpleDateFormat.format(vo.getCreateTime()).substring(8,10);
				if(voYear.equals(time)){
					ordersVO.setVolume(vo.getVolume());
					ordersVO.setActualIncome(vo.getActualIncome());
					y=1;
				}
			}
			if(y==0){
				ordersVO.setVolume("0");
				ordersVO.setActualIncome("0");
			}
			ordersNewList.add(ordersVO);
		}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return page.setRecords(ordersNewList);
	}

	@Override
	public boolean updateOrder(Orders orders) {
		//修改订单状态
		baseMapper.updateById(orders);
		//修改订单详情状态
		TrxorderDetail trxorderDetail = TrxorderDetail.builder().build();
		trxorderDetail.setTrxorderId(orders.getId());
		List<TrxorderDetail> trxorderDetailList = trxorderDetailService.list(Condition.getQueryWrapper(trxorderDetail));
		for(TrxorderDetail trxorderDetail1:trxorderDetailList){
			trxorderDetail1.setAuthStatus(orders.getStates());
			if(("LINECLASS").equals(orders.getOrderType())){
				LineclassEnroll lineclassEnrollIs= new LineclassEnroll();
				lineclassEnrollIs.setUserId(orders.getUserId());
				lineclassEnrollIs.setCourseId(trxorderDetail1.getCourseId());
				lineclassEnrollIs =lineclassEnrollService.getOne(Condition.getQueryWrapper(lineclassEnrollIs));
				lineclassEnrollIs.setState(2);
				lineclassEnrollIs.setTrxorderId(orders.getId());
				lineclassEnrollService.saveOrUpdate(lineclassEnrollIs);
			}
		}
		//批量更新订单详情状态
		return trxorderDetailService.updateBatchById(trxorderDetailList);
	}

	@Override
	public boolean createOrder(OrdersDTO order) {
		BigDecimal totalAmount = new BigDecimal(0);
		List<TrxorderDetail> list = order.getList();
		for (TrxorderDetail detail : list) {
			detail.setTrxorderId(order.getId());
			totalAmount = totalAmount.add(detail.getCurrentPrice());
		}
		order.setSumMoney(totalAmount);
		this.save(order);
		//更新订单id
		list.forEach(detail ->{
			detail.setTrxorderId(order.getId());
		});
		trxorderDetailService.saveBatch(list);
		return false;
	}

	@Override
	public boolean accountPay(Integer orderId) {
		//查询订单
		Orders orders = baseMapper.selectById(orderId);
		//储值扣款
		userAccountService.money(orders.getUserId(),orders.getOrderAmount(),"2", UserAccountHistory.BIZTYPE_COURSE,"储值消费",orders.getOrderNo());
		//设置订单状态 设置支付类型
		orders.setPayType(Orders.PAYTYPE_MONEYPAY);
		orders.setStates(Orders.STATES_SUCCESS);
		orders.setPayTime(DateUtil.now());
		baseMapper.updateById(orders);
		//修改订单详情
		TrxorderDetail trxorderDetail = new TrxorderDetail();
		trxorderDetail.setTrxorderId(orderId);
		List<TrxorderDetail> list = trxorderDetailService.list(new QueryWrapper<>(trxorderDetail));
		for (TrxorderDetail detail : list) {
			//设置订单状态
			detail.setAuthStatus(TrxorderDetail.STATUS_SUCCESS);
			//设置课程过期时间
			detail.setBeginTime(DateUtil.now());
			Course course = new Course();
			course.setLoseTime(detail.getLoseTime());
			course.setLosetype(detail.getLosetype());
			course.setEndTime(detail.getLoseAbsTime());
			detail.setAuthTime(CourseUtils.getAuthTime(course));
			detail.setPayTime(DateUtil.now());
			//处理销售量
			Course tcourse = courseMapper.selectById(detail.getCourseId());
			QueryWrapper<TrxorderDetail> queryWrapper = new QueryWrapper<TrxorderDetail>();
			queryWrapper.lambda().eq(TrxorderDetail::getCourseId,detail.getCourseId()).eq(TrxorderDetail::getAuthStatus,TrxorderDetail.STATUS_SUCCESS);
			int buyCount = trxorderDetailService.count(queryWrapper);
			tcourse.setPageBuycount(buyCount);
			courseMapper.updateById(tcourse);
			if("LINECLASS".equals(detail.getTrxorderType())){
				LineclassEnroll lineclassEnroll = new LineclassEnroll();
				lineclassEnroll.setUserId(detail.getUserId());
				lineclassEnroll.setCourseId(detail.getCourseId());
				lineclassEnroll.setTrxorderId(detail.getTrxorderId());
				lineclassEnrollService.updateLineClassEnrol(lineclassEnroll);
			}
		}

		trxorderDetailService.updateBatchById(list);
		return true;
	}

	@Override
	public Map<String, Object> selectOrderMoney(Map<String, Object> param) {
		return baseMapper.selectOrderMoney(param);
	}
	@Override
	public void exportOrder(HttpServletResponse response ,String startTime) {
		ExcelWriter excelWriter = null;
		try {

			//获取当前的月份
			int days = 0;
			String year=startTime.substring(0 , 4);
			String month=startTime.substring(5 , 7);
			if(Integer.valueOf(month)==1||Integer.valueOf(month)==3||Integer.valueOf(month)==5||Integer.valueOf(month)==7||Integer.valueOf(month)==8||Integer.valueOf(month)==10||Integer.valueOf(month)==12){
				days=31;
			}else if(Integer.valueOf(month)==4||Integer.valueOf(month)==6||Integer.valueOf(month)==9||Integer.valueOf(month)==11){
				days=30;
			}else if(Integer.valueOf(month)==2){
				if(Integer.valueOf(year)%4==0 && Integer.valueOf(year)%100!=0){
					days=29;
				}else if(Integer.valueOf(year)%400==0){
					days=29;
				}else {
					days=28;
				}
			}

			Integer size = 3000;
			excelWriter = EasyExcel.write(response.getOutputStream(), ExportOrderExcel.class).build();
			WriteSheet writeSheet = EasyExcel.writerSheet("用户订单数据表").build();
			//分页写入
			QueryWrapper<Orders> queryWrapper = Condition.getQueryWrapper(new Orders());
			Integer count = baseMapper.selectCount(queryWrapper);
			Page<ExportOrderExcel> page = new Page<>();

			List<ExportOrderExcel> list = new ArrayList<>();

			page.setCurrent(1);
			page.setSize(size);
			String endTime = year + "-" + month + "-" + days;//本月末最后一天
			list = baseMapper.exportOrder(page ,startTime , endTime);

			//新的表数据
			List<ExportOrderExcel> listNew = new ArrayList<>();
			//有数据记录的日期
			String dateTime = "";

			for (int w = 0 ; days > w ; w++){
				ExportOrderExcel exportOrderExcel = new ExportOrderExcel();
				listNew.add(w , exportOrderExcel);

				if ((w+1) < 10) {
					listNew.get(w).setCreateTimeStatis(year + "-" + month + "-" + 0 + (w+1));
				}else {
					listNew.get(w).setCreateTimeStatis(year  + "-" + month  + "-" + (w+1));
				}
				for (int z = 0; list.size() > z; z++) {
					dateTime = list.get(z).getCreateTimeStatis().substring(0, 10);
					if (dateTime.equals(listNew.get(w).getCreateTimeStatis())) {
						listNew.get(w).setActualIncome(list.get(z).getActualIncome());
						listNew.get(w).setVolume(list.get(z).getVolume());
						break;
					}
				}

				if (ObjectUtil.isEmpty(listNew.get(w).getVolume()) || ObjectUtil.isEmpty(listNew.get(w).getActualIncome())){
					listNew.get(w).setActualIncome(0 + "");
					listNew.get(w).setVolume(0 + "");
				}
			}
			excelWriter.write(listNew, writeSheet);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (excelWriter != null) {
				excelWriter.finish();
			}
		}
	}

	@Override
	public void exportOrders(HttpServletResponse response , Map<String, Object> Orders) {
		ExcelWriter excelWriter = null;
		try {
			Integer size = 3000;
			excelWriter = EasyExcel.write(response.getOutputStream(), ExportOrdersExcel.class).build();
			WriteSheet writeSheet = EasyExcel.writerSheet("用户订单数据表").build();
			//分页写入
			OrdersVO ordersVO = new OrdersVO();
			ordersVO.setOrderNo((String) Orders.get("orderNo"));
			ordersVO.setMobile((String) Orders.get("mobile"));
			ordersVO.setEmail((String) Orders.get("email"));
			ordersVO.setUserName((String) Orders.get("userName"));
			ordersVO.setStates((String) Orders.get("states"));
			ordersVO.setCreateTime(Orders.get("createTime").equals("") ? null : (Date) Orders.get("createTime"));
			ordersVO.setEndTime(Orders.get("endTime").equals("") ? null : (Date) Orders.get("endTime"));
			ordersVO.setPayType((String) Orders.get("payType"));
			ordersVO.setOrderType((String) Orders.get("orderType"));
			ordersVO.setCourseId(Integer.valueOf(Orders.get("courseId")+""));
			ordersVO.setUserId(Integer.valueOf(Orders.get("userId")+""));

			List<ExportOrdersExcel> list = baseMapper.exportOrders(null, ordersVO);

			if (list.size() <= size){
				excelWriter.write(list, writeSheet);
			}else {
				Page<ExportOrdersExcel> page = new Page<>();
				for (int i = 1; i <= (list.size() / size) + 1; i++) {
					page.setCurrent(i);
					page.setSize(size);
					list = baseMapper.exportOrders(page, ordersVO);
					excelWriter.write(list, writeSheet);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (excelWriter != null) {
				excelWriter.finish();
			}
		}
	}

	@Override
	public void exportLineOrders(HttpServletResponse response, Map<String, Object> Orders) {
		ExcelWriter excelWriter = null;
		try {
			Integer size = 3000;
			excelWriter = EasyExcel.write(response.getOutputStream(), ExportOrdersExcel.class).build();
			WriteSheet writeSheet = EasyExcel.writerSheet("用户订单数据表").build();
			//分页写入
			OrdersVO ordersVO = new OrdersVO();
			if(Func.isNotEmpty(Orders.get("states"))&&!("undefined").equals(Orders.get("states").toString())){
				ordersVO.setStates((String) Orders.get("states"));
			}
			ordersVO.setCreateTime(Orders.get("createTime").equals("") ? null : (Date) Orders.get("createTime"));
			ordersVO.setEndTime(Orders.get("endTime").equals("") ? null : (Date) Orders.get("endTime"));
			if(Func.isNotEmpty(Orders.get("payType"))&&!("undefined").equals(Orders.get("payType").toString())){
				ordersVO.setPayType((String) Orders.get("payType"));
			}
			ordersVO.setOrderType("LINECLASS");
			ordersVO.setCourseId(Integer.valueOf(Orders.get("courseId")+""));
			ordersVO.setUserId(Integer.valueOf(Orders.get("userId")+""));

			List<ExportOrdersExcel> list = baseMapper.exportOrders(null, ordersVO);

			if (list.size() <= size){
				excelWriter.write(list, writeSheet);
			}else {
				Page<ExportOrdersExcel> page = new Page<>();
				for (int i = 1; i <= (list.size() / size) + 1; i++) {
					page.setCurrent(i);
					page.setSize(size);
					list = baseMapper.exportOrders(page, ordersVO);
					excelWriter.write(list, writeSheet);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (excelWriter != null) {
				excelWriter.finish();
			}
		}
	}
}
