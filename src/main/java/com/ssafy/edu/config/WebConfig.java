package com.ssafy.edu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ssafy.edu.interceptor.JwtInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	public static final String ALLOWED_METHOD_NAMES = "GET,POST,PUT,DELETE";

	@Autowired
	JwtInterceptor jwtInterceptor;
	 
	@Override
	public void addCorsMappings(final CorsRegistry registry) {
		registry.addMapping("/**")
		.allowedMethods(ALLOWED_METHOD_NAMES.split(","));
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(jwtInterceptor)
		.addPathPatterns("/**")
		.excludePathPatterns("/login")
		.excludePathPatterns("/logout/**")
		.excludePathPatterns("/join")
				.excludePathPatterns("/check/**")
		.excludePathPatterns("/uploadImage")
		.excludePathPatterns("/review/**")
		.excludePathPatterns( 
				"/v2/api-docs",
                "/swagger-resources/**",
                "/swagger-ui.html",
                "/webjars/**");
	}

}
