
package org.springcrazy.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.core.log.model.LogError;

/**
 * 服务类

 */
public interface ILogErrorService extends IService<LogError> {

    public void clearSystemLog();

}
