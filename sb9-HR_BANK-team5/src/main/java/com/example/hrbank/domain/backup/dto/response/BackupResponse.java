package com.example.hrbank.domain.backup.dto.response;

import com.example.hrbank.domain.backup.entity.BackupStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record BackupResponse(
    Long id,
    String worker,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime startedAt,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime endedAt,
    BackupStatus status,
    Long fileId
) {
}