package com.example.hrbank.domain.backup.service;

import com.example.hrbank.domain.backup.dto.response.BackupResponse;
import com.example.hrbank.domain.backup.entity.BackupStatus;

public interface BackupService {

  BackupResponse runBackup(String clientIp);

  void runBatchBackup();

  BackupResponse getLatestBackup(BackupStatus status);
}