
package org.springcrazy.modules.msg.wrapper;

import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.modules.msg.entity.MsgSystem;
import org.springcrazy.modules.msg.vo.MsgSystemVO;

/**
 * 系统消息包装类,返回视图层所需的字段
 *
 * @author TongZhou
 * @since 2020-05-18
 */
public class MsgSystemWrapper extends BaseEntityWrapper<MsgSystem, MsgSystemVO>  {

    public static MsgSystemWrapper build() {
        return new MsgSystemWrapper();
    }

	@Override
	public MsgSystemVO entityVO(MsgSystem msgSystem) {
		MsgSystemVO msgSystemVO = BeanUtil.copy(msgSystem, MsgSystemVO.class);

		return msgSystemVO;
	}

}
