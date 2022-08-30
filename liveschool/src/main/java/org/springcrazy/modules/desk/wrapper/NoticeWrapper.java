
package org.springcrazy.modules.desk.wrapper;

import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.core.tool.utils.SpringUtil;
import org.springcrazy.modules.desk.entity.Notice;
import org.springcrazy.modules.desk.vo.NoticeVO;
import org.springcrazy.modules.system.service.IDictService;

/**
 * Notice包装类,返回视图层所需的字段

 */
public class NoticeWrapper extends BaseEntityWrapper<Notice, NoticeVO> {

	private static IDictService dictService;

	static {
		dictService = SpringUtil.getBean(IDictService.class);
	}

	public static NoticeWrapper build() {
		return new NoticeWrapper();
	}

	@Override
	public NoticeVO entityVO(Notice notice) {
		NoticeVO noticeVO = BeanUtil.copy(notice, NoticeVO.class);
		String categoryName = dictService.getValue("notice", noticeVO.getCategory());
		noticeVO.setCategoryName(categoryName);
		return noticeVO;
	}

}
