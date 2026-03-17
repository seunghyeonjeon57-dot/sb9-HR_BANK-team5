package com.example.hrbank.domain.employee.dto.request;

import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public record ChangeLogCountRequest(
    @DateTimeFormat(iso = ISO.DATE_TIME)
    LocalDateTime fromDate,
    @DateTimeFormat(iso = ISO.DATE_TIME)
    LocalDateTime toDate
) {
  public void validate() {
  if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
    throw new IllegalArgumentException("Invalid request: Invalid date range.");
  }
}

}
