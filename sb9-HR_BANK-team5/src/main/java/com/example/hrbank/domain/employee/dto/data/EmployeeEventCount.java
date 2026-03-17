package com.example.hrbank.domain.employee.dto.data;

import java.time.LocalDate;

public record EmployeeEventCount(
    LocalDate date,
    Long joinCount,
    Long quitCount
) {

}
