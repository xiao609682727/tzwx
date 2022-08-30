
package org.springcrazy.modules.edu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.edu.entity.CourseKpoint;
import org.springcrazy.modules.edu.vo.CourseKpointVO;

import java.util.List;

/**
 * 知识点的基本信息 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-04-25
 */
public interface CourseKpointMapper extends BaseMapper<CourseKpoint> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param courseKpoint
	 * @return
	 */
	List<CourseKpointVO> selectCourseKpointPage(IPage page, CourseKpointVO courseKpoint);

	/**
	 * 查询距离当前时间最近的直播章节
	 *
	 */
	CourseKpoint selectLiveCourseKpointOrderByBeginTime(CourseKpoint courseKpoint);
	/**
	 * 查询带有学习记录的章节数据
	 */
	List<CourseKpointVO> selectCourseKpointListForStudyRecord(CourseKpointVO courseKpoint);
	/**
	 * 树形结构
	 *
	 * @return
	 */
	List<CourseKpointVO> tree();
}
