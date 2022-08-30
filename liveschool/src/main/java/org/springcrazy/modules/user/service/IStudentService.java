
package org.springcrazy.modules.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.system.entity.UserInfo;
import org.springcrazy.modules.system.excel.StudentExcel;
import org.springcrazy.modules.user.entity.Student;
import org.springcrazy.modules.user.vo.StudentVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 学员表 服务类
 *
 * @author TongZhou
 * @since 2020-04-28
 */
public interface IStudentService extends IService<Student> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param student
	 * @return
	 */
	IPage<StudentVO> selectStudentPage(IPage<StudentVO> page, StudentVO student);

	/**
	 * 用户信息
	 *
	 * @return
	 */
	UserInfo userInfo(Integer userId);

	/**
	 * 用户信息
	 *
	 * @return
	 */
	UserInfo userInfos(Integer userId);

	/**
	 * 用户信息
	 *
	 * @param account
	 * @return
	 */
	UserInfo userInfo(String account);

	/**
	 * 用户信息
	 *
	 * @param account
	 * @param password
	 * @return
	 */
	UserInfo userInfo(String account, String password);

	/**
	 * 导入用户数据
	 */
	void importUser(List<StudentExcel> data);
	/**
	 * excel导入代理商用户数据
	 */
	void importUserAgent(List<StudentExcel> data);
	/**
	 * 获取导出用户数据
	 * @param response
	 * @return
	 */
	void exportUser(HttpServletResponse response, Map<String, Object> user);

	void register(Student student);

	Student checkStudent(Student student);

	Student checkStudentNew(Student student);

    Student checkStudentUserName(Student student);

	Student checkStudentMobile(Student student);

	Student checkStudentEmail(Student student);

	/**
	 * 用户信息
	 *
	 * @return
	 */
	Student getStudent(Integer userId);

	/**
	 * 冻结用户
	 * @param key
	 */
	void updateAvalible(String key);
}
