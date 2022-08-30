
package org.springcrazy.modules.edu.controller;

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
import org.springcrazy.core.secure.utils.SecureUtil;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.edu.entity.Course;
import org.springcrazy.modules.edu.entity.CourseFavorites;
import org.springcrazy.modules.edu.service.ICourseFavoritesService;
import org.springcrazy.modules.edu.service.ICourseService;
import org.springcrazy.modules.edu.vo.CourseFavoritesVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 收藏 控制器
 *
 * @author TongZhou
 * @since 2020-04-25
 */
@RestController
@AllArgsConstructor
@RequestMapping("/edu/coursefavorites")
@Api(value = "收藏", tags = "收藏接口")
public class CourseFavoritesController extends CrazyController {

	private ICourseFavoritesService courseFavoritesService;
	private ICourseService courseService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入courseFavorites")
	public R<CourseFavorites> detail(CourseFavorites courseFavorites) {
		CourseFavorites detail = courseFavoritesService.getOne(Condition.getQueryWrapper(courseFavorites));
		return R.data(detail);
	}

	/**
	 * 分页 收藏
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入courseFavorites")
	public R<IPage<CourseFavorites>> list(CourseFavorites courseFavorites, Query query) {
		QueryWrapper<CourseFavorites> courseFavoritesQueryWrapper = new QueryWrapper<>();
		courseFavoritesQueryWrapper.lambda().like(Func.isNotBlank(courseFavorites.getCourseName()),CourseFavorites::getCourseName,courseFavorites.getCourseName())
				.eq(Func.isNotEmpty(courseFavorites.getUserId()),CourseFavorites::getUserId,courseFavorites.getUserId());
		IPage<CourseFavorites> pages = courseFavoritesService.page(Condition.getPage(query), courseFavoritesQueryWrapper);
		return R.data(pages);
	}

	/**
	 * 自定义分页 收藏
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入courseFavorites")
	public R<IPage<CourseFavoritesVO>> page(CourseFavoritesVO courseFavorites, Query query) {
		//判断为学生 则进行学生id查询
		if(Func.equals(SecureUtil.getUserRole(),"student")){
			Integer userId = SecureUtil.getUserId();
			courseFavorites.setUserId(userId);
		}
		if(("5").equals(courseFavorites.getType())){
			courseFavorites.setType("4");
		}
		IPage<CourseFavoritesVO> pages = courseFavoritesService.selectCourseFavoritesPage(Condition.getPage(query), courseFavorites);
		return R.data(pages);
	}

	/**
	 * 新增 收藏
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入courseFavorites")
	public R save(@Valid @RequestBody CourseFavorites courseFavorites) {
		return R.status(courseFavoritesService.save(courseFavorites));
	}

	/**
	 * 修改 收藏
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入courseFavorites")
	public R update(@Valid @RequestBody CourseFavorites courseFavorites) {
		return R.status(courseFavoritesService.updateById(courseFavorites));
	}

	/**
	 * 新增或修改 收藏
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入courseFavorites")
	public R submit(@Valid @RequestBody CourseFavorites courseFavorites) {
		Integer userId = SecureUtil.getUserId();

		CourseFavorites cf = new CourseFavorites();
		cf.setCourseId(courseFavorites.getCourseId());
		cf.setUserId(userId);
		cf.setType(courseFavorites.getType());
		QueryWrapper<CourseFavorites> courseFavoritesQueryWrapper = new QueryWrapper<>(cf);
		int count = courseFavoritesService.count(courseFavoritesQueryWrapper);
		if(count == 0){
			Course course = courseService.getById(courseFavorites.getCourseId());
			courseFavorites.setUserId(userId);
			if(Func.isNotEmpty(course)){
				courseFavorites.setCourseName(course.getCourseName());
			}
			courseFavoritesService.saveOrUpdate(courseFavorites);
		}
		return R.data(courseFavorites);
	}
	/**
	 * 判断是否收藏过
	 */
	@GetMapping("/checkFavorites")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "判断是否收藏过", notes = "判断是否收藏过")
	public R checkFavorites(Integer courseId) {
		Integer userId = SecureUtil.getUserId();
		CourseFavorites courseFavorites = new CourseFavorites();
		courseFavorites.setUserId(userId);
		courseFavorites.setCourseId(courseId);
		List<CourseFavorites> list = courseFavoritesService.list(new QueryWrapper<>(courseFavorites));
		return R.data(list);
	}

	/**
	 * 删除 收藏
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(courseFavoritesService.removeByIds(Func.toIntList(ids)));
	}


}
