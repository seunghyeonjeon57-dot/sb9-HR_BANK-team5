package com.example.hrbank.domain.backup.controller;

import com.example.hrbank.domain.backup.dto.response.BackupCursorPageResponse;
import com.example.hrbank.domain.backup.dto.response.BackupResponse;
import com.example.hrbank.domain.backup.entity.BackupStatus;
import com.example.hrbank.domain.backup.service.BackupService;
import com.example.hrbank.global.util.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/backups")
@RequiredArgsConstructor
public class BackupController implements BackupApi {

  private final BackupService backupService;

  @Override
  public ResponseEntity<BackupResponse> createBackup(HttpServletRequest request) {
    String clientIp = IpUtil.getUserIp(request);
    BackupResponse response = backupService.runBackup(clientIp);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<BackupResponse> getLatestBackup(BackupStatus status) {
    BackupResponse response = backupService.getLatestBackup(status);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<BackupCursorPageResponse> getBackups(
      String worker,
      BackupStatus status,
      LocalDateTime startedAtFrom,
      LocalDateTime startedAtTo,
      Long idAfter,
      String cursor,
      Integer size,
      String sortField,
      String sortDirection) {

    BackupCursorPageResponse response = backupService.getBackupList(
        worker,
        status,
        startedAtFrom,
        startedAtTo,
        idAfter,
        size,
        sortField,
        sortDirection
    );

    return ResponseEntity.ok(response);
  }
}