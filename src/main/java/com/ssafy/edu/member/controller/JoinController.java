package com.ssafy.edu.member.controller;


import com.ssafy.edu.exception.BusinessException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.edu.exception.ErrorCode;
import com.ssafy.edu.member.model.dto.MemberDto;
import com.ssafy.edu.member.service.MemberService;
import com.ssafy.edu.utils.ApiUtils;
import com.ssafy.edu.utils.ApiUtils.ApiResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class JoinController {
	
	private final MemberService memberService;
	
	// 회원가입 : insert : exclude
	@PostMapping("/join")
	public ApiResult<MemberDto> join(@RequestBody MemberDto mdto) {
		memberService.join(mdto);
		MemberDto responsedto = memberService.memberDetail(mdto.getMemberId());
		// 성공
		if (responsedto != null) {
			return ApiUtils.success(responsedto);
		}
		
		throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
	}
	
	// 중복 아이디 체크 : include
	@GetMapping("/check/{memberId}")
	public ApiResult<MemberDto> duplicatedIdCheck(@PathVariable("memberId") String memberId){
		// 중복 멤버 있나 확인
		MemberDto responsedto = memberService.memberDetail(memberId);
		// 중복없음
		if (responsedto == null) {
			return ApiUtils.success(null);
		}
		// 204 던짐
		throw new BusinessException(ErrorCode.MEMBER_DUPLICATED);
	}
}
