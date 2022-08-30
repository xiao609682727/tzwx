
package org.springcrazy.modules.cms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.cms.entity.WebsiteImages;
import org.springcrazy.modules.cms.vo.WebsiteImagesVO;

import java.util.List;

/**
 * banner图管理 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-03-06
 */
public interface WebsiteImagesMapper extends BaseMapper<WebsiteImages> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param websiteImages
	 * @return
	 */
	List<WebsiteImagesVO> selectWebsiteImagesPage(IPage page, WebsiteImagesVO websiteImages);

}
