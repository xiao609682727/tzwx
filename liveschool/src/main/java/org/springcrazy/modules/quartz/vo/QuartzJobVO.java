
package org.springcrazy.modules.quartz.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.quartz.entity.QuartzJob;

/**
 * 定时任务视图实体类
 *
 * @author TongZhou
 * @since 2020-05-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "QuartzJobVO对象", description = "定时任务")
public class QuartzJobVO extends QuartzJob {
	private static final long serialVersionUID = 1L;

}
