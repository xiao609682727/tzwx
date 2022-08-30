
package org.springcrazy.modules.edu.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springcrazy.core.boot.ctrl.CrazyController;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.mp.support.Query;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.edu.entity.Course;
import org.springcrazy.modules.edu.entity.CourseKpoint;
import org.springcrazy.modules.edu.service.ICourseKpointService;
import org.springcrazy.modules.edu.service.ICourseService;
import org.springcrazy.modules.system.excel.GiveCourseExcel;
import org.springcrazy.modules.system.excel.GiveCourseImportListener;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 课程表 控制器
 *
 * @author TongZhou
 * @since 2020-04-25
 */
@RestController
@AllArgsConstructor
@RequestMapping("/edu/course")
@Api(value = "课程表", tags = "课程表接口")
public class CourseController extends CrazyController {

	private ICourseService courseService;
	private ICourseKpointService courseKpointService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入course")
	public R<Course> detail(Course course) {
		Course detail = courseService.getOne(Condition.getQueryWrapper(course));
		return R.data(detail);
	}

	/**
	 * 分页 课程表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入course")
	public R<IPage<Course>> list(Course course, Query query) {
		String courseName = course.getCourseName();
		Integer courseId = course.getId();
		course.setCourseName(null);
		course.setId(null);
		QueryWrapper<Course> queryWrapper = Condition.getQueryWrapper(course);
		queryWrapper.lambda().like(Func.isNotBlank(courseName),Course::getCourseName,courseName)
				.like(Func.isNotEmpty(courseId),Course::getId,courseId);
		queryWrapper.lambda().orderByDesc(Course::getCreateTime).orderByDesc(Course::getSort);
		IPage<Course> pages = courseService.page(Condition.getPage(query), queryWrapper);
		return R.data(pages);
	}

	/**
	 * 自定义分页 课程表
	 * 用于查询课程  排除 套餐类型
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入course")
	public R<IPage<Course>> page(Course course, Query query) {
		QueryWrapper<Course> queryWrapper = Condition.getQueryWrapper(course);
		queryWrapper.lambda().like(Func.isNotBlank(course.getCourseName()),Course::getCourseName,course.getCourseName());
		course.setCourseName(null);
		queryWrapper.in("sell_type","1","2");
		queryWrapper.orderByDesc("sort DESC,create_time");
		IPage<Course> pages = courseService.page(Condition.getPage(query), queryWrapper);
		return R.data(pages);
	}


	/**
	 * 自定义分页 课程表
	 * 用于查询课程  排除 套餐类型
	 */
	@GetMapping("/ClassCoursePage")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入course")
	public R<IPage<Course>> ClassCoursePage(Course course, Query query) {
		QueryWrapper<Course> queryWrapper = Condition.getQueryWrapper(course);
		queryWrapper.lambda().like(Func.isNotBlank(course.getCourseName()),Course::getCourseName,course.getCourseName());
		course.setCourseName(null);
		queryWrapper.orderByDesc("sort");
		IPage<Course> pages = courseService.page(Condition.getPage(query), queryWrapper);
		return R.data(pages);
	}

	/**
	 * 新增 课程表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入course")
	public R save(@Valid @RequestBody Course course) {
		return R.status(courseService.save(course));
	}

	/**
	 * 修改 课程表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入course")
	public R update(@Valid @RequestBody Course course) {
		return R.status(courseService.updateById(course));
	}

	/**
	 * 新增或修改 课程表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入course")
	public R submit(@Valid @RequestBody Course course) {
		return R.status(courseService.saveOrUpdate(course));
	}


	/**
	 * 删除 课程表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(courseService.removeByIds(Func.toIntList(ids)));
	}

	/**
	 * 赠送 课程
	 */
	@PostMapping("/give")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "赠送", notes = "传入course")
	public R give(String userIds,String courseIds,@RequestParam(defaultValue = "COURSE") String orderType) {

		courseService.giveCourse(userIds,courseIds);
		return R.status(true);
	}
	/**
	 * 赠送 课程
	 */
	@PostMapping("/openCourse")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "开通课程", notes = "传入courseIds和userIds")
	public R openCourse(String userIds,String courseIds,@RequestParam(defaultValue = "COURSE") String orderType) {
		courseService.openCourse(userIds,courseIds);
		return R.status(true);
	}
	/**
	 * 导入用户赠送的课程
	 */
	@PostMapping("import-course-user")
	@ApiOperationSupport(order = 12)
	@ApiOperation(value = "导入用户赠送的课程", notes = "传入excel")
	public R importCourseUser(MultipartFile file) {
		String filename = file.getOriginalFilename();
		if (StringUtils.isEmpty(filename)) {
			throw new RuntimeException("请上传文件!");
		}
		if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
			throw new RuntimeException("请上传正确的excel文件!");
		}
		InputStream inputStream;
		try {
			GiveCourseImportListener importListener = new GiveCourseImportListener(courseService);
			inputStream = new BufferedInputStream(file.getInputStream());
			ExcelReaderBuilder builder = EasyExcel.read(inputStream, GiveCourseExcel.class, importListener);
			builder.doReadAll();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return R.success("操作成功");
	}

	@PostMapping("/kpoints")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "检测课程中是否有章节", notes = "传入courseId")
	public R selectCourseKpoint(Integer courseId) {
		CourseKpoint courseKpoint = new CourseKpoint();
		courseKpoint.setCourseId(courseId);
		List<CourseKpoint> list = courseKpointService.list(Condition.getQueryWrapper(courseKpoint).lambda().orderByDesc(CourseKpoint::getSort));

		//判断是否有章节
		Boolean status = false;
		if (list.size() > 0){
			status = true;
		}
		return R.data(status);
	}
	/**
	 * 所有的课程
	 */
	@GetMapping("/allList")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入course")
	public R<List<Course>> allList(Course course, Query query) {
		String courseName = course.getCourseName();
		Integer courseId = course.getId();
		course.setCourseName(null);
		course.setId(null);
		QueryWrapper<Course> queryWrapper = Condition.getQueryWrapper(course);
		queryWrapper.lambda().like(Func.isNotBlank(courseName),Course::getCourseName,courseName)
				.like(Func.isNotEmpty(courseId),Course::getId,courseId);
		queryWrapper.lambda().orderByDesc(Course::getCreateTime).orderByDesc(Course::getSort);
		List<Course> pages = courseService.list(queryWrapper);
		List<Course> courseList = new ArrayList();
		for(Course course1:pages){
			Course course2 = new Course();
			course2.setId(course1.getId());
			course2.setCourseName(course1.getCourseName());
			courseList.add(course2);

		}
		return R.data(courseList);
	}
}
