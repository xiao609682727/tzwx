
package org.springcrazy.modules.edu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springcrazy.modules.edu.entity.Subject;
import org.springcrazy.modules.edu.vo.SubjectVO;

import java.util.List;

/**
 * 专业分类 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-04-25
 */
public interface SubjectMapper extends BaseMapper<Subject> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param subject
	 * @return
	 */
	List<SubjectVO> selectSubjectPage(IPage page, SubjectVO subject);



	Subject getSubjectByName(@Param("subjectName")String subjectName);


	Subject getSubjectBysubjectId(@Param("subjectId")int subjectId);


	List<Subject> getSubjectByNameList(@Param("subjectName")String subjectName);

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
	 * 获取考试的专业树形结构
	 * */
	List<SubjectVO> examTree();

	/**
	 * 获取面授地域
	 * */
	List<SubjectVO> addressTree();
}
