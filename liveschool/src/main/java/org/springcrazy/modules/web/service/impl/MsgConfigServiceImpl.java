
package org.springcrazy.modules.web.service.impl;

import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springcrazy.common.tool.MsgSMSFactory;
import org.springcrazy.common.tool.ProfileConfig;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.tool.utils.DateUtil;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.cms.entity.WebsiteProfile;
import org.springcrazy.modules.msg.entity.MsgReceive;
import org.springcrazy.modules.msg.service.IMsgReceiveService;
import org.springcrazy.modules.user.entity.Student;
import org.springcrazy.modules.web.entity.MsgConfig;
import org.springcrazy.modules.web.mapper.MsgConfigMapper;
import org.springcrazy.modules.web.service.IMsgConfigService;
import org.springcrazy.modules.web.vo.MsgConfigVO;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *  服务实现类
 *
 * @author TongZhou
 * @since 2020-05-09
 */
@Service
@AllArgsConstructor
public class MsgConfigServiceImpl extends ServiceImpl<MsgConfigMapper, MsgConfig> implements IMsgConfigService {

	private ThreadPoolTaskExecutor taskExecutor;
	private IMsgReceiveService msgReceiveService;

	@Override
	public IPage<MsgConfigVO> selectMsgConfigPage(IPage<MsgConfigVO> page, MsgConfigVO msgConfig) {
		return page.setRecords(baseMapper.selectMsgConfigPage(page, msgConfig));
	}

	@Override
	public void sendMsg(Student student, Map<String,String> params, String code) {
		MsgConfig msgConfig = new MsgConfig();
		msgConfig.setCode(code);
		msgConfig = this.getOne(Condition.getQueryWrapper(msgConfig));
		//发送消息

		List<String> arr = Func.toStrList(msgConfig.getMsgType());
		for (String msgType : arr) {
			switch (msgType){
				//发送站内信
				case "1":
					MsgReceive msgReceive = new MsgReceive();
					msgReceive.setContent(convertEngine(msgConfig.getContent(),params));
					msgReceive.setCusId(null);
					msgReceive.setType(MsgReceive.SYSTEMMSG);
					msgReceive.setStatus(MsgReceive.STATUS_NOREAD);
					msgReceive.setReceivingCusid(student.getId());
					msgReceive.setSendRange(msgReceive.getSendRange());
					msgReceive.setCreateTime(DateUtil.now());
					msgReceiveService.save(msgReceive);
					break;
				//发送邮件
				case "2":
					MsgConfig finalMsgConfig = msgConfig;
					taskExecutor.execute(()->{
						asynSendEmail(student.getEmail(), finalMsgConfig,params);
					});
					break;
				//发送短信
				case "3":
					MsgSMSFactory.getMsgSender().sendBatchSMS(Func.toStrList(student.getMobile()),msgConfig.getSmsSign(),msgConfig.getSmsTemplate(),params);
					break;
			}
		}
	}

	public String convertEngine(String content,Map<String,String> params){
		for(String key : params.keySet()){
			content = content.replace("{"+key+"}",params.get(key));
		}
		return content;
	}

	/**
	 * 给指定用户发送站内信
	 */
	public void asynSendEmail(String email, MsgConfig msgConfig,Map<String,String> params){
		//判断用户是否已经邮箱验证
		if(Func.isBlank(email)){
			return;
		}
		Map<String, String> config = ProfileConfig.getConfig(WebsiteProfile.EMAIL);
		MailAccount account = new MailAccount();
		account.setHost(config.get("smtp"));
		account.setPort(Integer.parseInt(config.get("port")));
		account.setAuth(true);
		account.setPass(config.get("password"));
		account.setFrom(config.get("login_name"));
		// ssl方式发送
		account.setSslEnable(true);


			Mail.create(account)
					.setTos(email)
					.setTitle(msgConfig.getName())
					.setContent(convertEngine(msgConfig.getContent(),params))
					.setHtml(true)
					//关闭session
					.setUseGlobalSession(false)
					.send();
	}

}
