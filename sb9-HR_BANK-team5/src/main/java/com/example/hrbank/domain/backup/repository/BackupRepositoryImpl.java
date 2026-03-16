package com.example.hrbank.domain.backup.repository;

import com.example.hrbank.domain.backup.entity.BackupHistory;
import com.example.hrbank.domain.backup.entity.BackupStatus;
import com.example.hrbank.domain.backup.entity.QBackupHistory; // QueryDSL 생성 클래스
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class BackupRepositoryImpl implements BackupRepositoryCustom {

  private final JPAQueryFactory queryFactory;
  private final QBackupHistory backupHistory = QBackupHistory.backupHistory;

  @Override
  public List<BackupHistory> searchBackups(
      String worker,
      BackupStatus status,
      LocalDateTime fromAt,
      LocalDateTime toAt,
      Long idAfter,
      int size,
      String sortField,
      String sortDirection
  ) {
    return queryFactory
        .selectFrom(backupHistory)
        .where(
            containsWorker(worker),
            eqStatus(status),
            betweenStartedAt(fromAt, toAt),
            ltIdAfter(idAfter)
        )
        .orderBy(createOrderSpecifier(sortField, sortDirection))
        .limit(size + 1)
        .fetch();
  }

  @Override
  public long countBackups(String worker, BackupStatus status, LocalDateTime fromAt, LocalDateTime toAt) {
    Long count = queryFactory
        .select(backupHistory.count())
        .from(backupHistory)
        .where(
            containsWorker(worker),
            eqStatus(status),
            betweenStartedAt(fromAt, toAt)
        )
        .fetchOne();
    return count != null ? count : 0L;
  }


  private BooleanExpression containsWorker(String worker) {
    return StringUtils.hasText(worker) ? backupHistory.worker.contains(worker) : null;
  }

  private BooleanExpression eqStatus(BackupStatus status) {
    return status != null ? backupHistory.status.eq(status) : null;
  }

  private BooleanExpression betweenStartedAt(LocalDateTime fromAt, LocalDateTime toAt) {
    if (fromAt == null && toAt == null) return null;
    if (fromAt != null && toAt != null) return backupHistory.startedAt.between(fromAt, toAt);
    if (fromAt != null) return backupHistory.startedAt.goe(fromAt);
    return backupHistory.startedAt.loe(toAt);
  }

  private BooleanExpression ltIdAfter(Long idAfter) {
    return idAfter != null ? backupHistory.id.lt(idAfter) : null;
  }

  private OrderSpecifier<?> createOrderSpecifier(String sortField, String sortDirection) {
    boolean isAsc = "asc".equalsIgnoreCase(sortDirection);

    if ("endedAt".equals(sortField)) {
      return isAsc ? backupHistory.endedAt.asc() : backupHistory.endedAt.desc();
    }

    return isAsc ? backupHistory.startedAt.asc() : backupHistory.startedAt.desc();
  }
}