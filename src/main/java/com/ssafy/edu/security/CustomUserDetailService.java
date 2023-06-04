package com.ssafy.edu.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ssafy.edu.member.model.dao.MemberMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

	private final MemberMapper memberMapper;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			return memberMapper.memberDetail(username);
		} catch (Exception e) {
			log.debug("CustomUserDetailService 에러");
			//return new UsernameNotFoundException("사용자를 찾을수 없습니다."));
			return null;
		}
	}

}
