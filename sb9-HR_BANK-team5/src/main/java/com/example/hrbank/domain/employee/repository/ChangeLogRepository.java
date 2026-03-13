package com.example.hrbank.domain.employee.repository;

import com.example.hrbank.domain.employee.entity.ChangeLog;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangeLogRepository extends JpaRepository<ChangeLog,Long> {

  boolean existsByUpdatedAtAfter(LocalDateTime lastBackupTime);
}
