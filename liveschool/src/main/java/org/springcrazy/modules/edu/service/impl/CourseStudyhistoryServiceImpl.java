
package org.springcrazy.modules.edu.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.edu.entity.Course;
import org.springcrazy.modules.edu.entity.CourseStudyhistory;
import org.springcrazy.modules.edu.mapper.CourseStudyhistoryMapper;
import org.springcrazy.modules.edu.service.ICourseService;
import org.springcrazy.modules.edu.service.ICourseStudyhistoryService;
import org.springcrazy.modules.edu.vo.CourseStudyhistoryVO;
import org.springcrazy.modules.system.excel.CourseStudyhistoryExcel;
import org.springcrazy.modules.system.excel.ExportStudentStudyExcel;
import org.springcrazy.modules.user.entity.Student;
import org.springcrazy.modules.user.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 课程播放记录(学习记录) 服务实现类
 *
 * @author TongZhou
 * @since 2020-05-06
 */
@Service
public class CourseStudyhistoryServiceImpl extends ServiceImpl<CourseStudyhistoryMapper, CourseStudyhistory> implements ICourseStudyhistoryService {
	@Autowired
	private ICourseService courseService;

	@Autowired
	private IStudentService studentService;

	@Override
	public IPage<CourseStudyhistoryVO> selectCourseStudyhistoryPage(IPage<CourseStudyhistoryVO> page, CourseStudyhistoryVO courseStudyhistory) {
		return page.setRecords(baseMapper.selectCourseStudyhistoryPage(page, courseStudyhistory));
	}

	@Override
	public IPage<CourseStudyhistory> selectCourseTrxorder(IPage<CourseStudyhistory> page, CourseStudyhistory courseStudyhistory) {
		return page.setRecords(baseMapper.selectCourseTrxorder(page, courseStudyhistory));
	}

	@Override
	public CourseStudyhistory getStudyTime(int userId, int courseId) {
		return baseMapper.getStudyTime(userId,courseId);
	}
	public CourseStudyhistory getPackageStudyTime(int userId,int coursePackageId,int courseId){
		return baseMapper.getPackageStudyTime(userId,coursePackageId,courseId);
	}
	@Override
	public int getstudykpoint(int courseId) {
		return Integer.valueOf(baseMapper.getstudykpoint(courseId));
	}
	public int getPackageStudykpoint(int coursePackageId){
		return Integer.valueOf(baseMapper.getPackageStudykpoint(coursePackageId));
	}
	@Override
	public int getstudystudyisok(int userId, int courseId, int coursePackageId) {
		return Integer.valueOf(baseMapper.getstudystudyisok(userId,courseId,coursePackageId));
	}

	@Override
	public List<CourseStudyhistory> getCourseList(CourseStudyhistory courseStudyhistory) {
		return baseMapper.getCourseList(courseStudyhistory);
	}

	@Override
	public List<CourseStudyhistory> getCourseListSort(CourseStudyhistory courseStudyhistory) {
		return baseMapper.getCourseListSort(courseStudyhistory);
	}

