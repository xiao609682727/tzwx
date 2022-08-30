
package org.springcrazy.modules.edu.wrapper;

import org.springcrazy.common.constant.CommonConstant;
import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.node.ForestNodeMerger;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.core.tool.utils.SpringUtil;
import org.springcrazy.modules.edu.entity.CourseStudyhistory;
import org.springcrazy.modules.edu.service.ICourseStudyhistoryService;
import org.springcrazy.modules.edu.vo.CourseStudyhistoryVO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 包装类,返回视图层所需的字段

 */
public class CourseStudyHistoryWrapper extends BaseEntityWrapper<CourseStudyhistory, CourseStudyhistoryVO> {

	private static ICourseStudyhistoryService courseStudyhistoryService;


	static {
		courseStudyhistoryService = SpringUtil.getBean(ICourseStudyhistoryService.class);
	}

	public static CourseStudyHistoryWrapper build() {
		return new CourseStudyHistoryWrapper();
	}

	@Override
	public CourseStudyhistoryVO entityVO(CourseStudyhistory courseStudyhistory) {
		CourseStudyhistoryVO courseStudyhistoryVO = BeanUtil.copy(courseStudyhistory, CourseStudyhistoryVO.class);
		if (Func.equals(courseStudyhistory.getParentId(), CommonConstant.TOP_PARENT_ID)) {
			courseStudyhistoryVO.setParentName(CommonConstant.TOP_PARENT_NAME);
		} else {
			CourseStudyhistory parent = courseStudyhistoryService.getById(courseStudyhistory.getParentId());
			courseStudyhistoryVO.setParentName(parent.getKpointName());
		}
		return courseStudyhistoryVO;
	}


	public List<CourseStudyhistoryVO> listNodeVO(List<CourseStudyhistory> list) {
		List<CourseStudyhistoryVO> collect = list.stream().map(courseStudyhistory -> BeanUtil.copy(courseStudyhistory, CourseStudyhistoryVO.class)).collect(Collectors.toList());
		return ForestNodeMerger.merge(collect);
	}

}
