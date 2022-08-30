
package org.springcrazy.modules.exam.wrapper;

import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.modules.exam.entity.PaperType;
import org.springcrazy.modules.exam.vo.PaperTypeVO;

/**
 * 试卷类型表包装类,返回视图层所需的字段
 *
 * @author TongZhou
 * @since 2020-12-01
 */
public class PaperTypeWrapper extends BaseEntityWrapper<PaperType, PaperTypeVO>  {

    public static PaperTypeWrapper build() {
        return new PaperTypeWrapper();
    }

	@Override
	public PaperTypeVO entityVO(PaperType paperType) {
		PaperTypeVO paperTypeVO = BeanUtil.copy(paperType, PaperTypeVO.class);

		return paperTypeVO;
	}

}
