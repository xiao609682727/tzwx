
package org.springcrazy.modules.user.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springcrazy.modules.system.excel.ExportStudentExcel;
import org.springcrazy.modules.user.entity.Student;
import org.springcrazy.modules.user.vo.StudentVO;

import java.util.List;

/**
 * 学员表 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-04-28
 */
public interface StudentMapper extends BaseMapper<Student> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param student
	 * @return
	 */
	List<StudentVO> selectStudentPage(IPage page, StudentVO student);

	Student selectStudentLogin(Student student);

	Student checkStudent(Student student);

	Student userInfos(Integer userId);

	/**
	 * 用户信息
	 *
	 * @return
	 */
	Student getStudent(Integer userId);

	/**
	 * 获取导出用户数据
	 *
	 * @param queryWrapper
	 * @return
	 */
	List<ExportStudentExcel> exportUser(IPage page, @Param("ew") Wrapper<Student> queryWrapper);

	Student checkStudentNew(Student student);

    Student checkStudentUserName(Student student);

    Student checkStudentMobile(Student student);

	Student checkStudentEmail(Student student);

    void updateAvalible(String key);
}
