
package org.springcrazy.modules.desk.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.core.mp.base.BaseServiceImpl;
import org.springcrazy.modules.desk.entity.Notice;
import org.springcrazy.modules.desk.mapper.NoticeMapper;
import org.springcrazy.modules.desk.service.INoticeService;
import org.springframework.stereotype.Service;

/**
 * 服务实现类

 */
@Service
public class NoticeServiceImpl extends BaseServiceImpl<NoticeMapper, Notice> implements INoticeService {

	@Override
	public IPage<Notice> selectNoticePage(IPage<Notice> page, Notice notice) {
		return page.setRecords(baseMapper.selectNoticePage(page, notice));
	}

}
