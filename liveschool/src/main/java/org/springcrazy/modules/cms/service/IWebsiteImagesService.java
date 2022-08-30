
package org.springcrazy.modules.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.cms.entity.WebsiteImages;
import org.springcrazy.modules.cms.vo.WebsiteImagesVO;

/**
 * banner图管理 服务类
 *
 * @author TongZhou
 * @since 2020-03-06
 */
public interface IWebsiteImagesService extends IService<WebsiteImages> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param websiteImages
	 * @return
	 */
	IPage<WebsiteImagesVO> selectWebsiteImagesPage(IPage<WebsiteImagesVO> page, WebsiteImagesVO websiteImages);

}
