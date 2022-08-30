
package org.springcrazy.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.core.log.model.LogError;
import org.springcrazy.modules.system.mapper.LogErrorMapper;
import org.springcrazy.modules.system.service.ILogErrorService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 服务实现类
 *

 */
@Service
public class LogErrorServiceImpl extends ServiceImpl<LogErrorMapper, LogError> implements ILogErrorService {


    @Override
    public void clearSystemLog() {
        LocalDateTime now = LocalDateTime.now();
        now = now.minus(30, ChronoUnit.DAYS);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.lt("create_time",now);
        this.remove(queryWrapper);
    }
}
