package com.ssafy.edu.member.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ssafy.edu.exception.BusinessException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.edu.exception.ErrorCode;
import com.ssafy.edu.member.model.dto.MemberDto;
import com.ssafy.edu.member.service.JwtServiceImpl;
import com.ssafy.edu.member.service.MemberService;
import com.ssafy.edu.utils.ApiUtils;
import com.ssafy.edu.utils.ApiUtils.ApiResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@Api("사용자 컨트롤러  API V1")
public class LoginController {
	
	private final JwtServiceImpl jwtService;
	
	private final MemberService memberService;
	
	// 로그인 : exclude 인터셉터
	@ApiOperation(value = "로그인", notes = "Access-token과 로그인 결과 메세지를 반환한다.", response = Map.class)
	@PostMapping("/login")
	public ApiResult<Map<String, Object>> login(@RequestBody MemberDto mdto){
		Map<String, Object> resultMap = new HashMap<>();
		MemberDto loginUser = memberService.login(mdto);

		if (loginUser != null) { // 유저정보가 있음.
			// 엑세스, 리프레시 생성
			String accessToken = jwtService.createAccessToken("memberId", loginUser.getMemberId());// key, data
			String refreshToken = jwtService.createRefreshToken("memberId", loginUser.getMemberId());// key, data
			
			// db 저장
			memberService.saveRefreshToken(mdto.getMemberId(), refreshToken);
			
			// 엑세스 토큰 담아서 전송 : 이거 맞나?
			resultMap.put("access-token", accessToken);
			resultMap.put("refresh-token", refreshToken);

			return ApiUtils.success(resultMap);
		}
		else { // 해당 유저가 없음.
			throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
		}
	}
	
	// include
	@GetMapping("/info/{memberId}")
	public ApiResult<MemberDto> getInfo(@PathVariable("memberId") String memberId,
			HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		MemberDto memberDto = memberService.memberDetail(memberId);
		return ApiUtils.success(memberDto);
	}
	
	// 로그아웃 : exclude
	@GetMapping("/logout/{memberId}")
	public ApiResult<?> removeToken(@PathVariable("memberId") String memberId) {
		memberService.deleRefreshToken(memberId);
		return ApiUtils.success(null);
	}
	
	
	// 토큰 갱신 : include
	@PostMapping("/refresh")
	public ApiResult<Map<String, Object>> refreshToken(@RequestBody MemberDto memberDto, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		String token = request.getHeader("refresh-token");
		// 리프레시 토큰이 일치
		if (token.equals(memberService.getRefreshToken(memberDto.getMemberId()))) {
			// 엑세스 토큰 생성
			String accessToken = jwtService.createAccessToken("memberId", memberDto.getMemberId());
			resultMap.put("access-token", accessToken);
			return ApiUtils.success(resultMap);
		}
		throw new BusinessException(ErrorCode.TOKEN_INVALID);
	}
}
