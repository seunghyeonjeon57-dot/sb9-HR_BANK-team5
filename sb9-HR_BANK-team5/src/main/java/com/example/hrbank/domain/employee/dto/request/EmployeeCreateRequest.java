package com.example.hrbank.domain.employee.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "직원 등록 요청")
public record EmployeeCreateRequest(
    String name,
    String email,
    Long departmentId,
    String position,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate hireDate,
    String memo
) {

}
