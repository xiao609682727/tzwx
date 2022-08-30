
package org.springcrazy.modules.cms.wrapper;

import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.modules.cms.entity.CmsSubject;
import org.springcrazy.modules.cms.vo.CmsSubjectVO;

/**
 * 专业信息包装类,返回视图层所需的字段
 *
 * @author TongZhou
 * @since 2020-02-28
 */
public class CmsSubjectWrapper extends BaseEntityWrapper<CmsSubject, CmsSubjectVO>  {

    public static CmsSubjectWrapper build() {
        return new CmsSubjectWrapper();
    }

	@Override
	public CmsSubjectVO entityVO(CmsSubject subject) {
		CmsSubjectVO subjectVO = BeanUtil.copy(subject, CmsSubjectVO.class);

		return subjectVO;
	}

}