	public void queryCourseKpointDateStudyHistoryInit(List<CourseStudyhistory>  courseStudyhistoryList,CourseStudyhistory courseStudyhistory) {
		//判断为空,如果为空则返回出去
		if(ObjectUtil.isNull(courseStudyhistoryList)){
			return;
		}
		Map<String,Map> courseKpointStudyHistoryDateMap = baseMapper.getCourseKpointStudyHistoryDateToMap(courseStudyhistory);
		/*应学习人数*/
		int studyPope=0;
		//判断是套餐还是点播和直播
		if(ObjectUtil.isNotNull(courseStudyhistory.getCoursePackageId())&&courseStudyhistory.getCoursePackageId()>0){
			//套餐的应学习人数
			studyPope =getcoursepepo(courseStudyhistory.getCoursePackageId());
		}else{
			//点播和直播进来的应学习人数
			studyPope =getcoursepepo(courseStudyhistory.getCourseId());
		}

		for(CourseStudyhistory kpoint:courseStudyhistoryList) {
			if (kpoint.getParentId() == 0) {
				kpoint.setPlayercount(-1);
				kpoint.setStudyPope(-1);
				kpoint.setStudyPopeShould(-1);
				kpoint.setStudyPopeComplete(-1);
				kpoint.setStudyPopeNo(-1);
				continue;
			}
			kpoint.setPlayercount(0);
			/*播放次数*/
			if (ObjectUtil.isNotEmpty(courseKpointStudyHistoryDateMap)) {
				if (ObjectUtil.isNotNull(courseKpointStudyHistoryDateMap.get(kpoint.getKpointId()))) {
					if (ObjectUtil.isNotNull(courseKpointStudyHistoryDateMap.get(kpoint.getKpointId()).get("sumPlayerCount"))) {
						/*播放次数*/
						kpoint.setPlayercount(Integer.valueOf(courseKpointStudyHistoryDateMap.get(kpoint.getKpointId()).get("sumPlayerCount") + ""));
					} else {
						kpoint.setPlayercount(0);
					}
				}
			}
			kpoint.setStudyPope(studyPope);
			/*已学习人数*/
			int studyPopeShould = 0;
			/*学习完成的人*/
			int studyPopeComplete = 0;
			/*已学习人数*/
			if (ObjectUtil.isNotEmpty(courseKpointStudyHistoryDateMap)) {
				if (ObjectUtil.isNotNull(courseKpointStudyHistoryDateMap.get(kpoint.getKpointId()))) {
					if (ObjectUtil.isNotNull(courseKpointStudyHistoryDateMap.get(kpoint.getKpointId()).get("studyNum"))) {
						studyPopeShould = Integer.valueOf(courseKpointStudyHistoryDateMap.get(kpoint.getKpointId()).get("studyNum") + "");
					}
				}
			}
			/*学习完成的人*/
			if (ObjectUtil.isNotEmpty(courseKpointStudyHistoryDateMap)) {
				if (ObjectUtil.isNotNull(courseKpointStudyHistoryDateMap.get(kpoint.getKpointId()))) {
					if (ObjectUtil.isNotNull(courseKpointStudyHistoryDateMap.get(kpoint.getKpointId()).get("studyFinishNum"))) {
						studyPopeComplete = Integer.valueOf(courseKpointStudyHistoryDateMap.get(kpoint.getKpointId()).get("studyFinishNum") + "");
					}
				}
			}
			/*已学习人数*/
			kpoint.setStudyPopeShould(studyPopeShould);
			/*学习完成的人*/
			kpoint.setStudyPopeComplete(studyPopeComplete);
			/*未学习人数*/
			kpoint.setStudyPopeNo(studyPope - studyPopeShould);
		}
	}

	@Override
	public IPage<CourseStudyhistory> getCoursepackageList(IPage<CourseStudyhistory> page, CourseStudyhistory courseStudyhistory) {
		return page.setRecords(baseMapper.getCoursepackageList(page, courseStudyhistory));
	}

	@Override
	public CourseStudyhistory getStudyHistory(int userId, int kpointId,Integer coursePackageId,int studyType) {
		return baseMapper.getStudyHistory(userId,kpointId,coursePackageId,studyType);
	}

	@Override
	public CourseStudyhistory getOnesStudyHistory(int userId, int kpointId, int studyType, int packAgee) {
		return baseMapper.getOnesStudyHistory(userId,kpointId,studyType,packAgee);
	}

	@Override
	public int getWatchNum(int courseId, int kpointId,int coursePackageId) {
		return Integer.valueOf(baseMapper.getWatchNum(courseId,kpointId,coursePackageId));
	}

	@Override
	public int getcoursepepo(int courseId) {
		return Integer.valueOf(baseMapper.getcoursepepo(courseId));
	}

	@Override
	public int getWatchShould(int courseId, int kpointId,int coursePackageId) {
		return Integer.valueOf(baseMapper.getWatchShould(courseId,kpointId,coursePackageId));
	}

	@Override
	public int getWatchComplete(int courseId, int kpointId,int coursePackageId) {
		return Integer.valueOf(baseMapper.getWatchComplete(courseId,kpointId,coursePackageId));
	}

	@Override
	public IPage<CourseStudyhistory> selectCourseUser(IPage<CourseStudyhistory> page, CourseStudyhistory courseStudyhistory) {
		return page.setRecords(baseMapper.selectCourseUser(page, courseStudyhistory));
	}

	@Override
	public IPage<CourseStudyhistory> selectStudentShouldAndComplete(IPage<CourseStudyhistory> page, CourseStudyhistory courseStudyhistory) {
		return page.setRecords(baseMapper.selectStudentShouldAndComplete(page, courseStudyhistory));
	}

