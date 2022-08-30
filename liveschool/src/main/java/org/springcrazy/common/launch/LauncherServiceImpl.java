
package org.springcrazy.common.launch;

import org.springcrazy.common.constant.LauncherConstant;
import org.springcrazy.core.launch.service.LauncherService;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Properties;

/**
 * 启动参数拓展
 */
public class LauncherServiceImpl implements LauncherService {

	@Override
	public void launcher(SpringApplicationBuilder builder, String appName, String profile) {
		Properties props = System.getProperties();
		props.setProperty("spring.cloud.sentinel.transport.dashboard", LauncherConstant.sentinelAddr(profile));
	}

}
