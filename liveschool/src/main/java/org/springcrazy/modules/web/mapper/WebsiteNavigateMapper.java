
package org.springcrazy.modules.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.web.entity.WebsiteNavigate;
import org.springcrazy.modules.web.vo.WebsiteNavigateVO;

import java.util.List;

/**
 * 导航表 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-05-09
 */
public interface WebsiteNavigateMapper extends BaseMapper<WebsiteNavigate> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param websiteNavigate
	 * @return
	 */
	List<WebsiteNavigateVO> selectWebsiteNavigatePage(IPage page, WebsiteNavigateVO websiteNavigate);

}
