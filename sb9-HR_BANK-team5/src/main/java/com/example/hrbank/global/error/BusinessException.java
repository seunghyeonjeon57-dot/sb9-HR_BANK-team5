package com.example.hrbank.global.error;

import lombok.Getter;

//서비스 로직에서 사용하는 최상위 예외클래스
@Getter
public class BusinessException extends RuntimeException {
  private final ErrorCode errorCode;

  public BusinessException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}