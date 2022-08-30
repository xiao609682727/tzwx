
package org.springcrazy.modules.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.user.entity.Student;
import org.springcrazy.modules.web.entity.MsgConfig;
import org.springcrazy.modules.web.vo.MsgConfigVO;

import java.util.Map;

/**
 *  服务类
 *
 * @author TongZhou
 * @since 2020-05-09
 */
public interface IMsgConfigService extends IService<MsgConfig> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param msgConfig
	 * @return
	 */
	IPage<MsgConfigVO> selectMsgConfigPage(IPage<MsgConfigVO> page, MsgConfigVO msgConfig);


	void sendMsg(Student student, Map<String,String> params, String type);
}
