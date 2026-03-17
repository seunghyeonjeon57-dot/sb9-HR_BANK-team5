package com.example.hrbank.domain.employee.service.Impl;

import com.example.hrbank.domain.employee.dto.data.EmployeeDistributionDto;
import com.example.hrbank.domain.employee.dto.data.EmployeeEventCount;
import com.example.hrbank.domain.employee.dto.data.EmployeeTrendDto;
import com.example.hrbank.domain.employee.entity.enums.EmployeeStatus;
import com.example.hrbank.domain.employee.repository.EmployeeStatsRepository;
import com.example.hrbank.domain.employee.service.EmployeeStatsService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;


@Service
@RequiredArgsConstructor
public class EmployeeStatsServiceImpl implements EmployeeStatsService {
      private final EmployeeStatsRepository repository;
  @Transactional
  @Override
  public List<EmployeeTrendDto> getEmployeeTrend(LocalDate from, LocalDate to,String unit) {
    long currentTotal= repository.totalEmployeeBefore(from);
    List<EmployeeEventCount> events= repository.totalEventCounts(from,to);

    Map<LocalDate,EmployeeEventCount> eventMap = events.stream()
        .collect(Collectors.toMap(EmployeeEventCount::date,e->e));
    List<EmployeeTrendDto> result = new ArrayList<>();
    for(LocalDate date = from; !date.isAfter(to); date=getNextDate(date,unit)){
      EmployeeEventCount event = eventMap.getOrDefault(date,new EmployeeEventCount(date,0L,0L));
      long change = event.joinCount()- event.quitCount();
      long previousTotal = currentTotal;
      currentTotal += change;
      double rate = (previousTotal ==0) ? 0.0 : (double) change/previousTotal * 100;
      result.add(new EmployeeTrendDto(
          date,
          currentTotal,
          change,
          Math.round(rate *10.0)/10.0
      ));
    }
    return result;
  }
  @Transactional(readOnly = true)
  @Override
  public List<EmployeeDistributionDto> getEmployeeDistribution(String groupBy,
      EmployeeStatus status) {
    long totalCount = repository.totalEmployeeCount(status,null,null);
    List<EmployeeDistributionDto> distribution = repository.totalEmployeeDistribution(groupBy,status);
    if(totalCount ==0) return distribution;

    return distribution.stream()
        .map(dto->{
          double percentage = (double) dto.count()/totalCount*100;
          double roundedRate = Math.round(percentage*10.0)/10.0;
          return new EmployeeDistributionDto(
              dto.groupKey(),
              dto.count(),
              roundedRate
          );
        })
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public long getEmployeeCount(EmployeeStatus status, LocalDate fromDate, LocalDate toDate) {
    return repository.totalEmployeeCount(status,fromDate,toDate);
  }
  private LocalDate getNextDate(LocalDate date, String unit) {
    return switch (unit.toLowerCase()) {
      case "day" -> date.plusDays(1);
      case "week" -> date.plusWeeks(1);
      case "month" -> date.plusMonths(1);
      case "quarter" -> date.plusMonths(3);
      case "year" -> date.plusYears(1);
      default -> date.plusMonths(1);
    };
  }
}
