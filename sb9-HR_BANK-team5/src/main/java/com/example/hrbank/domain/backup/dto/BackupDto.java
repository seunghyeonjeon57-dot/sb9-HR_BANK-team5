package com.example.hrbank.domain.backup.dto;

import com.example.hrbank.domain.backup.entity.BackupHistory;
import com.example.hrbank.domain.backup.entity.BackupStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BackupDto {

  private Long id;
  private String worker;
  private LocalDateTime startedAt;
  private LocalDateTime endedAt;
  private BackupStatus status; // IN_PROGRESS, COMPLETED, SKIPPED, FAILED
  private Long fileId;

  public static BackupDto from(BackupHistory entity) {
    return BackupDto.builder()
        .id(entity.getId())
        .worker(entity.getWorker())
        .startedAt(entity.getStartedAt())
        .endedAt(entity.getEndedAt())
        .status(entity.getStatus())
        .fileId(entity.getFileId())
        .build();
  }
}