
package org.springcrazy.common.config;


import lombok.AllArgsConstructor;
import org.springcrazy.common.interceptor.ViewInterceptor;
import org.springcrazy.core.secure.registry.SecureRegistry;
import org.springcrazy.core.tool.constant.SystemConstant;
import org.springcrazy.core.tool.utils.ObjectUtil;
import org.springcrazy.core.tool.utils.RedisUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * TongZhou配置

 */
@Configuration
@AllArgsConstructor
public class CrazyConfiguration implements WebMvcConfigurer {

	private RedisUtil redisUtil;

	@Bean
	public SecureRegistry secureRegistry() {
		SecureRegistry secureRegistry = new SecureRegistry();
		//是否对jwt进行验证
		secureRegistry.setEnabled(true);
		secureRegistry.setSingleLogin(true);
		secureRegistry.excludePathPatterns("/crazy-auth/**");
		secureRegistry.excludePathPatterns("/crazy-log/**");
		secureRegistry.excludePathPatterns("/crazy-system/menu/auth-routes");
		secureRegistry.excludePathPatterns("/doc.html");
		secureRegistry.excludePathPatterns("/js/**");
		//本地启动的时候，图片放过此路径
		secureRegistry.excludePathPatterns("/upload/**");
		secureRegistry.excludePathPatterns("/webjars/**");
		secureRegistry.excludePathPatterns("/common/**");
		secureRegistry.excludePathPatterns("/swagger-resources/**");
		secureRegistry.excludePathPatterns("/edu/orders/statistics/*");
		secureRegistry.excludePathPatterns("/edu/orders/export-orders");
		return secureRegistry;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//访客记录
		registry.addInterceptor(new ViewInterceptor(redisUtil)).addPathPatterns("/front/**");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler("/js/**").addResourceLocations("classpath:/js/");
		registry.addResourceHandler("doc.html")
			.addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**")
			.addResourceLocations("classpath:/META-INF/resources/webjars/");
		//获取到配置文件
		SystemConstant me = SystemConstant.me();
		String fileRoot=me.getImagePath();
		if(ObjectUtil.isNotEmpty(me.getImagePath())){
			//结尾必须有 File.separator
			if (!me.getImagePath().endsWith("\\") && !me.getImagePath().endsWith("/")){
				fileRoot = me.getImagePath()+ File.separator;
			}
		} else {
			throw new RuntimeException("config 'project.file.root' not null");
		}
		//添加文件可访问路径
		registry.addResourceHandler("/upload/**")
				.addResourceLocations("file:"+fileRoot);
	}

}
