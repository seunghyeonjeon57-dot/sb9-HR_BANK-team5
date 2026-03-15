package com.example.hrbank.domain.employee.service.Impl;


import com.example.hrbank.domain.employee.dto.data.ChangeLogDetailDto;
import com.example.hrbank.domain.employee.dto.data.CursorPageResponseChangeLogDto;
import com.example.hrbank.domain.employee.dto.data.DiffDto;
import com.example.hrbank.domain.employee.dto.request.ChangeLogSearchRequest;
import com.example.hrbank.domain.employee.entity.ChangeLog;
import com.example.hrbank.domain.employee.entity.Employee;
import com.example.hrbank.domain.employee.entity.enums.ChannelType;
import com.example.hrbank.domain.employee.mapper.ChangeLogMapper;
import com.example.hrbank.domain.employee.repository.ChangeLogRepository;
import com.example.hrbank.domain.employee.repository.EmployeeRepository;
import com.example.hrbank.domain.employee.service.ChangeLogService;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ChangeLogServiceImpl implements ChangeLogService {
  private final ChangeLogRepository repository;
  private final ChangeLogMapper mapper;
  private final EmployeeRepository employeeRepository;
  @Override
  @Transactional
  public void createChannelLog(Employee employee, ChannelType type, String employeeNumber,
      List<DiffDto> diffs, String memo, String ipAddress) {
    ChangeLog log= ChangeLog.builder()
        .type(type)
        .employeeNumber(employeeNumber)
        .memo(memo)
        .ipAddress(ipAddress)
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
    List<ChangeLog> entities = repository.searchLogs(request);
    Long totalElements = repository.countSearchLogs(request);

    boolean hasNext = entities.size()>request.size();
    List<ChangeLog> subEntities = hasNext ? entities.subList(0,request.size()) : entities;

    return mapper.toCursorPageResponse(
        subEntities,
        request.type(),
        request.size(),
        totalElements,
        hasNext
    );

  }

  @Override
  public ChangeLogDetailDto getChangeLogDetail(Long id) {
    ChangeLog log = repository.findById(id).orElseThrow(()-> new EntityNotFoundException("존재하지 않는 id"));

    Employee employee= employeeRepository.findByEmployeeNumber(log.getEmployeeNumber())
        .orElseThrow(()->new EntityNotFoundException("직원 정보를 찾을 없습니다."));

    return mapper.toDetailDto(log,employee);
  }

  @Override
  public long countChangelogs(LocalDateTime fromDate, LocalDateTime toDate) {
    LocalDateTime start = (fromDate != null) ? fromDate : LocalDateTime.now().minusDays(7);
    LocalDateTime end = (toDate != null) ?  toDate : LocalDateTime.now();
    return repository.countChangeLogs(start,end);
  }
}
