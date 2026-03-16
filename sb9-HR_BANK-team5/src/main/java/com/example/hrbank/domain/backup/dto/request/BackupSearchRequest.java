package com.example.hrbank.domain.backup.dto.request;

import com.example.hrbank.domain.backup.entity.BackupStatus;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

public record BackupSearchRequest(
    String worker,
    BackupStatus status,

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime startedAtFrom,

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime startedAtTo,

    Long idAfter,
    String cursor,

    Integer size,
    String sortField,
    String sortDirection
) {

  public BackupSearchRequest {
    if (size == null) size = 10;
    if (sortField == null) sortField = "startedAt";
    if (sortDirection == null) sortDirection = "DESC";
  }
}