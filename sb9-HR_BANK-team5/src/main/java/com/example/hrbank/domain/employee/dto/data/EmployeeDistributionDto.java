package com.example.hrbank.domain.employee.dto.data;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "직원 분포 정보")
public record EmployeeDistributionDto(
    String groupKey,
    Integer count,
    Number percentage
) {

}
