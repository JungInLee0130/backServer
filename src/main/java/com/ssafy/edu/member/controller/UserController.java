package com.ssafy.edu.member.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.edu.member.model.dao.MemberMapper;
import com.ssafy.edu.member.model.dto.MemberDto;
import com.ssafy.edu.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

	private final JwtTokenProvider jwtTokenProvider;
	private final MemberMapper memberMapper;
	
	final String memberId = "admin"; // 아이디
	final String emailId = "admin"; // 이메일 아이디
	final String emailDomain = "naver.com"; // 이메일 도메인
	final String nickname = "admin"; // 닉네임
	final String password = "12345678"; // 비밀번호
	final String phoneNumber = "010-1234-1234"; // 전화번호
	
	final String createdAt = "2023-05-19 17:35:46"; // 생성 날짜
	final String modifiedAt = "2023-05-23 16:25:32"; // 수정 날짜
	
	final char memberRole = 'A'; // 사용자 역할
	final char status = 'Y'; // 삭제 상태 
  
	MemberDto mdto = new MemberDto(memberId, emailId, emailDomain, nickname,
			password, phoneNumber, createdAt, modifiedAt, memberRole, status);
	
	@PostMapping("/join")
	 public String join(){
        log.info("로그인 시도됨");

        try {
			memberMapper.join(mdto);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return mdto.toString();
    }
	
	@PostMapping("/login")
	public String login(@RequestBody Map<String, String> member) {
        log.info("member memberId = {}", member.get("memberId"));
        MemberDto dto = null;
		try {
			dto = memberMapper.memberDetail(member.get("memberId"));
		} catch (Exception e) {
			e.printStackTrace();
		}

        return jwtTokenProvider.createToken(dto.getMemberId(), dto.getMemberRole());
    }
}
