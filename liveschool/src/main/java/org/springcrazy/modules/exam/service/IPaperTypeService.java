
package org.springcrazy.modules.exam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.exam.entity.PaperType;
import org.springcrazy.modules.exam.vo.PaperTypeVO;

/**
 * 试卷类型表 服务类
 *
 * @author TongZhou
 * @since 2020-12-01
 */
public interface IPaperTypeService extends IService<PaperType> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param paperType
	 * @return
	 */
	IPage<PaperTypeVO> selectPaperTypePage(IPage<PaperTypeVO> page, PaperTypeVO paperType);

}
