
package org.springcrazy.modules.exam.wrapper;

import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.modules.exam.entity.Exampaper;
import org.springcrazy.modules.exam.vo.ExampaperVO;

/**
 * 考试试卷表包装类,返回视图层所需的字段
 *
 * @author TongZhou
 * @since 2020-12-01
 */

public class ExampaperWrapper extends BaseEntityWrapper<Exampaper, ExampaperVO>  {

    public static ExampaperWrapper build() {
        return new ExampaperWrapper();
    }

	@Override
	public ExampaperVO entityVO(Exampaper exampaper) {
		ExampaperVO exampaperVO = BeanUtil.copy(exampaper, ExampaperVO.class);

		return exampaperVO;
	}

}
