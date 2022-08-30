

package org.springcrazy.core.log.config;

import lombok.AllArgsConstructor;
import org.springcrazy.core.launch.props.CrazyProperties;
import org.springcrazy.core.launch.server.ServerInfo;
import org.springcrazy.core.log.aspect.ApiLogAspect;
import org.springcrazy.core.log.event.ApiLogListener;
import org.springcrazy.core.log.event.ErrorLogListener;
import org.springcrazy.core.log.event.UsualLogListener;
import org.springcrazy.core.log.logger.CrazyLogger;
import org.springcrazy.modules.system.service.ILogService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 日志工具自动配置

 */
@Configuration
@AllArgsConstructor
@ConditionalOnWebApplication
public class CrazyLogToolAutoConfiguration {

	private final ILogService logService;
	private final ServerInfo serverInfo;
	private final CrazyProperties crazyProperties;

	@Bean
	public ApiLogAspect apiLogAspect() {
		return new ApiLogAspect();
	}

	@Bean
	public CrazyLogger crazyLogger() {
		return new CrazyLogger();
	}

	@Bean
	public ApiLogListener apiLogListener() {
		return new ApiLogListener(logService, serverInfo, crazyProperties);
	}

	@Bean
	public ErrorLogListener errorEventListener() {
		return new ErrorLogListener(logService, serverInfo, crazyProperties);
	}

	@Bean
	public UsualLogListener crazyEventListener() {
		return new UsualLogListener(logService, serverInfo, crazyProperties);
	}

}
