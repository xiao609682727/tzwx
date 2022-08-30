
package org.springcrazy.common.interceptor;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springcrazy.common.cache.CacheNames;
import org.springcrazy.core.tool.jackson.JsonUtil;
import org.springcrazy.core.tool.utils.DateUtil;
import org.springcrazy.core.tool.utils.RedisUtil;
import org.springcrazy.core.tool.utils.WebUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 访客记录
 *
 */
@Slf4j
@AllArgsConstructor
public class ViewInterceptor extends HandlerInterceptorAdapter {

	private RedisUtil redisUtil;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		log.info("记录访客记录，请求接口：{}，请求IP：{}，请求参数：{}", request.getRequestURI(), WebUtil.getIP(request), JsonUtil.toJson(request.getParameterMap()));
		String dateStr = DateUtil.format(DateUtil.now(), DateUtil.PATTERN_DATE);
		//时间为10天
		redisUtil.sSetAndTime(CacheNames.VIEWLOG+"_"+ dateStr,864000,WebUtil.getIP(request));
		return true;
	}

}
