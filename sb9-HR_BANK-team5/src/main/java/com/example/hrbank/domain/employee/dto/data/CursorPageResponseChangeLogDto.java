package com.example.hrbank.domain.employee.dto.data;

import com.example.hrbank.domain.employee.entity.enums.ChannelType;
import java.util.List;

public record CursorPageResponseChangeLogDto(
    List<ChangeLogDto> content,
    ChannelType type,
    String nextCursor,
    Long nextIdAfter,
    Integer size,
    Long totalElements,
    boolean hasNext
) {

}
