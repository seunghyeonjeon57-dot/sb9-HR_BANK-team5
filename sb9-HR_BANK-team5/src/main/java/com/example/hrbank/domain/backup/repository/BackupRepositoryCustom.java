package com.example.hrbank.domain.backup.repository;

import com.example.hrbank.domain.backup.dto.request.BackupSearchRequest;
import com.example.hrbank.domain.backup.entity.BackupHistory;
import java.util.List;

public interface BackupRepositoryCustom {
  List<BackupHistory> searchBackups(BackupSearchRequest request);
  long countBackups(BackupSearchRequest request);
}