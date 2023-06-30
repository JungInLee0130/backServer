package com.ssafy.edu.member.controller;


import com.ssafy.edu.exception.BusinessException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
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
//	@PostMapping("/join")
//	public ApiResult<MemberDto> join(@RequestBody MemberDto mdto, BindingResult bindingResult) {
//		// 멤버 아이디가 8이상 16이하이고
//		// 영문자, 숫자만됨
//		// 동기일때 데이터 글자수 확인 및 데이터 남기기, 에러처리
//		// 특수문자 들어가면안됨.
//		// 길이 제한 에러
//		// 아이디가 null이면 안됨.
//		if (mdto.getMemberId() == null || 8 > mdto.getMemberId().length() || mdto.getMemberId().length() > 16){
//			bindingResult.addError(new FieldError("mdto", "memberId", mdto.getMemberId(), false, null, null, ErrorCode.MEMBERID_LENGTH.getMessage()));
//			throw new BusinessException(ErrorCode.MEMBERID_LENGTH);
//		}
//		if (mdto.getNickname() == null || 8 > mdto.getNickname().length() || mdto.getNickname().length()> 16){
//			bindingResult.addError(new FieldError("mdto", "nickname", mdto.getNickname(), false, null, null, ErrorCode.NICKNAME_LENGTH.getMessage()));
//			throw new BusinessException(ErrorCode.NICKNAME_LENGTH);
//		}
//		if (mdto.getPassword() == null || 8 > mdto.getPassword().length() || mdto.getPassword().length() > 16){
//			bindingResult.addError(new FieldError("mdto", "password", mdto.getPassword(), false, null, null, ErrorCode.PASSWORD_LENGTH.getMessage()));
//			throw new BusinessException(ErrorCode.PASSWORD_LENGTH);
//		}
//
//		// 복합 룰 검증... 회원가입에서 쓸일이 있나,,?
//		// 굳이 쓸려면
//		// 비밀번호, 비밀번호 확인
//		if (mdto.getPassword() != null && mdto.getPasswordIdentify() != null){
//			if (mdto.getPassword().equals(mdto.getPasswordIdentify())){
//				// nothing
//			}
//			else{
//				bindingResult.addError(new ObjectError("mdto", null, null, "비밀번호가 일치하지않습니다."));
//				// throw 해줘야함.
//			}
//		}
//
//		// 검증에 실패하면 redirection 로직 or....
//		if (bindingResult.hasErrors()){
//
//		}
//
//		memberService.join(mdto);
//		MemberDto responsedto = memberService.memberDetail(mdto.getMemberId());
//		// 성공
//		if (responsedto != null) {
//			return ApiUtils.success(responsedto);
//		}
//
//		throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
//	}

	@PostMapping("/join")
	public ApiResult<MemberDto> join(@RequestBody MemberDto mdto, BindingResult bindingResult) {
		// 멤버 아이디가 8이상 16이하이고
		// 영문자, 숫자만됨
		// 동기일때 데이터 글자수 확인 및 데이터 남기기, 에러처리
		// 특수문자 들어가면안됨.
		// 길이 제한 에러
		// 아이디가 null이면 안됨.
		if (mdto.getMemberId() == null || 8 > mdto.getMemberId().length() || mdto.getMemberId().length() > 16){
			bindingResult.addError(new FieldError("mdto", "memberId", mdto.getMemberId(), false, new String[]{"range.mdto.memberId"}, null, null));
			throw new BusinessException(ErrorCode.MEMBERID_LENGTH);
		}
		if (mdto.getNickname() == null || 8 > mdto.getNickname().length() || mdto.getNickname().length()> 16){
			bindingResult.addError(new FieldError("mdto", "nickname", mdto.getNickname(), false, new String[]{"range.mdto.nickname"}, null, null));
			throw new BusinessException(ErrorCode.NICKNAME_LENGTH);
		}
		if (mdto.getPassword() == null || 8 > mdto.getPassword().length() || mdto.getPassword().length() > 16){
			bindingResult.addError(new FieldError("mdto", "password", mdto.getPassword(), false, new String[]{"range.mdto.password"}, null, null));
			throw new BusinessException(ErrorCode.PASSWORD_LENGTH);
		}

		// 복합 룰 검증... 회원가입에서 쓸일이 있나,,?
		// 굳이 쓸려면
		// 비밀번호, 비밀번호 확인
		if (mdto.getPassword() != null && mdto.getPasswordIdentify() != null){
			if (mdto.getPassword().equals(mdto.getPasswordIdentify())){
				// nothing
			}
			else{
				bindingResult.addError(new ObjectError("mdto", new String[]{"passwordIdentification"}, null,null));
				// throw 해줘야함.
			}
		}

		// 검증에 실패하면 redirection 로직 or....
		if (bindingResult.hasErrors()){

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
