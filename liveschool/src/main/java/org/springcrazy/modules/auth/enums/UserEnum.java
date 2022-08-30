
package org.springcrazy.modules.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户类型枚举

 */
@Getter
@AllArgsConstructor
public enum UserEnum {

	/**
	 * web
	 */
	WEB("web", 1),

	/**
	 * app
	 */
	APP("app", 2),
	/**
	 * app
	 */
	STUDENT("student", 3),
	;

	final String name;
	final int category;

}
