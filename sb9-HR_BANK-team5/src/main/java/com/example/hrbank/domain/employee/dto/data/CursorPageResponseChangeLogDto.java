package com.example.hrbank.domain.employee.dto.data;

import com.example.hrbank.domain.employee.entity.enums.ChangeLogType;
import java.util.List;

public record CursorPageResponseChangeLogDto(
    List<ChangeLogDto> content,
    ChangeLogType type,
    String nextCursor,
    Long nextIdAfter,
    Integer size,
    Long totalElements,
    boolean hasNext
) {

}
