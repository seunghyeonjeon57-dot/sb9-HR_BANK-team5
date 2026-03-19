package com.example.hrbank.domain.department.service;

import com.example.hrbank.domain.department.dto.data.DepartmentDto;
import com.example.hrbank.domain.department.dto.request.DepartmentCreateRequest;
import com.example.hrbank.domain.department.dto.request.DepartmentSearchRequest;
import com.example.hrbank.domain.department.dto.request.DepartmentUpdateRequest;
import com.example.hrbank.domain.department.dto.response.CursorPageResponseDepartmentDto;
import com.example.hrbank.domain.department.entity.Department;

public interface DepartmentService {

  DepartmentDto create(DepartmentCreateRequest departmentCreateRequest, Integer employeeCount);

  
  Department findEntityById(Long id);

  
  DepartmentDto find(Long id);

  
  CursorPageResponseDepartmentDto findAll(DepartmentSearchRequest departmentSearchRequest);

  DepartmentDto update(Long id, DepartmentUpdateRequest departmentUpdateRequest);

  void delete(Long id);
}
