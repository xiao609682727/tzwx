
package org.springcrazy.modules.coursecard.wrapper;

import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.modules.coursecard.entity.Card;
import org.springcrazy.modules.coursecard.vo.CardVO;

/**
 * 课程卡主表包装类,返回视图层所需的字段
 *
 * @author TongZhou
 * @since 2021-03-31
 */
public class CardWrapper extends BaseEntityWrapper<Card, CardVO>  {

    public static CardWrapper build() {
        return new CardWrapper();
    }

	@Override
	public CardVO entityVO(Card card) {
		CardVO cardVO = BeanUtil.copy(card, CardVO.class);

		return cardVO;
	}

}
