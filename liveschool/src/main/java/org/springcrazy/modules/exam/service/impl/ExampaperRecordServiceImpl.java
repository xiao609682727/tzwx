
package org.springcrazy.modules.exam.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.api.client.util.Lists;
import com.google.api.client.util.Maps;
import org.springcrazy.core.mp.support.Query;
import org.springcrazy.core.tool.jackson.JsonUtil;
import org.springcrazy.core.tool.utils.DateUtil;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.exam.entity.*;
import org.springcrazy.modules.exam.mapper.ExampaperRecordMapper;
import org.springcrazy.modules.exam.service.*;
import org.springcrazy.modules.exam.vo.ExampaperRecordVO;
import org.springcrazy.modules.exam.vo.ExampaperVO;
import org.springcrazy.modules.exam.vo.QstmiddleVO;
import org.springcrazy.modules.system.excel.ExportExampaperRecordExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 考试记录表 服务实现类
 *
 * @author TongZhou
 * @since 2020-12-02
 */
@Service
public class ExampaperRecordServiceImpl extends ServiceImpl<ExampaperRecordMapper, ExampaperRecord> implements IExampaperRecordService {

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	@Autowired
	IQuestionRecordService questionRecordService;

	@Autowired
	IExampaperRecordJsonService exampaperRecordJsonService;

	@Autowired
	IExampaperService exampaperService;

	@Autowired
	IErrorQuestionService errorQuestionService;


	@Override
	public IPage<ExampaperRecordVO> selectExampaperRecordPage(IPage<ExampaperRecordVO> page, ExampaperRecordVO exampaperRecord) {
		return page.setRecords(baseMapper.selectExampaperRecordPage(page, exampaperRecord));
	}

	@Override
	public ExampaperRecordVO getExamPaper(ExampaperRecord exampaperRecord) {
		return baseMapper.getExamPaper(exampaperRecord);
	}

	@Override
	public int getExamPaperNums(Map<String,Object> param) {
		return baseMapper.getExamPaperNums(param);
	}

	@Override
	public int getQuestionNums(Map<String,Object> param) {
		return baseMapper.getQuestionNums(param);
	}

	@Override
	public int getquestionErrorNums(Map<String,Object> param) {
		return baseMapper.getquestionErrorNums(param);
	}

	@Override
	public List<PaperMiddle> queryQuestionRecord(String paperId) {
		return null;
	}

