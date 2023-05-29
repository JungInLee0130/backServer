package com.ssafy.edu.utils;

import org.springframework.http.HttpStatus;

public class ApiUtils {

  public static <T> ApiResult<T> success(T response) {
	  // success, response, error: null
    return new ApiResult<>(true, response, null);
  }

  public static ApiResult<?> error(Throwable throwable, HttpStatus status) {
    return new ApiResult<>(false, null, new ApiError(throwable, status));
  }

  public static ApiResult<?> error(String message, HttpStatus status) {
    return new ApiResult<>(false, null, new ApiError(message, status));
  }

  public static class ApiError {
    private final String message; // 에러내용
    private final int status; // 상태코드

    ApiError(Throwable throwable, HttpStatus status) {
      this(throwable.getMessage(), status);
    }

    ApiError(String message, HttpStatus status) {
      this.message = message;
      this.status = status.value();
    }

    public String getMessage() {
      return message;
    }

    public int getStatus() {
      return status;
    }

    
  }

  public static class ApiResult<T> {
    private final boolean success; // success or fail
    private final T response; // 데이터
    private final ApiError error; // error

    private ApiResult(boolean success, T response, ApiError error) {
      this.success = success;
      this.response = response;
      this.error = error;
    }

    public boolean isSuccess() {
      return success;
    }

    public ApiError getError() {
      return error;
    }

    public T getResponse() {
      return response;
    }

   
  }

}
