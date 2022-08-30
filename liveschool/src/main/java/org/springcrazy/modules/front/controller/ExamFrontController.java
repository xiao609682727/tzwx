
package org.springcrazy.modules.front.controller;

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
import org.springcrazy.core.tool.api.ResultCode;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.edu.entity.Subject;
import org.springcrazy.modules.edu.service.ISubjectService;
import org.springcrazy.modules.exam.entity.*;
import org.springcrazy.modules.exam.service.*;
import org.springcrazy.modules.exam.vo.ErrorQuestionVO;
import org.springcrazy.modules.exam.vo.ExampaperRecordVO;
import org.springcrazy.modules.exam.vo.ExampaperVO;
import org.springcrazy.modules.exam.vo.FavoritesQstVO;
import org.springcrazy.modules.exam.wrapper.ExampaperRecordWrapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * banner图管理 控制器
 *
 * @author TongZhou
 * @since 2020-03-06
 */
@RestController
@AllArgsConstructor
@RequestMapping("/front/exam")
@Api(value = "前端考试接口", tags = "前端考试接口")
public class ExamFrontController extends CrazyController {

	private IExampaperRecordService exampaperRecordService;
	private IFavoritesQstService favoritesQstService;
	private IErrorQuestionService errorQuestionService;
	private IPaperTypeService paperTypeService;
	private IExampaperService exampaperService;
	private ISubjectService subjectService;
	private IExampaperRecordJsonService exampaperRecordJsonService;


	/**
	 * 分页 试卷类型表
	 *
	 */
	@GetMapping("/paperType/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "获取试卷类型列表", notes = "传入paperType")
	public R<List<PaperType>> list(PaperType paperType) {
		List<PaperType> List = paperTypeService.list(Condition.getQueryWrapper(paperType).lambda().eq(PaperType::getStatus,"normal").orderByDesc(PaperType::getSort));
		return R.data(List);
	}

	/**
	 * 查询考试记录
	 */
	@GetMapping("/exampaperRecord/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "查询考试记录", notes = "查询考试记录")
	public R<IPage<ExampaperRecordVO>> exampaperRecordList(ExampaperRecord exampaperRecord, Query query) {
		int userId=SecureUtil.getUserId();
		//判断如果没有用户信息则进行登录
		if(Func.equals(userId,-1)){
			return R.fail(ResultCode.UN_AUTHORIZED);
		}
		exampaperRecord.setUserId(userId);
		IPage<ExampaperRecord> pages = exampaperRecordService.page(Condition.getPage(query), Condition.getQueryWrapper(exampaperRecord).lambda().orderByDesc(ExampaperRecord::getAddTime));
		return R.data(ExampaperRecordWrapper.build().pageVO(pages));
	}

	/**
	 * 查询考试记录
	 */
	@GetMapping("/exampaperRecord/{id}")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "查询考试记录", notes = "查询考试记录")
	public R<ExampaperRecordVO> exampaperRecordList(@PathVariable String id) {

		ExampaperRecord exampaperRecord = exampaperRecordService.getById(id);
		ExampaperRecordVO exampaperRecordVO = BeanUtil.copy(exampaperRecord, ExampaperRecordVO.class);
		QueryWrapper<ExampaperRecordJson> exampaperRecordJsonQueryWrapper = new QueryWrapper<>();
		exampaperRecordJsonQueryWrapper.lambda().eq(ExampaperRecordJson::getExamRecordId,id);
		ExampaperRecordJson one = exampaperRecordJsonService.getOne(exampaperRecordJsonQueryWrapper);
		exampaperRecordVO.setAnalysisJson(one.getAnalysisJson());

		Subject subject = subjectService.getById(exampaperRecord.getSubjectId());
		PaperType paperType = paperTypeService.getById(exampaperRecord.getExamType());
		exampaperRecordVO.setSubjectName(subject.getSubjectName());
		exampaperRecordVO.setTypeName(paperType.getTitle());
		return R.data(exampaperRecordVO);
	}

	/**
	 * 新增或修改 考试试题收藏表
	 */
	@PostMapping("/favoritesqst/save")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "考试试题收藏新增或修改", notes = "传入favoritesQst")
	public R favoritesqstSave(@Valid @RequestBody FavoritesQst favoritesQst) {
		Integer userId = SecureUtil.getUserId();
		//判断如果没有用户信息则进行登录
		if(Func.equals(userId,-1)){
			return R.fail(ResultCode.UN_AUTHORIZED);
		}
		favoritesQst.setUserId(userId);
		favoritesQst.setAddDate(LocalDateTime.now());
		return R.status(favoritesQstService.saveOrUpdate(favoritesQst));
	}