	@Override
	public ExampaperRecord addExampaperRecordSave(ExampaperVO exampaperVO) {
		//创建试卷答题记录
		//正确题数
		Integer correctNum = 0;
		//已做题数
		Integer doneCount = 0;
		//用户分数
		BigDecimal userScore = new BigDecimal(0);
		//考试记录对象
		ExampaperRecord exampaperRecord = new ExampaperRecord();
		//用户id
		exampaperRecord.setUserId(exampaperVO.getUserId());
		//考试类型
		exampaperRecord.setExamType(exampaperVO.getType());
		//添加时间
		exampaperRecord.setAddTime(LocalDateTime.now());
		//正确题数
		exampaperRecord.setCorrectNum(correctNum);//
		//已做题数
		exampaperRecord.setDoneCount(doneCount);
		//用户分数
		exampaperRecord.setUserScore(userScore);
		//试卷id
		exampaperRecord.setEpId(exampaperVO.getId());
		//试卷名称
		exampaperRecord.setPaperName(exampaperVO.getName());
		//专业id
		exampaperRecord.setSubjectId(exampaperVO.getSubjectId());

		exampaperRecord.setType(1);
		//该用户考试所用时间单位是秒
		exampaperRecord.setTestTime(exampaperVO.getTestTime());
		//0为默认完成，1为未考完
		exampaperRecord.setStatus(0);
		//考试总分
		exampaperRecord.setQstScore(exampaperVO.getScore());
		//考试题数
		exampaperRecord.setQstCount(exampaperVO.getQstCount());
		save(exampaperRecord);

		//创建试卷试题答题记录
		List<QuestionRecord> questionRecordList = Lists.newArrayList();
		//获取试卷下的试题对象
		Map<Integer,QstmiddleVO> paperMiddlesMap = exampaperService.queryQuestionReturnMap(exampaperVO.getId());
		List<PaperMiddle> list = exampaperVO.getList();
		//计算考试分数，已做题数，正确题数
		for (PaperMiddle paperMiddle : list) {
			List<QstmiddleVO> questionArr = paperMiddle.getQuestionArr();
			for (QstmiddleVO qstmiddle : questionArr) {
				//把试题标题和选项放到list对象中
				if(Func.isNotEmpty(qstmiddle)){
					QstmiddleVO qstmiddleVO = paperMiddlesMap.get(qstmiddle.getQstId());
					qstmiddle.setQstContent(qstmiddleVO.getQstContent());
					qstmiddle.setOptionList(qstmiddleVO.getOptionList());
				}


				//判断是否答题
				Integer qusFlag = 1;//0 为正确 1为错误
				if(Func.isNotBlank(qstmiddle.getUseranswer())){
					//已做题数加1
					doneCount++;
					//作对了
					if(Func.equals(qstmiddle.getUseranswer(),qstmiddle.getIsAsr())){
						correctNum++;
						userScore = userScore.add(new BigDecimal(paperMiddle.getScore()));
						qusFlag = 0;
					}
				}
				//创建答题记录
				QuestionRecord questionRecord = new QuestionRecord();
				questionRecord.setAddtime(LocalDateTime.now());
				questionRecord.setPaperRecordId(exampaperRecord.getId());
				questionRecord.setPapermiddleId(paperMiddle.getId());
				questionRecord.setQstType(qstmiddle.getQstType());
				questionRecord.setCusId(exampaperVO.getUserId());
				questionRecord.setScore(new BigDecimal(paperMiddle.getScore()));
				questionRecord.setPaperId(exampaperVO.getId());
				questionRecord.setQstId(qstmiddle.getQstId());
				questionRecord.setStatus(qusFlag);
				questionRecord.setUseranswer(qstmiddle.getUseranswer());
				questionRecord.setState(0);
				questionRecordList.add(questionRecord);
			}
		}
		if(questionRecordList.size() > 0){
			//处理错题
			handleErrorQues(questionRecordList,exampaperVO.getUserId());
			questionRecordService.saveBatch(questionRecordList);
		}

		//创建试卷答题json
		ExampaperRecordJson exampaperRecordJson = new ExampaperRecordJson();
		exampaperRecordJson.setExamRecordId(exampaperRecord.getId());
		exampaperRecordJson.setAnalysisJson(JsonUtil.toJson(exampaperVO));
		exampaperRecordJsonService.save(exampaperRecordJson);

		//更新exampaperRecord数据
		exampaperRecord.setCorrectNum(correctNum);//
		exampaperRecord.setDoneCount(doneCount);
		exampaperRecord.setUserScore(userScore);

		//计算正确率
		exampaperRecord.setAccuracy(0f);
		if(exampaperVO.getQstCount() > 0 ){
			BigDecimal correctNumDecimal = new BigDecimal(correctNum);
			BigDecimal qstCountDecimal = new BigDecimal(exampaperVO.getQstCount());
			BigDecimal accuracy = correctNumDecimal.divide(qstCountDecimal, 2, BigDecimal.ROUND_HALF_UP);
			exampaperRecord.setAccuracy(accuracy.floatValue()*100);//正确率
		}
		updateById(exampaperRecord);
		//更新试卷信息
//		Exampaper exampaper = exampaperService.getById(exampaperVO.getId());
//		exampaper.setJoinNum();
//		exampaper.set
		return exampaperRecord;
	}

