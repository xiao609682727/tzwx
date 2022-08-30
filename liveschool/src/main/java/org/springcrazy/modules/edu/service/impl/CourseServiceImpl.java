
package org.springcrazy.modules.edu.service.impl;

import com.alibaba.excel.util.DateUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.api.client.util.Lists;
import org.apache.commons.lang.math.NumberUtils;
import org.springcrazy.common.tool.CommonUtil;
import org.springcrazy.core.log.exception.ServiceException;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.secure.utils.SecureUtil;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.core.tool.utils.DateUtil;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.core.tool.utils.ObjectUtil;
import org.springcrazy.modules.agent.entity.AgentUserAccountHistory;
import org.springcrazy.modules.agent.service.IAgentAccountService;
import org.springcrazy.modules.edu.entity.Course;
import org.springcrazy.modules.edu.entity.Orders;
import org.springcrazy.modules.edu.entity.TrxorderDetail;
import org.springcrazy.modules.edu.mapper.CourseMapper;
import org.springcrazy.modules.edu.service.ICourseService;
import org.springcrazy.modules.edu.service.IOrdersService;
import org.springcrazy.modules.edu.service.ITrxorderDetailService;
import org.springcrazy.modules.edu.vo.CourseVO;
import org.springcrazy.modules.lineclass.entity.LineclassEnroll;
import org.springcrazy.modules.lineclass.service.ILineclassEnrollService;
import org.springcrazy.modules.system.excel.GiveCourseExcel;
import org.springcrazy.modules.user.entity.Student;
import org.springcrazy.modules.user.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 课程表 服务实现类
 *
 * @author TongZhou
 * @since 2020-04-25
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

	@Autowired
	IStudentService studentService;
	@Autowired
	IOrdersService ordersService;
	@Autowired
	ITrxorderDetailService trxorderDetailService;
	@Autowired
	ILineclassEnrollService lineclassEnrollService;


	@Override
	public IPage<CourseVO> selectCoursePage(IPage<CourseVO> page, CourseVO course) {
		return page.setRecords(baseMapper.selectCoursePage(page, course));
	}

	@Override
	public void giveCourse(String userIds, String courseIds) {
		//获取要赠送的课程列表
		List<Course> courseList = this.listByIds(Func.toIntList(courseIds));
		//获取要被赠送的学生列表
		List<Student> studentList = studentService.listByIds(Func.toIntList(userIds));
		//创建记录
		studentList.forEach(student -> courseOrder(student,courseList));
	}
	@Override
	@Transactional(rollbackFor = {RuntimeException.class, Error.class})
	public void openCourse(String userIds, String courseIds) {
		//获取要赠送的课程列表
		List<Course> courseList = this.listByIds(Func.toIntList(courseIds));
		//获取要被赠送的学生列表
		List<Student> studentList = studentService.listByIds(Func.toIntList(userIds));
		//创建记录
		studentList.forEach(student -> openCourseOrder(student,courseList));
	}
	@Override
	public Date getAuthTime(Course course) {
		Date authTime = null;
		// 时间段
		if (Func.equals(course.getLosetype(),"0")) {
			authTime = course.getEndTime();
		}
		// 时间点
		if (Func.equals(course.getLosetype(),"1")) {
			authTime = DateUtil.plusDays(DateUtil.now(),Func.toInt(course.getLoseTime()));
		}
		return authTime;
	}

	/*单个赠送课程*/
	public void courseOrder(Student student, List<Course> courseList){
		String orderType = "COURSE";
		for (Course course :courseList){
			//判断课程类型
			if (("2").equals(course.getSellType())) {
				orderType = "LIVE";
			}
			if (("3").equals(course.getSellType())) {
				orderType = "PACKAGE";
			}
			if (("4").equals(course.getSellType())) {
				orderType = "LINECLASS";
			}
		}
		//获取后台id
		Integer userId = SecureUtil.getUserId();
		//赠送课程
		BigDecimal zero = new BigDecimal(0);
		//创建订单
		Orders order = Orders.builder().userId(student.getId()).orderNo(CommonUtil.getOrderNum()).sumMoney(zero).states(Orders.STATES_SUCCESS)
				.payTime(DateUtil.now()).createTime(DateUtil.now()).sysUserId(userId).payType(Orders.PAYTYPE_BACKGIVEPAY).reqChannel("web").orderAmount(zero).cashAmount(zero)
				.vmAmount(zero).backAmount(zero).orderType(orderType).build();
		ordersService.save(order);
		//创建订单详情
		List<TrxorderDetail> list = Lists.newArrayList();
		courseList.stream().forEach(course -> {
			TrxorderDetail.TrxorderDetailBuilder builder = TrxorderDetail.builder();
			builder.userId(student.getId()).courseId(course.getId()).trxorderId(order.getId()).loseAbsTime(course.getEndTime()).loseTime(course.getLoseTime())
					.orderNo(order.getOrderNo()).losetype(course.getLosetype()).payTime(order.getPayTime()).sourcePrice(zero)
					.couponPrice(zero).currentPrice(zero).courseName(course.getCourseName()).authStatus(TrxorderDetail.STATUS_SUCCESS)
					.remindStatus("init").trxorderType(("1").equals(course.getSellType())?"COURSE":("2").equals(course.getSellType())?"LIVE":("4").equals(course.getSellType())?"LINECLASS":"PACKAGE").beginTime(DateUtil.now()).createTime(DateUtil.now()).lastUpdateTime(DateUtil.now()).delStatus("1");
			builder.description(course.getTitle());
			// 到期时间
			if (Func.equals(course.getLosetype(),"0")) {
				Date authDate = course.getEndTime();
				builder.authTime(authDate);
			}
			// 按天数计算
			if (Func.equals(course.getLosetype(),"1")) {//
				// 按所写时间推移过期时间
				Calendar now = Calendar.getInstance();
				now.setTime(DateUtil.now());
				now.set(Calendar.DATE, now.get(Calendar.DATE) + Integer.valueOf(course.getLoseTime()));
				Date authDate = now.getTime();
				builder.authTime(authDate);
			}
			TrxorderDetail trxorderDetail = builder.build();
			list.add(trxorderDetail);
			/*如果赠送的是面授课程需要创建未报名的报名记录。*/
			if (("4").equals(course.getSellType())) {
				LineclassEnroll lineclassEnroll = new LineclassEnroll();
				lineclassEnroll.setCourseId (course.getId());
				lineclassEnroll.setUserId(student.getId());
				LineclassEnroll lineclassEnrollIs=lineclassEnrollService.getOne(Condition.getQueryWrapper(lineclassEnroll));
				if(ObjectUtil.isEmpty(lineclassEnrollIs)){
					lineclassEnroll.setTrxorderId(order.getId());
					lineclassEnroll.setState(3);
					lineclassEnrollService.saveOrUpdate(lineclassEnroll);
				}else {
					lineclassEnrollIs.setTrxorderId(order.getId());
					lineclassEnrollIs.setState(2);
					lineclassEnrollService.saveOrUpdate(lineclassEnrollIs);
				}
			}
		});
		trxorderDetailService.saveBatch(list);
		//发送课程消息

	}

	@Autowired
	private IAgentAccountService agentAccountService;
	public void openCourseOrder(Student student, List<Course> courseList){

		if(Func.isNull(courseList)){
			return;
		}
		for (Course course :courseList){
			String orderType = "COURSE";
			//课程类型
			if (("1").equals(course.getSellType())) {
				orderType = "COURSE";
			}
			//直播课程类型
			if (("2").equals(course.getSellType())) {
				orderType = "LIVE";
			}
			//套餐课程
			if (("3").equals(course.getSellType())) {
				orderType = "PACKAGE";
			}
			//面授
			if (("4").equals(course.getSellType())) {
				orderType = "LINECLASS";
			}
			//后台登录用户id
			Integer userId = SecureUtil.getUserId();
			//赠送课程
			BigDecimal courseCurrentPrice = course.getCurrentPrice();
			BigDecimal zero = new BigDecimal(0);
			//创建订单
			Orders order = Orders.builder()
					.userId(student.getId())//开通课程的用户id
					.orderNo(CommonUtil.getOrderNum())//订单号
					.sumMoney(courseCurrentPrice)//订单总价
					.states(Orders.STATES_SUCCESS)//订单状态
					.payTime(DateUtil.now())//支付时间
					.createTime(DateUtil.now())//创建时间
					.sysUserId(userId)//操作人
					.payType(Orders.PAYTYPE_AGENT_OPEN_PAY)//支付类型：代理商开通类型订单
					.reqChannel("web")//请求渠道(web,app)
					.orderAmount(courseCurrentPrice)//订单原始金额
					.cashAmount(zero)//实际支付的cash金额
					.vmAmount(zero)//实际支付的vm金额
					.backAmount(zero)//实际支付的返现金额
					.orderType(orderType)
					.build();
			//添加订单
			ordersService.save(order);
			//添加流水
			TrxorderDetail.TrxorderDetailBuilder builder = TrxorderDetail.builder();
			//添加学生id
			builder.userId(student.getId())
					.courseId(course.getId())//课程id
					.trxorderId(order.getId())//订单id
					.orderNo(order.getOrderNo())//订单号
					.losetype(course.getLosetype())//有效期类型（前台快照）
					.loseAbsTime(course.getEndTime())//课程有效期
					.loseTime(course.getLoseTime())//订单过期时间点（前台快照）
					.payTime(order.getPayTime())//支付成功时间
					.sourcePrice(zero)//原价
					.couponPrice(zero)//优惠价格
					.currentPrice(courseCurrentPrice)//支付价格
					.courseName(course.getCourseName())//课程名字
					.authStatus(TrxorderDetail.STATUS_SUCCESS)//流水状态，支付成功
					.remindStatus("init")//过期是否提醒 init 未提醒 already 已提醒
					.trxorderType(orderType)//1 点播课程 2 直播课程 3 套餐课程
					.beginTime(DateUtil.now())//商品开始时间
					.createTime(DateUtil.now())//创建时间
					.lastUpdateTime(DateUtil.now())//最后更新时间
					.delStatus("1");//个人订单中心删除课程 1 正常
			builder.description(course.getTitle());
			// 到期时间
			if (Func.equals(course.getLosetype(),"0")) {
				Date authDate = course.getEndTime();
				builder.authTime(authDate);
			}
			// 按天数计算
			if (Func.equals(course.getLosetype(),"1")) {//
				// 按所写时间推移过期时间
				Calendar now = Calendar.getInstance();
				now.setTime(DateUtil.now());
				now.set(Calendar.DATE, now.get(Calendar.DATE) + Integer.valueOf(course.getLoseTime()));
				Date authDate = now.getTime();
				builder.authTime(authDate);
			}
			TrxorderDetail trxorderDetail = builder.build();
			//添加流水
			trxorderDetailService.save(trxorderDetail);
			//扣除代理商的账户上的钱
			agentAccountService.addMoney(userId,courseCurrentPrice, "3", AgentUserAccountHistory.BIZTYPE_COURSE,order);
		}

	}

	public Orders addCourseOrder(Student student, List<Course> courseList,String payType){
		String orderType = "COURSE";
		for (Course course :courseList){
			//判断课程类型
			if (("2").equals(course.getSellType())) {
				orderType = "LIVE";
			}
			if (("3").equals(course.getSellType())) {
				orderType = "PACKAGE";
			}
			if (("4").equals(course.getSellType())) {
				orderType = "LINECLASS";
			}
		}
		Integer userId = SecureUtil.getUserId();
		//赠送课程
		BigDecimal zero = new BigDecimal(0);
		//创建订单
		Orders order = Orders.builder().userId(student.getId()).orderNo(CommonUtil.getOrderNum()).sumMoney(zero).states(Orders.STATES_SUCCESS)
				.payTime(DateUtil.now()).createTime(DateUtil.now()).sysUserId(userId).payType(payType).reqChannel("web").orderAmount(zero).cashAmount(zero)
				.vmAmount(zero).backAmount(zero).orderType(orderType).build();
		ordersService.save(order);
		//创建订单详情
		List<TrxorderDetail> list = Lists.newArrayList();
		courseList.stream().forEach(course -> {
			TrxorderDetail.TrxorderDetailBuilder builder = TrxorderDetail.builder();
			builder.userId(student.getId()).courseId(course.getId()).trxorderId(order.getId()).loseAbsTime(course.getEndTime()).loseTime(course.getLoseTime())
					.orderNo(order.getOrderNo()).losetype(course.getLosetype()).payTime(order.getPayTime()).sourcePrice(zero)
					.couponPrice(zero).currentPrice(zero).courseName(course.getCourseName()).authStatus(TrxorderDetail.STATUS_SUCCESS)
					.remindStatus("init").trxorderType(("1").equals(course.getSellType())?"COURSE":("2").equals(course.getSellType())?"LIVE":"PACKAGE").beginTime(DateUtil.now()).createTime(DateUtil.now()).lastUpdateTime(DateUtil.now()).delStatus("1");
			builder.description(course.getTitle());
			// 到期时间
			if (Func.equals(course.getLosetype(),"0")) {
				Date authDate = course.getEndTime();
				builder.authTime(authDate);
			}
			// 按天数计算
			if (Func.equals(course.getLosetype(),"1")) {//
				// 按所写时间推移过期时间
				Calendar now = Calendar.getInstance();
				now.setTime(DateUtil.now());
				now.set(Calendar.DATE, now.get(Calendar.DATE) + Integer.valueOf(course.getLoseTime()));
				Date authDate = now.getTime();
				builder.authTime(authDate);
			}
			TrxorderDetail trxorderDetail = builder.build();
			list.add(trxorderDetail);
		});
		trxorderDetailService.saveBatch(list);
		//发送课程消息
		return order;
	}
	@Override
	public Course getCourseById(int courseId) {
		return baseMapper.getCourseById(courseId);
	}

	/**
	 * 生成订单号 当前用户id+毫秒数
	 */
	public String getOrderNum(Long userId) {
		return userId + DateUtils.format(new Date(), "yyyyMMddHHmmssSSS");
	}

	@Override
	public void importGiveCourse(List<GiveCourseExcel> data) {
		if (data.size() == 0){
			throw new ServiceException("文件无数据加载！！！");
		}

		int i = 1 ; //行数
		int num = 0 ; // key值个数

		Map<String , Object> map = new HashMap();
		String errMsg = "";//错误信息

		for (GiveCourseExcel giveCourseExcel : data){
			Student temp = new Student();
			//获取Excel中的数据
			Student student = Objects.requireNonNull(BeanUtil.copy(giveCourseExcel, Student.class));

			//验证
			if (Func.isNotEmpty(giveCourseExcel.getUserName()) || Func.isNotEmpty(giveCourseExcel.getMobile()) ||
					Func.isNotEmpty(giveCourseExcel.getEmail()) || Func.isNotEmpty(giveCourseExcel.getUserId()) ||
					Func.isNotEmpty(giveCourseExcel.getCourseIds()) ) {
				//查库
				int userId = 0;

				if (Func.isNotEmpty(student.getUserName())){
					Student tempUserName = studentService.checkStudentUserName(student);
					if(Func.isNull(tempUserName)){
						errMsg += "第" + i + "行，账号不存在;<br />";
					}else {
						userId = tempUserName.getId();
					}
				}

				if (Func.isNotEmpty(student.getMobile())) {
					Student tempMobile = studentService.checkStudentMobile(student);
					if (Func.isNull(tempMobile)) {
						errMsg += "第" + i + "行，手机号不存在;<br />";
					} else if (userId != 0 && userId != tempMobile.getId()) {
						errMsg += "第" + i + "行，账号/手机号/邮箱/用户id 不是同一用户;<br />";
					} else {
						userId = tempMobile.getId();
					}
				}

				if (Func.isNotEmpty(student.getEmail())) {
					Student tempEmail = studentService.checkStudentEmail(student);
					if (Func.isNull(tempEmail)) {
						errMsg += "第" + i + "行，邮箱不存在;<br />";
					} else if (userId != 0 && userId != tempEmail.getId()) {
						errMsg += "第" + i + "行，账号/手机号/邮箱/用户id 不是同一用户;<br />";
					} else {
						userId = tempEmail.getId();
					}
				}

				if (Func.isNotEmpty(giveCourseExcel.getUserId())) {
					//查询用户是否被冻结 或 被删除
					Student tempUserId = studentService.getById(giveCourseExcel.getUserId());
					if (Func.isNull(tempUserId)){
						errMsg += "第" + i + "行，没有用户id是\""+giveCourseExcel.getUserId()+"\";<br />";
					}else if (userId != 0 && userId != giveCourseExcel.getUserId()) {
						errMsg += "第" + i + "行，账号/手机号/邮箱/用户id 不是同一用户;<br />";
					}else {
						userId = giveCourseExcel.getUserId();
					}
				}

				if (userId == 0){
					errMsg += "第" + i + "行，请填写用户信息;<br />";
					i++ ; continue;
				}else {
					//查询用户是否被冻结 或 被删除
					temp = studentService.getById(userId);
					if (temp.getIsAvalible() != 2) {
						errMsg += "第" + i + "行，该用户已被冻结，无法赠课;<br />";
						i++ ; continue;
					}
				}
				//课程id
				if (Func.isNull(giveCourseExcel.getCourseIds())){
					errMsg += "第" + i + "行，赠送(套餐)课程id不可为空;<br />";
					i++ ; continue;
				}
				if (giveCourseExcel.getCourseIds().contains("，")){
					errMsg += "第" + i + "行，课程id间隔请使用英文\",\";<br />";
					i++ ; continue;
				}

				String[] cours = giveCourseExcel.getCourseIds().split(",");

				for (int courNum = 0 ; courNum < cours.length ; courNum++){
					List<Course> courseList = new ArrayList<>();//赠送课程集合
					//判断是否可以转成int类型
					if(!NumberUtils.isDigits(cours[courNum])){
						errMsg += "第" + i + "行，课程id间隔请使用英文\",\";<br />";
						continue;
					}
					//查询课程id是否有课程
					Course course = baseMapper.getCourseById(Integer.valueOf(cours[courNum]));
					//检测课程（套餐）id
					if (Func.isNull(course)){
						errMsg += "第" + i + "行，课程id为" + cours[courNum] + "的课程不存在;<br />";
						continue;
					}else {
						//被删除的课程是否参与赠送
						if (course.getIsDeleted() == 1) {
							errMsg += "第" + i + "行，课程id为" + cours[courNum] + "的课程已被删除;<br />";
							continue;
						}
					}
					courseList.add(course);

					map.put("userId" + num , temp);
					map.put("courseList"+num ,courseList);
					num++;
				}
				i++;
			}else {
				return;
			}
		}


		if (Func.equals(errMsg,"")) {
			for (int y = 0; y < map.size() / 2; y++) {
				courseOrder((Student) map.get("userId" + y), (List<Course>) map.get("courseList" + y));
			}
		}else {
			throw new ServiceException(errMsg);
		}
	}
}
