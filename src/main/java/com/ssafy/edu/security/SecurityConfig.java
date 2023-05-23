package com.ssafy.edu.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	private final JwtAuthenticationEntryPoint unauthorizedHandler;


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable();
		//http.httpBasic().disable();
		http.httpBasic().disable()
		.authorizeRequests()
		
		.antMatchers("/test").authenticated() // 인증 필요
		.antMatchers("/admin/**").hasRole("ADMIN") // 권한 확인
		.antMatchers("/user/**").hasRole("USER")
		.antMatchers("/**").permitAll()
		
		.and()
		// 필터 등록
		.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
				UsernamePasswordAuthenticationFilter.class);
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		super.configure(http);
	}

	
}
