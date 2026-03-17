package com.example.hrbank.domain.employee.dto.request;

import com.example.hrbank.domain.employee.entity.enums.EmployeeStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public record StatsDepartmentRequest(
    @Schema(allowableValues = {"department","position"},defaultValue = "department")
    String groupBy,
    @Schema(defaultValue = "ACTIVE")
    EmployeeStatus status
) {

}
