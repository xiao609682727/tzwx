
package org.springcrazy.modules.edu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.edu.entity.Subject;
import org.springcrazy.modules.edu.vo.SubjectVO;

import java.util.List;

/**
 * 专业分类 服务类
 *
 * @author TongZhou
 * @since 2020-04-25
 */
public interface ISubjectService extends IService<Subject> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param subject
	 * @return
	 */
	IPage<SubjectVO> selectSubjectPage(IPage<SubjectVO> page, SubjectVO subject);

	boolean deleteIds(String ids);

	Subject getSubjectByName(String subjectName);

	Subject getSubjectBysubjectId(int subjectId);

	List<Subject> getSubjectByNameList(String subjectName);

	/**
	 * 树形结构
	 *
	 * @return
	 */
	List<SubjectVO> tree();

	/**
	 * 获取活动菜单树形结构
	 *
	 * @return
	 */
	List<SubjectVO> lineClasstree();



	/**
	 * 获取考试的树结构
	 * */
	List<SubjectVO> examTree();

	/**
	 * 获取面授地域
	 * */
	List<SubjectVO> addressTree();



}
