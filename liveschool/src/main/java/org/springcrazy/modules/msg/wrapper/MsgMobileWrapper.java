
package org.springcrazy.modules.msg.wrapper;

import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.modules.msg.entity.MsgMobile;
import org.springcrazy.modules.msg.vo.MsgMobileVO;

/**
 * 手机短信发送记录包装类,返回视图层所需的字段
 *
 * @author TongZhou
 * @since 2020-05-18
 */
public class MsgMobileWrapper extends BaseEntityWrapper<MsgMobile, MsgMobileVO>  {

    public static MsgMobileWrapper build() {
        return new MsgMobileWrapper();
    }

	@Override
	public MsgMobileVO entityVO(MsgMobile msgMobile) {
		MsgMobileVO msgMobileVO = BeanUtil.copy(msgMobile, MsgMobileVO.class);

		return msgMobileVO;
	}

}
