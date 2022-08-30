
package org.springcrazy.modules.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.web.entity.StatUserArea;
import org.springcrazy.modules.web.vo.StatUserAreaVO;

/**
 * 用户ip登录所在区域记录 服务类
 *
 * @author TongZhou
 * @since 2020-05-20
 */
public interface IStatUserAreaService extends IService<StatUserArea> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param statUserArea
	 * @return
	 */
	IPage<StatUserAreaVO> selectStatUserAreaPage(IPage<StatUserAreaVO> page, StatUserAreaVO statUserArea);

}
