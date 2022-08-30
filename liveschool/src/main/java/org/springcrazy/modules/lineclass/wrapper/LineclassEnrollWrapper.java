
package org.springcrazy.modules.lineclass.wrapper;

import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.modules.lineclass.entity.LineclassEnroll;
import org.springcrazy.modules.lineclass.vo.LineclassEnrollVO;

/**
 * 线下课报名记录表包装类,返回视图层所需的字段
 *
 * @author TongZhou
 * @since 2021-05-27
 */
public class LineclassEnrollWrapper extends BaseEntityWrapper<LineclassEnroll, LineclassEnrollVO>  {

    public static LineclassEnrollWrapper build() {
        return new LineclassEnrollWrapper();
    }

	@Override
	public LineclassEnrollVO entityVO(LineclassEnroll lineclassEnroll) {
		LineclassEnrollVO lineclassEnrollVO = BeanUtil.copy(lineclassEnroll, LineclassEnrollVO.class);

		return lineclassEnrollVO;
	}

}
