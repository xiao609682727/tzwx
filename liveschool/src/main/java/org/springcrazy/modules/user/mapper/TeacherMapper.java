
package org.springcrazy.modules.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.user.entity.Teacher;
import org.springcrazy.modules.user.vo.TeacherVO;

import java.util.List;

/**
 * 讲师 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-04-28
 */
public interface TeacherMapper extends BaseMapper<Teacher> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param teacher
	 * @return
	 */
	List<TeacherVO> selectTeacherPage(IPage page, TeacherVO teacher);

}
