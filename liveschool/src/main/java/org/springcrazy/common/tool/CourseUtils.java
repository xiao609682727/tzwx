package org.springcrazy.common.tool;

import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.edu.entity.Course;

import java.util.Date;

public class CourseUtils {

    public static Date getAuthTime(Course course) {
        Date authTime = null;
        // 时间段
        if (Func.equals(course.getLosetype(),"0")) {
            authTime = course.getEndTime();
        }
        // 时间点
        if (Func.equals(course.getLosetype(),"1")) {
            authTime = org.springcrazy.core.tool.utils.DateUtil.plusDays(org.springcrazy.core.tool.utils.DateUtil.now(),Func.toInt(course.getLoseTime()));
        }
        return authTime;
    }
}