	@Override
	public void exportUserStudyHistory(HttpServletResponse response, Map<String, Object> CourseStudyhistory) {
		ExcelWriter excelWriter = null;
		try {
			Integer size = 3000;

			excelWriter = EasyExcel.write(response.getOutputStream(), CourseStudyhistoryExcel.class).build();
			WriteSheet writeSheet = EasyExcel.writerSheet("用户数据表").build();
			//分页写入
			CourseStudyhistoryVO courseStudyhistoryVO = new CourseStudyhistoryVO();
			Page<CourseStudyhistoryExcel> page = new Page<>();
			List<CourseStudyhistoryExcel> list = baseMapper.exportUserStudyHistory(page , courseStudyhistoryVO);
			for (int i = 1; i <= (list.size()/size)+1; i++) {
				page.setCurrent(i);
				page.setSize(size);
				list = baseMapper.exportUserStudyHistory(page, courseStudyhistoryVO);
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
	public List<CourseStudyhistory> getPackageList(CourseStudyhistory courseStudyhistory) {
		return  baseMapper.getPackageList(courseStudyhistory);
	}

	@Override
	public int getCourseWatchNum(int courseId, String userList) {
		return Integer.valueOf(baseMapper.getCourseWatchNum(courseId,userList));
	}

	@Override
	public int getCourseWatchTime(int courseId) {
		return baseMapper.getCourseWatchTime(courseId);
	}

	@Override
	public IPage<CourseStudyhistory> selectNoStudy(IPage<CourseStudyhistory> page, CourseStudyhistory courseStudyhistory) {
		return page.setRecords(baseMapper.selectNoStudy(page, courseStudyhistory));
	}

	@Override
	public IPage<CourseStudyhistory> selectPackageNoStudy(IPage<CourseStudyhistory> page, CourseStudyhistory courseStudyhistory) {
		return page.setRecords(baseMapper.selectPackageNoStudy(page, courseStudyhistory));
	}

	@Override
	public IPage<CourseStudyhistory> getPackageUserList(IPage<CourseStudyhistory> page, CourseStudyhistory courseStudyhistory) {
		return page.setRecords(baseMapper.getPackageUserList(page, courseStudyhistory));
	}

	@Override
	public CourseStudyhistory getKpointAndCourse(int courseId, int kpointId) {
		return baseMapper.getKpointAndCourse(courseId, kpointId);
	}

	@Override
	public IPage<CourseStudyhistory> selectPackageStudentShouldAndComplete(IPage<CourseStudyhistory> page, CourseStudyhistory courseStudyhistory) {
		return page.setRecords(baseMapper.selectPackageStudentShouldAndComplete(page, courseStudyhistory));
	}
	@Override
	public IPage<CourseStudyhistory> getCourseUserList(IPage<CourseStudyhistory> page, CourseStudyhistory courseStudyhistory) {
		List<CourseStudyhistory> list = baseMapper.getCourseUserList(page, courseStudyhistory);
		Course course = courseService.getById(courseStudyhistory.getCourseId());
		if("3".equals(course.getSellType())){
			for(CourseStudyhistory courseStudy:list) {
				initCoursePackageListStudyHistory(courseStudy);
			}
		}else{
			initCourseUserListStudyHistory(list);
		}

		return page.setRecords(list);
	}
	public void initCoursePackageListStudyHistory(CourseStudyhistory courseStudy){
		CourseStudyhistory course =getPackageStudyTime(courseStudy.getUserId(),courseStudy.getCourseId(),0);
		if(course!=null){
			if(course.getWatchTime()>0){
				courseStudy.setWatchStingTime(gitTime(course.getWatchTime()));
			}else {
				courseStudy.setWatchStingTime("暂未学习时长");
			}
			courseStudy.setUpdateTime(course.getUpdateTime());
			int v = getstudystudyisok(courseStudy.getUserId(),0,courseStudy.getCourseId());
			int y= getPackageStudykpoint(courseStudy.getCourseId());
			if(v<=0){
				courseStudy.setStudyLearning("0.00%");
			}else {
				float num= (float)v*100/y;
				DecimalFormat df = new DecimalFormat("0.00");//格式化小数
				courseStudy.setStudyLearning(df.format(num)+"%");
			}
		}else {
			courseStudy.setWatchStingTime("暂未学习时长");
			courseStudy.setStudyLearning("0.00%");
		}
	}
	public void initCoursePackageCourseListStudyHistory(CourseStudyhistory courseStudy){
		CourseStudyhistory course =getPackageStudyTime(courseStudy.getUserId(),courseStudy.getCoursePackageId(),courseStudy.getCourseId());
		if(course!=null){
			if(course.getWatchTime()>0){
				courseStudy.setWatchStingTime(gitTime(course.getWatchTime()));
			}else {
				courseStudy.setWatchStingTime("暂未学习时长");
			}
			courseStudy.setUpdateTime(course.getUpdateTime());
			int v = getstudystudyisok(courseStudy.getUserId(),courseStudy.getCourseId(),courseStudy.getCoursePackageId());
			int y= getstudykpoint(courseStudy.getCourseId());
			if(v<=0){
				courseStudy.setStudyLearning("0.00%");
			}else {
				float num= (float)v*100/y;
				DecimalFormat df = new DecimalFormat("0.00");//格式化小数
				courseStudy.setStudyLearning(df.format(num)+"%");
			}
		}else {
			courseStudy.setWatchStingTime("暂未学习时长");
			courseStudy.setStudyLearning("0.00%");
		}
	}
	public void initCourseUserListStudyHistory(List<CourseStudyhistory> courseStudyhistoryList){
		for(CourseStudyhistory courseStudy:courseStudyhistoryList){
			initCourseUserListStudyHistoryDate(courseStudy);
		}
	}
	public void initCourseUserListStudyHistoryDate(CourseStudyhistory courseStudy){
		CourseStudyhistory course =getStudyTime(courseStudy.getUserId(),courseStudy.getCourseId());
		if(course!=null){
			if(course.getWatchTime()>0){
				courseStudy.setWatchStingTime(gitTime(course.getWatchTime()));
			}else {
				courseStudy.setWatchStingTime("暂未学习时长");
			}
			courseStudy.setUpdateTime(course.getUpdateTime());
			int v = getstudystudyisok(courseStudy.getUserId(),courseStudy.getCourseId(),0);
			int y=getstudykpoint(courseStudy.getCourseId());
			if(v<=0){
				courseStudy.setStudyLearning("0.00%");
			}else {
				float num= (float)v*100/y;
				DecimalFormat df = new DecimalFormat("0.00");//格式化小数
				courseStudy.setStudyLearning(df.format(num)+"%");
			}
		}else {
			courseStudy.setWatchStingTime("暂未学习时长");
			courseStudy.setStudyLearning("0.00%");
		}
	}

	@Override
	public int deleteHistory(Integer id) {
		return baseMapper.removeByKpointId(id);
	}

	@Override
	public void exportUser(HttpServletResponse response, CourseStudyhistory courseStudyhistory) {
		ExcelWriter excelWriter = null;
		try {
			excelWriter = EasyExcel.write(response.getOutputStream(), ExportStudentStudyExcel.class).build();
			WriteSheet writeSheet = EasyExcel.writerSheet("用户学习记录表").build();
			List<ExportStudentStudyExcel> list  = new ArrayList<>();
			//查询对应课程的章节信息
			List<CourseStudyhistory> pages = baseMapper.getCourseListSort(courseStudyhistory);
			//查询学生信息
			Student student = studentService.getStudent(courseStudyhistory.getUserId());
			//查询课程信息
			Course courseName = courseService.getCourseById(courseStudyhistory.getCourseId());
			//查询如果是套餐，则加入套餐名称。
			Course coursePake = new Course();
			if(Func.isNotEmpty(courseStudyhistory.getCoursePackageId())&&courseStudyhistory.getCoursePackageId()!=0){
				 coursePake = courseService.getCourseById(courseStudyhistory.getCoursePackageId());
			}
			//循环赋值
			for(CourseStudyhistory history:pages){
				//只查章
				if(history.getParentId()==0){
					ExportStudentStudyExcel exportStudentStudyExcel =  new ExportStudentStudyExcel();
					exportStudentStudyExcel.setUserName(student.getUserName());
					exportStudentStudyExcel.setCourseName(courseName.getCourseName());
					exportStudentStudyExcel.setKpointName(history.getKpointName());
					if (Func.isNotEmpty(coursePake.getCourseName())){
						exportStudentStudyExcel.setPakeName(coursePake.getCourseName());
					}
					list.add(exportStudentStudyExcel);
					//添加完章查询对应的节
					for(CourseStudyhistory historychi:pages){
						if((history.getKpointId()).equals(historychi.getParentId())){
							ExportStudentStudyExcel exportStudentStudyExcelchil =  new ExportStudentStudyExcel();
							exportStudentStudyExcelchil.setUserName(student.getUserName());
							exportStudentStudyExcelchil.setCourseName(courseName.getCourseName());
							exportStudentStudyExcelchil.setKpointName(historychi.getKpointName());
							exportStudentStudyExcelchil.setStudyTime(historychi.getVideoTime());
							if (Func.isNotEmpty(coursePake.getCourseName())){
								exportStudentStudyExcelchil.setPakeName(coursePake.getCourseName());
							}
							CourseStudyhistory course=baseMapper.getStudyHistory(courseStudyhistory.getUserId(),historychi.getKpointId(),courseStudyhistory.getCoursePackageId(),0);
							if(course!=null){
								/*累计学习时长*/
								if(course.getWatchTime()>0){
									exportStudentStudyExcelchil.setStudyTimeNums(gitTime(course.getWatchTime()));
								}else {
									exportStudentStudyExcelchil.setStudyTimeNums("0");
								}
								/*学习进度*/
								exportStudentStudyExcelchil.setStudyLearning(course.getStudyLearning());
								/*是否完成*/
								if("1".equals(course.getComplete())){
									exportStudentStudyExcelchil.setComplete("未完成");
								}else {
									exportStudentStudyExcelchil.setComplete("完成");
								}
								/*最后学习时长*/
								exportStudentStudyExcelchil.setUpdateTime(course.getUpdateTime());
							}else {
								/*累计学习时长*/
								exportStudentStudyExcelchil.setStudyTimeNums("0");
								/*学习进度*/
								exportStudentStudyExcelchil.setStudyLearning("暂未学习进度");
								/*是否完成*/
								exportStudentStudyExcelchil.setComplete("未完成");

							}
							list.add(exportStudentStudyExcelchil);
						}
					}
				}
			}
			excelWriter.write(list, writeSheet);

		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (excelWriter != null) {
				excelWriter.finish();
			}
		}
	}

	@Override
	public void exportKpointIdUser(HttpServletResponse response, CourseStudyhistory courseStudyhistory) {
		ExcelWriter excelWriter = null;
		List<CourseStudyhistory> pages = null;
		try {
			excelWriter = EasyExcel.write(response.getOutputStream(), ExportStudentStudyExcel.class).build();
			WriteSheet writeSheet = EasyExcel.writerSheet("用户学习记录表").build();
			List<ExportStudentStudyExcel> list  = new ArrayList<>();
			//查询课程信息
			Course courseName = courseService.getCourseById(courseStudyhistory.getCourseId());
			//查询如果是套餐，则加入套餐名称。
			Course coursePake = new Course();
			if(Func.isNotEmpty(courseStudyhistory.getKpointCourseId())&&courseStudyhistory.getKpointCourseId()!=0){
				coursePake = courseService.getCourseById(courseStudyhistory.getKpointCourseId());
			}
			int studyType=1;
			CourseStudyhistory courseStudyhistory1 = baseMapper.getKpointAndCourse(courseStudyhistory.getCourseId(),courseStudyhistory.getKpointId());

			if("PACKAGE".equals(courseStudyhistory.getCourseType())){
				studyType=2;
				if("ALL".equals(courseStudyhistory.getPopeType())){
					//全部所有的人信息
					pages = baseMapper.getPackageUserListNoPage(courseStudyhistory);
				}else if("SHOULD".equals(courseStudyhistory.getPopeType())){
					//已学习
					pages=baseMapper.selectPackageStudentShouldAndCompleteNoPage(courseStudyhistory);
				}else if("COMPLETE".equals(courseStudyhistory.getPopeType())){
					//完成学员
					pages=baseMapper.selectPackageStudentShouldAndCompleteNoPage(courseStudyhistory);
				}else if("NO".equals(courseStudyhistory.getPopeType())){
					//未学习学员
					pages=baseMapper.selectPackageNoStudyNoPage(courseStudyhistory);
				}
			}else {
				if("ALL".equals(courseStudyhistory.getPopeType())){
					//全部所有的人信息
					pages=baseMapper.selectCourseUserNoPage(courseStudyhistory);
				}else if("SHOULD".equals(courseStudyhistory.getPopeType())){
					//已学习
					pages=baseMapper.selectStudentShouldAndCompleteNoPage(courseStudyhistory);
				}else if("COMPLETE".equals(courseStudyhistory.getPopeType())){
					//完成学员
					pages=baseMapper.selectStudentShouldAndCompleteNoPage(courseStudyhistory);
				}else if("NO".equals(courseStudyhistory.getPopeType())){
					//未学习学员
					pages=baseMapper.selectNoStudyNoPage(courseStudyhistory);
				}
			}

			for(CourseStudyhistory prpo:pages){
				//查询学生信息
				Student student = studentService.getStudent(prpo.getUserId());
				//填写表信息
				ExportStudentStudyExcel exportStudentStudyExcel =  new ExportStudentStudyExcel();
				if(Func.isNotEmpty(student.getUserName())){
					exportStudentStudyExcel.setUserName(student.getUserName());
				}else {
					exportStudentStudyExcel.setUserName(student.getShowName());
				}
				exportStudentStudyExcel.setCourseName(courseName.getCourseName());
				exportStudentStudyExcel.setKpointName(courseStudyhistory1.getKpointName());
				exportStudentStudyExcel.setStudyTime(courseStudyhistory1.getVideoTime());
				if (Func.isNotEmpty(coursePake.getCourseName())){
					exportStudentStudyExcel.setPakeName(coursePake.getCourseName());
				}
				CourseStudyhistory course = new CourseStudyhistory();
				if(Func.isNotEmpty(courseStudyhistory.getCoursePackageId())&&courseStudyhistory.getCoursePackageId()!=0){
					course=baseMapper.getStudyHistory(prpo.getUserId(),courseStudyhistory.getKpointId(),courseStudyhistory.getCoursePackageId(),studyType);
				}else {
					course=baseMapper.getStudyHistory(prpo.getUserId(),courseStudyhistory.getKpointId(),0,studyType);
				}
				if(course!=null){
					/*累计学习时长*/
					if(course.getWatchTime()>0){
						exportStudentStudyExcel.setStudyTimeNums(Integer.toString(course.getWatchTime()));
					}else {
						exportStudentStudyExcel.setStudyTimeNums("0");
					}
					/*学习进度*/
					exportStudentStudyExcel.setStudyLearning(course.getStudyLearning());
					/*是否完成*/
					if("1".equals(course.getComplete())){
						exportStudentStudyExcel.setComplete("未完成");
					}else {
						exportStudentStudyExcel.setComplete("完成");
					}
					/*最后学习时长*/
					exportStudentStudyExcel.setUpdateTime(course.getUpdateTime());
				}else {
					/*累计学习时长*/
					exportStudentStudyExcel.setStudyTimeNums("0");
					/*学习进度*/
					exportStudentStudyExcel.setStudyLearning("暂未学习进度");
					/*是否完成*/
					exportStudentStudyExcel.setComplete("未完成");

				}
				list.add(exportStudentStudyExcel);
			}
			excelWriter.write(list, writeSheet);

		} catch (IOException e) {
				e.printStackTrace();
		}finally {
			if (excelWriter != null) {
				excelWriter.finish();
			}
		}
	}

	public String  gitTime(int time){
		int theTime = Integer.valueOf(time);// 秒
		String middles= "";// 分
		int middle= 0;// 分
		int hour= 0;// 小时
		if(theTime > 60) {
			middles = theTime/60 < 10 ? "0" + theTime/60 : theTime/60+"" ;//小于10分钟的补位
			middle = theTime/60;//分钟数
			theTime = theTime%60;
//			if(middle> 60) {
//				hour= middle/60;
//				middle= middle%60;
//			}
		}else{
			if(theTime>9){
				return "00:"+theTime;
			}else{
				return "00:0"+theTime;
			}

		}
		String timec="";
//		if(hour> 0) {
//			timec += hour+":";
//		}
		if(middle > 0) {
			timec += middles+":";
		}
		timec += theTime < 10 ? "0" + theTime : theTime;
		return timec;
	}
}
