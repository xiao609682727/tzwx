
package org.springcrazy.core.log.event;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springcrazy.core.launch.props.CrazyProperties;
import org.springcrazy.core.launch.server.ServerInfo;
import org.springcrazy.core.log.constant.EventConstant;
import org.springcrazy.core.log.model.LogError;
import org.springcrazy.core.log.utils.LogAbstractUtil;
import org.springcrazy.modules.system.service.ILogService;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;

/**
 * 异步监听错误日志事件

 */
@Slf4j
@AllArgsConstructor
public class ErrorLogListener {

	private final ILogService logService;
	private final ServerInfo serverInfo;
	private final CrazyProperties crazyProperties;

	@Async
	@Order
	@EventListener(ErrorLogEvent.class)
	public void saveErrorLog(ErrorLogEvent event) {
		Map<String, Object> source = (Map<String, Object>) event.getSource();
		LogError logError = (LogError) source.get(EventConstant.EVENT_LOG);
		LogAbstractUtil.addOtherInfoToLog(logError, crazyProperties, serverInfo);
		logService.saveErrorLog(logError);
	}
}
