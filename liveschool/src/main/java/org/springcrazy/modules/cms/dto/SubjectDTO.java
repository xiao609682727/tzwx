
package org.springcrazy.modules.cms.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.cms.entity.CmsSubject;

/**
 * 专业信息数据传输对象实体类
 *
 * @author TongZhou
 * @since 2020-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SubjectDTO extends CmsSubject {
	private static final long serialVersionUID = 1L;

}
