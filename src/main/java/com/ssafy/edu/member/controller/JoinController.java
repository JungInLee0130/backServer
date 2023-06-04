package com.ssafy.edu.member.controller;


import com.ssafy.edu.exception.BusinessException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
	// BindingResult 사용할 부분
	@PostMapping("/join")
	public ApiResult<MemberDto> join(@RequestBody MemberDto mdto, BindingResult bindingResult) {
		// 멤버 아이디가 8이상 16이하이고
		// 영문자, 숫자만됨
		int memberIdLength = mdto.getMemberId().length();
		int passwordLength = mdto.getPassword().length();
		if (8 > memberIdLength || memberIdLength > 16){
			bindingResult.addError(new FieldError("mdto", "memberId", ErrorCode.MEMBERID_LENGTH.getMessage()));
			throw new BusinessException(ErrorCode.MEMBERID_LENGTH);
		}
		if (8 > passwordLength || passwordLength > 16){
			bindingResult.addError(new FieldError("mdto", "password", ErrorCode.PASSWORD_LENGTH.getMessage()));
			throw new BusinessException(ErrorCode.PASSWORD_LENGTH);
		}

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
