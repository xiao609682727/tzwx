
package org.springcrazy.modules.coursecard.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.coursecard.entity.Card;
import org.springcrazy.modules.coursecard.entity.CardCode;
import org.springcrazy.modules.coursecard.entity.CardCourse;
import org.springcrazy.modules.coursecard.mapper.CardMapper;
import org.springcrazy.modules.coursecard.service.ICardCodeService;
import org.springcrazy.modules.coursecard.service.ICardCourseService;
import org.springcrazy.modules.coursecard.service.ICardService;
import org.springcrazy.modules.coursecard.vo.CardCourseVO;
import org.springcrazy.modules.coursecard.vo.CardVO;
import org.springcrazy.modules.edu.entity.Course;
import org.springcrazy.modules.edu.entity.Orders;
import org.springcrazy.modules.edu.service.ICourseService;
import org.springcrazy.modules.user.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 课程卡主表 服务实现类
 *
 * @author TongZhou
 * @since 2021-03-31
 */
@Service
public class CardServiceImpl extends ServiceImpl<CardMapper, Card> implements ICardService {
	@Autowired
	private ICardCourseService cardCourseService;
	@Autowired
	private ICardCodeService cardCodeService;
	@Autowired
	private ICourseService courseService;

	@Override
	public IPage<CardVO> selectCardPage(IPage<CardVO> page, CardVO card) {
		return page.setRecords(baseMapper.selectCardPage(page, card));
	}
	/**
	 * 课程卡添加
	 */
	public void addCard(Card card,String courseIds){
		//课程id为空判断
		if(Func.isEmpty(courseIds)){
			return;
		}
		//字符串分割id
		String[] courseIdList = courseIds.split(",");
		//课程id数量如果为0则不操作
		if(courseIdList.length==0){
			return;
		}
		card.setCreateTime(LocalDateTime.now());
		save(card);
		for(String courseId:courseIdList){
			CardCourse cardCourse = new CardCourse();
			cardCourse.setCardId(card.getId());
			cardCourse.setCourseId(Integer.valueOf(courseId));
			cardCourse.setType("course");
			cardCourseService.save(cardCourse);
		}
		List<CardCode> list = new ArrayList<>();
		for(int i=0;i<card.getNum();i++){
			CardCode cardCode = new CardCode();
			cardCode.setCardId(card.getId());
			String ccode="1"+getFixString(Integer.valueOf(card.getId()+"").intValue(),3)+getFixString(i,3)+ RandomUtil.randomNumbers(4);
			cardCode.setCardCode(ccode);
			cardCode.setCardCodePassword(RandomUtil.randomNumbers(6));// 生成10位密码
			cardCode.setCreateTime(LocalDateTime.now());
			cardCode.setStatus("init");
			cardCode.setUserId(0);
			list.add(cardCode);
		}
		cardCodeService.saveBatch(list);
	}
	/**
	 * 长度补冲，前面加0
	 *
	 * @param num
	 * @param len
	 * @return String
	 */
	static String getFixString(int num, int len) {
		String tp = "" + num;
		if (len == 0) {
			return tp;
		}
		if (tp.length() == len) {
			return tp;
		}
		if (tp.length() > len) {
			return tp.substring(0, len);
		}
		for (int i = 0; i <= len / 4 + 1; i++) {
			tp = "00000" + tp;
		}
		return tp.substring(tp.length() - len);
	}
	/**
	 * 课程卡更新
	 */
	public void updateCard(Card card,String courseIds){
		//课程id为空判断
		if(Func.isEmpty(courseIds)){
			return;
		}
		//字符串分割id
		String[] courseIdList = courseIds.split(",");
		//课程id数量如果为0则不操作
		if(courseIdList.length==0){
			return;
		}
		card.setCreateTime(LocalDateTime.now());
		updateById(card);
		CardCourse cardCourse = new CardCourse();
		QueryWrapper<CardCourse> queryWrapper = Condition.getQueryWrapper(cardCourse);
		queryWrapper.lambda().eq(CardCourse::getCardId,card.getId());
		cardCourseService.remove(queryWrapper);
		for(String courseId:courseIdList){
			cardCourse = new CardCourse();
			cardCourse.setCardId(card.getId());
			cardCourse.setCourseId(Integer.valueOf(courseId));
			cardCourseService.save(cardCourse);

		}
	}

	/**
	 * 查看课程卡关联的课程信息
	 */
	public List<CardCourseVO> queryCourseCardCourseList(CardCourse cardCourse){
		List<CardCourseVO> cardCourseVOList = cardCourseService.queryCourseCardCourseList(cardCourse);
		return cardCourseVOList;
	}
	/**
	 * 课程卡激活方法
	 */
	public String queryCourseCardCourseActivation(CardCode cardCode){


		QueryWrapper<CardCode> queryWrapper = Condition.getQueryWrapper(new CardCode());
		queryWrapper.lambda().eq(CardCode::getCardCode,cardCode.getCardCode());
		queryWrapper.lambda().eq(CardCode::getCardCodePassword,cardCode.getCardCodePassword());
		//查询卡号和密码是否正确
		List<CardCode> cardCodeList = cardCodeService.list(queryWrapper);
		if(Func.notNull(cardCodeList)&&cardCodeList.size()>0){
			CardCode cardCode1 = cardCodeList.get(0);
			//卡已激活
			if("used".equals(cardCode1.getStatus())){
				return "cardUse";
			}
			//卡作废
			if("invalid".equals(cardCode1.getStatus())){
				return "cardInvalid";
			}

			Card card = getById(cardCode1.getCardId());
			// 此时此刻
			LocalDateTime now = LocalDateTime.now();
			//比较课程卡开始时间
			if(card.getBeginTime().compareTo(now)>0){
				return "cardUnBegin";
			}
			//比较课程卡结束时间
			if(card.getEndTime().compareTo(now)<0){
				return "cardEnd";
			}

			//查询课程卡相关联的课程
			QueryWrapper<CardCourse> cardCourseQueryWrapper = Condition.getQueryWrapper(new CardCourse());
			cardCourseQueryWrapper.lambda().eq(CardCourse::getCardId,cardCode1.getCardId());
			List<CardCourse> cardCourseList = cardCourseService.list(cardCourseQueryWrapper);
			String courseIds = "";
			for (CardCourse cardCourse:cardCourseList){
				courseIds+=cardCourse.getCourseId()+",";
			}
			cardCode1.setStatus("used");
			cardCode1.setUserId(cardCode.getUserId());
			cardCode1.setUseTime(LocalDateTime.now());

			Student student = new Student();
			student.setId(cardCode.getUserId());
			//获取要赠送的课程列表
			List<Course> courseList = courseService.listByIds(Func.toIntList(courseIds));
			Orders order = courseService.addCourseOrder(student,courseList, Orders.PAYTYPE_COURSECARD);
			cardCode1.setTrxorderId(order.getId());
			cardCode1.setRequestId(order.getOrderNo());
			cardCodeService.updateById(cardCode1);
			return "success";
		}else{
			return "cardError";
		}

	}
}
