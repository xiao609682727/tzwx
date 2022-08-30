
package org.springcrazy.modules.exam.wrapper;

import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.modules.exam.entity.ErrorQuestion;
import org.springcrazy.modules.exam.vo.ErrorQuestionVO;

/**
 * 错题记录表包装类,返回视图层所需的字段
 *
 * @author TongZhou
 * @since 2020-12-17
 */
public class ErrorQuestionWrapper extends BaseEntityWrapper<ErrorQuestion, ErrorQuestionVO>  {

    public static ErrorQuestionWrapper build() {
        return new ErrorQuestionWrapper();
    }

	@Override
	public ErrorQuestionVO entityVO(ErrorQuestion errorQuestion) {
		ErrorQuestionVO errorQuestionVO = BeanUtil.copy(errorQuestion, ErrorQuestionVO.class);

		return errorQuestionVO;
	}


	public ErrorQuestionVO setFav(ErrorQuestion errorQuestion) {
		ErrorQuestionVO errorQuestionVO = BeanUtil.copy(errorQuestion, ErrorQuestionVO.class);

		return errorQuestionVO;
	}
}
