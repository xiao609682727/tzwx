
package org.springcrazy.modules.quartz.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.quartz.entity.QuartzLog;

/**
 * 定时任务日志视图实体类
 *
 * @author TongZhou
 * @since 2020-05-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "QuartzLogVO对象", description = "定时任务日志")
public class QuartzLogVO extends QuartzLog {
	private static final long serialVersionUID = 1L;

}
