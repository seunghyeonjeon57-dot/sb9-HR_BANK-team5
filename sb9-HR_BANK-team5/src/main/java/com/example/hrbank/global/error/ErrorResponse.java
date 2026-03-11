package com.example.hrbank.global.error;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
  private final LocalDateTime timestamp;
  private final int status;
  private final String message;
  private final String details;

  public static ErrorResponse of(int status, String message, String details) {
    return ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(status)
        .message(message)
        .details(details)
        .build();
  }
}