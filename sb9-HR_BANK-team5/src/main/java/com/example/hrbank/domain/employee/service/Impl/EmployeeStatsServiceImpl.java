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
    String finalUnit = (unit != null && !unit.isEmpty()) ? unit.toLowerCase() : "month";
    LocalDate finalTo = (to != null) ? to : LocalDate.now();
    LocalDate finalFrom = from;
    if (finalFrom == null) {
      finalFrom = switch (finalUnit) {
        case "day" -> finalTo.minusDays(6);        // 일별: 최근 일주일 (오늘 포함 7일)
        case "week" -> finalTo.minusWeeks(6);      // 주별: 최근 7주 (이번 주 포함 7개)
        case "month" -> finalTo.minusMonths(11);   // 월별: 최근 12개월 (이번 달 포함 12개)
        case "quarter" -> finalTo.minusMonths(18); // 분기별: 최근 7분기 (이번 분기 포함 7개, 1분기=3달이므로 6*3=18개월 전)
        case "year" -> finalTo.minusYears(11);     // 연도별: 최근 12년 (올해 포함 12개)
        default -> finalTo.minusMonths(11);        // 오타 방어용 (기본 월별)
      };
    }
    long currentTotal = repository.totalEmployeeBefore(finalFrom);
    List<EmployeeEventCount> events = repository.totalEventCounts(finalFrom, finalTo);

    Map<LocalDate, EmployeeEventCount> eventMap = events.stream()
        .collect(Collectors.toMap(EmployeeEventCount::date, e -> e));
    List<EmployeeTrendDto> result = new ArrayList<>();

    // 반복문 수정: "딱 그날"이 아니라 "다음 구간 전까지"의 모든 이벤트를 긁어모읍니다.
    for (LocalDate date = finalFrom; !date.isAfter(finalTo); ) {
      LocalDate nextDate = getNextDate(date, finalUnit);

      // 해당 구간(date ~ nextDate 직전) 사이의 모든 입사/퇴사 합산
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
