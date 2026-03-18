package com.example.hrbank.domain.employee.dto.data;


import com.example.hrbank.domain.employee.entity.enums.ChangeLogType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "직원 정보 수정 이력(목록 조회용)")
public record ChangeLogDto(
    Long id,
    ChangeLogType type,
    String employeeNumber,
    String memo,
    String ipAddress,
    LocalDateTime at
) {

}
