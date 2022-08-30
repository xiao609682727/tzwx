
package org.springcrazy.modules.edu.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.modules.edu.entity.CourseMaterial;
import org.springcrazy.modules.edu.mapper.CourseMaterialMapper;
import org.springcrazy.modules.edu.service.ICourseMaterialService;
import org.springcrazy.modules.edu.vo.CourseMaterialVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 知识点的基本信息 服务实现类
 *
 * @author TongZhou
 * @since 2020-04-25
 */
@Service
public class CourseMaterialServiceImpl extends ServiceImpl<CourseMaterialMapper, CourseMaterial> implements ICourseMaterialService {

    @Override
    public IPage<CourseMaterialVO> selectCourseMaterialPage(IPage<CourseMaterialVO> page, CourseMaterialVO courseMaterial) {
        return page.setRecords(baseMapper.selectCourseMaterialPage(page, courseMaterial));
    }

    @Override
    public List<CourseMaterial> getMaterialsBykpointId(Integer id) {
        return baseMapper.getMaterialsBykpointId(id);
    }

    @Override
    public List<CourseMaterial> getMaterialsByCourseId(Integer id) {
        return baseMapper.getMaterialsByCourseId(id);
    }
}
