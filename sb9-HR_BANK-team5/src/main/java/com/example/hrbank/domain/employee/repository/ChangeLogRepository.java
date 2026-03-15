package com.example.hrbank.domain.employee.repository;

import com.example.hrbank.domain.employee.entity.ChangeLog;
import com.example.hrbank.domain.employee.repository.custom.ChangeLogRepositoryCustom;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeLogRepository extends JpaRepository<ChangeLog,Long>,
    ChangeLogRepositoryCustom {

  boolean existsByUpdatedAtAfter(LocalDateTime lastBackupTime);
}
