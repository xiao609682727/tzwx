
package org.springcrazy.modules.edu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.edu.entity.TrxorderDetail;
import org.springcrazy.modules.edu.vo.TrxorderDetailVO;

import java.util.List;

/**
 * 流水表 服务类
 *
 * @author TongZhou
 * @since 2020-05-09
 */
public interface ITrxorderDetailService extends IService<TrxorderDetail> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param trxorderDetail
	 * @return
	 */
	IPage<TrxorderDetailVO> selectTrxorderDetailPage(IPage<TrxorderDetailVO> page, TrxorderDetailVO trxorderDetail);


	/**
	 * 过期面授分页
	 *
	 * @param page
	 * @param trxorderDetail
	 * @return
	 */
	IPage<TrxorderDetailVO> getTrxorderDetailEndPage(IPage<TrxorderDetailVO> page, TrxorderDetailVO trxorderDetail);

	List<TrxorderDetailVO> selectTrxorderDetailList(List<Integer> list);

}
