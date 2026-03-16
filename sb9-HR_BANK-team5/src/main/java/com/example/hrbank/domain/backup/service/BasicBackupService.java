package com.example.hrbank.domain.backup.service;

import com.example.hrbank.domain.backup.dto.request.BackupSearchRequest;
import com.example.hrbank.domain.backup.dto.response.BackupCursorPageResponse;
import com.example.hrbank.domain.backup.dto.response.BackupResponse;
import com.example.hrbank.domain.backup.entity.BackupHistory;
import com.example.hrbank.domain.backup.entity.BackupStatus;
import com.example.hrbank.domain.backup.mapper.BackupMapper;
import com.example.hrbank.domain.backup.repository.BackupRepository;
import com.example.hrbank.domain.binarycontent.dto.data.BinaryContentDto;
import com.example.hrbank.domain.binarycontent.dto.request.BinaryContentRequest;
import com.example.hrbank.domain.binarycontent.service.BinaryContentService;
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
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicBackupService implements BackupService {

  private final BackupRepository backupRepository;
  private final EmployeeRepository employeeRepository;
  private final ChangeLogRepository changeLogRepository;
  private final BackupMapper backupMapper;
  private final BinaryContentService binaryContentService;

  @Override
  @Transactional
  public void runBatchBackup() {
    runBackup("system");
  }

  @Override
  @Transactional
  public BackupResponse runBackup(String worker) {
    if (backupRepository.existsByStatus(BackupStatus.IN_PROGRESS)) {
      throw new BusinessException(ErrorCode.BACKUP_ALREADY_IN_PROGRESS);
    }

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
      Long errorLogId = saveErrorLog(e);
      history.fail(errorLogId);
    }

    return backupMapper.toResponse(history);
  }

  @Override
  @Transactional(readOnly = true)
  public BackupCursorPageResponse getBackupList(BackupSearchRequest request) {
    int pageSize = request.size();
    List<BackupHistory> entities = backupRepository.searchBackups(request);

    boolean hasNext = entities.size() > pageSize;
    List<BackupHistory> content = hasNext ? entities.subList(0, pageSize) : entities;

    Long nextIdAfter = (hasNext && !content.isEmpty())
        ? content.get(content.size() - 1).getId() : null;

    long totalElements = backupRepository.countBackups(request);

    return backupMapper.toPageResponse(
        content,
        nextIdAfter != null ? nextIdAfter.toString() : null,
        nextIdAfter,
        pageSize,
        totalElements,
        hasNext
    );
  }

  @Override
  @Transactional(readOnly = true)
  public BackupResponse getLatestBackup(BackupStatus status) {
    return backupRepository.findFirstByStatusOrderByStartedAtDesc(status)
        .map(backupMapper::toResponse)
        .orElseThrow(() -> new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));
  }

  private Long performCsvBackup() throws Exception {
    Path tempFile = Files.createTempFile("backup_", ".csv");
    try (Stream<Employee> employeeStream = employeeRepository.streamAllBy();
        PrintWriter writer = new PrintWriter(Files.newBufferedWriter(tempFile))) {
      writer.println("ID,Name,Email,EmployeeNumber,Department,Position,HireDate,Status");
      employeeStream.forEach(emp -> writer.printf("%d,%s,%s,%s,%s,%s,%s,%s%n",
          emp.getId(), emp.getName(), emp.getEmail(), emp.getEmployeeNumber(),
          emp.getDepartment() != null ? emp.getDepartment().getName() : "N/A",
          emp.getPosition(), emp.getHireDate(), emp.getStatus()));
      writer.flush();
    }
    BinaryContentRequest request = new BinaryContentRequest(
        tempFile.getFileName().toString(), "text/csv", Files.size(tempFile));
    BinaryContentDto savedFile = binaryContentService.save(request, tempFile);
    Files.deleteIfExists(tempFile);
    return savedFile.id();
  }

  private Long saveErrorLog(Exception e) {
    try {
      Path logFile = Files.createTempFile("error_", ".log");
      StringWriter sw = new StringWriter();
      e.printStackTrace(new PrintWriter(sw));
      Files.writeString(logFile, sw.toString());
      BinaryContentRequest request = new BinaryContentRequest(
          logFile.getFileName().toString(), "text/plain", Files.size(logFile));
      BinaryContentDto savedLog = binaryContentService.save(request, logFile);
      Files.deleteIfExists(logFile);
      return savedLog.id();
    } catch (Exception ex) {
      log.error("Failed to save error log file", ex);
      return null;
    }
  }
}