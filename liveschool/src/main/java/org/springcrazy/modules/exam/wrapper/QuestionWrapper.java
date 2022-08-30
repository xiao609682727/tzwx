
package org.springcrazy.modules.exam.wrapper;

import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.modules.exam.entity.Question;
import org.springcrazy.modules.exam.vo.QuestionVO;

/**
 * 考试试题表包装类,返回视图层所需的字段
 *
 * @author TongZhou
 * @since 2020-12-02
 */
public class QuestionWrapper extends BaseEntityWrapper<Question, QuestionVO>  {

    public static QuestionWrapper build() {
        return new QuestionWrapper();
    }

	@Override
	public QuestionVO entityVO(Question question) {
		QuestionVO questionVO = BeanUtil.copy(question, QuestionVO.class);

		return questionVO;
	}

}
