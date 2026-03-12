package com.example.hrbank.domain.employee.dto.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "직원 수 추이 정보")
public record EmployeeTrendDto(
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate date,
    Integer count,
    Integer change,
    Number changeRate

) {

}
