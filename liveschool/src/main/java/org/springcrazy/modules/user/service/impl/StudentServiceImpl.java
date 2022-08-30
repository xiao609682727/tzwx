
package org.springcrazy.modules.user.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.api.client.util.Lists;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang.math.NumberUtils;
import org.springcrazy.core.log.exception.ServiceException;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.secure.CrazyUser;
import org.springcrazy.core.secure.utils.SecureUtil;
import org.springcrazy.core.tool.utils.*;
import org.springcrazy.modules.edu.entity.Course;
import org.springcrazy.modules.edu.service.ICourseService;
import org.springcrazy.modules.system.entity.User;
import org.springcrazy.modules.system.entity.UserInfo;
import org.springcrazy.modules.system.excel.ExportStudentExcel;
import org.springcrazy.modules.system.excel.StudentExcel;
import org.springcrazy.modules.system.service.IUserService;
import org.springcrazy.modules.user.entity.Student;
import org.springcrazy.modules.user.entity.UserAccount;
import org.springcrazy.modules.user.entity.UserLoginLog;
import org.springcrazy.modules.user.mapper.StudentMapper;
import org.springcrazy.modules.user.service.IStudentService;
import org.springcrazy.modules.user.service.IUserAccountService;
import org.springcrazy.modules.user.service.IUserLoginLogService;
import org.springcrazy.modules.user.vo.StudentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 学员表 服务实现类
 *
 * @author TongZhou
 * @since 2020-04-28
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

	@Autowired
	private IUserAccountService userAccountService;
	@Autowired
	private IUserLoginLogService userLoginLogService;
	@Autowired
	private ICourseService courseService;
	@Autowired
	private IUserService userService;


	@Override
	public IPage<StudentVO> selectStudentPage(IPage<StudentVO> page, StudentVO student) {
		return page.setRecords(baseMapper.selectStudentPage(page, student));
	}

	@Override
	public UserInfo userInfo(Integer userId) {
		UserInfo userInfo = new UserInfo();
		Student student = baseMapper.selectById(userId);
		if(Func.isNull(student)){
			return null;
		}
		//封装 CrazyUser
		CrazyUser CrazyUser = new CrazyUser();
		CrazyUser.setUserId(student.getId());
		CrazyUser.setTenantId(null);
		CrazyUser.setAccount(student.getMobile());
		CrazyUser.setUserName(student.getRealName());
		CrazyUser.setTenantId("000000");
		CrazyUser.setMobile(student.getMobile());
		CrazyUser.setShowName(student.getShowName());
		CrazyUser.setEmail(student.getEmail());
		CrazyUser.setRealName(student.getRealName());
		userInfo.setUser(CrazyUser);
		//设置用户角色
		List<String> roleAlias = Lists.newArrayList();
		roleAlias.add("student");
		userInfo.setRoles(roleAlias);
		return userInfo;
	}

	@Override
	public UserInfo userInfos(Integer userId) {
		UserInfo userInfo = new UserInfo();
		Student student = baseMapper.userInfos(userId);
		if(Func.isNull(student)){
			return null;
		}
		//封装 CrazyUser
		CrazyUser CrazyUser = new CrazyUser();
		CrazyUser.setUserId(student.getId());
		CrazyUser.setTenantId(null);
		CrazyUser.setAccount(student.getMobile());
		if(student.getShowName()!=null&&!("").equals(student.getShowName())){
			CrazyUser.setUserName(student.getShowName());
		}else {
			if(student.getMobile()!=null&&!("").equals(student.getMobile())){
				CrazyUser.setUserName(student.getMobile());
			}else {
				if(student.getEmail()!=null&&!("").equals(student.getEmail())){
					CrazyUser.setUserName(student.getEmail());
				}
			}
		}
		CrazyUser.setTenantId("000000");
		CrazyUser.setMobile(student.getMobile());
		CrazyUser.setShowName(student.getShowName());
		CrazyUser.setEmail(student.getEmail());
		CrazyUser.setRealName(student.getRealName());
		userInfo.setUser(CrazyUser);
		//设置用户角色
		List<String> roleAlias = Lists.newArrayList();
		roleAlias.add("student");
		userInfo.setRoles(roleAlias);
		return userInfo;
	}

	@Override
	public UserInfo userInfo(String account) {
		UserInfo userInfo = new UserInfo();
		Student student = new Student();
		student.setLoginAccount(account);
		student = baseMapper.selectStudentLogin(student);
		if(Func.isNull(student)){
			return null;
		}
		//封装 CrazyUser
		CrazyUser CrazyUser = new CrazyUser();
		CrazyUser.setUserId(student.getId());
		CrazyUser.setTenantId(null);
		CrazyUser.setAccount(account);
		CrazyUser.setUserName(student.getRealName());
		CrazyUser.setMobile(student.getMobile());
		CrazyUser.setShowName(student.getShowName());
		CrazyUser.setEmail(student.getEmail());
		CrazyUser.setRealName(student.getRealName());
		CrazyUser.setTenantId("000000");
		userInfo.setUser(CrazyUser);
		//设置用户角色
		List<String> roleAlias = Lists.newArrayList();
		roleAlias.add("student");
		userInfo.setRoles(roleAlias);
		return userInfo;
	}

	@Override
	public UserInfo userInfo(String account, String password) {
		UserInfo userInfo = new UserInfo();
		Student student = new Student();
		student.setLoginAccount(account);
		student.setPassword(password);
		student = baseMapper.selectStudentLogin(student);
		if(Func.isNull(student)){
			return null;
		}
		//账号冻结判断
		if(student.getIsAvalible() == 1){
			throw new ServiceException("该账户已被冻结，请联系管理员");
		}
		//封装 CrazyUser
		CrazyUser CrazyUser = new CrazyUser();
		CrazyUser.setUserId(student.getId());
		CrazyUser.setTenantId(null);
		CrazyUser.setAccount(account);
		CrazyUser.setUserName(student.getRealName());
		CrazyUser.setMobile(student.getMobile());
		CrazyUser.setShowName(student.getShowName());
		CrazyUser.setEmail(student.getEmail());
		CrazyUser.setRealName(student.getRealName());
		CrazyUser.setTenantId("000000");
		//学生用户登录权限   1
		CrazyUser.setRoleName("student");
		CrazyUser.setRoleId("-1");
		userInfo.setUser(CrazyUser);
		//设置用户角色
		List<String> roleAlias = Lists.newArrayList();
		roleAlias.add("student");
		userInfo.setRoles(roleAlias);
		//记录登录记录
		UserLoginLog userLoginLog = new UserLoginLog();
		UserAgent userAgent = UserAgent.parseUserAgentString(WebUtil.getRequest().getHeader("User-Agent"));
		userLoginLog.setOsName(userAgent.getBrowser().getName());
		userLoginLog.setUserAgent(userAgent.getOperatingSystem().getName());
		userLoginLog.setIp(WebUtil.getIP());
		userLoginLog.setUserId(student.getId());
		userLoginLog.setLoginTime(DateUtil.now());
		userLoginLogService.save(userLoginLog);
		return userInfo;
	}


	@Override
	public void importUserAgent(List<StudentExcel> data) {
		if (data.size() == 0){
			throw new ServiceException("文件无数据加载！！！");
		}

		int i = 1 ; //行数
		String errMsg = "";//错误信息
		List<Student> studentList = new ArrayList<>(); //导入用户数据集合
		//检测是否为纯数字
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		//检测手机号
		String pMobile = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\\\d{8}$";
		//检测邮箱
		Pattern patternEmail = Pattern.compile("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?");
		//检测字符串中是否包含特殊符号
		String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
		Pattern p = Pattern.compile(regEx);


		for (StudentExcel studentExcel : data){
			//集合添值
			Student studentNow = new Student();
			studentList.add(i-1 , studentNow) ;
			//获取Excel中的数据
			Student student = Objects.requireNonNull(BeanUtil.copy(studentExcel, Student.class));

			if (Func.isNotEmpty(studentExcel.getUserName()) || Func.isNotEmpty(studentExcel.getMobile()) ||
					Func.isNotEmpty(studentExcel.getEmail()) || Func.isNotEmpty(studentExcel.getShowName()) ||
					Func.isNotEmpty(studentExcel.getRealName()) || Func.isNotEmpty(studentExcel.getIdCardNo()) ||
					Func.isNotEmpty(studentExcel.getSex()) || Func.isNotEmpty(studentExcel.getAge()) ||
					Func.isNotEmpty(studentExcel.getPassword()) ) {
				//验证
				if (Func.isNull(studentExcel.getUserName()) && Func.isNull(studentExcel.getMobile()) &&
						Func.isNull(studentExcel.getEmail())){
					errMsg += "第" + i + "行，账号、手机、邮箱 三者必须填写其中一个;<br />";
				}
				//查库
				Student temp = new Student();
				//账号
				if (Func.isNotEmpty(student.getUserName())){
					temp = baseMapper.checkStudentUserName(student);

					if(Func.isNotEmpty(temp)){
						errMsg += "第" + i + "行，账号重复;<br />";
					}else if (pattern.matcher(studentExcel.getUserName()).matches()){
						errMsg += "第" + i + "行，账号不能为纯数字;<br />";
					}else {
						studentList.get(i-1).setUserName(studentExcel.getUserName());
					}
				}

				//手机
				if (Func.isNotEmpty(student.getMobile())){
					temp = baseMapper.checkStudentMobile(student);
					if(Func.isNotEmpty(temp)){
						errMsg += "第" + i + "行，电话重复;<br />";
					}else if (Func.isNotEmpty(studentExcel.getMobile()) && !pattern.matcher(studentExcel.getMobile()).matches() && studentExcel.getMobile().matches(pMobile)){
						errMsg += "第" + i + "行，手机号不正确;<br />";
					}else {
						studentList.get(i-1).setMobile(studentExcel.getMobile());
					}
				}

				//邮箱
				if (Func.isNotEmpty(student.getEmail())){
					temp = baseMapper.checkStudentEmail(student);
					if(Func.isNotEmpty(temp)){
						errMsg += "第" + i + "行，邮箱重复;<br />";
					}else if (!patternEmail.matcher(studentExcel.getEmail()).matches()){
						errMsg += "第" + i + "行，邮箱不正确;<br />";
					}else{
						studentList.get(i-1).setEmail(studentExcel.getEmail());
					}
				}

				//真实姓名
				if (Func.isNull(studentExcel.getRealName())){
					studentList.get(i - 1).setRealName("");
				}else {
					if (pattern.matcher(studentExcel.getRealName()).matches()) {
						errMsg += "第" + i + "行，真实姓名不能为纯数字；<br />";
					} else if (p.matcher(studentExcel.getRealName()).find()) {
						errMsg += "第" + i + "行，真实姓名不可以包含符号；<br />";
					} else {
						studentList.get(i - 1).setRealName(studentExcel.getRealName());
					}
				}

				studentList.get(i-1).setShowName(Func.isNull(studentExcel.getShowName()) ? "" : studentExcel.getShowName());
				studentList.get(i-1).setIdCardNo(Func.isNull(student.getIdCardNo()) ? "" : student.getIdCardNo());
				if (Func.isNull(studentExcel.getSex())) {
					studentList.get(i-1).setSex(0);
				} else if (studentExcel.getSex().equals("男")) {
					studentList.get(i-1).setSex(1);
				} else if (studentExcel.getSex().equals("女")) {
					studentList.get(i-1).setSex(2);
				}
				studentList.get(i-1).setAge(Func.isNull(student.getAge()) ? null : student.getAge());
				studentList.get(i-1).setRegisterFrom("6");
				// 设置默认密码
				studentList.get(i-1).setPassword(Func.isNull(student.getPassword()) ?"123456" : student.getPassword());

				//赠送课程
				if(Func.isNotEmpty(studentExcel.getCourseIdList())){
					if (studentExcel.getCourseIdList().contains("，")){
						errMsg += "第" + i + "行，课程id间隔请使用英文\",\"<br />";
						i++;continue;
					}
					String[] cours = studentExcel.getCourseIdList().split(",");
					List<Integer> courseIdList=new ArrayList<>();
					for (int courNum = 0 ; courNum < cours.length ; courNum++){
						//判断是否可以转成int类型
						if(!NumberUtils.isDigits(cours[courNum])){
							errMsg += "第" + i + "行，课程id间隔请使用英文\",\"<br />";
							continue;
						}
						//查询课程id是否有课程
						Course course = courseService.getCourseById(Integer.valueOf(cours[courNum]));
						//检测课程（套餐）id
						if (Func.isNull(course)){
							errMsg += "第" + i + "行，课程id为" + cours[courNum] + "的课程不存在;<br />";
							continue;
						}else
							//被删除的课程是否参与赠送
							if(course.getIsDeleted()==1){
								errMsg += "第" + i + "行，课程id为" + cours[courNum] + "的课程已被删除;<br />";
								continue;
							}
						courseIdList.add(Integer.valueOf(cours[courNum]));
					}
					studentList.get(i-1).setCourseIdList(courseIdList);
				}
				if(i == data.size()){
					break ;
				}else {
					i++;
				}
			}else {
				return;
			}
		}
		//数据入库
		if (Func.equals(errMsg,"")) {
			for (Student student : studentList) {
				student.setAgentId(SecureUtil.getUserId());
				this.register(student);
				if (Func.isNotEmpty(student.getCourseIdList())) {
					Student student1 = baseMapper.checkStudentNew(student);
					for (Integer courseId : student.getCourseIdList()) {
						List<Course> courseList = new ArrayList<>();
						Course course = courseService.getCourseById(courseId);
						courseList.add(course);

						courseService.courseOrder(student1, courseList);
					}
				}
			}
		}else {
			throw new ServiceException(errMsg);
		}
	}
	@Override
	public void importUser(List<StudentExcel> data) {
		if (data.size() == 0){
			throw new ServiceException("文件无数据加载！！！");
		}

		int i = 1 ; //行数
		String errMsg = "";//错误信息
		List<Student> studentList = new ArrayList<>(); //导入用户数据集合
		//检测是否为纯数字
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		//检测手机号
		String pMobile = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\\\d{8}$";
		//检测邮箱
		Pattern patternEmail = Pattern.compile("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?");
		//检测字符串中是否包含特殊符号
		String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
		Pattern p = Pattern.compile(regEx);


		for (StudentExcel studentExcel : data){
			//集合添值
			Student studentNow = new Student();
			studentList.add(i-1 , studentNow) ;
			//获取Excel中的数据
			Student student = Objects.requireNonNull(BeanUtil.copy(studentExcel, Student.class));

			if (Func.isNotEmpty(studentExcel.getUserName()) || Func.isNotEmpty(studentExcel.getMobile()) ||
					Func.isNotEmpty(studentExcel.getEmail()) || Func.isNotEmpty(studentExcel.getShowName()) ||
					Func.isNotEmpty(studentExcel.getRealName()) || Func.isNotEmpty(studentExcel.getIdCardNo()) ||
					Func.isNotEmpty(studentExcel.getSex()) || Func.isNotEmpty(studentExcel.getAge()) ||
					Func.isNotEmpty(studentExcel.getPassword()) ) {
				//验证
				if (Func.isNull(studentExcel.getUserName()) && Func.isNull(studentExcel.getMobile()) &&
						Func.isNull(studentExcel.getEmail())){
					errMsg += "第" + i + "行，账号、手机、邮箱 三者必须填写其中一个;<br />";
				}
				//查库
				Student temp = new Student();
				//账号
				if (Func.isNotEmpty(student.getUserName())){
					temp = baseMapper.checkStudentUserName(student);

					if(Func.isNotEmpty(temp)){
						errMsg += "第" + i + "行，账号重复;<br />";
					}else if (pattern.matcher(studentExcel.getUserName()).matches()){
						errMsg += "第" + i + "行，账号不能为纯数字;<br />";
					}else {
						studentList.get(i-1).setUserName(studentExcel.getUserName());
					}
				}

				//手机
				if (Func.isNotEmpty(student.getMobile())){
					temp = baseMapper.checkStudentMobile(student);
					if(Func.isNotEmpty(temp)){
						errMsg += "第" + i + "行，电话重复;<br />";
					}else if (Func.isNotEmpty(studentExcel.getMobile()) && !pattern.matcher(studentExcel.getMobile()).matches() && studentExcel.getMobile().matches(pMobile)){
						errMsg += "第" + i + "行，手机号不正确;<br />";
					}else {
						studentList.get(i-1).setMobile(studentExcel.getMobile());
					}
				}

				//邮箱
				if (Func.isNotEmpty(student.getEmail())){
					temp = baseMapper.checkStudentEmail(student);
					if(Func.isNotEmpty(temp)){
						errMsg += "第" + i + "行，邮箱重复;<br />";
					}else if (!patternEmail.matcher(studentExcel.getEmail()).matches()){
						errMsg += "第" + i + "行，邮箱不正确;<br />";
					}else{
						studentList.get(i-1).setEmail(studentExcel.getEmail());
					}
				}

				//真实姓名
				if (Func.isNull(studentExcel.getRealName())){
					studentList.get(i - 1).setRealName("");
				}else {
					if (pattern.matcher(studentExcel.getRealName()).matches()) {
						errMsg += "第" + i + "行，真实姓名不能为纯数字；<br />";
					} else if (p.matcher(studentExcel.getRealName()).find()) {
						errMsg += "第" + i + "行，真实姓名不可以包含符号；<br />";
					} else {
						studentList.get(i - 1).setRealName(studentExcel.getRealName());
					}
				}

				studentList.get(i-1).setShowName(Func.isNull(studentExcel.getShowName()) ? "" : studentExcel.getShowName());
				studentList.get(i-1).setIdCardNo(Func.isNull(student.getIdCardNo()) ? "" : student.getIdCardNo());
				if (Func.isNull(studentExcel.getSex())) {
					studentList.get(i-1).setSex(0);
				} else if (studentExcel.getSex().equals("男")) {
					studentList.get(i-1).setSex(1);
				} else if (studentExcel.getSex().equals("女")) {
					studentList.get(i-1).setSex(2);
				}
				studentList.get(i-1).setAge(Func.isNull(student.getAge()) ? null : student.getAge());
				studentList.get(i-1).setRegisterFrom("5");
				// 设置默认密码
				studentList.get(i-1).setPassword(Func.isNull(student.getPassword()) ?"123456" : student.getPassword());

				//赠送课程
				if(Func.isNotEmpty(studentExcel.getCourseIdList())){
					if (studentExcel.getCourseIdList().contains("，")){
						errMsg += "第" + i + "行，课程id间隔请使用英文\",\"<br />";
						i++;continue;
					}
					String[] cours = studentExcel.getCourseIdList().split(",");
					List<Integer> courseIdList=new ArrayList<>();
					for (int courNum = 0 ; courNum < cours.length ; courNum++){
						//判断是否可以转成int类型
						if(!NumberUtils.isDigits(cours[courNum])){
							errMsg += "第" + i + "行，课程id间隔请使用英文\",\"<br />";
							continue;
						}
						//查询课程id是否有课程
						Course course = courseService.getCourseById(Integer.valueOf(cours[courNum]));
						//检测课程（套餐）id
						if (Func.isNull(course)){
							errMsg += "第" + i + "行，课程id为" + cours[courNum] + "的课程不存在;<br />";
							continue;
						}else
							//被删除的课程是否参与赠送
							if(course.getIsDeleted()==1){
								errMsg += "第" + i + "行，课程id为" + cours[courNum] + "的课程已被删除;<br />";
								continue;
							}
						courseIdList.add(Integer.valueOf(cours[courNum]));
					}
					studentList.get(i-1).setCourseIdList(courseIdList);
				}
				if(i == data.size()){
					break ;
				}else {
					i++;
				}
			}else {
				return;
			}
		}
		//数据入库
		if (Func.equals(errMsg,"")) {
			for (Student student : studentList) {
				this.register(student);
				if (Func.isNotEmpty(student.getCourseIdList())) {
					Student student1 = baseMapper.checkStudentNew(student);
					for (Integer courseId : student.getCourseIdList()) {
						List<Course> courseList = new ArrayList<>();
						Course course = courseService.getCourseById(courseId);
						courseList.add(course);

						courseService.courseOrder(student1, courseList);
					}
				}
			}
		}else {
			throw new ServiceException(errMsg);
		}
	}
	@Override
	public void exportUser(HttpServletResponse response , Map<String, Object> user) {
		ExcelWriter excelWriter = null;
		try {
			Integer size = 3000;

			excelWriter = EasyExcel.write(response.getOutputStream(), ExportStudentExcel.class).build();
			WriteSheet writeSheet = EasyExcel.writerSheet("用户数据表").build();
			//分页写入
			QueryWrapper<Student> queryWrapper = Condition.getQueryWrapper(new Student());
			queryWrapper.lambda().like(Func.isNotEmpty(user.get("id")),Student::getId,user.get("id"));
			queryWrapper.lambda().like(Func.isNotEmpty(user.get("mobile")),Student::getMobile,user.get("mobile"));
			queryWrapper.lambda().like(Func.isNotEmpty(user.get("email")),Student::getEmail,user.get("email"));
			queryWrapper.lambda().like(Func.isNotEmpty(user.get("showName")),Student::getShowName,user.get("showName"));
			queryWrapper.lambda().like(Func.isNotEmpty(user.get("realName")),Student::getRealName,user.get("realName"));
			queryWrapper.lambda().like(Func.isNotEmpty(user.get("userName")),Student::getUserName,user.get("userName"));

			queryWrapper.lambda().eq(Func.isNotEmpty(user.get("registerFrom")),Student::getRegisterFrom,user.get("registerFrom"));
			queryWrapper.lambda().eq(Student::getIsAvalible,"2");

			Integer count = baseMapper.selectCount(queryWrapper);
			Page<ExportStudentExcel> page = new Page<>();
			for (int i = 1; i <= (count/size)+1; i++) {
				page.setCurrent(i);
				page.setSize(size);
				List<ExportStudentExcel> list = baseMapper.exportUser(page, queryWrapper);
				User adminUser = new User();
				adminUser.setRoleId("12");
				List<User> listUserAdmin = userService.list(Condition.getQueryWrapper(adminUser));
				for(ExportStudentExcel studentExcel:list){
					if(Func.isNotEmpty(studentExcel.getAgentId())&&!("-1").equals(studentExcel.getAgentId())){
						for(User userAdmin:listUserAdmin){
							if((Integer.valueOf(studentExcel.getAgentId())).equals(userAdmin.getId())){
								studentExcel.setAgentId(userAdmin.getName());
								break;
							}
						}
					}else {
						studentExcel.setAgentId("");
					}
				}
				excelWriter.write(list, writeSheet);
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
	public void register(Student student) {
		student.setIsAvalible(2);
		//密码加密
		student.setPassword(DigestUtil.encrypt(student.getPassword()));
		save(student);
		//保存学生信息
		//创建学生储值
		BigDecimal zero = new BigDecimal(0);
		UserAccount userAccount = new UserAccount();
		userAccount.setBalance(zero);
		userAccount.setBackAmount(zero);
		userAccount.setCashAmount(zero);
		userAccount.setForzenAmount(zero);
		userAccount.setVmAmount(zero);
		userAccount.setUserId(student.getId());
		userAccountService.save(userAccount);
	}

	@Override
	public Student checkStudent(Student student) {
		return baseMapper.checkStudent(student);
	}

	@Override
	public Student checkStudentNew(Student student) {
		return baseMapper.checkStudentNew(student);
	}

	@Override
	public Student checkStudentUserName(Student student) {
		return baseMapper.checkStudentUserName(student);
	}

	@Override
	public Student checkStudentMobile(Student student) {
		return baseMapper.checkStudentMobile(student);
	}

	@Override
	public Student checkStudentEmail(Student student) {
		return baseMapper.checkStudentEmail(student);
	}

	@Override
	public Student getStudent(Integer userId) {
		return baseMapper.getStudent(userId);
	}

	@Override
	public void updateAvalible(String key) {
		key = key.substring(6,key.length());
		baseMapper.updateAvalible(key);
	}
}
