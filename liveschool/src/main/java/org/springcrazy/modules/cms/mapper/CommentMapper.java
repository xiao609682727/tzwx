
package org.springcrazy.modules.cms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springcrazy.modules.cms.entity.Comment;
import org.springcrazy.modules.cms.vo.CommentVO;

import java.util.List;

/**
 *  Mapper 接口
 *
 * @author TongZhou
 * @since 2020-03-01
 */
public interface CommentMapper extends BaseMapper<Comment> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param comment
	 * @return
	 */
	List<CommentVO> selectCommentPage(IPage page,@Param("comment")  CommentVO comment);

}
