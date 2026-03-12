package com.example.hrbank.domain.department.dto.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;


@Schema(description = "부서 정보")
public record DepartmentDto(
    Long id,
    String name,
    String description,
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate establishedDate,
    Integer employeeCount
) {}
