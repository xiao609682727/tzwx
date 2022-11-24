
package org.springcrazy.modules.edu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.minio.messages.S3Key;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springcrazy.core.boot.ctrl.CrazyController;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.mp.support.Query;
import org.springcrazy.core.secure.utils.SecureUtil;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.utils.Charsets;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.core.tool.utils.RedisUtil;
import org.springcrazy.modules.edu.entity.CourseKpoint;
import org.springcrazy.modules.edu.entity.CourseStudyhistory;
import org.springcrazy.modules.edu.service.ICourseKpointService;
import org.springcrazy.modules.edu.service.ICourseStudyhistoryService;
import org.springcrazy.modules.edu.vo.CourseStudyhistoryVO;
import org.springcrazy.modules.edu.wrapper.CourseStudyHistoryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 课程播放记录(学习记录) 控制器
 *
 * @author TongZhou
 * @since 2020-05-06
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/edu/coursestudyhistory")
@Api(value = "课程播放记录(学习记录)", tags = "课程播放记录(学习记录)接口")
public class CourseStudyhistoryController extends CrazyController {

	@Autowired
	private RedisUtil redisUtil;
	private ICourseStudyhistoryService courseStudyhistoryService;
	@Autowired
	private ICourseKpointService courseKpointService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入courseStudyhistory")
	public R<CourseStudyhistory> detail(CourseStudyhistory courseStudyhistory) {
		CourseStudyhistory detail = courseStudyhistoryService.getOne(Condition.getQueryWrapper(courseStudyhistory));
		return R.data(detail);
	}

