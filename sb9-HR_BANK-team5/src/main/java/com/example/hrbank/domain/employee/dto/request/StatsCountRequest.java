package com.example.hrbank.domain.employee.dto.request;

import com.example.hrbank.domain.employee.entity.enums.EmployeeStatus;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record StatsCountRequest(
    EmployeeStatus status,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate fromDate,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate toDate
) {

}
