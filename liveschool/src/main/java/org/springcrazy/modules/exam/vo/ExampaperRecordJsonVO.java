
package org.springcrazy.modules.exam.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.exam.entity.ExampaperRecordJson;

/**
 * 视图实体类
 *
 * @author TongZhou
 * @since 2021-01-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ExampaperRecordJsonVO对象", description = "ExampaperRecordJsonVO对象")
public class ExampaperRecordJsonVO extends ExampaperRecordJson {
	private static final long serialVersionUID = 1L;

}
