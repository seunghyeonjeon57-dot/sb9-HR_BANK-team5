package com.example.hrbank.domain.backup.controller;

import com.example.hrbank.domain.backup.dto.response.BackupCursorPageResponse;
import com.example.hrbank.domain.backup.dto.response.BackupResponse;
import com.example.hrbank.domain.backup.entity.BackupStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Tag(name = "데이터 백업 관리", description = "데이터 백업 관리 API")
public interface BackupApi {

  @Operation(summary = "데이터 백업 생성", description = "데이터 백업을 생성합니다. 이미 진행 중인 백업이 있으면 409 에러를 반환합니다.")
  @PostMapping
  ResponseEntity<BackupResponse> createBackup(HttpServletRequest request);

  @Operation(summary = "최근 백업 정보 조회", description = "지정된 상태의 가장 최근 백업 정보를 조회합니다.")
  @GetMapping("/latest")
  ResponseEntity<BackupResponse> getLatestBackup(
      @Parameter(description = "백업 상태 (COMPLETED, FAILED, IN_PROGRESS)")
      @RequestParam(value = "status", defaultValue = "COMPLETED") BackupStatus status);

  @Operation(summary = "데이터 백업 목록 조회", description = "데이터 백업 이력 목록을 조회합니다. 커서 기반 페이지네이션을 지원합니다.")
  @GetMapping
  ResponseEntity<BackupCursorPageResponse> getBackups(
      @RequestParam(required = false) String worker,
      @RequestParam(required = false) BackupStatus status,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam(required = false) LocalDateTime startedAtFrom,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam(required = false) LocalDateTime startedAtTo,
      @RequestParam(required = false) Long idAfter,
      @RequestParam(required = false) String cursor,
      @RequestParam(required = false, defaultValue = "10") Integer size,
      @RequestParam(required = false, defaultValue = "startedAt") String sortField,
      @RequestParam(required = false, defaultValue = "DESC") String sortDirection);
}