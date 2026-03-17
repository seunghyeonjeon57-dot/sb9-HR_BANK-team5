package com.example.hrbank.domain.employee.repository;


import com.example.hrbank.domain.employee.dto.data.EmployeeDistributionDto;
import com.example.hrbank.domain.employee.dto.data.EmployeeEventCount;
import com.example.hrbank.domain.employee.entity.enums.EmployeeStatus;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeStatsRepositoryCustom {
  List<EmployeeEventCount> totalEventCounts(LocalDate from,LocalDate to);
  List<EmployeeDistributionDto> totalEmployeeDistribution(String groupBy, EmployeeStatus status);
  long totalEmployeeCount(EmployeeStatus status,LocalDate fromDate,LocalDate toDate);
  long totalEmployeeBefore(LocalDate date);

}
