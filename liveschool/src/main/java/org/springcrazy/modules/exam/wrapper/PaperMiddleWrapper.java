
package org.springcrazy.modules.exam.wrapper;

import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.modules.exam.entity.PaperMiddle;
import org.springcrazy.modules.exam.vo.PaperMiddleVO;

/**
 * 大题（试卷试题类型中间表）包装类,返回视图层所需的字段
 *
 * @author TongZhou
 * @since 2020-12-15
 */
public class PaperMiddleWrapper extends BaseEntityWrapper<PaperMiddle, PaperMiddleVO>  {

    public static PaperMiddleWrapper build() {
        return new PaperMiddleWrapper();
    }

	@Override
	public PaperMiddleVO entityVO(PaperMiddle paperMiddle) {
		PaperMiddleVO paperMiddleVO = BeanUtil.copy(paperMiddle, PaperMiddleVO.class);

		return paperMiddleVO;
	}

}
