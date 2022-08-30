
package org.springcrazy.modules.system.wrapper;


import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.core.tool.utils.SpringUtil;
import org.springcrazy.modules.system.entity.Post;
import org.springcrazy.modules.system.service.IDictService;
import org.springcrazy.modules.system.vo.PostVO;

import java.util.Objects;

/**
 * 岗位表包装类,返回视图层所需的字段

 */
public class PostWrapper extends BaseEntityWrapper<Post, PostVO> {

	private static IDictService dictService;

	static {
		dictService = SpringUtil.getBean(IDictService.class);
	}

	public static org.springcrazy.modules.system.wrapper.PostWrapper build() {
		return new org.springcrazy.modules.system.wrapper.PostWrapper();
	}

	@Override
	public PostVO entityVO(Post post) {
		PostVO postVO = Objects.requireNonNull(BeanUtil.copy(post, PostVO.class));
		String categoryName = dictService.getValue("post_category", post.getCategory());
		postVO.setCategoryName(categoryName);
		return postVO;
	}

}
