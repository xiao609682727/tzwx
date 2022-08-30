
package org.springcrazy.modules.edu.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.core.tool.node.INode;
import org.springcrazy.modules.edu.entity.CourseKpoint;

import java.util.ArrayList;
import java.util.List;

/**
 * 知识点的基本信息视图实体类
 *
 * @author TongZhou
 * @since 2020-04-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CourseKpointVO对象", description = "知识点的基本信息")
public class CourseKpointVO extends CourseKpoint implements INode {
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
	 * 视频时长格式化
	 */
	private String videoTimeFmt;
	/**
	 * 学习进度
	 */
	private String studyLearning;

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
