
package org.springcrazy.modules.web.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.core.tool.node.INode;
import org.springcrazy.modules.web.entity.HelpMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * 帮助菜单视图实体类
 *
 * @author TongZhou
 * @since 2020-05-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "HelpMenuVO对象", description = "帮助菜单")
public class HelpMenuVO extends HelpMenu implements INode{
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
