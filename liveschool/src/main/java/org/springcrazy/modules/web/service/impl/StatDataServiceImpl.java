
package org.springcrazy.modules.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.api.client.util.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import org.springcrazy.common.cache.CacheNames;
import org.springcrazy.core.tool.jackson.JsonUtil;
import org.springcrazy.core.tool.utils.DateUtil;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.core.tool.utils.RedisUtil;
import org.springcrazy.modules.edu.entity.Orders;
import org.springcrazy.modules.edu.service.IOrdersService;
import org.springcrazy.modules.user.entity.Student;
import org.springcrazy.modules.user.service.IStudentService;
import org.springcrazy.modules.web.entity.Area;
import org.springcrazy.modules.web.entity.StatData;
import org.springcrazy.modules.web.mapper.StatDataMapper;
import org.springcrazy.modules.web.service.IAreaService;
import org.springcrazy.modules.web.service.IStatDataService;
import org.springcrazy.modules.web.vo.StatDataVO;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  服务实现类
 *
 * @author TongZhou
 * @since 2020-05-19
 */
@Service
@AllArgsConstructor
public class StatDataServiceImpl extends ServiceImpl<StatDataMapper, StatData> implements IStatDataService {

	private RedisUtil redisUtil;
	private IAreaService areaService;
	private IStudentService studentService;
	private IOrdersService ordersService;

	@Override
	public IPage<StatDataVO> selectStatDataPage(IPage<StatDataVO> page, StatDataVO statData) {
		return page.setRecords(baseMapper.selectStatDataPage(page, statData));
	}


	/**
	 * 按照省份查询用户分布
	 */
	@Override
	public void updateUserDataByArea() {
		//查询所有省份
		Area area = new Area();
		area.setParent(0);
		List<Area> list = areaService.list(new QueryWrapper<Area>(area));
		//分组查询所有用户按照省份
		List<Map<String, String>> areaList = baseMapper.selectStudentByArea();
		Integer total = 0;
		List<Map<String, String>> areadata = Lists.newArrayList();
		for (Area a : list) {
			Map<String, String> userMap = Maps.newHashMap();
			userMap.put("name", a.getName());
			userMap.put("value", "0");
			for (Map<String, String> m : areaList) {
				if(Func.equals(a.getId(),m.get("province"))){
					int count = Func.toInt(m.get("count"));
					total += count;
					userMap.put("value", count+"");
					break;
				}
			}
			areadata.add(userMap);
		}
		Map<String,Object> data = Maps.newHashMap();
		data.put("total",total);
		data.put("areadata",areadata);

		StatData statData = new StatData();
		statData.setCode(StatData.AREADATA);
		StatData one = getOne(new QueryWrapper<StatData>(statData));
		if(!Func.isNull(one)){
			statData = one;
		}
		statData.setData(JsonUtil.toJson(data));
		statData.setDay(DateUtil.today());
		statData.setUpdateTime(DateUtil.now());
		saveOrUpdate(statData);
	}

