
package org.springcrazy.modules.cms.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.cms.entity.CmsSubject;

/**
 * 专业信息视图实体类
 *
 * @author TongZhou
 * @since 2020-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SubjectVO对象", description = "专业信息")
public class CmsSubjectVO extends CmsSubject {
	private static final long serialVersionUID = 1L;

}
