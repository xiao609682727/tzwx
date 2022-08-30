
package org.springcrazy.modules.coursecard.wrapper;

import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.modules.coursecard.entity.CardCode;
import org.springcrazy.modules.coursecard.vo.CardCodeVO;

/**
 * 课程卡编码表包装类,返回视图层所需的字段
 *
 * @author TongZhou
 * @since 2021-04-02
 */
public class CardCodeWrapper extends BaseEntityWrapper<CardCode, CardCodeVO>  {

    public static CardCodeWrapper build() {
        return new CardCodeWrapper();
    }

	@Override
	public CardCodeVO entityVO(CardCode cardCode) {
		CardCodeVO cardCodeVO = BeanUtil.copy(cardCode, CardCodeVO.class);

		return cardCodeVO;
	}

}
