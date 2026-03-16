package com.example.hrbank.domain.backup.repository;

import com.example.hrbank.domain.backup.entity.BackupHistory;
import com.example.hrbank.domain.backup.entity.BackupStatus;
import java.time.LocalDateTime;
import java.util.List;

public interface BackupRepositoryCustom {
  List<BackupHistory> searchBackups(
      String worker,
      BackupStatus status,
      LocalDateTime fromAt,
      LocalDateTime toAt,
      Long idAfter,
      int size,
      String sortField,
      String sortDirection
  );

  long countBackups(
      String worker,
      BackupStatus status,
      LocalDateTime fromAt,
      LocalDateTime toAt
  );
}