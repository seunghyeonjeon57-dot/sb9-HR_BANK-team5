package com.example.hrbank.global.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e, HttpServletRequest request) {
    ErrorCode errorCode = e.getErrorCode();

    ErrorResponse response = ErrorResponse.of(errorCode.getStatus(), errorCode.getMessage(), request.getRequestURI());
    return new ResponseEntity<>(response, org.springframework.http.HttpStatus.valueOf(errorCode.getStatus()));
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request)
  { log.error("Unhandled Exception: ", e);
    ErrorResponse response = ErrorResponse.of(500, "서버 내부 오류", request.getRequestURI());
    return new ResponseEntity<>(response, org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
  }

}