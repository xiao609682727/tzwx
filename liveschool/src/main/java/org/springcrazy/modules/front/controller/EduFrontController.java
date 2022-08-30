
package org.springcrazy.modules.front.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.google.api.client.util.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springcrazy.common.tool.ProfileConfig;
import org.springcrazy.core.boot.ctrl.CrazyController;
import org.springcrazy.core.log.exception.ServiceException;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.mp.support.Query;
import org.springcrazy.core.secure.utils.SecureUtil;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.api.ResultCode;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.core.tool.utils.RedisUtil;
import org.springcrazy.modules.cms.entity.*;
import org.springcrazy.modules.cms.service.*;
import org.springcrazy.modules.cms.vo.ArticleVO;
import org.springcrazy.modules.cms.vo.CommentVO;
import org.springcrazy.modules.cms.wrapper.ArticleWrapper;
import org.springcrazy.modules.edu.entity.*;
import org.springcrazy.modules.edu.service.*;
import org.springcrazy.modules.edu.vo.*;
import org.springcrazy.modules.edu.wrapper.CourseKpointWrapper;
import org.springcrazy.modules.edu.wrapper.CourseWrapper;
import org.springcrazy.modules.exam.entity.Exampaper;
import org.springcrazy.modules.exam.service.IExampaperService;
import org.springcrazy.modules.user.entity.Teacher;
import org.springcrazy.modules.user.service.ITeacherService;
import org.springcrazy.modules.user.vo.TeacherVO;
import org.springcrazy.modules.web.entity.HelpMenu;
import org.springcrazy.modules.web.entity.WebsiteNavigate;
import org.springcrazy.modules.web.service.IHelpMenuService;
import org.springcrazy.modules.web.service.IWebsiteNavigateService;
import org.springcrazy.modules.web.service.IWebsiteRecommendCategoryService;
import org.springcrazy.modules.web.vo.HelpMenuVO;
import org.springcrazy.modules.web.vo.WebsiteRecommendCategoryVO;
import org.springcrazy.modules.web.wrapper.HelpMenuWrapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * banner图管理 控制器
 *
 * @author TongZhou
 * @since 2020-03-06
 */
@RestController
@AllArgsConstructor
@RequestMapping("/front")
@Api(value = "前端未登录时数据接口", tags = "前端未登录时数据接口")
public class EduFrontController extends CrazyController {

	private IWebsiteImagesService websiteImagesService;
	private IWebsiteRecommendCategoryService websiteRecommendCategoryService;
	private IHelpMenuService helpMenuService;
	private IWebsiteNavigateService websiteNavigateService;
	private ICourseService courseService;
	private ISubjectService subjectService;
	private IWebsiteProfileService websiteProfileService;
	private ICmsSubjectService cmsSubjectService;
	private IArticleService articleService;
	private ICourseKpointService courseKpointService;
	private ITeacherService teacherService;
	private ThreadPoolTaskExecutor taskExecutor;
	private ICoursePackageService coursePackageService;
	private ICommentService commentService;
	private ITrxorderDetailService trxorderDetailService;
	private ICourseMaterialService courseMaterialService;
	private ICourseStudyhistoryService courseStudyhistoryService;
	private ILivePlaybackService livePlaybackService;
	private ILiveRoomService liveRoomService;
	private IExampaperService exampaperService;

