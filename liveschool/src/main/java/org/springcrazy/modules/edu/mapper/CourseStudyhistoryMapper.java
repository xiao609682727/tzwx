
package org.springcrazy.modules.edu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springcrazy.modules.edu.entity.CourseStudyhistory;
import org.springcrazy.modules.edu.vo.CourseStudyhistoryVO;
import org.springcrazy.modules.system.excel.CourseStudyhistoryExcel;

import java.util.List;
import java.util.Map;

/**
 * 课程播放记录(学习记录) Mapper 接口
 *
 * @author TongZhou
 * @since 2020-05-06
 */
public interface CourseStudyhistoryMapper extends BaseMapper<CourseStudyhistory> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param courseStudyhistory
	 * @return
	 */
	List<CourseStudyhistoryVO> selectCourseStudyhistoryPage(IPage page, CourseStudyhistoryVO courseStudyhistory);

	/**
	 * 按照用户数据查询所有订单课程信息
	 *
	 * @param page
	 * @param courseStudyhistory
	 * @return
	 */
	List<CourseStudyhistory> selectCourseTrxorder(IPage page, @Param("e")CourseStudyhistory courseStudyhistory);

	/**
	 * 学习总时长和最后学习时长
	 *
	 * @param userId
	 * @param courseId
	 * @return
	 */
	CourseStudyhistory getStudyTime(@Param("userId")int userId,@Param("courseId")int courseId);
	/**
	 * 学习总时长和最后学习时长
	 * @return
	 */
	CourseStudyhistory getPackageStudyTime(@Param("userId")int userId,@Param("coursePackageId")int coursePackageId,@Param("courseId")int courseId);

	/**
	 * 查询出某个
	 * @param courseId
	 */
	String getstudykpoint(@Param("courseId")int courseId);
	/**
	 * 查询某个套餐下的所有章节数
	 * @param coursePackageId
	 */
	String getPackageStudykpoint(@Param("coursePackageId")int coursePackageId);
	/**
	 * 学习进度学习完成度
	 *
	 * @param userId
	 * @param courseId
	 * @return
	 */
	String getstudystudyisok(@Param("userId")int userId,@Param("courseId")int courseId,@Param("coursePackageId")int coursePackageId);

	/**
	 * 课程章节信息查询学习记录
	 *
	 * @param
	 * @param courseStudyhistory
	 * @return
	 */
	List<CourseStudyhistory> getCourseList(@Param("e")CourseStudyhistory courseStudyhistory);

	/**
	 * 课程章节信息查询学习记录导出用
	 *
	 * @param
	 * @param courseStudyhistory
	 * @return
	 */
	List<CourseStudyhistory> getCourseListSort(@Param("e")CourseStudyhistory courseStudyhistory);


	/**
	 * 套餐课程查询学习记录
	 *
	 * @param
	 * @param courseStudyhistory
	 * @return
	 */
	List<CourseStudyhistory> getCoursepackageList(IPage page, @Param("e")CourseStudyhistory courseStudyhistory);

	/**
	 * 根据学员和章节查询当前学习记录详情
	 *
	 * @param userId
	 * @param kpointId
	 * @return
	 */
	CourseStudyhistory getStudyHistory(@Param("userId")int userId,@Param("kpointId")int kpointId,@Param("coursePackageId")Integer coursePackageId,@Param("studyType")int studyType);

	/**
	 * 根据学员和章节查询当前学习记录详情
	 *
	 * @param userId
	 * @param kpointId
	 * @return
	 */
	CourseStudyhistory getOnesStudyHistory(@Param("userId")int userId,@Param("kpointId")int kpointId,@Param("studyType")int studyType,@Param("packAgee")int packAgee);

	/**
	 * 章节观看总数
	 *
	 * @param courseId
	 * @param kpointId
	 * @return
	 */
	String getWatchNum(@Param("courseId")int courseId,@Param("kpointId")int kpointId,@Param("coursePackageId")int coursePackageId);

	/**
	 * 购买单录播课程总人数
	 * @param courseId
	 * 	@param courseType
	 */
	String getcoursepepo(@Param("courseId")int courseId);

	/**
	 * 已学习人数
	 *
	 * @param courseId
	 * @param kpointId
	 * @return
	 */
	String getWatchShould(@Param("courseId")int courseId,@Param("kpointId")int kpointId,@Param("coursePackageId")int coursePackageId);

	/**
	 *
	 *学习完成人数
	 * @param courseId
	 * @param kpointId
	 * @return
	 */
	String getWatchComplete(@Param("courseId")int courseId,@Param("kpointId")int kpointId,@Param("coursePackageId")int coursePackageId);

	/**
	 * 获取章节观看总数数据，返回map
	 */
	@MapKey("kpointId")
	Map<String,Map> getCourseKpointStudyHistoryDateToMap(CourseStudyhistory courseStudyhistory);

	/**
	 * 按照用户数据查询所有订单课程信息
	 *
	 * @param page
	 * @param courseStudyhistory
	 * @return
	 */
	List<CourseStudyhistory> selectCourseUser(IPage page, @Param("e")CourseStudyhistory courseStudyhistory);


	/**
	 * 按照用户数据查询所有订单课程信息全部信息
	 *
	 * @param courseStudyhistory
	 * @return
	 */
	List<CourseStudyhistory> selectCourseUserNoPage( @Param("e")CourseStudyhistory courseStudyhistory);

	/**
	 * 按照用户数据查询所有订单课程信息
	 *
	 * @param page
	 * @param courseStudyhistory
	 * @return
	 */
	List<CourseStudyhistory> selectStudentShouldAndComplete(IPage page, @Param("e")CourseStudyhistory courseStudyhistory);

	/**
	 * 按照用户数据查询所有订单课程信息全部信息
	 *
	 * @param courseStudyhistory
	 * @return
	 */
	List<CourseStudyhistory> selectStudentShouldAndCompleteNoPage(@Param("e")CourseStudyhistory courseStudyhistory);


	/**
	 * 学员学习记录导出
	 * @param page
	 * @return
	 */
	List<CourseStudyhistoryExcel> exportUserStudyHistory(Page<CourseStudyhistoryExcel> page, @Param("ew") CourseStudyhistoryVO courseStudyhistoryVO);


	/**
	 * 课程章节信息查询学习记录
	 *
	 * @param
	 * @param courseStudyhistory
	 * @return
	 */
	List<CourseStudyhistory> getPackageList(@Param("e")CourseStudyhistory courseStudyhistory);

	/**
	 * 课程观看总数
	 *
	 * @param courseId
	 * @param userList
	 * @return
	 */
	String getCourseWatchNum(@Param("courseId")int courseId, @Param("userList") String userList);

	/**
	 * 课程观看总时长
	 *
	 * @param courseId
	 * @param
	 * @return
	 */
	int getCourseWatchTime(@Param("courseId")int courseId);


	/**
	 * 查询购买但没有学习的用户
	 *
	 * @param page
	 * @param courseStudyhistory
	 * @return
	 */
	List<CourseStudyhistory> selectNoStudy(IPage page, @Param("e") CourseStudyhistory courseStudyhistory);


	/**
	 * 查询购买但没有学习的用户全部信息
	 *
	 * @param courseStudyhistory
	 * @return
	 */
	List<CourseStudyhistory> selectNoStudyNoPage(@Param("e") CourseStudyhistory courseStudyhistory);


	/**
	 * 查询购买但没有学习的用户
	 *
	 * @param page
	 * @param courseStudyhistory
	 * @return
	 */
	List<CourseStudyhistory> selectPackageNoStudy(IPage page, @Param("e") CourseStudyhistory courseStudyhistory);

	/**
	 * 查询购买但没有学习的用户全部信息
	 *
	 * @param courseStudyhistory
	 * @return
	 */
	List<CourseStudyhistory> selectPackageNoStudyNoPage(@Param("e") CourseStudyhistory courseStudyhistory);

	/**
	 * 查询所有的套餐购买学员
	 *
	 * @param
	 * @param courseStudyhistory
	 * @return
	 */
	List<CourseStudyhistory> getPackageUserList(IPage page, @Param("e")CourseStudyhistory courseStudyhistory);

	/**
	 * 查询所有的套餐购买学员全部信息
	 *
	 * @param courseStudyhistory
	 * @return
	 */
	List<CourseStudyhistory> getPackageUserListNoPage( @Param("e")CourseStudyhistory courseStudyhistory);

	/**
	 * 查询所有购买课程的学员
	 */
	List<CourseStudyhistory> getCourseUserList(IPage page, @Param("e")CourseStudyhistory courseStudyhistory);


	/**
	 * 查询课程及章节信息
	 *
	 * @param kpointId
	 * @param courseId
	 * @return
	 */
	CourseStudyhistory getKpointAndCourse(@Param("courseId")int courseId,@Param("kpointId")int kpointId);

	/**
	 * 查询套餐已学习的课程
	 *
	 * @param page
	 * @param courseStudyhistory
	 * @return
	 */
	List<CourseStudyhistory> selectPackageStudentShouldAndComplete(IPage page, @Param("e")CourseStudyhistory courseStudyhistory);


	/**
	 * 查询套餐已学习的课程全部信息
	 *
	 * @param courseStudyhistory
	 * @return
	 */
	List<CourseStudyhistory> selectPackageStudentShouldAndCompleteNoPage( @Param("e")CourseStudyhistory courseStudyhistory);

	/**
	 * 根据节删章节id删除学习记录
	 * @param id
	 */
    int removeByKpointId(Integer id);
}
