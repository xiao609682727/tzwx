
package org.springcrazy.modules.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.modules.user.entity.Teacher;
import org.springcrazy.modules.user.mapper.TeacherMapper;
import org.springcrazy.modules.user.service.ITeacherService;
import org.springcrazy.modules.user.vo.TeacherVO;
import org.springframework.stereotype.Service;

/**
 * 讲师 服务实现类
 *
 * @author TongZhou
 * @since 2020-04-28
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements ITeacherService {

	@Override
	public IPage<TeacherVO> selectTeacherPage(IPage<TeacherVO> page, TeacherVO teacher) {
		return page.setRecords(baseMapper.selectTeacherPage(page, teacher));
	}

}
