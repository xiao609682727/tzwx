
package org.springcrazy.modules.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.web.entity.SmsConfig;
import org.springcrazy.modules.web.vo.SmsConfigVO;

import java.util.List;

/**
 *  Mapper 接口
 *
 * @author TongZhou
 * @since 2020-05-09
 */
public interface SmsConfigMapper extends BaseMapper<SmsConfig> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param smsConfig
	 * @return
	 */
	List<SmsConfigVO> selectSmsConfigPage(IPage page, SmsConfigVO smsConfig);

}
