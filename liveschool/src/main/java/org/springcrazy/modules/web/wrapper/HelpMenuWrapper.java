
package org.springcrazy.modules.web.wrapper;

import org.springcrazy.common.constant.CommonConstant;
import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.node.ForestNodeMerger;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.core.tool.utils.SpringUtil;
import org.springcrazy.modules.web.entity.HelpMenu;
import org.springcrazy.modules.web.service.IHelpMenuService;
import org.springcrazy.modules.web.vo.HelpMenuVO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 包装类,返回视图层所需的字段

 */
public class HelpMenuWrapper extends BaseEntityWrapper<HelpMenu, HelpMenuVO> {

	private static IHelpMenuService helpMenuService;


	static {
		helpMenuService = SpringUtil.getBean(IHelpMenuService.class);
	}

	public static HelpMenuWrapper build() {
		return new HelpMenuWrapper();
	}

	@Override
	public HelpMenuVO entityVO(HelpMenu helpMenu) {
		HelpMenuVO helpMenuVO = BeanUtil.copy(helpMenu, HelpMenuVO.class);
		if (Func.equals(helpMenu.getParentId(), CommonConstant.TOP_PARENT_ID)) {
			helpMenuVO.setParentName(CommonConstant.TOP_PARENT_NAME);
		} else {
			HelpMenu parent = helpMenuService.getById(helpMenu.getParentId());
			helpMenuVO.setParentName(parent.getName());
		}
		return helpMenuVO;
	}


	public List<HelpMenuVO> listNodeVO(List<HelpMenu> list) {
		List<HelpMenuVO> collect = list.stream().map(helpMenu -> BeanUtil.copy(helpMenu, HelpMenuVO.class)).collect(Collectors.toList());
		return ForestNodeMerger.merge(collect);
	}

}
