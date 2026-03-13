package com.example.hrbank.domain.employee.repository.custom;

import com.example.hrbank.domain.employee.dto.data.ChangeLogDto;
import com.example.hrbank.domain.employee.dto.request.ChangeLogSearchRequest;
import com.example.hrbank.domain.employee.entity.ChangeLog;
import java.util.List;

public interface ChangeLogRepositoryCustom {
  List<ChangeLog> searchLogs(ChangeLogSearchRequest request);

}