	/**
	 * 删除 考试试题收藏表
	 */
	@PostMapping("/favoritesqst/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "试试题收藏删除", notes = "传入ids")
	public R favoritesqstRemove(@Valid @RequestBody FavoritesQst favoritesQst) {
		Integer userId = SecureUtil.getUserId();
		//判断如果没有用户信息则进行登录
		if(Func.equals(userId,-1)){
			return R.fail(ResultCode.UN_AUTHORIZED);
		}
		favoritesQst.setUserId(userId);
		QueryWrapper<FavoritesQst> favoritesQstQueryWrapper = new QueryWrapper<>(favoritesQst);
		return R.status(favoritesQstService.remove(favoritesQstQueryWrapper));
	}


	/**
	 * 删除 考试试题收藏表
	 */
	@PostMapping("/favoritesqst/removeById")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "试试题收藏删除", notes = "传入ids")
	public R favoritesqstRemove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(favoritesQstService.removeByIds(Func.toIntList(ids)));
	}

	/**
	 * 分页 试题收藏表
	 */
	@GetMapping("/favoritesqst/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "试题收藏", notes = "试题收藏")
	public R<IPage<FavoritesQstVO>> favoritesqstList(FavoritesQstVO favoritesQstVO, Query query) {
		Integer userId = SecureUtil.getUserId();
		//判断如果没有用户信息则进行登录
		if(Func.equals(userId,-1)){
			return R.fail(ResultCode.UN_AUTHORIZED);
		}
		favoritesQstVO.setUserId(userId);
		IPage<FavoritesQstVO> pages = favoritesQstService.selectFavoritesQstPage(Condition.getPage(query), favoritesQstVO);
		return R.data(pages);
	}


	/**
	 * 分页 试题收藏表
	 */
	@GetMapping("/favoritesqst/listAll")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "试题收藏", notes = "试题收藏")
	public R<List<FavoritesQstVO>> favoritesqstListAll(FavoritesQstVO favoritesQstVO) {
		Integer userId = SecureUtil.getUserId();
		//判断如果没有用户信息则进行登录
		if(Func.equals(userId,-1)){
			return R.fail(ResultCode.UN_AUTHORIZED);
		}
		favoritesQstVO.setUserId(userId);
		List<FavoritesQstVO> pages = favoritesQstService.selectFavoritesQstList(favoritesQstVO);
		return R.data(pages);
	}

	/**
	 * 分页 错题记录表
	 */
	@GetMapping("/errorquestion/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "错题记录", notes = "传入errorQuestion")
	public R<IPage<ErrorQuestionVO>> errorquestionList(ErrorQuestionVO errorQuestionVO, Query query, String favFlag) {
		Integer userId = SecureUtil.getUserId();
		//判断如果没有用户信息则进行登录
		if(Func.equals(userId,-1)){
			return R.fail(ResultCode.UN_AUTHORIZED);
		}
		errorQuestionVO.setUserId(userId);
		IPage<ErrorQuestionVO> pages = errorQuestionService.selectErrorQuestionPage(Condition.getPage(query), errorQuestionVO);
		if(Func.equals(favFlag,"1")){
			QueryWrapper<FavoritesQst> favoritesQstQueryWrapper = new QueryWrapper<FavoritesQst>();
			favoritesQstQueryWrapper.lambda().eq(FavoritesQst::getUserId,userId);
			List<FavoritesQst> list = favoritesQstService.list(favoritesQstQueryWrapper);
			Set<Integer> collect = list.stream().map(e -> e.getQstId()).collect(Collectors.toSet());
			List<ErrorQuestionVO> records = pages.getRecords();
			for (ErrorQuestionVO record : records) {
				if(collect.contains(record.getQstId())){
					record.setFavFlag("2");
				}else{
					record.setFavFlag("1");
				}
			}
		}
		return R.data(pages);
	}


	/**
	 * 分页 错题记录表
	 */
	@GetMapping("/errorquestion/listAll")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "错题记录", notes = "传入errorQuestion")
	public R<List<ErrorQuestionVO>> errorquestionListAll(ErrorQuestionVO errorQuestionVO) {
		Integer userId = SecureUtil.getUserId();
		//判断如果没有用户信息则进行登录
		if(Func.equals(userId,-1)){
			return R.fail(ResultCode.UN_AUTHORIZED);
		}
		errorQuestionVO.setUserId(userId);

		List<ErrorQuestionVO> pages = errorQuestionService.selectErrorQuestionList(errorQuestionVO);
		QueryWrapper<FavoritesQst> favoritesQstQueryWrapper = new QueryWrapper<FavoritesQst>();
		favoritesQstQueryWrapper.lambda().eq(FavoritesQst::getUserId,errorQuestionVO.getUserId());
		List<FavoritesQst> list = favoritesQstService.list(favoritesQstQueryWrapper);
		Set<Integer> collect = list.stream().map(e -> e.getQstId()).collect(Collectors.toSet());
		for (ErrorQuestionVO page : pages) {
			if(collect.contains(page.getQstId())){
				page.setFavFlag("2");
			}else{
				page.setFavFlag("1");
			}
		}
		return R.data(pages);
	}

	/**
	 * 删除 错题记录表
	 */
	@PostMapping("/errorquestion/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R errorquestionRemove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(errorQuestionService.removeByIds(Func.toIntList(ids)));
	}


	/**
	 * 获取试卷详情
	 */
	@GetMapping("/examPaper/{id}")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "获取试卷详情", notes = "获取试卷详情")
	public R<ExampaperVO> examPaperById(@PathVariable String id) {
		Exampaper exampaper = exampaperService.getById(id);
		ExampaperVO exampaperVO = BeanUtil.copy(exampaper, ExampaperVO.class);
		Subject subject = subjectService.getById(exampaper.getSubjectId());
		PaperType paperType = paperTypeService.getById(exampaper.getType());
		exampaperVO.setSubjectName(subject.getSubjectName());
		exampaperVO.setTypeName(paperType.getTitle());
		QueryWrapper<ExampaperRecord> exampaperRecordQueryWrapper = new QueryWrapper<ExampaperRecord>();
		exampaperRecordQueryWrapper.lambda().eq(ExampaperRecord::getEpId,id);
		exampaperRecordQueryWrapper.select("max(user_score) as max","min(user_score) as min","count(DISTINCT user_id) as num","avg(user_score) as avg");
		Map<String, Object> map = exampaperRecordService.getMap(exampaperRecordQueryWrapper);
		exampaperVO.setMaxScore(Func.toStr(map.get("max"),"0"));
		exampaperVO.setMinScore(Func.toStr(map.get("min"),"0"));
		exampaperVO.setActualJoinNum(Func.toInt(map.get("num"),0));
		exampaperVO.setAvgScore(new BigDecimal(Func.toStr(map.get("avg"),"0")));
		return R.data(exampaperVO);
	}

    /**
     * 获取试卷详情
     */
    @GetMapping("/examPaper/detail")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "获取试卷详情", notes = "获取试卷详情")
    public R<ExampaperVO> detail(String id) {
        Exampaper exampaper = exampaperService.getById(id);
        ExampaperVO exampaperVO = BeanUtil.copy(exampaper, ExampaperVO.class);
        List<PaperMiddle> paperMiddles = exampaperService.queryQuestion(id);
        exampaperVO.setList(paperMiddles);
		Subject subject = subjectService.getById(exampaper.getSubjectId());
		PaperType paperType = paperTypeService.getById(exampaper.getType());
		exampaperVO.setSubjectName(subject.getSubjectName());
		exampaperVO.setTypeName(paperType.getTitle());
        return R.data(exampaperVO);
    }
	/**
	 * 获取用户最新考试记录及部分统计数据
	 */
	@GetMapping("/examHistoy")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "获取用户最新考试记录", notes = "获取用户最新考试记录")
	public R<ExampaperRecordVO> examHistoyByUserId(String subjectId) {
		Integer userId = SecureUtil.getUserId();
//判断如果没有用户信息则进行登录
		if(Func.equals(userId,-1)){
			return R.fail(ResultCode.UN_AUTHORIZED);
		}
		Map<String,Object> param = Maps.newHashMap();
		param.put("cusId",userId);
		param.put("userId",userId);
		param.put("subjectId",subjectId);

		ExampaperRecord exampaperRecord = new ExampaperRecord();
		ExampaperRecordVO examhistory = new ExampaperRecordVO();
		exampaperRecord.setUserId(userId);
		ExampaperRecordVO exampaperRecordVO = exampaperRecordService.getExamPaper(exampaperRecord);
		if(exampaperRecordVO!=null){
			examhistory=exampaperRecordVO;
		}
		/*获取7天内用户所做的试卷数量*/
		examhistory.setPaperRecordNums(exampaperRecordService.getExamPaperNums(param));
		/*获取7天内用户所做的试题数量*/
		examhistory.setQuestionNums(exampaperRecordService.getQuestionNums(param));
		/*获取7天内用户所做的试题错误数量*/
		examhistory.setQuestionErrorNums(exampaperRecordService.getquestionErrorNums(param));
		/*获取7天内用户收藏的试题数量*/
		examhistory.setCollectionQuestionNums(favoritesQstService.getFavoriteNums(param));
		//获取答题图表数据
		Map<String, Object> quesData = exampaperRecordService.getQuesData(param);
		examhistory.setQuesData(quesData);
		return R.data(examhistory);
	}


	/**
	 * 交卷
	 */
	@PostMapping("/examRecord/save")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "交卷", notes = "交卷")
	public R examRecordSave(@Valid @RequestBody ExampaperVO exampaperVO) {
		ExampaperRecord exampaperRecord = exampaperRecordService.addExampaperRecordSave(exampaperVO);
		return R.data(exampaperRecord);
	}

}
