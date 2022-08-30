
package org.springcrazy.modules.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springcrazy.core.boot.ctrl.CrazyController;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.mp.support.Query;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.user.entity.Teacher;
import org.springcrazy.modules.user.service.ITeacherService;
import org.springcrazy.modules.user.vo.TeacherVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * 讲师 控制器
 *
 * @author TongZhou
 * @since 2020-04-28
 */
@RestController
@AllArgsConstructor
@RequestMapping("/user/teacher")
@Api(value = "讲师", tags = "讲师接口")
public class TeacherController extends CrazyController {

	private ITeacherService teacherService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入teacher")
	public R<Teacher> detail(Teacher teacher) {
		Teacher detail = teacherService.getOne(Condition.getQueryWrapper(teacher));
		return R.data(detail);
	}

	/**
	 * 分页 讲师
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入teacher")
	public R<IPage<Teacher>> list(Teacher teacher, Query query) {
		Map<String, Object> param = Maps.newHashMap();
		param.put("name_like",teacher.getName());
		param.put("subjectId_equal",teacher.getSubjectId());
		param.put("teaTitle_equal",teacher.getTeaTitle());

		QueryWrapper<Teacher> queryWrapper = Condition.getQueryWrapper(param, Teacher.class);
		queryWrapper.orderByDesc("sort desc,id");
		IPage<Teacher> pages = teacherService.page(Condition.getPage(query),queryWrapper );
		return R.data(pages);
	}

	/**
	 * 自定义分页 讲师
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入teacher")
	public R<IPage<TeacherVO>> page(TeacherVO teacher, Query query) {
		IPage<TeacherVO> pages = teacherService.selectTeacherPage(Condition.getPage(query), teacher);
		return R.data(pages);
	}

	/**
	 * 新增 讲师
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入teacher")
	public R save(@Valid @RequestBody Teacher teacher) {
		return R.status(teacherService.save(teacher));
	}

	/**
	 * 修改 讲师
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入teacher")
	public R update(@Valid @RequestBody Teacher teacher) {
		return R.status(teacherService.updateById(teacher));
	}

	/**
	 * 新增或修改 讲师
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入teacher")
	public R submit(@Valid @RequestBody Teacher teacher) {
		return R.status(teacherService.saveOrUpdate(teacher));
	}


	/**
	 * 删除 讲师
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(teacherService.removeByIds(Func.toIntList(ids)));
	}


}
