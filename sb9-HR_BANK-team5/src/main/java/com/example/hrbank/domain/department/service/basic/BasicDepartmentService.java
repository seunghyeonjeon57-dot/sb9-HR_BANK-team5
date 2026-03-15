package com.example.hrbank.domain.department.service.basic;

import com.example.hrbank.domain.department.dto.data.DepartmentDto;
import com.example.hrbank.domain.department.dto.request.DepartmentCreateRequest;
import com.example.hrbank.domain.department.dto.request.DepartmentSearchRequest;
import com.example.hrbank.domain.department.dto.request.DepartmentUpdateRequest;
import com.example.hrbank.domain.department.dto.response.CursorPageResponseDepartmentDto;
import com.example.hrbank.domain.department.entity.Department;
import com.example.hrbank.domain.department.mapper.DepartmentMapper;
import com.example.hrbank.domain.department.repository.DepartmentRepository;
import com.example.hrbank.domain.department.service.DepartmentService;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicDepartmentService implements DepartmentService {

  private final DepartmentRepository departmentRepository;
  private final DepartmentMapper departmentMapper;

  @Transactional
  @Override
  public DepartmentDto create(DepartmentCreateRequest departmentCreateRequest, Integer employeeCount) {
    String name = departmentCreateRequest.name();
    String description = departmentCreateRequest.description();
    LocalDate establishedDate = departmentCreateRequest.establishedDate();

    if(departmentRepository.existsByName(name))
      throw new IllegalArgumentException("Departmentname with email " + name + " already exists");

    Department department = new Department(name, description, establishedDate, employeeCount);
    departmentRepository.save(department);

    return departmentMapper.toDto(department);
  }

  //"부서 목록 상세 조회"
  @Override
  @Transactional(readOnly = true)
  public DepartmentDto find(Long id) {
    return departmentRepository.findById(id)
        .map(departmentMapper::toDto)
        .orElseThrow(() -> new NoSuchElementException("Department with id " + id + " not found"));
  }

  //커서 이건 "부서 목록 조회"
  @Override
  @Transactional(readOnly = true)
  public CursorPageResponseDepartmentDto findAll(DepartmentSearchRequest departmentSearchRequest) {
    List<Department> departments = List.of();

    return departmentMapper.toCursorPageDto(
        departments,
        0,
        0L,
        false
    );
  }

  @Override
  @Transactional
  public DepartmentDto update(Long id, DepartmentUpdateRequest departmentUpdateRequest) {
    return null;
  }

  @Override
  @Transactional
  public void delete(Long id) {

  }
}
