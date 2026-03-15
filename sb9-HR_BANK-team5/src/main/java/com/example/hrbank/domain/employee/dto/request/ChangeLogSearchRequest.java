package com.example.hrbank.domain.employee.dto.request;

import com.example.hrbank.domain.employee.entity.enums.ChannelType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "이력 목록 검색 요청")
public record ChangeLogSearchRequest(
    String employeeNumber,
    String memo,
    String ipAddress,
    LocalDateTime atFrom,
    LocalDateTime atTo,
    ChannelType type,
    Long lastId,
    Integer size,
    String sortField//Ip주소나 시간으로 정렬
) {}
