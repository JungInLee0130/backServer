package com.ssafy.edu.member.service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtServiceImpl implements JwtService {

	public static final Logger logger = LoggerFactory.getLogger(JwtServiceImpl.class);

//	SALT는 토큰 유효성 확인 시 사용하기 때문에 외부에 노출되지 않게 주의해야 한다.
	private static final String SALT = "ssafySecret";
	
	private static final long ACCESS_TOKEN_EXPIRE = 1000 * 60 * 10; // 분단위
	private static final long REFRESH_TOKEN_EXPIRE = 1000 * 60 * 60 * 24 * 7 * 2L; // 주단위

	@Override
	public <T> String createAccessToken(String key, T data) {
		return create(key, data, "access-token", ACCESS_TOKEN_EXPIRE);
	}

	@Override
	public <T> String createRefreshToken(String key, T data) {
		return create(key, data, "refresh-token", REFRESH_TOKEN_EXPIRE);
	}

	@Override
	public <T> String create(String key, T data, String subject, long expire) {
		Claims claims = Jwts.claims()
				// 토큰 제목 설정 ex) access-token, refresh-token
				.setSubject(subject)
				// 생성일 설정
				.setIssuedAt(new Date())
				// 만료일 설정 (유효기간)
				.setExpiration(new Date(System.currentTimeMillis() + expire));
		
		claims.put(key, data); 
		
		String jwt = Jwts.builder()
				// Header 설정 : 토큰의 타입, 해쉬 알고리즘 정보 세팅.
				.setHeaderParam("typ", "JWT")
				.setClaims(claims)
				// Signature 설정 : secret key를 활용한 암호화.
				.signWith(SignatureAlgorithm.HS256, this.generateKey())
				.compact(); // 직렬화 처리.
		return jwt;
	}

	private byte[] generateKey() {
		byte[] key = null;
		try {
			key = SALT.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			if (logger.isInfoEnabled()) {
				e.printStackTrace();
			} else {
				logger.error("Making JWT Key Error ::: {}", e.getMessage());
			}
		}

		return key;
	}

	@Override
	public boolean checkToken(String jwt) {
		Jws<Claims> claims = Jwts.parser().setSigningKey(this.generateKey()).parseClaimsJws(jwt);
		return true;
	}
	
	
	@Override
	public String getMemberId(String accessToken) {
		
		Jws<Claims> claims = null;
		try {
			claims = Jwts.parser().setSigningKey(SALT.getBytes("UTF-8")).parseClaimsJws(accessToken);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		Map<String, Object> value = claims.getBody();
		String memberId = value.get("memberId").toString();
		return memberId;
	}

}
