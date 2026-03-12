package com.example.hrbank.domain.employee.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "직원 정보 수정 이력 변경 내용(상세 조회용)")
public record DiffDto(
    String propertyName,
    String before,
    String after
) {

}
