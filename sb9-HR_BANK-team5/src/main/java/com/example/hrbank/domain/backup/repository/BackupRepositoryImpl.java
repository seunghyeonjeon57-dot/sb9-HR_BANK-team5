package com.example.hrbank.domain.backup.repository;

import com.example.hrbank.domain.backup.dto.request.BackupSearchRequest;
import com.example.hrbank.domain.backup.entity.BackupHistory;
import com.example.hrbank.domain.backup.entity.BackupStatus;
import com.example.hrbank.domain.backup.entity.QBackupHistory;
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
  public List<BackupHistory> searchBackups(BackupSearchRequest request) {
    return queryFactory
        .selectFrom(backupHistory)
        .where(
            containsWorker(request.worker()),
            eqStatus(request.status()),
            betweenStartedAt(request.startedAtFrom(), request.startedAtTo()),
            ltIdAfter(request.idAfter())
        )
        .orderBy(createOrderSpecifier(request.sortField(), request.sortDirection()))
        .limit(request.size() + 1)
        .fetch();
  }

  @Override
  public long countBackups(BackupSearchRequest request) {
    Long count = queryFactory
        .select(backupHistory.count())
        .from(backupHistory)
        .where(
            containsWorker(request.worker()),
            eqStatus(request.status()),
            betweenStartedAt(request.startedAtFrom(), request.startedAtTo())
        )
        .fetchOne();
    return count != null ? count : 0L;
  }

  // --- 아래 헬퍼 메서드들은 그대로 유지 (request에서 값을 받아 처리) ---
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