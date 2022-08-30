
package org.springcrazy.modules.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.web.entity.HelpMenu;
import org.springcrazy.modules.web.vo.HelpMenuVO;

import java.util.List;

/**
 * 帮助菜单 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-05-09
 */
public interface HelpMenuMapper extends BaseMapper<HelpMenu> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param helpMenu
	 * @return
	 */
	List<HelpMenuVO> selectHelpMenuPage(IPage page, HelpMenuVO helpMenu);

	/**
	 * 树形结构
	 *
	 * @return
	 */
	List<HelpMenuVO> tree();

}
