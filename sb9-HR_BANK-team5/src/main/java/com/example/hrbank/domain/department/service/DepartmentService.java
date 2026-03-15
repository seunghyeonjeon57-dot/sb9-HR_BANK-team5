package com.example.hrbank.domain.department.service;

import com.example.hrbank.domain.department.dto.data.DepartmentDto;
import com.example.hrbank.domain.department.dto.request.DepartmentCreateRequest;
import com.example.hrbank.domain.department.dto.request.DepartmentSearchRequest;
import com.example.hrbank.domain.department.dto.request.DepartmentUpdateRequest;
import com.example.hrbank.domain.department.dto.response.CursorPageResponseDepartmentDto;
import java.util.List;

public interface DepartmentService {

  DepartmentDto create(DepartmentCreateRequest departmentCreateRequest, Integer employeeCount);

  //"부서 목록 상세 조회"
  DepartmentDto find(Long id);

  //커서 이건 "부서 목록 조회"
  CursorPageResponseDepartmentDto findAll(DepartmentSearchRequest departmentSearchRequest);

  DepartmentDto update(Long id, DepartmentUpdateRequest departmentUpdateRequest);

  void delete(Long id);
}
