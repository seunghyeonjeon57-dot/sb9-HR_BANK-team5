package com.example.hrbank.domain.employee.dto.data;

import com.example.hrbank.domain.employee.entity.enums.EmployeeStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "직원 정보")
public record EmployeeDto(
    Long id,
    String name,
    String email,
    String employeeNumber,
    Long departmentId,
    String departmentName,
    String position,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate hireDate,
    EmployeeStatus status,
    Long profileImageId
)
{

}
