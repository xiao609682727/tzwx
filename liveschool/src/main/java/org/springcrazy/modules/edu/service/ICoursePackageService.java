
package org.springcrazy.modules.edu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.edu.entity.CoursePackage;
import org.springcrazy.modules.edu.vo.CoursePackageVO;

import java.util.List;

/**
 *  服务类
 *
 * @author TongZhou
 * @since 2020-05-05
 */
public interface ICoursePackageService extends IService<CoursePackage> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param coursePackage
	 * @return
	 */
	IPage<CoursePackageVO> selectCoursePackagePage(IPage<CoursePackageVO> page, CoursePackageVO coursePackage);


	List<CoursePackageVO> selectCoursePackageList(CoursePackageVO coursePackage);
}
