package com.example.hrbank.domain.employee.service.Impl;

import com.example.hrbank.domain.binarycontent.entity.BinaryContent;
import com.example.hrbank.domain.binarycontent.service.BinaryContentService;
import com.example.hrbank.domain.employee.dto.data.ChangeLogDto;
import com.example.hrbank.domain.employee.dto.data.CursorPageResponseEmployeeDto;
import com.example.hrbank.domain.employee.dto.data.EmployeeDto;
import com.example.hrbank.domain.employee.dto.request.EmployeeCreateRequest;
import com.example.hrbank.domain.employee.dto.request.EmployeeSearchRequest;
import com.example.hrbank.domain.employee.dto.request.EmployeeUpdateRequest;
import com.example.hrbank.domain.employee.entity.ChangeLog;
import com.example.hrbank.domain.employee.entity.Employee;
import com.example.hrbank.domain.employee.entity.enums.ChannelType;
import com.example.hrbank.domain.employee.entity.enums.EmployeeStatus;
import com.example.hrbank.domain.employee.mapper.EmployeeMapper;
import com.example.hrbank.domain.employee.repository.ChangeLogRepository;

import com.example.hrbank.domain.employee.repository.EmployeeRepository;
import com.example.hrbank.domain.employee.service.EmployeeService;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeMapper mapper;
  private final EmployeeRepository repository;
  private final ChangeLogRepository changeLogRepository;
  private final BinaryContentService service;

  @Transactional
  @Override
  public EmployeeDto create(EmployeeCreateRequest request, BinaryContent profile) {
    return null;
  }

  @Transactional(readOnly = true)
  @Override
  public CursorPageResponseEmployeeDto getEmployeePage(EmployeeSearchRequest request) {
    List<Employee> employees = repository.searchEmployee(request);
    long totalElements = repository.countEmployee(request);
    boolean hasNext = employees.size()>request.size();
    List<Employee> resultEmployees = hasNext ? employees.subList(0, request.size()):employees;
    return mapper.toCursorPageResponse(
        resultEmployees,
        totalElements,
        request.size(),
        hasNext
    );
  }
  @Transactional
  @Override
  public EmployeeDto updateEmployee(Long id, EmployeeUpdateRequest request) {
    Employee employee= repository.findById(id)
        .orElseThrow(()-> new NoSuchElementException("사원을 찾을 수 없습니다."));
    if(!employee.getEmail().equals(request.email())){
      if(repository.existsByEmail(request.email())){
        throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
      }
    }
    if(employee.getStatus()!= EmployeeStatus.RESIGNED && request.status().equals(EmployeeStatus.RESIGNED)){
      employee.resign();
    }

    ChangeLog log = ChangeLog.builder()
        .type(ChannelType.UPDATED)
        .employeeNumber(employee.getEmployeeNumber())
        .memo(request.memo())
        .ipAddress("192.168.0.1")
        .build();

    if (!employee.getName().equals(request.name())) {
      log.addDiff("name", employee.getName(), request.name());
    }
    if (!employee.getEmail().equals(request.email())) {
      log.addDiff("email", employee.getEmail(), request.email());
    }
    if (!employee.getPosition().equals(request.position())) {
      log.addDiff("position", employee.getPosition(), request.position());
    }
    employee.updateEmployee(request.name(),request.email(),request.position(),request.hireDate(),request.status());
    changeLogRepository.save(log);
    return mapper.toDto(employee);
  }

  @Override
  @Transactional(readOnly = true)
  public EmployeeDto findEmployeeById(Long id)
  {
    return repository.findWithDetailsById(id).map(mapper::toDto)
        .orElseThrow(()->new NoSuchElementException("해당 Id를 가진 사원이 없습니다."));
  }

  @Override
  @Transactional
  public void deleteEmployee(Long id) {
    Employee employee = repository.findById(id)
        .orElseThrow(()->new NoSuchElementException("사원이 없습니다."));

    ChangeLog log = ChangeLog.builder()
        .type(ChannelType.DELETED)
        .employeeNumber(employee.getEmployeeNumber())
        .memo("직원을 삭제합니다")
        .ipAddress("192.168.0.1")
        .build();
    changeLogRepository.save(log);
    if(employee.getProfileImage()!=null){
      service.delete(employee.getProfileImage().getId());
    }
    repository.delete(employee);
  }

}

