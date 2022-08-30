
package org.springcrazy.modules.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.cms.entity.Comment;
import org.springcrazy.modules.cms.vo.CommentVO;

/**
 *  服务类
 *
 * @author TongZhou
 * @since 2020-03-01
 */
public interface ICommentService extends IService<Comment> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param comment
	 * @return
	 */
	IPage<CommentVO> selectCommentPage(IPage<CommentVO> page,CommentVO comment);

}
