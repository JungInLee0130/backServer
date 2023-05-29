package com.ssafy.edu.interceptor;


import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.ssafy.edu.exception.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ssafy.edu.exception.ErrorCode;
import com.ssafy.edu.member.service.JwtService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.swagger.models.HttpMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtInterceptor implements HandlerInterceptor {
	
	private final JwtService jwtService;

	@Override
	// JwtInterceptor : jwt validation만 검증
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (isPreflightRequest(request)) {
			return true;
		}
		String accessToken = request.getHeader("access-token"); // 엑세스 토큰 받아옴
		String refreshToken = request.getHeader("refresh-token");
		
		// 엑세스 토큰 없음
		if (accessToken == null) {
			throw new BusinessException(ErrorCode.UN_AUTHORIZED);
		}

		// 엑세스 토큰 형식 검증, expired 검증
		try {
			jwtService.checkToken(accessToken);
		} catch(MalformedJwtException | UnsupportedJwtException | SignatureException e) {
			throw new BusinessException(ErrorCode.TOKEN_INVALID);
		} catch(ExpiredJwtException e) {
			// 로그인시 인터셉터를 거치지 않으므로 여기서 만들필요없다.
			// 단, 만료되었으면 refreshtoken 확인 -> access 생성후 던져줌.
			//response.setHeader("Access-Control-Expose-Headers", "access-token");
			//response.setHeader("access-token", newToken);
			String memberId = jwtService.getMemberId(refreshToken);
			jwtService.createAccessToken("memberId", memberId);
			throw new BusinessException(ErrorCode.TOKEN_EXPIRED);
		}

		// 엑세스토큰 validation 검증 : 전체 exception
		try {
			jwtService.checkToken(accessToken);
		} catch(Exception e) {
			throw new BusinessException(ErrorCode.TOKEN_INVALID);
		}

		return true;
	}


	private boolean isPreflightRequest(HttpServletRequest request) {
		return isOptions(request) && hasHeaders(request) && hasMethod(request) && hasOrigin(request);
	}

	private boolean hasOrigin(HttpServletRequest request) {
		return Objects.nonNull(request.getHeader("Origin"));
	}

	private boolean hasMethod(HttpServletRequest request) {
		return Objects.nonNull(request.getHeader("Access-Control-Request-Method"));
	}

	private boolean isOptions(HttpServletRequest request) {
		return request.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.toString());
	}

	private boolean hasHeaders(HttpServletRequest request) {
		return Objects.nonNull(request.getHeader("Access-Control-Request-Headers"));
	}


}
