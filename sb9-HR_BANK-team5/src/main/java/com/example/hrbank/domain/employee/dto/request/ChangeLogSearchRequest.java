package com.example.hrbank.domain.employee.dto.request;

import com.example.hrbank.domain.employee.entity.enums.ChangeLogType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Schema(description = "이력 목록 검색 요청")
public record ChangeLogSearchRequest(
    String employeeNumber,
    ChangeLogType type,
    String memo,
    String ipAddress,
    @DateTimeFormat(iso = ISO.DATE_TIME)
    LocalDateTime atFrom,
    @DateTimeFormat(iso = ISO.DATE_TIME)
    LocalDateTime atTo,
    Long lastId,
    Integer size,
    @Schema(allowableValues = {"ipAddress","at"},defaultValue = "at")
    String sortField,//Ip주소나 시간으로 정렬
    @Schema(allowableValues = {"asc","desc"},defaultValue = "desc")
    String sortDirection
) {}
