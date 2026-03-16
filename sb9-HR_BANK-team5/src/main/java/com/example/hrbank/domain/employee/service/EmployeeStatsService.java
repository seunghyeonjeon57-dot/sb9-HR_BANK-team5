package com.example.hrbank.domain.employee.service;

import com.example.hrbank.domain.employee.dto.data.EmployeeDistributionDto;
import com.example.hrbank.domain.employee.dto.data.EmployeeTrendDto;
import com.example.hrbank.domain.employee.entity.enums.EmployeeStatus;
import java.time.LocalDate;
import java.util.List;

public interface EmployeeStatsService {
  List<EmployeeTrendDto> getEmployeeTrend(LocalDate from, LocalDate to,String unit);
  List<EmployeeDistributionDto> getEmployeeDistribution(String groupBy, EmployeeStatus status);
  long getEmployeeCount(EmployeeStatus status,LocalDate fromDate,LocalDate toDate);

}
