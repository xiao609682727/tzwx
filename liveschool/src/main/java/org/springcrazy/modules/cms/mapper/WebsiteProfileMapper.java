
package org.springcrazy.modules.cms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.cms.entity.WebsiteProfile;
import org.springcrazy.modules.cms.vo.WebsiteProfileVO;

import java.util.List;

/**
 * 系统配置表 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-03-04
 */
public interface WebsiteProfileMapper extends BaseMapper<WebsiteProfile> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param websiteProfile
	 * @return
	 */
	List<WebsiteProfileVO> selectWebsiteProfilePage(IPage page, WebsiteProfileVO websiteProfile);

}
