
package org.springcrazy.modules.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.exam.entity.ErrorCheck;
import org.springcrazy.modules.exam.vo.ErrorCheckVO;

import java.util.List;

/**
 * 试题纠错表 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-12-17
 */
public interface ErrorCheckMapper extends BaseMapper<ErrorCheck> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param errorCheck
	 * @return
	 */
	List<ErrorCheckVO> selectErrorCheckPage(IPage page, ErrorCheckVO errorCheck);

}
