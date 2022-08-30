
package org.springcrazy.modules.exam.wrapper;

import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.modules.exam.entity.ErrorCheck;
import org.springcrazy.modules.exam.vo.ErrorCheckVO;

/**
 * 试题纠错表包装类,返回视图层所需的字段
 *
 * @author TongZhou
 * @since 2020-12-17
 */
public class ErrorCheckWrapper extends BaseEntityWrapper<ErrorCheck, ErrorCheckVO>  {

    public static ErrorCheckWrapper build() {
        return new ErrorCheckWrapper();
    }

	@Override
	public ErrorCheckVO entityVO(ErrorCheck errorCheck) {
		ErrorCheckVO errorCheckVO = BeanUtil.copy(errorCheck, ErrorCheckVO.class);

		return errorCheckVO;
	}

}
