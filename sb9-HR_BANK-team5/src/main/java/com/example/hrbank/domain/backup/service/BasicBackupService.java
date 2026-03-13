package com.example.hrbank.domain.backup.service;

import com.example.hrbank.domain.backup.dto.response.BackupResponse;
import com.example.hrbank.domain.backup.entity.BackupHistory;
import com.example.hrbank.domain.backup.entity.BackupStatus;
import com.example.hrbank.domain.backup.mapper.BackupMapper;
import com.example.hrbank.domain.backup.repository.BackupRepository;
import com.example.hrbank.domain.employee.entity.Employee;
import com.example.hrbank.domain.employee.repository.ChangeLogRepository;
import com.example.hrbank.domain.employee.repository.EmployeeRepository;
import com.example.hrbank.global.error.BusinessException;
import com.example.hrbank.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicBackupService implements BackupService {

  private final BackupRepository backupRepository;
  private final EmployeeRepository employeeRepository;
  private final ChangeLogRepository changeLogRepository;
  private final BackupMapper backupMapper;
  // 선우님의 파일 저장 서비스 받기
  // private final BinaryContentService binaryContentService;

  @Override
  @Transactional
  public void runBatchBackup() {
    runBackup("system");
  }

  @Override
  @Transactional
  public BackupResponse runBackup(String worker) {
    LocalDateTime lastBackupTime = backupRepository.findFirstByStatusOrderByStartedAtDesc(BackupStatus.COMPLETED)
        .map(BackupHistory::getStartedAt)
        .orElse(LocalDateTime.MIN);
    boolean needsBackup = changeLogRepository.existsByUpdatedAtAfter(lastBackupTime);

    if (!needsBackup) {
      BackupHistory skipHistory = BackupHistory.builder()
          .worker(worker)
          .startedAt(LocalDateTime.now())
          .status(BackupStatus.SKIPPED)
          .build();
      skipHistory.skip();
      return backupMapper.toResponse(backupRepository.save(skipHistory));
    }

    BackupHistory history = backupRepository.save(BackupHistory.builder()
        .worker(worker)
        .startedAt(LocalDateTime.now())
        .status(BackupStatus.IN_PROGRESS)
        .build());

    try {
      Long fileId = performCsvBackup();

      history.complete(fileId);

    } catch (Exception e) {
      log.error("Backup failed: ", e);
      history.fail(null);
    }

    return backupMapper.toResponse(history);
  }

  private Long performCsvBackup() throws Exception {
    Path tempFile = Files.createTempFile("backup_", ".csv");

    try (Stream<Employee> employeeStream = employeeRepository.streamAllBy();
        PrintWriter writer = new PrintWriter(Files.newBufferedWriter(tempFile))) {

      writer.println("ID,Name,Email,EmployeeNumber,Department,Position,HireDate,Status");

      employeeStream.forEach(emp -> {
        writer.printf("%d,%s,%s,%s,%s,%s,%s,%s%n",
            emp.getId(), emp.getName(), emp.getEmail(), emp.getEmployeeNumber(),
            emp.getDepartment() != null ? emp.getDepartment().getName() : "",
            emp.getPosition(), emp.getHireDate(), emp.getStatus());
      });
    }

    log.info("Temporary backup file created at: {}", tempFile.toAbsolutePath());
    return 1L;
  }

  @Override
  @Transactional(readOnly = true)
  public BackupResponse getLatestBackup(BackupStatus status) {
    return backupRepository.findFirstByStatusOrderByStartedAtDesc(status)
        .map(backupMapper::toResponse)
        .orElseThrow(() -> new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));
  }
}