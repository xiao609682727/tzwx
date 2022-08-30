
package org.springcrazy.modules.edu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springcrazy.core.boot.ctrl.CrazyController;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.mp.support.Query;
import org.springcrazy.core.secure.utils.SecureUtil;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.utils.DateUtil;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.core.tool.utils.ObjectUtil;
import org.springcrazy.modules.edu.entity.Course;
import org.springcrazy.modules.edu.entity.TrxorderDetail;
import org.springcrazy.modules.edu.service.ICourseService;
import org.springcrazy.modules.edu.service.ICourseStudyhistoryService;
import org.springcrazy.modules.edu.service.ITrxorderDetailService;
import org.springcrazy.modules.edu.vo.TrxorderDetailVO;
import org.springcrazy.modules.lineclass.entity.LineclassEnroll;
import org.springcrazy.modules.lineclass.service.ILineclassEnrollService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

/**
 * 流水表 控制器
 *
 * @author TongZhou
 * @since 2020-05-09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/edu/trxorderdetail")
@Api(value = "流水表", tags = "流水表接口")
public class TrxorderDetailController extends CrazyController {

	private ITrxorderDetailService trxorderDetailService;
	private ICourseStudyhistoryService courseStudyhistoryService;
	private ILineclassEnrollService lineclassEnrollService;
	private ICourseService courseService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入trxorderDetail")
	public R<TrxorderDetail> detail(TrxorderDetail trxorderDetail) {
		TrxorderDetail detail = trxorderDetailService.getOne(Condition.getQueryWrapper(trxorderDetail));
		return R.data(detail);
	}

	/**
	 * 分页 流水表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入trxorderDetail")
	public R<IPage<TrxorderDetail>> list(TrxorderDetail trxorderDetail, Query query) {
		Map<String, Object> param = Maps.newHashMap();
		param.put("courseName_like",trxorderDetail.getCourseName());
		param.put("trxorderId",trxorderDetail.getTrxorderId());
		if(ObjectUtil.isNotEmpty(trxorderDetail.getOrderNo())){
			param.put("orderNo",trxorderDetail.getOrderNo());
		}
		IPage<TrxorderDetail> pages = trxorderDetailService.page(Condition.getPage(query), Condition.getQueryWrapper(param,TrxorderDetail.class));
		if(pages.getRecords().size()!=0){
			if(pages.getRecords().get(0).getTrxorderType().equals("1")){
				pages.getRecords().get(0).setTrxorderType("COURSE");
			}
			if(pages.getRecords().get(0).getTrxorderType().equals("2")){
				pages.getRecords().get(0).setTrxorderType("LIVE");
			}
			if(pages.getRecords().get(0).getTrxorderType().equals("3")){
				pages.getRecords().get(0).setTrxorderType("PACKAGE");
			}
			if(pages.getRecords().get(0).getTrxorderType().equals("4")){
				pages.getRecords().get(0).setTrxorderType("LINECLASS");
			}
		}
		return R.data(pages);
	}
	/**
	 * 分页 流水表
	 */
	@GetMapping("/listForUser")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入trxorderDetail")
	public R<IPage<TrxorderDetailVO>> listForUser(TrxorderDetailVO trxorderDetail, Query query) {

		IPage<TrxorderDetailVO> pages = trxorderDetailService.selectTrxorderDetailPage(Condition.getPage(query), trxorderDetail);
		if(pages.getRecords().get(0).getTrxorderType().equals("1")){
			pages.getRecords().get(0).setTrxorderType("COURSE");
		}
		if(pages.getRecords().get(0).getTrxorderType().equals("2")){
			pages.getRecords().get(0).setTrxorderType("LIVE");
		}
		if(pages.getRecords().get(0).getTrxorderType().equals("3")){
			pages.getRecords().get(0).setTrxorderType("PACKAGE");
		}
		return R.data(pages);
	}

	/**
	 * 自定义分页 流水表  查询过滤过期的课程
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入trxorderDetail")
	public R<IPage<TrxorderDetailVO>> page(TrxorderDetailVO trxorderDetail, Query query) {
		//判断为学生 则进行学生id查询
		Integer userId = SecureUtil.getUserId();
		trxorderDetail.setUserId(userId);

		IPage<TrxorderDetailVO> pages = trxorderDetailService.selectTrxorderDetailPage(Condition.getPage(query), trxorderDetail);
		for(TrxorderDetailVO course:pages.getRecords()){
			//点播课程或者是直播
			if("COURSE".equals(course.getTrxorderType())||"LIVE".equals(course.getTrxorderType())||"1".equals(course.getTrxorderType())||"2".equals(course.getTrxorderType())){
				int y=courseStudyhistoryService.getstudykpoint(course.getCourseId());
				int v=courseStudyhistoryService.getstudystudyisok(userId,course.getCourseId(),0);
				course.setStudyKpoints(y);
				course.setStudyKpoint(v);
				if(v<=0){
					course.setStudyLearning("0.00%");
				}else {
					float num= (float)v*100/y;
					DecimalFormat df = new DecimalFormat("0.00");//格式化小数
					course.setStudyLearning(df.format(num)+"%");
				}
			}
			if("PACKAGE".equals(course.getTrxorderType())||"3".equals(course.getTrxorderType())){
				int y=courseStudyhistoryService.getPackageStudykpoint(course.getCourseId());
				int v=courseStudyhistoryService.getstudystudyisok(userId,0,course.getCourseId());
				course.setStudyKpoints(y);
				course.setStudyKpoint(v);
				if(v<=0){
					course.setStudyLearning("0.00%");
				}else {
					float num= (float)v*100/y;
					DecimalFormat df = new DecimalFormat("0.00");//格式化小数
					course.setStudyLearning(df.format(num)+"%");
				}
			}
			if("LINECLASS".equals(course.getTrxorderType())||"4".equals(course.getTrxorderType())){
				LineclassEnroll lineclassEnroll = new LineclassEnroll();
				lineclassEnroll.setTrxorderId(course.getTrxorderId());
				LineclassEnroll lineclassEnrollA=lineclassEnrollService.getOne(Condition.getQueryWrapper(lineclassEnroll));
				if(ObjectUtil.isNotEmpty(lineclassEnrollA)){
					Course course1 = courseService.getCourseById(course.getCourseId());
					Instant instant1=course1.getFaceTeachingTime().toInstant();
					course.setLineBagenTime(LocalDateTime.ofInstant(instant1, ZoneId.systemDefault()));
					Instant instant2=course1.getEndTime().toInstant();
					course.setLineEndTime(LocalDateTime.ofInstant(instant2, ZoneId.systemDefault()));
					if(lineclassEnrollA.getState()==2){
						course.setLineTeacherIs(1);
						course.setLineName(lineclassEnrollA.getUserName());
						course.setLineMobile(lineclassEnrollA.getMobile());
						course.setLineTime(lineclassEnrollA.getCreateTime());
					}else if(lineclassEnrollA.getState()==3){
						if(Func.isNotEmpty(lineclassEnrollA.getMobile())&&Func.isNotEmpty(lineclassEnrollA.getUserName())){
							course.setLineTeacherIs(1);
							course.setLineName(lineclassEnrollA.getUserName());
							course.setLineMobile(lineclassEnrollA.getMobile());
							course.setLineTime(lineclassEnrollA.getCreateTime());
						}else {
							course.setLineTeacherIs(2);
						}
					}else {
						course.setLineTeacherIs(2);
					}
				}else {
					course.setLineTeacherIs(2);
				}
				course.setIsCourseEnd(0);
			}
		}
		return R.data(pages);
	}


	/**
	 * 自定义分页 流水表 查询过期的面授课程
	 */
	@GetMapping("/pageLine")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "分页", notes = "传入trxorderDetail")
	public R<IPage<TrxorderDetailVO>> pageLine(TrxorderDetailVO trxorderDetail, Query query) {
		//判断为学生 则进行学生id查询
		Integer userId = SecureUtil.getUserId();
		trxorderDetail.setUserId(userId);
		trxorderDetail.setTrxorderType("");
		IPage<TrxorderDetailVO> pages = trxorderDetailService.getTrxorderDetailEndPage(Condition.getPage(query), trxorderDetail);

		for(TrxorderDetailVO course:pages.getRecords()){
			//点播课程或者是直播
			if("COURSE".equals(course.getTrxorderType())||"LIVE".equals(course.getTrxorderType())||"1".equals(course.getTrxorderType())||"2".equals(course.getTrxorderType())){
				int y=courseStudyhistoryService.getstudykpoint(course.getCourseId());
				int v=courseStudyhistoryService.getstudystudyisok(userId,course.getCourseId(),0);
				course.setStudyKpoints(y);
				course.setStudyKpoint(v);
				if(v<=0){
					course.setStudyLearning("0.00%");
				}else {
					float num= (float)v*100/y;
					DecimalFormat df = new DecimalFormat("0.00");//格式化小数
					course.setStudyLearning(df.format(num)+"%");
				}
			}
			if("PACKAGE".equals(course.getTrxorderType())||"3".equals(course.getTrxorderType())){
				int y=courseStudyhistoryService.getPackageStudykpoint(course.getCourseId());
				int v=courseStudyhistoryService.getstudystudyisok(userId,0,course.getCourseId());
				course.setStudyKpoints(y);
				course.setStudyKpoint(v);
				if(v<=0){
					course.setStudyLearning("0.00%");
				}else {
					float num= (float)v*100/y;
					DecimalFormat df = new DecimalFormat("0.00");//格式化小数
					course.setStudyLearning(df.format(num)+"%");
				}
			}
			if("LINECLASS".equals(course.getTrxorderType())||"4".equals(course.getTrxorderType())){
				LineclassEnroll lineclassEnroll = new LineclassEnroll();
				lineclassEnroll.setTrxorderId(course.getTrxorderId());
				LineclassEnroll lineclassEnrollA=lineclassEnrollService.getOne(Condition.getQueryWrapper(lineclassEnroll));
				if(ObjectUtil.isNotEmpty(lineclassEnrollA)){
					Course course1 = courseService.getCourseById(course.getCourseId());
					Instant instant1=course1.getFaceTeachingTime().toInstant();
					course.setLineBagenTime(LocalDateTime.ofInstant(instant1, ZoneId.systemDefault()));
					Instant instant2=course1.getEndTime().toInstant();
					course.setLineEndTime(LocalDateTime.ofInstant(instant2, ZoneId.systemDefault()));
					if(lineclassEnrollA.getState()==2){
						course.setLineTeacherIs(3);
						course.setLineName(lineclassEnrollA.getUserName());
						course.setLineMobile(lineclassEnrollA.getMobile());
						course.setLineTime(lineclassEnrollA.getCreateTime());
					}else if(lineclassEnrollA.getState()==3){
						if(Func.isNotEmpty(lineclassEnrollA.getMobile())&&Func.isNotEmpty(lineclassEnrollA.getUserName())){
							course.setLineTeacherIs(3);
							course.setLineName(lineclassEnrollA.getUserName());
							course.setLineMobile(lineclassEnrollA.getMobile());
							course.setLineTime(lineclassEnrollA.getCreateTime());
						}else {
							course.setLineTeacherIs(2);
						}
					}else {
						course.setLineTeacherIs(2);
					}
				}else {
					course.setLineTeacherIs(2);
				}
				course.setIsCourseEnd(1);

			}
		}
		return R.data(pages);
	}

	/**
	 * 新增 流水表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入trxorderDetail")
	public R save(@Valid @RequestBody TrxorderDetail trxorderDetail) {
		return R.status(trxorderDetailService.save(trxorderDetail));
	}

	/**
	 * 修改 流水表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入trxorderDetail")
	public R update(@Valid @RequestBody TrxorderDetail trxorderDetail) {
		return R.status(trxorderDetailService.updateById(trxorderDetail));
	}

	/**
	 * 新增或修改 流水表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入trxorderDetail")
	public R submit(@Valid @RequestBody TrxorderDetail trxorderDetail) {
		if("LINECLASS".equals(trxorderDetail.getTrxorderType())&&("4").equals(trxorderDetail.getAuthStatus())){
				lineclassEnrollService.delectLineClassEnrol(trxorderDetail.getUserId(),trxorderDetail.getCourseId());
		}
		return R.status(trxorderDetailService.saveOrUpdate(trxorderDetail));
	}


	/**
	 * 删除 流水表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(trxorderDetailService.removeByIds(Func.toIntList(ids)));
	}

	/**
	 * 查看学生是否拥有此课程
	 */
	@GetMapping("/checkCourse")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "查看学生是否拥有此课程", notes = "查看学生是否拥有此课程")
	public R checkCourse(TrxorderDetail trxorderDetail) {
		Integer userId = SecureUtil.getUserId();
		trxorderDetail.setUserId(userId);
		//验证课程id不能为空
		if(Func.isEmpty(trxorderDetail.getCourseId())){
			return R.data(false);
		}
		QueryWrapper<TrxorderDetail> trxorderDetailQueryWrapper = new QueryWrapper<>(trxorderDetail);
		trxorderDetailQueryWrapper.lambda().ge(TrxorderDetail::getAuthTime, DateUtil.now()).eq(TrxorderDetail::getAuthStatus,TrxorderDetail.STATUS_SUCCESS);
		int count = trxorderDetailService.count(trxorderDetailQueryWrapper);
		return R.data(count > 0);

	}

}
