
package org.springcrazy.modules.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.core.mp.base.BaseService;
import org.springcrazy.modules.cms.entity.CmsSubject;
import org.springcrazy.modules.cms.vo.CmsSubjectVO;

/**
 * 专业信息 服务类
 *
 * @author TongZhou
 * @since 2020-02-28
 */
public interface ICmsSubjectService extends BaseService<CmsSubject> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param subject
	 * @return
	 */
	IPage<CmsSubjectVO> selectSubjectPage(IPage<CmsSubjectVO> page, CmsSubjectVO subject);

	CmsSubject getSubject(Integer id);

	boolean deleteIds(String ids);

}
