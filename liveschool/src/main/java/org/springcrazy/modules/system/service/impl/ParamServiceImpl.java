
package org.springcrazy.modules.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.core.mp.base.BaseServiceImpl;
import org.springcrazy.modules.system.entity.Param;
import org.springcrazy.modules.system.mapper.ParamMapper;
import org.springcrazy.modules.system.service.IParamService;
import org.springcrazy.modules.system.vo.ParamVO;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 *

 */
@Service
public class ParamServiceImpl extends BaseServiceImpl<ParamMapper, Param> implements IParamService {

	@Override
	public IPage<ParamVO> selectParamPage(IPage<ParamVO> page, ParamVO param) {
		return page.setRecords(baseMapper.selectParamPage(page, param));
	}

}
