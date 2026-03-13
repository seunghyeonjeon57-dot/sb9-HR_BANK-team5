package com.example.hrbank.domain.employee.dto.data;

import com.example.hrbank.domain.employee.entity.enums.ChannelType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Schema(description = "직원 정보 수정 이력 상세 (상세 조회용)")
public record ChangeLogDetailDto(
    Long id,
    ChannelType type,
    String employeeNumber,
    String memo,
    String ipAddress,
    LocalDateTime at,
    String employeeName,
    Long profileImageId,
    List<DiffDto> diffs
) {

}
