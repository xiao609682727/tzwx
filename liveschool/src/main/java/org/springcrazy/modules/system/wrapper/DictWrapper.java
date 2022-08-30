
package org.springcrazy.modules.system.wrapper;

import org.springcrazy.common.constant.CommonConstant;
import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.node.ForestNodeMerger;
import org.springcrazy.core.tool.node.INode;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.core.tool.utils.SpringUtil;
import org.springcrazy.modules.system.entity.Dict;
import org.springcrazy.modules.system.service.IDictService;
import org.springcrazy.modules.system.vo.DictVO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 包装类,返回视图层所需的字段
 *

 */
public class DictWrapper extends BaseEntityWrapper<Dict, DictVO> {

	private static IDictService dictService;

	static {
		dictService = SpringUtil.getBean(IDictService.class);
	}

	public static DictWrapper build() {
		return new DictWrapper();
	}

	@Override
	public DictVO entityVO(Dict dict) {
		DictVO dictVO = BeanUtil.copy(dict, DictVO.class);
		if (Func.equals(dict.getParentId(), CommonConstant.TOP_PARENT_ID)) {
			dictVO.setParentName(CommonConstant.TOP_PARENT_NAME);
		} else {
			Dict parent = dictService.getById(dict.getParentId());
			dictVO.setParentName(parent.getDictValue());
		}
		return dictVO;
	}

	public List<INode> listNodeVO(List<Dict> list) {
		List<INode> collect = list.stream().map(dict -> BeanUtil.copy(dict, DictVO.class)).collect(Collectors.toList());
		return ForestNodeMerger.merge(collect);
	}

}
