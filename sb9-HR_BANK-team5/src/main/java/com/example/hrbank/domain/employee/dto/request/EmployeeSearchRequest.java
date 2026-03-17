package com.example.hrbank.domain.employee.dto.request;

import com.example.hrbank.domain.employee.entity.enums.EmployeeStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record EmployeeSearchRequest(

    String nameOrEmail,
    String employeeNumber,
    String departmentName,
    String position,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate hireDateFrom,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate hireDateTo,
    EmployeeStatus status,
    Long idAfter,
    String cursor,
    @Schema(defaultValue = "10")
    Integer size,
    @Schema(allowableValues = {"name","employeeNumber","hireDate"},defaultValue = "name")
    String sortField,

    @Schema(allowableValues = {"ASC", "DESC"}, defaultValue = "ASC")
    String sortDirection


) {}