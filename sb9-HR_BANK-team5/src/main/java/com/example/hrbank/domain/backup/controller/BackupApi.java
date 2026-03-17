package com.example.hrbank.domain.backup.controller;

import com.example.hrbank.domain.backup.dto.request.BackupSearchRequest; // 추가
import com.example.hrbank.domain.backup.dto.response.BackupCursorPageResponse;
import com.example.hrbank.domain.backup.dto.response.BackupResponse;
import com.example.hrbank.domain.backup.entity.BackupStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.annotations.ParameterObject; // 추가
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "데이터 백업 관리", description = "데이터 백업 관리 API")
public interface BackupApi {

  @Operation(summary = "데이터 백업 생성", description = "데이터 백업을 생성합니다.")
  @PostMapping
  ResponseEntity<BackupResponse> createBackup(HttpServletRequest request);

  @Operation(summary = "최근 백업 정보 조회", description = "지정된 상태의 가장 최근 백업 정보를 조회합니다.")
  @GetMapping("/latest")
  ResponseEntity<BackupResponse> getLatestBackup(
      @RequestParam(value = "status", defaultValue = "COMPLETED") BackupStatus status);

  @Operation(summary = "데이터 백업 목록 조회", description = "DTO를 이용한 동적 검색 및 커서 페이징을 지원합니다.")
  @GetMapping
  ResponseEntity<BackupCursorPageResponse> getBackups(@ParameterObject BackupSearchRequest request);
}