package com.example.hrbank.domain.employee.service.Impl;

import com.example.hrbank.domain.binarycontent.dto.data.BinaryContentDto;
import com.example.hrbank.domain.binarycontent.dto.request.BinaryContentRequest;
import com.example.hrbank.domain.binarycontent.entity.BinaryContent;
import com.example.hrbank.domain.binarycontent.repository.BinaryContentRepository;
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeMapper mapper;
  private final EmployeeRepository repository;
  private final ChangeLogRepository changeLogRepository;
  private final BinaryContentService service;
  private final BinaryContentRepository binaryContentRepository;

  @Transactional
  @Override
  public EmployeeDto createEmployee(EmployeeCreateRequest request, MultipartFile profile) {
    BinaryContent profileEntity = null;

    if (profile != null && !profile.isEmpty()) {
      Path tempPath = null;
      try {
        // 1. 팩트: 서버 디스크에 임시 파일 생성
        tempPath = Files.createTempFile("profile_", "_" + profile.getOriginalFilename());

        // 2. 팩트: 스트림 방식으로 내용 복사 (메모리 절약)
        profile.transferTo(tempPath);

        // 3. 선우님의 서비스 규격에 맞춰 호출 (DTO 생성 및 Path 전달)
        // ※ 선우님 서비스가 엔티티를 반환한다고 가정
        BinaryContentDto savedDto = service.save(
            new BinaryContentRequest(
                profile.getOriginalFilename(),
                profile.getContentType(),
                profile.getSize()
            ),
            tempPath
        );
        profileEntity = binaryContentRepository.findById(savedDto.id())
            .orElseThrow(()->new NoSuchElementException("저장된 이미지가 없습니다"));

      } catch (IOException e) {
        throw new RuntimeException("파일 처리 중 오류가 발생했습니다.", e);
      } finally {
        // 4. 팩트: 사용이 끝난 임시 파일은 반드시 삭제하여 디스크 누수 방지
        if (tempPath != null) {
          try { Files.deleteIfExists(tempPath); } catch (IOException ignored) {}
        }
      }
    }
    Employee  employee = Employee.builder()
        .name(request.name())
        .email(request.email())
        .position(request.position())
        .hireDate(request.hireDate())
        .status(EmployeeStatus.ACTIVE)
        .profileImage(profileEntity)
        .build();
    return mapper.toDto(repository.save(employee));
  }

  @Transactional(readOnly = true)
  @Override
  public CursorPageResponseEmployeeDto searchEmployees(EmployeeSearchRequest request) {
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
    BinaryContent newProfileImage = null;
    if (profile != null && !profile.isEmpty()) {
      Path tempPath = null;
      try {
        tempPath = Files.createTempFile("update_profile_", "_" + profile.getOriginalFilename());
        profile.transferTo(tempPath);

        // 1. 팩트: DTO로 결과 받기
        BinaryContentDto savedDto = service.save(
            new BinaryContentRequest(
                profile.getOriginalFilename(),
                profile.getContentType(),
                profile.getSize()
            ),
            tempPath
        );

        // 2. 팩트: ID로 엔티티 찾아와서 타입 맞추기
        newProfileImage = binaryContentRepository.findById(savedDto.id())
            .orElseThrow(() -> new NoSuchElementException("저장된 이미지를 찾을 수 없습니다."));

      } catch (IOException e) {
        throw new RuntimeException("프로필 이미지 업데이트 중 오류가 발생했습니다.", e);
      } finally {
        if (tempPath != null) {
          try { Files.deleteIfExists(tempPath); } catch (IOException ignored) {}
        }
      }
    } else {
      // 기존 이미지는 이미 엔티티 타입이므로 그대로 유지
      newProfileImage = employee.getProfileImage();
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
    employee.updateEmployee(request.name(),request.email(),request.position(),request.hireDate(),request.status(),newProfileImage);
    changeLogRepository.save(log);
    return mapper.toDto(employee);
  }

  @Override
  @Transactional(readOnly = true)
  public EmployeeDto searchEmployeeById(Long id)
  {
    return repository.findWithDetailsById(id).map(mapper::toDto)
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

}

