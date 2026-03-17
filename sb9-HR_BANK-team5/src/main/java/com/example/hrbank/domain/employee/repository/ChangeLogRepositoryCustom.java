package com.example.hrbank.domain.employee.repository;

import com.example.hrbank.domain.employee.dto.request.ChangeLogSearchRequest;
import com.example.hrbank.domain.employee.entity.ChangeLog;
import java.time.LocalDateTime;
import java.util.List;

public interface ChangeLogRepositoryCustom {
  List<ChangeLog> totalLogs(ChangeLogSearchRequest request);
  Long totalSearchLogs(ChangeLogSearchRequest request);
  Long totalChangeLogs(LocalDateTime fromDate, LocalDateTime toDate);

}