	/**
	 * 更新后台首页统计
	 */
	@Override
	public void updateAdminIndexData() {
		Map<String,Object> json = Maps.newHashMap();
		json.put("span",4);
		List<Map<String,Object>> data = Lists.newArrayList();
		//新增访客
		String dateStr = DateUtil.format(DateUtil.now(), DateUtil.PATTERN_DATE);
		Set<Object> view = redisUtil.sGet(CacheNames.VIEWLOG+"_"+ dateStr);
		Map<String,Object> viewData = Maps.newHashMap();
		viewData.put("title","新增访客");
		viewData.put("subtitle","今日");
		viewData.put("count",view.size());
		viewData.put("color","rgb(27, 201, 142)");
		viewData.put("key","访");
		data.add(viewData);
		//新增注册
		//全部用户
		int count = studentService.count();
		//查询今日新增
		QueryWrapper<Student> query = new QueryWrapper<Student>();
		query.apply("date_format(create_time,'%Y%m%d') = {0}",DateUtil.today());
		int daycount = studentService.count(query);
		Map<String,Object> registerData = Maps.newHashMap();
		registerData.put("title","新增注册");
		registerData.put("subtitle","今日");
		registerData.put("count",daycount);
		registerData.put("allcount",count);
		registerData.put("text","总计人数");
		registerData.put("color","rgb(230, 71, 88)");
		registerData.put("key","注");
		data.add(registerData);
		//新增订单
		//全部订单
		int ordercount = ordersService.count();
		//查询订单今日新增
		QueryWrapper<Orders> orderquery = new QueryWrapper<Orders>();
		orderquery.apply("date_format(create_time,'%Y%m%d') = {0}",DateUtil.today());
		int orderdaycount = ordersService.count(orderquery);
		Map<String,Object> orderData = Maps.newHashMap();
		orderData.put("title","新增订单");
		orderData.put("subtitle","今日");
		orderData.put("count",orderdaycount);
		orderData.put("allcount",ordercount);
		orderData.put("text","总计");
		orderData.put("color","rgb(178, 159, 255)");
		orderData.put("key","订");
		data.add(orderData);
		//新增收入
		Map<String,Object> params = Maps.newHashMap();
		Map<String, Object> resultMap = ordersService.selectOrderMoney(params);
		Object sumMoney = resultMap.get("sum_money");
		params.put("createTime",DateUtil.today());
		resultMap = ordersService.selectOrderMoney(params);
		Object datasumMoney = 0;
		if(Func.isNotEmpty(resultMap)){
			datasumMoney = resultMap.get("sum_money");
		}
		Map<String,Object> moneyData = Maps.newHashMap();
		moneyData.put("title","新增收入");
		moneyData.put("subtitle","今日");
		moneyData.put("count",datasumMoney);
		moneyData.put("allcount",sumMoney);
		moneyData.put("text","总计");
		moneyData.put("color","#FF6600");
		moneyData.put("key","收");
		data.add(moneyData);
		//保存记录
		StatData statData = new StatData();
		statData.setCode(StatData.STATADMINDATA);
		StatData one = getOne(new QueryWrapper<StatData>(statData));
		if(!Func.isNull(one)){
			statData = one;
		}
		statData.setData(JsonUtil.toJson(data));
		statData.setDay(DateUtil.today());
		statData.setUpdateTime(DateUtil.now());
		saveOrUpdate(statData);
	}

	@Override
	public void updateUserLog() {
		List<String> dateList = Lists.newArrayList();
		List<Integer> viewDataList = Lists.newArrayList();
		List<Integer> loginDataList = Lists.newArrayList();

		for (int i = 30; i > 0; i--) {
			Date date = DateUtil.minusDays(DateUtil.now(), i);
			String dateStr = DateUtil.format(date, DateUtil.PATTERN_DATE);
			Set<Object> viewSet = redisUtil.sGet(CacheNames.VIEWLOG + "_" + dateStr);
			Set<Object> loginSet = redisUtil.sGet(CacheNames.LOGINLOG + "_" + dateStr);
			int viewSize = 0;
			int loginSize = 0;
			if(Func.isNotEmpty(viewSet)){
				viewSize = viewSet.size();
			}
			if(Func.isNotEmpty(loginSet)){
				loginSize = loginSet.size();
			}
			dateList.add(dateStr);
			viewDataList.add(viewSize);
			loginDataList.add(loginSize);
		}
		Map<String,Object> result = Maps.newHashMap();
		result.put("data",dateList);
		result.put("viewDataList",viewDataList);
		result.put("loginDataList",loginDataList);
		//保存记录
		StatData statData = new StatData();
		statData.setCode(StatData.STATADMINUSERDATA);
		StatData one = getOne(new QueryWrapper<StatData>(statData));
		if(!Func.isNull(one)){
			statData = one;
		}
		statData.setData(JsonUtil.toJson(result));
		statData.setDay(DateUtil.today());
		statData.setUpdateTime(DateUtil.now());
		saveOrUpdate(statData);
	}


}
