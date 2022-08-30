
package org.springcrazy.modules.msg.wrapper;

import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.modules.msg.entity.MsgEmail;
import org.springcrazy.modules.msg.vo.MsgEmailVO;

/**
 * 邮件发送记录包装类,返回视图层所需的字段
 *
 * @author TongZhou
 * @since 2020-05-18
 */
public class MsgEmailWrapper extends BaseEntityWrapper<MsgEmail, MsgEmailVO>  {

    public static MsgEmailWrapper build() {
        return new MsgEmailWrapper();
    }

	@Override
	public MsgEmailVO entityVO(MsgEmail msgEmail) {
		MsgEmailVO msgEmailVO = BeanUtil.copy(msgEmail, MsgEmailVO.class);

		return msgEmailVO;
	}

}
