
package org.springcrazy.modules.lineclass.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.lineclass.entity.LineclassEnroll;
import org.springcrazy.modules.lineclass.mapper.LineclassEnrollMapper;
import org.springcrazy.modules.lineclass.service.ILineclassEnrollService;
import org.springcrazy.modules.lineclass.vo.LineclassEnrollVO;
import org.springcrazy.modules.system.excel.ExportLineClassExcel;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 线下课报名记录表 服务实现类
 *
 * @author TongZhou
 * @since 2021-05-27
 */
@Service
public class LineclassEnrollServiceImpl extends ServiceImpl<LineclassEnrollMapper, LineclassEnroll> implements ILineclassEnrollService {

	@Override
	public IPage<LineclassEnrollVO> selectLineclassEnrollPage(IPage<LineclassEnrollVO> page, LineclassEnrollVO lineclassEnroll) {
		return page.setRecords(baseMapper.selectLineclassEnrollPage(page, lineclassEnroll));
	}

	@Override
	public void delectLineClassEnrol(Integer userId, Integer courseId) {
		baseMapper.delectLineClassEnrol(userId,courseId);
	}

	@Override
	public void updateLineClassEnrol(LineclassEnroll lineclassEnroll) {
		baseMapper.updateLineClassEnrol(lineclassEnroll);
	}

	@Override
	public IPage<LineclassEnrollVO> getCourseUserList(IPage<LineclassEnroll> page, LineclassEnroll lineclassEnroll) {
		return baseMapper.getCourseUserList(page,lineclassEnroll);
	}

	@Override
	public void exportLineClassUser(HttpServletResponse response, Map<String, Object> lineclassEnroll) {
		ExcelWriter excelWriter = null;
		try {
			Integer size = 3000;
			excelWriter = EasyExcel.write(response.getOutputStream(), ExportLineClassExcel.class).build();
			WriteSheet writeSheet = EasyExcel.writerSheet("报名记录表").build();
			//分页写入
			LineclassEnroll lineclassEnrollSelect = new LineclassEnroll();
			QueryWrapper<LineclassEnroll> queryWrapper = Condition.getQueryWrapper(new LineclassEnroll());
			queryWrapper.lambda().eq(LineclassEnroll::getCourseId,lineclassEnroll.get("courseId"));
			lineclassEnrollSelect.setCourseId(Integer.valueOf(lineclassEnroll.get("courseId").toString()));
			queryWrapper.lambda().like(Func.isNotEmpty(lineclassEnroll.get("userName")),LineclassEnroll::getUserName,lineclassEnroll.get("userName"));
			if(Func.isNotEmpty(lineclassEnroll.get("userName"))){
				lineclassEnrollSelect.setUserName(lineclassEnroll.get("userName").toString());
			}
			if(Func.isNotEmpty(lineclassEnroll.get("state"))){
				lineclassEnrollSelect.setState(Integer.valueOf(lineclassEnroll.get("state").toString()));
			}
			queryWrapper.lambda().like(Func.isNotEmpty(lineclassEnroll.get("mobile")),LineclassEnroll::getMobile,lineclassEnroll.get("mobile"));
			if(Func.isNotEmpty(lineclassEnroll.get("mobile"))){
				lineclassEnrollSelect.setUserName(lineclassEnroll.get("mobile").toString());
			}
			Integer count = baseMapper.selectCount(queryWrapper);
			Page<ExportLineClassExcel> page = new Page<>();
			for (int i = 1; i <= (count/size)+1; i++) {
				page.setCurrent(i);
				page.setSize(size);
				List<ExportLineClassExcel> list = baseMapper.exportLineClass(page, lineclassEnrollSelect);
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
