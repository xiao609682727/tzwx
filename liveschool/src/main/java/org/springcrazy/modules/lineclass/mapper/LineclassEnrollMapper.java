
package org.springcrazy.modules.lineclass.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springcrazy.modules.lineclass.entity.LineclassEnroll;
import org.springcrazy.modules.lineclass.vo.LineclassEnrollVO;
import org.springcrazy.modules.system.excel.ExportLineClassExcel;

import java.util.List;

/**
 * 线下课报名记录表 Mapper 接口
 *
 * @author TongZhou
 * @since 2021-05-27
 */
public interface LineclassEnrollMapper extends BaseMapper<LineclassEnroll> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param lineclassEnroll
	 * @return
	 */
	List<LineclassEnrollVO> selectLineclassEnrollPage(IPage page, LineclassEnrollVO lineclassEnroll);

	/**
	 * 根据用户id及课程id删除报名记录
	 * */
	void delectLineClassEnrol(@Param("userId") Integer userId, @Param("courseId") Integer courseId);

	/**
	 * 根据用户id及课程id删除报名记录
	 * */
	void updateLineClassEnrol(@Param("e") LineclassEnroll lineclassEnroll);

	/**
	 * 查询所有的报名了面授课学生
	 */
	IPage<LineclassEnrollVO> getCourseUserList(IPage page, @Param("e")LineclassEnroll lineclassEnroll);

	/**
	 * 获取导出用户数据
	 *
	 * @param lineclassEnroll
	 * @return
	 */
	List<ExportLineClassExcel> exportLineClass(IPage page, @Param("e") LineclassEnroll lineclassEnroll);

}
