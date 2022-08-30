
package org.springcrazy.modules.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.web.entity.StatData;
import org.springcrazy.modules.web.vo.StatDataVO;

/**
 *  服务类
 *
 * @author TongZhou
 * @since 2020-05-19
 */
public interface IStatDataService extends IService<StatData> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param statData
	 * @return
	 */
	IPage<StatDataVO> selectStatDataPage(IPage<StatDataVO> page, StatDataVO statData);

	/**
	 * 根据用户信息统计区域数据
	 */
	void updateUserDataByArea();

	void updateAdminIndexData();

	/**
	 * 登录与访客记录
	 */
	void updateUserLog();

}
