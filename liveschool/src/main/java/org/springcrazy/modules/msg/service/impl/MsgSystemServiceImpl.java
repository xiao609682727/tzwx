
package org.springcrazy.modules.msg.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.api.client.util.Lists;
import lombok.AllArgsConstructor;
import org.springcrazy.core.mp.base.BaseServiceImpl;
import org.springcrazy.core.tool.utils.DateUtil;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.msg.entity.MsgReceive;
import org.springcrazy.modules.msg.entity.MsgSystem;
import org.springcrazy.modules.msg.mapper.MsgSystemMapper;
import org.springcrazy.modules.msg.service.IMsgReceiveService;
import org.springcrazy.modules.msg.service.IMsgSystemService;
import org.springcrazy.modules.msg.vo.MsgSystemVO;
import org.springcrazy.modules.user.entity.Student;
import org.springcrazy.modules.user.service.IStudentService;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统消息 服务实现类
 *
 * @author TongZhou
 * @since 2020-05-18
 */
@Service
@AllArgsConstructor
public class MsgSystemServiceImpl extends BaseServiceImpl<MsgSystemMapper, MsgSystem> implements IMsgSystemService {

	private IStudentService studentService;
	private ThreadPoolTaskExecutor taskExecutor;
	private IMsgReceiveService msgReceiveService;


	@Override
	public IPage<MsgSystemVO> selectMsgSystemPage(IPage<MsgSystemVO> page, MsgSystemVO msgSystem) {
		return page.setRecords(baseMapper.selectMsgSystemPage(page, msgSystem));
	}

	@Override
	public boolean saveMsgSystem(MsgSystem msgSystem) {
		this.saveOrUpdate(msgSystem);
		//发送消息
		sendMsg(msgSystem);
		return true;
	}

	public void sendMsg(MsgSystem msgSystem){
		//发送全部用户
		if(Func.equals(msgSystem.getSendRange(),MsgSystem.ALLUSER)){
			asynSendMsgAllUser(msgSystem);
		}
		//发送某个用户
		if(Func.equals(msgSystem.getSendRange(),MsgSystem.USERS)){
			List<Integer> userList = Func.toIntList(msgSystem.getIds());
			asynSendMsgByList(userList,msgSystem);
		}
	}

	/**
	 * 给全部用户发送站内信
	 * @param msgSystem
	 */
	public void asynSendMsgAllUser(MsgSystem msgSystem){
		taskExecutor.execute(()->{
			int count = studentService.count();
			int totalPages = (count/1000)+1;
			for (int i = 0; i < totalPages; i++) {
				Page<Student> page = new Page((long)i, 1000);
				Page<Student> pages = studentService.page(page);
				List<Integer> userList = pages.getRecords().stream().map(student -> student.getId()).collect(Collectors.toList());
				asynSendMsgByList(userList,msgSystem);
			}
		});
	}

	/**
	 * 给指定用户发送站内信
	 * @param userList
	 * @param msgSystem
	 */
	public void asynSendMsgByList(List<Integer> userList,MsgSystem msgSystem){
		taskExecutor.execute(()->{
			List<MsgReceive> list = Lists.newArrayList();
			userList.forEach(u ->{
				MsgReceive msgReceive = new MsgReceive();
				msgReceive.setContent(msgSystem.getContent());
				msgReceive.setCusId(msgSystem.getCreateUser());
				msgReceive.setType(MsgReceive.SYSTEMMSG);
				msgReceive.setStatus(MsgReceive.STATUS_NOREAD);
				msgReceive.setReceivingCusid(u);
				msgReceive.setSendRange(msgReceive.getSendRange());
				msgReceive.setCreateTime(DateUtil.now());
				list.add(msgReceive);
			});
			msgReceiveService.saveBatch(list);
		});
	}

}
