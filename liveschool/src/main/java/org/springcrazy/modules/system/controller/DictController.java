
package org.springcrazy.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springcrazy.core.boot.ctrl.CrazyController;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.mp.support.Query;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.node.INode;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.system.entity.Dict;
import org.springcrazy.modules.system.service.IDictService;
import org.springcrazy.modules.system.vo.DictVO;
import org.springcrazy.modules.system.wrapper.DictWrapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static org.springcrazy.common.cache.CacheNames.DICT_LIST;
import static org.springcrazy.common.cache.CacheNames.DICT_VALUE;

/**
 * 控制器
 *

 */
@RestController
@AllArgsConstructor
@RequestMapping("/crazy-system/dict")
@ApiIgnore
@Api(value = "字典", tags = "字典")
public class DictController extends CrazyController {

	private IDictService dictService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入dict")
	public R<DictVO> detail(Dict dict) {
		Dict detail = dictService.getOne(Condition.getQueryWrapper(dict));
		return R.data(DictWrapper.build().entityVO(detail));
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "code", value = "字典编号", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "dictValue", value = "字典名称", paramType = "query", dataType = "string")
	})
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "列表", notes = "传入dict")
	public R<List<INode>> list(@ApiIgnore @RequestParam Map<String, Object> dict) {
		@SuppressWarnings("unchecked")
		List<Dict> list = dictService.list(Condition.getQueryWrapper(dict, Dict.class).lambda().orderByAsc(Dict::getSort));
		return R.data(DictWrapper.build().listNodeVO(list));
	}


	/**
	 * 分页 定时任务
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入Dict")
	public R<IPage<Dict>> page(Dict dict, Query query) {
		LambdaQueryWrapper<Dict> dictLambdaQueryWrapper = Condition.getQueryWrapper(new Dict()).lambda().orderByDesc(Dict::getSort);
		if(Func.isEmpty(dict.getParentId())){
			dict.setParentId(0);
			dictLambdaQueryWrapper.eq(Dict::getParentId,0);
		}
		dictLambdaQueryWrapper.like(Func.isNotBlank(dict.getCode()),Dict::getCode,dict.getCode())
				.like(Func.isNotBlank(dict.getDictValue()),Dict::getDictValue,dict.getDictValue())
				.eq(Func.isNotEmpty(dict.getParentId()),Dict::getParentId,dict.getParentId()).orderByDesc(Dict::getSort);
		IPage<Dict> pages = dictService.page(Condition.getPage(query), dictLambdaQueryWrapper);
		return R.data(pages);
	}
	/**
	 * 获取字典树形结构
	 *
	 * @return
	 */
	@GetMapping("/tree")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "树形结构", notes = "树形结构")
	public R<List<DictVO>> tree() {
		List<DictVO> tree = dictService.tree();
		return R.data(tree);
	}

	/**
	 * 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增或修改", notes = "传入dict")
	public R submit(@Valid @RequestBody Dict dict) {
		return R.status(dictService.submit(dict));
	}


	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 5)
	@CacheEvict(cacheNames = {DICT_LIST, DICT_VALUE}, allEntries = true)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(dictService.removeByIds(Func.toIntList(ids)));
	}

	/**
	 * 获取字典
	 *
	 * @return
	 */
	@GetMapping("/dictionary")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "获取字典", notes = "获取字典")
	public R<List<Dict>> dictionary(String code) {
		List<Dict> tree = dictService.getList(code);
		for (int i = 0; i < tree.size() - 1; i++) {
			for (int j = 1; j < tree.size() - i; j++) {
				Dict a = new Dict();
				if ((tree.get(j - 1).getSort()).compareTo(tree.get(j).getSort()) < 0) { // 比较两个整数的大小
					a = tree.get(j - 1);
					tree.set((j - 1), tree.get(j));
					tree.set(j, a);
				}
			}
		}
		return R.data(tree);
	}

	/**
	 * 获取字典
	 *
	 * @return
	 */
	@GetMapping("/select")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "获取字典", notes = "获取字典")
	public R<List<Dict>> select(String code) {
		Dict dict = new Dict();
		dict.setDictKey("-1");
		List<Dict> tree = dictService.list(new QueryWrapper<>(dict));
		tree.stream().forEach(t->{
			t.setDictValue(t.getDictValue()+"("+t.getCode()+")");
		});
		return R.data(tree);
	}
}
