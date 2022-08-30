
package org.springcrazy.modules.exam.wrapper;

import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.modules.exam.entity.QuestionRecord;
import org.springcrazy.modules.exam.vo.QuestionRecordVO;

/**
 * 考试试题记录表包装类,返回视图层所需的字段
 *
 * @author TongZhou
 * @since 2021-01-05
 */
public class QuestionRecordWrapper extends BaseEntityWrapper<QuestionRecord, QuestionRecordVO>  {

    public static QuestionRecordWrapper build() {
        return new QuestionRecordWrapper();
    }

	@Override
	public QuestionRecordVO entityVO(QuestionRecord questionRecord) {
		QuestionRecordVO questionRecordVO = BeanUtil.copy(questionRecord, QuestionRecordVO.class);

		return questionRecordVO;
	}

}
