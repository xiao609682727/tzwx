
package org.springcrazy.modules.cms.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.cms.entity.Article;

/**
 * 文章信息表数据传输对象实体类
 *
 * @author TongZhou
 * @since 2020-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleDTO extends Article {
	private static final long serialVersionUID = 1L;

}
