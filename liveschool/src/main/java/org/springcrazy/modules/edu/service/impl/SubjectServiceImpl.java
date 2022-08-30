
package org.springcrazy.modules.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.core.tool.node.ForestNodeMerger;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.edu.entity.Subject;
import org.springcrazy.modules.edu.mapper.SubjectMapper;
import org.springcrazy.modules.edu.service.ISubjectService;
import org.springcrazy.modules.edu.vo.SubjectVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 专业分类 服务实现类
 *
 * @author TongZhou
 * @since 2020-04-25
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements ISubjectService {


	@Override
	public IPage<SubjectVO> selectSubjectPage(IPage<SubjectVO> page, SubjectVO subject) {
		return page.setRecords(baseMapper.selectSubjectPage(page, subject));
	}

	@Override
	public List<SubjectVO> tree() {
		return ForestNodeMerger.merge(baseMapper.tree());
	}

	@Override
	public List<SubjectVO> lineClasstree() {
		return ForestNodeMerger.merge(baseMapper.lineClasstree());
	}

	@Override
	public List<SubjectVO> examTree() {
		return ForestNodeMerger.merge(baseMapper.examTree());
	}

	@Override
	public List<SubjectVO> addressTree() {
		return ForestNodeMerger.merge(baseMapper.addressTree());
	}


	@Override
	public boolean deleteIds(String ids) {
		Func.toIntList(ids).forEach(id ->deleteId(id));
		return true;
	}

	@Override
	public Subject getSubjectByName(String subjectName) {
		return baseMapper.getSubjectByName(subjectName);
	}

	@Override
	public Subject getSubjectBysubjectId(int subjectId) {
		return baseMapper.getSubjectBysubjectId(subjectId);
	}

	@Override
	public List<Subject> getSubjectByNameList(String subjectName) {
		return baseMapper.getSubjectByNameList(subjectName);
	}

	/**
	 * 递归删除文件
	 * @param id
	 */
	public void deleteId(Integer id){
		this.removeById(id);
		List<Subject> list = this.list(new QueryWrapper<Subject>().eq("parent_id", id));
		list.forEach(resourceFile -> {
			deleteId(resourceFile.getId());
		});
	}
}
