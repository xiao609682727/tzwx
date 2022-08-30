
package org.springcrazy.modules.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.web.entity.HelpMenu;
import org.springcrazy.modules.web.vo.HelpMenuVO;

import java.util.List;

/**
 * 帮助菜单 服务类
 *
 * @author TongZhou
 * @since 2020-05-09
 */
public interface IHelpMenuService extends IService<HelpMenu> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param helpMenu
	 * @return
	 */
	IPage<HelpMenuVO> selectHelpMenuPage(IPage<HelpMenuVO> page, HelpMenuVO helpMenu);

	/**
	 * 树形结构
	 *
	 * @return
	 */
	List<HelpMenuVO> tree();

}
