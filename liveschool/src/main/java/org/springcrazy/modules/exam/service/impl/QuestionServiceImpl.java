
package org.springcrazy.modules.exam.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springcrazy.core.log.exception.ServiceException;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.edu.entity.Subject;
import org.springcrazy.modules.edu.service.ISubjectService;
import org.springcrazy.modules.exam.entity.Question;
import org.springcrazy.modules.exam.mapper.QuestionMapper;
import org.springcrazy.modules.exam.service.IQuestionService;
import org.springcrazy.modules.exam.vo.QuestionVO;
import org.springcrazy.modules.system.excel.ExamExcel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 考试试题表 服务实现类
 *
 * @author TongZhou
 * @since 2020-12-02
 */
@Service
@AllArgsConstructor
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {


	private ISubjectService subjectService;

	@Override
	public IPage<QuestionVO> selectQuestionPage(IPage<QuestionVO> page, QuestionVO question) {
		return page.setRecords(baseMapper.selectQuestionPage(page, question));
	}

	@Override
	public void importExam(List<ExamExcel> data) {

		int i = 1 ; //行数

		List<Question> questionsList = new ArrayList<>();
		for (ExamExcel examExcel : data) {

			Question question = new Question();
			String tableName="";
			if(examExcel.getQstType()==1){
				 tableName="单选题";
				question.setQstType(examExcel.getQstType());
				//选择
				if(Func.isNotEmpty(examExcel.getOptionA())||Func.isNotEmpty(examExcel.getOptionB())){
					List<JSONObject> optionList = getOption(examExcel);
					question.setOptionList(optionList.toString());
				}else {
					throw new ServiceException(tableName+"第" + i + "行，选项A或选项B为空");
				}
				//答案
				if(Func.isNotEmpty(examExcel.getIsAsr())){
					String regex = "^[A-H]+$";
					if(examExcel.getIsAsr().length()>1){
						throw new ServiceException(tableName+"第" + i + "行，答案填写有误");
					}else if(!examExcel.getIsAsr().matches(regex)){
						throw new ServiceException(tableName+"第" + i + "行，答案填写有误，不是大写英文字母买，或超出选项范围");
					}
					question.setIsAsr(examExcel.getIsAsr());
				} else {
					throw new ServiceException(tableName+"第" + i + "行，答案为空;");
				}
			}else if(examExcel.getQstType()==2){
				 tableName="多选题";
				question.setQstType(examExcel.getQstType());
				//答案
				if(Func.isNotEmpty(examExcel.getIsAsr())){
					if (examExcel.getIsAsr().contains("，")){
						throw new ServiceException(tableName+"第" + i + "行，答案间隔请使用英文\",\"<br />");
					}
					String[] cours = examExcel.getIsAsr().split(",");
					for (int  courNum= 0 ; courNum < cours.length ; courNum++){
						String regex = "^[A-H]+$";
						if(!cours[courNum].matches(regex)){
							throw new ServiceException(tableName+"第" + i + "行,答案填写有误，不是大写英文字母买，或超出选项范围\"");
						}
						if(cours[courNum].length()>1){
							throw new ServiceException(tableName+"第" + i + "行，答案间隔请使用英文\",\"<br />");
						}
					}
					question.setIsAsr(examExcel.getIsAsr());
				}else {
					throw new ServiceException(tableName+"第" + i + "行，答案为空;");
				}
				//选择
				if(Func.isNotEmpty(examExcel.getOptionA())||Func.isNotEmpty(examExcel.getOptionB())){
					List<JSONObject> optionList = getOption(examExcel);
					question.setOptionList(optionList.toString());
				}else {
					throw new ServiceException(tableName+"第" + i + "行，选项A或选项B为空");
				}
			}else if(examExcel.getQstType()==3){
				 tableName="判断题";
				question.setQstType(examExcel.getQstType());
				//答案
				if(Func.isNotEmpty(examExcel.getIsAsr())){
					if("正确".equals(examExcel.getIsAsr())){
						question.setIsAsr("A");
					}else if("错误".equals(examExcel.getIsAsr())){
						question.setIsAsr("B");
					}else {
						throw new ServiceException(tableName+"第" + i + "行，答案不是正确或错误。");
					}

				}else {
					throw new ServiceException(tableName+"第" + i + "行，答案为空;");
				}
			}else if(examExcel.getQstType()==4){
				 tableName="填空题";
				question.setQstType(examExcel.getQstType());
				if(Func.isNotEmpty(examExcel.getIsAsr1())){
					String isAsrString = getIsAsr(examExcel);
					question.setIsAsr(isAsrString);
				}else {
					throw new ServiceException(tableName+"第" + i + "行，答案1为空");
				}
			}
			//题干
			if(Func.isNotEmpty(examExcel.getQstContent())){
				Integer cnt = baseMapper.selectCount(Wrappers.<Question>query().lambda().eq(Question::getQstContent, examExcel.getQstContent()).eq(Question::getQstType, examExcel.getQstType()));
				if(cnt > 0){
					throw new ServiceException(tableName+"第" + i + "行，题干已存在;");
				}else {
					question.setQstContent(examExcel.getQstContent());
				}
			}else {
				throw new ServiceException(tableName+"第" + i + "行，题干为空;");
			}
			//解析
			if(Func.isNotEmpty(examExcel.getQstAnalyze())){
				question.setQstAnalyze(examExcel.getQstAnalyze());
			}
			//分类
			if(Func.isNotEmpty(examExcel.getSubjectName())){
				List<Subject> subjectVO =subjectService.getSubjectByNameList(examExcel.getSubjectName());
				if(subjectVO.size()!=0){
					question.setSubjectId(subjectVO.get(0).getId());
				}else {
					throw new ServiceException(tableName+"第" + i + "行，分类错误;");
				}
			}else {
				throw new ServiceException(tableName+"第" + i + "行，分类为空;");
			}
			//难度
			if(Func.isNotEmpty(examExcel.getLevelName())){
				if("简单".equals(examExcel.getLevelName())){
					question.setLevel(1);
				}else if("普通".equals(examExcel.getLevelName())){
					question.setLevel(2);
				}else if("困难".equals(examExcel.getLevelName())){
					question.setLevel(3);
				}else {
					throw new ServiceException(tableName+"第" + i + "行，难度写法不正确");
				}
			}else {
				throw new ServiceException(tableName+"第" + i + "行，难度为空");
			}
			//排序
			if(Func.isNotEmpty(examExcel.getSort())){
				if(examExcel.getSort().matches("^[0-9]*$")){
					question.setSort(Integer.valueOf(examExcel.getSort()));
				}else {
					throw new ServiceException(tableName+"第" + i + "行，排序不正确，不是数字");
				}
			}else {
				question.setSort(1);
			}
			if(Func.isNotEmpty(question.getQstContent())){
				i+=1;
				questionsList.add(question);
			}
		}
		this.submit(questionsList);
	}

	@Override
	public void submit(List<Question> questionList) {
		if(questionList!=null&&questionList.size()!=0){
			for(Question question:questionList){
					question.setUpdateTime(LocalDateTime.now());
					question.setAuthor("管理员");
					saveOrUpdate(question);
			}
		}
	}

	public String getIsAsr(ExamExcel examExcel){
		String isAsr ="";
		if(Func.isNotEmpty(examExcel.getIsAsr1())){
			isAsr+=examExcel.getIsAsr1();
		}
		if(Func.isNotEmpty(examExcel.getIsAsr2())){
			isAsr+=","+examExcel.getIsAsr2();
		}
		if(Func.isNotEmpty(examExcel.getIsAsr3())){
			isAsr+=","+examExcel.getIsAsr3();
		}
		if(Func.isNotEmpty(examExcel.getIsAsr4())){
			isAsr+=","+examExcel.getIsAsr4();
		}
		if(Func.isNotEmpty(examExcel.getIsAsr5())){
			isAsr+=","+examExcel.getIsAsr5();
		}
		if(Func.isNotEmpty(examExcel.getIsAsr6())){
			isAsr+=","+examExcel.getIsAsr6();
		}
		return isAsr;

	}

	public List<JSONObject> getOption(ExamExcel examExcel){
		List<JSONObject> OptionList =  new ArrayList<>();
		if(Func.isNotEmpty(examExcel.getOptionA())){
			Map<String,Object> map = new HashMap<>();
			map.put("value",examExcel.getOptionA());
			map.put("editor",false);
			OptionList.add(JSONUtil.parseObj(map));
		}
		if(Func.isNotEmpty(examExcel.getOptionB())){
			Map<String,Object> map = new HashMap<>();
			map.put("value",examExcel.getOptionB());
			map.put("editor",false);
			OptionList.add(JSONUtil.parseObj(map));
		}
		if(Func.isNotEmpty(examExcel.getOptionC())){
			Map<String,Object> map = new HashMap<>();
			map.put("value",examExcel.getOptionC());
			map.put("editor",false);
			OptionList.add(JSONUtil.parseObj(map));
		}
		if(Func.isNotEmpty(examExcel.getOptionD())){
			Map<String,Object> map = new HashMap<>();
			map.put("value",examExcel.getOptionD());
			map.put("editor",false);
			OptionList.add(JSONUtil.parseObj(map));
		}
		if(Func.isNotEmpty(examExcel.getOptionE())){
			Map<String,Object> map = new HashMap<>();
			map.put("value",examExcel.getOptionE());
			map.put("editor",false);
			OptionList.add(JSONUtil.parseObj(map));
		}
		if(Func.isNotEmpty(examExcel.getOptionF())){
			Map<String,Object> map = new HashMap<>();
			map.put("value",examExcel.getOptionF());
			map.put("editor",false);
			OptionList.add(JSONUtil.parseObj(map));
		}
		if(Func.isNotEmpty(examExcel.getOptionG())){
			Map<String,Object> map = new HashMap<>();
			map.put("value",examExcel.getOptionG());
			map.put("editor",false);
			OptionList.add(JSONUtil.parseObj(map));
		}
		if(Func.isNotEmpty(examExcel.getOptionH())){
			Map<String,Object> map = new HashMap<>();
			map.put("value",examExcel.getOptionH());
			map.put("editor",false);
			OptionList.add(JSONUtil.parseObj(map));
		}
		return OptionList;
	}

}
