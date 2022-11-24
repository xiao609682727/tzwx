
package org.springcrazy.modules.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.api.client.util.Lists;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.core.tool.utils.ObjectUtil;
import org.springcrazy.modules.exam.entity.Exampaper;
import org.springcrazy.modules.exam.entity.PaperMiddle;
import org.springcrazy.modules.exam.entity.Qstmiddle;
import org.springcrazy.modules.exam.mapper.ExampaperMapper;
import org.springcrazy.modules.exam.service.IExampaperService;
import org.springcrazy.modules.exam.service.IPaperMiddleService;
import org.springcrazy.modules.exam.service.IQstmiddleService;
import org.springcrazy.modules.exam.vo.ExampaperVO;
import org.springcrazy.modules.exam.vo.QstmiddleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 考试试卷表 服务实现类
 *
 * @author TongZhou
 * @since 2020-12-01
 */
@Service
public class ExampaperServiceImpl extends ServiceImpl<ExampaperMapper, Exampaper> implements IExampaperService {

	@Autowired
	private IPaperMiddleService paperMiddleService;
	@Autowired
	private IQstmiddleService qstmiddleService;

	@Override
	public IPage<ExampaperVO> selectExampaperPage(IPage<ExampaperVO> page, ExampaperVO exampaper) {
		return page.setRecords(baseMapper.selectExampaperPage(page, exampaper));
	}

	@Override
	public void insertQuestion(List<PaperMiddle> examQuestionDTOList,String id) {
		Integer	 paperId = Integer.valueOf(id);

		Integer qstCount = 0;
		BigDecimal score = new BigDecimal(0);


		List<Integer> qstmiddleIds = Lists.newArrayList();
		List<Integer> paperMiddleIds = Lists.newArrayList();

		List<Qstmiddle> qstmiddleList = Lists.newArrayList();
		//循环大题 判断出要修改的与要添加的
		if(examQuestionDTOList.size() > 0){
			//重新修改题目数量
			for(PaperMiddle examQuestionDT:examQuestionDTOList){
				examQuestionDT.setNum(examQuestionDT.getQuestionArr().size());
			}
			paperMiddleService.saveOrUpdateBatch(examQuestionDTOList);
		}
		for (int i = 0; i < examQuestionDTOList.size(); i++) {
			PaperMiddle paperMiddle = examQuestionDTOList.get(i);
			paperMiddle.setSort(i+1);
			for (Qstmiddle qstmiddle : paperMiddle.getQuestionArr()) {
				qstmiddle.setPapermiddleId(paperMiddle.getId());
			}
			//计算试题数
			qstCount += paperMiddle.getQuestionArr().size();
			Double score1 = paperMiddle.getScore() * paperMiddle.getQuestionArr().size();
			score = score.add(new BigDecimal(score1));
			qstmiddleList.addAll(paperMiddle.getQuestionArr());
		}

		//循环小题 判断出要修改的与要添加的
		//小题进行排序
		for (int i = 0; i < qstmiddleList.size(); i++) {
			Qstmiddle paperMiddle = qstmiddleList.get(i);
			paperMiddle.setSort(i+1);
		}
		if(qstmiddleList.size() > 0){
			qstmiddleService.saveOrUpdateBatch(qstmiddleList);
		}
		paperMiddleIds = examQuestionDTOList.stream().map(e->e.getId()).collect(Collectors.toList());
		qstmiddleIds = qstmiddleList.stream().map(e->e.getId()).collect(Collectors.toList());
		// 查询出大题  判断要删除的大题
		if(paperMiddleIds.size() > 0){
			QueryWrapper<PaperMiddle> paperMiddleQueryWrapper = new QueryWrapper<>();
			paperMiddleQueryWrapper.lambda().eq(PaperMiddle::getPaperId,paperId).notIn(PaperMiddle::getId,paperMiddleIds);
			paperMiddleService.remove(paperMiddleQueryWrapper);
		}else{
			QueryWrapper<PaperMiddle> paperMiddleQueryWrapper = new QueryWrapper<>();
			paperMiddleQueryWrapper.lambda().eq(PaperMiddle::getPaperId,paperId);
			paperMiddleService.remove(paperMiddleQueryWrapper);
		}
		//查询出小题 判断要删除的小题
		if(qstmiddleIds.size() > 0){
			QueryWrapper<Qstmiddle> qstmiddleQueryWrapper = new QueryWrapper<Qstmiddle>();
			qstmiddleQueryWrapper.lambda().eq(Qstmiddle::getPaperId,paperId).notIn(Qstmiddle::getId,qstmiddleIds);
			qstmiddleService.remove(qstmiddleQueryWrapper);
		}else{
			QueryWrapper<Qstmiddle> qstmiddleQueryWrapper = new QueryWrapper<Qstmiddle>();
			qstmiddleQueryWrapper.lambda().eq(Qstmiddle::getPaperId,paperId);
			qstmiddleService.remove(qstmiddleQueryWrapper);
		}

		//更新试卷试题总数与试卷总分
		Exampaper exampaper = baseMapper.selectById(paperId);
		exampaper.setQstCount(qstCount);
		exampaper.setScore(score);
		baseMapper.updateById(exampaper);
	}

	@Override
	public List<PaperMiddle> queryQuestion(String paperId) {
		//查询试卷下所有大题
		QueryWrapper<PaperMiddle> paperMiddleQueryWrapper = new QueryWrapper<>();
		paperMiddleQueryWrapper.lambda().eq(PaperMiddle::getPaperId,paperId).orderByAsc(PaperMiddle::getSort);
		List<PaperMiddle> list1 = paperMiddleService.list(paperMiddleQueryWrapper);
		//查询试卷下所有小题
		QueryWrapper<Qstmiddle> qstmiddleQueryWrapper = new QueryWrapper<Qstmiddle>();
		qstmiddleQueryWrapper.lambda().eq(Qstmiddle::getPaperId,paperId);
		List<QstmiddleVO> list2 = qstmiddleService.selectQstmiddle(paperId);

		for (PaperMiddle paperMiddle : list1) {
			List<QstmiddleVO> questionArr = Lists.newArrayList();
			for (QstmiddleVO qstmiddle : list2) {
				if(Func.equals(qstmiddle.getPapermiddleId(),paperMiddle.getId())&& ObjectUtil.isNotEmpty(qstmiddle.getIsAsr())){
					questionArr.add(qstmiddle);
				}
			}
			paperMiddle.setQuestionArr(questionArr);
		}
		return list1;
	}
	@Override
	public Map<Integer,QstmiddleVO> queryQuestionReturnMap(Integer paperId){
		Map<Integer,QstmiddleVO> map = new HashMap<>();

		//查询试卷下所有小题
		QueryWrapper<Qstmiddle> qstmiddleQueryWrapper = new QueryWrapper<Qstmiddle>();
		qstmiddleQueryWrapper.lambda().eq(Qstmiddle::getPaperId,paperId);
		List<QstmiddleVO> list2 = qstmiddleService.selectQstmiddle(paperId+"");
		if(Func.isNotEmpty(list2)){
			for(QstmiddleVO qstmiddleVO:list2){
				map.put(qstmiddleVO.getQstId(),qstmiddleVO);
			}
		}
		return map;
	}


}
