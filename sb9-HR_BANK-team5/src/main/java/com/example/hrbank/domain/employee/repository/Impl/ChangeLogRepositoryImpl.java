package com.example.hrbank.domain.employee.repository.Impl;

import com.example.hrbank.domain.employee.dto.data.ChangeLogDto;
import com.example.hrbank.domain.employee.dto.request.ChangeLogSearchRequest;
import com.example.hrbank.domain.employee.entity.ChangeLog;
import com.example.hrbank.domain.employee.entity.QChangeLog;
import com.example.hrbank.domain.employee.entity.enums.ChannelType;
import com.example.hrbank.domain.employee.repository.custom.ChangeLogRepositoryCustom;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class ChangeLogRepositoryImpl implements ChangeLogRepositoryCustom {

  private final JPAQueryFactory queryFactory;
  private final QChangeLog changeLog = QChangeLog.changeLog;

  @Override
  public List<ChangeLog> searchLogs(ChangeLogSearchRequest request) {
    return queryFactory
        .selectFrom(changeLog)
        .where(
            containsEmpNo(request.employeeNumber()),
            containsMemo(request.memo()),
            containsIp(request.ipAddress()),
            betweenAt(request.atFrom(), request.atTo()),
            eqType(request.type()),
            ltLastId(request.lastId())

        )
        .orderBy(createOrderSpecifier(request.sortField(), request.sortDirection()))
        .limit(request.size() + 1)
        .fetch();
  }

  @Override
  public Long countSearchLogs(ChangeLogSearchRequest request) {
    return queryFactory.select(changeLog.count())
        .from(changeLog)
        .where(
            containsEmpNo(request.employeeNumber()),
            containsMemo(request.memo()),
            containsIp(request.ipAddress()),
            betweenAt(request.atFrom(), request.atTo()),
            eqType(request.type())
        )
        .fetchOne();
  }

  private BooleanExpression containsEmpNo(String empNo) {

    return StringUtils.hasText(empNo) ? changeLog.employeeNumber.contains(empNo) : null;
  }

  private BooleanExpression containsMemo(String memo) {
    return StringUtils.hasText(memo) ? changeLog.memo.contains(memo) : null;
  }

  private BooleanExpression containsIp(String ip) {
    return StringUtils.hasText(ip) ? changeLog.ipAddress.contains(ip) : null;
  }

  private BooleanExpression eqType(ChannelType type) {
    return type != null ? changeLog.type.eq(type) : null;
  }

  private BooleanExpression betweenAt(LocalDateTime from, LocalDateTime to) {

    return (from != null && to != null) ? changeLog.createdAt.between(from, to) : null;
  }

  private BooleanExpression ltLastId(Long lastId) {

    return lastId != null ? changeLog.id.lt(lastId) : null;
  }




  @Override
  public Long countChangeLogs(LocalDateTime fromDate, LocalDateTime toDate) {
    LocalDateTime start = (fromDate != null) ? fromDate : LocalDateTime.now().minusDays(7);
    LocalDateTime end = (toDate != null) ? toDate : LocalDateTime.now();

    Long count = queryFactory.select(changeLog.count())
        .from(changeLog)
        .where(
            changeLog.createdAt.between(start, end)
        )
        .fetchOne();

    return count !=null ?count : 0L;
  }
  private OrderSpecifier<?> createOrderSpecifier(String sortField, String sortDirection) {
    boolean isAsc = "asc".equalsIgnoreCase(sortDirection);


    if ("at".equals(sortField)) {
      return isAsc ? changeLog.createdAt.asc() : changeLog.createdAt.desc();
    }

    if ("ipAddress".equals(sortField)) {
      return isAsc ? changeLog.ipAddress.asc() : changeLog.ipAddress.desc();
    }


    return changeLog.createdAt.desc();
  }

}
