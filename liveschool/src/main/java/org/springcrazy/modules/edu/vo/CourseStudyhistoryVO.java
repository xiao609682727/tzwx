
package org.springcrazy.modules.edu.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.core.tool.node.INode;
import org.springcrazy.modules.edu.entity.CourseStudyhistory;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程播放记录(学习记录)视图实体类
 *
 * @author TongZhou
 * @since 2020-05-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CourseStudyhistoryVO对象", description = "课程播放记录(学习记录)")
public class CourseStudyhistoryVO extends CourseStudyhistory implements INode{
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	private Integer id;

	/**
	 * 父节点ID
	 */
	private Integer parentId;

	/**
	 * 上级菜单
	 */
	private String parentName;

	/**
	 * 子孙节点
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<INode> children;



	@Override
	public List<INode> getChildren() {
		if (this.children == null) {
			this.children = new ArrayList<>();
		}
		return this.children;
	}
}
