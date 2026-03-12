package com.example.hrbank.domain.employee.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.ArrayList;


@Schema(description = "직원 정보 수정 이력 상세 (상세 조회용)")
public record ChangeLogDetailDto(
    Long id,
    String type,
    String employeeNumber,
    String memo,
    String ipAddress,
    Instant at,
    String employeeName,
    Long profileImageId,
    ArrayList<Object> diffs
) {

}
