package com.ssafy.edu.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    private final ErrorCode errorCode;
    private final String errorMessage;
    public BusinessException(ErrorCode errorCode){
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getMessage();
    }
}
