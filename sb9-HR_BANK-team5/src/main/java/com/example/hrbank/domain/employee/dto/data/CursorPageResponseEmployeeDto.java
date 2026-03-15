package com.example.hrbank.domain.employee.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import lombok.Builder;

@Builder
@Schema(description = "커서 시반 패이지 응답")
public record CursorPageResponseEmployeeDto(
    List<EmployeeDto> content,
    String nextCursor,
    Long nextIdAfter,
    Integer size,
    Long totalElements,
    boolean hasNext
) {

}
