package com.example.hrbank.domain.backup.dto.response;

import lombok.Builder;
import java.util.List;

@Builder
public record BackupCursorPageResponse(
    List<BackupResponse> content,
    String nextCursor,
    Long nextIdAfter,
    Integer size,
    Long totalElements,
    Boolean hasNext
) {
}