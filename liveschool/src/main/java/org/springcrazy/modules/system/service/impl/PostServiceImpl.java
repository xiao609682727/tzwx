
package org.springcrazy.modules.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.api.client.util.Lists;
import org.springcrazy.core.mp.base.BaseServiceImpl;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.system.entity.Post;
import org.springcrazy.modules.system.mapper.PostMapper;
import org.springcrazy.modules.system.service.IPostService;
import org.springcrazy.modules.system.vo.PostVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 岗位表 服务实现类
 *

 */
@Service
public class PostServiceImpl extends BaseServiceImpl<PostMapper, Post> implements IPostService {

	@Override
	public IPage<PostVO> selectPostPage(IPage<PostVO> page, PostVO post) {
		return page.setRecords(baseMapper.selectPostPage(page, post));
	}

	@Override
	public String getPostIds(String tenantId, String postNames) {
		List<Post> postList = baseMapper.selectList(Wrappers.<Post>query().lambda().eq(Post::getTenantId, tenantId).in(Post::getPostName, Func.toStrList(postNames)));
		if (postList != null && postList.size() > 0) {
			return postList.stream().map(post -> Func.toStr(post.getId())).distinct().collect(Collectors.joining(","));
		}
		return null;
	}

	@Override
	public List<String> getPostNames(String postIds) {
		if(Func.toLongArray(postIds).length == 0){
			return Lists.newArrayList();
		}else{
			return baseMapper.getPostNames(Func.toLongArray(postIds));
		}

	}

}
