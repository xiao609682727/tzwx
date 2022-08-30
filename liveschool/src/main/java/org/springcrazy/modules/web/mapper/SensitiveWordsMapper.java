
package org.springcrazy.modules.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springcrazy.modules.web.entity.SensitiveWords;
import org.springcrazy.modules.web.vo.SensitiveWordsVO;

import java.util.List;

/**
 * 网站敏感词 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-05-09
 */
public interface SensitiveWordsMapper extends BaseMapper<SensitiveWords> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param sensitiveWords
	 * @return
	 */
	List<SensitiveWordsVO> selectSensitiveWordsPage(IPage page, SensitiveWordsVO sensitiveWords);
	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param sensitiveWords
	 * @return
	 */
	List<SensitiveWords> selectSensitiveWordsPageList (IPage page, @Param("sensitiveWords") SensitiveWords sensitiveWords);
}
