
package org.springcrazy.modules.exam.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.modules.exam.entity.ErrorCheck;
import org.springcrazy.modules.exam.mapper.ErrorCheckMapper;
import org.springcrazy.modules.exam.service.IErrorCheckService;
import org.springcrazy.modules.exam.vo.ErrorCheckVO;
import org.springframework.stereotype.Service;

/**
 * 试题纠错表 服务实现类
 *
 * @author TongZhou
 * @since 2020-12-17
 */
@Service
public class ErrorCheckServiceImpl extends ServiceImpl<ErrorCheckMapper, ErrorCheck> implements IErrorCheckService {

	@Override
	public IPage<ErrorCheckVO> selectErrorCheckPage(IPage<ErrorCheckVO> page, ErrorCheckVO errorCheck) {
		return page.setRecords(baseMapper.selectErrorCheckPage(page, errorCheck));
	}

}
