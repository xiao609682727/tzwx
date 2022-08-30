package org.springcrazy.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer{
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")	// 允许跨域访问的路径
//        .allowedOriginPatterns("*")	// 允许跨域访问的源
//        .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")	// 允许请求方法
//        .maxAge(168000)	// 预检间隔时间
//        .allowedHeaders("*")  // 允许头部设置
//        .allowCredentials(true);	// 是否发送cookie
//    }

//    @Bean
//    public CorsFilter corsFilter() {
//        CorsConfiguration config = new CorsConfiguration();
//        // 设置允许跨域请求的域名
//        config.addAllowedOrigin("*");
//        // 是否允许证书 不再默认开启
//        // config.setAllowCredentials(true);
//        // 设置允许的方法
//        config.addAllowedMethod("*");
//        // 允许任何头
//        config.addAllowedHeader("*");
//        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
//        configSource.registerCorsConfiguration("/**", config);
//        return new CorsFilter(configSource);
//    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.setAllowCredentials(true);
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);
        return new CorsFilter(configSource);
    }
}
