
package org.springcrazy.modules.edu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.edu.entity.CourseMaterial;
import org.springcrazy.modules.edu.vo.CourseMaterialVO;

import java.util.List;

/**
 * 知识点的基本信息 服务类
 *
 * @author TongZhou
 * @since 2020-04-25
 */
public interface ICourseMaterialService extends IService<CourseMaterial> {
    /**
     * 自定义分页
     *
     * @param page
     * @param courseMaterial
     * @return
     */
    IPage<CourseMaterialVO> selectCourseMaterialPage(IPage<CourseMaterialVO> page, CourseMaterialVO courseMaterial);

    /**
     * 根据章节id获取资料
     * @param id
     * @return
     */
    List<CourseMaterial> getMaterialsBykpointId(Integer id);

    /**
     * 根据课程id获取资料
     * @param id
     * @return
     */
    List<CourseMaterial> getMaterialsByCourseId(Integer id);
}
