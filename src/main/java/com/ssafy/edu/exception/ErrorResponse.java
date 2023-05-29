package com.ssafy.edu.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    private String code;
    private String message;
    private int status;

    private List<CustomFieldError> errors;


    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    
    // 비즈니스 로직 에러
    public static class CustomFieldError{
        private String field;
        private String value;
        private String reason;

        private CustomFieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        // validation
        private CustomFieldError(FieldError fieldError) {
            this.field = fieldError.getField();
            this.value = fieldError.getRejectedValue().toString();
            this.reason = fieldError.getDefaultMessage();
        }
    }

    // errorresponse에 errorcode 내용 대입
    private void setErrorCode(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus();
    }

    private ErrorResponse(ErrorCode errorCode, List<FieldError> errors){
        setErrorCode(errorCode);
        // fielderror 내용대입
        this.errors = errors.stream().map(CustomFieldError::new).collect(Collectors.toList());
    }

    private ErrorResponse(ErrorCode errorCode, String exceptionMessage) {
        setErrorCode(errorCode);
        List<CustomFieldError> list = new ArrayList<>(Arrays.asList(new CustomFieldError("", "", exceptionMessage)));
        List<CustomFieldError> list2 = Collections.unmodifiableList(list);
        this.errors = list2;
    }

    public static ErrorResponse of(ErrorCode errorCode){
        return new ErrorResponse(errorCode, Collections.emptyList());
    }

    public static ErrorResponse of(ErrorCode errorCode, BindingResult bindingResult) {
        return new ErrorResponse(errorCode, bindingResult.getFieldErrors());
    }

    public static ErrorResponse of(ErrorCode errorCode, String exceptionMessage) {
        return new ErrorResponse(errorCode, exceptionMessage);
    }
}
