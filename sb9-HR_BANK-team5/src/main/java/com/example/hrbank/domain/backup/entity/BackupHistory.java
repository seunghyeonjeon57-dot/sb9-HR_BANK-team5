package com.example.hrbank.domain.backup.entity;

import com.example.hrbank.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BackupHistory extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String worker;

  @Column(nullable = false)
  private LocalDateTime startedAt; // 시작 시간

  private LocalDateTime endedAt; // 종료 시간

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private BackupStatus status;

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