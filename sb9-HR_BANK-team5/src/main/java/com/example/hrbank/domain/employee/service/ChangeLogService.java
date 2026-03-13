package com.example.hrbank.domain.employee.service;

import com.example.hrbank.domain.employee.dto.data.ChangeLogDetailDto;
import com.example.hrbank.domain.employee.dto.data.ChangeLogDto;
import com.example.hrbank.domain.employee.dto.data.CursorPageResponseChangeLogDto;
import com.example.hrbank.domain.employee.dto.data.DiffDto;
import com.example.hrbank.domain.employee.dto.request.EmployeeSearchRequest;
import com.example.hrbank.domain.employee.entity.Employee;
import com.example.hrbank.domain.employee.entity.enums.ChannelType;
import java.time.LocalDateTime;
import java.util.List;

public interface ChangeLogService {

  void createChannelLog(Employee employee,ChannelType type,String employeeNumber,
      List<DiffDto> diffs,String memo, String ipAddress);
  CursorPageResponseChangeLogDto getChangeLog(ChangeLogDto dto);
  ChangeLogDetailDto getChangeLogDetail(Long id);
  long countChangelogs(LocalDateTime fromDate,LocalDateTime toDate);

}
