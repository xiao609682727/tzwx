
package org.springcrazy.modules.cms.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.cms.entity.Comment;

/**
 * 数据传输对象实体类
 *
 * @author TongZhou
 * @since 2020-03-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommentDTO extends Comment {
	private static final long serialVersionUID = 1L;

}
