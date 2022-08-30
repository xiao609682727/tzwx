
package org.springcrazy.modules.edu.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.edu.entity.CourseMaterial;

/**
 * 章节资料信息视图实体类
 *
 * @author TongZhou
 * @since 2020-10-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CourseMaterialVO对象", description = "章节资料信息")
public class CourseMaterialVO extends CourseMaterial {
	private static final long serialVersionUID = 1L;

}
