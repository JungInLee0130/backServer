package com.ssafy.edu.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	// C001: common
	// M001: member
	TOKEN_INVALID(401,"C002", "토큰이 유효하지 않습니다."),
	TOKEN_EXPIRED(401,"C003","토큰 유효기간이 만료되었습니다."),
	UN_AUTHORIZED(401,"C004","로그인이 필요 합니다."),

	MEMBER_NOT_FOUND(204,"M001","회원을 찾을 수 없습니다."),
	MEMBER_DUPLICATED(204,"M002","중복된 회원 입니다."),

	// Exception
	INTERNAL_SERVER_ERROR(500,"E001","내부 서버 오류입니다.");

	private final String code;
	private final String message;
	private int status;

	ErrorCode(final int status, final String code, final String message){
		this.status = status;
		this.message = message;
		this.code = code;
	}
}
