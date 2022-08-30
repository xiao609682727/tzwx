
package org.springcrazy.modules.edu.wrapper;

import org.springcrazy.common.constant.CommonConstant;
import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.node.ForestNodeMerger;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.core.tool.utils.SpringUtil;
import org.springcrazy.modules.edu.entity.Subject;
import org.springcrazy.modules.edu.service.ISubjectService;
import org.springcrazy.modules.edu.vo.SubjectVO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 包装类,返回视图层所需的字段

 */
public class SubjectWrapper extends BaseEntityWrapper<Subject, SubjectVO> {

	private static ISubjectService subjectService;


	static {
		subjectService = SpringUtil.getBean(ISubjectService.class);
	}

	public static SubjectWrapper build() {
		return new SubjectWrapper();
	}

	@Override
	public SubjectVO entityVO(Subject subject) {
		SubjectVO subjectVO = BeanUtil.copy(subject, SubjectVO.class);
		if (Func.equals(subject.getParentId(), CommonConstant.TOP_PARENT_ID)) {
			subjectVO.setParentName(CommonConstant.TOP_PARENT_NAME);
		} else {
			Subject parent = subjectService.getById(subject.getParentId());
			subjectVO.setParentName(parent.getSubjectName());
		}
		return subjectVO;
	}


	public List<SubjectVO> listNodeVO(List<Subject> list) {
		List<SubjectVO> collect = list.stream().map(subject -> BeanUtil.copy(subject, SubjectVO.class)).collect(Collectors.toList());
		return ForestNodeMerger.merge(collect);
	}

}
