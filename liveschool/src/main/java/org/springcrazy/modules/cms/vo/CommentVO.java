
package org.springcrazy.modules.cms.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.cms.entity.Comment;

/**
 * 视图实体类
 *
 * @author TongZhou
 * @since 2020-03-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CommentVO对象", description = "CommentVO对象")
public class CommentVO extends Comment {
	private static final long serialVersionUID = 1L;

	//头像
	private String headImg;
	//昵称
	private String showName;

	private String mobile;

	private String email;
}
