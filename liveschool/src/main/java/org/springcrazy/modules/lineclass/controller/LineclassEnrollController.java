
package org.springcrazy.modules.lineclass.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.codec.Charsets;
import org.springcrazy.core.boot.ctrl.CrazyController;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.mp.support.Query;
import org.springcrazy.core.secure.utils.SecureUtil;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.core.tool.utils.ObjectUtil;
import org.springcrazy.modules.edu.entity.Course;
import org.springcrazy.modules.edu.entity.TrxorderDetail;
import org.springcrazy.modules.edu.service.ICourseService;
import org.springcrazy.modules.edu.service.ITrxorderDetailService;
import org.springcrazy.modules.lineclass.entity.LineclassEnroll;
import org.springcrazy.modules.lineclass.service.ILineclassEnrollService;
import org.springcrazy.modules.lineclass.vo.LineclassEnrollVO;
import org.springcrazy.modules.lineclass.wrapper.LineclassEnrollWrapper;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 线下课报名记录表 控制器
 *
 * @author TongZhou
 * @since 2021-05-27
 */
@RestController
@AllArgsConstructor
@RequestMapping("/lineclass/lineclassenroll")
@Api(value = "线下课报名记录表", tags = "线下课报名记录表接口")
public class LineclassEnrollController extends CrazyController {

	private ILineclassEnrollService lineclassEnrollService;
	private ITrxorderDetailService trxorderDetailService ;
	private ICourseService courseService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入lineclassEnroll")
	public R<LineclassEnrollVO> detail(LineclassEnroll lineclassEnroll) {
		LineclassEnroll detail = lineclassEnrollService.getOne(Condition.getQueryWrapper(lineclassEnroll));
		return R.data(LineclassEnrollWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 线下课报名记录表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入lineclassEnroll")
	public R<IPage<LineclassEnrollVO>> list(LineclassEnroll lineclassEnroll, Query query) {
		IPage<LineclassEnroll> pages = lineclassEnrollService.page(Condition.getPage(query), Condition.getQueryWrapper(lineclassEnroll));
		return R.data(LineclassEnrollWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 线下课报名记录表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入lineclassEnroll")
	public R<IPage<LineclassEnrollVO>> page(LineclassEnrollVO lineclassEnroll, Query query) {
		IPage<LineclassEnrollVO> pages = lineclassEnrollService.getCourseUserList(Condition.getPage(query), lineclassEnroll);
		return R.data(pages);
	}

	/**
	 * 新增 线下课报名记录表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入lineclassEnroll")
	public R save(@Valid @RequestBody LineclassEnroll lineclassEnroll) {
		return R.status(lineclassEnrollService.save(lineclassEnroll));
	}

	/**
	 * 新增 线下课报名记录表
	 */
	@GetMapping("/addSaveAndPay")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "新增", notes = "传入lineclassEnroll")
	public R addSaveAndPay( LineclassEnroll lineclassEnroll) {
		//购买状态  0为未购买或未支付。1为支付成功，不需要新加订单
		int returnstate=0;
		LineclassEnroll lineclassEnrollIs= new LineclassEnroll();
		lineclassEnrollIs.setUserId(SecureUtil.getUserId());
		lineclassEnrollIs.setCourseId(lineclassEnroll.getCourseId());
		//检测是否是后台赠送，如果是后台赠送则报名记录里没有姓名等信息。
		lineclassEnrollIs =lineclassEnrollService.getOne(Condition.getQueryWrapper(lineclassEnrollIs));
		if(ObjectUtil.isNotEmpty(lineclassEnrollIs)){
			if(Func.isNotEmpty(lineclassEnrollIs.getTrxorderId())){
				returnstate=2;
				lineclassEnrollIs.setState(2);
				lineclassEnrollIs.setCreateTime(LocalDateTime.now());
				lineclassEnrollIs.setUserName(lineclassEnroll.getUserName());
				lineclassEnrollIs.setMobile(lineclassEnroll.getMobile());
				lineclassEnrollService.saveOrUpdate(lineclassEnrollIs);
			}else {
				returnstate=1;
			}
		}else {
			lineclassEnroll.setUserId(SecureUtil.getUserId());
			lineclassEnroll.setCreateTime(LocalDateTime.now());
			lineclassEnrollService.saveOrUpdate(lineclassEnroll);
			//查询是否有订单。
			TrxorderDetail trxorderDetail = new TrxorderDetail();
			trxorderDetail.setUserId(lineclassEnroll.getUserId());
			trxorderDetail.setCourseId(lineclassEnroll.getCourseId());
			List<TrxorderDetail> detail = trxorderDetailService.list(Condition.getQueryWrapper(trxorderDetail));
			if(ObjectUtil.isNotEmpty(detail)){
				for(TrxorderDetail trxorderDetail1:detail){
					if(("2").equals(trxorderDetail1.getAuthStatus())){
						returnstate=2;
						lineclassEnroll.setState(2);
						lineclassEnroll.setTrxorderId(trxorderDetail1.getTrxorderId());
						lineclassEnrollService.saveOrUpdate(lineclassEnroll);
						break;
					}
				}
			}
		}
		return R.data(returnstate);
	}

	/**
	 * 详情
	 */
	@GetMapping("/getStatic")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "详情", notes = "传入lineclassEnroll")
	public R getStatic(LineclassEnroll lineclassEnroll) {
		lineclassEnroll.setUserId(SecureUtil.getUserId());
		LineclassEnroll detail = lineclassEnrollService.getOne(Condition.getQueryWrapper(lineclassEnroll));
		if(ObjectUtil.isNotEmpty(detail)&&ObjectUtil.isNotEmpty(detail.getState() )){
			return R.data(detail.getState());
		}else {
			return R.data(0);
		}
	}

	/**
	 * 修改 线下课报名记录表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入lineclassEnroll")
	public R update(@Valid @RequestBody LineclassEnroll lineclassEnroll) {
		return R.status(lineclassEnrollService.updateById(lineclassEnroll));
	}

	/**
	 * 新增或修改 线下课报名记录表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入lineclassEnroll")
	public R submit(@Valid @RequestBody LineclassEnroll lineclassEnroll) {
		return R.status(lineclassEnrollService.saveOrUpdate(lineclassEnroll));
	}


	/**
	 * 删除 线下课报名记录表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(lineclassEnrollService.removeByIds(Func.toIntList(ids)));
	}

	/**
	 * 导出面授报名记录
	 */
	@SneakyThrows
	@GetMapping("/exportLineCLassUser")
	@ApiOperationSupport(order = 13)
	@ApiOperation(value = "导出用户订单数据", notes = "传入orders")
	public void exportLineCLassUser(@ApiIgnore @RequestParam Map<String, Object> lineclassEnroll, HttpServletResponse response) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		String date=simpleDateFormat.format(new Date());
		lineclassEnroll.remove("crazy-auth");
		Course course = new Course();
		course.setId(Integer.valueOf(lineclassEnroll.get("courseId").toString()));
		course = courseService.getOne(Condition.getQueryWrapper(course));
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding(Charsets.UTF_8.name());
		String fileName = URLEncoder.encode("《"+course.getCourseName()+"》_用户报名数据", Charsets.UTF_8.name());
		response.setHeader("Content-disposition", "attachment;filename=" + (fileName+date) + ".xlsx");
		lineclassEnrollService.exportLineClassUser(response ,lineclassEnroll);
	}
}
