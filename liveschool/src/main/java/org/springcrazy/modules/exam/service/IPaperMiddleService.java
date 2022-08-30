
package org.springcrazy.modules.exam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.exam.entity.PaperMiddle;
import org.springcrazy.modules.exam.vo.PaperMiddleVO;

/**
 * 大题（试卷试题类型中间表） 服务类
 *
 * @author TongZhou
 * @since 2020-12-15
 */
public interface IPaperMiddleService extends IService<PaperMiddle> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param paperMiddle
	 * @return
	 */
	IPage<PaperMiddleVO> selectPaperMiddlePage(IPage<PaperMiddleVO> page, PaperMiddleVO paperMiddle);

}
