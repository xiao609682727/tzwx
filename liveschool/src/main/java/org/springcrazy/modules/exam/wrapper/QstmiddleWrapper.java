
package org.springcrazy.modules.exam.wrapper;

import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.modules.exam.entity.Qstmiddle;
import org.springcrazy.modules.exam.vo.QstmiddleVO;

/**
 * 考试试卷表包装类,返回视图层所需的字段
 *
 * @author TongZhou
 * @since 2020-12-15
 */
public class QstmiddleWrapper extends BaseEntityWrapper<Qstmiddle, QstmiddleVO>  {

    public static QstmiddleWrapper build() {
        return new QstmiddleWrapper();
    }

	@Override
	public QstmiddleVO entityVO(Qstmiddle qstmiddle) {
		QstmiddleVO qstmiddleVO = BeanUtil.copy(qstmiddle, QstmiddleVO.class);

		return qstmiddleVO;
	}

}
