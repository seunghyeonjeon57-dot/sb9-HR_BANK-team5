package com.example.hrbank.domain.employee.repository.custom;


import com.example.hrbank.domain.employee.dto.data.EmployeeDistributionDto;
import com.example.hrbank.domain.employee.dto.data.EmployeeEventCount;
import com.example.hrbank.domain.employee.dto.data.EmployeeTrendDto;
import com.example.hrbank.domain.employee.entity.enums.EmployeeStatus;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeStatsRepositoryCustom {
  List<EmployeeEventCount> getEventCounts(LocalDate from,LocalDate to);
  List<EmployeeDistributionDto> getEmployeeDistribution(String groupBy, EmployeeStatus status);
  long getEmployeeCount(EmployeeStatus status,LocalDate fromDate,LocalDate toDate);
  long countEmployeeBefore(LocalDate date);

}
