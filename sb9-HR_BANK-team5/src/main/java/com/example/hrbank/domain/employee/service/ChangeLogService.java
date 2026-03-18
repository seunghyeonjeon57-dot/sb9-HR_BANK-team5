package com.example.hrbank.domain.employee.service;

import com.example.hrbank.domain.employee.dto.data.ChangeLogDetailDto;
import com.example.hrbank.domain.employee.dto.data.CursorPageResponseChangeLogDto;
import com.example.hrbank.domain.employee.dto.data.DiffDto;
import com.example.hrbank.domain.employee.dto.request.ChangeLogSearchRequest;
import com.example.hrbank.domain.employee.entity.Employee;
import com.example.hrbank.domain.employee.entity.enums.ChangeLogType;
import java.time.LocalDateTime;
import java.util.List;

public interface ChangeLogService {

  void createChannelLog(Employee employee, ChangeLogType type,String employeeNumber,
      List<DiffDto> diffs,String memo, String ipAddress);
  CursorPageResponseChangeLogDto getChangeLog(ChangeLogSearchRequest request);
  ChangeLogDetailDto getChangeLogDetail(Long id);
  long countChangelogs(LocalDateTime fromDate,LocalDateTime toDate);

}
