package com.example.hrbank.domain.backup.controller;

import com.example.hrbank.domain.backup.dto.request.BackupSearchRequest;
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

@RestController
@RequestMapping("/api/backups")
@RequiredArgsConstructor
public class BackupController implements BackupApi {

  private final BackupService backupService;

  @Override
  public ResponseEntity<BackupResponse> createBackup(HttpServletRequest request) {
    String clientIp = IpUtil.getUserIp(request);
    return ResponseEntity.ok(backupService.runBackup(clientIp));
  }

  @Override
  public ResponseEntity<BackupResponse> getLatestBackup(BackupStatus status) {
    return ResponseEntity.ok(backupService.getLatestBackup(status));
  }

  @Override
  public ResponseEntity<BackupCursorPageResponse> getBackups(BackupSearchRequest request) {
    return ResponseEntity.ok(backupService.getBackupList(request));
  }
}