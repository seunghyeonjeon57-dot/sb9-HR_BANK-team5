package com.example.hrbank.domain.employee.service.Impl;


import com.example.hrbank.domain.employee.dto.data.ChangeLogDetailDto;
import com.example.hrbank.domain.employee.dto.data.ChangeLogDto;
import com.example.hrbank.domain.employee.dto.data.CursorPageResponseChangeLogDto;
import com.example.hrbank.domain.employee.dto.data.DiffDto;
import com.example.hrbank.domain.employee.dto.request.ChangeLogSearchRequest;
import com.example.hrbank.domain.employee.entity.ChangeLog;
import com.example.hrbank.domain.employee.entity.Employee;
import com.example.hrbank.domain.employee.entity.enums.ChannelType;
import com.example.hrbank.domain.employee.mapper.ChannelLogMapper;
import com.example.hrbank.domain.employee.repository.ChangeLogRepository;
import com.example.hrbank.domain.employee.service.ChangeLogService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ChannelLogServiceImpl implements ChangeLogService {
  private final ChangeLogRepository repository;
  private final ChannelLogMapper mapper;
  @Override
  @Transactional
  public void createChannelLog(Employee employee, ChannelType type, String employeeNumber,
      List<DiffDto> diffs, String memo, String ipAddress) {
    ChangeLog log= ChangeLog.builder()
        .type(type)
        .employeeNumber(employeeNumber)
        .memo(memo)
        .ipAddress(ipAddress)
        .at(LocalDateTime.now())
        .build();

    if(diffs!=null){
      for(DiffDto diff:diffs){
        log.addDiff(diff.propertyName(),diff.before(),diff.after());
      }

    }
    repository.save(log);
  }

  @Override
  @Transactional(readOnly = true)
  public CursorPageResponseChangeLogDto getChangeLog(ChangeLogSearchRequest request) {
    return repository.searchLogs()
  }

  @Override
  public ChangeLogDetailDto getChangeLogDetail(Long id) {
    return null;
  }

  @Override
  public long countChangelogs(LocalDateTime fromDate, LocalDateTime toDate) {
    return 0;
  }
}
