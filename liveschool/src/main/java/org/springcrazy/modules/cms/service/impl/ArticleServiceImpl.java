
package org.springcrazy.modules.cms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.core.mp.base.BaseServiceImpl;
import org.springcrazy.modules.cms.entity.Article;
import org.springcrazy.modules.cms.mapper.ArticleMapper;
import org.springcrazy.modules.cms.service.IArticleService;
import org.springcrazy.modules.cms.vo.ArticleVO;
import org.springframework.stereotype.Service;

/**
 * 文章信息表 服务实现类
 *
 * @author TongZhou
 * @since 2020-02-28
 */
@Service
public class ArticleServiceImpl extends BaseServiceImpl<ArticleMapper, Article> implements IArticleService {

	@Override
	public IPage<ArticleVO> selectArticlePage(IPage<ArticleVO> page, ArticleVO article) {
		return page.setRecords(baseMapper.selectArticlePage(page, article));
	}

}
