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
import org.springframework.web.bind.annotation.RequestBody;


@Service
@RequiredArgsConstructor
public class EmployeeStatsServiceImpl implements EmployeeStatsService {
      private final EmployeeStatsRepository repository;

  @Override
  public List<EmployeeTrendDto> getEmployeeTrend(LocalDate from, LocalDate to) {
    long currentTotal= repository.countEmployeeBefore(from);
    List<EmployeeEventCount> events= repository.getEventCounts(from,to);

    Map<LocalDate,EmployeeEventCount> eventMap = events.stream()
        .collect(Collectors.toMap(EmployeeEventCount::date,e->e));
    List<EmployeeTrendDto> result = new ArrayList<>();
    for(LocalDate date = from; !date.isAfter(to); date=date.plusDays(1)){
      EmployeeEventCount event = eventMap.getOrDefault(date,new EmployeeEventCount(date,0L,0L));
      long change = event.joinCount()- event.quitCount();
      currentTotal+=change;
      double rate = (currentTotal ==0) ? 0.0 : (double) change/currentTotal * 100;
      result.add(new EmployeeTrendDto(
          date,
          currentTotal,
          change,
          Math.round(rate *10.0)/10.0
      ));
    }
    return result;
  }

  @Override
  public List<EmployeeDistributionDto> getEmployeeDistribution(String groupBy,
      EmployeeStatus status) {
    long totalCount = repository.getEmployeeCount(status,null,null);
    List<EmployeeDistributionDto> distribution = repository.getEmployeeDistribution(groupBy,status);
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
  public long getEmployeeCount(EmployeeStatus status, LocalDate fromDate, LocalDate toDate) {
    return repository.getEmployeeCount(status,fromDate,toDate);
  }
}
