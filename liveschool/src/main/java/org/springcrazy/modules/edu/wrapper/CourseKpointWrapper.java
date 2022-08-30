
package org.springcrazy.modules.edu.wrapper;

import org.springcrazy.common.constant.CommonConstant;
import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.node.ForestNodeMerger;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.core.tool.utils.SpringUtil;
import org.springcrazy.modules.edu.entity.CourseKpoint;
import org.springcrazy.modules.edu.service.ICourseKpointService;
import org.springcrazy.modules.edu.vo.CourseKpointVO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 包装类,返回视图层所需的字段

 */
public class CourseKpointWrapper extends BaseEntityWrapper<CourseKpoint, CourseKpointVO> {

	private static ICourseKpointService courseKpointService;


	static {
		courseKpointService = SpringUtil.getBean(ICourseKpointService.class);
	}

	public static CourseKpointWrapper build() {
		return new CourseKpointWrapper();
	}

	@Override
	public CourseKpointVO entityVO(CourseKpoint courseKpoint) {
		CourseKpointVO courseKpointVO = BeanUtil.copy(courseKpoint, CourseKpointVO.class);
		if (Func.equals(courseKpoint.getParentId(), CommonConstant.TOP_PARENT_ID)) {
			courseKpointVO.setParentName(CommonConstant.TOP_PARENT_NAME);
		} else {
			CourseKpoint parent = courseKpointService.getById(courseKpoint.getParentId());
			courseKpointVO.setParentName(parent.getName());
		}
		return courseKpointVO;
	}


	public List<CourseKpointVO> listNodeVO(List<CourseKpoint> list) {
		List<CourseKpointVO> collect = list.stream().map(courseKpoint -> BeanUtil.copy(courseKpoint, CourseKpointVO.class)).collect(Collectors.toList());
		return ForestNodeMerger.merge(collect);
	}
	public List<CourseKpointVO> listCourseKpointNodeVO(List<CourseKpointVO> list) {
		List<CourseKpointVO> collect = list.stream().map(courseKpoint -> BeanUtil.copy(courseKpoint, CourseKpointVO.class)).collect(Collectors.toList());
		return ForestNodeMerger.merge(collect);
	}
}
