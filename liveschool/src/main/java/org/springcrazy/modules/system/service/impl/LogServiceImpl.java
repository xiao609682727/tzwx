package org.springcrazy.modules.system.service.impl;

import lombok.AllArgsConstructor;
import org.springcrazy.core.log.model.LogApi;
import org.springcrazy.core.log.model.LogError;
import org.springcrazy.core.log.model.LogUsual;
import org.springcrazy.modules.system.service.ILogApiService;
import org.springcrazy.modules.system.service.ILogErrorService;
import org.springcrazy.modules.system.service.ILogService;
import org.springcrazy.modules.system.service.ILogUsualService;
import org.springframework.stereotype.Service;

/**
 * Created by TongZhou.
 *

 */
@Service
@AllArgsConstructor
public class LogServiceImpl implements ILogService {

	ILogUsualService usualService;
	ILogApiService apiService;
	ILogErrorService errorService;

	@Override
	public Boolean saveUsualLog(LogUsual log) {
		return usualService.save(log);
	}

	@Override
	public Boolean saveApiLog(LogApi log) {
		return apiService.save(log);
	}

	@Override
	public Boolean saveErrorLog(LogError log) {
		return errorService.save(log);
	}

}
