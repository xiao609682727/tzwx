
package org.springcrazy.modules.msg.service.impl;

import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springcrazy.common.tool.ProfileConfig;
import org.springcrazy.core.mp.base.BaseServiceImpl;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.core.tool.utils.RegexUtil;
import org.springcrazy.modules.cms.entity.WebsiteProfile;
import org.springcrazy.modules.cms.service.IWebsiteProfileService;
import org.springcrazy.modules.msg.entity.MsgEmail;
import org.springcrazy.modules.msg.entity.MsgSystem;
import org.springcrazy.modules.msg.mapper.MsgEmailMapper;
import org.springcrazy.modules.msg.service.IMsgEmailService;
import org.springcrazy.modules.msg.vo.MsgEmailVO;
import org.springcrazy.modules.user.entity.Student;
import org.springcrazy.modules.user.service.IStudentService;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 邮件发送记录 服务实现类
 *
 * @author TongZhou
 * @since 2020-05-18
 */
@Service
@AllArgsConstructor
public class MsgEmailServiceImpl extends BaseServiceImpl<MsgEmailMapper, MsgEmail> implements IMsgEmailService {

	private ThreadPoolTaskExecutor taskExecutor;
	private IStudentService studentService;
	private IWebsiteProfileService websiteProfileService;

	@Override
	public IPage<MsgEmailVO> selectMsgEmailPage(IPage<MsgEmailVO> page, MsgEmailVO msgEmail) {
		return page.setRecords(baseMapper.selectMsgEmailPage(page, msgEmail));
	}

	@Override
	public boolean saveMsgEmail(MsgEmail msgEmail) {
		this.saveOrUpdate(msgEmail);
		//发送消息
		sendMsg(msgEmail);
		return true;
	}

	@Override
	public void sendMsg(MsgEmail msgEmail){
		//发送全部用户
		if(Func.equals(msgEmail.getSendRange(),MsgSystem.ALLUSER)){
			asynSendMsgAllUser(msgEmail);
		}
		//发送某个用户
		if(Func.equals(msgEmail.getSendRange(),MsgSystem.USERS)){
			List<Student> students = studentService.listByIds(Func.toIntList(msgEmail.getIds()));
			List<String> collect = students.stream().filter(student -> Func.isNotEmpty(student.getEmail())).map(student -> student.getEmail()).collect(Collectors.toList());
			asynSendMsgByList(collect,msgEmail);
		}
	}

	/**
	 * 给全部用户发送站内信
	 */
	public void asynSendMsgAllUser(MsgEmail msgEmail){
		taskExecutor.execute(()->{
			int count = studentService.count();
			int totalPages = (count/1000)+1;
			for (int i = 0; i < totalPages; i++) {
				Page<Student> page = new Page((long)i, 1000);
				Page<Student> pages = studentService.page(page);

				List<String> userList = pages.getRecords().stream().filter(student ->Func.isNotEmpty(student.getEmail()) && RegexUtil.match(RegexUtil.EMAIL,student.getEmail())).map(student -> student.getEmail()).collect(Collectors.toList());
				asynSendMsgByList(userList,msgEmail);
			}
		});
	}

	/**
	 * 给指定用户发送站内信
	 * @param userList
	 */
	public void asynSendMsgByList(List<String> userList,MsgEmail msgEmail){
		Map<String, String> config = ProfileConfig.getConfig(WebsiteProfile.EMAIL);
		MailAccount account = new MailAccount();
		account.setHost(config.get("smtp"));
		account.setPort(Integer.parseInt(config.get("port")));
		account.setAuth(true);
		account.setPass(config.get("password"));
		account.setFrom(config.get("login_name"));
		// ssl方式发送
		account.setSslEnable(true);
		userList.forEach(u ->{
			Mail.create(account)
					.setTos(u)
					.setTitle(msgEmail.getTitle())
					.setContent(msgEmail.getContent())
					.setHtml(true)
					//关闭session
					.setUseGlobalSession(false)
					.send();
		});
	}
}
