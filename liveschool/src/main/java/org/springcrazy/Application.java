
package org.springcrazy;

import org.springcrazy.common.constant.LauncherConstant;
import org.springcrazy.core.launch.CrazyApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动器

 */
@EnableScheduling
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		CrazyApplication.run(LauncherConstant.APPLICATION_NAME, Application.class, args);
	}

}

