
package org.springcrazy.modules.exam.wrapper;

import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.modules.exam.entity.ExampaperRecord;
import org.springcrazy.modules.exam.vo.ExampaperRecordVO;

/**
 * 考试记录表包装类,返回视图层所需的字段
 *
 * @author TongZhou
 * @since 2020-12-02
 */
public class ExampaperRecordWrapper extends BaseEntityWrapper<ExampaperRecord, ExampaperRecordVO>  {

    public static ExampaperRecordWrapper build() {
        return new ExampaperRecordWrapper();
    }

	@Override
	public ExampaperRecordVO entityVO(ExampaperRecord exampaperRecord) {
		ExampaperRecordVO exampaperRecordVO = BeanUtil.copy(exampaperRecord, ExampaperRecordVO.class);

		return exampaperRecordVO;
	}

}
