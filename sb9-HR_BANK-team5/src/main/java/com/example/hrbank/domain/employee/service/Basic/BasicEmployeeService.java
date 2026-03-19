package com.example.hrbank.domain.employee.service.Basic;

import com.example.hrbank.domain.binarycontent.dto.data.BinaryContentDto;
import com.example.hrbank.domain.binarycontent.dto.request.BinaryContentRequest;
import com.example.hrbank.domain.binarycontent.entity.BinaryContent;
import com.example.hrbank.domain.binarycontent.repository.BinaryContentRepository;
import com.example.hrbank.domain.binarycontent.service.BinaryContentService;
import com.example.hrbank.domain.department.entity.Department;
import com.example.hrbank.domain.department.repository.DepartmentRepository;
import com.example.hrbank.domain.employee.dto.data.CursorPageResponseEmployeeDto;
import com.example.hrbank.domain.employee.dto.data.EmployeeDto;
import com.example.hrbank.domain.employee.dto.request.EmployeeCreateRequest;
import com.example.hrbank.domain.employee.dto.request.EmployeeSearchRequest;
import com.example.hrbank.domain.employee.dto.request.EmployeeUpdateRequest;
import com.example.hrbank.domain.employee.entity.ChangeLog;
import com.example.hrbank.domain.employee.entity.Employee;
import com.example.hrbank.domain.employee.entity.enums.ChangeLogType;
import com.example.hrbank.domain.employee.entity.enums.EmployeeStatus;
import com.example.hrbank.domain.employee.mapper.EmployeeMapper;
import com.example.hrbank.domain.employee.repository.ChangeLogRepository;
import com.example.hrbank.domain.employee.repository.EmployeeRepository;
import com.example.hrbank.domain.employee.service.EmployeeService;
import com.example.hrbank.global.error.BusinessException;
import com.example.hrbank.global.error.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicEmployeeService implements EmployeeService {

  private final EmployeeMapper mapper;
  private final EmployeeRepository repository;
  private final ChangeLogRepository changeLogRepository;
  private final BinaryContentService binaryContentService;
  private final BinaryContentRepository binaryContentRepository;
  private final DepartmentRepository departmentRepository;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Transactional
  @Override
  public EmployeeDto createEmployee(EmployeeCreateRequest request, MultipartFile profile,String ipAddress) {
    BinaryContent profileEntity = uploadProfileImage(profile);
    if(repository.existsByEmail(request.email())){
      throw new IllegalArgumentException("이미 사용 중인 이메일이에요");
    }


    Department department = departmentRepository.findById(request.departmentId())
        .orElseThrow(() -> new NoSuchElementException("부서가 없습니다."));
    department.addEmployee();

    Employee employee = Employee.builder()
        .name(request.name())
        .email(request.email())
        .position(request.position())
        .hireDate(request.hireDate())
        .status(EmployeeStatus.ACTIVE)
        .department(department)
        .profileImage(profileEntity)
        .build();


    Employee savedEmployee = repository.save(employee);


    ChangeLog initLog = ChangeLog.builder()
        .type(ChangeLogType.CREATED)
        .employeeNumber(savedEmployee.getEmployeeNumber())
        .memo("신규 사원 등록: " + savedEmployee.getName() + " (" + department.getName() + ")")
        .ipAddress(ipAddress)
        .build();
    changeLogRepository.save(initLog);

    log.info("신규 사원 생성 및 로그 기록 완료: {}", savedEmployee.getEmployeeNumber());
    return mapper.toDto(savedEmployee);
  }

  @Transactional(readOnly = true)
  @Override
  public CursorPageResponseEmployeeDto searchEmployees(EmployeeSearchRequest request) {
    String lastValue = null;
    Long lastId = 0L;

    
    if (request.cursor() != null && !request.cursor().isBlank()) {
      try {
        byte[] decodedBytes = Base64.getDecoder().decode(request.cursor());
        Map<String, Object> map = objectMapper.readValue(new String(decodedBytes), Map.class);
        lastValue = map.get("v").toString();
        lastId = ((Number) map.get("id")).longValue();
      } catch (Exception e) {
        throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
      }
    }

    List<Employee> employees = repository.totalEmployee(request, lastValue, lastId);
    long totalElements = repository.totalCountEmployee(request);

    boolean hasNext = employees.size() > request.size();
    List<Employee> resultEmployees = hasNext ? employees.subList(0, request.size()) : employees;

    return mapper.toCursorPageResponse(
        resultEmployees,
        totalElements,
        request.size(),
        hasNext,
        request.sortField()
    );
  }

  @Transactional
  @Override
  public EmployeeDto updateEmployee(Long id, EmployeeUpdateRequest request, MultipartFile profile, String ipAddress) {
    Employee employee = repository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("사원을 찾을 수 없습니다."));

    if (!employee.getEmail().equals(request.email()) && repository.existsByEmail(request.email())) {
      throw new IllegalArgumentException("이미 사용 중인 이메일이예요");
    }

    BinaryContent newProfileImage = uploadProfileImage(profile);
    if (newProfileImage == null) newProfileImage = employee.getProfileImage();

    
    ChangeLog logEntity = ChangeLog.builder()
        .type(ChangeLogType.UPDATED)
        .employeeNumber(employee.getEmployeeNumber())
        .memo(request.memo())
        .ipAddress(ipAddress)
        .build();

    
    if (!employee.getName().equals(request.name())) logEntity.addDiff("name", employee.getName(), request.name());
    if (!employee.getEmail().equals(request.email())) logEntity.addDiff("email", employee.getEmail(), request.email());

    if (request.departmentId() != null && (employee.getDepartment() == null || !employee.getDepartment().getId().equals(request.departmentId()))) {
      Department newDept = departmentRepository.findById(request.departmentId())
          .orElseThrow(() -> new NoSuchElementException("이동할 부서가 없습니다."));
      if(employee.getDepartment()!=null){
        employee.getDepartment().removeEmployee();
      }
      newDept.addEmployee();
      String oldDeptName = employee.getDepartment() != null ? employee.getDepartment().getName() : "미지정";
      logEntity.addDiff("department", oldDeptName, newDept.getName());
      employee.changeDepartment(newDept);
    }

    employee.updateEmployee(request.name(), request.email(), request.position(), request.hireDate(), request.status(), newProfileImage);
    changeLogRepository.save(logEntity);

    return mapper.toDto(employee);
  }

  @Override
  @Transactional(readOnly = true)
  public EmployeeDto searchEmployeeById(Long id) {
    return repository.findById(id).map(mapper::toDto)
        .orElseThrow(() -> new NoSuchElementException("해당 사원이 없습니다."));
  }

  @Override
  @Transactional
  public void deleteEmployee(Long id, String ipAddress) {

    Employee employee = repository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("사원이 없습니다."));

    
    Department department = employee.getDepartment();
    if (employee.getStatus() == EmployeeStatus.ACTIVE && department != null) {
      department.removeEmployee(); 
      departmentRepository.save(department); 
    }

    
    ChangeLog logEntity = ChangeLog.builder()
        .type(ChangeLogType.DELETED)
        .employeeNumber(employee.getEmployeeNumber())
        .memo("직원 퇴사 처리 (부서 인원 차감)")
        .ipAddress(ipAddress)
        .build();
    changeLogRepository.save(logEntity);

    
    employee.resign();

    
    if (employee.getProfileImage() != null) {
      binaryContentService.delete(employee.getProfileImage().getId());
    }
  }

  private BinaryContent uploadProfileImage(MultipartFile profile) {
    if (profile == null || profile.isEmpty()) return null;
    Path tempPath = null;
    try {
      tempPath = Files.createTempFile("profile_", "_" + profile.getOriginalFilename());
      profile.transferTo(tempPath);
      BinaryContentDto savedDto = binaryContentService.save(
          new BinaryContentRequest(profile.getOriginalFilename(), profile.getContentType(), profile.getSize()), tempPath);
      return binaryContentRepository.findById(savedDto.id())
          .orElseThrow(() -> new NoSuchElementException("이미지 저장 실패"));
    } catch (IOException e) {
      throw new RuntimeException("프로필 처리 오류", e);
    } finally {
      if (tempPath != null) try { Files.deleteIfExists(tempPath); } catch (IOException ignored) {}
    }
  }
}