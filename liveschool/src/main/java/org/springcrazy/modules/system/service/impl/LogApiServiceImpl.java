
package org.springcrazy.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.core.log.model.LogApi;
import org.springcrazy.modules.system.mapper.LogApiMapper;
import org.springcrazy.modules.system.service.ILogApiService;
import org.springframework.stereotype.Service;

/**
 * 服务实现类

 */
@Service
public class LogApiServiceImpl extends ServiceImpl<LogApiMapper, LogApi> implements ILogApiService {


}
