package com.example.hrbank.domain.employee.dto.request;

import com.example.hrbank.domain.employee.entity.enums.EmployeeStatus;
import java.time.LocalDate;

public record EmployeeSearchRequest(
    Long id,
    String nameOrEmail,
    String departmentName,
    String position,
    String employeeNumber,
    LocalDate hireDateFrom,
    LocalDate hireDateTo,
    EmployeeStatus status,
    Integer size
) {}