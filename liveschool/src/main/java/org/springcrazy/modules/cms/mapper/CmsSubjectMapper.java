
package org.springcrazy.modules.cms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.cms.entity.CmsSubject;
import org.springcrazy.modules.cms.vo.CmsSubjectVO;

import java.util.List;

/**
 * 专业信息 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-02-28
 */
public interface CmsSubjectMapper extends BaseMapper<CmsSubject> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param subject
	 * @return
	 */
	List<CmsSubjectVO> selectSubjectPage(IPage page, CmsSubjectVO subject);

}