	/**
	 * 分页 banner图管理
	 */
	@GetMapping("/websiteImages/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "查询banner图列表", notes = "传入websiteImages")
	@ApiImplicitParams({
			@ApiImplicitParam(name="typeId",value="广告类型1 首页banner",dataType="int", paramType = "query")})
	public R<List<WebsiteImages>> WebsiteImageList(Integer typeId) {
		List<WebsiteImages> list = websiteImagesService.list(new QueryWrapper<WebsiteImages>().lambda().eq(WebsiteImages::getTypeId,typeId)
				.orderByDesc(WebsiteImages::getSeriesNumber).last("limit 3"));
		return R.data(list);
	}

	/**
	 * 查询首页推荐
	 */
	@GetMapping("/recommendCategory/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "查询首页推荐", notes = "传入frontType ")
	@ApiImplicitParams({
			@ApiImplicitParam(name="frontType",value="推荐类型1首页推荐",dataType="int", paramType = "query")})
	public R<List<WebsiteRecommendCategoryVO>> RecommendCategoryList(Integer frontType) {
		List<WebsiteRecommendCategoryVO> list = websiteRecommendCategoryService.getRecommendCategoryList(frontType);
		return R.data(list);
	}

	/**
	 * 查询帮助中心
	 */
	@GetMapping("/helpMenu/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "查询帮助中心", notes = "查询帮助中心 ")
	@ApiImplicitParams({
			@ApiImplicitParam(name="parentId",value="父级id 顶层为0",dataType="int", paramType = "query")})
	public R<List<HelpMenuVO>> helpMenu(Integer parentId) {
		HelpMenu helpMenu = new HelpMenu();
		helpMenu.setParentId(parentId);
		List<HelpMenu> list = helpMenuService.list(new QueryWrapper<HelpMenu>(helpMenu).lambda().orderByDesc(HelpMenu::getSort));
		return R.data(HelpMenuWrapper.build().listNodeVO(list));
	}

	/**
	 * 查询尾部链接与 首部导航条 尾部标签
	 */
	@GetMapping("/websiteNavigate/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "查询尾部链接与 首部导航条 尾部标签", notes = "查询尾部链接与 首部导航条 尾部标签 ")
	@ApiImplicitParams({
			@ApiImplicitParam(name="type",value="1首页、3尾部友链、4尾部标签",dataType="int", paramType = "query")})
	public R<List<WebsiteNavigate>> websiteNavigate(String type) {
		WebsiteNavigate websiteNavigate = new WebsiteNavigate();
		websiteNavigate.setType(type);
		websiteNavigate.setStatus("2");
		List<WebsiteNavigate> list = websiteNavigateService.list(new QueryWrapper<WebsiteNavigate>(websiteNavigate).lambda().orderByDesc(WebsiteNavigate::getSort));
		return R.data(list);
	}

	/**
	 * 查询课程
	 */
	@GetMapping("/course/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "查询课程列表", notes = "查询课程 ")
	@ApiImplicitParams({
			@ApiImplicitParam(name="sellType",value="课程类型",dataType="string", paramType = "query"),
			@ApiImplicitParam(name="subjectId",value="专业id",dataType="int", paramType = "query"),
			@ApiImplicitParam(name="courseName",value="课程名称",dataType="int", paramType = "query"),
			@ApiImplicitParam(name="level",value="专业树层级 1或2",dataType="string", paramType = "query"),
			@ApiImplicitParam(name="teacherId",value="教师id",dataType="int", paramType = "query"),
			@ApiImplicitParam(name="orderBy",value="默认（default），最新（update），免费(free)",dataType="string", paramType = "query"),

	})
	public R<IPage<CourseVO>> course(String sellType,String courseName,Integer subjectId,Integer teacherId,String level,String orderBy,Query query) {
		Course course = new Course();
		if(Func.isNotBlank(sellType)){
			course.setSellType(sellType);
		}
		List<Integer> idList = Lists.newArrayList();
		//当为第一层的时候查询所有第二层
		if(Func.equals("1",level)){
			Subject subject = new Subject();
			subject.setParentId(subjectId);
			List<Subject> list = subjectService.list(new QueryWrapper<>(subject));
			if(list.size() == 0){
				idList.add(subjectId);
			}else{
				idList = list.stream().map(t -> t.getId()).collect(Collectors.toList());
			}

		}
		if(Func.equals("2",level)){
			idList.add(subjectId);
		}
		QueryWrapper<Course> queryWrapper = new QueryWrapper<Course>(course)
				.in(idList.size()>0,"subject_id",idList);
		//默认排序
		if(ObjectUtil.isEmpty(orderBy) ||"default".equals(orderBy)){
			queryWrapper.orderByDesc("sort DESC,create_time");
		}
		//最新排序
		if("update".equals(orderBy)){
			queryWrapper.orderByDesc("create_time");
		}
		/*如果查全部的话，则不查面授课程。*/
		if(Func.isBlank(sellType)){
			queryWrapper.lambda().ne(Course::getSellType,4);
		}
		//只查询上架商品
		queryWrapper.lambda().eq(Course::getIsAvaliable,"1").like(Func.isNotBlank(courseName),Course::getCourseName,courseName);
		//查询教师id
		if(Func.isNotEmpty(teacherId)&&teacherId!=0){
			queryWrapper.lambda().eq(Course::getTeacherId,teacherId);
		}
		//免费课程
		if("free".equals(orderBy)){
			queryWrapper.orderByDesc("sort DESC,create_time");
			queryWrapper.lambda().eq(Course::getCurrentPrice,"0");
		}
		IPage<Course> pages = courseService.page(Condition.getPage(query), queryWrapper);
		//如果是直播的话整理课程列表，查询当前时间下一场直播
		IPage<CourseVO> courseVOPages = initLiveCourse(Condition.getPage(query),pages);
		return R.data(courseVOPages);
	}
	private RedisUtil redisUtil;
	/**
	 * 整理课程列表数据，查询直播当前时间的下一场直播
	 */
	public IPage<CourseVO> initLiveCourse(IPage<CourseVO> courseVOPages,IPage<Course> coursePages){
		List<CourseVO> courseVOList = new ArrayList<>();
		if(Func.isEmpty(coursePages)){
			return courseVOPages;
		}
		if(Func.isEmpty(coursePages.getRecords())){
			return courseVOPages;
		}
		for(Course course:coursePages.getRecords()){
			CourseVO courseVO = CourseWrapper.build().entityVO(course);
			//类型为直播的课程特殊处理，查询当前时间下一场直播
			if("2".equals(courseVO.getSellType())){
				//增加缓存
				CourseKpoint courseKpoint = (CourseKpoint)redisUtil.get("frontCourseLiveCourseKpoint_"+course.getId());
				if(Func.isEmpty(courseKpoint)){
					courseKpoint = new CourseKpoint();
					courseKpoint.setCourseId(course.getId());
					courseKpoint = courseKpointService.selectLiveCourseKpointOrderByBeginTime(courseKpoint);
					redisUtil.set("frontCourseLiveCourseKpoint_"+course.getId(),courseKpoint,60);
				}
				courseVO.setNextLiveCourseKpoint(courseKpoint);
			}
			if("4".equals(courseVO.getSellType())){
				String faceTeachingSubjectAddress =(String) redisUtil.get("faceTeachingSubjectAddress_"+course.getId());
				if(Func.isEmpty(faceTeachingSubjectAddress)){
					Subject subject =subjectService.getSubjectBysubjectId(courseVO.getFaceTeachingSubject());
					faceTeachingSubjectAddress=subject.getSubjectName();
					redisUtil.set("faceTeachingSubjectAddress_"+course.getId(),faceTeachingSubjectAddress,60);
				}
				courseVO.setFaceTeachingSubjectAddress(faceTeachingSubjectAddress);
			}
			courseVOList.add(courseVO);
		}
		courseVOPages.setRecords(courseVOList);
		courseVOPages.setTotal(coursePages.getTotal());
		return courseVOPages;
	}

    /**
     * 查询面授课程
     */
    @GetMapping("/lineCourse/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "查询课程列表", notes = "查询课程 ")
    @ApiImplicitParams({
            @ApiImplicitParam(name="sellType",value="课程类型",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="subjectId",value="专业id",dataType="int", paramType = "query"),
            @ApiImplicitParam(name="courseName",value="课程名称",dataType="int", paramType = "query"),
            @ApiImplicitParam(name="level",value="专业树层级 1或2",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="teacherId",value="教师id",dataType="int", paramType = "query"),
            @ApiImplicitParam(name="orderBy",value="默认（default），最新（update），免费(free)",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="addressActive",value="地址id",dataType="int", paramType = "query"),

    })
    public R<IPage<CourseVO>> lineCourse(String sellType,String courseName,Integer subjectId,Integer teacherId,String level,String orderBy,int addressActive,Query query) {
        Course course = new Course();
        if(Func.isNotBlank(sellType)){
            course.setSellType(sellType);
        }
        List<Integer> idList = Lists.newArrayList();
        //当为第一层的时候查询所有第二层
        if(Func.equals("1",level)){
            Subject subject = new Subject();
            subject.setParentId(subjectId);
            List<Subject> list = subjectService.list(new QueryWrapper<>(subject));
            if(list.size() == 0){
                idList.add(subjectId);
            }else{
                idList = list.stream().map(t -> t.getId()).collect(Collectors.toList());
            }
        }
        if(Func.equals("2",level)){
            idList.add(subjectId);
        }
        QueryWrapper<Course> queryWrapper = new QueryWrapper<Course>(course)
                .in(idList.size()>0,"subject_id",idList);
        //默认排序
        if(ObjectUtil.isEmpty(orderBy) ||"default".equals(orderBy)){
            queryWrapper.orderByDesc("sort DESC,create_time");
        }
        //最新排序
        if("update".equals(orderBy)){
            queryWrapper.orderByDesc("create_time");
        }
        if(Func.isNotEmpty(addressActive)){
            if(addressActive!=0){
                queryWrapper.lambda().eq(Course::getFaceTeachingSubject,addressActive);
            }
        }
        //只查询上架商品
        queryWrapper.lambda().eq(Course::getIsAvaliable,"1").like(Func.isNotBlank(courseName),Course::getCourseName,courseName);
        //查询教师id
        if(Func.isNotEmpty(teacherId)&&teacherId!=0){
            queryWrapper.lambda().eq(Course::getTeacherId,teacherId);
        }
        //免费课程
        if("free".equals(orderBy)){
            queryWrapper.orderByDesc("sort DESC,create_time");
            queryWrapper.lambda().eq(Course::getCurrentPrice,"0");
        }
        IPage<Course> pages = courseService.page(Condition.getPage(query), queryWrapper);
        //如果是直播的话整理课程列表，查询当前时间下一场直播
        IPage<CourseVO> courseVOPages = initLiveCourse(Condition.getPage(query),pages);
        return R.data(courseVOPages);
    }
	/**
	 * 查询试卷
	 */
	@GetMapping("/exam/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "查询试卷列表", notes = "查询试卷列表 ")
	@ApiImplicitParams({
			@ApiImplicitParam(name="sellType",value="课程类型",dataType="string", paramType = "query"),
			@ApiImplicitParam(name="subjectId",value="专业id",dataType="int", paramType = "query"),
			@ApiImplicitParam(name="examName",value="课程名称",dataType="int", paramType = "query"),
			@ApiImplicitParam(name="level",value="专业树层级 1或2",dataType="string", paramType = "query"),
			@ApiImplicitParam(name="orderBy",value="默认（default），最新（update），免费(free)",dataType="string", paramType = "query")
	})
	public R<IPage<Exampaper>> exam(String sellType,String examName,Integer subjectId,String level,String orderBy,Query query) {
		Exampaper exam= new Exampaper();
		if(Func.isNotBlank(sellType)){
			exam.setType(Integer.valueOf(sellType));
		}
		List<Integer> idList = Lists.newArrayList();
		if(Func.equals("1",level)){
			Subject subject = new Subject();
			subject.setParentId(subjectId);
			List<Subject> list = subjectService.list(new QueryWrapper<>(subject));
			if(list.size() == 0){
				idList.add(subjectId);
			}else{
				idList = list.stream().map(t -> t.getId()).collect(Collectors.toList());
			}

		}
		if(Func.equals("2",level)){
			idList.add(subjectId);
		}
		//查询正常状态的考试
		exam.setStatus("normal");
		QueryWrapper<Exampaper> queryWrapper = new QueryWrapper<Exampaper>(exam)
				.in(idList.size()>0,"subjectId",idList);
		if(ObjectUtil.isEmpty(orderBy) ||"default".equals(orderBy)){
			queryWrapper.orderByDesc("sortNum desc ,addTime");
		}
		if("update".equals(orderBy)){
			queryWrapper.orderByDesc("addTime");
		}
		//只查询上架商品
		queryWrapper.lambda().eq(Exampaper::getStatus,"normal").like(Func.isNotBlank(examName),Exampaper::getName,examName);
		IPage<Exampaper> pages = exampaperService.page(Condition.getPage(query), queryWrapper);
		return R.data(pages);
	}

	/**
	 * 查询课程详情
	 */
	@GetMapping("/course/{id}")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "查询课程详情", notes = "课程详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name="id",value="课程id",dataType="string", paramType = "query"),
			@ApiImplicitParam(name="courseIds",value="套餐课程id",dataType="string", paramType = "query")
	})
	public R<CourseVO> course(@PathVariable Integer id,String courseIds) {
		Course course = courseService.getById(id);
		if(Func.isNull(course)) {
			throw new ServiceException("暂无课程");
		}
		CourseVO courseVO = CourseWrapper.build().entityVO(course);
		courseVO.setAuthTime("");
		//获取章节目录
		CourseKpoint courseKpoint = new CourseKpoint();
		courseKpoint.setCourseId(id);
		//类型为3是套餐类型
		if(Func.equals(course.getSellType(),"3")){
			CoursePackageVO coursePackage = new CoursePackageVO();
			coursePackage.setParentCourseId(course.getId());
			List<CoursePackageVO> coursePackageVOS = coursePackageService.selectCoursePackageList(coursePackage);
			courseVO.setList(coursePackageVOS);
		}else if(Func.equals(course.getSellType(),"1")||Func.equals(course.getSellType(),"2")){
			//当课程为直播和录播时获取章节信息
			List<CourseKpoint> list = courseKpointService.list(Condition.getQueryWrapper(courseKpoint).lambda().orderByDesc(CourseKpoint::getSort));
			//整理章节中的点播时间格式化显示
			List<CourseKpointVO> courseKpointVOList = new ArrayList<CourseKpointVO>();
			for(CourseKpoint kpoint:list){
				CourseStudyhistory courseStudyhistory = new CourseStudyhistory();
				CourseKpointVO courseKpointVO = CourseKpointWrapper.build().entityVO(kpoint);
				if(kpoint.getParentId()!=0){
					if(Func.isNumeric(courseKpointVO.getVideoTime())){
						courseKpointVO.setVideoTimeFmt(secToTime(Integer.valueOf(courseKpointVO.getVideoTime())));
						courseKpointVO.setStudyLearning("0%");
					}
				}
				courseKpointVOList.add(courseKpointVO);
			}
			//判断当前用户是否是登录状态
			if(SecureUtil.getUserId()!=-1){
				int userId=SecureUtil.getUserId();
				//判断如果没有用户信息则进行登录
				if(Func.equals(userId,-1)){
					return R.fail(ResultCode.UN_AUTHORIZED);
				}
				for(CourseKpointVO kpoint:courseKpointVOList){
					CourseStudyhistory courseStudyhistory = new CourseStudyhistory();
					if(kpoint.getParentId()!=0){
						if(ObjectUtil.isNotNull(courseIds)){
							courseStudyhistory =courseStudyhistoryService.getOnesStudyHistory(userId,kpoint.getId(),2,Integer.valueOf(courseIds));
						}else {
							courseStudyhistory =courseStudyhistoryService.getOnesStudyHistory(userId,kpoint.getId(),1,0);
						}

						if(ObjectUtil.isNotNull(courseStudyhistory)){
							//是否完成了章节
							kpoint.setComplete(courseStudyhistory.getComplete());
							//如果学习进度为空则显示0
							if(Func.isEmpty(courseStudyhistory.getStudyLearning())){
								//学习进度
								kpoint.setStudyLearning("0%");
							}else{
								//学习进度
								kpoint.setStudyLearning(courseStudyhistory.getStudyLearning());
							}

						}else {
							kpoint.setComplete("0");
							kpoint.setStudyLearning("0%");
						}
					}
				}
//				courseVO.setList(CourseKpointWrapper.build().listNodeVO(list));
			}
			courseVO.setList(CourseKpointWrapper.build().listCourseKpointNodeVO(courseKpointVOList));
		}
		//获取科目信息
		Subject subSubject = subjectService.getById(course.getSubjectId());
		courseVO.setSubSubject(subSubject);
		if(!Func.isNull(subSubject)){
			Subject subject = subjectService.getById(subSubject.getParentId());
			courseVO.setSubject(subject);
		}
		if(Func.equals(course.getSellType(),"4")){
			courseVO.setFaceTeachingSubjectAddress(subjectService.getSubjectBysubjectId(course.getFaceTeachingSubject()).getSubjectName());
		}
		//获取教师信息
		Teacher teacher = teacherService.getById(course.getTeacherId());
		if(Func.isNotEmpty(teacher)){
			courseVO.setTeacher(teacher);
			Subject subject = subjectService.getSubjectBysubjectId(teacher.getSubjectId());
			courseVO.setTeacherSubjectName(subject!=null?subject.getSubjectName():"");
		}
		//处理浏览量
		taskExecutor.execute(()->{
			course.setPageViewcount(course.getPageViewcount()+1);
			courseService.updateById(course);
		});

		//设置用户id判断当前是否是登录状态
		courseVO.setUserId(SecureUtil.getUserId());
		return R.data(courseVO);
	}

	public static String secToTime(int time) {
		String timeStr = null;
		int minute = 0;
		int second = 0;
		if (time <= 0)
			return "00:00";
		else {
			minute = time / 60;
			second = time % 60;
			timeStr = unitFormat(minute) + ":" + unitFormat(second);
		}
		return timeStr;
	}
	private static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + i;
		else
			retStr = "" + i;
		return retStr;
	}
	/**
	 * 查询课程资料
	 */
	@GetMapping("/material/{id}")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "查询课程资料", notes = "课程资料")
	@ApiImplicitParams({
			@ApiImplicitParam(name="id",value="章节id",dataType="string", paramType = "query")})
	public R<List<CourseMaterial>> material(@PathVariable Integer id) {
		List<CourseMaterial> materials = new ArrayList<>();
		//判断是否为章节id
		CourseKpoint parent = courseKpointService.getById(id);
		//获取章节资料
		if (Func.isNotEmpty(parent)) {
			materials = courseMaterialService.getMaterialsBykpointId(id);
		}else {
			//查询课程是否被删除
			Course course = courseService.getById(id);
			if(Func.isNull(course)) {
				return R.data(materials);
			}else {
				//该课程下所有章节资料
				materials = courseMaterialService.getMaterialsByCourseId(id);
			}
		}
		return R.data(materials);
	}

	/**
	 * 获取菜单树形结构
	 */
	@GetMapping("/subject/tree")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "获取专业信息", notes = "树形结构")
	public R<List<SubjectVO>> subjectTree() {
		List<SubjectVO> tree = subjectService.tree();
		return R.data(tree);
	}

	/**
	 * 获取面授地域
	 */
	@GetMapping("/subject/addressTree")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "获取专业信息", notes = "树形结构")
	public R<List<SubjectVO>> addressTree() {
		List<SubjectVO> tree = subjectService.addressTree();
		return R.data(tree);
	}

	/**
	 * 获取菜单树形结构
	 */
	@GetMapping("/subjectExam/tree")
	 @ApiOperationSupport(order = 12)
	 @ApiOperation(value = "获取专业信息", notes = "树形结构")
	 public R<List<SubjectVO>> subjectExanTree() {
	 List<SubjectVO> tree = subjectService.examTree();
	 return R.data(tree);
	 }


	 /**
	 * 查询系统配置
	 */
	@GetMapping("/websiteProfile/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "查询系统配置", notes = "查询系统配置 ")
	@ApiImplicitParams({
			@ApiImplicitParam(name="configType",value="web站点配置、privacy隐私协议",dataType="string", paramType = "query")})
	public R<Map<String, String>> websiteProfile(String configType) {
		List<String> configTypeList = Func.toStrList(configType);

		//增加缓存
		List<WebsiteProfile> websiteProfileList = (List<WebsiteProfile>)redisUtil.get("websiteProfileList_"+configType);
		if(Func.isEmpty(websiteProfileList)){
			websiteProfileList = websiteProfileService.list(new QueryWrapper<WebsiteProfile>().lambda().in(configTypeList.size()>0,WebsiteProfile::getConfigType,configTypeList).orderByDesc(WebsiteProfile::getSort));
			redisUtil.set("websiteProfileList_"+configType,websiteProfileList,60);
		}

		//list转map
		Map<String, String> map = Maps.newHashMap();
		for (WebsiteProfile websiteProfile : websiteProfileList) {
			map.put(websiteProfile.getDataKey(),websiteProfile.getDataValue());
		}
		return R.data(map);
	}


	/**
	 * 查询资讯类型
	 */
	@GetMapping("/cmsSubject/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "查询资讯类型", notes = "查询资讯类型 ")
	@ApiImplicitParams({
			@ApiImplicitParam(name="type",value="article 网校资讯 ",dataType="string", paramType = "query")})
	public R<List<CmsSubject>> cmsSubject(String type) {
		CmsSubject cmsSubject = new CmsSubject();
		List<CmsSubject> list = cmsSubjectService.list(new QueryWrapper<CmsSubject>(cmsSubject).lambda().orderByDesc(CmsSubject::getSort));
		return R.data(list);
	}

	/**
	 * 资讯列表分页
	 */
	@GetMapping("/article/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "资讯列表分页", notes = "资讯列表分页")
	@ApiImplicitParams({
			@ApiImplicitParam(name="subjectId",value=" 网校资讯类型 ",dataType="int", paramType = "query")})
	public R<IPage<Article>> article(Integer subjectId, Query query) {
		Article article = new Article();
		article.setSubjectId(subjectId);
		IPage<Article> pages = articleService.page(Condition.getPage(query), Condition.getQueryWrapper(article).lambda().orderByDesc(Article::getSort).orderByDesc(Article::getId));
		return R.data(pages);
	}


	/**
	 * app升级操作
	 */
	@GetMapping("/checkVersion")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "app升级操作", notes = "app升级操作")
	@ResponseBody
	@ApiImplicitParams({
	@ApiImplicitParam(name="version",value=" 版本号 ",dataType="String", paramType = "String"),
	@ApiImplicitParam(name="versionType",value="手机类型",dataType="String", paramType = "String")})
	public R checkVersion(String version,String versionType) {
		Map<String,Object> map = new HashMap<>();
		Map<String,String> appConfig = ProfileConfig.getConfig(WebsiteProfile.APPANDROID);
		String url =appConfig.get("updateUrl");
		//获取后台版本号
		String versionNew = appConfig.get("appId");
		//升级提示
		String versionDescription=appConfig.get("versionDescription");
		if(version.compareTo(versionNew)<0) {
			Map<String,Object> data = new HashMap<>();
			//下载链接
			data.put("appAddress",url);
			//升级提示
			data.put("versionDescription",versionDescription);
			//机型
			data.put("versionType",versionType);
			//升级版本号
			data.put("version",versionNew);
			map.put("data",data);
			map.put("code",0);
		}else {
			map.put("code",1);
		}

		return R.data(map);
	}


	/**
	 * 热门资讯列表
	 */
	@GetMapping("/article/hotList")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "热门资讯列表", notes = "热门资讯列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name="subjectId",value="网校资讯类型",dataType="int", paramType = "query")})
	public R<IPage<Article>> hotArticle(Integer subjectId, Query query) {
		Article article = new Article();
		article.setSubjectId(subjectId);
		IPage<Article> pages = articleService.page(Condition.getPage(query), Condition.getQueryWrapper(article).lambda().orderByDesc(Article::getClickNum));
		return R.data(pages);
	}


	/**
	 * 资讯详情
	 */
	@GetMapping("/article/{id}")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "查询资讯详情", notes = "资讯详情")
	public R<ArticleVO> articleInfo(@PathVariable Integer id) {
		Article article = articleService.getById(id);
		//更新浏览量
		article.setClickNum(article.getClickNum()+1);
		articleService.updateById(article);
		return R.data(ArticleWrapper.build().entityVO(article));
	}

	/**
	 * 用户评价查询分页
	 */
	@GetMapping("/comment/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "用户评价查询分页", notes = "传入comment")
	public R<IPage<CommentVO>> commentPage(CommentVO comment, Query query) {
		IPage<CommentVO> pages = commentService.selectCommentPage(Condition.getPage(query), comment);
		return R.data(pages);
	}

	/**
	 * 检查用户是否可以评论
	 */
	@GetMapping("/comment/checkComment")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "检查用户是否可以评论", notes = "检查用户是否可以评论")
	public R checkComment(Integer courseId) {
		//判断用户是否已获取此课程，如果没有获取则不能评论
		TrxorderDetail trxorderDetail = new TrxorderDetail();
		Integer userId = SecureUtil.getUserId();
		//判断如果没有用户信息则进行登录
		if(Func.equals(userId,-1)){
			return R.fail(ResultCode.UN_AUTHORIZED);
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


	/**
	 *  查询专业列表
	 */
	@GetMapping("/subject/subjectLevel2List")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "查询专业列表", notes = "传入subject")
	public R<List<Map<String,Object>>> subjectLevel2List(Subject subject) {
		LambdaQueryWrapper<Subject> subjectLambdaQueryWrapper = new QueryWrapper<Subject>().lambda().orderByDesc(Subject::getSort).eq(Subject::getType,"course");
		subjectLambdaQueryWrapper.ne(Subject::getParentId,0);
		List<Subject> list = subjectService.list(subjectLambdaQueryWrapper);
		List<Map<String,Object>> resultList = Lists.newArrayList();
		for (Subject subj : list) {
			Map<String,Object> map = Maps.newHashMap();
			map.put("id",subj.getId());
			map.put("key",subj.getId());
			map.put("value",subj.getId());
			map.put("title",subj.getSubjectName());
			map.put("parentId",subj.getParentId());
			resultList.add(map);
		}
		return R.data(resultList);
	}

	/**
	 *  查询考试专业列表
	 */
	@GetMapping("/subject/subjectExamList")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "查询考试专业列表", notes = "传入subject")
	public R<List<Map<String,Object>>> subjectExamList(Subject subject) {
		LambdaQueryWrapper<Subject> subjectLambdaQueryWrapper = new QueryWrapper<Subject>().lambda().orderByDesc(Subject::getSort).eq(Subject::getType,"exam");
		subjectLambdaQueryWrapper.ne(Subject::getParentId,0);
		List<Subject> list = subjectService.list(subjectLambdaQueryWrapper);
		List<Map<String,Object>> resultList = Lists.newArrayList();
		for (Subject subj : list) {
			Map<String,Object> map = Maps.newHashMap();
			map.put("id",subj.getId());
			map.put("key",subj.getId());
			map.put("value",subj.getId());
			map.put("title",subj.getSubjectName());
			map.put("parentId",subj.getParentId());
			resultList.add(map);
		}
		return R.data(resultList);
	}

	/**
	 * 获取考试分类
	 * */
	@GetMapping("/subject/getExamSubjectList")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "获取考试分类", notes = "传入subject")
	public List<Subject> getExamSubjectList(Subject subject) {
		LambdaQueryWrapper<Subject> subjectLambdaQueryWrapper = new QueryWrapper<Subject>().lambda().orderByDesc(Subject::getSort).eq(Subject::getType,"exam");
		subjectLambdaQueryWrapper.ne(Subject::getParentId,0);
		List<Subject> list = subjectService.list(subjectLambdaQueryWrapper);
		return list;
	}

	/**
	 *  直播回调
	 */
	@PostMapping("/playback")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "直播回调", notes = "传入livePlaybackMap")
	public Object back(LivePlaybackMap livePlaybackMap) {
		if(Func.equals(livePlaybackMap.getStatus(),"100")){
			if(Func.isNotBlank(livePlaybackMap.getRoom_id())){
				LivePlayback livePlayback = livePlaybackService.getOne(new QueryWrapper<LivePlayback>().lambda().eq(LivePlayback::getLiveRoomId, livePlaybackMap.getRoom_id()));
				if(Func.isEmpty(livePlayback)){
					livePlayback = new LivePlayback();
				}
				livePlayback.setLiveRoomId(livePlaybackMap.getRoom_id());
				livePlayback.setPrefaceUrl(livePlaybackMap.getPreface_url());
				livePlayback.setTotalSize(livePlaybackMap.getTotal_size());
				livePlayback.setStatus(livePlaybackMap.getStatus());
				livePlayback.setVideoId(livePlaybackMap.getVideo_id());
				livePlayback.setVideoDuration(livePlaybackMap.getLength());
				livePlayback.setCreateTime(LocalDateTime.now());

				LiveRoom liveRoom = liveRoomService.getOne(new QueryWrapper<LiveRoom>().lambda().eq(LiveRoom::getClassroomId, livePlaybackMap.getRoom_id()));
				//1 未录制   2 已录制
				liveRoom.setPlaybackStatus("2");
				liveRoomService.updateById(liveRoom);
				livePlayback.setName(liveRoom.getName());
				livePlaybackService.saveOrUpdate(livePlayback);
			}
		}
		Map<String,Object> result = Maps.newHashMap();
		result.put("code",0);
		return R.data(result);
	}

	/**
	 * 教师列表
	 */
	@GetMapping("/teacher/teacherList")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "教师列表分页", notes = "传入TeacherVO")
	public R<IPage<TeacherVO>> queryTeacherList(TeacherVO teacher, Query query) {
		IPage<TeacherVO> pages = teacherService.selectTeacherPage(Condition.getPage(query), teacher);
		return R.data(pages);
	}
	/**
	 * 教师详情
	 */
	@GetMapping("/teacher/teacherInfo/{id}")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "查询教师详情", notes = "教师详情,传入教师id")
	public R<Teacher> teacherInfo(@PathVariable Integer id) {
		Teacher teacher = teacherService.getById(id);
		return R.data(teacher);
	}
}
