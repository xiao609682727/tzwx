
package org.springcrazy.modules.develop.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.develop.entity.Code;

/**
 * 服务类
 *

 */
public interface ICodeService extends IService<Code> {

	/**
	 * 提交
	 *
	 * @param code
	 * @return
	 */
	boolean submit(Code code);

}
