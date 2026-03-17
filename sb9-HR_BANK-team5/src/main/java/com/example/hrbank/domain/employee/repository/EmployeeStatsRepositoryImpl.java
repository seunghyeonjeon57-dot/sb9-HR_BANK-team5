package com.example.hrbank.domain.employee.repository;

import com.example.hrbank.domain.employee.dto.data.EmployeeDistributionDto;
import com.example.hrbank.domain.employee.dto.data.EmployeeEventCount;
import com.example.hrbank.domain.employee.entity.QEmployee;
import com.example.hrbank.domain.employee.entity.enums.EmployeeStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmployeeStatsRepositoryImpl implements EmployeeStatsRepositoryCustom {
  private final JPAQueryFactory factory;
  private final QEmployee employee=QEmployee.employee;

  @Override
  public List<EmployeeEventCount> totalEventCounts(LocalDate from, LocalDate to) {
    Map<LocalDate,Long> joinMap = factory.select(employee.hireDate,employee.count())
        .from(employee)
        .where(employee.hireDate.between(from,to))
        .groupBy(employee.hireDate)
        .fetch()
        .stream()
        .collect(Collectors.toMap(tuple->tuple.get(employee.hireDate),tuple->tuple.get(employee.count())));

    Map<LocalDate, Long> quitMap = factory
        .select(employee.resignationDate, employee.count())
        .from(employee)
        .where(employee.resignationDate.between(from, to))
        .groupBy(employee.resignationDate)
        .fetch()
        .stream()
        .collect(Collectors.toMap(tuple -> tuple.get(employee.resignationDate), tuple -> tuple.get(employee.count())));

    return mergeEvents(from,to,joinMap,quitMap);
  }

  @Override
  public List<EmployeeDistributionDto> totalEmployeeDistribution(String groupBy, EmployeeStatus status) {
    // 1. 기본값 및 소문자 처리
    String criteria = (groupBy == null || groupBy.isEmpty()) ? "department" : groupBy.toLowerCase();

    // 2. Expression 타입을 StringExpression으로 지정 (as 메서드 사용 가능해짐)
    com.querydsl.core.types.dsl.StringExpression groupPath;

    if ("department".equals(criteria)) {
      groupPath = employee.department.name.coalesce("미지정");
    } else if ("position".equals(criteria)) {
      groupPath = employee.position;
    } else {
      throw new IllegalArgumentException("지원하지 않는 그룹화 기준입니다.");
    }

    // 3. 타입 안정성을 위해 JPAQuery를 명시적으로 선언 (Tuple 에러 방지)
    var query = factory.select(Projections.constructor(EmployeeDistributionDto.class,
            groupPath, // 팩트: 여기서 .as() 안 써도 생성자 순서만 맞으면 들어갑니다.
            employee.count(),
            Expressions.asNumber(0.0)))
        .from(employee);

    if ("department".equals(criteria)) {
      query.leftJoin(employee.department);
    }

    // 4. fetch() 결과가 List<EmployeeDistributionDto>임을 보장함
    return query.where(statusEq(status))
        .groupBy(groupPath)
        .fetch();
  }

  @Override
  public long totalEmployeeCount(EmployeeStatus status, LocalDate fromDate, LocalDate toDate) {
    Long count = factory.select(employee.count())
        .from(employee)
        .where(statusEq(status),
            hireDateBetween(fromDate,toDate))
        .fetchOne();

    return count != null ?count :0L;
  }

  @Override
  public long totalEmployeeBefore(LocalDate date) {
    Long count=factory
        .select(employee.count())
        .from(employee)
        .where(
            employee.hireDate.before(date),
            employee.resignationDate.isNull()
                .or(employee.resignationDate.goe(date))
        )
        .fetchOne();
    return count !=null ? count:0L;
  }

  public List<EmployeeEventCount> mergeEvents(LocalDate from,LocalDate to,Map<LocalDate,Long> joinMap,Map<LocalDate,Long> quitMap){
    Set<LocalDate> allDates = new TreeSet<>(joinMap.keySet());
    allDates.addAll(quitMap.keySet());

    return allDates.stream()
        .map(date->new EmployeeEventCount(
            date,
            joinMap.getOrDefault(date,0L),
            quitMap.getOrDefault(date,0L)
        ))
        .collect(Collectors.toList());
  }
  public BooleanExpression statusEq(EmployeeStatus status){
    return status != null ? employee.status.eq(status) : null;
  }
  public BooleanExpression hireDateBetween(LocalDate from,LocalDate to){
    if(from == null || to== null)return null;
    return employee.hireDate.between(from,to);

  }



}



