
package org.springcrazy.modules.develop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.core.tool.constant.CrazyConstant;
import org.springcrazy.modules.develop.entity.Code;
import org.springcrazy.modules.develop.mapper.CodeMapper;
import org.springcrazy.modules.develop.service.ICodeService;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 *

 */
@Service
public class CodeServiceImpl extends ServiceImpl<CodeMapper, Code> implements ICodeService {

	@Override
	public boolean submit(Code code) {
		code.setIsDeleted(CrazyConstant.DB_NOT_DELETED);
		return saveOrUpdate(code);
	}

}
