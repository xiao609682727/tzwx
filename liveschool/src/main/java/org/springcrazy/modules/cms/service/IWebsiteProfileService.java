
package org.springcrazy.modules.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.cms.entity.WebsiteProfile;
import org.springcrazy.modules.cms.vo.WebsiteProfileVO;

import java.util.Map;

/**
 * 系统配置表 服务类
 *
 * @author TongZhou
 * @since 2020-03-04
 */
public interface IWebsiteProfileService extends IService<WebsiteProfile> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param websiteProfile
	 * @return
	 */
	IPage<WebsiteProfileVO> selectWebsiteProfilePage(IPage<WebsiteProfileVO> page, WebsiteProfileVO websiteProfile);

	Map<String,String> getConfig(String configType);
}
