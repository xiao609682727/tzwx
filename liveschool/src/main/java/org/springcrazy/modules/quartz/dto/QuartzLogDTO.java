
package org.springcrazy.modules.quartz.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.quartz.entity.QuartzLog;

/**
 * 定时任务日志数据传输对象实体类
 *
 * @author TongZhou
 * @since 2020-05-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuartzLogDTO extends QuartzLog {
	private static final long serialVersionUID = 1L;

}
