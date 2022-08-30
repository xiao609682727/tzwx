
package org.springcrazy.modules.lineclass.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.lineclass.entity.LineclassEnroll;

/**
 * 线下课报名记录表数据传输对象实体类
 *
 * @author TongZhou
 * @since 2021-05-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LineclassEnrollDTO extends LineclassEnroll {
	private static final long serialVersionUID = 1L;

}
