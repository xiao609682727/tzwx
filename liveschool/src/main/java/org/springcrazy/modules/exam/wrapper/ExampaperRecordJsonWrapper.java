
package org.springcrazy.modules.exam.wrapper;

import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.modules.exam.entity.ExampaperRecordJson;
import org.springcrazy.modules.exam.vo.ExampaperRecordJsonVO;

/**
 * 包装类,返回视图层所需的字段
 *
 * @author TongZhou
 * @since 2021-01-05
 */
public class ExampaperRecordJsonWrapper extends BaseEntityWrapper<ExampaperRecordJson, ExampaperRecordJsonVO>  {

    public static ExampaperRecordJsonWrapper build() {
        return new ExampaperRecordJsonWrapper();
    }

	@Override
	public ExampaperRecordJsonVO entityVO(ExampaperRecordJson exampaperRecordJson) {
		ExampaperRecordJsonVO exampaperRecordJsonVO = BeanUtil.copy(exampaperRecordJson, ExampaperRecordJsonVO.class);

		return exampaperRecordJsonVO;
	}

}
