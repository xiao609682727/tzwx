
package org.springcrazy.modules.coursecard.wrapper;

import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.modules.coursecard.entity.CardCourse;
import org.springcrazy.modules.coursecard.vo.CardCourseVO;

/**
 * 课程卡课程表包装类,返回视图层所需的字段
 *
 * @author TongZhou
 * @since 2021-04-02
 */
public class CardCourseWrapper extends BaseEntityWrapper<CardCourse, CardCourseVO>  {

    public static CardCourseWrapper build() {
        return new CardCourseWrapper();
    }

	@Override
	public CardCourseVO entityVO(CardCourse cardCourse) {
		CardCourseVO cardCourseVO = BeanUtil.copy(cardCourse, CardCourseVO.class);

		return cardCourseVO;
	}

}
