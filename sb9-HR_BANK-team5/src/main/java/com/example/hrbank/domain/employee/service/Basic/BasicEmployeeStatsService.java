package com.example.hrbank.domain.employee.service.Basic;

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



@Service
@RequiredArgsConstructor
public class BasicEmployeeStatsService implements EmployeeStatsService {

  private final EmployeeStatsRepository repository;

  @Transactional
  @Override
  public List<EmployeeTrendDto> getEmployeeTrend(LocalDate from, LocalDate to, String unit) {
    String finalUnit = (unit != null && !unit.isEmpty()) ? unit.toLowerCase() : "month";
    LocalDate finalTo = (to != null) ? to : LocalDate.now();
    LocalDate finalFrom = from;
    if (finalFrom == null) {
      finalFrom = switch (finalUnit) {
        case "day" -> finalTo.minusDays(6);
        case "week" -> finalTo.minusWeeks(6);
        case "month" -> finalTo.minusMonths(11);
        case "quarter" -> finalTo.minusMonths(18);
        case "year" -> finalTo.minusYears(11);
        default -> finalTo.minusMonths(11);
      };
    }
    long currentTotal = repository.totalEmployeeBefore(finalFrom);
    List<EmployeeEventCount> events = repository.totalEventCounts(finalFrom, finalTo);

    Map<LocalDate, EmployeeEventCount> eventMap = events.stream()
        .collect(Collectors.toMap(EmployeeEventCount::date, e -> e));
    List<EmployeeTrendDto> result = new ArrayList<>();

    for (LocalDate date = finalFrom; !date.isAfter(finalTo); ) {
      LocalDate nextDate = getNextDate(date, finalUnit);

      LocalDate currentLoopDate = date;
      long joinCount = events.stream()
          .filter(e -> !e.date().isBefore(currentLoopDate) && e.date().isBefore(nextDate))
          .mapToLong(EmployeeEventCount::joinCount).sum();
      long quitCount = events.stream()
          .filter(e -> !e.date().isBefore(currentLoopDate) && e.date().isBefore(nextDate))
          .mapToLong(EmployeeEventCount::quitCount).sum();

      long change = joinCount - quitCount;
      long previousTotal = currentTotal;
      currentTotal += change;
      double rate = (previousTotal == 0) ? 0.0 : (double) change / previousTotal * 100;

      result.add(new EmployeeTrendDto(
          date,
          currentTotal,
          change,
          Math.round(rate * 10.0) / 10.0
      ));
      date = nextDate;
    }
    return result;
  }

  @Transactional(readOnly = true)
  @Override
  public List<EmployeeDistributionDto> getEmployeeDistribution(String groupBy,
      EmployeeStatus status) {
    long totalCount = repository.totalEmployeeCount(status, null, null);
    List<EmployeeDistributionDto> distribution = repository.totalEmployeeDistribution(groupBy,
        status);
    if (totalCount == 0) {
      return distribution;
    }

    return distribution.stream()
        .map(dto -> {
          double percentage = (double) dto.count() / totalCount * 100;
          double roundedRate = Math.round(percentage * 10.0) / 10.0;
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
    return repository.totalEmployeeCount(status, fromDate, toDate);
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
