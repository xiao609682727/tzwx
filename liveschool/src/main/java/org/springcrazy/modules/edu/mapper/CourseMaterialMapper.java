
package org.springcrazy.modules.edu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.edu.entity.CourseMaterial;
import org.springcrazy.modules.edu.vo.CourseMaterialVO;

import java.util.List;

/**
 * 章节资料信息 Mapper 接口
 *
 * @author NCX
 * @since 2020-10-14
 */
public interface CourseMaterialMapper extends BaseMapper<CourseMaterial> {

    /**
     * 自定义分页
     *
     * @param page
     * @param courseMaterial
     * @return
     */
    List<CourseMaterialVO> selectCourseMaterialPage(IPage page, CourseMaterialVO courseMaterial);

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
