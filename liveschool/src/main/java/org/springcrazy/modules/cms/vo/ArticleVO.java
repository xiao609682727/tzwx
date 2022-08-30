
package org.springcrazy.modules.cms.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.cms.entity.Article;

/**
 * 文章信息表视图实体类
 *
 * @author TongZhou
 * @since 2020-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ArticleVO对象", description = "文章信息表")
public class ArticleVO extends Article {
	private static final long serialVersionUID = 1L;

	private String subjectName;

}