	@Override
	public Map<String, Object> getQuesData(Map<String,Object> param) {
		Map<String,Object> result = Maps.newHashMap();
		List<Map<String, Object>> quesData = baseMapper.getQuesData(param);
		List<String> days = Lists.newArrayList();
		//答题数据数组
		List<String> data1 = Lists.newArrayList();
		//错题数据数组
		List<String> data2 = Lists.newArrayList();
		for (int i = 8; i > 0; i--) {
			Date date = DateUtil.minusDays(DateUtil.now(), i);
			String day = DateUtil.formatDate(date);
			days.add(day);
			List<Map<String, Object>> dayList = quesData.stream().filter(e -> Func.equals(e.get("day"), day)).collect(Collectors.toList());
			if(dayList.size() > 0 ){
				data1.add(Func.toStr(dayList.get(0).get("count"),"0"));
				data2.add(Func.toStr(dayList.get(0).get("errorCount"),"0"));
			}else{
				data1.add("0");
				data2.add("0");
			}
		}
		result.put("days",days);
		result.put("data1",data1);
		result.put("data2",data2);
		return result;
	}

	/**
	 * 处理错题
	 * @param questionRecordList
	 */
	public void handleErrorQues(List<QuestionRecord> questionRecordList,Integer userId){
		taskExecutor.execute(()->{
			List<ErrorQuestion> errorQuestiondList = Lists.newArrayList();
			QueryWrapper<ErrorQuestion> errorQuestionQueryWrapper = new QueryWrapper<>();
			errorQuestionQueryWrapper.lambda().eq(ErrorQuestion::getUserId,userId);
			List<ErrorQuestion> list = errorQuestionService.list(errorQuestionQueryWrapper);

			for (QuestionRecord questionRecord : questionRecordList) {
				List<ErrorQuestion> collect = list.stream().filter(e -> Func.equals(e.getQstId(), questionRecord.getQstId())).collect(Collectors.toList());
				//未创建错题
				if(collect.size() == 0){
					//创建错题
					if(questionRecord.getStatus() == 1){
						ErrorQuestion errorQuestion = new ErrorQuestion();
						errorQuestion.setAddtime(LocalDateTime.now());
						errorQuestion.setUserId(userId);
						errorQuestion.setPaperRecordId(questionRecord.getPaperRecordId());
						errorQuestion.setPaperId(questionRecord.getPaperId());
						errorQuestion.setQstId(questionRecord.getQstId());
						errorQuestion.setCount(0);
						errorQuestion.setErrorCount(0);
						errorQuestiondList.add(errorQuestion);
					}
				}else{
					//已创建错题
					for (ErrorQuestion errorQuestion : collect) {
						if(questionRecord.getStatus() == 1){
							Integer errorCount = errorQuestion.getErrorCount() == null?0:errorQuestion.getErrorCount();
							errorQuestion.setErrorCount(errorCount+1);
						}
						Integer count = errorQuestion.getCount() == null?0:errorQuestion.getCount();
						errorQuestion.setCount(count+1);
						errorQuestiondList.add(errorQuestion);
					}
				}
			}
			errorQuestionService.saveOrUpdateBatch(errorQuestiondList);
		});

	}

	@Override
	public void exportExampaperRecord(HttpServletResponse response , ExampaperRecordVO exampaperRecord) {
		ExcelWriter excelWriter = null;
		try {
			Integer size = 3000;

			excelWriter = EasyExcel.write(response.getOutputStream(), ExportExampaperRecordExcel.class).build();
			WriteSheet writeSheet = EasyExcel.writerSheet("考试记录数据表").build();

			Query query = new Query();
			Page<ExportExampaperRecordExcel> page = new Page<>();
			List<ExportExampaperRecordExcel> list = baseMapper.exportExampaperRecord(page, exampaperRecord);
			for (int i = 1; i <= (page.getTotal()/size)+1; i++) {
				page.setCurrent(i);
				page.setSize(size);
				list = baseMapper.exportExampaperRecord(page, exampaperRecord);
				excelWriter.write(list, writeSheet);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (excelWriter != null) {
				excelWriter.finish();
			}
		}
	}
}
