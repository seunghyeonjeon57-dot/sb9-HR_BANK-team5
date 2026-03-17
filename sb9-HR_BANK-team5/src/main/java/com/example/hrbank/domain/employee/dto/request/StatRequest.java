package com.example.hrbank.domain.employee.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record StatRequest(
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate from,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate to,
    @Schema(allowableValues = {"day","week","month","quarter","year"},defaultValue = "month")
    String unit

) {
  public StatRequest{
    if(unit == null) unit="month";
  }
}
