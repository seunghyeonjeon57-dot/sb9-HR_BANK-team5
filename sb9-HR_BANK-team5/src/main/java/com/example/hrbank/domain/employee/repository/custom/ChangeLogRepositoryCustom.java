package com.example.hrbank.domain.employee.repository.custom;

import com.example.hrbank.domain.employee.dto.data.ChangeLogDto;
import com.example.hrbank.domain.employee.dto.request.ChangeLogSearchRequest;
import com.example.hrbank.domain.employee.entity.ChangeLog;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeLogRepositoryCustom {
  List<ChangeLog> searchLogs(ChangeLogSearchRequest request);
  Long countSearchLogs(ChangeLogSearchRequest request);
  Long countChangeLogs(LocalDateTime fromDate, LocalDateTime toDate);

}
