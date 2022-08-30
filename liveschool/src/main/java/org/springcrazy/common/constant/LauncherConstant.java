
package org.springcrazy.common.constant;

import org.springcrazy.core.launch.constant.AppConstant;

/**
 * 通用常量
 */
public interface LauncherConstant {

	/**
	 * app name
	 */
	String APPLICATION_NAME = AppConstant.APPLICATION_NAME_PREFIX + "api";

	/**
	 * sentinel dev 地址
	 */
	String SENTINEL_DEV_ADDR = "127.0.0.1:8858";

	/**
	 * sentinel prod 地址
	 */
	String SENTINEL_PROD_ADDR = "192.168.1.1:8858";

	/**
	 * sentinel test 地址
	 */
	String SENTINEL_TEST_ADDR = "192.168.1.1:8858";

	/**
	 * 动态获取sentinel地址
	 *
	 * @param profile 环境变量
	 * @return addr
	 */
	static String sentinelAddr(String profile) {
		switch (profile) {
			case (AppConstant.PROD_CODE):
				return SENTINEL_PROD_ADDR;
			case (AppConstant.TEST_CODE):
				return SENTINEL_TEST_ADDR;
			default:
				return SENTINEL_DEV_ADDR;
		}
	}
}
