package com.example.hrbank.domain.backup.repository;

import com.example.hrbank.domain.backup.entity.BackupHistory;
import com.example.hrbank.domain.backup.entity.BackupStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface BackupRepository extends JpaRepository<BackupHistory, Long>, BackupRepositoryCustom {


  boolean existsByStatus(BackupStatus status);

  Optional<BackupHistory> findFirstByStatusOrderByStartedAtDesc(BackupStatus status);
}