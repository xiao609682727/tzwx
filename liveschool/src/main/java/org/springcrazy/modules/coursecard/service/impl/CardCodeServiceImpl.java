
package org.springcrazy.modules.coursecard.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.modules.coursecard.entity.CardCode;
import org.springcrazy.modules.coursecard.mapper.CardCodeMapper;
import org.springcrazy.modules.coursecard.service.ICardCodeService;
import org.springcrazy.modules.coursecard.vo.CardCodeVO;
import org.springcrazy.modules.system.excel.ExportCardCodeExcel;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 课程卡编码表 服务实现类
 *
 * @author TongZhou
 * @since 2021-04-02
 */
@Service
public class CardCodeServiceImpl extends ServiceImpl<CardCodeMapper, CardCode> implements ICardCodeService {

	@Override
	public IPage<CardCodeVO> selectCardCodePage(IPage<CardCodeVO> page, CardCodeVO cardCodeVO) {
		return page.setRecords(baseMapper.selectCardCodePage(page, cardCodeVO));
	}
	/**
	 * 获取导出课程卡卡号数据
	 */
	public void exportCardCode(HttpServletResponse response,CardCodeVO cardCode){
		ExcelWriter excelWriter = null;
		try {
			Integer size = 3000;

			excelWriter = EasyExcel.write(response.getOutputStream(), ExportCardCodeExcel.class).build();
			WriteSheet writeSheet = EasyExcel.writerSheet("课程卡数据表").build();
			//分页写入
//			QueryWrapper<CardCode> queryWrapper = Condition.getQueryWrapper(new CardCode());

//			queryWrapper.lambda().eq(Func.isNotEmpty(cardCode.get("registerFrom")),Student::getRegisterFrom,cardCode.get("registerFrom"));

//			Integer count = baseMapper.selectCount(queryWrapper);
			Page<ExportCardCodeExcel> page = new Page<>();
			List<ExportCardCodeExcel> list1 = baseMapper.exportCardCodeVO(page, cardCode);
			Integer count =Integer.valueOf(page.getTotal()+"");
			for (int i = 1; i <= (count/size)+1; i++) {
				page.setCurrent(i);
				page.setSize(size);
				List<ExportCardCodeExcel> list = baseMapper.exportCardCodeVO(page, cardCode);
				excelWriter.write(list, writeSheet);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (excelWriter != null) {
				excelWriter.finish();
			}
		}
	}
}
