
package org.springcrazy.modules.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.web.entity.SmsConfig;
import org.springcrazy.modules.web.vo.SmsConfigVO;

/**
 *  服务类
 *
 * @author TongZhou
 * @since 2020-05-09
 */
public interface ISmsConfigService extends IService<SmsConfig> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param smsConfig
	 * @return
	 */
	IPage<SmsConfigVO> selectSmsConfigPage(IPage<SmsConfigVO> page, SmsConfigVO smsConfig);

}
