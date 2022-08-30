
package org.springcrazy.modules.msg.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springcrazy.common.tool.MsgSMSFactory;
import org.springcrazy.core.mp.base.BaseServiceImpl;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.msg.entity.MsgMobile;
import org.springcrazy.modules.msg.entity.MsgSystem;
import org.springcrazy.modules.msg.mapper.MsgMobileMapper;
import org.springcrazy.modules.msg.service.IMsgMobileService;
import org.springcrazy.modules.msg.vo.MsgMobileVO;
import org.springcrazy.modules.user.entity.Student;
import org.springcrazy.modules.user.service.IStudentService;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 手机短信发送记录 服务实现类
 *
 * @author TongZhou
 * @since 2020-05-18
 */
@Service
@AllArgsConstructor
public class MsgMobileServiceImpl extends BaseServiceImpl<MsgMobileMapper, MsgMobile> implements IMsgMobileService {

	private ThreadPoolTaskExecutor taskExecutor;
	private IStudentService studentService;

	@Override
	public IPage<MsgMobileVO> selectMsgMobilePage(IPage<MsgMobileVO> page, MsgMobileVO msgMobile) {
		return page.setRecords(baseMapper.selectMsgMobilePage(page, msgMobile));
	}

	@Override
	public boolean saveMsgMobile(MsgMobile msgMobile) {
		this.saveOrUpdate(msgMobile);
		//发送消息
		sendMsg(msgMobile,msgMobile.getContent());
		return true;
	}


	@Override
	public void sendMsg(MsgMobile msgMobile,String params){
		if(Func.isBlank(params)){
			params = "{}";
		}
		//发送全部用户
		if(Func.equals(msgMobile.getSendRange(), MsgSystem.ALLUSER)){
			asynSendMsgAllUser(msgMobile,params);
		}
		//发送某个用户
		if(Func.equals(msgMobile.getSendRange(),MsgSystem.USERS)){
			if(Func.isNotEmpty(msgMobile.getIds())){
				List<Student> students = studentService.listByIds(Func.toIntList(msgMobile.getIds()));
				List<String> collect = students.stream().filter(student -> Func.isNotEmpty(student.getMobile())).map(student -> student.getMobile()).collect(Collectors.toList());
				asynSendMsgByList(collect,msgMobile,params);
			}
		}
	}

	/**
	 * 给全部用户发送站内信
	 * 短信群发一次100个
	 */
	public void asynSendMsgAllUser(MsgMobile msgMobile,String params){
		taskExecutor.execute(()->{
			int count = studentService.count();
			int totalPages = (count/100)+1;
			for (int i = 0; i < totalPages; i++) {
				Page<Student> page = new Page((long)i, 100);
				Page<Student> pages = studentService.page(page);
				List<String> userList = pages.getRecords().stream().filter(student -> Func.isNotEmpty(student.getMobile())).map(student -> student.getMobile()).collect(Collectors.toList());
				asynSendMsgByList(userList,msgMobile,params);
			}
		});
	}

	/**
	 * 给指定用户发送站内信
	 * @param userList
	 */
	public void asynSendMsgByList(List<String> userList,MsgMobile msgMobile,String params){
			MsgSMSFactory.getMsgSender().sendBatchSMS(userList,msgMobile.getSignName(),msgMobile.getTemplateCode(),params);
	}

}
