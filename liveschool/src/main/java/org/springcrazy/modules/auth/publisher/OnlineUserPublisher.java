

package org.springcrazy.modules.auth.publisher;

import org.springcrazy.core.secure.AuthInfo;
import org.springcrazy.core.tool.utils.SpringUtil;
import org.springcrazy.modules.auth.event.OnlineUserEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * 在线用户记录事件发送

 */
public class OnlineUserPublisher {

	public static void publishEvent(AuthInfo authInfo, Map<String, Object> cityInfo) {
		Map<String, Object> event = new HashMap<>(16);
		event.put("authInfo", authInfo);
		event.put("cityInfo", cityInfo);
		SpringUtil.publishEvent(new OnlineUserEvent(event));
	}

}
