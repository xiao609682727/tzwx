

package org.springcrazy.modules.auth.event;

import org.springframework.context.ApplicationEvent;

import java.util.Map;

/**
 * 在线用户记录事件

 */
public class OnlineUserEvent extends ApplicationEvent {

	public OnlineUserEvent(Map<String, Object> source) {
		super(source);
	}

}
