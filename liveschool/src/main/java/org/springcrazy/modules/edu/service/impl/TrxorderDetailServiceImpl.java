
package org.springcrazy.modules.edu.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.modules.edu.entity.TrxorderDetail;
import org.springcrazy.modules.edu.mapper.TrxorderDetailMapper;
import org.springcrazy.modules.edu.service.ITrxorderDetailService;
import org.springcrazy.modules.edu.vo.TrxorderDetailVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 流水表 服务实现类
 *
 * @author TongZhou
 * @since 2020-05-09
 */
@Service
public class TrxorderDetailServiceImpl extends ServiceImpl<TrxorderDetailMapper, TrxorderDetail> implements ITrxorderDetailService {

	@Override
	public IPage<TrxorderDetailVO> selectTrxorderDetailPage(IPage<TrxorderDetailVO> page, TrxorderDetailVO trxorderDetail) {
		return page.setRecords(baseMapper.selectTrxorderDetailPage(page, trxorderDetail));
	}

	@Override
	public IPage<TrxorderDetailVO> getTrxorderDetailEndPage(IPage<TrxorderDetailVO> page, TrxorderDetailVO trxorderDetail) {
		return page.setRecords(baseMapper.getTrxorderDetailEndPage(page, trxorderDetail));
	}

	@Override
	public List<TrxorderDetailVO> selectTrxorderDetailList(List<Integer> list) {
		return baseMapper.selectTrxorderDetailList(list);
	}

}
