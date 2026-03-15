package com.example.hrbank.domain.backup.scheduler;

import com.example.hrbank.domain.backup.service.BackupService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BackupScheduler {

  private final BackupService backupService;

  @Scheduled(cron = "${backup.interval:0 0 * * * *}")
  public void scheduleBackup() {
    backupService.runBatchBackup();
  }
}