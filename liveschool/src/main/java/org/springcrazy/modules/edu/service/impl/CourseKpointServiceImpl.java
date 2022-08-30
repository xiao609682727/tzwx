
package org.springcrazy.modules.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.core.tool.node.ForestNodeMerger;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.edu.entity.CourseKpoint;
import org.springcrazy.modules.edu.mapper.CourseKpointMapper;
import org.springcrazy.modules.edu.service.ICourseKpointService;
import org.springcrazy.modules.edu.service.ICourseStudyhistoryService;
import org.springcrazy.modules.edu.vo.CourseKpointVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 知识点的基本信息 服务实现类
 *
 * @author TongZhou
 * @since 2020-04-25
 */
@Service
public class CourseKpointServiceImpl extends ServiceImpl<CourseKpointMapper, CourseKpoint> implements ICourseKpointService {

	@Autowired
	ICourseStudyhistoryService courseStudyhistoryService;

	@Override
	public IPage<CourseKpointVO> selectCourseKpointPage(IPage<CourseKpointVO> page, CourseKpointVO courseKpoint) {
		return page.setRecords(baseMapper.selectCourseKpointPage(page, courseKpoint));
	}
	/**
	 * 查询距离当前时间最近的直播章节
	 */
	public CourseKpoint selectLiveCourseKpointOrderByBeginTime(CourseKpoint courseKpoint){
		return baseMapper.selectLiveCourseKpointOrderByBeginTime(courseKpoint);
	}
	/**
	 * 查询带有学习记录的章节数据
	 */
	public List<CourseKpointVO> selectCourseKpointListForStudyRecord(CourseKpointVO courseKpoint){
		return baseMapper.selectCourseKpointListForStudyRecord(courseKpoint);
	}
	@Override
	public List<CourseKpointVO> tree() {
		return ForestNodeMerger.merge(baseMapper.tree());
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
		this.removeById(id);
		List<CourseKpoint> list = this.list(new QueryWrapper<CourseKpoint>().eq("parent_id", id));
		//直接删除节时删除该节学习记录
		if (list.size() == 0){
			courseStudyhistoryService.deleteHistory(id);
		}
		//删除章时 删除章下节  学习记录
		list.forEach(resourceFile -> {
			courseStudyhistoryService.deleteHistory(resourceFile.getId());
			deleteId(resourceFile.getId());
		});
	}
}
