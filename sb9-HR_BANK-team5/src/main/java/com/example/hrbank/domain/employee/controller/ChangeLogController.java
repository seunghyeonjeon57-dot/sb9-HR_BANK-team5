package com.example.hrbank.domain.employee.controller;

import com.example.hrbank.domain.employee.controller.api.ChangeLogControllerApi;
import com.example.hrbank.domain.employee.dto.data.ChangeLogDetailDto;
import com.example.hrbank.domain.employee.dto.data.CursorPageResponseChangeLogDto;
import com.example.hrbank.domain.employee.dto.request.ChangeLogCountRequest;
import com.example.hrbank.domain.employee.dto.request.ChangeLogSearchRequest;
import com.example.hrbank.domain.employee.service.ChangeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/change-logs")
@RestController
@RequiredArgsConstructor
public class ChangeLogController implements ChangeLogControllerApi {
  private final ChangeLogService service;

  @Override
  @GetMapping
  public ResponseEntity<CursorPageResponseChangeLogDto> getChangeLogs(
      ChangeLogSearchRequest request) {
    return ResponseEntity.ok(service.getChangeLog(request));
  }
  @GetMapping("/{id}")
  @Override
  public ResponseEntity<ChangeLogDetailDto> getChangeLogsById(@PathVariable Long id) {
    return ResponseEntity.ok(service.getChangeLogDetail(id));
  }
  @GetMapping("/count")
  @Override
  public ResponseEntity<Long> getChangeLogsCount( ChangeLogCountRequest request) {
    return ResponseEntity.ok(service.countChangelogs(request.fromDate(),request.toDate()));
  }
}
