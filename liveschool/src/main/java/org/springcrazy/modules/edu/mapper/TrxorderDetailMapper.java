
package org.springcrazy.modules.edu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springcrazy.modules.edu.entity.TrxorderDetail;
import org.springcrazy.modules.edu.vo.TrxorderDetailVO;

import java.util.List;

/**
 * 流水表 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-05-09
 */
public interface TrxorderDetailMapper extends BaseMapper<TrxorderDetail> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param trxorderDetail
	 * @return
	 */
	List<TrxorderDetailVO> selectTrxorderDetailPage(IPage page,@Param("trxorderDetail") TrxorderDetailVO trxorderDetail);

	/**
	 * 过期面授分页
	 *
	 * @param page
	 * @param trxorderDetail
	 * @return
	 */
	List<TrxorderDetailVO> getTrxorderDetailEndPage(IPage page,@Param("trxorderDetail")TrxorderDetailVO trxorderDetail);

	List<TrxorderDetailVO> selectTrxorderDetailList(List<Integer> list);
}
