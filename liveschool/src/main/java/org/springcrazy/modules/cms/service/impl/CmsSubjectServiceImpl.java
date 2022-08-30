
package org.springcrazy.modules.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.api.client.util.Lists;
import org.springcrazy.common.tool.CacheUtils;
import org.springcrazy.core.mp.base.BaseServiceImpl;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.cms.entity.CmsSubject;
import org.springcrazy.modules.cms.mapper.CmsSubjectMapper;
import org.springcrazy.modules.cms.service.ICmsSubjectService;
import org.springcrazy.modules.cms.vo.CmsSubjectVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 专业信息 服务实现类
 *
 * @author TongZhou
 * @since 2020-02-28
 */
@Service
public class CmsSubjectServiceImpl extends BaseServiceImpl<CmsSubjectMapper, CmsSubject> implements ICmsSubjectService {

	@Override
	public IPage<CmsSubjectVO> selectSubjectPage(IPage<CmsSubjectVO> page, CmsSubjectVO subject) {
		return page.setRecords(baseMapper.selectSubjectPage(page, subject));
	}

	@Override
	public CmsSubject getSubject(Integer id) {
		Map<Integer,CmsSubject> map = (Map<Integer, CmsSubject>) CacheUtils.get("subjectMap");
		if(Func.isEmpty(map)){
			List<CmsSubject> list = this.list();
			map = list.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
			CacheUtils.put("subjectMap",map);
		}
		return map.get(id);
	}

	@Override
	public boolean deleteIds(String ids) {
		Func.toIntList(ids).forEach(id ->deleteId(id));
		return true;
	}

	/**
	 * 递归删除文件
	 * @param id
	 */
	public void deleteId(Integer id){
		ArrayList<Integer> arr = Lists.newArrayList();
		arr.add(id);
		this.deleteLogic(arr);
		List<CmsSubject> list = this.list(new QueryWrapper<CmsSubject>().eq("parent_id", id));
		list.forEach(resourceFile -> {
			deleteId(resourceFile.getId());
		});
	}

}