	/**
	 * 分页 课程播放记录(学习记录)
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入courseStudyhistory")
	public R<IPage<CourseStudyhistory>> list(CourseStudyhistory courseStudyhistory, Query query) {
		QueryWrapper<CourseStudyhistory> queryWrapper = Condition.getQueryWrapper(courseStudyhistory);
		queryWrapper.lambda().like(Func.isNotBlank(courseStudyhistory.getCourseName()),CourseStudyhistory::getCourseName,courseStudyhistory.getCourseName());
		queryWrapper.lambda().like(Func.isNotBlank(courseStudyhistory.getKpointName()),CourseStudyhistory::getKpointName,courseStudyhistory.getKpointName());
		courseStudyhistory.setCourseName(null);
		courseStudyhistory.setKpointName(null);
		IPage<CourseStudyhistory> pages = courseStudyhistoryService.page(Condition.getPage(query), queryWrapper.lambda().orderByDesc(CourseStudyhistory::getUpdateTime));
		return R.data(pages);
	}

	/**
	 * 自定义分页 课程播放记录(学习记录)
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入courseStudyhistory")
	public R<IPage<CourseStudyhistoryVO>> page(CourseStudyhistoryVO courseStudyhistory, Query query) {
		IPage<CourseStudyhistoryVO> pages = courseStudyhistoryService.selectCourseStudyhistoryPage(Condition.getPage(query), courseStudyhistory);
		return R.data(pages);
	}

	/**
	 * 新增 课程播放记录(学习记录)
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入courseStudyhistory")
	public R save(@Valid @RequestBody CourseStudyhistory courseStudyhistory) {
		return R.status(courseStudyhistoryService.save(courseStudyhistory));
	}

	/**
	 * 修改 课程播放记录(学习记录)
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入courseStudyhistory")
	public R update(CourseStudyhistory courseStudyhistory) {
		//检测同一个用户同一章节的学习记录时长请求，如果50秒内请求超过3次，则为非正常请求。不接收。
		String studyTimeRedis="STUDYTIMEREDIS_"+ SecureUtil.getUserId() +"_"+courseStudyhistory.getKpointId();
		Object studyTimeRedisNums = redisUtil.get(studyTimeRedis);
		int StudyGetNums=0;
		// 判断redis存储的是否有值
		if (Func.notNull(studyTimeRedisNums)) {
			 StudyGetNums=(int) studyTimeRedisNums;
			 //如果存储中的值大于等于2，说明本次请求为50秒内的第三次。则判断为无效。不进行储存
			if(StudyGetNums>=2){
				log.info("请勿频繁请求学习记录接口。");
				return R.status(false);
			}else {
				StudyGetNums+=1;
			}
		}else {
			StudyGetNums=1;
		}
		redisUtil.set(studyTimeRedis,StudyGetNums,50);
		int times = Integer.valueOf(courseStudyhistory.getWatchStingTime());
		//查询学习记录
		courseStudyhistory.setUserId(SecureUtil.getUserId());
		QueryWrapper<CourseStudyhistory> courseStudyhistoryQueryWrapper = new QueryWrapper<>();
		courseStudyhistoryQueryWrapper.lambda().eq(CourseStudyhistory::getCourseId,courseStudyhistory.getCourseId())
				.eq(CourseStudyhistory::getUserId,courseStudyhistory.getUserId())
				.eq(CourseStudyhistory::getKpointId,courseStudyhistory.getKpointId())
				.eq(CourseStudyhistory::getCourseType,courseStudyhistory.getCourseType())
				.eq(CourseStudyhistory::getKpointCourseId,courseStudyhistory.getKpointCourseId());
		CourseStudyhistory tcourse = courseStudyhistoryService.getOne(courseStudyhistoryQueryWrapper);
		//如果学习记录不为空，则保存学习记录
		if(Func.isNotEmpty(tcourse)){
			courseStudyhistory.setId(tcourse.getId());
			courseStudyhistory.setWatchTime(times+tcourse.getWatchTime());
			//根据课程id和章节id查询该章节应该学习的时间，点播的化从视频云处拿到视频时长，直播是结束时间减开始时间。
			CourseStudyhistory courseStudyhistory2 = courseStudyhistoryService.getKpointAndCourse(tcourse.getCourseId(),tcourse.getKpointId());
			int videoTime = Integer.valueOf(courseStudyhistory2.getVideoTime());
			int y=times+tcourse.getWatchTime();
			//如果章节的视频时长是小等于0则他的学习进度是0
			if(videoTime<=0){
				courseStudyhistory.setStudyLearning("0.00%");
			}else {
				float num= (float)y*100/videoTime;
				DecimalFormat df = new DecimalFormat("0.00");//格式化小数
				if(num<100){
					courseStudyhistory.setStudyLearning(df.format(num)+"%");
				}else if(num>=100){
					courseStudyhistory.setComplete("2");
					courseStudyhistory.setStudyLearning("100%");
				}
			}
			courseStudyhistoryService.updateById(courseStudyhistory);
		}
		return R.status(true);
	}

	/**
	 * 新增或修改 课程播放记录(学习记录)
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入courseStudyhistory")
	public R submit(CourseStudyhistory courseStudyhistory) {
		courseStudyhistory.setUserId(SecureUtil.getUserId());
		QueryWrapper<CourseStudyhistory> courseStudyhistoryQueryWrapper = new QueryWrapper<>();
		courseStudyhistoryQueryWrapper.lambda().eq(CourseStudyhistory::getCourseId,courseStudyhistory.getCourseId())
				.eq(CourseStudyhistory::getUserId,courseStudyhistory.getUserId())
				.eq(CourseStudyhistory::getKpointId,courseStudyhistory.getKpointId())
				.eq(CourseStudyhistory::getCourseType,courseStudyhistory.getCourseType())
				.eq(CourseStudyhistory::getKpointCourseId,courseStudyhistory.getKpointCourseId());
		CourseStudyhistory tcourse = courseStudyhistoryService.getOne(courseStudyhistoryQueryWrapper);

		courseStudyhistory.setPlayercount(1);
		if(Func.isNotEmpty(tcourse)){
			courseStudyhistory = tcourse;
			courseStudyhistory.setPlayercount(courseStudyhistory.getPlayercount()+1);
		}
		courseStudyhistory.setUpdateTime(new Date());
		return R.status(courseStudyhistoryService.saveOrUpdate(courseStudyhistory));
	}


	/**
	 * 删除 课程播放记录(学习记录)
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(courseStudyhistoryService.removeByIds(Func.toIntList(ids)));
	}

	/**
	 * 分课程播放记录(学习记录)
	 */
	@GetMapping("/courselist")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "分页", notes = "传入courseStudyhistory")
	public R<IPage<CourseStudyhistory>> courselist(CourseStudyhistory courseStudyhistory, Query query) {
		IPage<CourseStudyhistory> pages = courseStudyhistoryService.selectCourseTrxorder(Condition.getPage(query),courseStudyhistory);
		courseStudyhistoryService.initCourseUserListStudyHistory(pages.getRecords());
		return R.data(pages);
	}

	/**
	 * 分页 课程播放记录(学习记录)
	 */
	@GetMapping("/getcourselist")
	@ApiOperationSupport(order = 10)
	@ApiOperation(value = "分页", notes = "传入courseStudyhistory")
	public R<List<CourseStudyhistoryVO>> getcourselist(CourseStudyhistory courseStudyhistory) {
		List<CourseStudyhistory> pages = courseStudyhistoryService.getCourseList(courseStudyhistory);
		for(CourseStudyhistory history:pages){
			CourseStudyhistory course=courseStudyhistoryService.getStudyHistory(courseStudyhistory.getUserId(),history.getKpointId(),courseStudyhistory.getCoursePackageId(),0);
			if(history.getParentId()!=0){
				if(course!=null){
					/*累计学习时长*/
					if(course.getWatchTime()>0){
						history.setWatchStingTime(courseStudyhistoryService.gitTime(course.getWatchTime()));
					}else {
						history.setWatchStingTime("暂未学习时长");
					}
					/*学习进度*/
					history.setStudyLearning(course.getStudyLearning());
					/*是否完成*/
					history.setComplete(course.getComplete());
					/*最后学习时长*/
					history.setUpdateTime(course.getUpdateTime());
				}else {
					/*累计学习时长*/
					history.setWatchStingTime("暂未学习时长");
					/*学习进度*/
					history.setStudyLearning("暂未学习进度");
					/*是否完成*/
					history.setComplete("1");
				}
			}
		}
		return R.data(CourseStudyHistoryWrapper.build().listNodeVO(pages));
	}


    /**
     * 分页 课程播放记录(学习记录)
     */
    @GetMapping("/getkpointlist")
    @ApiOperationSupport(order = 11)
    @ApiOperation(value = "分页", notes = "传入courseStudyhistory")
    public R<List<CourseStudyhistoryVO>> getkpointlist(CourseStudyhistory courseStudyhistory) {
        List<CourseStudyhistory> courseStudyhistoryLIst = courseStudyhistoryService.getCourseList(courseStudyhistory);
        //把前台传来的套餐id 放入coursePackageId属性中
		courseStudyhistory.setCoursePackageId(courseStudyhistory.getKpointCourseId());
		//查询课程章节的学习记录数据，整理查询并放入list对象中
		courseStudyhistoryService.queryCourseKpointDateStudyHistoryInit(courseStudyhistoryLIst,courseStudyhistory);
        return R.data(CourseStudyHistoryWrapper.build().listNodeVO(courseStudyhistoryLIst));
    }

	/**
	 * 分课程播放记录(学习记录)
	 */
	@GetMapping("/getkpointStudentlist")
	@ApiOperationSupport(order = 12)
	@ApiOperation(value = "分页", notes = "传入courseStudyhistory")
	public R<IPage<CourseStudyhistory>> getkpointStudentlist(CourseStudyhistory courseStudyhistory, Query query) {
		IPage<CourseStudyhistory> pages = null;
		int studyType=1;
		CourseStudyhistory courseStudyhistory1 = new CourseStudyhistory();
		if("PACKAGE".equals(courseStudyhistory.getCourseType())){
			studyType=2;
			if("ALL".equals(courseStudyhistory.getPopeType())){
				/*全部*/
				/*所有的人信息*/
				 pages = courseStudyhistoryService.getPackageUserList(Condition.getPage(query),courseStudyhistory);
				 courseStudyhistory1 =courseStudyhistoryService.getKpointAndCourse(courseStudyhistory.getCourseId(),courseStudyhistory.getKpointId());
			}else if("SHOULD".equals(courseStudyhistory.getPopeType())){
				/*已学习*/
				pages=courseStudyhistoryService.selectPackageStudentShouldAndComplete(Condition.getPage(query),courseStudyhistory);
			}else if("COMPLETE".equals(courseStudyhistory.getPopeType())){
				/*完成学员*/
				pages=courseStudyhistoryService.selectPackageStudentShouldAndComplete(Condition.getPage(query),courseStudyhistory);
			}else if("NO".equals(courseStudyhistory.getPopeType())){
				/*未学习学员*/
				courseStudyhistory1 =courseStudyhistoryService.getKpointAndCourse(courseStudyhistory.getCourseId(),courseStudyhistory.getKpointId());
				pages=courseStudyhistoryService.selectPackageNoStudy(Condition.getPage(query),courseStudyhistory);
			}

		}else {
			if("ALL".equals(courseStudyhistory.getPopeType())){
				/*全部*/
				pages=courseStudyhistoryService.selectCourseUser(Condition.getPage(query),courseStudyhistory);
			}else if("SHOULD".equals(courseStudyhistory.getPopeType())){
				/*已学习*/
				pages=courseStudyhistoryService.selectStudentShouldAndComplete(Condition.getPage(query),courseStudyhistory);
			}else if("COMPLETE".equals(courseStudyhistory.getPopeType())){
				/*完成学员*/
				pages=courseStudyhistoryService.selectStudentShouldAndComplete(Condition.getPage(query),courseStudyhistory);
			}else if("NO".equals(courseStudyhistory.getPopeType())){
				/*未学习学员*/
				pages=courseStudyhistoryService.selectNoStudy(Condition.getPage(query),courseStudyhistory);
			}
		}
		for(CourseStudyhistory prpo:pages.getRecords()){
			if("PACKAGE".equals(courseStudyhistory.getCourseType())){
				/*查课程及章节信息信息*/
				if("ALL".equals(courseStudyhistory.getPopeType())||"NO".equals(courseStudyhistory.getPopeType())){
					/*全部*/
					prpo.setCourseName(courseStudyhistory1.getCourseName());
					prpo.setCourseId(courseStudyhistory1.getCourseId());
					prpo.setKpointId(courseStudyhistory1.getKpointId());
					prpo.setKpointName(courseStudyhistory1.getKpointName());
					prpo.setVideoTime(courseStudyhistory1.getVideoTime());
				}
			}
			CourseStudyhistory course=courseStudyhistoryService.getStudyHistory(prpo.getUserId(),courseStudyhistory.getKpointId(),0,studyType);
			if(course!=null){
				prpo=huanCourseStudyhistory(course,prpo);
			}else {
				/*累计学习时长*/
				prpo.setWatchStingTime("暂未学习时长");
				/*学习进度*/
				prpo.setStudyLearning("暂未学习进度");
				/*是否完成*/
				prpo.setComplete("1");
			}
		}
		return R.data(pages);
	}
	/**
	 * 分页 课程播放记录(学习记录)
	 */
	@GetMapping("/getpackagelist")
	@ApiOperationSupport(order = 14)
	@ApiOperation(value = "分页", notes = "传入courseStudyhistory")
	public R<IPage<CourseStudyhistory>> getpackagelist(CourseStudyhistory courseStudyhistory, Query query) {
		IPage<CourseStudyhistory> pages = courseStudyhistoryService.getCoursepackageList(Condition.getPage(query),courseStudyhistory);
		List<CourseStudyhistory> userList = courseStudyhistoryService.getPackageList(courseStudyhistory);
		String userListString="";
		int i=0;
		for(CourseStudyhistory user:userList){
			if(i==0){
				userListString+=user.getUserId();
			}else {
				userListString+=","+user.getUserId();
			}
			i+=1;
		}
		for(CourseStudyhistory history:pages.getRecords()){
			/*总共观看人数*/
			history.setPlayercount(courseStudyhistoryService.getCourseWatchNum(history.getCourseId(),userListString));
			/*课程总时长*/
			history.setVideoTime(courseStudyhistoryService.gitTime(courseStudyhistoryService.getCourseWatchTime(history.getCourseId())));
			/*应学人数*/
			history.setStudyPope(i);
			history.setKpointCourseId(courseStudyhistory.getCourseId());
		}

		return R.data(pages);
	}

	/**
	 * 导出用户订单
	 */
	@SneakyThrows
	@GetMapping("export-userStudyHistory")
	@ApiOperationSupport(order = 13)
	@ApiOperation(value = "导出用户订单	", notes = "传入UserAccountHistory")
	public void exportUserAccountHistory(@ApiIgnore @RequestParam Map<String, Object> CourseStudyhistory, HttpServletResponse response) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String date=simpleDateFormat.format(new Date());
		CourseStudyhistory.remove("crazy-auth");
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding(Charsets.UTF_8.name());
		String fileName = URLEncoder.encode("用户学习记录导出"+date, Charsets.UTF_8.name());
		response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
		/*courseStudyhistoryService.exportUserStudyHistory(response , CourseStudyhistory);*/

	}



	private CourseStudyhistory huanCourseStudyhistory(CourseStudyhistory courseStudyhistory,CourseStudyhistory newcourseStudyhistory){
		/*累计学习时长*/
		if(courseStudyhistory.getWatchTime()>0){
			newcourseStudyhistory.setWatchStingTime(courseStudyhistoryService.gitTime(courseStudyhistory.getWatchTime()));
		}else {
			newcourseStudyhistory.setWatchStingTime("暂未学习时长");
		}
		/*学习进度*/
		newcourseStudyhistory.setStudyLearning(courseStudyhistory.getStudyLearning());
		/*是否完成*/
		newcourseStudyhistory.setComplete(courseStudyhistory.getComplete());
		/*最后学习时长*/
		newcourseStudyhistory.setUpdateTime(courseStudyhistory.getUpdateTime());
		/*最后学习时长*/
		newcourseStudyhistory.setBeginsTime(courseStudyhistory.getBeginsTime());
		/*最后学习时长*/
		newcourseStudyhistory.setEndTime(courseStudyhistory.getEndTime());
		return newcourseStudyhistory;
	}


	/**
	 * 分课程播放记录(学习记录)
	 */
	@GetMapping("/getCourseStudentlist")
	@ApiOperationSupport(order = 15)
	@ApiOperation(value = "分页", notes = "传入courseStudyhistory")
	public R<IPage<CourseStudyhistory>> getCourseStudentlist(CourseStudyhistory courseStudyhistory, Query query) {
		IPage<CourseStudyhistory> pages = null;
		pages = courseStudyhistoryService.getCourseUserList(Condition.getPage(query),courseStudyhistory);

		return R.data(pages);
	}

	/**
	 * 分页 课程播放记录(学习记录)
	 */
	@GetMapping("/getpackageStudentStudyRecordlist")
	@ApiOperationSupport(order = 16)
	@ApiOperation(value = "分页", notes = "传入courseStudyhistory")
	public R<IPage<CourseStudyhistory>> getpackageStudentStudyRecordlist(CourseStudyhistory courseStudyhistory, Query query) {
		courseStudyhistory.setCourseId(courseStudyhistory.getCoursePackageId());
		IPage<CourseStudyhistory> pages = courseStudyhistoryService.getCoursepackageList(Condition.getPage(query),courseStudyhistory);
		for(CourseStudyhistory courseStudy:pages.getRecords()){
			courseStudy.setUserId(courseStudyhistory.getUserId());
			courseStudy.setCoursePackageId(courseStudyhistory.getCoursePackageId());
			courseStudyhistoryService.initCoursePackageCourseListStudyHistory(courseStudy);
		}

		return R.data(pages);
	}


	/**
	 * 用户学习记录导出
	 */
	@SneakyThrows
	@GetMapping("/export-user")
	@ApiOperationSupport(order = 10)
	@ApiOperation(value = "分页", notes = "传入courseStudyhistory")
	public void getcourselistElex(@ApiIgnore @RequestParam Map<String, Object>  courseStudyhistory, HttpServletResponse response) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		String date=simpleDateFormat.format(new Date());
		courseStudyhistory.remove("crazy-auth");
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding(org.apache.commons.codec.Charsets.UTF_8.name());
		String fileName = URLEncoder.encode("用户学习记录数据导出"+date, org.apache.commons.codec.Charsets.UTF_8.name());
		response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
		CourseStudyhistory courseStudyhistory1 = new CourseStudyhistory();
		courseStudyhistory1.setUserId(Integer.valueOf(courseStudyhistory.get("userId").toString()));
		courseStudyhistory1.setCourseId(Integer.valueOf(courseStudyhistory.get("courseId").toString()));
		if(Func.isNotEmpty(courseStudyhistory.get("coursePackageId"))&&!("undefined").equals(courseStudyhistory.get("coursePackageId"))){
			courseStudyhistory1.setCoursePackageId(Integer.valueOf(courseStudyhistory.get("coursePackageId").toString()));
		}
		courseStudyhistoryService.exportUser(response , courseStudyhistory1);
	}


	/**
	 * 课程章节学习记录导出
	 */
	@SneakyThrows
	@GetMapping("/exportKpointIdUser")
	@ApiOperationSupport(order = 10)
	@ApiOperation(value = "分页", notes = "传入courseStudyhistory")
	public void getcoursekpointIdExcel(@ApiIgnore @RequestParam Map<String, Object>  courseStudyhistory, HttpServletResponse response) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		String date=simpleDateFormat.format(new Date());
		courseStudyhistory.remove("crazy-auth");
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding(org.apache.commons.codec.Charsets.UTF_8.name());
		String studentName="";
		if("ALL".equals(courseStudyhistory.get("popeType").toString())){
			//全部
			studentName="全部学员";
		}else if("SHOULD".equals(courseStudyhistory.get("popeType").toString())){
			//已学习
			studentName="已学习学员";
		}else if("COMPLETE".equals(courseStudyhistory.get("popeType").toString())){
			//完成学员
			studentName="完成学员";
		}else if("NO".equals(courseStudyhistory.get("popeType").toString())){
			//未学习学员
			studentName="未学习学员";
		}
		CourseKpoint courseKpoint =courseKpointService.getById(Integer.valueOf(courseStudyhistory.get("kpointId").toString()));
		String fileName = URLEncoder.encode(courseKpoint.getName()+"("+studentName+"学习记录)"+date, org.apache.commons.codec.Charsets.UTF_8.name());
		response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
		CourseStudyhistory courseStudyhistory1 = new CourseStudyhistory();
		courseStudyhistory1.setCourseId(Integer.valueOf(courseStudyhistory.get("courseId").toString()));
		courseStudyhistory1.setPopeType(courseStudyhistory.get("popeType").toString());
		courseStudyhistory1.setKpointId(Integer.valueOf(courseStudyhistory.get("kpointId").toString()));
		courseStudyhistory1.setCourseType(courseStudyhistory.get("courseType").toString());
		if(Func.isNotEmpty(courseStudyhistory.get("kpointCourseId"))&&!("undefined").equals(courseStudyhistory.get("kpointCourseId"))){
			courseStudyhistory1.setKpointCourseId(Integer.valueOf(courseStudyhistory.get("kpointCourseId").toString()));
		}
		courseStudyhistoryService.exportKpointIdUser(response , courseStudyhistory1);
	}
}
