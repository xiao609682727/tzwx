
package org.springcrazy.modules.cms.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springcrazy.common.sensitive.SensitivewordFilter;
import org.springcrazy.core.boot.ctrl.CrazyController;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.mp.support.Query;
import org.springcrazy.core.secure.utils.SecureUtil;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.utils.DateUtil;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.cms.entity.Comment;
import org.springcrazy.modules.cms.service.ICommentService;
import org.springcrazy.modules.cms.vo.CommentVO;
import org.springcrazy.modules.edu.entity.TrxorderDetail;
import org.springcrazy.modules.edu.service.ITrxorderDetailService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 *  控制器
 *
 * @author TongZhou
 * @since 2020-03-01
 */
@RestController
@AllArgsConstructor
@RequestMapping("/cms/comment")
@Api(value = "评论", tags = "评论")
public class CommentController extends CrazyController {

	private ICommentService commentService;

	private ITrxorderDetailService trxorderDetailService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入comment")
	public R<Comment> detail(Comment comment) {
		Comment detail = commentService.getOne(Condition.getQueryWrapper(comment));
		return R.data(detail);
	}

	/**
	 * 分页
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入comment")
	public R<IPage<Comment>> list(Comment comment, Query query) {
		IPage<Comment> pages = commentService.page(Condition.getPage(query), Condition.getQueryWrapper(comment));
		return R.data(pages);
	}

	/**
	 * 自定义分页
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入comment")
	public R<IPage<CommentVO>> page(CommentVO comment, Query query) {
		IPage<CommentVO> pages = commentService.selectCommentPage(Condition.getPage(query), comment);
		return R.data(pages);
	}

	/**
	 * 新增
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入comment")
	public R save(@Valid @RequestBody Comment comment) {
		comment.setAddtime(DateUtil.now());
		comment.setContent(SensitivewordFilter.replaceSensitiveWord(comment.getContent(),1,"*"));
		Integer userId = SecureUtil.getUserId();
		comment.setUserId(userId);
		return R.status(commentService.save(comment));
	}

	/**
	 * 修改
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入comment")
	public R update(@Valid @RequestBody Comment comment) {
		return R.status(commentService.updateById(comment));
	}

	/**
	 * 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入comment")
	public R submit(@Valid @RequestBody Comment comment) {
		comment.setContent(SensitivewordFilter.replaceSensitiveWord(comment.getContent(),1,"*"));
		return R.status(commentService.saveOrUpdate(comment));
	}


	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(commentService.removeByIds(Func.toIntList(ids)));
	}

	@PostMapping("/reply")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入comment")
	public R reply(@RequestBody Comment Pcomment) {

		Comment comment = commentService.getById(Pcomment.getId());
		Comment temp = new Comment();
		temp.setParentId(comment.getId());
		temp.setUserId(SecureUtil.getUserId());
		temp.setContent(SensitivewordFilter.replaceSensitiveWord(Pcomment.getContent(),2,"*"));
		temp.setAddtime(DateUtil.now());
		temp.setOtherId(comment.getOtherId());
		temp.setType(comment.getType());
		temp.setRoleType(comment.getRoleType());
		return R.status(commentService.save(temp));
	}


	/**
	 * 检查用户是否可以评论
	 */
	@GetMapping("/checkComment")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "检查用户是否可以评论", notes = "检查用户是否可以评论")
	public R checkComment(Integer courseId) {
		//判断用户是否已获取此课程，如果没有获取则不能评论
		TrxorderDetail trxorderDetail = new TrxorderDetail();
		Integer userId = SecureUtil.getUserId();
		if(ObjectUtil.isNull(userId)){
			return R.data(false);
		}
		trxorderDetail.setUserId(userId);
		trxorderDetail.setCourseId(courseId);
		trxorderDetail.setAuthStatus(TrxorderDetail.STATUS_SUCCESS);
		int count = trxorderDetailService.count(new QueryWrapper<>(trxorderDetail));
		//没有获取此课程
		if(count == 0){
			return R.data(false);
		}
		//判断用户是否已评论过此课程，如果评论过则不能评论
		Comment comment = new Comment();
		comment.setUserId(userId);
		comment.setOtherId(courseId);
		//查看课程评论
		comment.setType(2);
		count = commentService.count(new QueryWrapper<>(comment));
		return R.data(count == 0);
	}
}
