package com.example.hrbank.domain.backup.service;

import com.example.hrbank.domain.backup.dto.response.BackupCursorPageResponse; // 추가
import com.example.hrbank.domain.backup.dto.response.BackupResponse;
import com.example.hrbank.domain.backup.entity.BackupStatus;
import java.time.LocalDateTime; // 추가

public interface BackupService {

  BackupResponse runBackup(String clientIp);

  void runBatchBackup();

  BackupResponse getLatestBackup(BackupStatus status);

  // 추가: 백업 이력 목록 조회 (커서 기반 페이지네이션)
  BackupCursorPageResponse getBackupList(
      String worker,
      BackupStatus status,
      LocalDateTime startedAtFrom,
      LocalDateTime startedAtTo,
      Long idAfter,
      Integer size,
      String sortField,
      String sortDirection
  );
}