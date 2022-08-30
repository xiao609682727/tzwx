
package org.springcrazy.modules.lineclass.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.springcrazy.modules.lineclass.entity.LineclassEnroll;
import org.springcrazy.modules.lineclass.vo.LineclassEnrollVO;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 线下课报名记录表 服务类
 *
 * @author TongZhou
 * @since 2021-05-27
 */
public interface ILineclassEnrollService extends IService<LineclassEnroll> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param lineclassEnroll
	 * @return
	 */
	IPage<LineclassEnrollVO> selectLineclassEnrollPage(IPage<LineclassEnrollVO> page, LineclassEnrollVO lineclassEnroll);

	/**
	 * 根据用户id及课程id删除报名记录
	 * */
	void delectLineClassEnrol(@Param("userId") Integer userId,@Param("courseId") Integer courseId);


	/**
	 * 根据用户id及课程id删除报名记录
	 * */
	void updateLineClassEnrol(@Param("e") LineclassEnroll lineclassEnroll);


	/**
	 * 查询所有的报名了面授课学生
	 */
	IPage<LineclassEnrollVO> getCourseUserList(IPage<LineclassEnroll> page,LineclassEnroll lineclassEnroll);

	/**
	 * 获取导出用户报名信息
	 *
	 * @param response
	 * @return
	 */
	void exportLineClassUser(HttpServletResponse response , Map<String, Object> lineclassEnroll);


}
