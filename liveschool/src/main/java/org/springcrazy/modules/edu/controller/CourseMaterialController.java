
package org.springcrazy.modules.edu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springcrazy.core.boot.ctrl.CrazyController;
import org.springcrazy.core.boot.file.CrazyFile;
import org.springcrazy.core.launch.props.CrazyProperties;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.mp.support.Query;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.constant.SystemConstant;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.edu.entity.CourseMaterial;
import org.springcrazy.modules.edu.service.ICourseMaterialService;
import org.springcrazy.modules.edu.vo.CourseMaterialVO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Map;

/**
 * 章节资料信息 控制器
 *
 * @author TongZhou
 * @since 2020-10-14
 */
@RestController
@AllArgsConstructor
@RequestMapping("/edu/coursematerial")
@Api(value = "章节资料信息", tags = "章节资料信息接口")
public class CourseMaterialController extends CrazyController {

	private ICourseMaterialService courseMaterialService;
	private CrazyProperties crazyProperties;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入courseMaterial")
	public R<CourseMaterial> detail(CourseMaterial courseMaterial) {
		CourseMaterial detail = courseMaterialService.getOne(Condition.getQueryWrapper(courseMaterial));
		return R.data(detail);
	}

	/**
	 * 分页 章节资料信息
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入courseMaterial")
	public R<IPage<CourseMaterial>> list(CourseMaterial courseMaterial, Query query) {
		Map<String,Object> params = Maps.newHashMap();
		params.put("kpointId_like",courseMaterial.getKpointId());
		params.put("name_like",courseMaterial.getName());
		QueryWrapper<CourseMaterial> queryWrapper = Condition.getQueryWrapper(params, CourseMaterial.class);
		queryWrapper.lambda().like(Func.isNotBlank(courseMaterial.getName()),CourseMaterial::getName,courseMaterial.getName());
		IPage<CourseMaterial> pages = courseMaterialService.page(Condition.getPage(query), queryWrapper.lambda().orderByDesc(CourseMaterial::getSort));
		return R.data(pages);
	}

	/**
	 * 自定义分页 章节资料信息
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入courseMaterial")
	public R<IPage<CourseMaterialVO>> page(CourseMaterialVO courseMaterial, Query query) {
		IPage<CourseMaterialVO> pages = courseMaterialService.selectCourseMaterialPage(Condition.getPage(query), courseMaterial);
		return R.data(pages);
	}

	/**
	 * 新增 章节资料信息
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入courseMaterial")
	public R save(@Valid @RequestBody CourseMaterial courseMaterial) {
		return R.status(courseMaterialService.save(courseMaterial));
	}

	/**
	 * 修改 章节资料信息
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入courseMaterial")
	public R update(@Valid @RequestBody CourseMaterial courseMaterial) {
		return R.status(courseMaterialService.updateById(courseMaterial));
	}

	/**
	 * 新增或修改 章节资料信息
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入courseMaterial")
	public R submit(@Valid @RequestBody CourseMaterial courseMaterial) {
		return R.status(courseMaterialService.saveOrUpdate(courseMaterial));
	}


	/**
	 * 删除 章节资料信息
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(courseMaterialService.removeByIds(Func.toIntList(ids)));
	}

	/**
	 * 资料上传请求
	 */
	@PostMapping("/fileUpload")
	@ApiOperation(value = "资料上传", notes = "资料上传")
	public R uploadCourseMaterial(MultipartFile file){

		SystemConstant me = SystemConstant.me();

		String fileName = file.getOriginalFilename();
		CrazyFile crazyFile = getFile(file,"courseMaterial");
		crazyFile.transfer();
		//上传文件
		Map<String,String> result = Maps.newHashMap();
		result.put("url", me.getDomain() + crazyFile.getUploadVirtualPath());
		result.put("fileName" , fileName);
		return R.data(result);
	}
}
