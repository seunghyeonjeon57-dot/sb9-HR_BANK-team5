package com.example.hrbank.domain.employee.service.Impl;

import com.example.hrbank.domain.binarycontent.dto.data.BinaryContentDto;
import com.example.hrbank.domain.binarycontent.dto.request.BinaryContentRequest;
import com.example.hrbank.domain.binarycontent.entity.BinaryContent;
import com.example.hrbank.domain.binarycontent.repository.BinaryContentRepository;
import com.example.hrbank.domain.binarycontent.service.BinaryContentService;
import com.example.hrbank.domain.department.entity.Department;
import com.example.hrbank.domain.department.repository.DepartmentRepository;
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeMapper mapper;
  private final EmployeeRepository repository;
  private final ChangeLogRepository changeLogRepository;
  private final BinaryContentService service;
  private final BinaryContentRepository binaryContentRepository;
  private final DepartmentRepository departmentRepository;

  @Transactional
  @Override
  public EmployeeDto createEmployee(EmployeeCreateRequest request, MultipartFile profile) {
    BinaryContent profileEntity = uploadProfileImage(profile);
    Department department = departmentRepository.findById(request.departmentId())
        .orElseThrow(()-> new NoSuchElementException("부서가 없습니다"));

    Employee  employee = Employee.builder()
        .name(request.name())
        .email(request.email())
        .position(request.position())
        .hireDate(request.hireDate())
        .status(EmployeeStatus.ACTIVE)
        .department(department)
        .profileImage(profileEntity)
        .build();

    Employee savedEmployee = repository.save(employee);
    log.info("신규 사원 생성 완료:{}",savedEmployee);
    return mapper.toDto(repository.save(employee));
  }

  @Transactional(readOnly = true)
  @Override
  public CursorPageResponseEmployeeDto searchEmployees(EmployeeSearchRequest request) {
    List<Employee> employees = repository.totalEmployee(request);
    long totalElements = repository.totalCountEmployee(request);
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
  public EmployeeDto updateEmployee(Long id, EmployeeUpdateRequest request, MultipartFile profile,String ipAddress) {
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
    BinaryContent newProfileImage =uploadProfileImage(profile);
    if(newProfileImage == null){
      newProfileImage=employee.getProfileImage();
    }


    ChangeLog log = ChangeLog.builder()
        .type(ChannelType.UPDATED)
        .employeeNumber(employee.getEmployeeNumber())
        .memo(request.memo())
        .ipAddress(ipAddress)
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

    String beforeDeptName = (employee.getDepartment() != null)
        ? employee.getDepartment().getName()
        : "미지정";
    if(request.departmentId()!=null) {
      boolean isChanged = (employee.getDepartment() ==null) || (!employee.getDepartment().getId().equals(request.departmentId()));
      if(isChanged){
        Department newDept  = departmentRepository.findById(request.departmentId())
            .orElseThrow(()->new NoSuchElementException("이동할 부서가 존재하지 않습니다."));
        log.addDiff("department",beforeDeptName,newDept.getName());
        employee.changeDepartment(newDept);

      }


    }
    employee.updateEmployee(request.name(),request.email(),request.position(),request.hireDate(),request.status(),newProfileImage);
    changeLogRepository.save(log);
    return mapper.toDto(employee);
  }

  @Override
  @Transactional(readOnly = true)
  public EmployeeDto searchEmployeeById(Long id)
  {
    return repository.findById(id).map(mapper::toDto)
        .orElseThrow(()->new NoSuchElementException("해당 Id를 가진 사원이 없습니다."));
  }

  @Override
  @Transactional
  public void deleteEmployee(Long id, String ipAddress) {
    Employee employee = repository.findById(id)
        .orElseThrow(()->new NoSuchElementException("사원이 없습니다."));



    ChangeLog log = ChangeLog.builder()
        .type(ChannelType.DELETED)
        .employeeNumber(employee.getEmployeeNumber())
        .memo("직원을 삭제합니다")
        .ipAddress(ipAddress)
        .build();
    changeLogRepository.save(log);
    if(employee.getProfileImage()!=null){
      service.delete(employee.getProfileImage().getId());
    }
    repository.delete(employee);
  }


  private BinaryContent uploadProfileImage(MultipartFile profile) {
    if (profile == null || profile.isEmpty()) return null;

    Path tempPath = null;
    try {
      tempPath = Files.createTempFile("profile_", "_" + profile.getOriginalFilename());
      profile.transferTo(tempPath);

      BinaryContentDto savedDto = service.save(
          new BinaryContentRequest(profile.getOriginalFilename(), profile.getContentType(), profile.getSize()),
          tempPath
      );

      return binaryContentRepository.findById(savedDto.id())
          .orElseThrow(() -> new NoSuchElementException("저장된 이미지가 없습니다."));
    } catch (IOException e) {
      throw new RuntimeException("프로필 이미지 처리 중 오류 발생", e);
    } finally {
      if (tempPath != null) {
        try { Files.deleteIfExists(tempPath); } catch (IOException ignored) {}
      }
    }
  }

}

