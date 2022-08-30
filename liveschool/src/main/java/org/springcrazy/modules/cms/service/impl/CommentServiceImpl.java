
package org.springcrazy.modules.cms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.modules.cms.entity.Comment;
import org.springcrazy.modules.cms.mapper.CommentMapper;
import org.springcrazy.modules.cms.service.ICommentService;
import org.springcrazy.modules.cms.vo.CommentVO;
import org.springframework.stereotype.Service;

/**
 *  服务实现类
 *
 * @author TongZhou
 * @since 2020-03-01
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

	@Override
	public IPage<CommentVO> selectCommentPage(IPage<CommentVO> page, CommentVO comment) {
		return page.setRecords(baseMapper.selectCommentPage(page, comment));
	}

}
