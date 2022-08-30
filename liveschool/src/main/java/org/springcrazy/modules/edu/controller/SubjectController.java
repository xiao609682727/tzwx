
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
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.core.tool.utils.ObjectUtil;
import org.springcrazy.modules.edu.entity.Subject;
import org.springcrazy.modules.edu.service.ISubjectService;
import org.springcrazy.modules.edu.vo.SubjectVO;
import org.springcrazy.modules.edu.wrapper.SubjectWrapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 专业分类 控制器
 *
 * @author TongZhou
 * @since 2020-04-25
 */
@RestController
@AllArgsConstructor
@RequestMapping("/edu/subject")
@Api(value = "专业分类", tags = "专业分类接口")
public class SubjectController extends CrazyController {

	private ISubjectService subjectService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入subject")
	public R<Subject> detail(Subject subject) {
		Subject detail = subjectService.getOne(Condition.getQueryWrapper(subject));
		return R.data(detail);
	}

	/**
	 * 分页 专业分类
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入subject")
	public R<List<SubjectVO>> list(Subject subject) {
		List<Subject> list = new ArrayList<>();
		if(ObjectUtil.isNotEmpty(subject.getType())){
			if("lineclass".equals(subject.getType())){
				QueryWrapper<Subject> subjectWrapper = Condition.getQueryWrapper(new Subject());
				subjectWrapper.lambda().like(Func.isNotBlank(subject.getSubjectName()),Subject::getSubjectName,subject.getSubjectName())
						.eq(Subject::getType, subject.getType()).orderByDesc(Subject::getSort);
				list =subjectService.list(subjectWrapper);
			}else {
				list =subjectService.list(Condition.getQueryWrapper(subject).lambda().orderByDesc(Subject::getSort));
			}
		}else {
			list =subjectService.list(Condition.getQueryWrapper(subject).lambda().orderByDesc(Subject::getSort));
		}
		return R.data(SubjectWrapper.build().listNodeVO(list));
	}

	/**
	 * 自定义分页 专业分类
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入subject")
	public R<IPage<SubjectVO>> page(SubjectVO subject, Query query) {
		IPage<SubjectVO> pages = subjectService.selectSubjectPage(Condition.getPage(query), subject);
		return R.data(pages);
	}

	/**
	 * 新增 专业分类
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入subject")
	public R save(@Valid @RequestBody Subject subject) {
		return R.status(subjectService.save(subject));
	}

	/**
	 * 修改 专业分类
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入subject")
	public R update(@Valid @RequestBody Subject subject) {
		if(subject.getParentId()!=0&&"exam".equals(subject.getType())){
			Subject subject1 =subjectService.getSubjectByName(subject.getSubjectName());
			if(subject1!=null){
				return R.fail("该分类名称已有，不可添加重复名称");
			}
		}
		return R.status(subjectService.updateById(subject));
	}

	/**
	 * 新增或修改 专业分类
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入subject")
	public R submit(@Valid @RequestBody Subject subject) {
		if(Func.isNull(subject.getParentId())){
			subject.setParentId(0);
		}
		if(subject.getParentId()!=0&&"exam".equals(subject.getType())){
			Subject subject1 =subjectService.getSubjectByName(subject.getSubjectName());
			if(subject1!=null){
				return R.fail("该分类名称已有，不可添加重复名称");
			}
		}
		return R.status(subjectService.saveOrUpdate(subject));
	}


	/**
	 * 删除 专业分类
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(subjectService.deleteIds(ids));
	}

	/**
	 * 获取菜单树形结构
	 */
	@GetMapping("/tree")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "树形结构", notes = "树形结构")
	public R<List<SubjectVO>> tree() {
		List<SubjectVO> tree = subjectService.tree();
		return R.data(tree);
	}

	/**
	 * 获取活动菜单树形结构
	 */
	@GetMapping("/lineClasstree")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "树形结构", notes = "树形结构")
	public R<List<SubjectVO>> lineClasstree() {
		List<SubjectVO> tree = subjectService.lineClasstree();
		return R.data(tree);
	}

	/**
	 *  查询专业列表
	 */
	@GetMapping("/subjectList")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入subject")
	public R<List<Subject>> subjectList(Subject subject) {
		List<Subject> list = subjectService.list(Condition.getQueryWrapper(subject).lambda().orderByDesc(Subject::getSort));
		return R.data(list);
	}


	/**
	 * 获取考试类型专业树形结构
	 */
	@GetMapping("/exam/tree")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "树形结构", notes = "树形结构")
	public R<List<SubjectVO>> examTree() {
		List<SubjectVO> tree = subjectService.examTree();
		return R.data(tree);
	}


}
