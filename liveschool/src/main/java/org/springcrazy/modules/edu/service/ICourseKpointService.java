
package org.springcrazy.modules.edu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.edu.entity.CourseKpoint;
import org.springcrazy.modules.edu.vo.CourseKpointVO;

import java.util.List;

/**
 * 知识点的基本信息 服务类
 *
 * @author TongZhou
 * @since 2020-04-25
 */
public interface ICourseKpointService extends IService<CourseKpoint> {

	/**
	 * 自定义分页
	 */
	IPage<CourseKpointVO> selectCourseKpointPage(IPage<CourseKpointVO> page, CourseKpointVO courseKpoint);
	/**
	 * 查询距离当前时间最近的直播章节
	 */
	CourseKpoint selectLiveCourseKpointOrderByBeginTime(CourseKpoint courseKpoint);

	/**
	 * 查询带有学习记录的章节数据
	 */
	List<CourseKpointVO> selectCourseKpointListForStudyRecord(CourseKpointVO courseKpoint);



	boolean deleteIds(String ids);

	/**
	 * 树形结构
	 *
	 * @return
	 */
	List<CourseKpointVO> tree();
}
