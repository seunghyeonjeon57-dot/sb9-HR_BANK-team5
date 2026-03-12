package com.example.hrbank.domain.backup.entity;

import com.example.hrbank.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "backup_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BackupHistory extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "worker", nullable = false)
  private String worker;

  @Column(name = "started_at", nullable = false)
  private LocalDateTime startedAt;

  @Column(name = "ended_at")
  private LocalDateTime endedAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private BackupStatus status;

  @Column(name = "file_id")
  private Long fileId;

  public void complete(Long fileId) {
    this.status = BackupStatus.COMPLETED;
    this.endedAt = LocalDateTime.now();
    this.fileId = fileId;
  }

  public void fail(Long errorLogId) {
    this.status = BackupStatus.FAILED;
    this.endedAt = LocalDateTime.now();
    this.fileId = errorLogId;
  }

  public void skip() {
    this.status = BackupStatus.SKIPPED;
    this.endedAt = LocalDateTime.now();
  }
}