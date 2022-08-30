
package org.springcrazy.modules.coursecard.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.coursecard.entity.CardCode;
import org.springcrazy.modules.coursecard.vo.CardCodeVO;

import javax.servlet.http.HttpServletResponse;

/**
 * 课程卡编码表 服务类
 *
 * @author TongZhou
 * @since 2021-04-02
 */
public interface ICardCodeService extends IService<CardCode> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param cardCode
	 * @return
	 */
	IPage<CardCodeVO> selectCardCodePage(IPage<CardCodeVO> page, CardCodeVO cardCode);
	/**
	 * 获取导出课程卡卡号数据
	 * @param response
	 * @return
	 */
	void exportCardCode(HttpServletResponse response, CardCodeVO cardCode);
}
